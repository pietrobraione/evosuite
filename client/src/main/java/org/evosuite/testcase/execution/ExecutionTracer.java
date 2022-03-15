/*
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.testcase.execution;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.evosuite.Properties;
import org.evosuite.TestGenerationContext;
import org.evosuite.Properties.Criterion;
import org.evosuite.coverage.dataflow.DefUsePool;
import org.evosuite.coverage.dataflow.Definition;
import org.evosuite.coverage.dataflow.Use;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoal;
import org.evosuite.coverage.seepep.SeepepTraceItem;
import org.evosuite.instrumentation.testability.BooleanHelper;
import org.evosuite.seeding.ConstantPoolManager;
import org.evosuite.utils.ArrayUtil;
import org.evosuite.utils.LoggingUtils;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This class collects information about chosen branches/paths at runtime
 * 
 * @author Gordon Fraser
 */
public class ExecutionTracer {

	private static final Logger logger = LoggerFactory.getLogger(ExecutionTracer.class);

	private static ExecutionTracer instance = null;

	/**
	 * We need to disable the execution tracer sometimes, e.g. when calling
	 * equals in the branch distance function
	 */
	private boolean disabled = true;

    /**
     * Flag that is used to kill threads that are stuck in endless loops
     */
	private boolean killSwitch = false;

	private int num_statements = 0;

	private ExecutionTrace trace;


	private static boolean checkCallerThread = true;

	/**
	 * If a thread of a test case survives for some reason (e.g. long call to
	 * external library), then we don't want its data in the current trace
	 */
	private static volatile Thread currentThread = null;

	/**
	 * <p>
	 * setThread
	 * </p>
	 * 
     * @param thread a {@link java.lang.Thread} object.
	 */
	public static void setThread(Thread thread) {
		currentThread = thread;
	}

	/**
	 * <p>
	 * disable
	 * </p>
	 */
	public static void disable() {
		ExecutionTracer tracer = ExecutionTracer.getExecutionTracer();
		tracer.disabled = true;
	}

	/**
	 * <p>
	 * enable
	 * </p>
	 */
	public static void enable() {
		ExecutionTracer tracer = ExecutionTracer.getExecutionTracer();
		tracer.disabled = false;
	}

	/**
	 * <p>
	 * isEnabled
	 * </p>
	 * 
	 * @return a boolean.
	 */
	public static boolean isEnabled() {
		ExecutionTracer tracer = ExecutionTracer.getExecutionTracer();
		return !tracer.disabled;
	}

	/**
	 * <p>
	 * Setter for the field <code>killSwitch</code>.
	 * </p>
	 * 
     * @param value a boolean.
	 */
	public static void setKillSwitch(boolean value) {
		ExecutionTracer tracer = ExecutionTracer.getExecutionTracer();
		tracer.killSwitch = value;
	}

	/**
	 * <p>
	 * Setter for the field <code>checkCallerThread</code>.
	 * </p>
	 * 
     * @param checkCallerThread a boolean.
	 */
	public static void setCheckCallerThread(boolean checkCallerThread) {
		ExecutionTracer.checkCallerThread = checkCallerThread;
	}

	/**
	 * <p>
	 * enable context instrumentation
	 * </p>
	 */
	public static void enableContext(){
		logger.info("enable context and trace instrumentation");
		ExecutionTraceImpl.enableContext();
	}
	
	/**
	 * <p>
	 * disable context instrumentation
	 * </p>
	 */
	public static void disableContext(){
		logger.info("disable context and trace instrumentation");
		ExecutionTraceImpl.disableContext();
	}
	
	/**
	 * <p>
	 * disableTraceCalls
	 * </p>
	 */
	public static void disableTraceCalls() {
		ExecutionTraceImpl.disableTraceCalls();
	}

	/**
	 * <p>
	 * enableTraceCalls
	 * </p>
	 */
	public static void enableTraceCalls() {
		ExecutionTraceImpl.enableTraceCalls();
	}

	public static boolean isTraceCallsEnabled() {
		return ExecutionTraceImpl.isTraceCallsEnabled();
	}

	public static void enableSeepepTracing() { /*SEEPEP: DAG coverage*/
		ExecutionTraceImpl.enableSeepepTracing();
	}

	/**
	 * <p>
	 * getExecutionTracer
	 * </p>
	 * 
	 * @return a {@link org.evosuite.testcase.execution.ExecutionTracer} object.
	 */
	public static ExecutionTracer getExecutionTracer() {
		if (instance == null) {
			instance = new ExecutionTracer();
		}
		return instance;
	}

	/**
	 * Reset for new execution
	 */
	public void clear() {
		if (traceEventListener != null) { //SUSHI: Aiding path conditions
			trace = traceEventListener;
		} else
		trace = new ExecutionTraceProxy();
		BooleanHelper.clearStack();
		num_statements = 0;
	}

	/**
	 * Obviously more than one thread is executing during the creation of
	 * concurrent TestCases. #TODO steenbuck we should test if
	 * Thread.currentThread() is in the set of currently executing threads
	 * 
	 * @return
	 */
	public static boolean isThreadNeqCurrentThread() {
		if (!checkCallerThread) {
			return false;
		}
		if (currentThread == null) {
			logger.error("CurrentThread has not been set!");
			Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
			for (Thread t : map.keySet()) {
				String msg = "Thread: " + t+"\n";
				for (StackTraceElement e : map.get(t)) {
					msg += " -> " + e + "\n";
				}
				logger.error(msg);
			}
			currentThread = Thread.currentThread();
		}
		return Thread.currentThread() != currentThread;
	}

	/**
	 * Return trace of current execution
	 * 
	 * @return a {@link org.evosuite.testcase.execution.ExecutionTrace} object.
	 */
	public ExecutionTrace getTrace() {
		trace.finishCalls();
		return trace;

		// ExecutionTrace copy = trace.clone();
		// // copy.finishCalls();
		// return copy;
	}

	/**
	 * Return the last explicitly thrown exception
	 * 
	 * @return a {@link java.lang.Throwable} object.
	 */
	public Throwable getLastException() {
		return trace.getExplicitException();
	}

	/**
	 * Called by instrumented code whenever a new method is called
	 * 
     * @param classname  a {@link java.lang.String} object.
     * @param methodname a {@link java.lang.String} object.
     * @param caller     a {@link java.lang.Object} object.
     * @throws org.evosuite.testcase.execution.TestCaseExecutor$TimeoutExceeded if any.
	 */
	public static void enteredMethod(String classname, String methodname, Object caller)
	        throws TestCaseExecutor.TimeoutExceeded {
		ExecutionTracer tracer = getExecutionTracer();

		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		//logger.trace("Entering method " + classname + "." + methodname);
		tracer.trace.enteredMethod(classname, methodname, caller);
	}

	/**
	 * Called by instrumented code whenever a return values is produced
	 * 
     * @param value      a int.
     * @param className  a {@link java.lang.String} object.
     * @param methodName a {@link java.lang.String} object.
	 */
	public static void returnValue(int value, String className, String methodName) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		//logger.trace("Return value: " + value);
		tracer.trace.returnValue(className, methodName, value);
	}

	/**
	 * Called by instrumented code whenever a return values is produced
	 * 
     * @param value      a {@link java.lang.Object} object.
     * @param className  a {@link java.lang.String} object.
     * @param methodName a {@link java.lang.String} object.
	 */
	public static void returnValue(Object value, String className, String methodName) {
		if (isThreadNeqCurrentThread())
			return;

		if (!ExecutionTracer.isEnabled())
			return;

		if (value == null) {
			returnValue(0, className, methodName);
			return;
		}
		StringBuilder tmp = null;
		try {
			// setLineCoverageDeactivated(true);
			// logger.warn("Disabling tracer: returnValue");
			ExecutionTracer.disable();
			tmp = new StringBuilder(value.toString());
		} catch (Throwable t) {
			return;
		} finally {
			ExecutionTracer.enable();
		}
		int index = 0;
		int position = 0;
		boolean found = false;
		boolean deleteAddresses = true;
		char c = ' ';
		// quite fast method to detect memory addresses in Strings.
		while ((position = tmp.indexOf("@", index)) > 0) {
			for (index = position + 1; index < position + 17 && index < tmp.length(); index++) {
				c = tmp.charAt(index);
				if ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'f')
				        || (c >= 'A' && c <= 'F')) {
					found = true;
				} else {
					break;
				}
			}
			if (deleteAddresses && found) {
				tmp.delete(position + 1, index);
			}
		}

		returnValue(tmp.toString().hashCode(), className, methodName);
	}

	/**
	 * Called by instrumented code whenever a method is left
	 * 
     * @param classname  a {@link java.lang.String} object.
     * @param methodname a {@link java.lang.String} object.
	 */
	public static void leftMethod(String classname, String methodname) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		tracer.trace.exitMethod(classname, methodname);
		// logger.trace("Left method " + classname + "." + methodname);
	}

	/**
	 * Called by the instrumented code each time a new source line is executed
	 */
	public static void checkTimeout() {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (tracer.killSwitch) {
			// logger.info("Raising TimeoutException as kill switch is active - passedLine");
			if(!isInStaticInit())
				throw new TestCaseExecutor.TimeoutExceeded();
		}
	}
	
	private static boolean isInStaticInit() {
		for(StackTraceElement elem : Thread.currentThread().getStackTrace()) {
			if(elem.getMethodName().equals("<clinit>"))
				return true;
		}
		return false;
	}

	/**
	 * Called by the instrumented code each time a new source line is executed
	 * 
     * @param line       a int.
     * @param className  a {@link java.lang.String} object.
     * @param methodName a {@link java.lang.String} object.
	 */
	public static void passedLine(String className, String methodName, int line) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		tracer.trace.linePassed(className, methodName, line);
	}

	/**
	 * Called by the instrumented code each time a new branch is taken
	 * 
     * @param val         a int.
     * @param opcode      a int.
     * @param branch      a int.
     * @param bytecode_id a int.
	 */
	public static void passedBranch(int val, int opcode, int branch, int bytecode_id) {

		ExecutionTracer tracer = getExecutionTracer();
		// logger.info("passedBranch val="+val+", opcode="+opcode+", branch="+branch+", bytecode_id="+bytecode_id);
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		ConstantPoolManager.getInstance().addDynamicConstant(val);

		// logger.trace("Called passedBranch1 with opcode "+AbstractVisitor.OPCODES[opcode]+" and val "+val+" in branch "+branch);
		double distance_true = 0.0;
		double distance_false = 0.0;
		switch (opcode) {
		case Opcodes.IFEQ:
			distance_true = Math.abs((double) val); // The greater abs is, the
			// further away from 0
			distance_false = distance_true == 0 ? 1.0 : 0.0; // Anything but 0
			// is good
			break;
		case Opcodes.IFNE:
			distance_false = Math.abs((double) val); // The greater abs is, the
			// further away from 0
			distance_true = distance_false == 0 ? 1.0 : 0.0; // Anything but 0
			// leads to NE
			break;
		case Opcodes.IFLT:
			distance_true = val >= 0 ? val + 1.0 : 0.0; // The greater, the
			// further away from < 0
			distance_false = val < 0 ? 0.0 - val + 1.0 : 0.0; // The smaller,
			// the further
			// away from < 0
			break;
		case Opcodes.IFGT:
			distance_true = val <= 0 ? 0.0 - val + 1.0 : 0.0;
			distance_false = val > 0 ? val + 1.0 : 0.0;
			break;
		case Opcodes.IFGE:
			distance_true = val < 0 ? 0.0 - val + 1.0 : 0.0;
			distance_false = val >= 0 ? val + 1.0 : 0.0;
			break;
		case Opcodes.IFLE:
			distance_true = val > 0 ? val + 1.0 : 0.0; // The greater, the
			// further away from < 0
			distance_false = val <= 0 ? 0.0 - val + 1.0 : 0.0; // The smaller,
			// the further
			// away from < 0
			break;
		default:
			logger.error("Unknown opcode: " + opcode);

		}
		// logger.trace("1 Branch distance true : " + distance_true);
		// logger.trace("1 Branch distance false: " + distance_false);

		// Add current branch to control trace
		tracer.trace.branchPassed(branch, bytecode_id, distance_true, distance_false);
	}

	public static void passedPutStatic(String classNameWithDots, String fieldName) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();
		
		tracer.trace.putStaticPassed(classNameWithDots, fieldName);
	}

	
	/**
	 * This method is added in the transformed bytecode
	 *
	 * @param className
	 */
	public static void exitClassInit(String className) {
		
		final String classNameWithDots = className.replace('/', '.');

		ExecutionTracer tracer = getExecutionTracer();
//		if (tracer.disabled)
//			return;
//
//		if (isThreadNeqCurrentThread())
//			return;
//
//		checkTimeout();
		
		tracer.trace.classInitialized(classNameWithDots);
		
	}
	
	/**
	 * @param classNameWithDots
	 * @param fieldName
	 */
	public static void passedGetStatic(String classNameWithDots, String fieldName) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		tracer.trace.getStaticPassed(classNameWithDots, fieldName);
	}


	/**
	 * Called by the instrumented code each time a new branch is taken
	 * 
     * @param val1        a int.
     * @param val2        a int.
     * @param opcode      a int.
     * @param branch      a int.
     * @param bytecode_id a int.
	 */
	public static void passedBranch(int val1, int val2, int opcode, int branch,
	        int bytecode_id) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();
		
		ConstantPoolManager.getInstance().addDynamicConstant(val1);
		ConstantPoolManager.getInstance().addDynamicConstant(val2);

		/* logger.trace("Called passedBranch2 with opcode "
		        + AbstractVisitor.OPCODES[opcode] + ", val1=" + val1 + ", val2=" + val2
		        + " in branch " + branch); */
		double distance_true = 0;
		double distance_false = 0;
		switch (opcode) {
		// Problem is that the JVM is a stack machine
		// x < 5 gets compiled to a val2 > val1,
		// because operators are on the stack in reverse order
		case Opcodes.IF_ICMPEQ:
			// The greater the difference, the further away
			distance_true = Math.abs((double) val1 - (double) val2);
			// Anything but 0 is good
			distance_false = distance_true == 0 ? 1.0 : 0.0;
			break;
		case Opcodes.IF_ICMPNE:
			// The greater abs is, the further away from 0
			distance_false = Math.abs((double) val1 - (double) val2);
			// Anything but 0 leads to NE
			distance_true = distance_false == 0 ? 1.0 : 0.0;
			break;
		case Opcodes.IF_ICMPLT:
			// val1 >= val2?
			distance_true = val1 >= val2 ? (double) val1 - (double) val2 + 1.0 : 0.0;
			distance_false = val1 < val2 ? (double) val2 - (double) val1 + 1.0 : 0.0;
			break;
		case Opcodes.IF_ICMPGE:
			// val1 < val2?
			distance_true = val1 < val2 ? (double) val2 - (double) val1 + 1.0 : 0.0;
			distance_false = val1 >= val2 ? (double) val1 - (double) val2 + 1.0 : 0.0;
			break;
		case Opcodes.IF_ICMPGT:
			// val1 <= val2?
			distance_true = val1 <= val2 ? (double) val2 - (double) val1 + 1.0 : 0.0;
			distance_false = val1 > val2 ? (double) val1 - (double) val2 + 1.0 : 0.0;
			break;
		case Opcodes.IF_ICMPLE:
			// val1 > val2?
			distance_true = val1 > val2 ? (double) val1 - (double) val2 + 1.0 : 0.0;
			distance_false = val1 <= val2 ? (double) val2 - (double) val1 + 1.0 : 0.0;
			break;
		default:
			logger.error("Unknown opcode: " + opcode);
		}
		// logger.trace("2 Branch distance true: " + distance_true);
		// logger.trace("2 Branch distance false: " + distance_false);

		// Add current branch to control trace
		tracer.trace.branchPassed(branch, bytecode_id, distance_true, distance_false);
		// tracer.trace.branchPassed(branch, distance_true, distance_false);

	}

	/**
	 * Called by the instrumented code each time a new branch is taken
	 * 
     * @param val1        a {@link java.lang.Object} object.
     * @param val2        a {@link java.lang.Object} object.
     * @param opcode      a int.
     * @param branch      a int.
     * @param bytecode_id a int.
	 */
	public static void passedBranch(Object val1, Object val2, int opcode, int branch,
	        int bytecode_id) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		double distance_true = 0;
		double distance_false = 0;
		// logger.warn("Disabling tracer: passedBranch with 2 Objects");

		switch (opcode) {
		case Opcodes.IF_ACMPEQ:
			distance_true = val1 == val2 ? 0.0 : 1.0;
			break;
		case Opcodes.IF_ACMPNE:
			distance_true = val1 != val2 ? 0.0 : 1.0;
			break;
		}

		distance_false = distance_true == 0 ? 1.0 : 0.0;

		// Add current branch to control trace
		tracer.trace.branchPassed(branch, bytecode_id, distance_true, distance_false);
	}

	/**
	 * Called by the instrumented code each time a new branch is taken
	 * 
     * @param val         a {@link java.lang.Object} object.
     * @param opcode      a int.
     * @param branch      a int.
     * @param bytecode_id a int.
	 */
	public static void passedBranch(Object val, int opcode, int branch, int bytecode_id) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		double distance_true = 0;
		double distance_false = 0;
		switch (opcode) {
		case Opcodes.IFNULL:
			distance_true = val == null ? 0.0 : 1.0;
			break;
		case Opcodes.IFNONNULL:
			distance_true = val == null ? 1.0 : 0.0;
			break;
		default:
			logger.error("Warning: encountered opcode " + opcode);
		}
		distance_false = distance_true == 0 ? 1.0 : 0.0;
		// enable();

		// logger.trace("Branch distance true: " + distance_true);
		// logger.trace("Branch distance false: " + distance_false);

		// Add current branch to control trace
		tracer.trace.branchPassed(branch, bytecode_id, distance_true, distance_false);
	}

	/**
	 * Called by instrumented code each time a variable gets written to (a
	 * Definition)
	 * 
     * @param caller a {@link java.lang.Object} object.
     * @param defID  a int.
	 */
	public static void passedDefinition(Object object, Object caller, int defID) {
		if (isThreadNeqCurrentThread())
			return;

		ExecutionTracer tracer = getExecutionTracer();
		if (!tracer.disabled)
			tracer.trace.definitionPassed(object, caller, defID);
	}

	/**
	 * Called by instrumented code each time a variable is read from (a Use)
	 * 
     * @param caller a {@link java.lang.Object} object.
     * @param useID  a int.
	 */
	public static void passedUse(Object object, Object caller, int useID) {

		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		tracer.trace.usePassed(object, caller, useID);
	}

	/**
	 * Called by instrumented code each time a field method call is passed
     * <p>
	 * Since it was not clear whether the field method call constitutes a
	 * definition or a use when the instrumentation was initially added this
	 * method will redirect the call accordingly
	 * 
	 * @param caller
	 * @param defuseId
	 */
	public static void passedFieldMethodCall(Object callee, Object caller, int defuseId) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		if (DefUsePool.isKnownAsDefinition(defuseId)) {
			Definition passedDef = DefUsePool.getDefinitionByDefUseId(defuseId);
			passedDefinition(callee, caller, passedDef.getDefId());
		} else if (DefUsePool.isKnownAsUse(defuseId)) {
			Use passedUse = DefUsePool.getUseByDefUseId(defuseId);
			passedUse(callee, caller, passedUse.getUseId());
		} else
			throw new EvosuiteError(
			        "instrumentation called passedFieldMethodCall with invalid defuseId: "
			                + defuseId + ", known IDs: " + DefUsePool.getDefUseCounter());
	}

	/**
	 * Called by instrumented code each time method is executed. This method
	 * receives the full set of parameters of the corresponding call
	 * 
	 * @param params
	 *            the parameters passed to the call
	 */
	public static void passedMethodCall(String className, String methodName, Object[] params) { /*SUSHI: Path condition fitness*/
		ExecutionTracer tracer = getExecutionTracer();

		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;
		
		checkTimeout();

		if (ArrayUtil.contains(Properties.CRITERION, Criterion.SEEPEP)) { /*SEEPEP: DAG coverage*/
			if (methodName.equals(Properties.SEEPEP_ENTRY_METHOD)) {
				if (tracer.trace.checkSetSeepepDone(true)) {
					throw new RuntimeException("stop");
				}
				SeepepTraceItem startMarker = SeepepTraceItem.makeTraceStartMarker();
				tracer.trace.passedSeepepItem(startMarker);
				//LoggingUtils.getEvoLogger().info("Passing start trace: {}", startMarker);
				for (int i = 0; i < params.length - 1; i++) {
					SeepepTraceItem seepepItem = SeepepTraceItem.makeInputParameter(i, params[i + 1]);
					tracer.trace.passedSeepepItem(seepepItem);
					//LoggingUtils.getEvoLogger().info("Passing input: {}", seepepItem);
				}
			} else if (methodName.equals("notifyActionPassed(Ljava/lang/String;Ljava/lang/Object;)V")) {
				SeepepTraceItem seepepItem = SeepepTraceItem.makeAction((String) params[0], params[1]);
				tracer.trace.passedSeepepItem(seepepItem);
				//LoggingUtils.getEvoLogger().info("Passing action:{}", seepepItem);
			} else if (methodName.equals("notifyTransformationPassed(Ljava/lang/String;I)V")) {
				SeepepTraceItem seepepItem = SeepepTraceItem.makeTransformation((String) params[0], (int) params[1]);
				tracer.trace.passedSeepepItem(seepepItem);
				//LoggingUtils.getEvoLogger().info("Passing transformation: {}", seepepItem);
			} else if (methodName.equals("notifyOuterTransformationPassed(Ljava/lang/String;I)V")) {
				List<SeepepTraceItem> itemsSoFar = tracer.trace.getTraversedSeepepItems();
				SeepepTraceItem itemToReplace = itemsSoFar.remove(itemsSoFar.size() - 1);
				SeepepTraceItem seepepItem = SeepepTraceItem.makeTransformation((String) params[0], (int) params[1]);
				tracer.trace.passedSeepepItem(seepepItem);
				//LoggingUtils.getEvoLogger().info("Passing outer transformation: {} (replaces {})", seepepItem, itemToReplace);
			}
		}
		
		if (!mustCheckPathConditionsForThisCall()) {
			return;
		}

		Map<String, List<PathConditionCoverageGoal>> classEvaluators = tracer.pathConditions.get(className);
		if (classEvaluators == null) 
			return; // no evaluator for this class

		List<PathConditionCoverageGoal> methodEvaluators = classEvaluators.get(methodName);
		if (methodEvaluators == null) 
			return; // no evaluator for this method
		
		ClassLoader cl = TestGenerationContext.getInstance().getClassLoaderForSUT();
		resetEvaluationBackbone(cl);
		
		for (PathConditionCoverageGoal goal : methodEvaluators) {
			Object evaluator = getEvaluatorForPathConditionGoal(goal, tracer, cl);
			Method evaluatorMethod = getEvaluatorMethod(evaluator, "test0", goal, className, methodName);
			double d = executeEvaluator(evaluator, evaluatorMethod, params, className, methodName);
			// Add path condition distance to control trace
			tracer.trace.passedPathCondition(goal.getPathConditionId(), d);
			//LoggingUtils.getEvoLogger().info("-- Evaluator on:{} = {}", goal, d );
		}
	}
	
	/**
	 * Called by instrumented code each time method is executed at the exit. This method
	 * receives both the return value and full set of parameters of the corresponding call
	 * 
	 * @param retVal
	 *            the return value of the call
	 * @param params
	 *            the parameters passed to the call
	 */
	public static void passedMethodExit(Object retVal, String className, String methodName, Object[] params) { /*SUSHI: Path condition fitness*/
		ExecutionTracer tracer = getExecutionTracer();

		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;
		
		if (!mustCheckPathConditionsForThisCall()) {
			return;
		}

		Map<String, List<PathConditionCoverageGoal>> classEvaluators = tracer.pathConditions.get(className);
		if (classEvaluators == null) 
			return; // no evaluator for this class

		List<PathConditionCoverageGoal> methodEvaluators = classEvaluators.get(methodName);
		if (methodEvaluators == null) 
			return; // no evaluator for this method
		
		ClassLoader cl = TestGenerationContext.getInstance().getClassLoaderForSUT();
		resetEvaluationBackbone(cl);

		for (PathConditionCoverageGoal goal : methodEvaluators) {
			Object evaluator = getEvaluatorForPathConditionGoal(goal, tracer, cl);
			Method evaluatorMethod = getEvaluatorMethod(evaluator, "test1", goal, className, methodName);
			
			Object [] paramValues = new Object[params.length + 1];
			paramValues[0] = retVal;
			for (int i = 0; i < params.length; i++) {
				paramValues[i + 1] = params[i];
			}
			double d = executeEvaluator(evaluator, evaluatorMethod, paramValues, className, methodName);

			// Add path condition distance to control trace
			tracer.trace.passedPostCondition(goal.getPathConditionId(), d);
			//LoggingUtils.getEvoLogger().info("-- Evaluator on:{} = {}", goal, d );
		}
	}

	private static boolean mustCheckPathConditionsForThisCall() {
		StackTraceElement[] strace = Thread.currentThread().getStackTrace();
		if (Properties.CHECK_PATH_CONDITIONS_ONLY_FOR_DIRECT_CALLS && 
				strace.length > 4 && 
				!strace[4].toString().startsWith("sun.reflect.")) {
			/*
			 * the stack trace includes for sure: 1) getStackTrace, 2) this method,
			 * 3) ExecutionTracer.passedMethod..., and 4) the method that executed the instrumentation.
			 * Then, if the 5th method belongs to sun.reflect.*, it means that
			 * the instrumented method was called directly from within the test case, otherwise
			 * it was reached indirectly call when the test case called another method.
			 */
			return false;
		} 
		return true;
	}

	private static void resetEvaluationBackbone(ClassLoader cl) {
		try {
			Class.forName("sushi.compile.path_condition_distance.CandidateBackbone", false, cl).
			getMethod("resetAndReuseUntilReset", null).invoke(null, null);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			LoggingUtils.getEvoLogger().info("FAILED ATTEMPT TO RESET BACKBONE");
		} catch (ClassNotFoundException e) {
			//This means that either Evaluators are designed not to use sushi-lib, or we have an higher level problem.
			//Thus, we do not log the problem to support the case in which indeed users do not care of sushi-lib
		}		
	}

	private static double executeEvaluator(Object evaluator, Method evaluatorMethod, Object[] paramValues, String targetClassName, String targetMethodName) {
		//execute the evaluator
		double d = Double.MAX_VALUE;
		try {
			d = (double) evaluatorMethod.invoke(evaluator, paramValues);
			//LoggingUtils.getEvoLogger().info("**computed d: " + d + ", pc = " + goal.getEvaluatorName());
		} catch (IllegalAccessException | IllegalArgumentException e) {
			//throw new EvosuiteError
			LoggingUtils.getEvoLogger().info("Cannot execute post condition evaluator: " + evaluatorMethod
			+ "\n\t path condition for method " + targetClassName + "." + targetMethodName
			+ "\n\t called on objects " + Arrays.toString(paramValues)
			+ "\n\t failed because of: " + e
					);
		} catch (InvocationTargetException e) {
			StackTraceElement[] st = e.getCause().getStackTrace();
			LoggingUtils.getEvoLogger().info("Exception thrown within post condition evaluator: " + evaluatorMethod
			+ "\n\t path condition for method " + targetClassName + "." + targetMethodName
			+ "\n\t called on objects " + Arrays.toString(paramValues)
			+ "\n\t failed because of: " + e.getCause()
			+ "\n\t stack trace is " + st.length + " items long: " + 
			(st.length <= 50 ? Arrays.toString(st) :
				Arrays.toString(Arrays.copyOfRange(st, 0, 25)) + 
				" ...... " +  Arrays.toString(Arrays.copyOfRange(st, st.length - 25, st.length))));
		} catch (Throwable e) {
			StackTraceElement[] st = e.getCause().getStackTrace();
			throw new EvosuiteError("Unexpected failure when executing evaluator: " + evaluatorMethod
			+ "\n\t path condition for method " + targetClassName + "." + targetMethodName
			+ "\n\t called on objects " + Arrays.toString(paramValues)
			+ "\n\t failed because of: " + e.getCause()
			+ "\n\t stack trace is " + st.length + " items long: " + 
			(st.length <= 50 ? Arrays.toString(st) :
				Arrays.toString(Arrays.copyOfRange(st, 0, 25)) + 
				" ...... " +  Arrays.toString(Arrays.copyOfRange(st, st.length - 25, st.length))));
		}
		return d;
	}

	private static Method getEvaluatorMethod(Object evaluator, String methodName, PathConditionCoverageGoal targetGoal, String targetClassName, String targetMethodName) {
		Method evaluatorMethod = null;
		try {
			Method[] methods = evaluator.getClass().getDeclaredMethods();
			for (Method m : methods) {
				if (m.getName().startsWith(methodName)) {
					evaluatorMethod = m;
					break;
				}
			}
		} catch (Throwable e) { 
			throw new EvosuiteError("Cannot execute path condition evaluator: " + targetGoal.getEvaluatorName() +
					"\n\t path condition for method " + targetClassName + "." + targetMethodName +
					"\n\t due to: " + e);
		}
		
		if (evaluatorMethod == null) {
			throw new EvosuiteError("Cannot execute path condition evaluator: " + targetGoal.getEvaluatorName() +
					"\n\t path condition for method " + targetClassName + "." + targetMethodName +
					"\n\t because there is no 'test0' method in the evaluator");
		}
		return evaluatorMethod;
	}

	private static Object getEvaluatorForPathConditionGoal(PathConditionCoverageGoal goal, ExecutionTracer tracer, ClassLoader cl) {
		Object evaluator = tracer.evaluatorCache.get(goal);
		if (evaluator == null) {
			try {
				Class<?> clazz = Class.forName(goal.getEvaluatorName(), true, cl);
				Constructor<?>[] cnstrs = clazz.getConstructors();
				for (Constructor<?> c : cnstrs) {
					Class<?>[] parTypes = c.getParameterTypes();
					if (parTypes.length >= 1 && parTypes[0] == ClassLoader.class) {
						evaluator = c.newInstance(cl);
						break;
					}
				}
				if (evaluator == null) {
					evaluator = clazz.newInstance();							
				}
				tracer.evaluatorCache.put(goal, evaluator);
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new EvosuiteError("Cannot instantiate path condition evaluator: " + goal.getEvaluatorName() +
						" because of: " + e + " ::: " + Arrays.toString(e.getStackTrace()));
			}
		}
		return evaluator;
	}


	private Map<String, Map<String, List<PathConditionCoverageGoal>>> pathConditions = new HashMap<String, Map<String, List<PathConditionCoverageGoal>>>(); //classname --> methodname --> PathCondWrapper /*SUSHI: Path condition fitness*/
	private Map<PathConditionCoverageGoal, Object> evaluatorCache = new HashMap<>();  /*SUSHI: Path condition fitness*/
	public static void addEvaluatorForPathCondition(PathConditionCoverageGoal g) { /*SUSHI: Path condition fitness*/
		ExecutionTracer tracer = getExecutionTracer();
		try {
			Object evaluator = null;
			Class<?> clazz = Class.forName(g.getEvaluatorName());
			boolean loadedAndAcceptClassloader = false;
			Constructor<?>[] cnstrs = clazz.getConstructors();
			for (Constructor<?> c : cnstrs) {
				Class<?>[] parTypes = c.getParameterTypes();
				if (parTypes.length >= 1 && parTypes[0] == ClassLoader.class) {
					evaluator = c.newInstance(new Object[] {null});
					loadedAndAcceptClassloader = true;
					break;
				}
			} 
			if (!loadedAndAcceptClassloader) {
				evaluator = clazz.newInstance();
			}
			try {
				Method toString = evaluator.getClass().getDeclaredMethod("toString", new Class[]{});
				String customDescription = (String) toString.invoke(evaluator, new Object[]{});
				g.setCustomDescription(customDescription);
			} catch (NoSuchMethodException | SecurityException e) {
				//no custom description could be loaded
			}
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			LoggingUtils.getEvoLogger().info("Cannot instantiate path condition evaluator: " + g.getEvaluatorName() +
					" because of: " + e);
			return;
		}
		Map<String, List<PathConditionCoverageGoal>> classEvaluators = tracer.pathConditions.get(g.getClassName());
		if (classEvaluators == null) {
			classEvaluators = new HashMap<String, List<PathConditionCoverageGoal>>();
			tracer.pathConditions.put(g.getClassName(), classEvaluators);
		}
		List<PathConditionCoverageGoal> methodEvaluators = classEvaluators.get(g.getMethodName());
		if (methodEvaluators == null) {
			methodEvaluators = new ArrayList<PathConditionCoverageGoal>();
			classEvaluators.put(g.getMethodName(), methodEvaluators);
		}
		methodEvaluators.add(g);
	}
	
	public static void logEvaluatorsForPathConditions() {
		ExecutionTracer tracer = getExecutionTracer();
		LoggingUtils.getEvoLogger().info("GOALS: " + tracer.pathConditions.toString());		
	}
	
	public static void removeEvaluatorForPathCondition(PathConditionCoverageGoal g) { /*SUSHI: Path condition fitness*/
		ExecutionTracer tracer = getExecutionTracer();
		Map<String, List<PathConditionCoverageGoal>> classEvaluators = tracer.pathConditions.get(g.getClassName());
		if (classEvaluators == null) return;
		List<PathConditionCoverageGoal> methodEvaluators = classEvaluators.get(g.getMethodName());
		if (methodEvaluators == null) return;
		methodEvaluators.remove(g);
		if (methodEvaluators.isEmpty()) {
			classEvaluators.remove(g.getMethodName());
			if (classEvaluators.isEmpty()) {
				tracer.pathConditions.remove(g.getClassName());
			}
		}
		tracer.evaluatorCache.remove(g);
		LoggingUtils.getEvoLogger().info("GOALS: " + tracer.pathConditions.toString());
	}
		
	/**
	 * <p>
	 * passedMutation
	 * </p>
	 * 
     * @param distance   a double.
     * @param mutationId a int.
	 */
	public static void passedMutation(double distance, int mutationId) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		tracer.trace.mutationPassed(mutationId, distance);
	}

	/**
	 * <p>
	 * exceptionThrown
	 * </p>
	 * 
     * @param exception  a {@link java.lang.Object} object.
     * @param className  a {@link java.lang.String} object.
     * @param methodName a {@link java.lang.String} object.
	 */
	public static void exceptionThrown(Object exception, String className,
	        String methodName) {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		tracer.trace.setExplicitException((Throwable) exception);

	}

	/**
	 * <p>
	 * statementExecuted
	 * </p>
	 */
	public static void statementExecuted() {
		ExecutionTracer tracer = getExecutionTracer();
		if (tracer.disabled)
			return;

		if (isThreadNeqCurrentThread())
			return;

		checkTimeout();

		tracer.num_statements++;
	}

	/**
	 * <p>
	 * getNumStatementsExecuted
	 * </p>
	 * 
	 * @return a int.
	 */
	public int getNumStatementsExecuted() {
		return num_statements;
	}

	private ExecutionTracer() {
		trace = new ExecutionTraceProxy();
	}

	private static ExecutionTrace traceEventListener = null;//SUSHI: Aiding path conditions
	public static void setTraceEventListener(ExecutionTrace executionTraceEventListener) {
		traceEventListener = executionTraceEventListener;
	}

}

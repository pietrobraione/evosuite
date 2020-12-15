package org.evosuite.ga.metaheuristics.mosa.jbse;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.evosuite.Properties;
import org.evosuite.coverage.branch.BranchCoverageTestFitness;
import org.evosuite.coverage.pathcondition.AidingPathConditionGoalFitness;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoal;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.execution.ExecutionTraceProxy;
import org.evosuite.testcase.execution.ExecutionTracer;
import org.evosuite.utils.LoggingUtils;

public class JBSEManager {

	/* private static JBSERunner _instance = null; //TODO: Set as ingleton
	public static JBSERunner _I() {
		if (_instance = null) {
			_instance = new JBSERunner();
		}
		return null;
	}*/
	
	public static AidingPathConditionGoalFitness[] computeAidingPathConditionGoal(BranchCoverageTestFitness branchF, TestChromosome tc)  {
		if (Properties.TMP_TEST_DIR == null) {
			LoggingUtils.getEvoLogger().info("[JBSE] CANNOT GENERATE AIDING PATH CONDITION SINCE TMP_TEST_DIR IS NOT SET");
			return null;
		}

		ExecutionTraceEventListener traceData = selectEntryMethodFromTestTrace(branchF, tc);
		MethodData entryMethodData = null;
		if (traceData.entryMethod != null) {
			String entryClassName = traceData.entryMethod.className;
			String entryMethodName = traceData.entryMethod.methodName.substring(0,  traceData.entryMethod.methodName.indexOf('('));
			String entryMethodDescr = traceData.entryMethod.methodName.substring(entryMethodName.length());
			entryMethodData = new MethodData(entryClassName, entryMethodName, entryMethodDescr);
		} else {
			return null;
		}
		/*String entryClassName = branchF.getClassName().replace('.', '/');
		String entryMethodName = branchF.getMethod().substring(0,  branchF.getMethod().indexOf('('));
		String entryMethodDescr = branchF.getMethod().substring(entryMethodName.length());
		MethodData entryMethodData = 
				new MethodData(entryClassName, entryMethodName, entryMethodDescr);
		*/
		String[] evaluators = JBSERunner.run(branchF.getBranch(), tc.getTestCase(), entryMethodData, traceData.entryMethodOccurrences, traceData.targetBranchOccurrences);
		
		if (evaluators != null) {
			AidingPathConditionGoalFitness[] ret = new AidingPathConditionGoalFitness[evaluators.length];
			for (int i = 0; i < evaluators.length; i++) {
				ret[i] = new AidingPathConditionGoalFitness(branchF, branchF.getFitness(tc),
					new PathConditionCoverageGoal(
							branchF.getBranch().getActualBranchId() * 10000 + i, 
							entryMethodData.getClassNameDotted(), 
							entryMethodData.getMethodName() + entryMethodData.getMethodDescriptor(), 
							evaluators[i]));
			}
			return ret;
		} else {			
			return null;
		}
	}
	
	private static ExecutionTraceEventListener selectEntryMethodFromTestTrace(BranchCoverageTestFitness targetBranch, TestChromosome tc) {
		double currentFitness = targetBranch.getFitness(tc);
		
		ExecutionTraceEventListener listener = new ExecutionTraceEventListener();
		ExecutionTracer.setTraceEventListener(listener);
		
		targetBranch.runTest(tc.getTestCase());				
		ExecutionTracer.setTraceEventListener(null);

		
		for (TraceEvent item : listener.stack) {
			LoggingUtils.getEvoLogger().info("[TRACE] {}", item);
		}
		LoggingUtils.getEvoLogger().info("With test case {}", tc.getTestCase());
			
		listener.extractTraceInfo(targetBranch.getBranch().getActualBranchId(), targetBranch.getValue(),
				currentFitness);
		
		return listener;	
	}
	
	private static class ExecutionTraceEventListener extends ExecutionTraceProxy {
		List<TraceEvent> stack = new LinkedList<>();

		int targetBranchOccurrences = -1;
		EnteredMethodEvent entryMethod = null;
		int entryMethodOccurrences = -1;

		@Override
		public void branchPassed(int branch, int bytecode_id, double true_distance, double false_distance) {
			stack.add(new BranchPassedEvent(branch, bytecode_id, true_distance, false_distance));
			super.branchPassed(branch, bytecode_id, true_distance, false_distance);
		}

		@Override
		public void enteredMethod(String className, String methodName, Object caller) {
			stack.add(new EnteredMethodEvent(className, methodName, caller));
			super.enteredMethod(className, methodName, caller);
		}

		@Override
		public void exitMethod(String classname, String methodname) {
			stack.add(new ExitMethodEvent(classname, methodname));
			super.exitMethod(classname, methodname);
		}		
		
		public void extractTraceInfo(int branchId, boolean branchIsTrueSide, double currentFitness) {
			int targetBranchIndex = findTargetBranch(branchId, branchIsTrueSide, currentFitness);			

			if (targetBranchIndex < 0) {
				LoggingUtils.getEvoLogger().info("[DEBUG] branch id={}, fitness={} not found in TRACE", branchId, currentFitness);
				return;
			}

			//LoggingUtils.getEvoLogger().info("[DEBUG] branch id={}, fitness={} occurs at index {} -- {}", branchId, currentFitness, targetBranchIndex, stack.get(targetBranchIndex));

			targetBranchOccurrences = 1;			

			int entryMethodIndex = identifyEntryMethodAndCountBranchOccurrences(targetBranchIndex);
						

			if (entryMethodIndex == -1) {
				LoggingUtils.getEvoLogger().info("[DEBUG] entry method not found in TRACE");
				return;
			}


			entryMethod = (EnteredMethodEvent) stack.get(entryMethodIndex);
			entryMethodOccurrences = 1;
			
			LoggingUtils.getEvoLogger().info("[JBSE] Entry method occurs at index {} ({}) and the target branch is the {}th occurrence of the branch id={}", entryMethodIndex, entryMethod, targetBranchOccurrences, branchId);

			int traceIndex = entryMethodIndex;
			while (--traceIndex >= 0) {
				TraceEvent ev = stack.get(traceIndex);
				if (ev instanceof EnteredMethodEvent && ev.equals(entryMethod)) {
					entryMethodOccurrences++;
				}		
			}
			LoggingUtils.getEvoLogger().info("[JBSE] entry method is the {}th occurrence of the method", entryMethodOccurrences);
		}

		private int findTargetBranch(int branchId, boolean branchIsTrueSide, double currentFitness) {
			for (int targetBranchIndex = 0; targetBranchIndex < stack.size(); targetBranchIndex++) {
				TraceEvent ev = stack.get(targetBranchIndex);
				if (ev instanceof BranchPassedEvent) {
					BranchPassedEvent evBranch = (BranchPassedEvent) ev;
					if (evBranch.branch == branchId && Math.abs(evBranch.getFitness(branchIsTrueSide) - currentFitness) < .00001) {
						return targetBranchIndex;							
					}
				}
			}
			return -1;
		}

		private int identifyEntryMethodAndCountBranchOccurrences(int targetBranchIndex) {
			Stack<ExitMethodEvent> nestedMethods = new Stack<>();
			
			int traceIndex = targetBranchIndex;
			while (--traceIndex >= 0) {
				TraceEvent ev = stack.get(traceIndex);
				if (ev instanceof ExitMethodEvent) {
					nestedMethods.push((ExitMethodEvent) ev);				
				} else if (ev instanceof EnteredMethodEvent) {
					EnteredMethodEvent evEntered = (EnteredMethodEvent) ev;
					if (nestedMethods.isEmpty()) {
						if (isNotPrivateClassOrMethod(evEntered)) {
							return traceIndex;	
						} //else: search for outer non-private entry
					} else if (evEntered.equals(nestedMethods.peek())) {
						nestedMethods.pop();
					} else {
						LoggingUtils.getEvoLogger().info("[DEBUG] WARNING: unmatched ExitMethodEvent {} wrt EnteredMethodEvent {}", nestedMethods.peek(), evEntered);
						break; //TODO: throw exception (unmatched exitMethodEvent: should not happen)
					}
				} else if (ev instanceof BranchPassedEvent && 
						ev.equals(stack.get(targetBranchIndex))) {
					++targetBranchOccurrences;
				}
			}
			
			return -1;
		}

		private boolean isNotPrivateClassOrMethod(EnteredMethodEvent evEntered) {
			Class<?> clazz = null;
			try {
				clazz = Class.forName(evEntered.className);
			} catch (ClassNotFoundException e) {
				LoggingUtils.getEvoLogger().info("[DEBUG] WARNING: {} - while loading class for EnteredMethodEvent {}", e, evEntered);
				return false; //TODO: throw exception
			}
			if ((clazz.getModifiers() & Modifier.PRIVATE) != 0) {
				//LoggingUtils.getEvoLogger().info("[DEBUG] class is private -- skipping candidate entry method {}", evEntered);
				return false;
			}
			
			String methodName = evEntered.methodName.substring(0,  evEntered.methodName.indexOf('('));
			String methodDescr = evEntered.methodName.substring(methodName.length());				

			List<Executable> methods = new ArrayList<>(Arrays.asList(clazz.getDeclaredMethods()));
			methods.addAll(Arrays.asList(clazz.getDeclaredConstructors()));
			for (Executable m : methods) {
				if (methodName.equals("<init>")) {
					if (!(m instanceof Constructor<?>)) {
						continue;					
					}
				} else if (!methodName.equals(m.getName())) {
					continue;
				}
				
				String mDescr = "(";
			    for(final Class<?> c: m.getParameterTypes()) {
			    		mDescr += getDescriptorForClass(c);
			    }
			    mDescr += ')' + (m instanceof Constructor<?> ? "V" : getDescriptorForClass(((Method) m).getReturnType()));
			    
			    if (methodDescr.equals(mDescr)) { 
					if ((m.getModifiers() & Modifier.PRIVATE) != 0) {
						//LoggingUtils.getEvoLogger().info("[DEBUG] method is private -- skipping candidate entry method {}", evEntered);
						return false;
					} else {
						return true;									
					}
				}
			}

			LoggingUtils.getEvoLogger().info("[DEBUG] WARNING: checking if {} refers to non-private method - ISSUE: method {} not found in class {}", evEntered, evEntered.methodName, evEntered.className);
			return false; //TODO: throw exception
		}
		
		private String getDescriptorForClass(final Class<?> c) {
			if(c.isPrimitive()) {
				if(c == byte.class)
					return "B";
				if(c == char.class)
					return "C";
				if(c == double.class)
					return "D";
				if(c == float.class)
					return "F";
				if(c == int.class)
					return "I";
				if(c == long.class)
					return "J";
				if(c == short.class)
					return "S";
				if(c == boolean.class)
					return "Z";
				if(c == void.class)
					return "V";
				throw new RuntimeException("Unrecognized primitive " + c);
			}

			if(c.isArray()) {
				return c.getName().replace('.', '/');	
			} else {
				return ('L' + c.getName() + ';').replace('.', '/');
			}
		}

	}

	static interface TraceEvent {};
	
	static abstract class MethodEvent implements TraceEvent {
		final String className; 
		final String methodName;
		
		MethodEvent(String className, String methodName){
			this.className = className;
			this.methodName = methodName;
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (!(obj instanceof MethodEvent))
				return false;
			MethodEvent other = (MethodEvent) obj;
			if (className == null) {
				if (other.className != null)
					return false;
			} else if (!className.equals(other.className))
				return false;
			if (methodName == null) {
				if (other.methodName != null)
					return false;
			} else if (!methodName.equals(other.methodName))
				return false;
			return true;
		}	
	}
	
	static class EnteredMethodEvent extends MethodEvent {
		final Object caller;
		
		EnteredMethodEvent(String className, String methodName, Object caller){
			super(className, methodName);
			this.caller = caller;
		}
		
		@Override
		public String toString() {
			return "EnteredMethodEvent [className=" + className + ", methodName=" + methodName + "]";
		}
	}
	
	static class ExitMethodEvent extends MethodEvent {
		ExitMethodEvent(String className, String methodName){
			super(className, methodName);
		}
		
		@Override
		public String toString() {
			return "ExitMethodEvent [className=" + className + ", methodName=" + methodName + "]";
		}
	}
	
	static class BranchPassedEvent implements TraceEvent {
		final int branch;
		final int bytecode_id;
		final double true_distance;
		final double false_distance;
	 
		BranchPassedEvent(int branch, int bytecode_id, double true_distance, double false_distance) {
			this.branch = branch;
			this.bytecode_id = bytecode_id;
			this.true_distance = true_distance;
			this.false_distance = false_distance;
		}
		
		@Override
		public String toString() {
			return "BranchPassedEvent [branch=" + branch + ", bytecode_id=" + bytecode_id + ", true_distance="
					+ true_distance + ", false_distance=" + false_distance + "]";
		}
		
		public double getFitness(boolean branchIsTrueSide) {
			double f = branchIsTrueSide ? true_distance : false_distance;
			return f / (1 + f); //normalized
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BranchPassedEvent other = (BranchPassedEvent) obj;
			if (branch != other.branch)
				return false;
			if (bytecode_id != other.bytecode_id)
				return false;
			return true;
		}
	}

}
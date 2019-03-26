package org.evosuite.ga.metaheuristics.mosa.jbse;

import static org.evosuite.ga.metaheuristics.mosa.jbse.JBSERunnerUtil.bytecodeJump;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

import org.evosuite.Properties;
import org.evosuite.classpath.ClassPathHandler;
import org.evosuite.coverage.branch.Branch;
import org.evosuite.junit.writer.TestSuiteWriter;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.utils.LoggingUtils;

import jbse.algo.exc.CannotManageStateException;
import jbse.apps.run.DecisionProcedureGuidanceJDI;
import jbse.bc.exc.InvalidClassFileFactoryClassException;
import jbse.common.exc.ClasspathException;
import jbse.dec.DecisionProcedureAlgorithms;
import jbse.dec.DecisionProcedureAlwSat;
import jbse.dec.DecisionProcedureClassInit;
import jbse.dec.exc.DecisionException;
import jbse.jvm.Runner;
import jbse.jvm.RunnerBuilder;
import jbse.jvm.RunnerParameters;
import jbse.jvm.EngineParameters.StateIdentificationMode;
import jbse.jvm.Runner.Actions;
import jbse.jvm.exc.CannotBacktrackException;
import jbse.jvm.exc.CannotBuildEngineException;
import jbse.jvm.exc.EngineStuckException;
import jbse.jvm.exc.FailureException;
import jbse.jvm.exc.InitializationException;
import jbse.jvm.exc.NonexistingObservedVariablesException;
import jbse.mem.State;
import jbse.mem.exc.ContradictionException;
import jbse.mem.exc.FrozenStateException;
import jbse.mem.exc.ThreadStackEmptyException;
import jbse.rewr.CalculatorRewriting;
import jbse.rewr.RewriterOperationOnSimplex;
import jbse.rules.ClassInitRulesRepo;
import jbse.tree.StateTree.BranchPoint;

public class JBSERunner {
	
	public static String[] run(Branch branch, TestCase tc, MethodData entryMethodData, int entryMethodOccurrences, int targetBranchOccurrences) {		
		LoggingUtils.getEvoLogger().info("[JBSE] TRACKING BRANCH: " + branch 
				+ " - FROM METHOD " + entryMethodData + " - GUIDED FROM TEST ");
		LoggingUtils.getEvoLogger().info(tc.toString());		

		//write and compile the guiding test case for JBSE
		String testFileName = emitAndCompileGuidingTestCase(tc, branch.getActualBranchId());
		if (testFileName == null) {
			return null;
		}

		//call JBSE as external process
		String[] retValue = null;
		try {
			LoggingUtils.getEvoLogger().info("[JBSE] ASKING RELOCATION FOR BRANCH {}, INSTR {}", branch, branch.getInstruction());
			int bytecodeOffset = 
					JBSEBytecodeRelocationRegistry._I().getRelocatedOffset(branch.getInstruction());
			
			Process process = Runtime.getRuntime().exec(
					"/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/jre/bin/java " +
					"-classpath " + ClassPathHandler.getInstance().getTargetProjectClasspath() +
					":/Users/denaro/git/DynaMOSA-SUSHI/client/target/classes" +
					":/Users/denaro/git/DynaMOSA-SUSHI/sushi-lib/lib/javaparser-core-3.4.0.jar" + //needed for using java-parser on test cases
					":/Library/Java/JavaVirtualMachines/jdk1.8.0_131.jdk/Contents/Home/lib/tools.jar" + //needed for calling javac 
					":/Users/denaro/.m2/repository/org/javassist/javassist/3.22.0-GA/javassist-3.22.0-GA.jar" + 
					":/Users/denaro/.m2/repository/org/slf4j/slf4j-api/1.7.22/slf4j-api-1.7.22.jar" + 
					//":/Users/denaro/.m2/repository/org/ow2/asm/asm-all/5.1/asm-all-5.1.jar" + 
					":/Users/denaro/git/DynaMOSA-SUSHI/runtime/target/classes" +
					":/Users/denaro/git/DynaMOSA-SUSHI/client/target/classes" +
					":" + Properties.TMP_TEST_DIR +
					
					" org.evosuite.ga.metaheuristics.mosa.jbse.JBSERunner " +
					
					/*TARGET BRANCH DATA */				
					branch.getClassName() + " " +
					branch.getMethodName() + " " +
					bytecodeOffset + " " +
					targetBranchOccurrences + " " +
					
					/*ENTRY_METHOD_DATA */
					entryMethodData.getClassNameSlashed() + " " +
					entryMethodData.getMethodName() + " " +
					entryMethodData.getMethodDescriptor() + " " + 
					entryMethodOccurrences + " " +
					
					/*TEST_DIR */Properties.TMP_TEST_DIR + " " +					
					/*TEST_FILE_NAME */ testFileName + " " +
					/*APC_ID */ branch.getActualBranchId());
			
			BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader brErr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			String line;
			while ((line = br.readLine()) != null) {
				LoggingUtils.getEvoLogger().info("[JBSE EXT PROCESS] " + line);
				if(line.startsWith("SUCCESS:")) {
					retValue = line.split(":");
					retValue = Arrays.copyOfRange(retValue, 1, retValue.length);
				}
			}
			while ((line = brErr.readLine()) != null) {
				LoggingUtils.getEvoLogger().info(line);
			}
			if (retValue == null) {
				LoggingUtils.getEvoLogger().info("[JBSE] TARGET BRANCH NOT FOUND");
			}
		} catch (IOException e) {
			LoggingUtils.getEvoLogger().info(Arrays.toString(e.getStackTrace()));
		}
		
		return retValue;
	}


	private static JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	
	private static final String COMMANDLINE_LAUNCH_Z3 = System.getProperty("os.name").toLowerCase().contains("windows") ? " /smt2 /in /t:10" : " -smt2 -in -t:10";

	private static String TEST_DIR;
	private static String TARGET_BRANCH_CLASS;
	private static String TARGET_BRANCH_METHOD;
	private static int TARGET_BRANCH_BYTECODE_OFFSET;
	private static int TARGET_BRANCH_OCCURRENCES;
	private static MethodData ENTRY_METHOD_DATA;
	private static int ENTRY_METHOD_OCCURRENCES;
	private static GuidingTestCaseData GUIDING_TC_DATA;
	
	private String[] classpath;
	private final String z3Path;
	private final RunnerParameters commonParamsGuided;
	private final RunnerParameters commonParamsGuiding;
	private State initialState = null;

	public static void main(String[] args) throws DecisionException, CannotBuildEngineException, InitializationException, InvalidClassFileFactoryClassException, NonexistingObservedVariablesException, ClasspathException, CannotBacktrackException, CannotManageStateException, ThreadStackEmptyException, ContradictionException, EngineStuckException, FailureException {
		
		System.out.println("ARGS: " + Arrays.toString(args));
		TARGET_BRANCH_CLASS = args[0];
		TARGET_BRANCH_METHOD = args[1];
		TARGET_BRANCH_BYTECODE_OFFSET = Integer.parseInt(args[2]);
		TARGET_BRANCH_OCCURRENCES = Integer.parseInt(args[3]);
		
		ENTRY_METHOD_DATA = new MethodData(args[4], args[5], args[6]);
		ENTRY_METHOD_OCCURRENCES = Integer.parseInt(args[7]);
		
		TEST_DIR = args[8];
		String TEST_FILE_NAME = args[9];
		GUIDING_TC_DATA = new GuidingTestCaseData(
				TEST_FILE_NAME, "()V", "test0", 
				Paths.get(TEST_DIR));
		int APC_ID = Integer.parseInt(args[10]);
			

		JBSERunner runner = new JBSERunner(); 
		State finalState = runner.runProgram();	
		if (finalState != null) {
			String evaluatorNames[] = emitAndCompilePathConditionEvaluator(
					APC_ID, runner.initialState, finalState);
			String successString = "SUCCESS";
			for (int i = 0; i < evaluatorNames.length; i++) {
				successString += ":" + evaluatorNames[i];
			}
			System.out.println(successString);
		}
	}
	
	public JBSERunner() {

        //String[] projectClasspath = ClassPathHandler.getInstance().getTargetProjectClasspath().replace("\\", "/").split(":");
		String[] projectClasspath = System.getProperty("java.class.path").replace("\\", "/").split(":");
		this.classpath = Arrays.copyOf(projectClasspath, projectClasspath.length + 4);
		this.classpath[projectClasspath.length] =  "/Users/denaro/Desktop/evosuite-shaded-1.0.6-SNAPSHOT.jar"; //TODO: how can I get this path?
		this.classpath[projectClasspath.length + 1] =  "/Users/denaro/git/DynaMOSA-SUSHI/runtime/target/classes"; //TODO: how can I get this path?
		this.classpath[projectClasspath.length + 2] =  "/Users/denaro/git/DynaMOSA-SUSHI/client/target/classes"; //TODO: how can I get this path?
		this.classpath[projectClasspath.length + 3] = TEST_DIR;
		
		System.out.println("Using classpath: " + Arrays.toString(this.classpath));
		
		this.z3Path = Paths.get("/Users", "denaro", "Desktop", "RTools", "Z3", "z3-4.3.2.d548c51a984e-x64-osx-10.8.5", "bin", "z3").toString(); //TODO Options
		
		//builds the template parameters object for the guided (symbolic) execution
		this.commonParamsGuided = new RunnerParameters();
		this.commonParamsGuided.addUserClasspath(this.classpath);
		this.commonParamsGuided.setCountScope(1000); // To avoid infinte loops

		//builds the template parameters object for the guiding (concrete) execution
		this.commonParamsGuiding = new RunnerParameters();
		this.commonParamsGuiding.addUserClasspath(this.classpath);
		this.commonParamsGuiding.setStateIdentificationMode(StateIdentificationMode.COMPACT);
	}

	
	/**
	 * Performs symbolic execution of the target method guided by a test case,
	 * and returns the final state. Equivalent to {@link #runProgram(int) runProgram}{@code (-1).}
	 * {@link List#get(int) get}{@code (0)}.
	 * @param branch 
	 * 
	 * @param testCase a {@link GuidingTestCaseData}, it will guide symbolic execution.
	 * @return the final {@link State}.
	 * @throws DecisionException
	 * @throws CannotBuildEngineException
	 * @throws InitializationException
	 * @throws InvalidClassFileFactoryClassException
	 * @throws NonexistingObservedVariablesException
	 * @throws ClasspathException
	 * @throws CannotBacktrackException
	 * @throws CannotManageStateException
	 * @throws ThreadStackEmptyException
	 * @throws ContradictionException
	 * @throws EngineStuckException
	 * @throws FailureException
	 */
	public State runProgram()
			throws DecisionException, CannotBuildEngineException, InitializationException, 
			InvalidClassFileFactoryClassException, NonexistingObservedVariablesException, 
			ClasspathException, CannotBacktrackException, CannotManageStateException, 
			ThreadStackEmptyException, ContradictionException, EngineStuckException, 
			FailureException {

		//builds the parameters
		final RunnerParameters pGuided = this.commonParamsGuided.clone();
		final RunnerParameters pGuiding = this.commonParamsGuiding.clone();

		//sets the calculator
		final CalculatorRewriting calc = new CalculatorRewriting();
		calc.addRewriter(new RewriterOperationOnSimplex());
		pGuided.setCalculator(calc);
		pGuiding.setCalculator(calc);
		
		//sets the decision procedures
		pGuided.setDecisionProcedure(new DecisionProcedureAlgorithms(new DecisionProcedureAlwSat(), calc));
		/*pGuided.setDecisionProcedure(new DecisionProcedureAlgorithms(
				new DecisionProcedureClassInit( //useless?
						new DecisionProcedureLICS( //useless?
								new DecisionProcedureSMTLIB2_AUFNIRA(
										new DecisionProcedureAlwSat(), 
										calc, this.z3Path + COMMANDLINE_LAUNCH_Z3), 
								calc, new LICSRulesRepo()), 
						calc, new ClassInitRulesRepo()), calc));*/
		pGuiding.setDecisionProcedure(
				new DecisionProcedureAlgorithms(
						new DecisionProcedureClassInit(
								new DecisionProcedureAlwSat(), 
								calc, new ClassInitRulesRepo()), 
						calc)); //for concrete execution

		//sets the guiding method (to be executed concretely) and the entry method (symbolically)
		pGuiding.setMethodSignature(GUIDING_TC_DATA.getClassName(), GUIDING_TC_DATA.getMethodDescriptor(), GUIDING_TC_DATA.getMethodName());
		pGuided.setMethodSignature(ENTRY_METHOD_DATA.getClassNameSlashed(), ENTRY_METHOD_DATA.getMethodDescriptor(), ENTRY_METHOD_DATA.getMethodName());
		
		//creates the guidance decision procedure and sets it
		/*int numberOfHits = countNumberOfInvocations(GUIDING_TC_DATA.getSourcePath(), ENTRY_METHOD_DATA.getMethodName(), ENTRY_METHOD_DATA.getMethodDescriptor());
		System.out.println("Entry point method called " + numberOfHits + " times in test case; starting guidance until last call");
		if (numberOfHits == 0) {
			numberOfHits = 1; //the method is called indirectly from the test code, we arbitrarily assume it suffices to stop at the first occurrence 
		}*/
		
		final DecisionProcedureGuidanceJDI guid = new DecisionProcedureGuidanceJDI(pGuided.getDecisionProcedure(),
				pGuided.getCalculator(), pGuiding, pGuided.getMethodSignature(), ENTRY_METHOD_OCCURRENCES);
		pGuided.setDecisionProcedure(guid);
		System.out.println("Guidance test successfully started");

		/*SettingsReader rd = null;
		try {
			rd = new SettingsReader("/Users/denaro/Documents/workspace.luna/sushi-experiments-closure72/sushi/settings/closure_parse_tree_hex_minimal.jbse");
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		rd.fillRunnerParameters(pGuided);*/
		
		//sets the actions
		final ActionsRunner actions = new ActionsRunner();
		pGuided.setActions(actions);

		/*((CalculatorRewriting) pGuided.getCalculator()).addRewriter(new RewriterPolynomials());
		((CalculatorRewriting) pGuided.getCalculator()).addRewriter(new RewriterSinCos());
		((CalculatorRewriting) pGuided.getCalculator()).addRewriter(new RewriterSqrt());
		((CalculatorRewriting) pGuided.getCalculator()).addRewriter(new RewriterAbsSum());*/

		//builds the runner and runs it
		final RunnerBuilder rb = new RunnerBuilder();
		final Runner r = rb.build(pGuided);
		r.run();

		//outputs
		this.initialState = rb.getEngine().getInitialState();
		
		//finalizes
		rb.getEngine().close();

		return actions.getFinalState();
	}
	
/*	private static class CountVisitor extends VoidVisitorAdapter<Object> {
		final String methodName;
		final int numArgs;
		int methodCallCounter = 0;
		final String methodDescriptor; //TODO: not used at the moment, we check only on num of params

		public CountVisitor(String methodName, String methodDescriptor) {
			this.methodName = methodName;
			this.methodDescriptor = methodDescriptor;
			
			int numArgs = 0;
			String[] parts = methodDescriptor.substring(1, methodDescriptor.indexOf(')')).split(";");
			for (String p : parts) {
				if (p.indexOf('L') >= 0) {
					p = p.substring(0, p.indexOf('L') + 1);
				}
				numArgs += p.length();
			}
			this.numArgs = numArgs;
		}

		@Override
		public void visit(MethodCallExpr n, Object arg) {
			super.visit(n, arg);
			if (n.getNameAsString().equals(this.methodName) && n.getArguments().size() == numArgs) {
				this.methodCallCounter++;
			}
		}
	}

	private int countNumberOfInvocations(Path sourcePath, String methodName, String methodDescriptor){
		//TODO use the whole signature of the target method to avoid ambiguities (that's quite hard)
		final CountVisitor v = new CountVisitor(methodName, methodDescriptor);
		try {
			final FileInputStream in = new FileInputStream(sourcePath.toFile());
			v.visit(JavaParser.parse(in), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return v.methodCallCounter;
	}
*/
	private static class ActionsRunner extends Actions {
		private State finalState = null;
		private boolean postInitial = false;
		private boolean atJump = false;
		private int targetBranchOccurrences = 0;
		
		public ActionsRunner() {
		}
		
		public State getFinalState() {
			return finalState;
		}
						
		@Override
		public boolean atInitial() {
			/*final State currentState = getEngine().getCurrentState();
			try {
				System.out.println(currentState);
				System.out.println(currentState.getIdentifier() + "[" + currentState.getSequenceNumber() + "] " + currentState.getCurrentMethodSignature() + " " + currentState.getPC());
			} catch (FrozenStateException | ThreadStackEmptyException e) {
				e.printStackTrace();
			}*/
			this.postInitial = true;
			return super.atInitial();
		}
		
		@Override
		public boolean atStepPre() {
			if (this.postInitial) {
				try {
					final State currentState = getEngine().getCurrentState();
					//System.out.println(currentState.getIdentifier() + "[" + currentState.getSequenceNumber() + "] " + currentState.getCurrentMethodSignature() + " " + currentState.getPC());
				
					String searchedSig = TARGET_BRANCH_CLASS.replace('.', '/') + ":" +
							TARGET_BRANCH_METHOD;
					String currentSig = currentState.getCurrentMethodSignature().getClassName() + ":" +
							currentState.getCurrentMethodSignature().getName() + 
							currentState.getCurrentMethodSignature().getDescriptor();
					//System.out.println(currentState.getPC() + ": " + currentState.getCurrentMethodSignature());						
					if (!currentSig.equals(searchedSig)) {
						if (currentSig.contains(TARGET_BRANCH_METHOD)) {
							System.out.println("DIFFERENT? " + currentSig);	
						}
						return super.atStepPre();
					}
					this.atJump = bytecodeJump(currentState.getInstruction());
					if (this.atJump) {
						//System.out.println(currentState.getPC() + ": Jump instr in target method");						
						this.atJump = currentState.getPC() >= TARGET_BRANCH_BYTECODE_OFFSET - 2 && currentState.getPC() <= TARGET_BRANCH_BYTECODE_OFFSET + 2; //TODO: identify precise bytecode offset
						if (this.atJump) {
							targetBranchOccurrences++;
						}
						this.atJump = targetBranchOccurrences == TARGET_BRANCH_OCCURRENCES;
						/*if (this.atJump) {
							System.out.println("FOUND: " + currentState.getCurrentMethodSignature());
						}*/
					}
				} catch (ThreadStackEmptyException | FrozenStateException e) {
					//this should never happen
					throw new RuntimeException(e); //TODO better exception!
				}
			}
			return super.atStepPre();
		}
		
		@Override
		public boolean atStepPost() {
			final State currentState = getEngine().getCurrentState();
			if (this.postInitial && this.atJump) {
				this.finalState = currentState.clone();				
				//System.out.println("atStepPost: " + Arrays.toString(finalState.getPathCondition().toArray()));
				getEngine().stopCurrentTrace();
			}
			return super.atStepPost();
		}
		
		@Override
		public boolean atBacktrackPost(BranchPoint bp) {
			final State currentState = getEngine().getCurrentState();
			if (this.atJump) {
				this.finalState = currentState.clone();
				//System.out.println("atBacktrackPost: " + Arrays.toString(finalState.getPathCondition().toArray()));
				getEngine().stopCurrentTrace();
			}
			return super.atBacktrackPost(bp);
		}

		@Override
		public void atEnd() {
			super.atEnd();
		}
	}

	
	private static String emitAndCompileGuidingTestCase(TestCase tc, int id) {		
		//write the source of the test case in the output directory
		String testClassName = "TemporaryTestForBranch_" + id + "_Test";
		TestSuiteWriter suiteWriter = new TestSuiteWriter();
		suiteWriter.insertTest(tc, "Reach frontier of (even though does not cover) coverage goal");
		List<?> testFiles = suiteWriter.writeTestSuite(testClassName, Properties.TMP_TEST_DIR, new ArrayList<ExecutionResult>());
		LoggingUtils.getEvoLogger().info("[JBSE] WRITTEN TEMPORARY TEST CASE FOR JBSE: " + testFiles.get(0) + ", " + testFiles.get(1));		
		
		//compile the test case
		final Path javacLogFilePath = Paths.get(Properties.TMP_TEST_DIR).resolve("javac-log-test_" + id + ".txt");
		final String classpathCompilationTest = Properties.TMP_TEST_DIR + 
				File.pathSeparator + ClassPathHandler.getInstance().getTargetProjectClasspath().replace("\\", "/") +
				File.pathSeparator + "/Users/denaro/Documents/workspace.luna/sushi-experiments-closure01/lib/junit.jar" + //TODO: how can I get this path?
				File.pathSeparator + "/Users/denaro/git/DynaMOSA-SUSHI/runtime/target/classes"; //TODO: how can I get this path?
		final String[] javacParametersTestCase = { "-cp", classpathCompilationTest, "-d", Properties.TMP_TEST_DIR, testFiles.get(0).toString() };
		final String[] javacParametersTestScaff = { "-cp", classpathCompilationTest, "-d", Properties.TMP_TEST_DIR, testFiles.get(1).toString()};
		try (final OutputStream w = new BufferedOutputStream(Files.newOutputStream(javacLogFilePath))) {
			compiler.run(null, w, w, javacParametersTestScaff);
			compiler.run(null, w, w, javacParametersTestCase);
		} catch (IOException e) {
			LoggingUtils.getEvoLogger().info("[JBSE] CANNOT GENERATE AIDING PATH CONDITION: Unexpected I/O error while creating log file " + javacLogFilePath + ": " + e);
			return null; //TODO throw an exception?
		}
		
		//extract the qualified name of the test class
		String testClass = testFiles.get(0).toString();
		testClass = testClass.substring(Properties.TMP_TEST_DIR.length() + 1, testClass.length() - 5).replace('/',  '.');
		
		//check the compiled code includes method test0
		/*try {
			final URLClassLoader cloader = URLClassLoader.newInstance(new URL[]{ Paths.get(Properties.TMP_TEST_DIR).toUri().toURL() }); 
			cloader.loadClass(testClass).getDeclaredMethod("test0");
			LoggingUtils.getEvoLogger().info("[JBSE] COMPILED TEST CASE: " + testClass + ".class");		
		} catch (NoSuchMethodException | MalformedURLException | SecurityException | ClassNotFoundException e) { 
			//EvoSuite failed to generate a valid test case, thus we just ignore it 
			LoggingUtils.getEvoLogger().info("[JBSE] CANNOT GENERATE AIDING PATH CONDITION: Failed to generate a valid the test case " + testClass + ": exception is: " + e);
			return null; //TODO throw an exception
		}*/
		
		return testClass.replace('.', '/');
	}
	
	/**
	 * Emits the EvoSuite wrapper (file .java) for the path condition of some state.
	 * 
	 * @param testCount an {@code int}, the number used to identify the test.
	 * @param initialState a {@link State}; must be the initial state in the execution 
	 *        for which we want to generate the wrapper.
	 * @param finalState a {@link State}; must be the final state in the execution 
	 *        for which we want to generate the wrapper.
	 * @return a {@link Path}, the file path of the generated EvoSuite wrapper.
	 */
	private static String[] emitAndCompilePathConditionEvaluator(int id, State initialState, State finalState) {
		final String evaluatorName = "APCFitnessEvaluator_" + id;
		String packageString = ENTRY_METHOD_DATA.getClassNameDotted();
		int lastDotIndex = packageString.lastIndexOf('.');
		if (lastDotIndex > 0) {
			packageString = packageString.substring(0, lastDotIndex);
		} else {
			packageString = null; //default package
		}

		
		final StateFormatterAidingPathCondition fmt = new StateFormatterAidingPathCondition(id, packageString, () -> initialState);
		fmt.decomposePathConditionInIndipendentFormulas(finalState);
		String[] ret = new String[fmt.getNumOfIndipendentFormulas()];
		for (int i = 0; i < fmt.getNumOfIndipendentFormulas(); i++) {
			fmt.formatPrologue(i);
			fmt.formatState(finalState, i);
			fmt.formatEpilogue();
		
			final Path evaluatorFilePath = Paths.get(TEST_DIR).resolve(packageString.replace('.',  '/')).resolve(evaluatorName + "_" + i + ".java");
			try (final BufferedWriter w = Files.newBufferedWriter(evaluatorFilePath)) {
				String evaluatorJavaCode = fmt.emit();
				w.write(evaluatorJavaCode);
				//System.out.println(evaluatorJavaCode);
			} catch (IOException e) {
				System.out.println("[JBSE] Unexpected I/O error while creating fitness evaluator for path conditon " + evaluatorFilePath.toString() + ": " + e);
				return null;
				//TODO throw an exception
			}
			fmt.cleanup();
	
			//compile the evaluator
			final String classpathCompilationWrapper = System.getProperty("java.class.path").replace("\\", "/"); //TODO: this.classesPath + File.pathSeparator + this.sushiLibPath;
			
			final Path javacLogFilePath = Paths.get(TEST_DIR).resolve("javac-log-APCFitnessEvaluator_" + id + "_" + i + ".txt");
			final String[] javacParameters = { "-cp", classpathCompilationWrapper, "-d", TEST_DIR, evaluatorFilePath.toString() };
			try (final OutputStream w = new BufferedOutputStream(Files.newOutputStream(javacLogFilePath))) {
				compiler.run(null, w, w, javacParameters);
				ret[i]  = packageString + "." + evaluatorName + "_" + i;
			} catch (IOException e) {
				System.out.println("[JBSE] Unexpected I/O error while creating evaluator compilation log file " + javacLogFilePath.toString() + ": " + e);
				return null;
				//TODO throw an exception
			}
			
		}
		
		return ret;
	}

}

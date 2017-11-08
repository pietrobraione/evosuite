package org.evosuite.ga.metaheuristics.mosa.structural;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.evosuite.Properties;
import org.evosuite.coverage.pathcondition.PathConditionCoverageFactory;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.junit.writer.TestSuiteWriter;
import org.evosuite.testcase.ConstantInliner;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.TestCaseMinimizer;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.testcase.execution.ExecutionTracer;
import org.evosuite.testcase.execution.TestCaseExecutor;
import org.evosuite.utils.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class manages the path condition goals to consider during the search
 * @author Giovanni Denaro
 * 
 */
public class PathConditionManager<T extends Chromosome> extends StructuralGoalManager<T>{

	private static final Logger logger = LoggerFactory.getLogger(PathConditionManager.class);

	/**
	 * Constructor used to initialize the set of uncovered goals, and the initial set
	 * of goals to consider as initial contrasting objectives
	 * @param fitnessFunctions List of all FitnessFunction<T>
	 */
	public PathConditionManager(List<FitnessFunction<T>> fitnessFunctions){
		super(fitnessFunctions);
		// initialize uncovered goals
		uncoveredGoals.addAll(fitnessFunctions);
		// initialize current goals
		this.currentGoals.addAll(fitnessFunctions);
	}

	@Override
	public void calculateFitness(T c){
		// run the test
		if (c.isChanged()){
			TestCase test = ((TestChromosome) c).getTestCase();
			ExecutionResult result = TestCaseExecutor.runTest(test);
			((TestChromosome) c).setLastExecutionResult(result);
			c.setChanged(false);
			
			if (result.hasTimeout() || result.hasTestException()){
				for (FitnessFunction<T> f : currentGoals)
					c.setFitness(f, Double.MAX_VALUE);
				return;
			}
		}

		// We update the archive and the set of currents goals
		LinkedList<FitnessFunction<T>> targets = new LinkedList<FitnessFunction<T>>();
		targets.addAll(this.currentGoals);

		while (targets.size()>0){
			FitnessFunction<T> fitnessFunction = targets.poll();
			double value = fitnessFunction.getFitness(c);
			if (value == 0.0) {
				updateCoveredGoals(fitnessFunction, c);

				if (Properties.EMIT_TESTS_INCREMENTALLY) { /*SUSHI: Incremental test cases*/
					emitTestCase((PathConditionCoverageGoalFitness) fitnessFunction, (TestChromosome) c);
				}

				if (fitnessFunction instanceof PathConditionCoverageGoalFitness) {
					ExecutionTracer.removeEvaluatorForPathCondition(((PathConditionCoverageGoalFitness) fitnessFunction).getPathConditionGoal());
				}
			}
		}
	}

	@Override
	public void restoreInstrumentationForAllGoals() {
		if (!Properties.EMIT_TESTS_INCREMENTALLY) {
			PathConditionCoverageFactory pathConditionFactory = new PathConditionCoverageFactory();
			List<PathConditionCoverageGoalFitness> goals = pathConditionFactory.getCoverageGoals();

			for (PathConditionCoverageGoalFitness g : goals) {
				ExecutionTracer.addEvaluatorForPathCondition(g.getPathConditionGoal());
			}		
		} else {
			Properties.JUNIT_TESTS = false;
		}
	}


	/*SUSHI: Incremental test cases*/
	private void emitTestCase(PathConditionCoverageGoalFitness goal, TestChromosome tc) {
		if (Properties.JUNIT_TESTS) {

			TestChromosome tcToWrite = (TestChromosome) tc.clone();

			if (Properties.MINIMIZE) {
				TestCaseMinimizer minimizer = new TestCaseMinimizer(goal);
				minimizer.minimize(tcToWrite);
			}

			if (Properties.INLINE) {
				ConstantInliner inliner = new ConstantInliner();
				inliner.inline(tcToWrite.getTestCase());
			}

			//writing the output

			TestSuiteWriter suiteWriter = new TestSuiteWriter();
			suiteWriter.insertTest(tcToWrite.getTestCase(), "Covered goal: " + goal.toString());

			String evaluatorName = goal.getEvaluatorName().substring(goal.getEvaluatorName().lastIndexOf('.') + 1);
			String suffix = evaluatorName.substring(evaluatorName.indexOf('_')) + "_Test";
			String testName = Properties.TARGET_CLASS.substring(Properties.TARGET_CLASS.lastIndexOf(".") + 1) + suffix;
			String testDir = Properties.TEST_DIR;

			suiteWriter.writeTestSuite(testName, testDir, new ArrayList());

			LoggingUtils.getEvoLogger().info("\n\n* EMITTED TEST CASE: " + goal.getEvaluatorName() + ", " + testName);
		}
	}

}

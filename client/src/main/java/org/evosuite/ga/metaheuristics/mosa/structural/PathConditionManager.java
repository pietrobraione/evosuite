package org.evosuite.ga.metaheuristics.mosa.structural;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.coverage.branch.BranchCoverageGoal;
import org.evosuite.coverage.branch.BranchCoverageTestFitness;
import org.evosuite.coverage.pathcondition.AidingPathConditionGoalFitness;
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
	
	private Map<PathConditionCoverageGoalFitness, Set<BranchCoverageGoal>> pcRelatedUncoveredBranches = new LinkedHashMap<>(); //map path conditions to the branch targets that they (lead to) traverse
	private Set<BranchCoverageGoal> coveredAndAlreadyPrunedBranches = new HashSet<>(); //cache of branches that were already pruned from path conditions

	/**
	 * Constructor used to initialize the set of uncovered goals, and the initial set
	 * of goals to consider as initial contrasting objectives
	 * @param fitnessFunctions List of all FitnessFunction<T>
	 */
	public PathConditionManager(List<FitnessFunction<T>> pathConditionsGoals){
		super(pathConditionsGoals);
		// initialize uncovered goals
		uncoveredGoals.addAll(pathConditionsGoals);
		// initialize current goals
		this.currentGoals.addAll(pathConditionsGoals);
	}

	public PathConditionManager(List<FitnessFunction<T>> pathConditionsGoals, List<FitnessFunction<T>> branchGoals){
		this(pathConditionsGoals);
		
		if (Properties.PATH_CONDITION_SUSHI_BRANCH_COVERAGE_INFO == null) return;
		
		List<BranchCoverageGoal> relevantBranches = new ArrayList<>();
		for (FitnessFunction<T> f1 : branchGoals) {
			if (!(f1 instanceof BranchCoverageTestFitness)) continue;
			relevantBranches.add(((BranchCoverageTestFitness) f1).getBranchGoal());
		}

		for (FitnessFunction<T> f : pathConditionsGoals) {
			if (!(f instanceof PathConditionCoverageGoalFitness)) continue;
			PathConditionCoverageGoalFitness pc = (PathConditionCoverageGoalFitness) f;
			
			Set<BranchCoverageGoal> branchCovInfo = PathConditionCoverageFactory._I().getBranchCovInfo(pc, relevantBranches);
						
			if (branchCovInfo != null && !branchCovInfo.isEmpty()) {
				Set<BranchCoverageGoal> uncoveredBranches = new HashSet<>(branchCovInfo);
				pcRelatedUncoveredBranches.put(pc, uncoveredBranches);
			} else {
				doneWithPathCondition(pc);				
			}
		}
	}

	public void doneWithPathCondition(PathConditionCoverageGoalFitness pc) {
		pcRelatedUncoveredBranches.remove(pc);
		currentGoals.remove(pc);
		uncoveredGoals.remove(pc);
		ExecutionTracer.removeEvaluatorForPathCondition(pc.getPathConditionGoal());
	}

	@Override
	public void calculateFitness(T c) {
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
				//if (!(fitnessFunction instanceof AidingPathConditionGoalFitness)) {
				updateCoveredGoals(fitnessFunction, c);
				//}
					
				if (fitnessFunction instanceof PathConditionCoverageGoalFitness) {
					if (Properties.EMIT_TESTS_INCREMENTALLY) { /*SUSHI: Incremental test cases*/
						emitTestCase((PathConditionCoverageGoalFitness) fitnessFunction, (TestChromosome) c);
					}
					doneWithPathCondition((PathConditionCoverageGoalFitness) fitnessFunction);
				}
			}
		}
	}

	@Override
	public void restoreInstrumentationForAllGoals() {
		if (!Properties.EMIT_TESTS_INCREMENTALLY) {
			PathConditionCoverageFactory pathConditionFactory = PathConditionCoverageFactory._I();
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

	public void prunePathConditionsByCoveredBranches(Map<FitnessFunction<T>, T> coveredBranches) {
		// let's check for newly covered branches and update path conditions accordingly
		if (Properties.PATH_CONDITION_SUSHI_BRANCH_COVERAGE_INFO == null) return;
		
		if (coveredBranches.size() > coveredAndAlreadyPrunedBranches.size()) {
			for (FitnessFunction<T> b : coveredBranches.keySet()) {
				if (b instanceof BranchCoverageTestFitness) {
					BranchCoverageGoal branch = ((BranchCoverageTestFitness) b).getBranchGoal();
					if (coveredAndAlreadyPrunedBranches.contains(branch)) continue;
					prunePathConditionsByCoveredBranches(branch);
					coveredAndAlreadyPrunedBranches.add(branch);
				}
			}
		}

	}

	private void prunePathConditionsByCoveredBranches(BranchCoverageGoal b) {
		Set<PathConditionCoverageGoalFitness> toRemove = new LinkedHashSet<>();

		// prune b from the set of uncovered branches associated with the path conditions
		for (Entry<PathConditionCoverageGoalFitness, Set<BranchCoverageGoal>> pcEntry : pcRelatedUncoveredBranches.entrySet()) {
			
			Set<BranchCoverageGoal> uncoveredBranches = pcEntry.getValue();
			uncoveredBranches.remove(b);
			if (uncoveredBranches.isEmpty()) {
				toRemove.add(pcEntry.getKey());
			}
		}
		
		// remove the path conditions that do not associate with any target branch
		for (PathConditionCoverageGoalFitness pc : toRemove) {
			doneWithPathCondition(pc);
		}
	}
	
}

package org.evosuite.ga.metaheuristics.mosa.structural;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.coverage.branch.BranchCoverageGoal;
import org.evosuite.coverage.branch.BranchCoverageTestFitness;
import org.evosuite.coverage.pathcondition.PathConditionCoverageFactory;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.junit.writer.TestSuiteWriter;
import org.evosuite.testcase.ConstantInliner;
import org.evosuite.testcase.TestCaseMinimizer;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testcase.execution.ExecutionTracer;
import org.evosuite.utils.LoggingUtils;

/**
 * This Class manages the path condition goals to consider during the search
 * @author Giovanni Denaro
 * 
 */
public class PathConditionManager extends MultiCriteriaManager {

	private static final long serialVersionUID = -9076875100376403089L;

	private Map<PathConditionCoverageGoalFitness, Set<BranchCoverageGoal>> pcRelatedUncoveredBranches = new LinkedHashMap<>(); //map path conditions to the branch targets that they (lead to) traverse
	private Set<BranchCoverageGoal> coveredAndAlreadyPrunedBranches = new HashSet<>(); //cache of branches that were already pruned from path conditions

	/**
	 * Constructor used to initialize the set of uncovered goals, and the initial set
	 * of goals to consider as initial contrasting objectives
	 * @param fitnessFunctions List of all FitnessFunction<T>
	 */
	public PathConditionManager(List<TestFitnessFunction> targets){
		super(targets);
		
		//add all path condition goals as current goals
		for (TestFitnessFunction goal : targets) {
			if (goal instanceof PathConditionCoverageGoalFitness) {
				this.currentGoals.add(goal);
			}
		}
			
		if (Properties.PATH_CONDITION_SUSHI_BRANCH_COVERAGE_INFO == null) {
			addDependenciesBetweenPathConditionsAndRelevantBranches();
		}
	}

	private void addDependenciesBetweenPathConditionsAndRelevantBranches() {
		List<BranchCoverageGoal> relevantBranches = new ArrayList<>();
		for (TestFitnessFunction goal : this.getUncoveredGoals()){
			if (goal instanceof BranchCoverageTestFitness) {
				relevantBranches.add(((BranchCoverageTestFitness) goal).getBranchGoal());
			}
		}

		for (TestFitnessFunction goal : this.getUncoveredGoals()) {
			if (!(goal instanceof PathConditionCoverageGoalFitness)) continue;
			PathConditionCoverageGoalFitness pc = (PathConditionCoverageGoalFitness) goal;
			
			Set<BranchCoverageGoal> branchCovInfo = PathConditionCoverageFactory._I().getBranchCovInfo(pc, relevantBranches);
						
			if (branchCovInfo != null && !branchCovInfo.isEmpty()) {
				Set<BranchCoverageGoal> uncoveredBranches = new HashSet<>(branchCovInfo);
				pcRelatedUncoveredBranches.put(pc, uncoveredBranches);
			} else {
				doneWithPathCondition(pc);				
			}
		}
	}

	protected void doneWithPathCondition(PathConditionCoverageGoalFitness pc) {
		pcRelatedUncoveredBranches.remove(pc);
		currentGoals.remove(pc);
		ExecutionTracer.removeEvaluatorForPathCondition(pc.getPathConditionGoal());
	}

	@Override
	public void calculateFitness(TestChromosome c, GeneticAlgorithm<TestChromosome> ga) {
		HashSet<TestFitnessFunction> currentGoalsBefore = new HashSet<>(this.getCurrentGoals());
		super.calculateFitness(c, ga);
		
		//Check for completed path-condition goals
		for (TestFitnessFunction goal: currentGoalsBefore) {
			if (!(goal instanceof PathConditionCoverageGoalFitness) || this.getCurrentGoals().contains(goal)) {
				continue; // goal is not a path condition or is not yet completed
			}
			//for each completed path condition:
			if (Properties.EMIT_TESTS_INCREMENTALLY) { /*SUSHI: Incremental test cases*/
				emitTestCase((PathConditionCoverageGoalFitness) goal, (TestChromosome) c);
			}
			doneWithPathCondition((PathConditionCoverageGoalFitness) goal);
		}
		// If new targets were covered, try to prune the path conditions related to any newly covered branch
		if (this.getCurrentGoals().size() < currentGoalsBefore.size()) { 
			prunePathConditionsByCoveredBranches(this.getCoveredGoals());
		}
	}

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

	private void prunePathConditionsByCoveredBranches(Collection<TestFitnessFunction> coveredTargets) {
		// let's check for newly covered branches and update path conditions accordingly
		if (Properties.PATH_CONDITION_SUSHI_BRANCH_COVERAGE_INFO == null) return;
		
		for (TestFitnessFunction t : coveredTargets) {
			if (t instanceof BranchCoverageTestFitness) {
				BranchCoverageGoal branch = ((BranchCoverageTestFitness) t).getBranchGoal();
				if (coveredAndAlreadyPrunedBranches.contains(branch)) continue;
				prunePathConditionsByCoveredBranches(branch);
				coveredAndAlreadyPrunedBranches.add(branch);
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

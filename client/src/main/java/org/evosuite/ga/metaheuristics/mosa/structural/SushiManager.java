package org.evosuite.ga.metaheuristics.mosa.structural;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.Properties.Criterion;
import org.evosuite.coverage.branch.BranchCoverageTestFitness;
import org.evosuite.coverage.pathcondition.AidingPathConditionGoalFitness;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.ga.metaheuristics.mosa.jbse.JBSEManager;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testcase.execution.ExecutionTracer;
import org.evosuite.utils.ArrayUtil;
import org.evosuite.utils.LoggingUtils;

public class SushiManager extends PathConditionManager {

	private static final long serialVersionUID = -3206971271103866231L;

	private Map<BranchCoverageTestFitness, AidingPathConditionInfoManager> aidingPathConditions = new HashMap<>();	
	private List<BranchCoverageTestFitness> alreadyAidedGoals = new ArrayList<>();
	
	public SushiManager(List<TestFitnessFunction> targets) {
		super(targets);
	}

	@Override
	public Set<TestFitnessFunction> getCoveredGoals() {
		if (!ArrayUtil.contains(Properties.CRITERION, Criterion.BRANCH_WITH_AIDING_PATH_CONDITIONS)) {
			return super.getCoveredGoals();
		} else {
			Set<TestFitnessFunction> covered = new HashSet<>();
			for (TestFitnessFunction goal: super.getCoveredGoals()) {
				if (goal instanceof PathConditionCoverageGoalFitness) {
					continue; // do not count path-conditions as coverage goal: they are dynamically added/removed to aid other goals
				}
				covered.add(goal);
			}
			return covered;
		}
	}

	public Set<TestFitnessFunction> getCoveredPathConditions() {
		Set<TestFitnessFunction> coveredPCs = new HashSet<>();
		for (TestFitnessFunction goal: super.getCoveredGoals()) {
			if (goal instanceof PathConditionCoverageGoalFitness) {
				coveredPCs.add(goal);
			}
		}
		return coveredPCs;
	}


	@Override
	public void calculateFitness(TestChromosome c, GeneticAlgorithm<TestChromosome> ga) {
		super.calculateFitness(c, ga);
		
		/* SushiManager continues while there are coverage goals other than the path-condition goals */
		boolean stopSearch = true;
		for (TestFitnessFunction goal: this.getUncoveredGoals()) {
			if (!(goal instanceof PathConditionCoverageGoalFitness)) {
				stopSearch = false;
				break;
			}
		}
		if (stopSearch) {
			this.currentGoals.clear();
			this.getUncoveredGoals().clear();
		}
	}
	
	@Override
	public void restoreInstrumentationForAllGoals() {
		if (ArrayUtil.contains(Properties.CRITERION, Criterion.BRANCH_WITH_AIDING_PATH_CONDITIONS)) {
			//do not restore path condition goals, and remove the ones yet uncovered
			for (TestFitnessFunction goal: this.getUncoveredGoals()) {
				if (goal instanceof PathConditionCoverageGoalFitness) {
					this.doneWithPathCondition((PathConditionCoverageGoalFitness) goal);
				}
			}
		} else {
			super.restoreInstrumentationForAllGoals();
		}
	}

	public void manageAidingPathConditions(List<TestChromosome> bestFront, int currentIteration) {
		if (!ArrayUtil.contains(Properties.CRITERION, Criterion.BRANCH_WITH_AIDING_PATH_CONDITIONS)) {
			return;
		}
		if (Properties.APC_RATE <= 0) {
			return;
		}
		if ((currentIteration + 1) % Properties.APC_RATE != 0) { // +1 to avoid activation at iteration 0 
			return;
		}
		
		//TODO: remove? do we need counting: no? do we need removing? do we need to store apc in map?
		int numOfActiveAPC = 0;
		List<TestFitnessFunction> toRemove = new ArrayList<>();
		for (TestFitnessFunction goalWithAPC : aidingPathConditions.keySet()) {
			AidingPathConditionInfoManager apcm = aidingPathConditions.get(goalWithAPC);
			
			numOfActiveAPC += apcm.updtDoneAndCountActive();
			if (apcm.isDone()) {
				toRemove.add(goalWithAPC);
			}
		}
		for (TestFitnessFunction goalWithAPC : toRemove) {
			aidingPathConditions.remove(goalWithAPC);			
		}
		
		for (TestFitnessFunction goal :  this.getCurrentGoals()) {			
			if (!(goal instanceof BranchCoverageTestFitness)) continue;
			BranchCoverageTestFitness branchGoal = (BranchCoverageTestFitness) goal;
			if (branchGoal.getBranch() == null) continue;
						
			AidingPathConditionInfoManager apcd = aidingPathConditions.get(branchGoal);
			if (apcd == null) {
				apcd = new AidingPathConditionInfoManager(branchGoal, this);
				aidingPathConditions.put(branchGoal, apcd);
			}
			
			//TODO: here we decide if is stable for too many iterations, in case we may want to make some change
			apcd.updateAPCFitness(bestFront, currentIteration, alreadyAidedGoals);

			//TODO: Already aided stores branches that we want to skip anyway
			if (alreadyAidedGoals.contains(branchGoal)) continue; //round robin, postpone this and serve never-seen ones first

			//TODO: if there is room we may add new apc, if this branch needs some
			//if (numOfActiveAPC < Properties.APC_MAX) {
				int addedAPCs = apcd.chekForComputingAPCs(bestFront, currentIteration, alreadyAidedGoals);
				if (addedAPCs > 0) {
					numOfActiveAPC += addedAPCs;
				}
			//}
		}
		
		//TODO: we do not need this, if we remove the already-aided concept
		//round robin: now try to serve the alreadyAidedBranches in queue
		/*for (int i = 0, index = 0; i < alreadyAidedBranches.size() && numOfActiveAPC < Properties.APC_MAX; i++, index++) {
			BranchCoverageTestFitness branchGoal = alreadyAidedBranches.get(index);
			
			if (!branchManager.getCurrentGoals().contains(branchGoal)) {
				alreadyAidedBranches.remove(index--);
				continue;
			}

			AidingPathConditionManager<T> apcd = aidingPathConditions.get(branchGoal);
			if (apcd == null) {
				apcd = new AidingPathConditionManager<T>(branchGoal, pathConditionManager);
				aidingPathConditions.put(branchGoal, apcd);
			}

			int addedAPCs = apcd.chekForComputingAPCs(bestFront, currentIteration, alreadyAidedBranches);
			if (addedAPCs >= 0) {
				numOfActiveAPC += addedAPCs;
				alreadyAidedBranches.remove(index--);
			}
		}*/
	}
	
	private static class AidingPathConditionInfoManager {
		public static final int MAX_DONT_AID_ITERS = 1000;
		public static final int MAX_NO_IMPROVEMENT_ITERS_BEFORE_ADDING_APC = 200;
		public static final int MAX_NO_IMPROVEMENT_ITERS_BEFORE_SWITCHING_APC = 1000;
		public static final int MAX_NEW_APC_PER_BRANCH = 1;

		private final BranchCoverageTestFitness branchGoal;
		private double lastKnownFitnessOfBranchGoal = Double.MAX_VALUE;
		private int lastImprovementIterOfBranchGoal = -MAX_NO_IMPROVEMENT_ITERS_BEFORE_ADDING_APC;

		private AidingPathConditionGoalFitness[] apcGoals = null;
		private int nextGoalToConsiderIndex = 0;
		private int[] currentGoalIndices;
		private double[] lastKnownFitnessValuesOfApcGoals = null; 
		private int[] lastImprovementIterOfApcGoals = null;
		
		private int dontAidThisBranchResetIteration = -MAX_DONT_AID_ITERS;
		
		private boolean done = false;
		
		
		private final PathConditionManager pathConditionManager;
		
		public AidingPathConditionInfoManager(BranchCoverageTestFitness branchGoal, PathConditionManager pathConditionManager) {		
			this.branchGoal = branchGoal;
			this.pathConditionManager = pathConditionManager;
		}

		public boolean isDone() {
			return done;
		}
		
		public int updtDoneAndCountActive() {		
			int count = 0;
		
			if (apcGoals != null) {  
				for (int i = 0; i < currentGoalIndices.length; i++) {
					int currentGoalIndex = currentGoalIndices[i];
					if (!pathConditionManager.getCurrentGoals().contains(apcGoals[currentGoalIndex])) {
						done = true;
						count = 0;
					} else {
						++count;
					}
				}
			}
			/*
			if (apcGoals != null) {
				for (AidingPathConditionGoalFitness apc : apcGoals) {
					if (apc != null) {
						++count;
						if (!pathConditionManager.getCurrentGoals().contains(apc)) {
							done = true;
							count = 0;
							break;
						}
					}
				}
			} */

			if (done) {
				apcGoals = null;
			}
			
			return count;
		}
				
		public void updateAPCFitness(List<TestChromosome> bestFront, int currentIteration, List<BranchCoverageTestFitness> alreadyAidedBranches) {
			if (apcGoals == null) {
				return;
			}
			for (int i = 0; i < currentGoalIndices.length; i++) {
				int currentGoalIndex = currentGoalIndices[i];
				double currFitness = Double.MAX_VALUE;
				for (TestChromosome c : bestFront) {
					if (c.size() == 0) continue; //TODO: understand this better
					if (apcGoals[currentGoalIndex].getFitness((TestChromosome) c) < currFitness) {
						currFitness = apcGoals[currentGoalIndex].getFitness((TestChromosome) c);
					}
				}
				if (currFitness < lastKnownFitnessValuesOfApcGoals[currentGoalIndex]) {
					lastKnownFitnessValuesOfApcGoals[currentGoalIndex] = currFitness;
					lastImprovementIterOfApcGoals[currentGoalIndex] = currentIteration;
				} 
				if (currentIteration - lastImprovementIterOfApcGoals[currentGoalIndex] >= MAX_NO_IMPROVEMENT_ITERS_BEFORE_SWITCHING_APC) {
					if (apcGoals.length > MAX_NEW_APC_PER_BRANCH) {
						LoggingUtils.getEvoLogger().info("\n\n* WARNING: SWITCHING GOAL FOR APC {} AFTER {} ATTEMPTS", apcGoals[nextGoalToConsiderIndex].getPathConditionId(), MAX_NO_IMPROVEMENT_ITERS_BEFORE_SWITCHING_APC);
						pathConditionManager.doneWithPathCondition(apcGoals[currentGoalIndex]);

						LoggingUtils.getEvoLogger().info("  Scheduling New aiding path condition: {}", apcGoals[nextGoalToConsiderIndex].getPathConditionId());
						ExecutionTracer.addEvaluatorForPathCondition(apcGoals[nextGoalToConsiderIndex].getPathConditionGoal());
						pathConditionManager.getCurrentGoals().add(apcGoals[nextGoalToConsiderIndex]); 
						//pathConditionManager.getUncoveredGoals().add(apcGoals[nextGoalToConsiderIndex]);	
						this.lastKnownFitnessValuesOfApcGoals[nextGoalToConsiderIndex] = Double.MAX_VALUE;
						this.lastImprovementIterOfApcGoals[nextGoalToConsiderIndex] = currentIteration;
						currentGoalIndices[i] = nextGoalToConsiderIndex;
						nextGoalToConsiderIndex = (nextGoalToConsiderIndex + 1) % apcGoals.length;
					}
				}
			}
			/*
			for (int i = 0; i < apcGoals.length; i++) {				
				if (apcGoals[i] == null) {
					continue;
				}
				
				double currFitness = Double.MAX_VALUE;
				for (T c : bestFront) {
					if (c.size() == 0) continue; //TODO: understand this better
					if (apcGoals[i].getFitness((TestChromosome) c) < currFitness) {
						currFitness = apcGoals[i].getFitness((TestChromosome) c);
					}
				}

				if (currFitness < lastKnownFitnessValuesOfApcGoals[i]) {
					lastKnownFitnessValuesOfApcGoals[i] = currFitness;
					lastImprovementIterOfApcGoals[i] = currentIteration;
				} 
				
				if (currentIteration - lastImprovementIterOfApcGoals[i] >= MAX_NO_IMPROVEMENT_ITERS_BEFORE_REMOVING_APC) {
					LoggingUtils.getEvoLogger().info("\n\n* WARNING: REMOVING APC {} AFTER {} ATTEMPTS", apcGoals[i], MAX_NO_IMPROVEMENT_ITERS_BEFORE_REMOVING_APC);
					pathConditionManager.doneWithPathCondition(apcGoals[i]);	
					apcGoals[i] = null;
				}
			} 
			
			int count = 0;
			for (AidingPathConditionGoalFitness apc : apcGoals) {
				if (apc != null) {
					++count;
				}
			}
			if (count == 0) {
				apcGoals = null;
				alreadyAidedBranches.add(branchGoal);//dontAidThisBranchResetIteration = currentIteration; //because none worked 
			} */
			
		}
		
		public int chekForComputingAPCs(List<TestChromosome> bestFront, int currentIteration, List<BranchCoverageTestFitness> alreadyAidedBranches) {
			if (apcGoals != null || currentIteration - dontAidThisBranchResetIteration < MAX_DONT_AID_ITERS) {
				return -1;
			}
			
			//get the test with best fitness for this branchGoal
			double currentBranchGoalFitness = Double.MAX_VALUE;
			TestChromosome bestTest = null;
			for (TestChromosome c : bestFront) {
				if (c.size() == 0) continue; //TODO: understand this better
				if (branchGoal.getFitness((TestChromosome) c) < currentBranchGoalFitness) {
					currentBranchGoalFitness = branchGoal.getFitness((TestChromosome) c) ;
					bestTest = c;
				}
			}
			if (currentBranchGoalFitness >= 1.0) {
				return -1; //tests are approaching but do not yet reach the branch
			}
			if (bestTest == null) {
				LoggingUtils.getEvoLogger().info("\n\n* WARNING: NO BEST TEST FOR GOAL: {} -- CHECK THIS", branchGoal.toString());
				return -1;
			} 

			if (currentBranchGoalFitness < lastKnownFitnessOfBranchGoal) {
				lastKnownFitnessOfBranchGoal = currentBranchGoalFitness;
				lastImprovementIterOfBranchGoal = currentIteration;
			} 
			
			if (currentIteration - lastImprovementIterOfBranchGoal >= MAX_NO_IMPROVEMENT_ITERS_BEFORE_ADDING_APC) {
				LoggingUtils.getEvoLogger().info("\n\nAIDING FRONTIER GOAL: {} - fit {}", branchGoal, lastKnownFitnessOfBranchGoal);
				
				AidingPathConditionGoalFitness[] aidingPCGoals = 
						introduceAidingPathCondition(branchGoal, (TestChromosome) bestTest);
				
				if (aidingPCGoals != null) {
					this.apcGoals = aidingPCGoals;
					this.lastKnownFitnessValuesOfApcGoals = new double[aidingPCGoals.length];
					this.lastImprovementIterOfApcGoals = new int[aidingPCGoals.length];
					for (int i = 0; i < aidingPCGoals.length; i++) {
						this.lastKnownFitnessValuesOfApcGoals[i] = Double.MAX_VALUE;
						this.lastImprovementIterOfApcGoals[i] = currentIteration;
					}
					return aidingPCGoals.length;
				} else {
					dontAidThisBranchResetIteration = currentIteration; //Blacklist because we failed at computing aiding path conditions
					alreadyAidedBranches.add(branchGoal);
					return 0;
				}

			} else {
				return -1;
			}
		}
		
		public AidingPathConditionGoalFitness[] introduceAidingPathCondition(BranchCoverageTestFitness branchGoal, TestChromosome bestTest) {
			
			//Clone and minimize the test case
			/*TestChromosome minimizedTest = (TestChromosome) bestTest.clone();
			TestCaseMinimizer minimizer = new TestCaseMinimizer(branchF);
			minimizer.minimize(minimizedTest);
			if (branchF.getFitness(bestTest) != branchF.getFitness(minimizedTest)) {
				LoggingUtils.getEvoLogger().info("[JBSE] Failed to generate the path condition: mimized test fitness ("+ branchF.getFitness(minimizedTest) +") differs from original one (" + branchF.getFitness(bestTest) + ")");
				return null;
			}*/

			//Compute the new path condition goal 
			AidingPathConditionGoalFitness[] aidingPathConditionGoals = JBSEManager.computeAidingPathConditionGoal(branchGoal, /* minimizedTest*/(TestChromosome) bestTest.clone());
			
			//Schedule the new path condition goal 
			if (aidingPathConditionGoals != null) {
				nextGoalToConsiderIndex = 0;
				currentGoalIndices = new int[Math.min(MAX_NEW_APC_PER_BRANCH, aidingPathConditionGoals.length)];
				for (int i = 0; i < MAX_NEW_APC_PER_BRANCH && i < aidingPathConditionGoals.length; i++) {
					AidingPathConditionGoalFitness apc = aidingPathConditionGoals[i];
					LoggingUtils.getEvoLogger().info("  Scheduling New aiding path condition: {}", apc.toString());
					ExecutionTracer.addEvaluatorForPathCondition(apc.getPathConditionGoal());
					pathConditionManager.getCurrentGoals().add((TestFitnessFunction) apc); 
					//pathConditionManager.getUncoveredGoals().add((TestFitnessFunction) apc);
					currentGoalIndices[i] = i;
					nextGoalToConsiderIndex = (nextGoalToConsiderIndex + 1) % aidingPathConditionGoals.length;
				}
			}
			
			return aidingPathConditionGoals;
		}

	}

}

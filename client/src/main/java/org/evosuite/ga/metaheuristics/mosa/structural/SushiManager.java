package org.evosuite.ga.metaheuristics.mosa.structural;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.Properties.Criterion;
import org.evosuite.coverage.branch.BranchCoverageTestFitness;
import org.evosuite.coverage.pathcondition.AidingPathConditionGoalFitness;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.utils.ArrayUtil;
import org.evosuite.utils.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SushiManager<T extends Chromosome> extends StructuralGoalManager<T>{
	
	private static final Logger logger = LoggerFactory.getLogger(BranchFitnessGraph.class);

	protected BranchesManager<T> branchManager;
	
	protected PathConditionManager<T> pathConditionManager;
	
	private Map<BranchCoverageTestFitness, AidingPathConditionManager<T>> aidingPathConditions = new HashMap<>();	
	private List<BranchCoverageTestFitness> alreadyAidedBranches = new ArrayList<>();
	
	public SushiManager(List<FitnessFunction<T>> fitnessFunctions) {
		super(fitnessFunctions);
		
		// we first separate branches from path conditions
		List<FitnessFunction<T>> branches = new ArrayList<>();
		List<FitnessFunction<T>> pathConditions = new ArrayList<>();
		for (FitnessFunction<T> fitness : fitnessFunctions){
			if (fitness instanceof BranchCoverageTestFitness){
				branches.add(fitness);
			} else if (fitness instanceof PathConditionCoverageGoalFitness){
				pathConditions.add(fitness);
			} else {
				throw new IllegalStateException("We expect only branches and path conditions as coverage targets");
			}
		}
		branchManager = new BranchesManager<>(branches);
		pathConditionManager = new PathConditionManager<>(pathConditions, branches);
		
		logger.debug("N. of Uncovered Branches = {}", branchManager.getUncoveredGoals().size());
		logger.debug("N. of Uncovered PathCondition = {}", pathConditionManager.getUncoveredGoals().size());
		
		
		this.currentGoals.addAll(branchManager.currentGoals);
		this.currentGoals.addAll(pathConditionManager.currentGoals);
	}

	@Override
	public void calculateFitness(T c) {


		// let's calculate the fitness values for branches
		branchManager.calculateFitness(c);
		// let's calculate the fitness values for the path conditions
		pathConditionManager.prunePathConditionsByCoveredBranches(branchManager.coveredGoals); //prune path conditions that relate only to already covered branches
		pathConditionManager.calculateFitness(c);
		
		this.coveredGoals.clear();
		this.coveredGoals.putAll(branchManager.coveredGoals);
		this.coveredGoals.putAll(pathConditionManager.coveredGoals);
		
		logger.debug("N. of Uncovered Branches = {}", branchManager.getUncoveredGoals().size());
		logger.debug("N. of Uncovered PathCondition = {}", pathConditionManager.getUncoveredGoals().size());
		
		if (branchManager.uncoveredGoals.size() > 0){
			// update the uncovered goals
			this.uncoveredGoals.clear();
			this.uncoveredGoals.addAll(branchManager.getUncoveredGoals());
			this.uncoveredGoals.addAll(pathConditionManager.getUncoveredGoals());
			
			this.currentGoals.clear();
			this.currentGoals.addAll(branchManager.getCurrentGoals());
			this.currentGoals.addAll(pathConditionManager.getCurrentGoals());
		} else {
			// if all branches are covered, we don't need to continue the search
			this.uncoveredGoals.clear();
			this.currentGoals.clear();
		}
	}
	
	@Override
	public Set<T> getArchive(){
		this.archive.clear();
		this.archive.putAll(this.branchManager.archive);
		this.archive.putAll(this.pathConditionManager.archive);
		return this.archive.keySet();
	}
	
	@Override
	public void restoreInstrumentationForAllGoals() {
		pathConditionManager.restoreInstrumentationForAllGoals();
	}

	public void manageAidingPathConditions(List<T> bestFront, int currentIteration) {
		if (!ArrayUtil.contains(Properties.CRITERION, Criterion.BRANCH_WITH_AIDING_PATH_CONDITIONS)) {
			return;
		}
		
		if (Properties.APC_RATE <= 0) {
			return;
		}
		
		if ((currentIteration + 1) % Properties.APC_RATE != 0) { // +1 to avoid activation at iteration 0 
			return;
		}
		
		int numOfActiveAPC = 0;
		List<BranchCoverageTestFitness> toRemove = new ArrayList<>();
		for (BranchCoverageTestFitness branchWithAPC : aidingPathConditions.keySet()) {
			AidingPathConditionManager<T> apcm = aidingPathConditions.get(branchWithAPC);
			
			numOfActiveAPC += apcm.updtDoneAndCountActive();
			if (apcm.isDone()) {
				toRemove.add(branchWithAPC);
			}
		}
		for (BranchCoverageTestFitness branchWithAPC : toRemove) {
			aidingPathConditions.remove(branchWithAPC);			
		}
		
		for (FitnessFunction<T> goal :  branchManager.getCurrentGoals()) {			
			if (!(goal instanceof BranchCoverageTestFitness)) continue;
			
			BranchCoverageTestFitness branchGoal = (BranchCoverageTestFitness) goal;
			if (branchGoal.getBranch() == null) continue;
						
			AidingPathConditionManager<T> apcd = aidingPathConditions.get(branchGoal);
			if (apcd == null) {
				apcd = new AidingPathConditionManager<T>(branchGoal, pathConditionManager);
				aidingPathConditions.put(branchGoal, apcd);
			}
			
			apcd.updateAPCFitness(bestFront, currentIteration, alreadyAidedBranches);

			if (alreadyAidedBranches.contains(branchGoal)) continue; //round robin, postpone this and serve never-seen ones first

			if (numOfActiveAPC < Properties.APC_MAX) {
				int addedAPCs = apcd.chekForComputingAPCs(bestFront, currentIteration, alreadyAidedBranches);
				if (addedAPCs > 0) {
					numOfActiveAPC += addedAPCs;
				}
			}
		}
		
		//round robin: now try to serve the alreadyAidedBranches in queue
		for (int i = 0, index = 0; i < alreadyAidedBranches.size() && numOfActiveAPC < Properties.APC_MAX; i++, index++) {
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
		}
	}
	
	private static class AidingPathConditionManager<T extends Chromosome> {
		public static final int MAX_DONT_AID_ITERS = 2000;
		public static final int MAX_NO_IMPROVEMENT_ITERS_BEFORE_ADDING_APC = 100;
		public static final int MAX_NO_IMPROVEMENT_ITERS_BEFORE_REMOVING_APC = 1000;

		private final BranchCoverageTestFitness branchGoal;
		private double lastKnownFitnessOfBranchGoal = Double.MAX_VALUE;
		private int lastImprovementIterOfBranchGoal = -MAX_NO_IMPROVEMENT_ITERS_BEFORE_ADDING_APC;

		private AidingPathConditionGoalFitness[] apcGoals = null;
		private double[] lastKnownFitnessValuesOfApcGoals = null; 
		private int[] lastImprovementIterOfApcGoals = null;
		
		private int dontAidThisBranchResetIteration = -MAX_DONT_AID_ITERS;
		
		private boolean done = false;
		
		
		private final PathConditionManager<T> pathConditionManager;
		
		public AidingPathConditionManager(BranchCoverageTestFitness branchGoal, PathConditionManager<T> pathConditionManager) {		
			this.branchGoal = branchGoal;
			this.pathConditionManager = pathConditionManager;
		}

		public boolean isDone() {
			return done;
		}
		
		public int updtDoneAndCountActive() {		
			int count = 0;
			
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
			}

			if (done) {
				apcGoals = null;
			}
			
			return count;
		}
				
		public void updateAPCFitness(List<T> bestFront, int currentIteration, List<BranchCoverageTestFitness> alreadyAidedBranches) {
			if (apcGoals == null) {
				return;
			}
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
			}
		}
		
		public int chekForComputingAPCs(List<T> bestFront, int currentIteration, List<BranchCoverageTestFitness> alreadyAidedBranches) {
			if (apcGoals != null || currentIteration - dontAidThisBranchResetIteration < MAX_DONT_AID_ITERS) {
				return -1;
			}
			
			//get the test with best fitness for this branchGoal
			double currentBranchGoalFitness = Double.MAX_VALUE;
			T bestTest = null;
			for (T c : bestFront) {
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
						pathConditionManager.introduceAidingPathCondition(branchGoal, (TestChromosome) bestTest);
				
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

	}

}

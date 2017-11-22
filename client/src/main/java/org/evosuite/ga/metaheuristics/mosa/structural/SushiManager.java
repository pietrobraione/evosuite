package org.evosuite.ga.metaheuristics.mosa.structural;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.evosuite.coverage.branch.BranchCoverageTestFitness;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SushiManager<T extends Chromosome> extends StructuralGoalManager<T>{
	
	private static final Logger logger = LoggerFactory.getLogger(BranchFitnessGraph.class);

	protected BranchesManager<T> branchManager;
	
	protected PathConditionManager<T> pathConditionManager;
	
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
		pathConditionManager.pruneByCoveredBranches(branchManager.coveredGoals); //prune path conditions that relate only to already covered branches
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

}

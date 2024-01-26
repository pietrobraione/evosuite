/**
 * Copyright (C) 2011,2012 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 * 
 * This file is part of EvoSuite.
 * 
 * EvoSuite is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Public License as published by the Free Software Foundation,
 * either version 3 of the License, or (at your option) any later version.
 * 
 * EvoSuite is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU Public License for more details.
 * 
 * You should have received a copy of the GNU Public License along with
 * EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.coverage.pathcondition;

import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.utils.LoggingUtils;

import java.util.ArrayList;

import org.evosuite.coverage.branch.BranchCoverageTestFitness;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;

/**
 * Fitness function for a single test on a single path condition
 * 
 * @author G. Denaro
 */
public class ApcGoalFitness extends PathConditionCoverageGoalFitness { /*SUSHI: Aiding path conditions*/

	private static final long serialVersionUID = -2217446504263861265L;

	/** Target pathCondition */
	private final BranchCoverageTestFitness branchGoal;
	private final double initialBranchFitness;

	/**
	 * Constructor - fitness is specific to a path condition
	 * 
	 */
	public ApcGoalFitness(BranchCoverageTestFitness brGoal, double initialBranchFitness, PathConditionCoverageGoal pcGoal) throws IllegalArgumentException {
		super(pcGoal);
		LoggingUtils.getEvoLogger().info("[EVOSUITE] NEW APC: {} FOR {}!!!", pcGoal, brGoal);
		if (brGoal == null){
			throw new IllegalArgumentException("goal cannot be null");
		}
		if (!(super.getPathConditionGoal().getEvaluator() instanceof IApcEvaluator)) {
			throw new IllegalArgumentException("the evaluator of the path condition associated with the ApcGoal must impelement org.evosuite.coverage.pathcondition.IApcEvaluator");
		}

		this.branchGoal = brGoal;
		this.initialBranchFitness = initialBranchFitness;
	}

	public BranchCoverageTestFitness getBranchGoal() {
		return branchGoal;
	}

	/**
	 * @return the evaluator of the path condition associated with this ApcGoal
	 */
	public IApcEvaluator getEvaluator() {
		return (IApcEvaluator) super.getPathConditionGoal().getEvaluator();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * Calculate fitness as computed by the path condition evaluator during test execution
	 */
	@Override
	public double getFitness(TestChromosome individual, ExecutionResult result) {
		double branchFitness = branchGoal.getFitness(individual, result);
		double fitness;
		if(branchFitness == 0) {
			LoggingUtils.getEvoLogger().info("\n\n[EVOSUITE] APC GOAL FITNESS ({}) SATISFIED CORRESPONDING BRANCH: {}!!!", super.getFitness(individual, result), this);
			fitness = 0d;
		} else if (branchFitness >= 1) {
			fitness = Double.MAX_VALUE;
		} else if (branchFitness < initialBranchFitness) {
			LoggingUtils.getEvoLogger().info("\n\n[EVOSUITE] REMOVING APC GOAL BECAUSE BRANCH FITNESS HAS IMPROVED: {}", this);
			fitness = 0d;
		} else {
			double pcFitness = super.getFitness(individual, result);
			fitness = pcFitness + branchFitness;
			
			// As this evaluation hits the target branch, we update the converging-clause information by using the feedback from this test case
			IApcEvaluator evaluator = getEvaluator();
			ArrayList<Object> feedback = result.getTrace().getPathConditionFeedbacks().get(getPathConditionGoal().getPathConditionId());
			//if feedback == null: possibly this test case did not hit the computation of this path-condition-fitness, and thus there is no feedback
			if (feedback != null) { 	
				evaluator.processFeedback(feedback);
			} 

			// If there is an undeclared exception it is a failing test
			if (result.hasTimeout() || result.hasTestException())
				fitness += 1d;
		}
		
		logger.debug("Aiding path condition fitness = " + fitness);

		updateIndividual(individual, fitness);
		return fitness;
	}


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Aiding path condition for branch " + branchGoal.toString() + 
				": " + super.toString();
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((branchGoal == null) ? 0 : branchGoal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ApcGoalFitness other = (ApcGoalFitness) obj;
		if (branchGoal == null) {
			if (other.branchGoal != null)
				return false;
		} else if (!branchGoal.equals(other.branchGoal))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.evosuite.testcase.TestFitnessFunction#getTargetClass()
	 */
	@Override
	public String getTargetClass() {
		return branchGoal.getTargetClass();
	}

	/* (non-Javadoc)
	 * @see org.evosuite.testcase.TestFitnessFunction#getTargetMethod()
	 */
	@Override
	public String getTargetMethod() {
		return branchGoal.getTargetMethod();
	}

	@Override
	public int compareTo(TestFitnessFunction other) {
		return 0; /* No ordering defined*/
	}

}

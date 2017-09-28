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

import java.util.ArrayList;
import java.util.List;

import org.evosuite.Properties;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testsuite.AbstractFitnessFactory;
import org.evosuite.testsuite.TestSuiteFitnessFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * PathCondtionCoverageFactory class.
 * </p>
 * 
 * @author G. Denaro
 */
public class PathConditionCoverageFactory extends AbstractFitnessFactory<PathConditionCoverageGoalFitness> { /*SUSHI: Path condition fitness*/

	private static final Logger logger = LoggerFactory.getLogger(PathConditionCoverageFactory.class);

	private static List<PathConditionCoverageGoalFitness> coverageGoals = null;
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.evosuite.coverage.TestCoverageFactory#getCoverageGoals()
	 */
	/** {@inheritDoc} */
	@Override
	public List<PathConditionCoverageGoalFitness> getCoverageGoals() {
		if (coverageGoals == null) {
			long start = System.currentTimeMillis();
			List<PathConditionCoverageGoalFitness> goals = new ArrayList<PathConditionCoverageGoalFitness>();

			int id = 0;
			for (String pathCondition : Properties.PATH_CONDITION) {
				String parts[] = Properties.pathConditionSplitClassMethodEvaluator(pathCondition);
				goals.add(createPathConditionCoverageTestFitness(id, parts[0], parts[1], parts[2]));
			}

			goalComputationTime = System.currentTimeMillis() - start;
			
			coverageGoals = goals;
		}
		return coverageGoals;
	}

	public TestFitnessFunction getPathConditionCoverageTestFitness() {
		List<PathConditionCoverageGoalFitness> goals = getCoverageGoals();
		return new PathConditionCoverageTestFitness(goals);
	}

	public TestSuiteFitnessFunction getPathConditionCoverageSuiteFitness() {
		List<PathConditionCoverageGoalFitness> goals = getCoverageGoals();
		return new PathConditionCoverageSuiteFitness(goals);
	}


	/**
	 * Create a fitness function for path condition coverage aimed at executing the
	 * path condition the identified path condition.
	 * 
	 * @return a {@link org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness}
	 *         object.
	 */
	private PathConditionCoverageGoalFitness createPathConditionCoverageTestFitness(int pathConditionId, String className, String methodName, String evaluatorClassName) {

		return new PathConditionCoverageGoalFitness(new PathConditionCoverageGoal(pathConditionId, className, methodName, evaluatorClassName));
	}
}

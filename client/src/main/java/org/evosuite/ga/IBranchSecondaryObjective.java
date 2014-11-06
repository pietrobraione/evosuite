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
package org.evosuite.ga;

import org.evosuite.coverage.ibranch.IBranchSuiteFitness;
import org.evosuite.testcase.ExecutableChromosome;
import org.evosuite.testsuite.AbstractTestSuiteChromosome;

/**
 * <p>
 * IBranchSecondaryObjective class.
 * </p>
 *
 * @author mattia
 */
public class IBranchSecondaryObjective extends
		SecondaryObjective<AbstractTestSuiteChromosome<? extends ExecutableChromosome>> {

	private IBranchSuiteFitness ff;
	private static final long serialVersionUID = 7211557650429998223L;

	public IBranchSecondaryObjective(IBranchSuiteFitness fitness) {
		ff = fitness;
	}

	@Override
	public int compareChromosomes(
			AbstractTestSuiteChromosome<? extends ExecutableChromosome> chromosome1,
			AbstractTestSuiteChromosome<? extends ExecutableChromosome> chromosome2) {

		ff.getFitness(chromosome1);
		ff.getFitness(chromosome2);

		logger.debug("Comparing sizes: " + chromosome1.getFitness(ff) + " vs "
				+ chromosome2.getFitness(ff));
		int i = (int) Math.signum(chromosome1.getFitness(ff) - chromosome2.getFitness(ff));
		
//		logger.error(i+ " " +chromosome1.getFitness(ff)+ " " + chromosome2.getFitness(ff));
		return i; 
	}

	@Override
	public int compareGenerations(
			AbstractTestSuiteChromosome<? extends ExecutableChromosome> parent1,
			AbstractTestSuiteChromosome<? extends ExecutableChromosome> parent2,
			AbstractTestSuiteChromosome<? extends ExecutableChromosome> child1,
			AbstractTestSuiteChromosome<? extends ExecutableChromosome> child2) {
		logger.debug("Comparing sizes: " + parent1.size() + ", " + parent1.size() + " vs "
				+ child1.size() + ", " + child2.size());

		ff.getFitness(parent1);
		ff.getFitness(parent2);
		ff.getFitness(child1);
		ff.getFitness(child2);

		double minParents = Math.min(parent1.getFitness(ff), parent2.getFitness(ff));
		double minChildren = Math.min(child1.getFitness(ff), child2.getFitness(ff));
		if (minParents < minChildren) {
			return -1;
		}
		if (minParents > minChildren) {
			return 1;
		}
		return 0;
	}

}

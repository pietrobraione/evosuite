/*
 * Copyright (C) 2010-2018 Gordon Fraser, Andrea Arcuri and EvoSuite
 * contributors
 *
 * This file is part of EvoSuite.
 *
 * EvoSuite is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3.0 of the License, or
 * (at your option) any later version.
 *
 * EvoSuite is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with EvoSuite. If not, see <http://www.gnu.org/licenses/>.
 */
package org.evosuite.testsuite;

import java.util.List;

import org.evosuite.Properties;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.populationlimit.PopulationLimit;


/**
 * <p>StatementsPopulationLimit class.</p>
 *
 * @author fraser
 */
public class StatementsPopulationLimit<T extends AbstractTestSuiteChromosome<T,?>>
		implements PopulationLimit<T> {

	private static final long serialVersionUID = 4794704248615412859L;

	/* (non-Javadoc)
	 * @see org.evosuite.ga.PopulationLimit#isPopulationFull(java.util.List)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean isPopulationFull(List<T> population) {
		int numStatements = population.stream()
				.mapToInt(T::totalLengthOfTestCases)
				.sum();
		return numStatements >= Properties.POPULATION;
	}

}

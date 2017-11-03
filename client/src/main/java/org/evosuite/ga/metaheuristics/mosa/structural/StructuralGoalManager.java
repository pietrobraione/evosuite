package org.evosuite.ga.metaheuristics.mosa.structural;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.TestSuiteGenerator;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoal;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.junit.writer.TestSuiteWriter;
import org.evosuite.rmi.ClientServices;
import org.evosuite.rmi.service.ClientState;
import org.evosuite.testcase.ConstantInliner;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.TestCaseMinimizer;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.evosuite.utils.LoggingUtils;

import junit.framework.TestSuite;

public abstract class StructuralGoalManager<T extends Chromosome> {

	/** Set of yet to cover goals **/
	protected Set<FitnessFunction<T>> uncoveredGoals;

	/** Set of goals currently used as objectives **/
	protected Set<FitnessFunction<T>> currentGoals;

	/** Map of covered goals **/
	protected Map<FitnessFunction<T>, T> coveredGoals;

	/** Map of test to archive and corresponding covered targets*/
	protected Map<T, List<FitnessFunction<T>>> archive;

	protected StructuralGoalManager(List<FitnessFunction<T>> fitnessFunctions){
		uncoveredGoals = new HashSet<FitnessFunction<T>>(fitnessFunctions.size());
		currentGoals = new HashSet<FitnessFunction<T>>(fitnessFunctions.size());
		coveredGoals = new HashMap<FitnessFunction<T>, T>(fitnessFunctions.size());
		archive = new HashMap<T, List<FitnessFunction<T>>>();
	}

	/**
	 * Update the set of covered goals and the set of current goals (actual objectives)
	 * @param population list of TestChromosome
	 * @return covered goals along with the corresponding test case
	 */
	public abstract void calculateFitness(T c);

	public Set<FitnessFunction<T>> getUncoveredGoals() {
		return uncoveredGoals;
	}

	public Set<FitnessFunction<T>> getCurrentGoals() {
		return currentGoals;
	}

	public Map<FitnessFunction<T>, T> getCoveredGoals() {
		return coveredGoals;
	}

	protected boolean isAlreadyCovered(FitnessFunction<T> target){
		if (uncoveredGoals.size() < coveredGoals.keySet().size()){
			if (!uncoveredGoals.contains(target))
				return true;
		} else {
			if (coveredGoals.keySet().contains(target))
				return true;
		}
		return false;
	}

	protected void updateCoveredGoals(FitnessFunction<T> f, T tc) {
		// the next two lines are needed since that coverage information are used
		// during EvoSuite post-processing
		TestChromosome tch = (TestChromosome) tc;
		tch.getTestCase().getCoveredGoals().add((TestFitnessFunction) f);

		// update covered targets
		boolean toArchive = false;
		T best = coveredGoals.get(f);
		if (best == null){
			toArchive = true;
			coveredGoals.put(f, tc);
			uncoveredGoals.remove(f);
			currentGoals.remove(f);
		} else {
			double bestSize = best.size();
			double size = tc.size();
			if (size < bestSize && size > 1){
				toArchive = true;
				coveredGoals.put(f, tc);
				archive.get(best).remove(f);
				if (archive.get(best).size() == 0)
					archive.remove(best);
			}
		}

		// update archive
		if (toArchive){
			List<FitnessFunction<T>> coveredTargets = archive.get(tc);
			if (coveredTargets == null){
				List<FitnessFunction<T>> list = new ArrayList<FitnessFunction<T>>();
				list.add(f);
				archive.put(tc, list);
			} else {
				coveredTargets.add(f);
			}
		}
	}

	public Set<T> getArchive(){
		return this.archive.keySet();
	}

	public void restoreInstrumentationForAllGoals() { //GIO: TODO
		//Default behavior: do nothing
	}

}

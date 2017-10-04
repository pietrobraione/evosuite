package org.evosuite.ga.metaheuristics.mosa.structural;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.testcase.TestCase;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.testcase.execution.TestCaseExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This Class manages the path condition goals to consider during the search
 * @author Giovanni Denaro
 * 
 */
public class PathConditionManager<T extends Chromosome> extends StructuralGoalManager<T>{

	private static final Logger logger = LoggerFactory.getLogger(PathConditionManager.class);

	/**
	 * Constructor used to initialize the set of uncovered goals, and the initial set
	 * of goals to consider as initial contrasting objectives
	 * @param fitnessFunctions List of all FitnessFunction<T>
	 */
	public PathConditionManager(List<FitnessFunction<T>> fitnessFunctions){
		super(fitnessFunctions);
		// initialize uncovered goals
		uncoveredGoals.addAll(fitnessFunctions);
		// initialize current goals
		this.currentGoals.addAll(fitnessFunctions);
	}

	public void calculateFitness(T c){
		// run the test
		TestCase test = ((TestChromosome) c).getTestCase();
		ExecutionResult result = TestCaseExecutor.runTest(test);
		((TestChromosome) c).setLastExecutionResult(result);
		c.setChanged(false);
		
		if (result.hasTimeout() || result.hasTestException()){
			for (FitnessFunction<T> f : currentGoals)
					c.setFitness(f, Double.MAX_VALUE);
			return;
		}

		// We update the archive and the set of currents goals
		LinkedList<FitnessFunction<T>> targets = new LinkedList<FitnessFunction<T>>();
		targets.addAll(this.currentGoals);

		while (targets.size()>0){
			FitnessFunction<T> fitnessFunction = targets.poll();
			double value = fitnessFunction.getFitness(c);
			if (value == 0.0) {
				updateCoveredGoals(fitnessFunction, c);
			}
		}
		currentGoals.removeAll(coveredGoals.keySet());
	}

}

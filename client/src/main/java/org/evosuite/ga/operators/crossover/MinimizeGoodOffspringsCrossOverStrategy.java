package org.evosuite.ga.operators.crossover;

import java.util.Map;

import org.evosuite.Properties;
import org.evosuite.coverage.pathcondition.PathConditionCoverageFactory;
import org.evosuite.coverage.pathcondition.PathConditionCoverageSuiteFitness;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.testcase.TestCaseMinimizer;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testsuite.TestSuiteFitnessFunction;
import org.evosuite.utils.LoggingUtils;

public class MinimizeGoodOffspringsCrossOverStrategy implements CrossOverListener { /*SUSHI: Minimization*/
 
	private Chromosome lastAttemptParent1 = null; 
	private Chromosome lastAttemptParent2 = null; 	
	
	private TestFitnessFunction testFitnessFunction = null;
	private TestCaseMinimizer minimizer = null; 
	private int minimizationCount = 0;
	private boolean afterMutation = false; 

	public int getNumberOfMinimizerCalls() {
		return minimizationCount;
	}


	@Override
	public void newEvolveStep() {
		if (!Properties.USE_MINIMIZER_DURING_CROSSOVER) return;

		//reset parents
		lastAttemptParent1 = null; 
		lastAttemptParent2 = null; 
		afterMutation = false; 

	}

	@Override
	public void crossOverBegin(Chromosome parent1, Chromosome parent2) {
		if (!Properties.USE_MINIMIZER_DURING_CROSSOVER) return;

		lastAttemptParent1 = parent1.clone();
		lastAttemptParent2 = parent2.clone();		
	}

	
	@Override
	public void crossOverException(Chromosome parent1, Chromosome parent2) {
		if (!Properties.USE_MINIMIZER_DURING_CROSSOVER) return;

		//reset parents, since they refer to a failed application of the crossover operator
		lastAttemptParent1 = null; 
		lastAttemptParent2 = null; 
	}

	@Override
	public void searchStarted(GeneticAlgorithm<?> algorithm) { 
		if (!Properties.USE_MINIMIZER_DURING_CROSSOVER) return;
		
		testFitnessFunction = null;
		
		try {
			testFitnessFunction = (TestFitnessFunction) algorithm.getFitnessFunction();
		} catch (ClassCastException e) {				
			FitnessFunction<?> ff = algorithm.getFitnessFunction();
			if (ff instanceof TestSuiteFitnessFunction) {
				if (ff instanceof PathConditionCoverageSuiteFitness) {
					testFitnessFunction = new PathConditionCoverageFactory().getPathConditionCoverageTestFitness();
				}
			} 
			//else if... TODO: Implement here other cases in which we can unbox a TestFitnessFunction out of a TestSuiteFitnessFunction			
		}

		if (testFitnessFunction == null) {
			LoggingUtils.getEvoLogger().info("* Cannot use minimizer: miss a suitable **Test**FitnessFunction. Deactivating this option");
			Properties.USE_MINIMIZER_DURING_CROSSOVER = false;
		} else {
				minimizer = new TestCaseMinimizer(testFitnessFunction);
		}
	}

	@Override
	public void fitnessEvaluation(Chromosome individual) {
		if (!Properties.USE_MINIMIZER_DURING_CROSSOVER) return;
			
		if (!afterMutation) 
			return;
		
		if (lastAttemptParent1 == null || lastAttemptParent2 == null) 
			return;
			
		Chromosome bestParent = (lastAttemptParent1.getFitness() < lastAttemptParent2.getFitness()) ? lastAttemptParent1 : lastAttemptParent2;

		if (individual.getFitness() < bestParent.getFitness() && 
				((TestChromosome) individual).getTestCase().size() > Properties.CHROMOSOME_LENGTH) {
			
			//LoggingUtils.getEvoLogger().info("-- BEGIN Minimize: ");
			//LoggingUtils.getEvoLogger().info(bestOffspring.toString());
			Map<FitnessFunction<?>, Double> fitnessValues = individual.getFitnessValues();
			int fitnessValuesCardinality = fitnessValues.size();
			double f1 = individual.getFitness();
			minimizer.minimize((TestChromosome) individual);
			int newValuesCount = fitnessValues.size() - fitnessValuesCardinality;
			if (newValuesCount > 0) {
				fitnessValues.remove(testFitnessFunction);
			}
			minimizationCount++;
			//LoggingUtils.getEvoLogger().info(bestOffspring.toString());
			//LoggingUtils.getEvoLogger().info("-- END Minimize: ");
		}
	}

	@Override
	public void iteration(GeneticAlgorithm<?> algorithm) { /*nothing to do */ }

	@Override
	public void modification(Chromosome individual) { 
		afterMutation  = true;
	}

	@Override
	public void searchFinished(GeneticAlgorithm<?> algorithm) { /*nothing to do*/ }

	@Override
	public void crossOverDone(Chromosome parent1, Chromosome parent2) { /*nothing to do */ }
		
}

package org.evosuite.ga.operators.crossover;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.TestSuiteGenerator;
import org.evosuite.coverage.TestFitnessFactory;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.ga.metaheuristics.mosa.DynaMOSA;
import org.evosuite.testcase.TestCaseMinimizer;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testcase.execution.ExecutionResult;
import org.evosuite.utils.LoggingUtils;

public class MinimizeGoodOffspringsCrossOverStrategy implements CrossOverListener { /*SUSHI: Minimization*/
	private String identifier = null;
	
	private GeneticAlgorithm<?> algorithm; 
	private Set<TestFitnessFunction> goals = null;

	private int minimizationCount = 0;

	public int getNumberOfMinimizerCalls() {
		return minimizationCount;
	}

	public MinimizeGoodOffspringsCrossOverStrategy(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public void searchStarted(GeneticAlgorithm<?> algorithm) { 
		if (!Properties.USE_MINIMIZER_DURING_CROSSOVER) return;
		
		this.algorithm = algorithm;
		
		List<TestFitnessFactory<? extends TestFitnessFunction>> testFitnessFactories = TestSuiteGenerator.getFitnessFactories();
        goals = new HashSet<>();
        for (TestFitnessFactory<?> ff : testFitnessFactories) {
            goals.addAll(ff.getCoverageGoals());
        }

		if (goals == null || goals.isEmpty()) {
			LoggingUtils.getEvoLogger().info("* Cannot use minimizer: miss a suitable **Test**FitnessFunction. Deactivating this option");
			Properties.USE_MINIMIZER_DURING_CROSSOVER = false;
		}
	}

	@Override
	public void inNextGeneration(Chromosome individual) {
		if (!Properties.USE_MINIMIZER_DURING_CROSSOVER) return;

		if (((TestChromosome) individual).getTestCase().size() <= Properties.CHROMOSOME_LENGTH)
			return;	
				
		//1. compute the subset of goals with respect to which we want to minimize
		Map<TestFitnessFunction, Double> improvedGoals = new HashMap<>();

		Set<FitnessFunction<TestChromosome>> coveredGoals = ((DynaMOSA<TestChromosome>) algorithm).getCoveredGoals();
		goals.removeAll(coveredGoals);
		
		for (TestFitnessFunction g :	 goals) {
			Double individualFitness = individual.getFitnessValues().get(g);
			if (individualFitness == null) {
				continue; // some goals may still be beyond the current frontier of DynaMosa
				//throw new RuntimeException("new generation offspring misses a fitness value for: " + g);
			}
			improvedGoals.put(g, individualFitness);
		}
		
		if (!improvedGoals.isEmpty()) {
		
			//2. stub a temporary local fitnessFunction out of improvedovedGoals. This must return the sum of the improvedovedGoals, but MAXVAL if any goal out of improvedovedGoals is worsened wrt the current value
			TestFitnessFunction minimizeFitness = new ImprovementTestFitnessFunction(improvedGoals);
		
			//3. instantiate and call the minimizer (as below) by using the above fitness function
            TestCaseMinimizer minimizer = new TestCaseMinimizer(minimizeFitness);
            		
			//LoggingUtils.getEvoLogger().info("-- BEGIN Minimize: individual {} of size {}", System.identityHashCode(individual), individual.size());

			minimizer.minimize((TestChromosome) individual);
			individual.getFitnessValues().remove(minimizeFitness);
			/*if (minimizeFitness.getFitness(copy) < minimizeFitness.getFitness((TestChromosome) individual)) {
				((TestChromosome) individual).setTestCase(testCase); = copy;
			}*/
			minimizationCount++;
			//LoggingUtils.getEvoLogger().info(individual.toString());
			//LoggingUtils.getEvoLogger().info("-- END Minimize: individual {} of size {}", System.identityHashCode(individual), individual.size());
		}
		
	}
	
	private static class ImprovementTestFitnessFunction extends TestFitnessFunction {
		private Map<TestFitnessFunction, Double> improvedGoals;

		public ImprovementTestFitnessFunction(Map<TestFitnessFunction, Double> improvedGoals) {
			this.improvedGoals = improvedGoals;
		}

		@Override
		public double getFitness(TestChromosome individual, ExecutionResult result) {
			double f = 0d;
			for (TestFitnessFunction g : improvedGoals.keySet()) {
				double gFitness = g.getFitness(individual, result);
				if (gFitness > improvedGoals.get(g)) {
					return Double.MAX_VALUE;
				}
				f += gFitness;
			}
			return f;
		}

		@Override
		public int compareTo(TestFitnessFunction other) {
			throw new RuntimeException("UNIMPLEMENTED: SHOULD NOT BE EVER CALLED");
		}

		@Override
		public int hashCode() {
			return System.identityHashCode(this);
		}

		@Override
		public boolean equals(Object other) {
			return this == other; 
		}

		@Override
		public String getTargetClass() {
			throw new RuntimeException("UNIMPLEMENTED: SHOULD NOT BE EVER CALLED");
		}

		@Override
		public String getTargetMethod() {
			throw new RuntimeException("UNIMPLEMENTED: SHOULD NOT BE EVER CALLED");
		}		
	}
	

	@Override
	public void iteration(GeneticAlgorithm<?> algorithm) { /*nothing to do */ 
		//LoggingUtils.getEvoLogger().info("* {} called {} times", "" + this.identifier, this.minimizationCount);
	}

	@Override
	public void fitnessEvaluation(Chromosome individual) { /*nothing to do*/ }

	@Override
	public void modification(Chromosome individual) { /*nothing to do*/ }

	@Override
	public void searchFinished(GeneticAlgorithm<?> algorithm) { /*nothing to do*/ }

	@Override
	public void crossOverBegin(Chromosome parent1, Chromosome parent2) { /*nothing to do */ }
	
	@Override
	public void crossOverException(Chromosome parent1, Chromosome parent2) { /*nothing to do */ }

	@Override
	public void crossOverDone(Chromosome parent1, Chromosome parent2) { /*nothing to do */ }

}

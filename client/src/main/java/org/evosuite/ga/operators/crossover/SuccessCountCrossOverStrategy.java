package org.evosuite.ga.operators.crossover;

import java.util.Hashtable;
import java.util.Map;

import org.evosuite.Properties;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;

public class SuccessCountCrossOverStrategy implements CrossOverListener { /*SUSHI: Statistics */

	private final String operatorName;
	
	private int bestSuccessCounterCrossOver = 0;
	private int bestSuccessCounterMutation = 0;
	private int overallSuccessCounterCrossOver = 0;
	private int overallSuccessCounterMutation = 0;
	
	private double bestFitness;
	
	private boolean success4bestAlreadyCountedCrossOver;
	private boolean success4bestAlreadyCountedMutation;
	
	private double fitnessBeforeCrossOver;
	private boolean crossOverCompletedSuccessfully;

	private double fitnessBeforeMutation;
	private boolean mutationDone;
	

	
	public SuccessCountCrossOverStrategy(String operatorName) {
		this.operatorName = operatorName;
	}

	public Map<String, Integer> getIterationBestSuccessCountsCrossOver() {
		 Map<String, Integer> ret = new Hashtable<String, Integer>();
		 
		 ret.put("" + operatorName, bestSuccessCounterCrossOver);
		 
		 return ret;
	}

	public Map<String, Integer> getIterationBestSuccessCountsMutation() {
		 Map<String, Integer> ret = new Hashtable<String, Integer>();
		 
		 ret.put("" + operatorName, bestSuccessCounterMutation);
		 
		 return ret;
	}

	public Map<String, Integer> getOverallSuccessCountsCrossOver() {
		 Map<String, Integer> ret = new Hashtable<String, Integer>();
		 
		 ret.put("" + operatorName, overallSuccessCounterCrossOver);
		 
		 return ret;
	}

	public Map<String, Integer> getOverallSuccessCountsMutation() {
		 Map<String, Integer> ret = new Hashtable<String, Integer>();
		 
		 ret.put("" + operatorName, overallSuccessCounterMutation);
		 
		 return ret;
	}

	@Override
	public void iteration(GeneticAlgorithm<?> algorithm)  {
		if (!Properties.SUSHI_STATISTICS) return;
		
		bestFitness = algorithm.getBestIndividual().getFitness();
		success4bestAlreadyCountedCrossOver = false;
		success4bestAlreadyCountedMutation = false;
	}

	@Override
	public void newEvolveStep() {
		if (!Properties.SUSHI_STATISTICS) return;
		
		crossOverCompletedSuccessfully = false;
		mutationDone = false;
		fitnessBeforeCrossOver = -1;
		fitnessBeforeMutation = -1;
	}

	@Override
	public void crossOverBegin(Chromosome parent1, Chromosome parent2) {
		if (!Properties.SUSHI_STATISTICS) return;

		double f1 = parent1.getFitness();
		double f2 = parent2.getFitness();
		fitnessBeforeCrossOver = f1 < f2 ? f1 : f2;
	}

	@Override
	public void crossOverDone(Chromosome parent1, Chromosome parent2) {
		if (!Properties.SUSHI_STATISTICS) return;

		crossOverCompletedSuccessfully = true;				
	}

	@Override
	public void modification(Chromosome individual)  { 
		if (!Properties.SUSHI_STATISTICS) return;
		
		mutationDone = true;
		double f = individual.getFitness();
		if (fitnessBeforeMutation < 0 || f < fitnessBeforeMutation) 
			fitnessBeforeMutation = f;
	}
	

	@Override
	public void crossOverException(Chromosome parent1, Chromosome parent2) { }

	
	@Override
	public void fitnessEvaluation(Chromosome individual) {
		if (!Properties.SUSHI_STATISTICS) return;
		
		double f = individual.getFitness();
		
		if (mutationDone) {
			if (f < fitnessBeforeMutation) {
				++overallSuccessCounterMutation;
				if (f < bestFitness) {
					bestFitness = f;
					if (!success4bestAlreadyCountedMutation) {
						++bestSuccessCounterMutation;
						success4bestAlreadyCountedMutation = true;
					}
				}				
			}
		} else if (crossOverCompletedSuccessfully) {
			if (f < fitnessBeforeCrossOver) {
				++overallSuccessCounterCrossOver;
				if (f < bestFitness) {
					bestFitness = f;
					if (!success4bestAlreadyCountedCrossOver) {
						++bestSuccessCounterCrossOver;
						success4bestAlreadyCountedCrossOver = true;
					}
				}
			}
		}

	}

	@Override
	public void searchStarted(GeneticAlgorithm<?> algorithm) { /*nothing to do */ }

	@Override
	public void searchFinished(GeneticAlgorithm<?> algorithm)  { /*nothing to do */ }
	
}

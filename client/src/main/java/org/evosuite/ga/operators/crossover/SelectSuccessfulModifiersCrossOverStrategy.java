package org.evosuite.ga.operators.crossover;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.coverage.pathcondition.PathConditionCoverageFactory;
import org.evosuite.coverage.pathcondition.PathConditionCoverageSuiteFitness;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.ga.metaheuristics.GeneticAlgorithm;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testcase.TestFitnessFunction;
import org.evosuite.testsuite.TestSuiteFitnessFunction;
import org.evosuite.utils.EqualityByIdentityWrapper;
import org.evosuite.utils.LoggingUtils;
import org.evosuite.utils.generic.GenericMethod;

public class SelectSuccessfulModifiersCrossOverStrategy implements CrossOverListener {
	private TestFitnessFunction testFitnessFunction = null;
	
	private final Map<EqualityByIdentityWrapper<TestChromosome>, List<ModifierRecord>> lastAppliedModifiers = new Hashtable<EqualityByIdentityWrapper<TestChromosome>, List<ModifierRecord>>();

	private Set<GenericMethod> successfulModifiers = new HashSet<GenericMethod>(); /*Local Singleton: share the identified modifiers globally, for use in local search operators*/ 

	public Set<GenericMethod> getSuccessfulModifiers() {
		Set<GenericMethod> ret = new HashSet<GenericMethod>();
		ret.addAll(successfulModifiers);
		return ret;
	}

	@Override
	public void newEvolveStep() {
		if (!Properties.SUSHI_MODIFIERS_LOCAL_SEARCH) return;

		lastAppliedModifiers.clear();		
	}

	@Override
	public void crossOverBegin(Chromosome parent1, Chromosome parent2) {
		if (!Properties.SUSHI_MODIFIERS_LOCAL_SEARCH) return;

		recordApplieddModifier((TestChromosome) parent1, (TestChromosome) parent1, null); //record intial state of parents
		recordApplieddModifier((TestChromosome) parent2, (TestChromosome) parent2, null); //record intial state of parents

	}
	
	@Override
	public void crossOverException(Chromosome parent1, Chromosome parent2) {
		if (!Properties.SUSHI_MODIFIERS_LOCAL_SEARCH) return;

		lastAppliedModifiers.clear();			
	}
	
	@Override
	public void crossOverDone(Chromosome parent1, Chromosome parent2) { /*nothing to do*/ }


	
	// record information that a modifier has been just added to an offspring, in its current version
	public void recordApplieddModifier(TestChromosome originalParent, TestChromosome currentOffspring, GenericMethod appliedModifier) {
		if (!Properties.SUSHI_MODIFIERS_LOCAL_SEARCH) return;

		List<ModifierRecord> modifiers = lastAppliedModifiers.get(new EqualityByIdentityWrapper<TestChromosome>(originalParent));

		if (modifiers == null) {
			modifiers = new ArrayList<ModifierRecord>();
			lastAppliedModifiers.put(new EqualityByIdentityWrapper<TestChromosome>(originalParent), modifiers);
		}
		
		ModifierRecord record = new ModifierRecord((TestChromosome) currentOffspring, appliedModifier); 
		if (appliedModifier != null) 
			record.test.setChanged(true);
		modifiers.add(record);
	}
	
	//Implements methods of SearchListener
	
	@Override
	public void searchStarted(GeneticAlgorithm<?> algorithm) {
		if (!Properties.SUSHI_MODIFIERS_LOCAL_SEARCH) return;
		
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
			LoggingUtils.getEvoLogger().info("* Cannot do modifiers local search: miss a suitable **Test**FitnessFunction. Deactivating this option");
			Properties.SUSHI_MODIFIERS_LOCAL_SEARCH = false;
		} 
	}

	@Override
	public void fitnessEvaluation(Chromosome individual) {
		if (!Properties.SUSHI_MODIFIERS_LOCAL_SEARCH) return;
			
		List<ModifierRecord> modifiers = lastAppliedModifiers.get(new EqualityByIdentityWrapper<TestChromosome>((TestChromosome) individual));
		if (modifiers == null || modifiers.isEmpty())
			return; //no ModifierInfo for this offspring
			
		TestChromosome correspondingParent = modifiers.get(0).test;
			
		if (individual.getFitness() < correspondingParent.getFitness()) { 
			modifiers.remove(0);
			storeSuccessfulModifiersForLS(correspondingParent.getFitness(), modifiers);
		}
		
		//done with this individual
		lastAppliedModifiers.remove(new EqualityByIdentityWrapper<TestChromosome>((TestChromosome) individual));
	}

	@Override
	public void modification(Chromosome individual)  { /*nothing to do */ }
	
	@Override
	public void iteration(GeneticAlgorithm<?> algorithm)  { /*nothing to do */ }
	
	@Override
	public void searchFinished(GeneticAlgorithm<?> algorithm)  { /*nothing to do */ }
	
	private	void storeSuccessfulModifiersForLS(double originalFitness, List<ModifierRecord> modifiers) {
		double currentFitness = originalFitness;
		for (int i = 0; i < modifiers.size(); i++) {
			ModifierRecord modifier = modifiers.get(i);
			if (successfulModifiers.contains(modifier.method)) continue; /* already there*/
			
			if (i > 0 && !modifiers.get(i-1).evaluated) {
				ModifierRecord modifierPrevious = modifiers.get(i-1);
				//LoggingUtils.getEvoLogger().info("-- Evaluate fitness prev: " + modifierPrevious.method);
				currentFitness = testFitnessFunction.getFitness(modifierPrevious.test);
				modifierPrevious.evaluated = true;
			}
			//LoggingUtils.getEvoLogger().info("** Evaluate fitness temp: " + modifier.method);
			double prevFitness = currentFitness;
			currentFitness = testFitnessFunction.getFitness(modifier.test);
			modifier.evaluated = true;
			if (currentFitness < prevFitness) {
				if (!successfulModifiers.contains(modifier.method))
					successfulModifiers.add(modifier.method);
			}
		}
	}
	
	private static class ModifierRecord {
		TestChromosome test;
	    GenericMethod method;
	    boolean evaluated = false;
	    ModifierRecord(TestChromosome test, GenericMethod method) {
	    	this.test = (TestChromosome) test.clone(); //do clone, since the offspring can change further 
	    	this.method = method;
	    }
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((method == null) ? 0 : method.hashCode());
			result = prime * result + ((method == null || method.getOwnerType() == null) ? 0 : method.getOwnerType().hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ModifierRecord other = (ModifierRecord) obj;
			if (method == null) {
				if (other.method != null)
					return false;
			} else {
				if (!method.equals(other.method))
				return false;
				if (method.getOwnerType() == null) {
					if (other.method.getOwnerType() != null)
						return false;
				} else if (!method.getOwnerType().equals(other.method.getOwnerType()))
					return false;
			}
			return true;
		}
		
		@Override
		public String toString() {
			return "" + method;
		}
	    
	}
	
}

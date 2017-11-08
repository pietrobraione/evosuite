package org.evosuite.ga.metaheuristics.mosa;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.evosuite.Properties;
import org.evosuite.Properties.Criterion;
import org.evosuite.coverage.pathcondition.PathConditionCoverageGoalFitness;
import org.evosuite.ga.Chromosome;
import org.evosuite.ga.ChromosomeFactory;
import org.evosuite.ga.FitnessFunction;
import org.evosuite.ga.metaheuristics.mosa.comparators.OnlyCrowdingComparator;
import org.evosuite.ga.metaheuristics.mosa.structural.BranchesManager;
import org.evosuite.ga.metaheuristics.mosa.structural.PathConditionManager;
import org.evosuite.ga.metaheuristics.mosa.structural.StatementManager;
import org.evosuite.ga.metaheuristics.mosa.structural.StrongMutationsManager;
import org.evosuite.ga.metaheuristics.mosa.structural.StructuralGoalManager;
import org.evosuite.ga.metaheuristics.mosa.structural.SushiManager;
import org.evosuite.testcase.TestChromosome;
import org.evosuite.testsuite.TestSuiteChromosome;
import org.evosuite.testsuite.TestSuiteFitnessFunction;
import org.evosuite.utils.ArrayUtil;
import org.evosuite.utils.LoggingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of the DynaMOSA (Many Objective Sorting Algorithm) described in the TSE'17 paper ...
 * 
 * @author Annibale, Fitsum
 *
 * @param <T>
 */
public class DynaMOSA<T extends Chromosome> extends AbstractMOSA<T> {

	private static final long serialVersionUID = 146182080947267628L;

	private static final Logger logger = LoggerFactory.getLogger(DynaMOSA.class);

	/** Manager to determine the test goals to consider at each generation */
	protected StructuralGoalManager<T> goalsManager = null;

	protected CrowdingDistance<T> distance = new CrowdingDistance<T>();

	private int unchangedPopulationIterations = 0; /*SUSHI: Reset*/

	public DynaMOSA(ChromosomeFactory<T> factory) {
		super(factory);
	}

	/** {@inheritDoc} */
	@Override
	protected void evolve() {
		int goodCrosoversAtBegin = goodOffsprings; /*SUSHI: Reset*/
		
		List<T> offspringPopulation = breedNextGeneration();

		// Create the union of parents and offSpring
		List<T> union = new ArrayList<T>(population.size()+offspringPopulation.size());
		union.addAll(offspringPopulation);
		union.addAll(population);

		// Ranking the union
		logger.debug("Union Size = {}", union.size());

		// Ranking the union using the best rank algorithm (modified version of the non dominated sorting algorithm
		ranking.computeRankingAssignment(union, goalsManager.getCurrentGoals());

		// let's form the next population using "preference sorting and non-dominated sorting" on the
		// updated set of goals
		int remain = Math.max(Properties.POPULATION, ranking.getSubfront(0).size());
		
		if(Properties.AVOID_REPLICAS_OF_INDIVIDUALS) { /*SUSHI: Prevent multiple copies of individuals*/
			remain = Math.min(remain, union.size());
		}

		int index = 0;
		List<T> front = null;
		population.clear();
		// Obtain the next front
		front = ranking.getSubfront(index);

		while (remain > 0 && remain >= front.size()) {
			// Assign crowding distance to individuals
			distance.fastEpsilonDominanceAssignment(front, goalsManager.getCurrentGoals());

			// Add the individuals of this front
			addToPopulation(front); //TODO: GIO: population.addAll(front);

			// Decrement remain
			remain = remain - front.size();

			// Obtain the next front
			index++;
			if (remain > 0) {
				front = ranking.getSubfront(index);
			} // if
		} // while

		// Remain is less than front(index).size, insert only the best one
		if (remain > 0) { // front contains individuals to insert
			distance.fastEpsilonDominanceAssignment(front, goalsManager.getCurrentGoals());
			Collections.sort(front, new OnlyCrowdingComparator());
			for (int k = 0; k < remain; k++) {
				addToPopulation(front.get(k)); //TODO: GIO: population.add(front.get(k));
				
			} // for

			remain = 0;
		} // if
		//for (T  p : population)
		//	logger.error("Rank {}, Distance {}", p.getRank(), p.getDistance());


		if(Properties.NO_CHANGE_ITERATIONS_BEFORE_RESET > 0) { /*SUSHI: Reset*/ 
			boolean someChanges = goodOffsprings > goodCrosoversAtBegin;
			handleReset(someChanges); 
		}

		currentIteration++;
		if (currentIteration % 10 == 0) {
		LoggingUtils.getEvoLogger().info("ITERATION: {}", currentIteration);
		//LoggingUtils.getEvoLogger().info("Population size= {}", population.size());
		//LoggingUtils.getEvoLogger().info("N. fronts = {}", ranking.getNumberOfSubfronts());
		//LoggingUtils.getEvoLogger().info("1* front size = {}", ranking.getSubfront(0).size());
		LoggingUtils.getEvoLogger().info("Covered goals = {}", goalsManager.getCoveredGoals().size());
		LoggingUtils.getEvoLogger().info("Current goals = {}", goalsManager.getCurrentGoals().size());
		//LoggingUtils.getEvoLogger().info("Uncovered goals = {}", goalsManager.getUncoveredGoals().size());
		}
	}

	private int resets = 0;
	private void printInfo(T c) {
		String fits = "";
		for (FitnessFunction<T> g : goalsManager.getCurrentGoals()) {
			fits += ((PathConditionCoverageGoalFitness) g).getPathConditionId();
			fits += "=" + g.getFitness(c) + ",";
		} 
		fits += " from it " + c.getAge();
		LoggingUtils.getEvoLogger().info("* id = {}, fits = {}", System.identityHashCode(c), fits, c.getFitness());			
	}
	
	private boolean handleReset(boolean changed) { /*SUSHI: Reset*/
		if (changed) unchangedPopulationIterations = 0;
		else unchangedPopulationIterations++;

		if (goalsManager.getCurrentGoals().isEmpty()) {
			return false; // search finished
		}

		// Console output each 10 iterations
		if (currentIteration % 10 == 0) {
			LoggingUtils.getEvoLogger().info("\n***{} no change iterations,  {} resets, {} good offsprings ({} mutation only), best fits:", unchangedPopulationIterations, resets, goodOffsprings, goodOffspringsMutationOnly);
			for (T c : ranking.getSubfront(0)) {
				printInfo(c);			
			}
		}
		
		if (unchangedPopulationIterations >  Properties.NO_CHANGE_ITERATIONS_BEFORE_RESET) {
			long time = System.currentTimeMillis();
			
			List<T> newPopulation = new ArrayList<T>();
			if (goalsManager.getCurrentGoals().size() == 1) {
				newPopulation.addAll(elitism());

			} else {
				newPopulation.addAll(ranking.getSubfront(0));
			}
			
			//initializePopulation(); NB: prefer explicit setup to avoid reset of all search params
			population.clear();
			generateInitialPopulation(Properties.POPULATION - newPopulation.size());
		
			for (T c : population) {
				c.updateAge(currentIteration);
				c.setChanged(true);
			}
			calculateFitness();
		
			population.addAll(0, newPopulation);
			
		
			// Calculate dominance ranks
			ranking.computeRankingAssignment(population, goalsManager.getCurrentGoals());

			time = System.currentTimeMillis() - time;
			resets++;
			unchangedPopulationIterations = 0;
			
			LoggingUtils.getEvoLogger().info("*******************************");
			LoggingUtils.getEvoLogger().info("* Population reset at iteration " + currentIteration + " (took " + time + " msec)" + "elite size is {}", newPopulation.size());
			LoggingUtils.getEvoLogger().info("******************************* ");
			
			return true;
		}
		return false;
	}


	private void addToPopulation(List<T> front) {
		for (T individual : front) {
			addToPopulation(individual); 
		}
	}

	private void addToPopulation(T t) {
		population.add(t);
		notifyInNextGeneration(t);
	}

	/** 
	 * This method computes the fitness scores only for the current goals
	 * @param c chromosome
	 */
	protected void calculateFitness(T c) {
		goalsManager.calculateFitness(c);
		notifyEvaluation(c);
	}

	/** 
	 * This method computes the fitness scores for all (covered and uncovered) goals
	 * @param c chromosome
	 */
	protected void completeCalculateFitness(T c) {
		for (FitnessFunction<T> fitnessFunction : this.goalsManager.getCoveredGoals().keySet()) {
			if (!c.getFitnessValues().containsKey(fitnessFunction))
				c.getFitness(fitnessFunction);
			//notifyEvaluation(c);
		}
		for (FitnessFunction<T> fitnessFunction : this.goalsManager.getCurrentGoals()) {
			if (!c.getFitnessValues().containsKey(fitnessFunction))
				c.getFitness(fitnessFunction);
			//notifyEvaluation(c);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void generateSolution() {
		logger.info("executing generateSolution function");

		if (ArrayUtil.contains(Properties.CRITERION, Criterion.BRANCH) && Properties.CRITERION.length==1){
			goalsManager = new BranchesManager<T>(fitnessFunctions);
		} else if (ArrayUtil.contains(Properties.CRITERION, Criterion.STATEMENT) && Properties.CRITERION.length==1){
			goalsManager = new StatementManager<T>(fitnessFunctions);
		} else if (ArrayUtil.contains(Properties.CRITERION, Criterion.STRONGMUTATION) && Properties.CRITERION.length==1){
			goalsManager = new StrongMutationsManager<T>(fitnessFunctions);
		} else if (ArrayUtil.contains(Properties.CRITERION, Criterion.PATHCONDITION) && Properties.CRITERION.length==1){
			goalsManager = new PathConditionManager<T>(fitnessFunctions);
		} else if (ArrayUtil.contains(Properties.CRITERION, Criterion.PATHCONDITION) && ArrayUtil.contains(Properties.CRITERION, Criterion.BRANCH)){
			goalsManager = new SushiManager<T>(fitnessFunctions);
		}

		LoggingUtils.getEvoLogger().info("\n Initial Number of Goals = "+goalsManager.getCurrentGoals().size());

		//initialize population
		if (population.isEmpty())
			initializePopulation();

		// update current goals
		calculateFitness();

		// Calculate dominance ranks and crowding distance
		ranking.computeRankingAssignment(population, goalsManager.getCurrentGoals());

		for (int i = 0; i<ranking.getNumberOfSubfronts(); i++){
			distance.fastEpsilonDominanceAssignment(ranking.getSubfront(i), goalsManager.getCurrentGoals());
		}
		// next generations
		while (!isFinished() && goalsManager.getUncoveredGoals().size() > 0) {
			evolve();
			notifyIteration();
		}
		completeCalculateFitness(); //TODO: GIO
		notifySearchFinished();
	}

	protected void completeCalculateFitness() {
		goalsManager.restoreInstrumentationForAllGoals(); //GIO: TODO
		logger.debug("Calculating fitness for " + population.size() + " individuals");
		for (T c : goalsManager.getCoveredGoals().values()){
			completeCalculateFitness(c);
		}
	}

	/** This method return the test goals covered by the test cases stored in the current archive **/
	public Set<FitnessFunction<T>> getCoveredGoals() {
		return goalsManager.getCoveredGoals().keySet();
	}

	protected List<T> getArchive() {
		//Set<T> tests = new HashSet<T>();
		//tests.addAll(goalsManager.getCoveredGoals().values());
		List<T> suite = new ArrayList<T>(goalsManager.getArchive());
		return suite;
	}

	protected List<T> getFinalTestSuite() {
		// trivial case where there are no branches to cover or the archive is empty
		List<T> archive = getArchive();
		if (archive.size() == 0){
			if (population.size() > 0) {
				ArrayList<T> list = new ArrayList<T>(population.size());
				list.add(population.get(population.size() - 1));
				return list;
			} else
				return archive;
		}
		//List<T>[] rank=this.nonDominatedSorting(archive);
		return archive;
	}

	@Override @SuppressWarnings("unchecked")
	public T getBestIndividual() {
		TestSuiteChromosome best = new TestSuiteChromosome();
		for (T test : getArchive()) {
			best.addTest((TestChromosome) test);
		}
		// compute overall fitness and coverage
		for (TestSuiteFitnessFunction suiteFitness : suiteFitnesses){
			double coverage = ((double) goalsManager.getCoveredGoals().size()) / ((double) this.fitnessFunctions.size());
			best.setFitness(suiteFitness,  this.fitnessFunctions.size() - goalsManager.getCoveredGoals().size());
			best.setCoverage(suiteFitness, coverage);
		}
		//suiteFitness.getFitness(best);
		return (T) best;
	}

	protected double numberOfCoveredTargets(){
		return this.goalsManager.getCoveredGoals().size();
	}

	@Override /*SUSHI: Prevent multiple copies of individuals*/
	protected boolean keepOffspring(T offspring, T parent1, T parent2) {
		for (FitnessFunction<T> g : goalsManager.getCurrentGoals()){
			double newFitness = g.getFitness(offspring);
			double p1Fitness = g.getFitness(parent1);
			double p2Fitness = g.getFitness(parent2);
			if (newFitness < p1Fitness && newFitness < p2Fitness) { 
				return true;
			}
		}
		return false;
	}

	@Override /*SUSHI: Prevent multiple copies of individuals*/
	protected boolean checkForRemove(T parent, T offspring1, T offspring2) {
		for (FitnessFunction<T> g : goalsManager.getCurrentGoals()){
			double newFitness1 = offspring1 != null ? g.getFitness(offspring1) : Double.MAX_VALUE;
			double newFitness2 = offspring2 != null ? g.getFitness(offspring2) : Double.MAX_VALUE;
			double pFitness = g.getFitness(parent);
			if (pFitness < newFitness1 && pFitness < newFitness2) { 
				return false; //do not remove
			}
		}
		return true;//population.remove(parent);
	}

}

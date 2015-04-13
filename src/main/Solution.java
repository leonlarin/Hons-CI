package main;

import java.text.DecimalFormat;
import java.util.*;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.impl.*;

import setup.Job;
import setup.Problem;

public class Solution {
	//Change this to swap the problem being solved.
	public static int problemNumber = 2;
	
	//Parameters for EA
	private static final int MAX_ALLOWED_EVOLUTIONS = 100;
	public static final int populationSize = 1000;
	
	//Mutation modifier is 1/x so number of 1000 will mutate one in 1000 genes
	private static final int mutationModifier = 10; 
	//How much of original population is selected for breeding.
	private static final double originalRate = 0.95;

	//Max payout is calculated by adding all the maximum possible payouts for any given job in the list
	//This is used as an arbitrary parameter for Fitness function and can't possibly be reached.
	private static int maximumPayout;
	
	public static void findSolution()
		      throws Exception {
		
		Problem.loadProblem("problems/Problem" + problemNumber + ".txt");
		ArrayList<Job> jobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));  
		maximumPayout = jobs.get(jobs.size() - 1).maxPayout;
		int jobsnumber = jobs.size();
		
		//Config for our setup.
		Configuration conf = new DefaultConfiguration();
	    
	    conf.getNaturalSelectors(false).clear();
	    conf.getGeneticOperators().clear();
	    
	    conf.setPreservFittestIndividual(true);
	    
	    FitFunction myFunc =
	            new FitFunction(maximumPayout);
	        conf.setFitnessFunction(myFunc);
	    
	    //conf.addNaturalSelector(new CustomSelector(conf), true);//Selector Before
	    
	    //GreedyCrossover is a mutation operator that was designed for the Traveling Salesman Problem,
	    //and is used here to get valid permutations of genes instead of generating random genes that would be invalid.
	    GreedyCrossover greed = new GreedyCrossover(conf);
	    greed.setStartOffset(0);
	    conf.addGeneticOperator(greed);
	    
	    //Chooses the fittest chromosomes from the population for further breeding.
	    BestChromosomesSelector bcs = new BestChromosomesSelector(conf);
	    bcs.setOriginalRate(originalRate);
	    bcs.setDoubletteChromosomesAllowed(false);
	    conf.addNaturalSelector(bcs, false);
	    
	    //Another mutator that creates permutations of genes.
	    //Having two mutator opperator diversifies the population and leads to faster evolution to the solution.
	    SwappingMutationOperator swap = new SwappingMutationOperator(conf, mutationModifier);
	    swap.setStartOffset(0);
	    conf.addGeneticOperator(swap);
	    
	    
	    //conf.addNaturalSelector(new WeightedRouletteSelector(conf), false);
	    //conf.addNaturalSelector(new CustomSelector(conf), false);//Selector After
	    

	    //conf.setRandomGenerator(new StockRandomGenerator());
	    conf.setRandomGenerator(new GaussianRandomGenerator());
	    
	    //A gene representing all the jobs in order, this is used to create a sample chromosome.
	    Gene[] sampleGenes = new Gene[jobsnumber];
	    for(int i=0; i < jobsnumber;i++){
	    	sampleGenes[i] = new IntegerGene(conf, 0, jobsnumber-1);
	    	sampleGenes[i].setAllele(i);
	    }
	    
	    //A wildcard generator to assist in generating new solutions.
	    Gene[] randomisedGenes = sampleGenes;
	    Collections.shuffle(Arrays.asList(randomisedGenes));
	    
	    Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    Chromosome randomChromosome = new Chromosome(conf, randomisedGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    conf.setPopulationSize(populationSize);
	    
	    Population firstPopulation = new Population(conf);
	    
	    //Creating a sample population, consist
	    for(int o = 0; o<populationSize; o++)
	    {	
	    	firstPopulation.addChromosome(sampleChromosome);
	    	firstPopulation.addChromosome(randomChromosome);
	    }
	    Genotype population = new Genotype(conf, firstPopulation);
	    long startTime = System.nanoTime();
	    for(int j = 0; j<MAX_ALLOWED_EVOLUTIONS;j++) {
	    	//System.out.println(population.getPopulation().size());
	    	
	        // Evolve the population. Since we don't know what the best answer
	        // is going to be, we just evolve the max number of times.
	        // ---------------------------------------------------------------       
	    	population.evolve();  
	    	long endTime = System.nanoTime();
			
			double time = (endTime - startTime) / 1000000000.0;
			String timeTrunc = new DecimalFormat("#.###").format(time);	
	        //The fitness value is the total payout for a particular job order represented by a chromosome.
	        IChromosome bestSolutionSoFar = population.getFittestChromosome();
	        System.out.println("The best solution in this poulation has a fitness value of " +
	                         bestSolutionSoFar.getFitnessValue());
	        System.out.println(FitFunction.getJobOrderIds(population.getFittestChromosome()) + " Cycle: " + j 
	        			+ " Time: " + timeTrunc);
	        System.out.println();
	    }
}
	
    public static void main(String[] args) {
	    try {
	    	findSolution();
	    }
	    catch (Exception e) {
            e.printStackTrace();
	        }     
	}
}
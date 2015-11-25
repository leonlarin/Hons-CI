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
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

import setup.Job;
import setup.Problem;

public class Solution {
	private boolean _printToFile;
	public void setPrintToFile(boolean printToFile) {
		this._printToFile = printToFile;
    }
    public boolean getPrintToFile() {
        return this._printToFile;
    }
    
    private boolean _printToConsole;
	public void setprintToConsole(boolean printToConsole) {
		this._printToConsole = printToConsole;
    }
    public boolean getprintToConsole() {
        return this._printToConsole;
    }
    
	//Parameters for EA
	//Change this to swap the problem being solved(1 to 5).
	public static int problemNumber = 1;
	
    //Number of cycles/generations.
	private int _MAX_ALLOWED_EVOLUTIONS = 10;
	public void setMaxAllowedEvolutions(int MAX_ALLOWED_EVOLUTIONS) {
		_MAX_ALLOWED_EVOLUTIONS = MAX_ALLOWED_EVOLUTIONS;
    }
    public int getMaxAllowedEvolutions() {
        return _MAX_ALLOWED_EVOLUTIONS;
    }
	
	//Size of the initial population 
    //Bigger size will increase the entropy of the population but will require more resources to go through cycles.
	private int _populationSize = 1000;
	public void setPopulationSize(int populationSize) {
		this._populationSize = populationSize;
    }
    public int getPopulationSize() {
        return this._populationSize;
    }
    
	//Mutation modifier is an inverse proportion(1/x), so a setting of a 1000 will mutate 1 in every 1000 genes.
	private int _mutationModifier = 10;
	public void setMutationModifier(int mutationModifier) {
		this._mutationModifier = mutationModifier;
    }
    public int getMutationModifier() {
        return this._mutationModifier;
    }
    
	//How much of original population is selected for breeding.
	private double _originalRate = 0.95;
	public void originalRate() { this._originalRate = 0.95; }
	public void setOriginalRate(double originalRate) {
		this._originalRate = originalRate;
    }
    public double getOriginalRate() {
        return this._originalRate;
    }
	
	//Max payout is calculated by adding all the maximum possible payouts for any given job in the list
	//This is used as an arbitrary parameter for Fitness function and can't possibly be reached.
	private static int maximumPayout;
	
	
	
	public Genotype setupForEvolution()
		      throws Exception {
		
		Problem.loadProblem("problems/Problem" + problemNumber + ".txt");
		ArrayList<Job> jobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));  
		maximumPayout = jobs.get(jobs.size() - 1).maxPayout;
		int jobsnumber = jobs.size();
		
		//Config for our setup.
		Configuration conf = new DefaultConfiguration();
	    
		//Clearing all default Selectors and Operators as they mess with our desired type of mutation.
	    conf.getNaturalSelectors(false).clear();
	    conf.getGeneticOperators().clear();
	    
	    conf.setPreservFittestIndividual(true);
	    
	    
	    FitFunction myFunc = new FitFunction(maximumPayout);
	    conf.setFitnessFunction(myFunc);
	    
	    //Attempt at a custom selector, depricated
	    //conf.addNaturalSelector(new CustomSelector(conf), true); 
	    
	    //GreedyCrossover is a mutation operator that was designed for the Traveling Salesman Problem,
	    //and is used here to get valid permutations of genes instead of generating random genes that would be invalid.
	    GreedyCrossover greed = new GreedyCrossover(conf);
	    greed.setStartOffset(0);
	    conf.addGeneticOperator(greed);
	    
	    //Chooses the fittest chromosomes from the population for further breeding.
	    BestChromosomesSelector bcs = new BestChromosomesSelector(conf);
	    bcs.setOriginalRate(_originalRate);
	    bcs.setDoubletteChromosomesAllowed(false);
	    conf.addNaturalSelector(bcs, false);
	    
	    //Another mutator that creates permutations of genes.
	    //Having two mutator opperator diversifies the population and leads to faster evolution to the solution.
	    SwappingMutationOperator swap = new SwappingMutationOperator(conf, _mutationModifier);
	    swap.setStartOffset(0);
	    conf.addGeneticOperator(swap);
	    
	    //Testing Selectors
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
	    Gene[] randomizedGenes = sampleGenes;
	    Collections.shuffle(Arrays.asList(randomizedGenes));
	    
	    //Creating the initial population
	    Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    Chromosome randomChromosome = new Chromosome(conf, randomizedGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    conf.setPopulationSize(_populationSize);
	    
	    Population firstPopulation = new Population(conf);
	    
	    //Creating a sample population, consist
	    for(int o = 0; o<_populationSize; o++)
	    {	
	    	firstPopulation.addChromosome(sampleChromosome);
	    	firstPopulation.addChromosome(randomChromosome);
	    }
	    Genotype population = new Genotype(conf, firstPopulation);
	    return population;
	
	}
	public void findSolution(Genotype population) throws IOException{
		long startTime = System.nanoTime();
	  
	    //Main bit of the evolution algorithm, every cycle of this for loop is a generation.
	    for(int j = 0; j<_MAX_ALLOWED_EVOLUTIONS;j++) {
	    	//System.out.println(population.getPopulation().size());
	    	
	        // Evolve the population. Since we don't know what the best answer
	        // is going to be, we just evolve the max number of times.
	        // ---------------------------------------------------------------       
	    	population.evolve();
	    	
	    	//Timing the process for statistical and analytical purposes
	    	long endTime = System.nanoTime();
			
			double time = (endTime - startTime) / 1000000000.0;
			String timeTrunc = new DecimalFormat("#.###").format(time);	
			IChromosome bestSolutionSoFar = population.getFittestChromosome();
			if(_printToConsole == true)
			{
				//The fitness value is the total payout for a particular job order represented by a chromosome.
		        
		        System.out.println("The best solution in this poulation has a fitness value of " +
		                         bestSolutionSoFar.getFitnessValue());	        
		        System.out.println(FitFunction.getJobOrderIds(population.getFittestChromosome()) + " Cycle: " + j 
		        			+ " Time: " + timeTrunc);
		        System.out.println();
			}
	        
			if(_printToFile == true)
			{
				//Writing this to a file for analysis.
		        //The file is not formated so it can be easier read by a analysis algorithm
		        writeToFile(String.valueOf(j));	//Generation
		        writeToFile(String.valueOf(bestSolutionSoFar.getFitnessValue())); //Fitness
		        writeToFile(String.valueOf(timeTrunc));	//Time it took to calculate
		        writeToFile(String.valueOf(FitFunction.getJobOrderIds(population.getFittestChromosome()))); 	//job order
		        writeToFile(String.valueOf(population));
			}
        }
	}
	
	    


    public static void writeToFile(String textline) throws IOException {
		//Setting up the file writer
	    File file = new File("D:\\Documents\\Eclipse\\Hons-CI\\res\\results.txt");
	    FileWriter write = new FileWriter(file, true);
	    PrintWriter print_line = new PrintWriter( write );
	    print_line.printf( "%s" + "%n" , textline);
	    print_line.close();
	}
}
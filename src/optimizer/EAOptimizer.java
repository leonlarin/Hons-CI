package optimizer;

import java.text.DecimalFormat;

import java.util.*;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.impl.*;

import main.EvolutionaryAlgorithm;
import main.FitFunction;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;

public class EAOptimizer extends EvolutionaryAlgorithm{

	public Genotype setupForEvolution()
		      throws Exception {
		
		//UI mode lets to manually set the problem for a demo
		if(_UIMode)
		{
			System.out.println("Please enter the number of problem to solve(1 to 5).");
		    Scanner user_input = new Scanner( System.in );
		    problemNumber = Integer.parseInt(user_input.next());
		    if(problemNumber > 5 || problemNumber < 1)
		    {
		    	problemNumber = 1;
		    	System.out.println("Invalid value, procedeing with default problem 1.");
		    	user_input.close();
		    }
		}
		
		//Config for our setup.
		Configuration config = new DefaultConfiguration();
	    
		//Clearing all default Selectors and Operators as they mess with our desired type of mutation.
	    config.getNaturalSelectors(false).clear();
	    config.getGeneticOperators().clear();
	    
	    config.setPreservFittestIndividual(true);
	    
	    
	    OptimizerFitness myFunc = new OptimizerFitness();
	    config.setFitnessFunction(myFunc);
	    
	    //Attempt at a custom selector, depricated
	    //conf.addNaturalSelector(new CustomSelector(conf), true); 
	    
	    //GreedyCrossover is a mutation operator that was designed for the Traveling Salesman Problem,
	    //and is used here to get valid permutations of genes instead of generating random genes that would be invalid.
	    config.addGeneticOperator(new CustomMutator());
	    
	    //Chooses the fittest chromosomes from the population for further breeding.
	    BestChromosomesSelector bcs = new BestChromosomesSelector(config);
	    bcs.setOriginalRate(_originalRate);
	    bcs.setDoubletteChromosomesAllowed(false);
	    config.addNaturalSelector(bcs, false);
	    
	    //Testing Selectors
	    config.addNaturalSelector(new WeightedRouletteSelector(config), false);
	    

	    //conf.setRandomGenerator(new StockRandomGenerator());
	    config.setRandomGenerator(new GaussianRandomGenerator());
	    
	    Gene[] sampleGenes = new Gene[3];
	    
	    sampleGenes[0] = new IntegerGene(config, 0, 1000); //pop size
	    sampleGenes[1] = new IntegerGene(config, 10, 1000); //mut rate
	    sampleGenes[2] = new DoubleGene(config, 0.0, 1); //orig rate

	    
	    //A wildcard generator to assist in generating new solutions.
	    
	    //Creating the initial population
	    Chromosome sampleChromosome = new Chromosome(config, sampleGenes);
	    config.setSampleChromosome(sampleChromosome);
	    config.setPopulationSize(_populationSize);
	    
	    Population firstPopulation = new Population(config);
	    
	    //Creating a sample population, consist
	    for(int o = 0; o<_populationSize; o++)
	    {	
	    	firstPopulation.addChromosome(sampleChromosome);
	    }
	    Genotype population = new Genotype(config, firstPopulation);
	    return population;
	
	}
	public void findSolutions(Genotype population) throws IOException{
		long startTime = System.nanoTime();
		String timeTrunc = "";
		String output = "";
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
			timeTrunc = new DecimalFormat("#.###").format(time);	
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
	        
			output  = (String.valueOf(new DecimalFormat("#.##").format(bestSolutionSoFar.getFitnessValue() / (Float.parseFloat(timeTrunc) * 100.0))) + "," + 
	        		String.valueOf(bestSolutionSoFar.getFitnessValue()) + "," + 
	        		String.valueOf(timeTrunc) + "," +
	        		String.valueOf(_populationSize) + "," + 
	        		String.valueOf(_mutationModifier) + "," + 
	        		_originalRate + "," + 
	        		String.valueOf(FitFunction.getJobOrderIds(population.getFittestChromosome())));
			
			if(_printToCSV == true)
			{
		    	writeToFile(output);
			}
        }	    
	    if(_AnalyticsMode == true)
		{
			//Writing this to a file for analytics, only writes the fittest chromosome from the last cycle.
	    	writeToFile(output);
	    	System.out.println("Fittest chromosome and its settings: " + output);
	        
		}
	}
	    


    public static void writeToFile(String textline) throws IOException {
		//Setting up the file writer
	    File file = new File("D:\\Documents\\Eclipse\\Hons-CI\\res\\optimisation" + problemNumber + ".csv");
	    FileWriter write = new FileWriter(file, true);
	    PrintWriter print_line = new PrintWriter( write );
	    print_line.printf( "%s" + "%n" , textline);
	    print_line.close();
	}
}
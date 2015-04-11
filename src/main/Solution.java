package main;

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
	public static int problemNumber = 1;
	private static final int MAX_ALLOWED_EVOLUTIONS = 500;
	public static final int populationSize = 5000;
	private static final int mutationModifier = 2; 
	//Mutation modifier is 1/x so number of 1000 will mutate one in 1000 genes
	
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
	    
	    
	    
	    GreedyCrossover greed = new GreedyCrossover(conf);
	    greed.setStartOffset(0);
	    conf.addGeneticOperator(greed);
	    
	    BestChromosomesSelector bcs = new BestChromosomesSelector(conf);
	    bcs.setOriginalRate(0.2);
	    bcs.setDoubletteChromosomesAllowed(false);
	    conf.addNaturalSelector(bcs, false);
	    
	    SwappingMutationOperator swap = new SwappingMutationOperator(conf, mutationModifier);
	    swap.setStartOffset(0);
	    conf.addGeneticOperator(swap);
	    
	    //conf.addNaturalSelector(new WeightedRouletteSelector(conf), false);
	    
	    //conf.addNaturalSelector(new CustomSelector(conf), false);//Selector After
	    

	    //conf.setRandomGenerator(new StockRandomGenerator());
	    conf.setRandomGenerator(new GaussianRandomGenerator());
	    
	    
	    Gene[] sampleGenes = new Gene[jobsnumber];
	    for(int i=0; i < jobsnumber;i++){
	    	sampleGenes[i] = new IntegerGene(conf, 0, jobsnumber-1);
	    	sampleGenes[i].setAllele(i);
	    }
	    
	    Gene[] randomisedGenes = sampleGenes;
	    Collections.shuffle(Arrays.asList(randomisedGenes));
	    
	    Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    Chromosome randomChromosome = new Chromosome(conf, randomisedGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    conf.setPopulationSize(populationSize);
	    
	    Population firstPopulation = new Population(conf);
	    
	    for(int o = 0; o<populationSize; o++)
	    {	
	    	firstPopulation.addChromosome(sampleChromosome);
	    	//firstPopulation.addChromosome(randomChromosome);
	    }
	    Genotype population = new Genotype(conf, firstPopulation);
	    for(int j = 0; j<MAX_ALLOWED_EVOLUTIONS;j++) {
	    	   
	    
	    //IChromosome bestSolutionSoFar = population.getFittestChromosome();
	    //Genotype population = Genotype.randomInitialGenotype( conf );
	    
	      for (int run = 0; run < 10; run++) {
	    	  
	    	  //System.out.println(population.getPopulation().size());
        	
        	  
	        // Evolve the population. Since we don't know what the best answer
	        // is going to be, we just evolve the max number of times.
	        // ---------------------------------------------------------------
	        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
	          //try{
	        	  population.evolve();
	        	  
	        	  
	        //  }
	        	//catch (java.lang.Error e){}
	        
	          // add current best fitness to chart
	          double fitness = population.getFittestChromosome().getFitnessValue();
	          //System.out.println(fitness);
	          if (i % 3 == 0) {
	            String s = String.valueOf(i);
	            //eval.setValue(permutation, run, fitness, "" + permutation, s);
	            //eval.storeGenotype(permutation, run, population);

	          }
	        }
	      }		
	      IChromosome bestSolutionSoFar = population.getFittestChromosome();
	      System.out.println("The best solution in the poulation has a fitness value of " +
	                         bestSolutionSoFar.getFitnessValue());
	      System.out.println(FitFunction.getJobOrderIds(population.getFittestChromosome()));
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
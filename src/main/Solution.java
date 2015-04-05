package main;

import java.util.*;

import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.audit.Evaluator;
import org.jgap.audit.PermutingConfiguration;
import org.jgap.impl.*;

import setup.Job;
import setup.Problem;

public class Solution {
	
	private static final int MAX_ALLOWED_EVOLUTIONS = 80;
	private static int maximumPayout;
	
	public double fitnessFunct(IChromosome jobOrderChromosome) {
		int payoutOfJobOrder = payoutForJob(jobOrderChromosome);
		int payoutDiffirence = Math.abs(maximumPayout - payoutOfJobOrder);
		
		double fitness = (maximumPayout - payoutDiffirence);
		if(payoutOfJobOrder == maximumPayout)
		{
			fitness = maximumPayout;
		}
		return fitness;
	}
	public static int payoutForJob(IChromosome orderedJobs ){
		return -1; //Problem.score(chromosomeToArrayList(orderedJobs));
	}

	public static void findSolution()
		      throws Exception {
		Problem.loadProblem("problems/Problem1.txt");
		ArrayList<Job> jobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));  
		maximumPayout = jobs.get(0).maxPayout;
		int jobsnumber = jobs.size();
		//Config for our setup.
		Configuration conf = new DefaultConfiguration();
	    conf.setPreservFittestIndividual(true);
	    
	    FitFunction myFunc =
	            new FitFunction(maximumPayout);
	        conf.setFitnessFunction(myFunc);
	    
	    conf.addGeneticOperator(new GreedyCrossover(conf));
	    conf.addGeneticOperator(new SwappingMutationOperator(conf, 20));
	    
	    Gene[] sampleGenes = new Gene[jobsnumber];
	    for(int i=0; i < jobsnumber;i++){
	    	sampleGenes[i] = new IntegerGene(conf, 0, jobsnumber);
	    }
	    
	    
	        
	    Chromosome sampleChromosome = new Chromosome(conf, sampleGenes);
	    conf.setSampleChromosome(sampleChromosome);
	    conf.setPopulationSize(500);
	    PermutingConfiguration pconf = new PermutingConfiguration(conf);
	    pconf.addGeneticOperatorSlot(new CrossoverOperator(conf));
	    pconf.addGeneticOperatorSlot(new MutationOperator(conf));
	    pconf.addNaturalSelectorSlot(new BestChromosomesSelector(conf));
	    pconf.addNaturalSelectorSlot(new WeightedRouletteSelector(conf));
	    pconf.addRandomGeneratorSlot(new StockRandomGenerator());
	    RandomGeneratorForTesting rn = new RandomGeneratorForTesting();
	    rn.setNextDouble(0.7d);
	    rn.setNextInt(2);
	    pconf.addRandomGeneratorSlot(rn);
	    pconf.addRandomGeneratorSlot(new GaussianRandomGenerator());
	    pconf.addFitnessFunctionSlot(new FitFunction(
	        maximumPayout));
	    Evaluator eval = new Evaluator(pconf);
	    int permutation = 0;
	    while (eval.hasNext()) {
	      // Create random initial population of Chromosomes.
	      // ------------------------------------------------
	      Genotype population = Genotype.randomInitialGenotype(eval.next());
	      for (int run = 0; run < 10; run++) {
	        // Evolve the population. Since we don't know what the best answer
	        // is going to be, we just evolve the max number of times.
	        // ---------------------------------------------------------------
	        for (int i = 0; i < MAX_ALLOWED_EVOLUTIONS; i++) {
	          population.evolve();
	          // add current best fitness to chart
	          double fitness = population.getFittestChromosome().getFitnessValue();
	          if (i % 3 == 0) {
	            String s = String.valueOf(i);
	            eval.setValue(permutation, run, fitness, "" + permutation, s);
	            eval.storeGenotype(permutation, run, population);

	          }
	        }
	      }		
	      IChromosome bestSolutionSoFar = population.getFittestChromosome();
	      System.out.println("The best solution has a fitness value of " +
	                         bestSolutionSoFar.getFitnessValue());
	      permutation++;
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
package optimizer;

import org.jgap.Configuration;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;

import main.EvolutionaryAlgorithm;

@SuppressWarnings("serial")
public class OptimizerFitness extends FitnessFunction{
	
	private static int _problemNumber;
	public static int getProblemNumber() {
		return _problemNumber;
	}
	public static void setProblemNumber(int _problemNumber) {
		OptimizerFitness._problemNumber = _problemNumber;
	}
	
	//Evaluating the fitness of a job order.
	public double evaluate(IChromosome settingsChromosome) {
		Gene[] setting = settingsChromosome.getGenes();
		
		EvolutionaryAlgorithm test_subject = new EvolutionaryAlgorithm();
		test_subject.setAnalyticsMode(true);
		EvolutionaryAlgorithm.problemNumber = 1;
		//if(setting[0].getAllele() == null){setting[0].setAllele(100);}
		test_subject.setPopulationSize((int) setting[0].getAllele());
		
		//if(setting[1].getAllele() == null){setting[1].setAllele(200);}
		test_subject.setMutationModifier((int) setting[1].getAllele());
		
		//if(setting[2].getAllele() == null){setting[2].setAllele(0.95);}
		test_subject.setOriginalRate((double) setting[2].getAllele());
		try {
			setting = null;
			Genotype population = test_subject.setupForEvolution();
			test_subject.findSolutions(population);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		double fitness = test_subject.getCoefficient();
		Configuration.reset();
		
		if (fitness<0){
			fitness=0;
		}
		
		return fitness;
	}
}

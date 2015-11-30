package optimizer;

import java.text.DecimalFormat;

import org.jgap.Configuration;
import org.jgap.Genotype;

import main.EvolutionaryAlgorithm;

public class BruteOptimisation {

	public static void main(String[] args) throws Exception {
		EvolutionaryAlgorithm test_subject = new EvolutionaryAlgorithm();
		test_subject.setPrintToConsole(false);
		test_subject.setUIMode(false);
		test_subject.setPrintToCSV(false);
		test_subject.setAnalyticsMode(true);
		
		long startTime = System.nanoTime();
		for (int probNumb = 1; probNumb <= 5; probNumb++){
			EvolutionaryAlgorithm.problemNumber = probNumb;
			for(int i = 0; i <= 5; i++){
				for(int popsize = 100; popsize <= 3000; popsize+=100) {
					for(int mutrate = 10; mutrate < 100; mutrate+=10) {
						for(int origrate = 0; origrate<99; origrate+=10) {
							test_subject.setPopulationSize(popsize);
							test_subject.setMutationModifier(mutrate);
							double originalRate = origrate / 100.0;
							test_subject.setOriginalRate(originalRate);
							Genotype population = test_subject.setupForEvolution();
							test_subject.findSolutions(population);
							Configuration.reset();
						}
					}
				}
			}
		}
		long endTime = System.nanoTime();
		
		double time = (endTime - startTime) / 1000000000.0;
		String timeTrunc = new DecimalFormat("#.###").format(time);
		System.out.println("Run complete. It took: " + timeTrunc + " seconds.");
	}

}

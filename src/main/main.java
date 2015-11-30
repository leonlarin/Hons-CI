package main;

import org.jgap.Genotype;

/* This is the main file to run the evolution algorithm to solve a designated problem.
 * The settings can be manually set or read from the settings file, otherwise they default to the preset values for that specific problem.*/
public class main {

	public static void demo() throws Exception {
		//Creating the instance of the evolution algorithm configuration.
		EvolutionaryAlgorithm solution = new EvolutionaryAlgorithm();
		
		//Prints each cycles results in console.
		solution.setPrintToConsole(true); 
		
		//Prints best chromosome from a cycle to a file(results#.csv).
		solution.setPrintToCSV(false); 
		
		//Analytics mode writes best solution found to a file(analytics#.csv) after all cycles have completed.
		solution.setAnalyticsMode(false);
		
		//This field is static so its not applied to the instance. Setts the problem to be solved.
		EvolutionaryAlgorithm.problemNumber = 1;
		
		//Number of cycles/generations the evolution algorithm runs for.
		//10 is default; bigger number = better answer, but takes longer
		solution.setMaxAllowedEvolutions(10);
		
		//Applying settings to a population object that will be evolved to find solutions.
		Genotype population = solution.setupForEvolution();
		
		//Running the EA with set preferences
		solution.findSolutions(population);

	}

}

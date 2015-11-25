package main;

import org.jgap.Genotype;

import main.EvolutionaryAlgorithm;

public class StackedSettingOptimisation {

	public static void main(String[] args) throws Exception {
	EvolutionaryAlgorithm optimised = new EvolutionaryAlgorithm();
	optimised.setPrintToConsole(true);
	optimised.setUIMode(false);
	EvolutionaryAlgorithm.problemNumber = 2;
	Genotype population = optimised.setupForEvolution();
	optimised.findSolutions(population);

	}

}

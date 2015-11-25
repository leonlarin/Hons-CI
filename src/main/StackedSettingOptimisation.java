package main;

import org.jgap.Genotype;

import main.Solution;

public class StackedSettingOptimisation {

	public static void main(String[] args) throws Exception {
	Solution optimised = new Solution();
	optimised.setprintToConsole(true);
	Genotype population = optimised.setupForEvolution();
	optimised.findSolution(population);

	}

}

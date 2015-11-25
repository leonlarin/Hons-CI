package main;

import org.jgap.Genotype;

import main.Solution;

public class StackedSettingOptimisation {

	public static void main(String[] args) throws Exception {
	Solution optimised = new Solution();
	optimised.setPrintToConsole(true);
	optimised.setUIMode(false);
	Solution.problemNumber = 2;
	Genotype population = optimised.setupForEvolution();
	optimised.findSolution(population);

	}

}

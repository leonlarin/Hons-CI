package main;

import java.util.ArrayList;
import java.util.Arrays;
import org.jgap.Chromosome;
import org.jgap.FitnessFunction;

import setup.Job;
import setup.Problem;

public class Evolution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Problem.loadProblem("problems/Problem1.txt");
		ArrayList<Job>  myJobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));
		
	}
	

}

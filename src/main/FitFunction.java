package main;

import java.util.ArrayList;
import java.util.Arrays;

import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.IChromosome;


import setup.Job;
import setup.Problem;

@SuppressWarnings("serial")
public class FitFunction extends FitnessFunction{
	
	private static int maximumPayout;
	private static ArrayList<Job> setupData(){
		Problem.loadProblem("problems/Problem" + Solution.problemNumber + ".txt");	
		ArrayList<Job>  myJobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));
		int problemSize = myJobs.size();
		maximumPayout = myJobs.get(problemSize - 1).maxPayout;
		return myJobs;
	}
	
	
	//This void might cause problems
	public FitFunction(int targetPayout)
	{
		ArrayList<Job> myJobs = setupData();
		if(targetPayout < 1 || targetPayout > maximumPayout)
		{
			
			throw new IllegalArgumentException(
					"Wrong MaxPayout");
			
		}
		maximumPayout = targetPayout;
	}
	
	/*private static ArrayList<Job> chromosomeToArrayList(IChromosome chromosome){
		//Do Stuff here to convert a Ichromosome to arraylist of jobs
		return ArrayList<Job>;
	}
	
	private static IChromosome arrayListToChromosome(ArrayList<Job> jobList){
		return IChromosome;
	}*/
	
	public double evaluate(IChromosome jobOrderChromosome) {
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
		Gene[] jobGeneOrder = orderedJobs.getGenes();
		
		ArrayList<Job> jobsData = setupData();
		ArrayList<Object> listOfJobIds = new ArrayList<Object>();
		ArrayList<Job> sortedJobs = new ArrayList<Job>();
		
		for(int i = 0; i < jobGeneOrder.length; i++){
			listOfJobIds.add(jobGeneOrder[i].getAllele());
		}
		
		for(int j =0; j < listOfJobIds.size();j++){
			sortedJobs.add(jobsData.get(j));
		}
		
		return Problem.score(sortedJobs);
	}
	
}

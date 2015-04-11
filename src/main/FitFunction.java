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
	
	//Setting up the Jobs data 
	private static ArrayList<Job> setupData(){
		Problem.loadProblem("problems/Problem" + Solution.problemNumber + ".txt");	
		ArrayList<Job>  myJobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));
		int problemSize = myJobs.size();
		maximumPayout = myJobs.get(problemSize - 1).maxPayout;
		return myJobs;
	}
	
	
	//Setting the Fittness function
	public FitFunction(int targetPayout)
	{
		@SuppressWarnings("unused")
		ArrayList<Job> myJobs = setupData();
		if(targetPayout < 1 || targetPayout > maximumPayout)
		{
			
			throw new IllegalArgumentException(
					"Wrong MaxPayout");
			
		}
		maximumPayout = targetPayout;
	}
	
	//Evaluating the fitness of a job order.
	public double evaluate(IChromosome jobOrderChromosome) {
		int payoutOfJobOrder = payoutForJob(jobOrderChromosome);
		
		double fitness = payoutOfJobOrder;
		
		if (fitness<0){
			fitness=0;
		}
		
		return fitness;
	}
	
	//Helper function that returns an ArrayList of job order Ids as objects.
	public static ArrayList<Object> getJobOrderIds(IChromosome jobGeneOrder){
		ArrayList<Object> listOfJobIds = new ArrayList<Object>();
		for(int i = 0; i < jobGeneOrder.size(); i++){
			listOfJobIds.add(jobGeneOrder.getGene(i).getAllele());
		}
		return listOfJobIds;
		
	}
	
	//Get the payout for the job order
	public static int payoutForJob(IChromosome orderedJobs ){
		Gene[] jobGeneOrder = orderedJobs.getGenes();
		
		ArrayList<Job> jobsData = setupData();
		ArrayList<Object> listOfJobIds = new ArrayList<Object>();
		ArrayList<Job> sortedJobs = new ArrayList<Job>();
		
		//Getting a list of Ids from genes.
		for(int i = 0; i < jobGeneOrder.length; i++){
			listOfJobIds.add(jobGeneOrder[i].getAllele());
		}
		
		//Converting Ids we got from genes to Job objects with all the info
		for(int j =0; j < listOfJobIds.size();j++){
			for(Job jobObject : jobsData){
				int tempId = Integer.parseInt(listOfJobIds.get(j).toString());
				if (jobObject.id == tempId){
					sortedJobs.add(jobObject);
				}
			}
		}
		return Problem.score(sortedJobs);
	}
	
}

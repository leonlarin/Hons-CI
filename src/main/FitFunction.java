package main;

import java.util.ArrayList;
import java.util.Arrays;

import org.jgap.Chromosome;
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
		
		double fitness = payoutOfJobOrder;
		
		if (fitness<0){
			fitness=0;
		}
		
		return fitness;
	}
	
	public static ArrayList<Object> getJobOrderIds(IChromosome jobGeneOrder){
		ArrayList<Object> listOfJobIds = new ArrayList<Object>();
		for(int i = 0; i < jobGeneOrder.size(); i++){
			listOfJobIds.add(jobGeneOrder.getGene(i).getAllele()); // [i].getAllele());// = jobGeneOrder[i].getAllele();
		}
		return listOfJobIds;
		
	}
	
	public static int payoutForJob(IChromosome orderedJobs ){
		Gene[] jobGeneOrder = orderedJobs.getGenes();
		
		ArrayList<Job> jobsData = setupData();
		ArrayList<Object> listOfJobIds = new ArrayList<Object>();
		ArrayList<Job> sortedJobs = new ArrayList<Job>();
		
		for(int i = 0; i < jobGeneOrder.length; i++){
			listOfJobIds.add(jobGeneOrder[i].getAllele());// = jobGeneOrder[i].getAllele();
		}
		
		for(int j =0; j < listOfJobIds.size();j++){
			for(Job jobObject : jobsData){
				int tempId = Integer.parseInt(listOfJobIds.get(j).toString());
				if (jobObject.id == tempId){
					sortedJobs.add(jobObject);
				}
			}
		}
		//System.out.println(listOfJobIds);
		int score = Problem.score(sortedJobs);
		//System.out.println(score);
		
		return score;
	}
	
}

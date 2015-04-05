package main;

import java.util.*;
import setup.Job;
import setup.Problem;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Problem.loadProblem("problems/Problem1.txt");
		ArrayList<Job>  myJobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));
		//Shuffle the jobs
		Collections.shuffle(Arrays.asList(myJobs));
		System.out.println("Delivery order ");
		for(Job j: myJobs){
			System.out.print(j.id +",");
			}
		System.out.println();
		System.out.println ("Reward =" +Problem.score(myJobs));		
		}
}
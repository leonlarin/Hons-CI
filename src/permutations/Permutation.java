package permutations;
import java.util.*;
import java.math.BigInteger;

import setup.Job;
import setup.Problem;

public class Permutation {
	//Johnson-Trotter Algorithm Listing All Permutations
	//Adapted to work with lists of job objects. 
	private List<DirectionalEntity> entityList = new ArrayList<DirectionalEntity>();
	
	private BigInteger numPermutations = new BigInteger("0");
	
	private ArrayList<DirectionalEntity> sortedList = new ArrayList<DirectionalEntity>();
	
	public Permutation(ArrayList<Job> jobs) {
		for(int x=0;x<jobs.size();x++) {
			DirectionalEntity job = new DirectionalEntity(jobs.get(x), -1);
			entityList.add(job);
		}
		
		Collections.sort(entityList);
		
		sortedList.addAll(entityList);
		Collections.reverse(sortedList);
		
		//1st Permutation: SortedList
		for(DirectionalEntity e : entityList) {
			System.out.print(e.getEntity().id+" ");
		}
		System.out.println("");
		numPermutations = numPermutations.add(BigInteger.ONE);
	}
	
	public void  getPermutation() {
		long startTime = System.nanoTime();
		
		int max = 0;
		while(true) {
			ArrayList<Job> orderedJobs = new ArrayList<Job>();
			
			DirectionalEntity mobileDirectionalEntity = getMaxMobileDirectionalEntity();
			if(mobileDirectionalEntity == null) {
				// all permutations have been found
				System.out.println("Total permutations: "+numPermutations);
				break;
			}
			if(mobileDirectionalEntity.getDir() == 1) {
				// Swap this with the right element in the list
				int positionInOriginalList = entityList.indexOf(mobileDirectionalEntity);
				entityList.set(positionInOriginalList, entityList.get(positionInOriginalList+1));
				entityList.set(positionInOriginalList+1, mobileDirectionalEntity);
			}
			else {
				// Swap this with the left element in the list
				int positionInOriginalList = entityList.indexOf(mobileDirectionalEntity);
				entityList.set(positionInOriginalList, entityList.get(positionInOriginalList-1));
				entityList.set(positionInOriginalList-1, mobileDirectionalEntity);
			}
			
			// We have got a unique permutation - print this
			String repres = new String(); 
			for(DirectionalEntity e : entityList) {
				//System.out.print(e.getEntity().id+" ");
				orderedJobs.add(e.getEntity());
				repres += e.getEntity().id + " ";
			}
			int score = Problem.score(orderedJobs);
			if(score > max){
				max = score;
				//code
				long endTime = System.nanoTime(); 
				System.out.print("Payout: " + score + ", job order: " + repres +"; Took "+ (endTime - startTime) + " ns");
				System.out.println("");
			}
			
			numPermutations = numPermutations.add(BigInteger.ONE);
			
			// Now reverse the direction of all the numbers greater than mobileDirectionalNumber
			for(DirectionalEntity e : entityList) {
				if(mobileDirectionalEntity.compareTo(e) < 0)
					e.reverseDir();
			}
		}
	}
	
	/**
	 * Gets the mobile directional number by checking 
	 * the numbers in decreasing order of magnitude
	 * @return mobileDirectionalNumber, if it is exist, else null
	 */
	private DirectionalEntity getMaxMobileDirectionalEntity() {
		for(DirectionalEntity dirEntity : sortedList) {
			DirectionalEntity maxDirEntity = dirEntity;
			// Now check for the position of maxDirNum in original List
			// to see if this maxDirNum is actually a mobile directional number
			int positionInOriginalList = entityList.indexOf(maxDirEntity);
			if((maxDirEntity.getDir() == -1 && (positionInOriginalList != 0 && entityList.get(positionInOriginalList-1).compareTo(maxDirEntity) < 0)
					|| (maxDirEntity.getDir() == 1 && (positionInOriginalList != entityList.size()-1 && entityList.get(positionInOriginalList+1).compareTo(maxDirEntity) < 0)))) {
				// This is the max mobile directional number - return this
				return maxDirEntity;
			}
		}
		return null; // No more mobile directional numbers exist. All the permutations have been found.
	}
	public static void main(String[] args) {
		Problem.loadProblem("problems/Problem2.txt");
		ArrayList<Job>  myJobs = new ArrayList<Job>(Arrays.asList(Problem.getJobs()));
		//System.out.println(myJobs);
		//System.out.println("Starting Bruteforcing " + myJobsIds.size() + " jobs.");
		//System.out.println("Jobs: " + myJobsIds);
		//printCombinations(myJobsIds, myJobsIds.size());	
		
		Permutation p = new Permutation(myJobs);
		p.getPermutation();
		
		System.out.println("Done.");
  }
}

//OBSOLETE
//This is wrong and almost melted my PC
//At the point of writing I forgot about the difference between permutation and combination.
//This code bruteforces all the combinations

	/*private static List<List<Integer>> combinations(List<Integer> list, int maxLength) {
		return combinations(list, maxLength, new ArrayList(), new ArrayList());
	}
	private static List<List<Integer>> combinations(List<Integer> list, int length, List<Integer> current, List<List<Integer>> result) {
		if (length == 0) {
			List<List<Integer>> newResult =  new ArrayList<>(result);
			newResult.add(current);
			System.out.println(current);
			return newResult;
		}
		List<List<List<Integer>>> res3 = new ArrayList<>();
		for (Integer i : list) {
			List<Integer> newCurrent = new ArrayList<>(current);
			newCurrent.add(i);
			res3.add(combinations(list, length - 1, newCurrent, result));
    		}
    	List<List<Integer>> res2 = new ArrayList<>();
    		for (List<List<Integer>> lst : res3) {
    			res2.addAll(lst);
    		}
    	return res2;
	}
	public static void printCombinations(List<Integer> list, int maxLength) {
		List<List<Integer>> combs = combinations(list, maxLength);
		for (List<Integer> lst : combs) {
			String line = "";
			for (Integer i : lst) {
				line += i;
			}
			System.out.println(line);
		}
	}
*/
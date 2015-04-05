package permutations;
import setup.Job;

public class DirectionalEntity implements Comparable<DirectionalEntity> {
	
	private Job job;
	
	private int dir; // can be -1 or 1 depending on the position
	
	public DirectionalEntity(Job job, int dir) {
		this.job = job;
		this.dir = dir;
	}
	
	public DirectionalEntity() {
		// TODO Auto-generated constructor stub
	}
	

	@Override
	public int compareTo(DirectionalEntity directionalEntity) {
		return (Integer.compare(this.job.id, directionalEntity.job.id));
	}
	
	public void reverseDir() {
		this.dir = dir * (-1);
	}

	public Job getEntity() {
		return job;
	}
	 

	public int getDir() {
		return dir;
	}
}

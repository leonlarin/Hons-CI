package setup;
public class Job {
    public int id;
	int pickup;
	int setdown;
	int available;
	int[][] payments = new int[4][2];
	public int maxPayout;
}
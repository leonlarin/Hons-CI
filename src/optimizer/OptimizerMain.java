package optimizer;

import java.text.DecimalFormat;

import org.jgap.Configuration;
import org.jgap.Genotype;

public class OptimizerMain {

	public static void main(String[] args) throws Exception {
		//This is EA optimizer
		
		EAOptimizer test_subject = new EAOptimizer();
		test_subject.setPrintToConsole(false);
		test_subject.setUIMode(false);
		test_subject.setPrintToCSV(false);
		test_subject.setAnalyticsMode(true);
		EAOptimizer.problemNumber = 1;
		long startTime = System.nanoTime();
		Genotype population = test_subject.setupForEvolution();
		test_subject.findSolutions(population);
		Configuration.reset();
		long endTime = System.nanoTime();
		
		double time = (endTime - startTime) / 1000000000.0;
		String timeTrunc = new DecimalFormat("#.###").format(time);
		System.out.println("Run complete. It took: " + timeTrunc + " seconds.");

	}

}

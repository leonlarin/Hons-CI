package optimizer;

import org.jgap.Configuration;
import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.IChromosome;
import org.jgap.InvalidConfigurationException;
import org.jgap.Population;
import org.jgap.impl.DoubleGene;
import org.jgap.impl.IntegerGene;

import java.util.ArrayList;
import java.util.List;
import java.util.Random; 

@SuppressWarnings("serial")
public class CustomMutator implements GeneticOperator {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void operate( final Population a_population, 
            final List a_candidateChromosomes ) {
		 int len = a_population.size();
		 ArrayList<Integer> populationSizes = new ArrayList<>();
		 for(int populationSize = 100; populationSize<3000; populationSize+=100){
			 populationSizes.add(populationSize);
		 }
		 
		 ArrayList<Integer> mutationModifiers = new ArrayList<>();
		 for(int mutationModifier = 10; mutationModifier<1000; mutationModifier+=10){
			 mutationModifiers.add(mutationModifier);
		 }
		 
		 ArrayList<Integer> originalRates = new ArrayList<>();
		 for(int originalRate = 0; originalRate<99; originalRate+=10){
			 if(originalRate>100){originalRate=100;}
			 originalRates.add(originalRate);
		 }
		 
		 Gene[] genes = new Gene[3];
		 IChromosome temp = null;
		 for ( int i = 0; i < len; i++ ) {
			 
			 
			 try {
				genes[0] = new IntegerGene();
			} catch (InvalidConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
					 //populationSizes.get(new Random().nextInt(populationSizes.size())));
			 
			 temp.getGene(1).setAllele(mutationModifiers.get(new Random().nextInt(mutationModifiers.size())));
	
			 temp.getGene(2).setAllele(originalRates.get(new Random().nextInt(originalRates.size()))/100.0);
			 
	         a_candidateChromosomes.add(temp); 
         } 

        
	}

}

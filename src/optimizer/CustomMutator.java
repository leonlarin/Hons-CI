package optimizer;

import org.jgap.Gene;
import org.jgap.GeneticOperator;
import org.jgap.Population;

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
		 
		 Gene[] genes = new Gene[2];
		 

		 genes[0].setAllele(populationSizes.get(new Random().nextInt(populationSizes.size())));
		 
		 genes[1].setAllele(mutationModifiers.get(new Random().nextInt(mutationModifiers.size())));

		 genes[2].setAllele(originalRates.get(new Random().nextInt(originalRates.size()))/100.0);
		 
		  
         for ( int i = 0; i < len; i++ ) 
         { 
             a_candidateChromosomes.add(genes); 
         } 
        
	}

}

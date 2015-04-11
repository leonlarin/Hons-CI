package main;

import java.util.*;

import org.jgap.*;
import org.jgap.util.*;

import setup.Job;
import setup.Problem;

public class CustomSelector 
	extends NaturalSelector {
	private Population m_chromosomes;

	public CustomSelector(final Configuration a_config) throws InvalidConfigurationException{
		m_chromosomes = new Population(a_config);
	}
	@Override
	public void empty() {
		// TODO Auto-generated method stub
		//m_chromosomes.getChromosomes().clear();
		
	}

	@Override
	public boolean returnsUniqueChromosomes() {
		// TODO Auto-generated method stub
		return false;
	}

	public void select(int arg0, Population in_pop, Population out_pop) {
		
		IChromosome selectedChromosome;
		for (int i = 0; i < in_pop.size(); i++) {
			m_chromosomes = in_pop;
	        selectedChromosome = m_chromosomes.getChromosome(i);
	        ArrayList<Object> idList = FitFunction.getJobOrderIds(selectedChromosome);
	        //ArrayList<Integer> 
	        Set inputSet = new HashSet(idList);
	        if(inputSet.size() == idList.size()){
	            out_pop.addChromosome(selectedChromosome);
	        }
		}
	}

	@Override
	protected void add(IChromosome arg0) {
		// TODO Auto-generated method stub
		
	}

}

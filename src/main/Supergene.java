package main;

import org.jgap.impl.IntegerGene;
import org.jgap.supergenes.AbstractSupergene;
import org.jgap.*;

import java.util.*;

/** Supergene to hold the jobs. Valid if jobs don't repeat. */

public class Supergene extends AbstractSupergene {
    /* It is important to provide these two constructors: */
    public Supergene(final Configuration a_config, Gene[] a_genes ) throws InvalidConfigurationException
     {
    	    super(a_config, a_genes);
     }
    
    public Supergene(final Configuration a_config) throws InvalidConfigurationException
    {
    	super(a_config, new Gene[] {});
    }
    
    public Supergene()
    	    throws InvalidConfigurationException {
        this(Genotype.getStaticConfiguration());
      }

    public boolean isValid(Gene [] genes)
    {
    	List inputList = Arrays.asList(genes);
        Set inputSet = new HashSet(inputList);
        if(inputSet.size()< inputList.size()){
            return false;
        }
        return true;
   }
}
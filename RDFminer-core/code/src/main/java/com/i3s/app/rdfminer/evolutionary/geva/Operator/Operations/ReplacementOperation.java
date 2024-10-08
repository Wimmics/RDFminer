/*
Grammatical Evolution in Java
Release: GEVA-v2.0.zip
Copyright (C) 2008 Michael O'Neill, Erik Hemberg, Anthony Brabazon, Conor Gilligan 
Contributors Patrick Middleburgh, Eliott Bartley, Jonathan Hugosson, Jeff Wrigh

Separate licences for asm, bsf, antlr, groovy, jscheme, commons-logging, jsci is included in the lib folder. 
Separate licence for rieps is included in src/com folder.

This licence refers to GEVA-v2.0.

This software is distributed under the terms of the GNU General Public License.


This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
/>.
*/

/*
 * ReplacementOperation.java
 *
 * Created on March 15, 2007, 4:38 PM
 *
 */

package com.i3s.app.rdfminer.evolutionary.geva.Operator.Operations;

import com.i3s.app.rdfminer.evolutionary.geva.Exceptions.BadParameterException;
import com.i3s.app.rdfminer.evolutionary.geva.Individuals.FitnessPackage.Fitness;
import com.i3s.app.rdfminer.evolutionary.geva.Individuals.GEIndividual;
import com.i3s.app.rdfminer.evolutionary.geva.Util.Constants;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * ReplacementOperation removes replacementSize Individuals from the population
 * @author Blip
 */
public class ReplacementOperation implements Operation {
    
    protected int replacementSize;
    protected Fitness[] fitnessA;
    
    /** Creates a new instance of ReplacementOperation
     * @param size size
     */
    public ReplacementOperation(int size) {
        this.replacementSize = size;
    }
    
    /** Creates a new instance of ReplacementOperation
     * @param p properties
     */
    public ReplacementOperation(Properties p) {
        this.setProperties(p);
    }

    /**
     * Set properties
     *
     * @param p object containing properties
     */
    public void setProperties(Properties p) {
	    String key;
        int size, popSize;
	    double valueD = 1.0;
        key = Constants.POPULATION_SIZE;
        popSize = Integer.parseInt(p.getProperty(key, Constants.DEFAULT_POPULATION_SIZE));
	    key = Constants.REPLACEMENT_TYPE;
	    if(p.getProperty(key)!=null) {
	        if(p.getProperty(key).equals(Constants.STEADY_STATE)) {
		        valueD = 1.0/popSize;
	        } else {
		        if(p.getProperty(key).equals(Constants.GENERATIONAL)) {
		            valueD = 1.0;
		        }
	        }
	    } else {
            key = Constants.GENERATION_GAP;
	        try {
		        if(p.getProperty(key)!=null) {
		            valueD = Double.parseDouble(p.getProperty(key));
		            if(valueD < 0.0 || valueD > 1.0) {
			            throw new BadParameterException(key);
		            }
		        }
	        } catch(Exception e) {
		        valueD = 1.0;
		        System.out.println(e+" using default: "+valueD);
            }
        }
        size = (int)(popSize*valueD);
        this.replacementSize = size;
    }
  
    public void doOperation(GEIndividual operand) {}
    
    /**
     * Sort ascending and remove the worst individuals
     * @param operand Individuals to trim
     */
    public void doOperation(List<GEIndividual> operand) {
        this.fitnessA = rankPopulation(operand);
        removeIndividuals(operand, this.replacementSize);
    }
    
    /**
     * Sort ascending and remove the size worst individuals
     * @param operand Individuals to trim
     * @param size Number of individuals to remove
     */
    public void doOperation(List<GEIndividual> operand, int size) {
        this.fitnessA = rankPopulation(operand);
        removeIndividuals(operand, size);
    }
    
    /**
     * Remove the size number of worst Individuals
     * @param operand Individuals that might be removed
     * @param size Number of individuals to remove
     **/
    private void removeIndividuals(List<GEIndividual> operand, int size) {
        int cnt = 0;
        while(cnt < size) {
            cnt++;
            operand.remove((GEIndividual) this.fitnessA[this.fitnessA.length - cnt].getIndividual());
        }
    }

    /**
     * Rank population ascending
     * @param operand Individuals to be ranked
     * @return The ranked Individuals fitness in an array
     **/
    private Fitness[] rankPopulation(List<GEIndividual> operand) {
        Fitness[] fA = new Fitness[operand.size()];
        for(int i=0;i<fA.length;i++) {
            fA[i] = operand.get(i).getFitness();
        }
        //Sort ascending
        Arrays.sort(fA);
        return fA;
    }

    /**
     * Set replacement size
     * @param i size
     */
    public void setReplacementSize(int i) {
        this.replacementSize = i;
    }

    /**
     * Get replacement size
     * @return size
     */
    public int getReplacementSize() {
        return replacementSize;
    }
    
}

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.common;

import com.hp.hpl.jena.ontology.OntClass;

import java.util.*;

// TODO: Auto-generated Javadoc
/**
 * The Class Argument, which contains one or more Chunk objects along with their associated probabilities.
 */
public class Argument extends HltContent {

	/** The argument type. */
	private final IType argumentType;
	
	/** The associated attribute. */
	private List<IType> attributes = null;

	/** The argument distribution. */
	private BoundedList<Pair<Chunk, Float>> argumentDistribution = new BoundedList<Pair<Chunk,Float>>(10);

	/**
	 * Instantiates a new argument.
	 * 
	 * @param argumentType
	 *            the argument type
	 * @param distributionSize
	 *            the distribution size
	 */
	public Argument(IType argumentType, int distributionSize) {
		super();
		this.argumentType = argumentType;
	}
	
	/**
	 * Instantiates a new argument.  Copy constructor.
	 * 
	 * @param arg
	 *            the argument to be copied
	 */	
	public Argument(Argument arg) {
		super();
		this.argumentType = arg.argumentType;
		this.attributes = arg.attributes;
		this.argumentDistribution = arg.argumentDistribution;
	}

    

	/**
	 * Gets the argument type.
	 * 
	 * @return the argument type
	 */
	public String getArgumentType() {
		return argumentType.getType();
	}
	
	/**
	 * Gets the argument ont type.
	 * 
	 * @return the argument type
	 */
	public OntClass getArgumentOntType() {
		return argumentType.getOntClass();
	}

    public IType getArgumentIType() {
        return argumentType;
    }

	/**
	 * Sets the attribute type.
	 *
	 * @param attribute the new attribute
	 */
	public void setAttribute(IType attribute) {
                if(attributes==null)
                  {
                     attributes = new ArrayList<IType>();
                  }
		attributes.add(attribute);
	}
	
	
	/**
	 * Gets the attribute type.
	 * 
	 * @return the attribute type
	 */
	public List<String> getAttributeTypes() {
		List<String> types = new ArrayList<String>();
        if(attributes != null)
          {
             for(IType attribute : attributes)
              {
                 types.add(attribute.getType());
              }
             return types;
          }
  
        else return null;
	}
	
	/**
	 * Gets the attribute ont type.
	 * 
	 * @return the attribute type
	 */
	public List<OntClass> getAttributeOntTypes() {
	    List<OntClass> types = new ArrayList<OntClass>();
        if(attributes != null)
          {
             for(IType attribute : attributes)
              {
                 types.add(attribute.getOntClass());
              }
             return types;
          }
  
        else return null;
	}

	/**
	 * Gets the argument distribution.
	 * 
	 * @return the argument distribution
	 */
	public BoundedList<Pair<Chunk, Float>> getArgumentDistribution() {
		return argumentDistribution;
	}

	/**
	 * Sets the argument distribution.
	 * 
	 * @param argumentDistribution
	 *            the argument distribution
	 */
	public void setArgumentDistribution(
			BoundedList<Pair<Chunk, Float>> argumentDistribution) {
		this.argumentDistribution = argumentDistribution;
	}

	/**
	 * Adds the argument confidence pair.
	 * 
	 * @param argument
	 *            the argument
	 * @param confidence
	 *            the confidence
	 * @return true, if successful
	 */
	public boolean addArgumentConfidencePair(Chunk argument, float confidence) {
		Pair<Chunk, Float> pair = new Pair<Chunk, Float>(argument, confidence);
		return argumentDistribution.add(pair);
	}

	/**
	 * Gets the best argument.
	 * 
	 * @return the best argument. 
	 * If there are two chunks with the same level of confidence, the first one is returned.
	 */
	public Chunk getBestArgument() {
	    Float bestConf = new Float(-1000);
		Chunk bestArg = null;
		for(Pair<Chunk,Float> argument : argumentDistribution)
		  {
		    if(argument.getR() > bestConf)
			  {
			    bestConf = argument.getR();
				bestArg = argument.getL();
			  }
			    
		  }
		return bestArg;
	}

}

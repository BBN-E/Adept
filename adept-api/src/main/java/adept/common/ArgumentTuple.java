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

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Relation.
 */
public class ArgumentTuple extends HltContent {

	/** The type. */
	protected final IType tupleType;
	
	/** Attributes, if any. */
	protected List<IType> attributes;

	/** The arguments belonging to the argument tuple. */
	protected List<Argument> arguments = new ArrayList<Argument>();

	/** The overall confidence of the argument tuple. */
	protected float confidence;

	/** The within-text justifications. */
	protected List<Chunk> justifications = new ArrayList<Chunk>(); 
	
	/** The textual context. */
	protected Chunk context;

	/** The size of the textual context to be set. */
	protected int contextSize = 2;
	
	/**
	 * Instantiates a new relation.
	 *
	 * @param type the type
	 */
	public ArgumentTuple(IType type) {
                checkArgument(type!=null);
		this.tupleType = type;
	}

	/**
	 * Gets the arguments.
	 * 
	 * @return the arguments
	 */
	public List<Argument> getArguments() {
		return arguments;
	}

	/**
	 * Adds the argument.
	 * 
	 * @param argument
	 *            the argument
	 * @return true, if successful
	 */
	public boolean addArgument(Argument argument) {
                checkArgument(argument!=null);
		Boolean bOK = this.arguments.add(argument);
		try {
			setContext();
		}
		catch(Exception e)
		{
			System.out.println("Exception thrown while trying to set context\n");
			//e.printStackTrace();
		}
		return bOK;
	}
	
	/**
	 * Sets the context.
	 */
	protected void setContext()
	{
		if (arguments == null || arguments.size() == 0) {
			context = null;
			return;
		}
		int min = 0;
		int max = 0;
		boolean bFirst = true;
		TokenStream tokenStream = null;
		for ( Argument arg : arguments){
			for ( Pair<Chunk, Float> pair : arg.getArgumentDistribution()) {
			TokenOffset to = pair.getL().getTokenOffset();
			if ( bFirst ) tokenStream = pair.getL().getTokenStream();
			if ( bFirst || to.getBegin() < min ) min = to.getBegin(); 
			if ( bFirst || to.getEnd() > max ) max = to.getEnd(); 
			bFirst = false;
			}
		}
		int begin = Math.max(0,  min-contextSize); 
		int end = Math.min(tokenStream.size()-1,max + contextSize);
		TokenOffset tokenOffset = new TokenOffset(begin,end);
		context = new Chunk(tokenOffset, tokenStream);
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(float confidence) {
		this.confidence = confidence;
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
                checkArgument(attribute!=null);
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
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return tupleType.getType();
	}
	
	/**
	 * Gets the ont type.
	 * 
	 * @return the type
	 */
	public OntClass getOntType() {
		return tupleType.getOntClass();
	}

	/**
	 * Gets the i type.
	 *
	 * @return the i type
	 */
	public IType getIType() {
		return tupleType;
	}
	
	/**
	 * Adds justification chunk
	 * 
	 */
	public void addJustification(Chunk justification)
	{
		//checkArgument(justification != null);
		justifications.add(justification);
	}
	
	/**
	 * Gets justification chunks
	 * 
	 * @return List<Chunk> justifications
	 */
	public List<Chunk> getJustifications()
	{
		return this.justifications;
	}
}

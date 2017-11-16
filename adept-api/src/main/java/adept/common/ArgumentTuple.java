package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

import static com.google.common.base.Preconditions.checkArgument;
import java.io.Serializable;


/**
 * The Class Relation.
 */
public class ArgumentTuple extends HltContent implements Serializable {
	private static final long serialVersionUID = -5705128021475287368L;

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
	 * @return the context chunk
	 */
	public Chunk getContext()
	{
		return context;
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
	public List<IType> getAttributeOntTypes() {
	       List<IType> types = new ArrayList<IType>();

                if(attributes != null)
                  {
                     for(IType attribute : attributes)
                      {
                         types.add(attribute);
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
	 * Gets the type URI.
	 * 
	 * @return the type uri
	 */
	public String getTypeURI() {
		return tupleType.getURI();
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
	 * @param justification the chunk to add
	 */
	public void addJustification(Chunk justification)
	{
		//checkArgument(justification != null);
		justifications.add(justification);
	}
	
	/**
	 * Gets justification chunks
	 * 
	 * @return justifications
	 */
	public List<Chunk> getJustifications()
	{
		return this.justifications;
	}
}

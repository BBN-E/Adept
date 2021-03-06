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

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hp.hpl.jena.ontology.OntClass;
import java.io.Serializable;


/**
 * The Class Argument, which contains one or more Chunk objects along with their associated probabilities.
 */
public class Argument extends HltContent implements Serializable {

	private static final long serialVersionUID = 6605383741302014033L;

	/** The argument type. */
	private final IType argumentType;
	
	/** Overall confidence of this argument instance. */
	private final float confidence;
	
	/** The associated attributes. */
	private List<IType> attributes = null;

	
	/** Map containing the chunks that back up this 
	argument, along with associated confidences. */
	private BoundedList<Pair<Chunk, Float>> argumentDistribution = new BoundedList<Pair<Chunk,Float>>(10);
	
	/** Map containing the KB level entities or relations
	 * that an instance of this class resolves to, and associated 
	 * confidences. */
	private Map<KBID, Float> kbArgumentDistribution;

	/**
	 * Instantiates a new argument.
	 * 
	 * @param argumentType
	 *            the argument type
	 * @param distributionSize
	 *            the distribution size
	 * @param confidence the overall confidence value to associate with this argument
	 */
	public Argument(IType argumentType, int distributionSize, float confidence) {
		super();
        checkArgument(argumentType!=null);
		this.argumentType = argumentType;
		this.confidence = confidence;
		
		this.kbArgumentDistribution = new HashMap<KBID, Float>();
	}
	
	public Argument(IType argumentType, int distributionSize) {
		this(argumentType, distributionSize, 0);
	}
	
	/**
	 * Instantiates a new argument.  Copy constructor.
	 * 
	 * @param arg
	 *            the argument to be copied
	 */	
	public Argument(Argument arg) {
		super();
		checkArgument(arg.argumentType!=null);
        this.argumentType = arg.argumentType;
		this.attributes = arg.attributes;
		this.argumentDistribution = arg.argumentDistribution;
		this.confidence = arg.confidence;
		
		this.kbArgumentDistribution = new HashMap<KBID, Float>();
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
	 * Gets the argument confidence.
	 * 
	 * @return the argument confidence
	 */
	public float getConfidence() {
		return confidence;
	}
	
	/**
	 * Gets the argument type uri.
	 * 
	 * @return the argument type uri
	 */
	public String getArgumentTypeURI() {
		return argumentType.getURI();
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
                checkArgument(argumentDistribution!=null);
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
                checkArgument(argument!=null);
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
	
	/**
	 * Gets the kb argument distribution.
	 * 
	 * @return the kb argument distribution
	 */
	public Map<KBID, Float> getKBArgumentDistribution() {
		return kbArgumentDistribution;
	}
	
	/**
	 * Sets the kb argument distribution.
	 * 
	 * @param kbArgumentDistribution
	 *            the kb argument distribution
	 */
	public void setKBArgumentDistribution(Map<KBID, Float> kbArgumentDistribution) {
        checkArgument(kbArgumentDistribution!=null);
		this.kbArgumentDistribution = kbArgumentDistribution;
	}


	/**
	 * Adds the kb argument confidence pair.
	 *
	 * @param kbArgument the kb argument
	 * @param confidence the confidence
	 */
	public void addKBArgumentConfidencePair(KBID kbArgument, float confidence) {
                checkArgument(kbArgument != null);		
		this.kbArgumentDistribution.put(kbArgument, new Float(confidence));
	}
	
	
	/**
	 * Gets the best kb entity, i.e. the one with
	 * highest confidence.
	 *
	 * @return the best kb entity
	 */
	public KBID getBestKBArgument() {
		KBID kba = null;
		if (kbArgumentDistribution.size() == 1) {
			kba = kbArgumentDistribution.entrySet().iterator().next().getKey();
		} else {
			float score = -1000f;
			for(Map.Entry<KBID, Float> entrySet : kbArgumentDistribution.entrySet()) {
				if (entrySet.getValue() > score) {
					kba = entrySet.getKey();
					score = entrySet.getValue();
				}
			}			
		}
		return kba;
	}

}

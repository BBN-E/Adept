/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/

package adept.common;

import java.util.HashMap;
import java.util.Map;

import com.hp.hpl.jena.ontology.OntClass;

// TODO: Auto-generated Javadoc
/**
 * The Class Relation, which defines a named N-ary 
 * relationship characteristic of two or more 
 * entities or parts together.
 */
public class Relation extends ArgumentTuple {

	/** The relation id. */
	private final long relationId;

	/** The entity id distribution. */
	Map<Long, Float> relationClusterIdDistribution;

	/**
	 * Instantiates a new relation.
	 *
	 * @param relationId the relation id
	 * @param relationType the relation type
	 */
	public Relation(long relationId, IType relationType) {
		super(relationType);
		this.relationId = relationId;
		this.relationClusterIdDistribution = new HashMap<Long, Float>();
	}
	
	/**
	 * Gets the relation cluster id distribution.
	 * 
	 * @return the relation cluster id distribution
	 */
	public Map<Long, Float> getRelationClusterIdDistribution() {
		return relationClusterIdDistribution;
	}

	/**
	 * Gets the relation type.
	 * 
	 * @return the relation type
	 */
	public String getRelationType() {
		return super.getType();
	}
	
	/**
	 * Gets the relation ont type.
	 * 
	 * @return the relation type
	 */
	public OntClass getRelationOntType() {
		return super.getOntType();
	}

	/**
	 * Gets the relation i type.
	 *
	 * @return the relation i type
	 */
	public IType getRelationIType() {
		return super.getIType();
	}

	/**
	 * Sets the relation cluster id distribution.
	 * 
	 * @param relationClusterIdDistribution
	 *            the relation cluster id distribution
	 */
	public void setRelationClusterIdDistribution(Map<Long, Float> relationClusterIdDistribution) {
		this.relationClusterIdDistribution = relationClusterIdDistribution;
	}

	/**
	 * Adds the relation cluster confidence pair.
	 * 
	 * @param relationClusterId
	 *            the relation cluster id
	 * @param confidence
	 *            the confidence
	 */
	public void addRelationClusterConfidencePair(long relationClusterId, float confidence) {
		this.relationClusterIdDistribution.put(relationClusterId, new Float(confidence));
	}

	/**
	 * Gets the confidence.
	 *
	 * @param relationClusterId the relation cluster id
	 * @return the confidence
	 */
	public float getConfidence(long relationClusterId) {
		return this.relationClusterIdDistribution.get(relationClusterId);
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Entity))
			return false;
		Relation relation= (Relation) obj;
		return (relation.relationId == this.relationId && relation.tupleType.equals(this.tupleType));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int id = (int) this.relationId;
		int hash = id * 27;
		return hash;
	}
}
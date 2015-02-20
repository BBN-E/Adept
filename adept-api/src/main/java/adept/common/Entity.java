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
import java.util.Set;

import adept.data.IEntity;

// TODO: Auto-generated Javadoc
/**
 * The Class Entity, which is a thing in concrete or abstract reality 
 * that can be identified by its distinct properties, for example persons, 
 * organizations, locations, expressions of times, quantities and 
 * monetary values. The entity type is drawn from an ontology.
 * 
 * Modified to allow additional type information (a string from an open-ended set of types)
 *   and categories (similarly open-ended).
 */
public class Entity extends HltContent implements IEntity {

	/** The entity id. */
	private final long entityId;

	/** The canonical mention. */
	private EntityMention canonicalMention;

	/** The entity type. */
	private final IType entityType;

	/** The entity id distribution. */
	Map<KBEntity, Float> kbEntityDistribution;

	/** A set of attributes relating to additional type information, from open ontology */
	private Map< String, Set< String > > typeInformation;
	
	/**
	 * Instantiates a new entity.
	 * 
	 * @param entityId
	 *            the entity id
	 * @param entityType
	 *            the entity type
	 */
	public Entity(long entityId, IType entityType) {
		this.entityId = entityId;
		this.entityType = entityType;
		this.kbEntityDistribution = new HashMap<KBEntity, Float>();
		this.setTypeInformation(new HashMap< String, Set< String > >());
	}
	
	/**
	 * Gets the kb entity distribution.
	 * 
	 * @return the kb entity distribution
	 */
	public Map<KBEntity, Float> getKBEntityDistribution() {
		return kbEntityDistribution;
	}

	/**
	 * Gets the entity id.
	 * 
	 * @return the entity id
	 */
	public long getEntityId() {
		return entityId;
	}

	/**
	 * Gets the canonical mention.
	 * 
	 * @return the canonical mention
	 */
	public EntityMention getCanonicalMention() {
		return canonicalMention;
	}

	/**
	 * Sets the kb entity distribution.
	 * 
	 * @param kbEntityDistribution
	 *            the kb entity distribution
	 */
	public void setKBEntityDistribution(Map<KBEntity, Float> kbEntityDistribution) {
		this.kbEntityDistribution = kbEntityDistribution;
	}

	/**
	 * Adds the kb entity confidence pair.
	 *
	 * @param kbEntity the kb entity
	 * @param confidence the confidence
	 */
	public void addKBEntityConfidencePair(KBEntity kbEntity, float confidence) {		
		this.kbEntityDistribution.put(kbEntity, new Float(confidence));
	}

	/**
	 * Sets the canonical mentions.
	 * 
	 * @param canonicalMention
	 *            the new canonical mentions
	 */
	public void setCanonicalMentions(EntityMention canonicalMention) {
		this.canonicalMention = canonicalMention;
		this.value = canonicalMention.getValue();
	}

	/**
	 * Gets the entity type.
	 * 
	 * @return the entity type
	 */
	public IType getEntityType() {
		return entityType;
	}		

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.data.IEntity#getEntity()
	 */
	@Override
	public String getEntity() {
		// TODO Auto-generated method stub
		return this.value;
	}

	/**
	 * Gets the best kb entity.
	 *
	 * @return the best kb entity
	 */
	public KBEntity getBestKBEntity() {
		KBEntity kbe = null;
		if (kbEntityDistribution.size() == 1) {
			kbe = kbEntityDistribution.entrySet().iterator().next().getKey();
		} else {
			float score = -1000f;
			for(Map.Entry<KBEntity, Float> entrySet : kbEntityDistribution.entrySet()) {
				if (entrySet.getValue() > score) {
					kbe = entrySet.getKey();
					score = entrySet.getValue();
				}
			}			
		}
		return kbe;
	}
	
	/**
	 * Gets the kB entity by uri.
	 *
	 * @param kbUri the kb uri
	 * @return the kB entity by uri
	 */
	public KBEntity getKBEntityByUri(String kbUri) {
		KBEntity kbe = null;	
		for(Map.Entry<KBEntity, Float> entrySet : kbEntityDistribution.entrySet()) {
			if (entrySet.getKey().getKbUri().equals(kbUri)) {
				kbe = entrySet.getKey();
			}
		}
		return kbe;
	}

	/**
	 * get the additional type information
	 * @return
	 */
	
	public Map< String, Set< String > > getTypeInformation() {
		return typeInformation;
	}
	
	
	/** 
	 * set the additional type information
	 * @param mapStringToSetString 
	 */

	public void setTypeInformation(HashMap<String, Set<String>> mapStringToSetString ) {
		this.typeInformation = mapStringToSetString ;
	}

	/**
	 * set a type attribute
	 * 
	 * @param attribute
	 * @param value
	 */

	public void setTypeAttribute( String attribute, Set< String > attributeValues )
	{
		this.typeInformation.put(attribute, attributeValues);
	}
	
	
	/**
	 * get a type attribute
	 * @param attribute
	 * @return
	 */
	public Set< String > getTypeAttribute( String attribute )
	{
		return this.typeInformation.get(attribute);
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
		Entity entity = (Entity) obj;
		return (entity.value.equals(this.value) && entity.entityType.getType().equals(this.entityType.getType()));			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%s_%s", this.value, this.entityType.getType());				
		return code.hashCode();
	}


}
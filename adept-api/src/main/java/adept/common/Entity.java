/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

/*
 * 
 */
package adept.common;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import adept.data.IEntity;

import com.google.common.base.Objects;


/**
 * The Class Entity, which is a thing in concrete or abstract reality 
 * that can be identified by its distinct properties, for example persons, 
 * organizations, locations, expressions of times, quantities and 
 * monetary values. The entity types are drawn from an ontology.
 * 
 * Modified to allow additional type information (a string from an open-ended set of types)
 *   and categories (similarly open-ended).
 */
public class Entity extends HltContent implements IEntity, HasOntologizedAttributes, HasFreeTextAttributes {

	/** The entity id. */
	private final long entityId;

	/** The canonical mention. */
	private EntityMention canonicalMention;

	/** The entity type that has the highest type confidence. */
	private final IType entityType;
	
	/** type confidences for each associated entity type. */
	private Map<IType, Double> typeConfidences = new HashMap<IType, Double>();
	
	/** confidence of the instance being a coherent rela world entity. */
	private double confidence = 1.0;
	
	/** confidence that the canonicalMention instance is the correct
	 * one for this Entity. */
	private double canonicalMentionConfidence = 1.0;
	

	/** Map containing the KB level entities
	 * that an instance of this class resolves to, and associated \
	 * confidences. */
	Map<KBID, Float> kbEntityDistribution;

	/** A set of ontology based attribute-value pairs */
	private Map<IType, IType> ontologizedAttributes = new HashMap<IType, IType>();
	
	/** A set of ontology based attribute, and free text value pairs */
	private Map<IType, String> freeTextAttributes = new HashMap<IType, String>();
		
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
 
                checkArgument(entityType != null);
		this.entityType = entityType;

		this.kbEntityDistribution = new HashMap<KBID, Float>();
		this.setTypeInformation(new HashMap< String, Set< String > >());
		
		typeConfidences.put(entityType, 1.0);
	}
	
	
	/**
	 * Gets the kb entity distribution, i.e., the one with
	 * highest confidence.
	 * 
	 *  @deprecated Not for public use in the future.
     * The KB entity distribution map has been moved to
     * {@link adept.common.HltContentContainer}, and should be both
     * set and retreived from there henceforth.
	 * 
	 * @return the kb entity distribution
	 */
	@Deprecated
	public Map<KBID, Float> getKBEntityDistribution() {
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
	 * @deprecated Not for public use in the future.
	 * The KB entity distribution map has been moved to 
	 * {@link adept.common.HltContentContainer}, and should be both
	 * set and retreived from there henceforth.
	 * 
	 * Sets the kb entity distribution.
	 * 
	 * @param kbEntityDistribution
	 *            the kb entity distribution
	 */
	@Deprecated
	public void setKBEntityDistribution(Map<KBID, Float> kbEntityDistribution) {
                checkArgument(kbEntityDistribution!=null);
		this.kbEntityDistribution = kbEntityDistribution;
	}

	/**
	 * Adds the kb entity confidence pair.
	 *
	 * @deprecated Not for public use in the future.
	 * The KB entity distribution map has been moved to 
	 * {@link adept.common.HltContentContainer}, and should be both
	 * set and retreived from there henceforth.
	 * 
	 * @param kbEntity the kb entity
	 * @param confidence the confidence
	 */
	@Deprecated
	public void addKBEntityConfidencePair(KBID kbEntity, float confidence) {
                checkArgument(kbEntity != null);		
		this.kbEntityDistribution.put(kbEntity, new Float(confidence));
	}

	/**
	 * Sets the canonical mentions.
	 * 
	 * @param canonicalMention
	 *            the new canonical mentions
	 */
	public void setCanonicalMentions(EntityMention canonicalMention) {
                checkArgument(canonicalMention != null);
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
	 * @deprecated Not for public use in the future.
	 * The KB entity distribution map has been moved to 
	 * {@link adept.common.HltContentContainer}, and should be both
	 * set and retreived from there henceforth.
	 *
	 * @return the best kb entity
	 */
	@Deprecated
	public KBID getBestKBEntity() {
		KBID kbe = null;
		if (kbEntityDistribution.size() == 1) {
			kbe = kbEntityDistribution.entrySet().iterator().next().getKey();
		} else {
			float score = -1000f;
			for(Map.Entry<KBID, Float> entrySet : kbEntityDistribution.entrySet()) {
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
	/*public KBEntity getKBEntityByUri(String kbUri) {
		KBEntity kbe = null;	
		for(Map.Entry<KBEntity, Float> entrySet : kbEntityDistribution.entrySet()) {
			if (entrySet.getKey().getKbUri().equals(kbUri)) {
				kbe = entrySet.getKey();
			}
		}
		return kbe;
	}*/

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
                // TODO: Add null check?
		this.typeInformation = mapStringToSetString;
	}

	/**
	 * set a type attribute
	 * 
	 * @param attribute
	 * @param value
	 */

	public void setTypeAttribute( String attribute, Set< String > attributeValues )
	{
                checkArgument(attribute!=null && attribute.trim().length()>0);
                checkArgument(attributeValues!=null);

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
		return (entity.value.equals(this.value) && entity.entityType.getType().equals(this.entityType.getType()) && entity.entityId == this.entityId);			
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.value, this.entityType.getType(), this.entityId);	
	}
	
	/**
	 * Get ontologized attributes
	 * 
	 * @return ontologized attributes map
	 */
	public Map<IType,IType> getOntologizedAttributes()
	{
		return ontologizedAttributes;
	}
	
	/**
	 * Sets ontologized attributes
	 */
	public void setOntologizedAttributes(Map<IType,IType> ontologizedAttributes)
	{
		this.ontologizedAttributes = ontologizedAttributes;
	}
	
	/**
	 * Gets free text attributes
	 * 
	 * @return free text attributes map
	 */
	public Map<IType,String> getFreeTextAttributes()
	{
		return freeTextAttributes;
	}
	
	/**
	 * Sets free text attributes
	 */
	public void setFreeTextAttributes(Map<IType,String> freeTextAttributes)
	{
		this.freeTextAttributes = freeTextAttributes;
	}
	
	
    /**
     * The methods below are convenience methods, added to be
     * easily used by algorithms producing gender, native language
     * and email address information. Note that the same functionality
     * can be achieved by the direct use of setters and getters for the
     * ontologizedAttributes and freeTextAttributes maps.
     */
	
	/**
	 * Gets gender.
	 * 
	 * @return gender.
	 */
	public IType getGender()
	{
		try
		{
			return ontologizedAttributes.get(EntityAttributesTypeFactory.getInstance().getType("GENDER"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Sets gender.
	 */
	public void setGender(IType gender)
	{
		try
		{
			ontologizedAttributes.put(EntityAttributesTypeFactory.getInstance().getType("GENDER"), gender);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets native language.
	 * 
	 * @return nativeLanguage.
	 */
	public IType getNativeLanguage()
	{
		try
		{
			return ontologizedAttributes.get(EntityAttributesTypeFactory.getInstance().getType("NATIVE LANGUAGE"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Sets native language.
	 */
	public void setNativeLanguage(IType nativeLanguage)
	{
		try
		{
			ontologizedAttributes.put(EntityAttributesTypeFactory.getInstance().getType("NATIVE LANGUAGE"), nativeLanguage);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets email address.
	 * 
	 * @return emailAddress.
	 */
	public String getEmailAddress()
	{
		try
		{
			return freeTextAttributes.get(EntityAttributesTypeFactory.getInstance().getType("EMAIL ADDRESS"));
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * Sets email address.
	 */
	public void setEmailAddress(String emailAddress)
	{
		try
		{
			freeTextAttributes.put(EntityAttributesTypeFactory.getInstance().getType("EMAIL ADDRESS"), emailAddress);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	/**
	 * Gets confidence.
	 * 
	 * @return confidence.
	 */
	public double getEntityConfidence()
	{
		return this.confidence;
	}
	
	/**
	 * Sets confidence
	 */
	public void setEntityConfidence(double confidence)
	{
		this.confidence = confidence;
		
	}
	
	/**
	 * Gets canonical mention confidence.
	 * 
	 * @return confidence.
	 */
	public double getCanonicalMentionConfidence()
	{
		return this.canonicalMentionConfidence;
	}
	
	/**
	 * Sets canonicalMentionConfidence
	 */
	public void setCanonicalMentionConfidence(double canonicalMentionConfidence)
	{
		this.canonicalMentionConfidence = canonicalMentionConfidence;
		
	}
	
	/**
	 * Gets type confidence.
	 * 
	 * @return type confidence of the type
	 * having highest confidence.
	 */
	public double getTypeConfidence()
	{
		return this.typeConfidences.get(entityType);
	}
	
	/**
	 * Add type(s)
	 */
	public void addType(IType type, double confidence)
	{
		this.typeConfidences.put(type, confidence);
	}
	
	public void addTypes(Map<IType,Double> types)
	{
		this.typeConfidences.putAll(types);
	}
	
	
	/**
	 * Get all types
	 */
	public Map<IType,Double> getAllTypes()
	{
		return this.typeConfidences;
	}

}

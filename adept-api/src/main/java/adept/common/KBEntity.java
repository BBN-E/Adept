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

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class KBEntity represents a pointer to a Knowledge Base Entity 
 * uniquely identified by KB URI and source KB type. It derives from parent
 * class {@link adept.common.KBID}.
 */
public class KBEntity extends KBID {

	/** The entity type. */
	private final IType entityType;

	/**
	 * Instantiates a new kB entity.
     *
	 * @param kbUri the kb uri
	 * @param entityType the entity type
	 * @param value the value
	 */
	public KBEntity(String kbUri, IType entityType) {
		super(kbUri, null);
                
		checkArgument(entityType!=null);
		this.entityType = entityType;
	}
	
	/**
	 * Instantiates a new kB entity.
	 *
	 * @param kbUri the kb uri
	 * @param entityType the entity type
	 * @param sourceKB the source KB
	 */
	public KBEntity(String kbUri, IType entityType, String sourceKB) {
		super(kbUri, sourceKB);
                
		checkArgument(entityType!=null);
		this.entityType = entityType;
	}

	
	/**
	 * Gets the entity type.
	 *
	 * @return the entity type
	 */
	public IType getEntityType() {
		return entityType;
	}

}

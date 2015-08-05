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
 * The Class KBRelation represents a pointer to a Knowledge Base Entity 
 * uniquely identified by KB URI and source KB type. It derives from parent
 * class {@link adept.common.KBID}.
 */
public class KBRelation extends KBID {

	/** The relation type. */
	private final IType relationType;

	/**
	 * Instantiates a new KB relation.
     *
	 * @param kbUri the kb uri
	 * @param relationType the relation type
	 */
	public KBRelation(String kbUri, IType relationType) {
		super(kbUri, null);
                
		checkArgument(relationType!=null);
		this.relationType = relationType;
	}
	
	/**
	 * Instantiates a new kB relation.
	 *
	 * @param kbUri the kb uri
	 * @param relationType the relation type
	 * @param sourceKB the source KB
	 */
	public KBRelation(String kbUri, IType relationType, String sourceKB) {
		super(kbUri, sourceKB);
                
		checkArgument(relationType!=null);
		this.relationType = relationType;
	}

	
	/**
	 * Gets the relation type.
	 *
	 * @return the relation type
	 */
	public IType getRelationType() {
		return relationType;
	}

}

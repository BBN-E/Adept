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

import java.util.List;


/**
 * The Class JointRelationCoreference represents the container for the output of
 * the algorithm that processes both coreference resolution and relation
 * extraction simultaneously.
 */
public class JointRelationCoreference extends HltContent {

	/** The coreference. */
	private Coreference coreference;

	/** The relations. */
	private List<Relation> relations;

	/**
	 * Instantiates a new joint relation coreference.
	 */
	public JointRelationCoreference() {

	}

	/**
	 * Gets the coreference.
	 * 
	 * @return the coreference
	 */
	public Coreference getCoreference() {
		return coreference;
	}

	/**
	 * Sets the coreference.
	 * 
	 * @param coreference
	 *            the new coreference
	 */
	public void setCoreference(Coreference coreference) {
                checkArgument(coreference!=null);
		this.coreference = coreference;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return the relations
	 */
	public List<Relation> getRelations() {
		return relations;
	}

	/**
	 * Sets the relations.
	 * 
	 * @param relations
	 *            the new relations
	 */
	public void setRelations(List<Relation> relations) {
                checkArgument(relations!=null);
		this.relations = relations;
	}

}

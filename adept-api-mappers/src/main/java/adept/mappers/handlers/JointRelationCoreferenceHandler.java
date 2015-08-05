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
package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.List;


/**
 * The Class JointRelationCoreference represents the container for the output of
 * the algorithm that processes both coreference resolution and relation
 * extraction simultaneously.
 */
public class JointRelationCoreferenceHandler extends HltContentHandler implements JointRelationCoreferenceService.Iface {

	private JointRelationCoreference myJointRelationCoreference;
	/**
	 * Instantiates a new joint relation coreference.
	 */
	public JointRelationCoreferenceHandler() {
		super();
		myJointRelationCoreference = new JointRelationCoreference();
		myJointRelationCoreference.id = myItem.id;
		myJointRelationCoreference.value = myItem.value;

	}

	/**
	 * Gets the coreference.
	 * 
	 * @return myJointRelationCoreference.the coreference
	 */
	public Coreference getCoreference() {
		return myJointRelationCoreference.coreference;
	}

	/**
	 * Sets the coreference.
	 * 
	 * @param coreference
	 *            the new coreference
	 */
	public void setCoreference(Coreference coreference) {
		myJointRelationCoreference.coreference = coreference;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return myJointRelationCoreference.the relations
	 */
	public List<Relation> getRelations() {
		return myJointRelationCoreference.relations;
	}

	/**
	 * Sets the relations.
	 * 
	 * @param relations
	 *            the new relations
	 */
	public void setRelations(List<Relation> relations) {
		myJointRelationCoreference.relations = relations;
	}

	public JointRelationCoreference getJointRelationCoreference() {
		return myJointRelationCoreference;
	}

}

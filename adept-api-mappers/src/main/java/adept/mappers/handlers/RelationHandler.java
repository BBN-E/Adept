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

import java.util.ArrayList;
import java.util.List;

import com.hp.hpl.jena.ontology.OntClass;

// TODO: Auto-generated Javadoc
/**
 * The Class Relation.
 */
public class RelationHandler extends HltContentHandler implements RelationService.Iface {

	private Relation myRelation;

	/**
	 * Instantiates a new relation.
	 * 
	 * @param relationType
	 *            the relation type
	 */
	public RelationHandler(Type relationType) {
		super();
		myRelation = new Relation(0L, relationType);
		myRelation.id = myItem.id;
		myRelation.value = myItem.value;
	}

	/**
	 * Gets the arguments.
	 * 
	 * @return myRelation.the arguments
	 */
	public List<Argument> getArguments() {
		return myRelation.arguments;
	}

	/**
	 * Adds the argument.
	 * 
	 * @param argument
	 *            the argument
	 * @return myRelation.true, if successful
	 */
	public boolean addArgument(Argument argument) {
		return myRelation.arguments.add(argument);
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return myRelation.the confidence
	 */
	public double getConfidence() {
		return myRelation.confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(double confidence) {
		myRelation.confidence = confidence;
	}

	/**
	 * Gets the relation type.
	 * 
	 * @return myRelation.the relation type
	 */
	public String getRelationType() {
		return myRelation.type.getType();
	}
	
	/**
	 * Gets the relation ont type.
	 * 
	 * @return myRelation.the relation type
	 */
//	public OntClass getRelationOntType() {
//		return myRelation.relationType.getOntClass();
//	}

	public Relation getRelation() {
		return myRelation;
	}

}

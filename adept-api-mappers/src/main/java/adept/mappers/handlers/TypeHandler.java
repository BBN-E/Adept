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

import com.hp.hpl.jena.ontology.OntClass;


/**
 * The Class Type.
 */
public class TypeHandler implements TypeService.Iface {

	private Type myType;

	/**
	 * Instantiates a new type.
	 * 
	 * @param type
	 *            the type
	 */
	public TypeHandler(String type) {
		myType = new Type();
		myType.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return myType.the type
	 */
	public String getType() {
		return myType.type;
	}

	/**
	 * gets OntClass
	 */
	@Deprecated
	public OntClass getOntClass() {
		// TODO Auto-generated method stub
		return null;
	}

	public Type getTypeStruct() {
		return myType;
	}

}

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

import com.hp.hpl.jena.ontology.OntClass;

// TODO: Auto-generated Javadoc
/**
 * The interface IType for recording Ontology type.
 */
public interface IType {


	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType();
	
    /**
     * Gets the ont class.
     *
     * @return the ontclass
     */
	public OntClass getOntClass();
}

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

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Type.
 */
public class Type implements IType {

	/** The type. */
	private final String type;

	/**
	 * Instantiates a new type.
	 * 
	 * @param type
	 *            the type
	 */
	public Type(String type) {
        checkArgument(type != null);
        this.type = type;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * gets OntClass.
	 *
	 * @return the ont class
	 */
	@Deprecated
	public OntClass getOntClass() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Type))
			return false;
		Type targetType = (Type) obj;
		return (targetType.getType().equals(this.getType()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return type.hashCode();
	}

}

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

// TODO: Auto-generated Javadoc
/**
 * The Class Dependency.
 */
public class Dependency extends HltContent {

	/** The governor. */
	private final Chunk governor;

	/** The dependent. */
	private final Chunk dependent;

	/** The dependency type. */
	private final String dependencyType;

	/**
	 * Instantiates a new dependency.
	 * 
	 * @param governor
	 *            the governor
	 * @param dependent
	 *            the dependent
	 * @param dependencyType
	 *            the dependency type
	 */
	public Dependency(Chunk governor, Chunk dependent, String dependencyType) {
		super();

                checkArgument(governor != null);
                checkArgument(dependent != null);
                checkArgument(dependencyType!=null && dependencyType.trim().length()>0);
		this.governor = governor;
		this.dependent = dependent;
		this.dependencyType = dependencyType;
	}

	/**
	 * Gets the governor.
	 * 
	 * @return the governor
	 */
	public Chunk getGovernor() {
		return governor;
	}

	/**
	 * Gets the dependent.
	 * 
	 * @return the dependent
	 */
	public Chunk getDependent() {
		return dependent;
	}

	/**
	 * Gets the dependency type.
	 * 
	 * @return the dependency type
	 */
	public String getDependencyType() {
		return dependencyType;
	}

}

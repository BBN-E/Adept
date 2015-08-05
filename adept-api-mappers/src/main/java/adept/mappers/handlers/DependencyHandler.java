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


/**
 * The Class Dependency.
 */
public class DependencyHandler extends HltContentHandler implements DependencyService.Iface {

	private Dependency myDependency;

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
	public DependencyHandler(ChunkUnion governor, ChunkUnion dependent, String dependencyType) {
		super();
		myDependency = new Dependency();
		myDependency.governor = governor;
		myDependency.dependent = dependent;
		myDependency.dependencyType = dependencyType;
		myDependency.id = myItem.id;
		myDependency.value = myItem.value;
	}

	/**
	 * Gets the governor.
	 * 
	 * @return myDependency.the governor
	 */
	public ChunkUnion getGovernor() {
		return myDependency.governor;
	}

	/**
	 * Gets the dependent.
	 * 
	 * @return myDependency.the dependent
	 */
	public ChunkUnion getDependent() {
		return myDependency.dependent;
	}

	/**
	 * Gets the dependency type.
	 * 
	 * @return myDependency.the dependency type
	 */
	public String getDependencyType() {
		return myDependency.dependencyType;
	}

	public Dependency getDependency() {
		return myDependency;
	}

}

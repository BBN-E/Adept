/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
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
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
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
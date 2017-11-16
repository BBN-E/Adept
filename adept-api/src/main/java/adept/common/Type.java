package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */


import com.hp.hpl.jena.ontology.OntClass;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Type.
 */
public class Type implements IType, Serializable {

	private static final long serialVersionUID = 1426561177307936304L;
	/** The type. */
	private final String type;

  	private Type() {
	  type = null;
	}

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
	 * gets URI.
	 *
	 * @return the URI
	 */
	public String getURI() {
		return null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result;
		result = prime + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Type other = (Type) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}

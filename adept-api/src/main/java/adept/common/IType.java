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
     * Gets the uri of the type.
     *
     * @return the uri string
     */
	public String getURI();
	
	/**
	 * equals
	 * 
	 * @see Object#equals(Object obj) equals
	 */
	public boolean equals(Object obj);
	
	/**
	 * hashCode
	 * @see Object#hashCode() hashCode
	 */
	public int hashCode();
}

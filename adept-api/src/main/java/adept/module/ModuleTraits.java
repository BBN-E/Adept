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

package adept.module;


/**
 * The Class ModuleTraits are basically the descriptions/features of an
 * algorithm such as expected input/output data types and dependencies, nature
 * of NLP processing performed by the algorithm as well as other user-defined
 * key-value properties. Each algorithm is responsible for defining
 * configuration, behavior and processing traits descriptions in traits file;
 * which is XML or JSON formatted. This file will be loaded by the algorithm
 * during module activation to populate the Module traits object..
 */
public class ModuleTraits {

	/** The version. */
	private String version;

	/** The trait. */
	private String trait;

	/** The schema. */
	private String schema;

	/**
	 * Instantiates a new module traits.
	 * 
	 * @param traitsFilePath
	 *            the traits file path
	 */
	public ModuleTraits(String traitsFilePath) {

	}

	/**
	 * Gets the version.
	 * 
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the version.
	 * 
	 * @param version
	 *            the new version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets the trait.
	 * 
	 * @return the trait
	 */
	public String getTrait() {
		return trait;
	}

	/**
	 * Sets the trait.
	 * 
	 * @param trait
	 *            the new trait
	 */
	public void setTrait(String trait) {
		this.trait = trait;
	}

	/**
	 * Gets the schema.
	 * 
	 * @return the schema
	 */
	public String getSchema() {
		return schema;
	}

	/**
	 * Sets the schema.
	 * 
	 * @param schema
	 *            the new schema
	 */
	public void setSchema(String schema) {
		this.schema = schema;
	}

}
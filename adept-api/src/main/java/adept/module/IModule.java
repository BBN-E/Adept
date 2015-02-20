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

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;

// TODO: Auto-generated Javadoc
/**
 * The Interface IModule defines the interfaces that are generic across all the
 * ADEPT algorithm modules.
 */
public interface IModule {

	/**
	 * Get the ModuleTraits object that describes the behavior of the underlying
	 * algorithm.
	 * 
	 * @return the module traits
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract ModuleTraits getModuleTraits() throws AdeptModuleException;

	/**
	 * Activate. Activates the algorithm and perform one or more following
	 * tasks: 1. Parse the config and initialize member variables 2. Allocate
	 * memory for buffers, lists, maps, etc. 3. Set up database connections and
	 * file I/O pointers
	 * 
	 * @param configFilePath
	 *            the config file path
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract void activate(String configFilePath)
			throws InvalidPropertiesFormatException, IOException,
			AdeptModuleException;

	/**
	 * Get the module configurations object that contains key-value properties
	 * for the module.
	 * 
	 * @return the module config
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract ModuleConfig getModuleConfig() throws AdeptModuleException;

	/**
	 * Deactivate. This method will signal a shutdown to the algorithm. All the
	 * allocated members and connections will be cleared through this
	 * invocation.
	 * 
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract void deactivate() throws AdeptModuleException;

}
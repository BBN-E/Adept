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
package adept.module;

import java.io.IOException;
import java.util.InvalidPropertiesFormatException;


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

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
 * The Class AbstractModule is an abstract definition of an algorithm module
 * that implements the IModule interface. The overridden methods will have
 * generic implementations to handle activation, initialization and deactivation
 * of ADEPT algorithm modules
 */
public abstract class AbstractModule implements IModule {

	/** The traits. */
	@SuppressWarnings("unused")
	private ModuleTraits traits;

	/** The config. */
	private ModuleConfig config;

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IModule#getModuleTraits()
	 */
	@Override
	public ModuleTraits getModuleTraits() throws AdeptModuleException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IModule#activate(java.lang.String)
	 */
	@Override
	public void activate(String configFilePath)
			throws InvalidPropertiesFormatException, IOException,
			AdeptModuleException {
		// TODO Auto-generated method stub
		config = new ModuleConfig();
		config.loadModuleConfig(configFilePath);
		//String traitsFilePath = config.getProperty("traits");
		//traits = new ModuleTraits(traitsFilePath);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IModule#getModuleConfig()
	 */
	@Override
	public ModuleConfig getModuleConfig() throws AdeptModuleException {
		// TODO Auto-generated method stub
		return config;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IModule#deactivate()
	 */
	@Override
	public void deactivate() throws AdeptModuleException {
		// TODO Auto-generated method stub

	}

}

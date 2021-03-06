package adept.module;

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

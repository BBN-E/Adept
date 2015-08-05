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
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.io.FileInputStream;

import adept.io.Reader;


/**
 * This class extends from configuration (or properties) class that has
 * Hashtable-based Key-Values.
 */
public class ModuleConfig extends Properties {

	/**
	 * Load module config.
	 * 
	 * @param configFilePath
	 *            the config file path
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void loadModuleConfig(String configFilePath)
			throws InvalidPropertiesFormatException, IOException {
		
		//InputStream is = this.getClass().getClassLoader().getResourceAsStream(configFilePath);
		InputStream is = Reader.getInstance().findStreamInClasspathOrFileSystem(configFilePath);
                if(is ==null)
		 is = new FileInputStream(configFilePath);
		this.loadFromXML(is);
	}

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

}

/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package thrift.adept.module;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * This class extends from configuration (or properties) class that has
 * Hashtable-based Key-Values.
 */
public class ModuleConfigHandler extends Properties {

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
	public Map<String, String> loadModuleConfig(String configFilePath)
			throws InvalidPropertiesFormatException, IOException {
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(configFilePath);
		if(is ==null)
		is = new FileInputStream(configFilePath);
		this.loadFromXML(is);
		Map<String, String> map = new HashMap<String, String>();
		for (final String name: this.stringPropertyNames()) {
			map.put(name, this.getProperty(name));
		}
		return map;
	}

}

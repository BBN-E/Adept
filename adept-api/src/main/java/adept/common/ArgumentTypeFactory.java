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
package adept.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.HashMap;


/**
 * A factory for creating ArgumentType objects.
 */
public class ArgumentTypeFactory {

	/** The argument type catalog. */
	private Properties argumentTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,ArgumentTypeFactory> instances = new HashMap<String, ArgumentTypeFactory>();
	

	/**
	 * Instantiates a new argument type factory.
	 */
	private ArgumentTypeFactory() {
		
	}
	
	/**
	 * Gets the single instance of ArgumentTypeFactory.
	 *
	 * @param argTypeCatalog the arg type catalog
	 * @return single instance of ArgumentTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ArgumentTypeFactory getInstance(String argTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		ArgumentTypeFactory atFactory;
		if (instances.get(argTypeCatalog) == null) {
			atFactory = new ArgumentTypeFactory();
			atFactory.loadArgumentTypeCatalog(argTypeCatalog);
			
			//insert into hashmap
            instances.put(argTypeCatalog, atFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			atFactory = instances.get(argTypeCatalog);
		}
		return atFactory;
	}

	/**
	 * Gets the single instance of ArgumentTypeFactory.
	 * 
	 * @return single instance of ArgumentTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ArgumentTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/ArgumentTypeFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String argumentType = argumentTypeCatalog.getProperty(type);
		if (argumentType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(argumentType);
	}

	/**
	 * Load argument type catalog.
	 *
	 * @param argTypeCatalog the arg type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadArgumentTypeCatalog(String argTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		argumentTypeCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				argTypeCatalog);			
		argumentTypeCatalog.loadFromXML(is);
	}
}

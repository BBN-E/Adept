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
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;


/**
 * A factory for creating SyntacticTag objects.
 */
public class SyntacticTagFactory {

	/** The syntactic tag catalog. */
	private Properties syntacticTagCatalog;
	
	/** The instance map. */
	private static HashMap<String,SyntacticTagFactory> instances = new HashMap<String, SyntacticTagFactory>();

	/**
	 * Instantiates a new syntactic tag factory.
	 */
	private SyntacticTagFactory() {
		
	}
	
	/**
	 * Gets the single instance of SyntacticTagFactory.
	 *
	 * @param sTypeCatalog the type catalog
	 * @return single instance of SyntacticTagFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SyntacticTagFactory getInstance(String sTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		SyntacticTagFactory stFactory;
		if (instances.get(sTypeCatalog) == null) {
			stFactory = new SyntacticTagFactory();
			stFactory.loadSyntacticTagCatalog(sTypeCatalog);
			
			//insert into hashmap
           instances.put(sTypeCatalog, stFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			stFactory = instances.get(sTypeCatalog);
		}
		return stFactory;
	}
	

	/**
	 * Gets the single instance of SyntacticTagFactory.
	 * 
	 * @return single instance of SyntacticTagFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static SyntacticTagFactory getInstance() throws InvalidPropertiesFormatException, IOException {
        return getInstance("adept/common/SyntacticTagFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String eType = syntacticTagCatalog.getProperty(type);
		if (eType == null) {
			return null;
		}
		return new Type(eType);
	}

	/**
	 * Load part of speech tag catalog.
	 *
	 * @param catalogName the catalog name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadSyntacticTagCatalog(String catalogName) throws InvalidPropertiesFormatException, IOException {
		syntacticTagCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(catalogName);
		syntacticTagCatalog.loadFromXML(is);
	}
	

}

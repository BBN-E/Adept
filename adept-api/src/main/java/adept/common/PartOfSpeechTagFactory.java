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

// TODO: Auto-generated Javadoc
/**
 * A factory for creating PartOfSpeechTag objects.
 */
public class PartOfSpeechTagFactory {

	/** The pos tag catalog. */
	private Properties posTagCatalog;
	
	/** The instance map. */
	private static HashMap<String,PartOfSpeechTagFactory> instances = new HashMap<String,PartOfSpeechTagFactory>();

	/**
	 * Instantiates a new part of speech tag factory.
	 */
	private PartOfSpeechTagFactory() {

	}
	
	/**
	 * Gets the single instance of PartOfSpeechTagFactory.
	 *
	 * @param pTypeCatalog the type catalog
	 * @return single instance of PartOfSpeechTagFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static PartOfSpeechTagFactory getInstance(String pTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		PartOfSpeechTagFactory ptFactory;
		if (instances.get(pTypeCatalog) == null) {
			ptFactory = new PartOfSpeechTagFactory();
			ptFactory.loadPartOfSpeechTagCatalog(pTypeCatalog);
			
			//insert into hashmap
           instances.put(pTypeCatalog, ptFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			ptFactory = instances.get(pTypeCatalog);
		}
		return ptFactory;
	}
	

	/**
	 * Gets the single instance of PartOfSpeechTagFactory.
	 * 
	 * @return single instance of PartOfSpeechTagFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static PartOfSpeechTagFactory getInstance() throws InvalidPropertiesFormatException, IOException {
        return getInstance("adept/common/PartOfSpeechTagFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String eType = posTagCatalog.getProperty(type);
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
	private void loadPartOfSpeechTagCatalog(String catalogName) throws InvalidPropertiesFormatException, IOException {
		posTagCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(catalogName);
		posTagCatalog.loadFromXML(is);
	}

}

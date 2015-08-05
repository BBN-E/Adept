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
 * A factory for creating MentionType objects.
 */
public class MentionTypeFactory {

	/** The mention type catalog. */
	private Properties mentionTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,MentionTypeFactory> instances = new HashMap<String, MentionTypeFactory>();

	/**
	 * Instantiates a new Mention type factory.
	 */
	private MentionTypeFactory() {

	}
	
	/**
	 * Gets the single instance of MentionTypeFactory.
	 *
	 * @param menTypeCatalog the men type catalog
	 * @return single instance of MentionTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static MentionTypeFactory getInstance(String menTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		MentionTypeFactory mtFactory;
		if (instances.get(menTypeCatalog) == null) {
			mtFactory = new MentionTypeFactory();
			mtFactory.loadMentionTypeCatalog(menTypeCatalog);
			
			//insert into hashmap
           instances.put(menTypeCatalog, mtFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			mtFactory = instances.get(menTypeCatalog);
		}
		return mtFactory;
	}

	/**
	 * Gets the single instance of MentionTypeFactory.
	 * 
	 * @return single instance of MentionTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static MentionTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/MentionTypeFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String mType = mentionTypeCatalog.getProperty(type);
		if (mType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(mType);
	}

	/**
	 * Load mention type catalog.
	 *
	 * @param catalogName the catalog name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadMentionTypeCatalog(String catalogName)
			throws InvalidPropertiesFormatException, IOException {
		mentionTypeCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				catalogName);
		mentionTypeCatalog.loadFromXML(is);
	}

}

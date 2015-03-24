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
 * A factory for creating GenderType objects.
 */
public class GenderTypeFactory {

	/** The entity type catalog. */
	private Properties genderTypeCatalog;
	
    /** Singleton instance. */
	private static GenderTypeFactory gtFactory;
	
	/**
	 * Instantiates a new gender type factory.
	 */
	protected GenderTypeFactory() {

	}
	
	/**
	 * Gets the single instance of GenderTypeFactory.
	 *
	 * @param genderTypeCatalog the gender type catalog
	 * @return single instance of GenderTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static GenderTypeFactory getInstance(String genderTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		if (gtFactory == null) {
			gtFactory = new GenderTypeFactory();
			gtFactory.loadGenderTypeCatalog(genderTypeCatalog);
		}
		
		return gtFactory;
	}

	/**
	 * Gets the single instance of GenderTypeFactory.
	 * 
	 * @return single instance of GenderTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static GenderTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/GenderTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String gType = genderTypeCatalog.getProperty(type);
		if (gType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(gType);
	}

	/**
	 * Load entity type catalog.
	 *
	 * @param entTypeCatalog the ent type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadGenderTypeCatalog(String genderTypeCatalogFile)
			throws InvalidPropertiesFormatException, IOException {
		genderTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(genderTypeCatalogFile);
		genderTypeCatalog.loadFromXML(is);
	}

}

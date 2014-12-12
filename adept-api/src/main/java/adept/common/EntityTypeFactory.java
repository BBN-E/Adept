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
 * A factory for creating EntityType objects.
 */
public class EntityTypeFactory {

	/** The entity type catalog. */
	private Properties entityTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,EntityTypeFactory> instances = new HashMap<String, EntityTypeFactory>();
	

	/**
	 * Instantiates a new entity type factory.
	 */
	protected EntityTypeFactory() {

	}
	
	/**
	 * Gets the single instance of EntityTypeFactory.
	 *
	 * @param entTypeCatalog the ent type catalog
	 * @return single instance of EntityTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EntityTypeFactory getInstance(String entTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		EntityTypeFactory mtFactory;
		if (instances.get(entTypeCatalog) == null) {
			mtFactory = new EntityTypeFactory();
			mtFactory.loadEntityTypeCatalog(entTypeCatalog);
			
			//insert into hashmap
           instances.put(entTypeCatalog, mtFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			mtFactory = instances.get(entTypeCatalog);
		}
		return mtFactory;
	}

	/**
	 * Gets the single instance of EntityTypeFactory.
	 * 
	 * @return single instance of EntityTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static EntityTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/EntityTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String eType = entityTypeCatalog.getProperty(type);
		if (eType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(eType);
	}

	/**
	 * Load entity type catalog.
	 *
	 * @param entTypeCatalog the ent type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadEntityTypeCatalog(String entTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		entityTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(entTypeCatalog);
		entityTypeCatalog.loadFromXML(is);
	}

}

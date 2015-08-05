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
 * A factory for creating EntityAttributesType objects.
 */
public class EntityAttributesTypeFactory {

	/** The entity type catalog. */
	private Properties entityAttributesTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,EntityAttributesTypeFactory> instances = new HashMap<String, EntityAttributesTypeFactory>();
	

	/**
	 * Instantiates a new entity type factory.
	 */
	protected EntityAttributesTypeFactory() {

	}
	
	/**
	 * Gets the single instance of EntityAttributesTypeFactory.
	 *
	 * @param entAttributesTypeCatalog the ent type catalog
	 * @return single instance of EntityAttributesTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EntityAttributesTypeFactory getInstance(String entAttributesTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		EntityAttributesTypeFactory eatFactory;
		if (instances.get(entAttributesTypeCatalog) == null) {
			eatFactory = new EntityAttributesTypeFactory();
			eatFactory.loadEntityAttributesTypeCatalog(entAttributesTypeCatalog);
			
			//insert into hashmap
           instances.put(entAttributesTypeCatalog, eatFactory);
		}
		else {
			eatFactory = instances.get(entAttributesTypeCatalog);
		}
		return eatFactory;
	}

	/**
	 * Gets the single instance of EntityAttributesTypeFactory.
	 * 
	 * @return single instance of EntityAttributesTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static EntityAttributesTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/EntityAttributesTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String eaType = entityAttributesTypeCatalog.getProperty(type);
		if (eaType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(eaType);
	}

	/**
	 * Load entity attributes type catalog.
	 *
	 * @param entAttributesTypeCatalog the ent type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadEntityAttributesTypeCatalog(String entAttributesTypeCatalogFile)
			throws InvalidPropertiesFormatException, IOException {
		entityAttributesTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(entAttributesTypeCatalogFile);
		entityAttributesTypeCatalog.loadFromXML(is);
	}

}

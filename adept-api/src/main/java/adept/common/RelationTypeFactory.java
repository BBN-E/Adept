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
 * A factory for creating RelationType objects.
 */
public class RelationTypeFactory {

	/** The relation type catalog. */
	private Properties relationTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,RelationTypeFactory> instances = new HashMap<String, RelationTypeFactory>();

	/**
	 * Instantiates a new relation type factory.
	 */
	private RelationTypeFactory() {

	}

	/**
	 * Gets the single instance of RelationTypeFactory.
	 *
	 * @param relTypeCatalog the rel type catalog
	 * @return single instance of RelationTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static RelationTypeFactory getInstance(String relTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		RelationTypeFactory rtFactory;
		if (instances.get(relTypeCatalog) == null) {
			rtFactory = new RelationTypeFactory();
			rtFactory.loadRelationTypeCatalog(relTypeCatalog);
			
			//insert into hashmap
           instances.put(relTypeCatalog, rtFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			rtFactory = instances.get(relTypeCatalog);
		}
		return rtFactory;
	}
	
	
	/**
	 * Gets the single instance of RelationTypeFactory.
	 * 
	 * @return single instance of RelationTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static RelationTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/RelationTypeFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String relationType = relationTypeCatalog.getProperty(type);
		if (relationType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(relationType);
	}
	
	/**
	 * Load relation type catalog.
	 *
	 * @param catalogName the catalog name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadRelationTypeCatalog(String catalogName)
			throws InvalidPropertiesFormatException, IOException {
		relationTypeCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				catalogName);		
		relationTypeCatalog.loadFromXML(is);
	}
}

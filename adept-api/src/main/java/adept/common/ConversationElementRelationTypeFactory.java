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
 * A factory for creating ConversationElementRelationType objects.
 */
public class ConversationElementRelationTypeFactory {

	/** The conversation element attributes type catalog. */
	private Properties ceRelationTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,ConversationElementRelationTypeFactory> instances = new HashMap<String, ConversationElementRelationTypeFactory>();
	

	/**
	 * Instantiates a new conversation element attributes type factory.
	 */
	protected ConversationElementRelationTypeFactory() {

	}
	
	/**
	 * Gets the single instance of ConversationElementRelationTypeFactory.
	 *
	 * @param ceRelationTypeCatalog the conversation element relation type catalog
	 * @return single instance of ConversationElementRelationTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ConversationElementRelationTypeFactory getInstance(String ceRelationTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		ConversationElementRelationTypeFactory certFactory;
		if (instances.get(ceRelationTypeCatalog) == null) {
			certFactory = new ConversationElementRelationTypeFactory();
			certFactory.loadConversationElementRelationTypeCatalog(ceRelationTypeCatalog);
			
			//insert into hashmap
			instances.put(ceRelationTypeCatalog, certFactory);
		}
		else {
			certFactory = instances.get(ceRelationTypeCatalog);
		}
		return certFactory;
	}

	/**
	 * Gets the single instance of ConversationElementRelationTypeFactory.
	 * 
	 * @return single instance of ConversationElementRelationTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ConversationElementRelationTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/ConversationElementRelationTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String cerType = ceRelationTypeCatalog.getProperty(type);
		if (cerType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(cerType);
	}

	/**
	 * Load conversation element relation type catalog.
	 *
	 * @param ceRelationTypeCatalog the ceRelation type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadConversationElementRelationTypeCatalog(String ceRelationTypeCatalogFile)
			throws InvalidPropertiesFormatException, IOException {
		ceRelationTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(ceRelationTypeCatalogFile);
		ceRelationTypeCatalog.loadFromXML(is);
	}

}

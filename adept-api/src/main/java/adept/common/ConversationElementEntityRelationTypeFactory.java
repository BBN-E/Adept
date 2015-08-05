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
 * A factory for creating ConversationElementEntityRelationsType objects.
 */
public class ConversationElementEntityRelationTypeFactory {

	/** The conversation element to entity relation type catalog. */
	private Properties ceeRelationTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,ConversationElementEntityRelationTypeFactory> instances = new HashMap<String, ConversationElementEntityRelationTypeFactory>();
	

	/**
	 * Instantiates a new conversation element attributes type factory.
	 */
	protected ConversationElementEntityRelationTypeFactory() {

	}
	
	/**
	 * Gets the single instance of ConversationElementEntityRelationsTypeFactory.
	 *
	 * @param ceeRelationTypeCatalog the conversation element attributes type catalog
	 * @return single instance of ConversationElementEntityRelationsTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ConversationElementEntityRelationTypeFactory getInstance(String ceeRelationTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		ConversationElementEntityRelationTypeFactory ceatFactory;
		if (instances.get(ceeRelationTypeCatalog) == null) {
			ceatFactory = new ConversationElementEntityRelationTypeFactory();
			ceatFactory.loadConversationElementEntityRelationsTypeCatalog(ceeRelationTypeCatalog);
			
			//insert into hashmap
           instances.put(ceeRelationTypeCatalog, ceatFactory);
		}
		else {
			ceatFactory = instances.get(ceeRelationTypeCatalog);
		}
		return ceatFactory;
	}

	/**
	 * Gets the single instance of ConversationElementEntityRelationsTypeFactory.
	 * 
	 * @return single instance of ConversationElementEntityRelationsTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ConversationElementEntityRelationTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/ConversationElementEntityRelationTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String ceerType = ceeRelationTypeCatalog.getProperty(type);
		if (ceerType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(ceerType);
	}

	/**
	 * Load conversation element to entity type catalog.
	 *
	 * @param ceeRelationTypeCatalog the ceeRelation type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadConversationElementEntityRelationsTypeCatalog(String ceeRelationTypeCatalogFile)
			throws InvalidPropertiesFormatException, IOException {
		ceeRelationTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(ceeRelationTypeCatalogFile);
		ceeRelationTypeCatalog.loadFromXML(is);
	}

}

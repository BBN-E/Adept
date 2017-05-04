package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

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

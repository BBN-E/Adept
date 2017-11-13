/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/

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
 * A factory for creating ConversationElementAttributesType objects.
 */
public class ConversationElementAttributesTypeFactory {

	/** The conversation element attributes type catalog. */
	private Properties ceAttributesTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,ConversationElementAttributesTypeFactory> instances = new HashMap<String, ConversationElementAttributesTypeFactory>();
	

	/**
	 * Instantiates a new conversation element attributes type factory.
	 */
	protected ConversationElementAttributesTypeFactory() {

	}
	
	/**
	 * Gets the single instance of ConversationElementAttributesTypeFactory.
	 *
	 * @param ceAttributesTypeCatalog the conversation element attributes type catalog
	 * @return single instance of ConversationElementAttributesTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ConversationElementAttributesTypeFactory getInstance(String ceAttributesTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		ConversationElementAttributesTypeFactory ceatFactory;
		if (instances.get(ceAttributesTypeCatalog) == null) {
			ceatFactory = new ConversationElementAttributesTypeFactory();
			ceatFactory.loadConversationElementAttributesTypeCatalog(ceAttributesTypeCatalog);
			
			//insert into hashmap
           instances.put(ceAttributesTypeCatalog, ceatFactory);
		}
		else {
			ceatFactory = instances.get(ceAttributesTypeCatalog);
		}
		return ceatFactory;
	}

	/**
	 * Gets the single instance of ConversationElementAttributesTypeFactory.
	 * 
	 * @return single instance of ConversationElementAttributesTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ConversationElementAttributesTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/ConversationElementAttributesTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String ceaType = ceAttributesTypeCatalog.getProperty(type);
		if (ceaType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(ceaType);
	}

	/**
	 * Load entity attributes type catalog.
	 *
	 * @param ceAttributesTypeCatalog the ceAttributes type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadConversationElementAttributesTypeCatalog(String ceAttributesTypeCatalogFile)
			throws InvalidPropertiesFormatException, IOException {
		ceAttributesTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(ceAttributesTypeCatalogFile);
		ceAttributesTypeCatalog.loadFromXML(is);
	}

}
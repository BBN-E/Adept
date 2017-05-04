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

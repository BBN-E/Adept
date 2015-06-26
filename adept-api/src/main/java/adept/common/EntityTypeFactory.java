/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
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
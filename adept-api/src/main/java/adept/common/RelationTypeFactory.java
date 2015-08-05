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
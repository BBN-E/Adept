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
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating SyntacticTag objects.
 */
public class SyntacticTagFactory {

	/** The syntactic tag catalog. */
	private Properties syntacticTagCatalog;
	
	/** The instance map. */
	private static HashMap<String,SyntacticTagFactory> instances = new HashMap<String, SyntacticTagFactory>();

	/**
	 * Instantiates a new syntactic tag factory.
	 */
	private SyntacticTagFactory() {
		
	}
	
	/**
	 * Gets the single instance of SyntacticTagFactory.
	 *
	 * @param sTypeCatalog the type catalog
	 * @return single instance of SyntacticTagFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static SyntacticTagFactory getInstance(String sTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		SyntacticTagFactory stFactory;
		if (instances.get(sTypeCatalog) == null) {
			stFactory = new SyntacticTagFactory();
			stFactory.loadSyntacticTagCatalog(sTypeCatalog);
			
			//insert into hashmap
           instances.put(sTypeCatalog, stFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			stFactory = instances.get(sTypeCatalog);
		}
		return stFactory;
	}
	

	/**
	 * Gets the single instance of SyntacticTagFactory.
	 * 
	 * @return single instance of SyntacticTagFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static SyntacticTagFactory getInstance() throws InvalidPropertiesFormatException, IOException {
        return getInstance("adept/common/SyntacticTagFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String eType = syntacticTagCatalog.getProperty(type);
		if (eType == null) {
			return null;
		}
		return new Type(eType);
	}

	/**
	 * Load part of speech tag catalog.
	 *
	 * @param catalogName the catalog name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadSyntacticTagCatalog(String catalogName) throws InvalidPropertiesFormatException, IOException {
		syntacticTagCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(catalogName);
		syntacticTagCatalog.loadFromXML(is);
	}
	

}
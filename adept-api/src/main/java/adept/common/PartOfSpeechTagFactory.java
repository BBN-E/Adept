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
 * A factory for creating PartOfSpeechTag objects.
 */
public class PartOfSpeechTagFactory {

	/** The pos tag catalog. */
	private Properties posTagCatalog;
	
	/** The instance map. */
	private static HashMap<String,PartOfSpeechTagFactory> instances = new HashMap<String,PartOfSpeechTagFactory>();

	/**
	 * Instantiates a new part of speech tag factory.
	 */
	private PartOfSpeechTagFactory() {

	}
	
	/**
	 * Gets the single instance of PartOfSpeechTagFactory.
	 *
	 * @param pTypeCatalog the type catalog
	 * @return single instance of PartOfSpeechTagFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static PartOfSpeechTagFactory getInstance(String pTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		PartOfSpeechTagFactory ptFactory;
		if (instances.get(pTypeCatalog) == null) {
			ptFactory = new PartOfSpeechTagFactory();
			ptFactory.loadPartOfSpeechTagCatalog(pTypeCatalog);
			
			//insert into hashmap
           instances.put(pTypeCatalog, ptFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			ptFactory = instances.get(pTypeCatalog);
		}
		return ptFactory;
	}
	

	/**
	 * Gets the single instance of PartOfSpeechTagFactory.
	 * 
	 * @return single instance of PartOfSpeechTagFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static PartOfSpeechTagFactory getInstance() throws InvalidPropertiesFormatException, IOException {
        return getInstance("adept/common/PartOfSpeechTagFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String eType = posTagCatalog.getProperty(type);
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
	private void loadPartOfSpeechTagCatalog(String catalogName) throws InvalidPropertiesFormatException, IOException {
		posTagCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(catalogName);
		posTagCatalog.loadFromXML(is);
	}

}
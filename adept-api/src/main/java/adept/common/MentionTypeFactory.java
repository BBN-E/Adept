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
 * A factory for creating MentionType objects.
 */
public class MentionTypeFactory {

	/** The mention type catalog. */
	private Properties mentionTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,MentionTypeFactory> instances = new HashMap<String, MentionTypeFactory>();

	/**
	 * Instantiates a new Mention type factory.
	 */
	private MentionTypeFactory() {

	}
	
	/**
	 * Gets the single instance of MentionTypeFactory.
	 *
	 * @param menTypeCatalog the men type catalog
	 * @return single instance of MentionTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static MentionTypeFactory getInstance(String menTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		MentionTypeFactory mtFactory;
		if (instances.get(menTypeCatalog) == null) {
			mtFactory = new MentionTypeFactory();
			mtFactory.loadMentionTypeCatalog(menTypeCatalog);
			
			//insert into hashmap
           instances.put(menTypeCatalog, mtFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			mtFactory = instances.get(menTypeCatalog);
		}
		return mtFactory;
	}

	/**
	 * Gets the single instance of MentionTypeFactory.
	 * 
	 * @return single instance of MentionTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static MentionTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/MentionTypeFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String mType = mentionTypeCatalog.getProperty(type);
		if (mType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(mType);
	}

	/**
	 * Load mention type catalog.
	 *
	 * @param catalogName the catalog name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadMentionTypeCatalog(String catalogName)
			throws InvalidPropertiesFormatException, IOException {
		mentionTypeCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				catalogName);
		mentionTypeCatalog.loadFromXML(is);
	}

}
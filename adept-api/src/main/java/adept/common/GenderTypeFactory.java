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
 * A factory for creating GenderType objects.
 */
public class GenderTypeFactory {

	/** The entity type catalog. */
	private Properties genderTypeCatalog;
	
    /** Singleton instance. */
	private static GenderTypeFactory gtFactory;
	
	/**
	 * Instantiates a new gender type factory.
	 */
	protected GenderTypeFactory() {

	}
	
	/**
	 * Gets the single instance of GenderTypeFactory.
	 *
	 * @param genderTypeCatalog the gender type catalog
	 * @return single instance of GenderTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static GenderTypeFactory getInstance(String genderTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		if (gtFactory == null) {
			gtFactory = new GenderTypeFactory();
			gtFactory.loadGenderTypeCatalog(genderTypeCatalog);
		}
		
		return gtFactory;
	}

	/**
	 * Gets the single instance of GenderTypeFactory.
	 * 
	 * @return single instance of GenderTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static GenderTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/GenderTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String gType = genderTypeCatalog.getProperty(type);
		if (gType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(gType);
	}

	/**
	 * Load entity type catalog.
	 *
	 * @param entTypeCatalog the ent type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadGenderTypeCatalog(String genderTypeCatalogFile)
			throws InvalidPropertiesFormatException, IOException {
		genderTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(genderTypeCatalogFile);
		genderTypeCatalog.loadFromXML(is);
	}

}
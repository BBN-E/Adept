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
 * A factory for creating NativeLanguageType objects.
 */
public class NativeLanguageTypeFactory {

	/** The entity type catalog. */
	private Properties nativeLanguageTypeCatalog;
	
    /** Singleton instance. */
	private static NativeLanguageTypeFactory nltFactory;
	
	/**
	 * Instantiates a new nativeLanguage type factory.
	 */
	protected NativeLanguageTypeFactory() {

	}
	
	/**
	 * Gets the single instance of NativeLanguageTypeFactory.
	 *
	 * @param nativeLanguageTypeCatalog the nativeLanguage type catalog
	 * @return single instance of NativeLanguageTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static NativeLanguageTypeFactory getInstance(String nativeLanguageTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		if (nltFactory == null) {
			nltFactory = new NativeLanguageTypeFactory();
			nltFactory.loadNativeLanguageTypeCatalog(nativeLanguageTypeCatalog);
		}
		
		return nltFactory;
	}

	/**
	 * Gets the single instance of NativeLanguageTypeFactory.
	 * 
	 * @return single instance of NativeLanguageTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static NativeLanguageTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/NativeLanguageTypeFactory.xml"); 
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String nlType = nativeLanguageTypeCatalog.getProperty(type);
		if (nlType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(nlType);
	}

	/**
	 * Load entity type catalog.
	 *
	 * @param entTypeCatalog the ent type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadNativeLanguageTypeCatalog(String nativeLanguageTypeCatalogFile)
			throws InvalidPropertiesFormatException, IOException {
		nativeLanguageTypeCatalog = new Properties();
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(nativeLanguageTypeCatalogFile);
		nativeLanguageTypeCatalog.loadFromXML(is);
	}

}
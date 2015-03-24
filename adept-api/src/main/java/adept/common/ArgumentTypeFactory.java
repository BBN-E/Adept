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
 * A factory for creating ArgumentType objects.
 */
public class ArgumentTypeFactory {

	/** The argument type catalog. */
	private Properties argumentTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,ArgumentTypeFactory> instances = new HashMap<String, ArgumentTypeFactory>();
	

	/**
	 * Instantiates a new argument type factory.
	 */
	private ArgumentTypeFactory() {
		
	}
	
	/**
	 * Gets the single instance of ArgumentTypeFactory.
	 *
	 * @param argTypeCatalog the arg type catalog
	 * @return single instance of ArgumentTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static ArgumentTypeFactory getInstance(String argTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		ArgumentTypeFactory atFactory;
		if (instances.get(argTypeCatalog) == null) {
			atFactory = new ArgumentTypeFactory();
			atFactory.loadArgumentTypeCatalog(argTypeCatalog);
			
			//insert into hashmap
            instances.put(argTypeCatalog, atFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			atFactory = instances.get(argTypeCatalog);
		}
		return atFactory;
	}

	/**
	 * Gets the single instance of ArgumentTypeFactory.
	 * 
	 * @return single instance of ArgumentTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static ArgumentTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/ArgumentTypeFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String argumentType = argumentTypeCatalog.getProperty(type);
		if (argumentType == null) {
			return new Type("UNKNOWN");
		}
		return new Type(argumentType);
	}

	/**
	 * Load argument type catalog.
	 *
	 * @param argTypeCatalog the arg type catalog
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadArgumentTypeCatalog(String argTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		argumentTypeCatalog = new Properties();
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(
				argTypeCatalog);			
		argumentTypeCatalog.loadFromXML(is);
	}
}
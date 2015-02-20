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

package thrift.adept.module;

import java.io.IOException;
import java.io.InputStream;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.io.FileInputStream;
import java.util.Map;
import java.util.HashMap;

// TODO: Auto-generated Javadoc
/**
 * This class extends from configuration (or properties) class that has
 * Hashtable-based Key-Values.
 */
public class ModuleConfigHandler extends Properties {

	/**
	 * Load module config.
	 * 
	 * @param configFilePath
	 *            the config file path
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public Map<String, String> loadModuleConfig(String configFilePath)
			throws InvalidPropertiesFormatException, IOException {
		
		InputStream is = this.getClass().getClassLoader().getResourceAsStream(configFilePath);
		if(is ==null)
		is = new FileInputStream(configFilePath);
		this.loadFromXML(is);
		Map<String, String> map = new HashMap<String, String>();
		for (final String name: this.stringPropertyNames()) {
			map.put(name, this.getProperty(name));
		}
		return map;
	}

}
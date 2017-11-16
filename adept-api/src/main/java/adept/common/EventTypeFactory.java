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

import java.io.FileInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.util.HashMap;


/**
 * A factory for creating EventType objects.
 */
public class EventTypeFactory {

	/** The Event type catalog. */
	private Properties eventTypeCatalog;
	
	/** The instance map. */
	private static HashMap<String,EventTypeFactory> instances = new HashMap<String, EventTypeFactory>();

	/**
	 * Instantiates a new Event Type factory.
	 */
	protected EventTypeFactory() {

	}
	
	/**
	 * Gets the single instance of EventTypeFactory.
	 *
	 * @param evTypeCatalog the ev type catalog
	 * @return single instance of EventTypeFactory
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static EventTypeFactory getInstance(String evTypeCatalog)
			throws InvalidPropertiesFormatException, IOException {
		EventTypeFactory etFactory;
		if (instances.get(evTypeCatalog) == null) {
			etFactory = new EventTypeFactory();
			etFactory.loadEventTypeCatalog(evTypeCatalog);
			
			//insert into hashmap
           instances.put(evTypeCatalog, etFactory);
		}
		else {
			//System.out.println("Found a factory instance previously made!");
			etFactory = instances.get(evTypeCatalog);
		}
		return etFactory;
	}

	/**
	 * Gets the single instance of EventTypeFactory.
	 * 
	 * @return single instance of EventTypeFactory
	 * @throws InvalidPropertiesFormatException
	 *             the invalid properties format exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static EventTypeFactory getInstance()
			throws InvalidPropertiesFormatException, IOException {
		return getInstance("adept/common/EventTypeFactory.xml");
	}

	/**
	 * Gets the type.
	 * 
	 * @param type
	 *            the type
	 * @return the type
	 */
	public Type getType(String type) {
		String eType = eventTypeCatalog.getProperty(type);
		if (eType == null) {
			return null;
		}
		return new Type(eType);
	}

	/**
	 * Load Event type catalog.
	 *
	 * @param catalogName the catalog name
	 * @throws InvalidPropertiesFormatException the invalid properties format exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void loadEventTypeCatalog(String catalogName)
			throws InvalidPropertiesFormatException, IOException {
		eventTypeCatalog = new Properties();
		
		try (FileInputStream fis = new FileInputStream(ClassLoader.getSystemResource(
				catalogName).getFile())) {
		  eventTypeCatalog.loadFromXML(fis);
		}
	}

}

/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.kbapi;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import adept.io.Reader;

/**
 * Reads the KBParameters.xml configuration file, and sets all KB parameters at
 * the outset.
 * 
 */
public class KBParameters {

	private static String defaultConfigFilename = "KBParameters.xml";

	public final String tripleStoreUrl;

	public final String metadataUrl;

	public final String metadataUsername;

	public final String metadataPassword;

	/**
	 * Load a KBParameters from the KBParameters file in the adept-kb package.
	 * 
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public KBParameters() throws InvalidPropertiesFormatException, IOException {
		this(new Object() {
		}.getClass().getPackage().getName().replace(".", "/") + "/" + defaultConfigFilename);
	}

	/**
	 * Load a KBParameters from an arbitrary file path.
	 * 
	 * @param configFilePath
	 * @throws InvalidPropertiesFormatException
	 * @throws IOException
	 */
	public KBParameters(String configFilePath) throws InvalidPropertiesFormatException, IOException {
		Properties testAppConfig = new Properties();
		DataInputStream dis = new DataInputStream(
				Reader.findStreamInClasspathOrFileSystem(configFilePath));
		testAppConfig.loadFromXML(dis);

		// instantiate KBParameters
		tripleStoreUrl = testAppConfig.getProperty("tripleStoreUrl");
		metadataUrl = testAppConfig.getProperty("metadataUrl");
		metadataUsername = testAppConfig.getProperty("metadataUsername");
		metadataPassword = testAppConfig.getProperty("metadataPassword");
	}

	/**
	 * Load a KBParameters by passing the parameters directly.
	 * 
	 * @param tripleStoreURL
	 * @param metadataURL
	 * @param metadataUsername
	 * @param metadataPassword
	 */
	public KBParameters(String tripleStoreURL, String metadataURL, String metadataUsername,
			String metadataPassword) {
		this.tripleStoreUrl = tripleStoreURL;
		this.metadataUrl = metadataURL;
		this.metadataUsername = metadataUsername;
		this.metadataPassword = metadataPassword;
	}
}

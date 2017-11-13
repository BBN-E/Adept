/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

package adept.kbapi;

/*-
 * #%L
 * adept-kb
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

import java.io.DataInputStream;
import java.io.IOException;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;
import java.io.Serializable;
import adept.io.Reader;

/**
 * Reads the KBParameters.xml configuration file, and sets all KB parameters at
 * the outset.
 * 
 */
public class KBParameters implements Serializable{

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_QUERY_URL_SUFFIX = "/query";
	
	private static final String DEFAULT_UPDATE_URL_SUFFIX = "/update";

	private static String defaultConfigFilename = "KBParameters.xml";

	public final String tripleStoreUrl;

	public final String metadataUrl;

	public final String metadataUsername;

	public final String metadataPassword;

	public final String queryURLSuffix;
	
	public final String updateURLSuffix;
	
	public final boolean useParliamentUpdates;
	

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
		
		String querySuffix = testAppConfig.getProperty("queryURLSuffix");
		if (querySuffix == null){
			queryURLSuffix = DEFAULT_QUERY_URL_SUFFIX;
		}else{
			queryURLSuffix = querySuffix;
		}
		
		String updateSuffix = testAppConfig.getProperty("updateURLSuffix");
		if (updateSuffix == null){
			updateURLSuffix = DEFAULT_UPDATE_URL_SUFFIX;
		}else{
			updateURLSuffix = updateSuffix;
		}
		
		String useParliament = testAppConfig.getProperty("useParliamentUpdates");
		if (useParliament == null){
			useParliamentUpdates = false;
		}else{
			useParliamentUpdates = Boolean.parseBoolean(useParliament);
		}
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
		this(tripleStoreURL, metadataURL, metadataUsername, metadataPassword, DEFAULT_QUERY_URL_SUFFIX, DEFAULT_UPDATE_URL_SUFFIX, false);
	}
	
	/**
	 * Load a KBParameters by passing the parameters directly.
	 * 
	 * @param tripleStoreURL
	 * @param metadataURL
	 * @param metadataUsername
	 * @param metadataPassword
	 * @param queryURLSuffix
	 */
	public KBParameters(String tripleStoreURL, String metadataURL, String metadataUsername,
			String metadataPassword, String queryURLSuffix, String updateURLSuffix, boolean useParliamentUpdates) {
		this.tripleStoreUrl = tripleStoreURL;
		this.metadataUrl = metadataURL;
		this.metadataUsername = metadataUsername;
		this.metadataPassword = metadataPassword;
		this.queryURLSuffix = queryURLSuffix;
		this.updateURLSuffix = updateURLSuffix;
		this.useParliamentUpdates = useParliamentUpdates;
	}
}
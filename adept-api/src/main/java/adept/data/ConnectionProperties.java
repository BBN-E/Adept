package adept.data;

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

import adept.io.Reader;
import java.io.IOException;
import java.util.Properties;


/**
 * The Class ConnectionProperties.
 */
public class ConnectionProperties extends Properties {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -7308666192314442172L;

    public ConnectionProperties() {
        super();
    }

	public ConnectionProperties(String configFile) {
		try {
			this.loadFromXML(Reader.findStreamInClasspathOrFileSystem(configFile));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

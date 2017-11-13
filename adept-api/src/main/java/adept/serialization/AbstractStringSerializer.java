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

package adept.serialization;

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

import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Abstract superclass for serializers that serialize from and to a string-type format.
 */
public abstract class AbstractStringSerializer extends AbstractSerializer {

	protected XStream xstream;

	/**
	 * Serialize to string.
	 * @param object The object to serialize
	 * @return A string representing the object
	 * @throws UnsupportedEncodingException
	 */
	public String serializeToString(Object object) throws UnsupportedEncodingException {
		return xstream.toXML(object);
	}

	/**
	 * Deserialize from string.
	 * @param data A string representing a serialized object
	 * @param c The class type to which to deserialize
	 * @return The deserialized object
	 * @throws UnsupportedEncodingException
	 */
	public Object deserializeFromString(String data, Class<?> c) throws UnsupportedEncodingException {
		xstream.setClassLoader(c.getClassLoader());
		return xstream.fromXML(data);
	}
}
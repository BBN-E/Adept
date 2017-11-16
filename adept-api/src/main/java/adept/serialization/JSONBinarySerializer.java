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


/**
 * Performs compressed binary serialization and deserialization,
 * where the binary is based on an JSON string as returned from
 * {@link JSONStringSerializer#serializeToString(Object)}.
 */
public class JSONBinarySerializer extends AbstractBinarySerializer {

	public JSONBinarySerializer() {
		stringSerializer = new JSONStringSerializer();
	}

	@Override
	public SerializationType getSerializationType() {
		return SerializationType.JSON_BINARY;
	}
}
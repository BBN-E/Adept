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

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Abstract superclass for serializers that serialize from and to a compressed binary-type format.
 * The resulting byte array is based on a serialized string as returned from the corresponding implementation
 * of {@link AbstractStringSerializer#serializeToString(Object)}.
 */
public abstract class AbstractBinarySerializer extends AbstractSerializer {

	protected AbstractStringSerializer stringSerializer;

	/**
	 * Serialize to byte array.
	 * @param object The object to serialize
	 * @return A byte array representing the object
	 * @throws UnsupportedEncodingException
	 */
	public byte[] serializeToByteArray(Object object) throws UnsupportedEncodingException {
		try {
			String serialized = stringSerializer.serializeToString(object);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(serialized.getBytes("UTF-8"));
			gzip.close();
			byte[] ret = out.toByteArray();
			out.close();
			return ret;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Deserialize from byte array.
	 * @param data A byte array representing a serialized object
	 * @param c The class type to which to deserialize
	 * @return The deserialized object
	 * @throws UnsupportedEncodingException
	 */
	public Object deserializeFromByteArray(byte[] data, Class<?> c) throws UnsupportedEncodingException {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(in);
			BufferedReader bf = new BufferedReader(new InputStreamReader(gzip, "UTF-8"));
			StringBuilder output = new StringBuilder();
			String line;
			while ((line = bf.readLine()) != null) {
				output.append(line);
			}
			bf.close();
			gzip.close();
			in.close();
			return stringSerializer.deserializeFromString(output.toString(), c);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}

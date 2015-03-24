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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.serialization;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class BinaryFromJSONSerializer.
 */
public class BinaryFromJSONSerializer extends AbstractSerializer implements
		ISerializer {

	/** The jsons. */
	private JSONSerializer jsons;

	/**
	 * Instantiates a new binary from json serializer.
	 * 
	 * @param serializationType
	 *            the serialization type
	 */
	public BinaryFromJSONSerializer(SerializationType serializationType) {
		super(serializationType);
		jsons = new JSONSerializer(SerializationType.JSON);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.serialization.ISerializer#serializeAsByteArray(java.lang.Object)
	 */
	@Override
	public byte[] serializeAsByteArray(Object object)
			throws UnsupportedEncodingException {
		try {
			String serialized = jsons.serializeAsString(object);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			GZIPOutputStream gzip = new GZIPOutputStream(out);
			gzip.write(serialized.getBytes("UTF-8"));
			gzip.close();
			return out.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.serialization.ISerializer#deserializeByteArray(byte[],
	 * java.lang.Class)
	 */
	@Override
	public Object deserializeByteArray(byte[] data, Class<? extends Object> c) {
		try {
			// uncompress
			ByteArrayInputStream in = new ByteArrayInputStream(data);
			GZIPInputStream gzip = new GZIPInputStream(in);
			BufferedReader bf = new BufferedReader(new InputStreamReader(gzip,
					"UTF-8"));
			String output = "";
			String line;
			while ((line = bf.readLine()) != null) {
				output += line;
			}
			return jsons.deserializeString(output, c);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.serialization.ISerializer#serializeAsString(java.lang.Object)
	 */
	@Override
	public String serializeAsString(Object object)
			throws UnsupportedEncodingException {
		// unimplemented
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.serialization.ISerializer#deserializeString(java.lang.String,
	 * java.lang.Class)
	 */
	@Override
	public Object deserializeString(String data, Class<? extends Object> c)
			throws UnsupportedEncodingException {
		// unimplemented
		return null;
	}

}
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

import java.io.UnsupportedEncodingException;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.json.JettisonMappedXmlDriver;

// TODO: Auto-generated Javadoc
/**
 * JSON serializer. We use the XStream library for XML with an additional
 * JettisonMappedDriver to serialize and deserialize into and from JSON as well.
 */
public class JSONSerializer extends AbstractSerializer implements ISerializer {
	// the serializer instance
	/** The xstream. */
	private XStream xstream;

	/**
	 * Instantiates a new jSON serializer.
	 * 
	 * @param serializationType
	 *            the serialization type
	 */
	public JSONSerializer(SerializationType serializationType) {
		super(serializationType);
		xstream = new XStream(new JettisonMappedXmlDriver());
        xstream.autodetectAnnotations(true);
	}

	/**
	 * Serializes the object and returns a String object.
	 * 
	 * @param object
	 *            the object
	 * @return the string
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	@Override
	public String serializeAsString(Object object)
			throws UnsupportedEncodingException {
		String json = xstream.toXML(object);
		// System.out.println("In serializer class: " + "\n" + json);
		return json;
	}

	/**
	 * Desrializes input string into object of class specified in argument.
	 * 
	 * @param data
	 *            the data
	 * @param c
	 *            the c
	 * @return the object
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	@Override
	public Object deserializeString(String data, Class<? extends Object> c)
			throws UnsupportedEncodingException {
        xstream.setClassLoader(c.getClassLoader());
		Object object = xstream.fromXML(data);
		return object;
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
		// Unimplemented yet
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.serialization.ISerializer#deserializeByteArray(byte[],
	 * java.lang.Class)
	 */
	@Override
	public Object deserializeByteArray(byte[] data, Class<? extends Object> c)
			throws UnsupportedEncodingException {
		// Unimplemented yet
		return null;
	}

}
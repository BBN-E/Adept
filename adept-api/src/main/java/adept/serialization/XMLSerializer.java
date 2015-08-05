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


/**
 * Performs XML serialization.
 */
public class XMLSerializer extends AbstractSerializer implements ISerializer {
	// Serializer instance
	/** The xstream. */
	private XStream xstream;

	/**
	 * Instantiates a new xML serializer.
	 * 
	 * @param serializationType
	 *            the serialization type
	 */
	public XMLSerializer(SerializationType serializationType) {
		super(serializationType);
		xstream = new XStream();
        xstream.autodetectAnnotations(true);
	}

	/**
	 * Method to serialize input object into string.
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

		String xml = xstream.toXML(object);
		// System.out.println("In serializer class: " + "\n" + xml);
		return xml;
	}

	/**
	 * Method to deserialize input object from string.
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

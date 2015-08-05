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


/**
 * Abstract class for serialization that enforces a srialization type to be
 * specified. Must be extended by all serializer class implementations.
 */
public abstract class AbstractSerializer {

	/** The serialization type. */
	private final SerializationType serializationType;

	/**
	 * Instantiates a new abstract serializer.
	 * 
	 * @param serializationType
	 *            the serialization type
	 */
	public AbstractSerializer(SerializationType serializationType) {
		this.serializationType = serializationType;
	}

	/**
	 * Gets the serialization type.
	 * 
	 * @return the serialization type
	 */
	public SerializationType getSerializationType() {
		return serializationType;
	}

}

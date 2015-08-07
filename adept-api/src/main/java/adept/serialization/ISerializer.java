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


/**
 * Interface that supports methods for serialization.
 */
public interface ISerializer {

	// Method to serialize object into byte array
	/**
	 * Serialize as byte array.
	 * 
	 * @param object
	 *            the object
	 * @return the byte[]
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public abstract byte[] serializeAsByteArray(Object object)
			throws UnsupportedEncodingException;

	// Method to read object from byte array
	/**
	 * Deserialize byte array.
	 * 
	 * @param data
	 *            the data
	 * @param c
	 *            the c
	 * @return the object
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public abstract Object deserializeByteArray(byte[] data,
			Class<? extends Object> c) throws UnsupportedEncodingException;

	// Method to serialize object into a string
	/**
	 * Serialize as string.
	 * 
	 * @param object
	 *            the object
	 * @return the string
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public abstract String serializeAsString(Object object)
			throws UnsupportedEncodingException;

	// Method to read object from input string
	/**
	 * Deserialize string.
	 * 
	 * @param data
	 *            the data
	 * @param c
	 *            the c
	 * @return the object
	 * @throws UnsupportedEncodingException
	 *             the unsupported encoding exception
	 */
	public abstract Object deserializeString(String data,
			Class<? extends Object> c) throws UnsupportedEncodingException;

}
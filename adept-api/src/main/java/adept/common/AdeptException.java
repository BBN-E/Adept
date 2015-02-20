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

package adept.common;

import java.io.Serializable;


// TODO: Auto-generated Javadoc
/**
 * The Class AdeptException.
 * 
 * @author akumar
 * 
 *         This is the parent class for all exceptions in ADEPT
 */

public class AdeptException extends Exception implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates new AdeptException *.
	 */
	public AdeptException() {

	}
	
	/**
	 * Constructs a new AdeptException with message and cause.
	 *
	 * @param message the message
	 * @param cause the cause
	 */ 
	public AdeptException(String message, Throwable cause) {
		super(message,cause);
	}
	
	/**
	 * Constructs a new AdeptException with message.
	 *
	 * @param message the message
	 */ 
	public AdeptException(String message) {
		super(message);
	}
	
}
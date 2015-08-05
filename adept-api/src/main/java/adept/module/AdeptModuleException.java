/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.module;

import adept.common.AdeptException;


/**
 * AdeptModuleException is to handle exception in the modules.
 */
public class AdeptModuleException extends AdeptException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9150777915172004026L;

	 /**
 	 * Instantiates new AdeptModuleException *.
 	 */
        public AdeptModuleException() {

        }
        
        /**
         * Constructs a new AdeptModuleException with message and cause.
         *
         * @param message the message
         * @param cause the cause
         */
        public AdeptModuleException(String message, Throwable cause) {
                super(message,cause);
        }
        
        /**
         * Constructs a new AdeptModuleException with message.
         *
         * @param message the message
         */
        public AdeptModuleException(String message) {
                super(message);
        }
        


}

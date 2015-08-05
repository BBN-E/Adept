/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.utilities;

import adept.common.Document;
import adept.common.TokenStream;


/**
 * The Interface ITokenizer.
 */
public interface ITokenizer {
	// document can be null
	/**
	 * Tokenize.
	 * 
	 * @param text
	 *            the text
	 * @param document
	 *            the document
	 * @return the token stream
	 */
	TokenStream tokenize(String text, Document document);

}

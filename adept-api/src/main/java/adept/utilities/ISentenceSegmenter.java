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

import adept.common.Sentence;
import adept.common.TokenStream;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Interface ITokenizer.
 */
public interface ISentenceSegmenter {
	// document can be null
	/**
	 * Make sentences.
	 *
	 * @param text the text
	 * @param tokenstream the tokenstream
	 * @return A list of sentences
	 */
	List<Sentence> getSentences(String text,TokenStream tokenstream);
}

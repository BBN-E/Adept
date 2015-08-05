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
package adept.utilities;

import adept.common.Sentence;
import adept.common.TokenStream;
import java.util.List;


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
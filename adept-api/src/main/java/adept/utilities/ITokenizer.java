package adept.utilities;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

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

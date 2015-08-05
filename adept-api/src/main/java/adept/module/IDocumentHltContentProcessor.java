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

package adept.module;

import java.util.List;

import adept.common.Document;
import adept.common.HltContent;
import adept.common.HltContentContainer;


/**
 * The Interface IDocumentHltContentProcessor defines the interfaces that will
 * be implemented by ADEPT modules that process a Document for HL content
 * extraction and need additional (or previously extracted) HLT metadata of
 * specific HltContent type to produce as output one or more HltContent objects
 * of that type.
 * 
 * @param <T>
 *            the generic type
 *
 * @deprecated Use {@link IDocumentProcessor} instead.
 */
@Deprecated
public interface IDocumentHltContentProcessor<T extends HltContent> {

	/**
	 * This definition of the process method takes a Document and
	 * HltContentContainer as input and returns as output the list of HltContent
	 * objects which include results from HLT text processing.
	 * 
	 * @param document
	 *            the document
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the list
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract List<T> process(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method.
	 * 
	 * @param document
	 *            the document
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract long processAsync(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;

	/**
	 * This method is invoked multiple times as a polling mechanism to get the
	 * output from the asynchronous process method for the particular requestId.
	 * The boolean return type indicates whether output is ready.
	 * 
	 * @param requestId
	 *            the request id
	 * @param hltContents
	 *            the hlt contents
	 * @return the boolean
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract Boolean tryGetResult(long requestId, List<T> hltContents)
			throws AdeptModuleException;

}
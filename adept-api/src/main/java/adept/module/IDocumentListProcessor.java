/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

import adept.common.DocumentList;
import adept.common.HltContentContainer;

import java.util.List;


/**
 * The Interface IDocumentListProcessor defines the interfaces that will be
 * implemented by ADEPT modules that process list of Documents and returns
 * HltContentContainer object containing one or more objects of HltContent type.
 */
public interface IDocumentListProcessor {

	/**
	 * This definition of the process method takes a DocumentList and
	 * HltContentContainer as input and returns as output the HltContentContainer
	 * object which includes the results from processing.
	 * 
	 * @param documentList
	 *            the document list
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the hlt content container
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	@Deprecated
	public abstract HltContentContainer process(DocumentList documentList,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method.
	 * 
	 * @param documentList
	 *            the document list
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	@Deprecated
	public abstract long processAsync(DocumentList documentList,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;
	
	/**
	 * This definition of the process method takes a DocumentList as input 
	 * and returns as list of HltContentContainer
	 * objects (one per input document) which include 
	 * the results from processing.
	 * 
	 * @param documentList
	 *            the document list
	 * @return List of hlt content container objects
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract List<HltContentContainer> process(DocumentList documentList)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method.
	 * 
	 * @param documentList
	 *            the document list
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract long processAsync(DocumentList documentList)
			throws AdeptModuleException;


	/**
	 * This method is invoked multiple times as a polling mechanism to get the
	 * output from the asynchronous process method for the particular requestId.
	 * The boolean return type indicates whether output is ready.
	 * 
	 * @param requestId
	 *            the request id
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the boolean
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract Boolean tryGetResult(long requestId,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;
}
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

import adept.common.Document;
import adept.common.HltContentContainer;


/**
 * The Interface IDocumentProcessor defines the interfaces that will be
 * implemented by ADEPT modules that for HL content extraction and need
 * additional (or previously extracted) HLT metadata available in
 * HltContentContainer to output an augmented or new HltContentContainer
 * containing one or more objects of HltContent type.
 */
public interface IDocumentProcessor {

	/**
	 * @deprecated - Deprecated because it is redundant to pass in a Document object
	 * as argument in addition to an HltContentContainer. This is because the
	 * HltContentContainer already contains a getDocument() method to retrieve
	 * the document. Note that one HltContentContainer only holds the results of
	 * algorithms run over a *single* document.
	 * 
	 * The method has been replaced by two separate process
	 * methods, one of which only takes in a document for processing, and
	 * the other takes in an HltContentContainer object.
	 * 
	 * This definition of the process method takes a Document and
	 * HltContentContainer as input and returns as output the
	 * HltContentContainer object which includes the results from processing..
	 * 
	 * @param document
	 *            the document
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the hlt content container
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	@Deprecated
	public abstract HltContentContainer process(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;

	/**
	 * @deprecated - Deprecated because it is redundant to pass in a Document object
	 * as argument in addition to an HltContentContainer. This is because the
	 * HltContentContainer already contains a getDocument() method to retrieve
	 * the document. Note that one HltContentContainer only holds the results of
	 * algorithms run over a *single* document.
	 * 
	 * The method has been replaced by two separate processAsync
	 * methods, one of which only takes in a document for processing, and
	 * the other takes in an HltContentContainer object.
	 * 
	 * This is asynchronous definition of the Process method. The invocation of
	 * this method returns a long 'requestId' as output parameter. This
	 * requestId is used to poll for the result of the asynchronous process..
	 * 
	 * @param document
	 *            the document
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	@Deprecated
	public abstract long processAsync(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;
	
	/**
	 * This definition of the process method takes a Document as input and returns as output the
	 * HltContentContainer object which includes the results from processing..
	 * 
	 * @param document
	 *            the document
	 * @return the hlt content container
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract HltContentContainer process(Document document)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method. The invocation of
	 * this method returns a long 'requestId' as output parameter. This
	 * requestId is used to poll for the result of the asynchronous process..
	 * 
	 * @param document
	 *            the document
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract long processAsync(Document document)
			throws AdeptModuleException;
	
	/**
	 * This definition of the process method takes an
	 * HltContentContainer as input and populates it
	 * with the results from processing.
	 * 
	 * @param hltContentContainer
	 *            the hlt content container
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract void process(HltContentContainer hltContentContainer)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method. The invocation of
	 * this method returns a long 'requestId' as output parameter. This
	 * requestId is used to poll for the result of the asynchronous process.
	 * 
	 * @param hltContentContainer
	 *            the hlt content container
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract long processAsync(HltContentContainer hltContentContainer)
			throws AdeptModuleException;

	/**
	 * This method is invoked multiple times as a polling mechanism to get the
	 * output from the asynchronous process method for a requestId. The boolean
	 * return type indicates whether the output is ready or not..
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

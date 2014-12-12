/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.module;

import adept.common.Document;
import adept.common.HltContentContainer;

// TODO: Auto-generated Javadoc
/**
 * The Interface IDocumentProcessor defines the interfaces that will be
 * implemented by ADEPT modules that for HL content extraction and need
 * additional (or previously extracted) HLT metadata available in
 * HltContentContainer to output an augmented or new HltContentContainer
 * containing one or more objects of HltContent type.
 */
public interface IDocumentProcessor {

	/**
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
	public abstract HltContentContainer process(Document document,
			HltContentContainer hltContentContainer)
			throws AdeptModuleException;

	/**
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
	public abstract long processAsync(Document document,
			HltContentContainer hltContentContainer)
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

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

import adept.common.DocumentList;
import adept.common.HltContentContainer;

// TODO: Auto-generated Javadoc
/**
 * The Interface IDocumentListProcessor defines the interfaces that will be
 * implemented by ADEPT modules that process list of Documents and returns
 * HltContentContainer object containing one or more objects of HltContent type.
 */
public interface IDocumentListProcessor {

	/**
	 * This definition of the process method takes a DocumentList and
	 * HltContenContainer as input and returns as output the HltContentContainer
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
	public abstract long processAsync(DocumentList documentList,
			HltContentContainer hltContentContainer)
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

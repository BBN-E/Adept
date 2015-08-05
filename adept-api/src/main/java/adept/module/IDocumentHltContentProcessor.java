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

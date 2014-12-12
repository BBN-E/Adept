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

// TODO: Auto-generated Javadoc
/**
 * The IConversationProcessor Interface defines the interfaces that will be
 * implemented by ADEPT modules that process CUBISM Conversation objects..
 */
public interface IConversationProcessor {

	/**
	 * This definition of the process method takes a raw conversation as input
	 * and returns as output an annotated Conversation object which includes the
	 * results from processing.
	 * 
	 * @param conversation
	 *            the conversation
	 * @return the conversation
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract Document process(Document conversation)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method.
	 * 
	 * @param conversation
	 *            the conversation
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract long processAsync(Document conversation)
			throws AdeptModuleException;

	/**
	 * This method is invoked multiple times as a polling mechanism to get the
	 * output from the asynchronous process method for the particular requestId.
	 * The boolean return type indicates whether output is ready.
	 * 
	 * @param requestId
	 *            the request id
	 * @param conversation
	 *            the conversation
	 * @return the boolean
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract Boolean tryGetResult(long requestId,
			Document conversation) throws AdeptModuleException;
}

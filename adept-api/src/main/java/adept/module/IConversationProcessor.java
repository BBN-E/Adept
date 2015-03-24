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
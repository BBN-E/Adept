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

import adept.common.HltContent;
import adept.common.Sentence;


/**
 * The Interface ISentenceProcessor defines the interfaces that will be
 * implemented by ADEPT modules that perform sentence-level processing. .
 * 
 * @param <T>
 *            the generic type
 */
public interface ISentenceProcessor<T extends HltContent> {

	/**
	 * This definition of the process method takes a Sentence object as input
	 * and returns a list of HltContent objects which includes the results from
	 * text processing..
	 * 
	 * @param sentence
	 *            the sentence
	 * @return the list
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract List<T> process(Sentence sentence)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method. The invocation of
	 * this method returns a long 'requestId' as output parameter. This
	 * requestId is used to poll for the result of the asynchronous process..
	 * 
	 * @param sentence
	 *            the sentence
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract long processAsync(Sentence sentence)
			throws AdeptModuleException;

	/**
	 * This method is invoked multiple times as a polling mechanism to get the
	 * output from the asynchronous process method for a requestId. The boolean
	 * return type indicates whether the output is ready or not..
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
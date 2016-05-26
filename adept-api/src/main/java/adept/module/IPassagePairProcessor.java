/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

/*
 * 
 */
package adept.module;

import java.util.List;

import adept.common.HltContent;
import adept.common.Passage;


/**
 * The Interface IPassagePairProcessor defines the interfaces that will be
 * implemented by ADEPT modules that perform processing on pair of passages.
 * 
 * @param <T>
 *            the generic type
 */
public interface IPassagePairProcessor<T extends HltContent> {

	/**
	 * This definition of the process method takes two Passage objects as input
	 * and returns a list of MetaContent objects which includes the results from
	 * text processing..
	 * 
	 * @param passage1
	 *            the passage1
	 * @param passage2
	 *            the passage2
	 * @return the list
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract List<T> process(Passage passage1, Passage passage2)
			throws AdeptModuleException;

	/**
	 * This is asynchronous definition of the Process method. The invocation of
	 * this method returns a long 'requestId' as output parameter. This
	 * requestId is used to poll for the result of the asynchronous process..
	 * 
	 * @param passage1
	 *            the passage1
	 * @param passage2
	 *            the passage2
	 * @return the long
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract long processAsync(Passage passage1, Passage passage2)
			throws AdeptModuleException;

	/**
	 * This method is invoked multiple times as a polling mechanism to get the
	 * output from the asynchronous process method for a requestId. The boolean
	 * return type indicates whether the output is ready or not..
	 * 
	 * @param requestId
	 *            the request id
	 * @param metaContents
	 *            the meta contents
	 * @return the boolean
	 * @throws AdeptModuleException
	 *             the adept module exception
	 */
	public abstract Boolean tryGetResult(long requestId, List<T> metaContents)
			throws AdeptModuleException;

}

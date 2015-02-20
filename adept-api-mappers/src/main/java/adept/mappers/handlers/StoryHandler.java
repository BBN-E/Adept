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

package adept.mappers.handlers;

import thrift.adept.common.*;


import java.util.List;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Story.
 */
public class StoryHandler extends ChunkHandler implements StoryService.Iface {

	private Story myStory;

	/**
	 * Instantiates a new story.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param sequenceId
	 *            the sequence id
	 * @param topicLabels
	 *            the topic labels
	 */
	public StoryHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, ArrayList<String> topicLabels) {
		super(tokenOffset, tokenStream);
		myStory = new Story();
		myStory.sequenceId = sequenceId;
		myStory.topicLabels = topicLabels;
		myStory.id = myItem.id;
		myStory.value = myItem.value;
		myStory.tokenOffset = myChunk.tokenOffset;
		myStory.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myStory.the sequence id
	 */
	public long getSequenceId() {
		return myStory.sequenceId;
	}

	/**
	 * Gets the topic labels.
	 * 
	 * @return myStory.the topic labels
	 */
	public List<String> getTopicLabels() {
		return myStory.topicLabels;
	}

	public Story getStory() {
		return myStory;
	}

}
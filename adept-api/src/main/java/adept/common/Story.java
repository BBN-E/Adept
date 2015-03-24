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

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Story.
 */
public class Story extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The content type. */
	private final ArrayList<String> topicLabels;

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
	public Story(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, ArrayList<String> topicLabels) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;

                checkArgument(topicLabels!=null);
                this.topicLabels = topicLabels;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the topic labels.
	 * 
	 * @return the topic labels
	 */
	public ArrayList<String> getTopicLabels() {
		return topicLabels;
	}

}
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

// TODO: Auto-generated Javadoc
/**
 * The Class TaggedChunk.
 */
public class TaggedChunk extends Chunk {

	/** The tag. */
	private final String tag;

	/** The sequence id. */
	private final long sequenceId;

	
	/**
	 * Instantiates a new tagged chunk.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tag
	 *            the chunk's tag
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public TaggedChunk(long sequenceId, String tag, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
                checkArgument(tag!=null && tag.trim().length()>0);
		this.tag = tag;
	}

	/**
	 * Gets the chunk's tag.
	 * 
	 * @return the chunk's tag
	 */
	public String getTaggedChunkTag() {
		return tag;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}
}
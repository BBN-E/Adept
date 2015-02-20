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

// TODO: Auto-generated Javadoc
/**
 * The Class Passage.
 */
public class PassageHandler extends ChunkHandler implements PassageService.Iface {

	private Passage myPassage;

	/**
	 * Instantiates a new passage.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public PassageHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		myPassage = new Passage();
		// TODO Auto-generated constructor stub
		myPassage.sequenceId = sequenceId;
		myPassage.id = myItem.id;
		myPassage.value = myItem.value;
		myPassage.tokenOffset = myChunk.tokenOffset;
		myPassage.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return myPassage.the content type
	 */
	public String getContentType() {
		return myPassage.contentType;
	}

	/**
	 * Sets the content type.
	 * 
	 * @param contentType
	 *            the new content type
	 */
	public void setContentType(String contentType) {
		myPassage.contentType = contentType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myPassage.the sequence id
	 */
	public long getSequenceId() {
		return myPassage.sequenceId;
	}

	public Passage getPassage() {
		return myPassage;
	}

}
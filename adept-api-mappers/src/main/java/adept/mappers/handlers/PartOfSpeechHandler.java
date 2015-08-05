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


/**
 * The Class PartOfSpeech.
 */
public class PartOfSpeechHandler extends ChunkHandler implements PartOfSpeechService.Iface {

	private PartOfSpeech myPartOfSpeech;

	/**
	 * Instantiates a new part of speech.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public PartOfSpeechHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		myPartOfSpeech = new PartOfSpeech();
		// TODO Auto-generated constructor stub
		myPartOfSpeech.sequenceId = sequenceId;
		myPartOfSpeech.id = myItem.id;
		myPartOfSpeech.value = myItem.value;
		myPartOfSpeech.tokenOffset = myChunk.tokenOffset;
		myPartOfSpeech.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the pos tag.
	 * 
	 * @return myPartOfSpeech.the pos tag
	 */
	public Type getPosTag() {
		return myPartOfSpeech.posTag;
	}

	/**
	 * Sets the pos tag.
	 * 
	 * @param posTag
	 *            the new pos tag
	 */
	public void setPosTag(Type posTag) {
		myPartOfSpeech.posTag = posTag;
	}

	/**
	 * Gets the part of speech tag.
	 * 
	 * @return myPartOfSpeech.the part of speech tag
	 */
	public Type getPartOfSpeechTag() {
		return myPartOfSpeech.posTag;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myPartOfSpeech.the sequence id
	 */
	public long getSequenceId() {
		return myPartOfSpeech.sequenceId;
	}

	public PartOfSpeech getPartOfSpeech() {
		return myPartOfSpeech;
	}

}
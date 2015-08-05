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
 * The Class SyntacticChunk.
 */
public class SyntacticChunkHandler extends ChunkHandler implements SyntacticChunkService.Iface {

	private SyntacticChunk mySyntacticChunk;

	/**
	 * Instantiates a new syntactic chunk.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param scType
	 *            the sc type
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	SyntacticChunkHandler(long sequenceId, Type scType, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		mySyntacticChunk = new SyntacticChunk();
		// TODO Auto-generated constructor stub
		mySyntacticChunk.sequenceId = sequenceId;
		mySyntacticChunk.scType = scType;
		mySyntacticChunk.id = myItem.id;
		mySyntacticChunk.value = myItem.value;
		mySyntacticChunk.tokenOffset = myChunk.tokenOffset;
		mySyntacticChunk.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the syntactic chunk type.
	 * 
	 * @return mySyntacticChunk.the syntactic chunk type
	 */
	public Type getSyntacticChunkType() {
		return mySyntacticChunk.scType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return mySyntacticChunk.the sequence id
	 */
	public long getSequenceId() {
		return mySyntacticChunk.sequenceId;
	}
	public SyntacticChunk getSyntacticChunk() {
		return mySyntacticChunk;
	}

}
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
 * The Class SyntacticChunk.
 */
public class SyntacticChunk extends Chunk {

	/** The sc type. */
	private final IType scType;

	/** The sequence id. */
	private final long sequenceId;

	
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
	public SyntacticChunk(long sequenceId, Type scType, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;
                checkArgument(scType!=null);
		this.scType = scType;
	}

	/**
	 * Gets the syntactic chunk type.
	 * 
	 * @return the syntactic chunk type
	 */
	public IType getSyntacticChunkType() {
		return scType;
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
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

// TODO: Auto-generated Javadoc
/**
 * The Class Chunk is defined as containing one or more tokens. A chunk is
 * assumed to be HLT algorithm generated metadata, and, hence inherits from
 * HltContent class.
 */
public class ChunkHandler extends HltContentHandler implements ChunkService.Iface {

	protected Chunk myChunk;

	/**
	 * Instantiates a new chunk.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public ChunkHandler(TokenOffset tokenOffset, TokenStream tokenStream) {
		super();
		myChunk = new Chunk();
		myChunk.tokenOffset = tokenOffset;
		myChunk.tokenStream = tokenStream;
		if(tokenOffset!=null)
			setValue();
	}

	/**
	 * Creates and sets the value by concatenating all the tokens within a
	 * chunk.
	 */
	private void setValue() {
		StringBuilder sb = new StringBuilder();
		for (int i = (int) myChunk.tokenOffset.getBeginIndex(); i <= myChunk.tokenOffset.getEndIndex(); i++) {
			// System.out.println(String.format("totalSize = %d, i = %d",
			// tokenStream.size(),i));
			sb.append(myChunk.tokenStream.getTokenList().get(i).getValue());
			sb.append(" ");
		}
		myChunk.value = sb.toString().trim();
	}

	/**
	 * Gets the token stream.
	 * 
	 * @return myChunk.the token stream
	 */
	public TokenStream getTokenStream() {
		return myChunk.tokenStream;
	}

	/**
	 * Gets the token offset.
	 * 
	 * @return myChunk.the token offset
	 */
	public TokenOffset getTokenOffset() {
		return myChunk.tokenOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Item#getValue()
	 */
	@Override
	public String getValue() {
		return myChunk.value;
	}

	/**
	 * Sets the token stream.
	 * 
	 * @param tokenStream
	 *            the new token stream
	 */
	public void setTokenStream(TokenStream tokenStream) {
		myChunk.tokenStream = tokenStream;
	}

	/**
	 * Contains.
	 * 
	 * @param chunk
	 *            the chunk
	 * @return myChunk.true, if successful
	 */
	public boolean Contains(ChunkUnion chunk) {
//		if (myChunk.tokenStream.getTokenizerType() == chunk.tokenStream
//				.getTokenizerType()) {
//			if ((chunk.getChunk().tokenOffset.getBeginIndex() >= myChunk.tokenOffset.getBeginIndex())
//					&& (chunk.getChunk().tokenOffset.getEndIndex() <= myChunk.tokenOffset.getEndIndex()))
//				return true;
//		}
		return false;
	}

	/**
	 * Gets the containing chunk.
	 * 
	 * @param chunks
	 *            the chunks
	 * @return myChunk.the containing chunk
	 */
	public ChunkUnion getContainingChunk(List<ChunkUnion> chunks) {
//		for (ChunkUnion chunk : chunks) {
//			if (myChunk.tokenStream.getTokenizerType() == chunk.tokenStream
//					.getTokenizerType()) {
//				if ((chunk.getChunk().tokenOffset.getBeginIndex() >= myChunk.tokenOffset
//						.getBeginIndex())
//						&& (chunk.getChunk().tokenOffset.getEndIndex() <= myChunk.tokenOffset
//								.getEndIndex()))
//					return chunk.getChunk();
//			}
//		}
		return null;
	}

	/**
	 * Gets the matching chunk.
	 * 
	 * @param chunks
	 *            the chunks
	 * @return myChunk.the matching chunk
	 */
	public ChunkUnion getMatchingChunk(List<ChunkUnion> chunks) {
//		for (ChunkUnion chunk : chunks) {
//			if (myChunk.tokenStream.getTokenizerType() == chunk.tokenStream
//					.getTokenizerType()) {
//				if ((chunk.getChunk().tokenOffset.getBeginIndex() == myChunk.tokenOffset
//						.getBeginIndex())
//						&& (chunk.getChunk().tokenOffset.getEndIndex() == myChunk.tokenOffset
//								.getEndIndex()))
//					return chunk.getChunk();
//			}
//		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(ChunkUnion obj) {
//		if (obj == null || !(obj instanceof ChunkUnion))
			return false;
//		ChunkUnion chunkObj = (ChunkUnion) obj;
//		return ((chunkObj.getChunk().tokenStream.getTokenizerType()) == myChunk.tokenStream
//				.getTokenizerType() && (chunkObj.getChunk().tokenOffset.getBeginIndex() == myChunk.tokenOffset
//				.getBeginIndex() && chunkObj.getChunk().tokenOffset.getEndIndex() == myChunk.tokenOffset
//				.getEndIndex()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	//@Override
	public int hashCode() {
		String code = String.format("%d_%d_%s", myChunk.tokenOffset.getBeginIndex(),
				myChunk.tokenOffset.getEndIndex(), myChunk.value);
		return code.hashCode();
	}

	public Chunk getChunk() {
		return myChunk;
	}

}
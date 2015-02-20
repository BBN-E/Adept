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

import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Chunk, which is a sequence of tokens that comprise 
 * a phrase or other syntactic unit within a sentence.
 */
public class Chunk extends HltContent {

	/** The token stream. */
	protected TokenStream tokenStream;

	/** The token offset which is the index into tokenstream. */
	protected final TokenOffset tokenOffset;

    /** The char offset. */
    protected final CharOffset charOffset;

	/**
	 * Instantiates a new chunk.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public Chunk(TokenOffset tokenOffset, TokenStream tokenStream) {
		if ( tokenOffset==null || tokenStream==null) throw new IllegalArgumentException();
		this.tokenOffset = tokenOffset;
		this.tokenStream = tokenStream;
        this.charOffset = new CharOffset(tokenStream.get(tokenOffset.getBegin()).getCharOffset().getBegin(),tokenStream.get(tokenOffset.getEnd()).getCharOffset().getEnd());
		this.setValue();
	}

	/**
	 * Creates and sets the value by concatenating all the tokens within a
	 * chunk.
	 */
	private void setValue() {
		this.value = getValue(tokenStream,  tokenOffset);
	}
	
	/**
	 * Gets the value.
	 *
	 * @param tokenStream the token stream
	 * @param tokenOffset the token offset
	 * @return the value
	 */
	public static String getValue(TokenStream tokenStream, TokenOffset tokenOffset) {
		Document doc = tokenStream.getDocument();
		String docValue = doc.getValue();
		//System.out.println("In Chunk class, doc value length: " + docValue.length());
		//System.out.println("In Chunk class, doc value: " + docValue);
		int begin = tokenOffset.getBegin();
		//System.out.println("In Chunk class, tokenOffset begin: " + begin);
		Token tokenBegin = tokenStream.get(begin);
		int charBegin = tokenBegin.getCharOffset().getBegin();
		//System.out.println("In Chunk class, charOffset begin: " + charBegin);
		int end = tokenOffset.getEnd();
		//System.out.println("In Chunk class, tokenOffset end: " + end);
		Token tokenEnd = tokenStream.get(end);
		int charEnd = tokenEnd.getCharOffset().getEnd();
		//System.out.println("In Chunk class, charOffset end: " + charEnd);
		return docValue.substring(charBegin, charEnd);
	}

	/**
	 * Gets the token stream.
	 * 
	 * @return the token stream
	 */
	public TokenStream getTokenStream() {
		return tokenStream;
	}

	/**
	 * Gets the token offset.
	 * 
	 * @return the token offset
	 */
	public TokenOffset getTokenOffset() {
		return tokenOffset;
	}

	/**
	 * Gets the token offset.
	 * 
	 * @return the token offset
	 */
	public CharOffset getCharOffset() {
		return charOffset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.common.Item#getValue()
	 */
	@Override
	public String getValue() {
		return value;
	}

	/**
	 * Sets the token stream.
	 * 
	 * @param tokenStream
	 *            the new token stream
	 */
	public void setTokenStream(TokenStream tokenStream) {
		this.tokenStream = tokenStream;
	}

	/**
	 * Contains.
	 * 
	 * @param chunk
	 *            the chunk
	 * @return true, if successful
	 */
	public boolean Contains(Chunk chunk) {
		if ( ! isCompatible(chunk)) return false;
		return (chunk.tokenOffset.getBegin() >= this.tokenOffset.getBegin())
				&& (chunk.tokenOffset.getEnd() <= this.tokenOffset.getEnd());
	}

	/**
	 * Gets the containing chunk.
	 * 
	 * @param chunks
	 *            the chunks
	 * @return the containing chunk
	 */
	public Chunk getContainingChunk(List<Chunk> chunks) {
		for (Chunk chunk : chunks) {
			if (isCompatible(chunk)) {
				if ((chunk.tokenOffset.getBegin() <= this.tokenOffset.getBegin())
					&& (chunk.tokenOffset.getEnd() >= this.tokenOffset.getEnd()))
					return chunk;
			}
		}
		return null;
	}
	
	/**
	 * Overlaps.
	 * 
	 * @param chunk
	 *            the chunk
	 * @return true, if successful
	 */
	public boolean Overlaps(Chunk chunk) {
		if ( ! isCompatible(chunk)) return false;
		return Overlaps( chunk.tokenOffset, this.tokenOffset);
	}
	
	private boolean Overlaps( TokenOffset to1, TokenOffset to2)
	{
		if ( to1.getBegin() <= to2.getBegin()
			&& to1.getEnd() > to2.getBegin())
			return true;			
		if ( to1.getEnd() >= to2.getEnd()
			&& to1.getBegin() < to2.getEnd())
			return true;			
		return false;
	}
	
	// TODO add further compatibility tests here.
	public boolean isCompatible( Chunk chunk)
	{
		return this.tokenStream.getTokenizerType() == chunk.tokenStream.getTokenizerType();
	}

	/**
	 * Gets the overlapping chunk.
	 * 
	 * @param chunks
	 *            the chunks
	 * @return the overlapping chunk
	 */
	public Chunk getOverlappingChunk(List<Chunk> chunks) {
		for (Chunk chunk : chunks) {
			if (isCompatible(chunk)) {
				if (Overlaps( chunk.tokenOffset, this.tokenOffset))
					return chunk;
			}
		}
		return null;
	}	

	/**
	 * Gets the matching chunk.
	 * 
	 * @param chunks
	 *            the chunks
	 * @return the matching chunk
	 */
	public Chunk getMatchingChunk(List<Chunk> chunks) {
		for (Chunk chunk : chunks) {
			if (isCompatible(chunk)) {
				if ((chunk.tokenOffset.getBegin() == this.tokenOffset.getBegin())
					&& (chunk.tokenOffset.getEnd() == this.tokenOffset.getEnd()))
					return chunk;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == null || !(obj instanceof Chunk))
			return false;
		Chunk chunk = (Chunk) obj;
		if ( ! isCompatible(chunk)) return false;
		return (chunk.tokenOffset.getBegin() == this.tokenOffset.getBegin() 
				&& chunk.tokenOffset.getEnd() == this.tokenOffset.getEnd() &&
				chunk.tokenStream.getDocument() == this.tokenStream.getDocument()
				&& (chunk.charOffset.getBegin() == this.charOffset.getBegin() && chunk
				.charOffset.getEnd() == this.charOffset.getEnd()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%d_%d_%s", this.tokenOffset.getBegin(),
				this.tokenOffset.getEnd(), this.value);
		return code.hashCode();
	}

}
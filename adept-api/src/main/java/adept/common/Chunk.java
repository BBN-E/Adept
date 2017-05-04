package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

import com.google.common.base.Objects;
import java.util.List;
import java.io.Serializable;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Chunk, which is a sequence of tokens that comprise 
 * a phrase or other syntactic unit within a sentence.
 */
public class Chunk extends HltContent implements Serializable {

	private static final long serialVersionUID = -6481662260010444789L;

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
        checkArgument(tokenOffset!=null);
        checkArgument(tokenStream!=null);
		this.tokenOffset = tokenOffset;
		this.tokenStream = tokenStream;
        this.charOffset = new CharOffset(tokenStream.get(tokenOffset.getBegin()).getCharOffset().getBegin(),tokenStream.get(tokenOffset.getEnd()).getCharOffset().getEnd());
		this.setValue();
	}

	public Chunk(CharOffset charOffset, TokenStream tokenStream) {
		checkArgument(charOffset != null);
		checkArgument(tokenStream != null);
		int tokenOffsetBegin = -1;
		int tokenOffsetEnd = -1;
		for (int i = 0; i < tokenStream.size(); i++) {
			if (tokenStream.get(i).getCharOffset().getBegin() == charOffset.getBegin()) {
				tokenOffsetBegin = i;
			}
			if (tokenStream.get(i).getCharOffset().getEnd() == charOffset.getEnd()) {
				tokenOffsetEnd = i;
			}
		}
		this.tokenOffset = new TokenOffset(tokenOffsetBegin, tokenOffsetEnd);
		this.tokenStream = tokenStream;
		this.charOffset = charOffset;
		setValue();
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
                checkArgument(tokenStream!=null);
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
		checkArgument(chunk!=null);
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
		checkArgument(chunks!=null);
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
		checkArgument(chunks!=null);
		for (Chunk chunk : chunks) {
			if (isCompatible(chunk)) {
				if ((chunk.tokenOffset.getBegin() == this.tokenOffset.getBegin())
					&& (chunk.tokenOffset.getEnd() == this.tokenOffset.getEnd()))
					return chunk;
			}
		}
		return null;
	}
	

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Chunk other = (Chunk) obj;
		if (charOffset == null) {
			if (other.charOffset != null)
				return false;
		} else if (!charOffset.equals(other.charOffset))
			return false;
		if (tokenOffset == null) {
			if (other.tokenOffset != null)
				return false;
		} else if (!tokenOffset.equals(other.tokenOffset))
			return false;
		if (tokenStream == null) {
			if (other.tokenStream != null)
				return false;
		} else if (!tokenStream.equals(other.tokenStream))
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((charOffset == null) ? 0 : charOffset.hashCode());
		result = prime * result + ((tokenOffset == null) ? 0 : tokenOffset.hashCode());
		result = prime * result + ((tokenStream == null) ? 0 : tokenStream.hashCode());
		return result;
	}

}

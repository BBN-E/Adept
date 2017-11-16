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

import static com.google.common.base.Preconditions.checkArgument;
import java.io.Serializable;

/**
 * The Class SyntacticChunk.
 */
public class SyntacticChunk extends Chunk implements Serializable {

	private static final long serialVersionUID = 5396383253300943360L;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((scType == null) ? 0 : scType.hashCode());
		result = prime * result + (int) (sequenceId ^ (sequenceId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		SyntacticChunk other = (SyntacticChunk) obj;
		if (scType == null) {
			if (other.scType != null)
				return false;
		} else if (!scType.equals(other.scType))
			return false;
		if (sequenceId != other.sequenceId)
			return false;
		return true;
	}
}

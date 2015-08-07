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


/**
 * The Class CommittedBelief.
 */
public class CommittedBelief extends Chunk {

	/** The modality. */
	private final Modality modality;

	/** The sequence id. */
	private final long sequenceId;

	/**
	 * Instantiates a new committed belief.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param modality
	 *            the modality
	 */
	public CommittedBelief(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream, Modality modality) {
		super(tokenOffset, tokenStream);
		// TODO Auto-generated constructor stub
		this.sequenceId = sequenceId;

                checkArgument(modality!=null);
		this.modality = modality;
	}

	/**
	 * Gets the modality.
	 * 
	 * @return the modality
	 */
	public Modality getModality() {
		return modality;
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
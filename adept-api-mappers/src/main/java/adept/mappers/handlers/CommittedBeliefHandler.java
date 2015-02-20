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
 * The Class CommittedBelief.
 */
public class CommittedBeliefHandler extends ChunkHandler implements CommittedBeliefService.Iface {

	private CommittedBelief myCommittedBelief;

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
	public CommittedBeliefHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream, Modality modality) {
		super(tokenOffset, tokenStream);
		myCommittedBelief = new CommittedBelief();
		// TODO Auto-generated constructor stub
		myCommittedBelief.sequenceId = sequenceId;
		myCommittedBelief.modality = modality;
		myCommittedBelief.id = myItem.id;
		myCommittedBelief.value = myItem.value;
		myCommittedBelief.tokenOffset = myChunk.tokenOffset;
		myCommittedBelief.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the modality.
	 * 
	 * @return myCommittedBelief.the modality
	 */
	public Modality getModality() {
		return myCommittedBelief.modality;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myCommittedBelief.the sequence id
	 */
	public long getSequenceId() {
		return myCommittedBelief.sequenceId;
	}

	public CommittedBelief getCommittedBelief() {
		return myCommittedBelief;
	}

}
/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
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

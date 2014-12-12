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
package adept.common;

// TODO: Auto-generated Javadoc
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

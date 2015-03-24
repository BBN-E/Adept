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
 * The Sentence class extends ChunkHandler and represents the output from sentence
 * boundary detection algorithm.
 */
public class SentenceHandler extends ChunkHandler implements SentenceService.Iface {

	private Sentence mySentence;

	/**
	 * Instantiates a new sentence.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public SentenceHandler(long sequenceId, TokenOffset tokenOffset,
			TokenStream tokenStream) {
		super(tokenOffset, tokenStream);
		mySentence = new Sentence();
		// TODO Auto-generated constructor stub
		mySentence.sequenceId = sequenceId;
		mySentence.id = myItem.id;
		mySentence.value = myItem.value;
		mySentence.tokenOffset = myChunk.tokenOffset;
		mySentence.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return mySentence.the sequence id
	 */
	public long getSequenceId() {
		return mySentence.sequenceId;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return mySentence.the uncertainty confidence
	 */
	public double getUncertaintyConfidence() {
		return mySentence.uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(double uncertaintyConfidence) {
		mySentence.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return mySentence.the novelty confidence
	 */
	public double getNoveltyConfidence() {
		return mySentence.noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(double noveltyConfidence) {
		mySentence.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the type.
	 * 
	 * @return mySentence.the type
	 */
	public SentenceType getType() {
		return mySentence.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(SentenceType type) {
		mySentence.type = type;
	}

	/**
	 * Gets the punctuation.
	 * 
	 * @return mySentence.the punctuation
	 */
	public String getPunctuation() {
		return mySentence.punctuation;
	}

	/**
	 * Sets the punctuation.
	 * 
	 * @param punctuation
	 *            the new punctuation
	 */
	public void setPunctuation(String punctuation) {
		mySentence.punctuation = punctuation;
	}

	public Sentence getSentence() {
		return mySentence;
	}

}

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
 * The Class ProsodicPhrase.
 */
public class ProsodicPhraseHandler extends ChunkHandler implements ProsodicPhraseService.Iface {

	private ProsodicPhrase myProsodicPhrase;

	/**
	 * Instantiates a new prosodic phrase.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param sequenceId
	 *            the sequence id
	 */
	public ProsodicPhraseHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId) {
		super(tokenOffset, tokenStream);
		myProsodicPhrase = new ProsodicPhrase();
		myProsodicPhrase.sequenceId = sequenceId;
		myProsodicPhrase.id = myItem.id;
		myProsodicPhrase.value = myItem.value;
		myProsodicPhrase.tokenOffset = myChunk.tokenOffset;
		myProsodicPhrase.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return myProsodicPhrase.the uncertainty confidence
	 */
	public double getUncertaintyConfidence() {
		return myProsodicPhrase.uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(double uncertaintyConfidence) {
		myProsodicPhrase.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return myProsodicPhrase.the novelty confidence
	 */
	public double getNoveltyConfidence() {
		return myProsodicPhrase.noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(double noveltyConfidence) {
		myProsodicPhrase.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return myProsodicPhrase.the confidence
	 */
	public double getConfidence() {
		return myProsodicPhrase.confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(double confidence) {
		myProsodicPhrase.confidence = confidence;
	}

	/**
	 * Gets the type.
	 * 
	 * @return myProsodicPhrase.the type
	 */
	public String getType() {
		return myProsodicPhrase.type;
	}

	/**
	 * Sets the type.
	 * 
	 * @param type
	 *            the new type
	 */
	public void setType(String type) {
		myProsodicPhrase.type = type;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myProsodicPhrase.the sequence id
	 */
	public long getSequenceId() {
		return myProsodicPhrase.sequenceId;
	}

	public ProsodicPhrase getProsodicPhrase() {
		return myProsodicPhrase;
	}

}

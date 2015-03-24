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
 * The Class Opinion.
 */
public class OpinionHandler extends ChunkHandler implements OpinionService.Iface {

	private Opinion myOpinion;

	/**
	 * Instantiates a new opinion.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param subjectivity
	 *            the subjectivity
	 * @param polarity
	 *            the polarity
	 */
	public OpinionHandler(TokenOffset tokenOffset, TokenStream tokenStream, Subjectivity subjectivity, Polarity polarity) {
		super(tokenOffset, tokenStream);
		myOpinion = new Opinion();
		myOpinion.subjectivity = subjectivity;
		myOpinion.polarity = polarity;
		myOpinion.id = myItem.id;
		myOpinion.value = myItem.value;
		myOpinion.tokenOffset = myChunk.tokenOffset;
		myOpinion.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the polarity.
	 * 
	 * @return myOpinion.the polarity
	 */
	public Polarity getPolarity() {
		return myOpinion.polarity;
	}

	/**
	 * Gets the subjectivity.
	 * 
	 * @return myOpinion.the subjectivity
	 */
	public Subjectivity getSubjectivity() {
		return myOpinion.subjectivity;
	}

	public Opinion getOpinion() {
		return myOpinion;
	}

}

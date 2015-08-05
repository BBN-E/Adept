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

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Opinion.
 */
public class Opinion extends Chunk {
	
	/** The opinion id. */
	private final long opinionId;

	/** The subjectivity. */
	private final Subjectivity subjectivity;

	/** The polarity. */
	private final Polarity polarity;

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
	public Opinion(long opinionId, TokenOffset tokenOffset, TokenStream tokenStream, Subjectivity subjectivity, Polarity polarity) {
		super(tokenOffset, tokenStream);
		this.opinionId = opinionId;
                // TODO: add non null checks?
		this.subjectivity = subjectivity;
		this.polarity = polarity;
	}

	/**
	 * Gets the polarity.
	 * 
	 * @return the polarity
	 */
	public Polarity getPolarity() {
		return polarity;
	}

	/**
	 * Gets the subjectivity.
	 * 
	 * @return the subjectivity
	 */
	public Subjectivity getSubjectivity() {
		return subjectivity;
	}

}

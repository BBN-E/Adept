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

import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Story.
 */
public class Story extends Chunk {

	/** The sequence id. */
	private final long sequenceId;

	/** The content type. */
	private final ArrayList<String> topicLabels;

	/**
	 * Instantiates a new story.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param sequenceId
	 *            the sequence id
	 * @param topicLabels
	 *            the topic labels
	 */
	public Story(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, ArrayList<String> topicLabels) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;
		this.topicLabels = topicLabels;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the topic labels.
	 * 
	 * @return the topic labels
	 */
	public ArrayList<String> getTopicLabels() {
		return topicLabels;
	}

}

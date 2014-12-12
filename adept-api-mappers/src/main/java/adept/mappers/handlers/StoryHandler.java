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


import java.util.List;
import java.util.ArrayList;

// TODO: Auto-generated Javadoc
/**
 * The Class Story.
 */
public class StoryHandler extends ChunkHandler implements StoryService.Iface {

	private Story myStory;

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
	public StoryHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, ArrayList<String> topicLabels) {
		super(tokenOffset, tokenStream);
		myStory = new Story();
		myStory.sequenceId = sequenceId;
		myStory.topicLabels = topicLabels;
		myStory.id = myItem.id;
		myStory.value = myItem.value;
		myStory.tokenOffset = myChunk.tokenOffset;
		myStory.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myStory.the sequence id
	 */
	public long getSequenceId() {
		return myStory.sequenceId;
	}

	/**
	 * Gets the topic labels.
	 * 
	 * @return myStory.the topic labels
	 */
	public List<String> getTopicLabels() {
		return myStory.topicLabels;
	}

	public Story getStory() {
		return myStory;
	}

}

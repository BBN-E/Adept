/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package thrift.adept.module;

import thrift.adept.common.Document;
import thrift.adept.common.HltContentContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class ConversationAnnotator.
 */
public class ConversationAnnotator extends AbstractModule implements
		DocumentProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IConversationProcessor#process(adept.common.Conversation)
	 */
	@Override
	public HltContentContainer process(Document document, HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IConversationProcessor#processAsync(adept.common.Conversation
	 * )
	 */
	@Override
	public long processAsync(Document document, HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IConversationProcessor#tryGetResult(long,
	 * adept.common.Conversation)
	 */
	@Override
	public boolean tryGetResult(long requestId, HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return false;
	}

}

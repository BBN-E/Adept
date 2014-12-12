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

import java.util.List;

import thrift.adept.common.HltContentUnion;
import thrift.adept.common.Passage;
import thrift.adept.common.Sentence;

// TODO: Auto-generated Javadoc
/**
 * The Class OpinionTagger.
 */
public class OpinionTagger extends AbstractModule implements
		SentenceProcessor.Iface, PassageProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentenceProcessor#process(adept.common.Sentence)
	 */
	@Override
	public List<HltContentUnion> process(Sentence sentence) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentenceProcessor#processAsync(adept.common.Sentence)
	 */
	@Override
	public long processAsync(Sentence sentence) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentenceProcessor#tryGetResult(long, java.util.List)
	 */
	@Override
	public boolean tryGetResult(long requestId, List<HltContentUnion> hltContents) {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IPassageProcessor#process(adept.common.Passage)
	 */
	@Override
	public List<HltContentUnion> process(Passage passage) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IPassageProcessor#processAsync(adept.common.Passage)
	 */
	@Override
	public long processAsync(Passage passage) {
		// TODO Auto-generated method stub
		return 0;
	}

}

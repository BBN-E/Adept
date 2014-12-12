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
import thrift.adept.common.Sentence;

// TODO: Auto-generated Javadoc
/**
 * The Class SentenceSimilarityDetector.
 */
public class SentenceSimilarityDetector extends AbstractModule implements
		SentencePairProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentencePairProcessor#process(adept.common.Sentence,
	 * adept.common.Sentence)
	 */
	@Override
	public List<HltContentUnion> process(Sentence sentence1,
			Sentence sentence2) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.ISentencePairProcessor#processAsync(adept.common.Sentence,
	 * adept.common.Sentence)
	 */
	@Override
	public long processAsync(Sentence sentence1, Sentence sentence2) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentencePairProcessor#tryGetResult(long,
	 * java.util.List)
	 */
	@Override
	public boolean tryGetResult(long requestId,
			List<HltContentUnion> hltContents) {
		// TODO Auto-generated method stub
		return false;
	}

}

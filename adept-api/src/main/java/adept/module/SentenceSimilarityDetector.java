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
package adept.module;

import java.util.List;

import adept.common.Sentence;
import adept.common.SentenceSimilarity;

// TODO: Auto-generated Javadoc
/**
 * The Class SentenceSimilarityDetector.
 */
public abstract class SentenceSimilarityDetector extends AbstractModule implements
		ISentencePairProcessor<SentenceSimilarity> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentencePairProcessor#process(adept.common.Sentence,
	 * adept.common.Sentence)
	 */
	@Override
	public SentenceSimilarity process(Sentence sentence1, Sentence sentence2) {
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
	public Boolean tryGetResult(long requestId, SentenceSimilarity hltContents) {
		// TODO Auto-generated method stub
		return null;
	}

}

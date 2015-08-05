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
import adept.common.SyntacticChunk;


/**
 * The Class SyntacticChunker.
 */
public abstract class SyntacticChunker extends AbstractModule implements
		ISentenceProcessor<SyntacticChunk> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.ISentenceProcessor#process(adept.common.Sentence)
	 */
	@Override
	public List<SyntacticChunk> process(Sentence sentence) {
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
	public Boolean tryGetResult(long requestId, List<SyntacticChunk> hltContents) {
		// TODO Auto-generated method stub
		return null;
	}

}

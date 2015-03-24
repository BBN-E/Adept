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

import adept.common.Chunk;
import adept.common.Paraphrase;

// TODO: Auto-generated Javadoc
/**
 * The Class Paraphraser.
 */
public abstract class Paraphraser implements IChunkProcessor<Paraphrase> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#process(adept.common.Chunk)
	 */
	@Override
	public List<Paraphrase> process(Chunk chunk) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#processAsync(adept.common.Chunk)
	 */
	@Override
	public long processAsync(Chunk chunk) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#tryGetResult(long, java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<Paraphrase> hltContents) {
		// TODO Auto-generated method stub
		return null;
	}

}

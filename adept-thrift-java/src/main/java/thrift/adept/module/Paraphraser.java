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
import thrift.adept.common.ChunkUnion;
import thrift.adept.common.Paraphrase;

// TODO: Auto-generated Javadoc
/**
 * The Class Paraphraser.
 */
public class Paraphraser implements ChunkProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#process(adept.common.Chunk)
	 */
	@Override
	public List<HltContentUnion> process(ChunkUnion chunk) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#processAsync(adept.common.Chunk)
	 */
	@Override
	public long processAsync(ChunkUnion chunk) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IChunkProcessor#tryGetResult(long, java.util.List)
	 */
	@Override
	public boolean tryGetResult(long requestId, List<HltContentUnion> hltContents) {
		// TODO Auto-generated method stub
		return false;
	}

}

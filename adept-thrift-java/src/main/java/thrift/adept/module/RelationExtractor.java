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

import thrift.adept.common.Document;
import thrift.adept.common.HltContentContainer;
import thrift.adept.common.HltContentUnion;

// TODO: Auto-generated Javadoc
/**
 * The Class RelationExtractor.
 */
public class RelationExtractor extends AbstractModule implements
		DocumentHltContentProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentHltContentProcessor#process(adept.common.TextDocument
	 * , adept.common.HltContentContainer)
	 */
	@Override
	public List<HltContentUnion> process(Document document,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IDocumentHltContentProcessor#processAsync(adept.common.
	 * TextDocument, adept.common.HltContentContainer)
	 */
	@Override
	public long processAsync(Document document,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IDocumentHltContentProcessor#tryGetResult(long,
	 * java.util.List)
	 */
	@Override
	public boolean tryGetResult(long requestId, List<HltContentUnion> hltContents) {
		// TODO Auto-generated method stub
		return false;
	}

}

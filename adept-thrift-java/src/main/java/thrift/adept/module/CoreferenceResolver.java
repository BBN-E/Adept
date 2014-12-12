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
import thrift.adept.common.DocumentList;
import thrift.adept.common.HltContentContainer;

// TODO: Auto-generated Javadoc
/**
 * The Class CoreferenceResolver.
 */
public class CoreferenceResolver extends AbstractModule implements
		DocumentProcessor.Iface, DocumentListProcessor.Iface {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentListProcessor#process(adept.common.DocumentList,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public HltContentContainer process(DocumentList documentList,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentListProcessor#processAsync(adept.common.DocumentList
	 * , adept.common.HltContentContainer)
	 */
	@Override
	public long processAsync(DocumentList documentList,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IDocumentProcessor#process(adept.common.TextDocument,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public HltContentContainer process(Document document,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentProcessor#processAsync(adept.common.TextDocument,
	 * adept.common.HltContentContainer)
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
	 * @see adept.module.IDocumentProcessor#tryGetResult(long,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public boolean tryGetResult(long requestId,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return false;
	}

}

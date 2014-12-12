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

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.InterPausalUnit;

// TODO: Auto-generated Javadoc
/**
 * The Class AcousticInteractionAnalyzer.
 */
public abstract class AcousticInteractionAnalyzer extends AbstractModule implements
		IDocumentHltContentProcessor<InterPausalUnit> {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentHltContentProcessor#process(adept.common.Document,
	 * adept.common.HltContentContainer)
	 */
	@Override
	public List<InterPausalUnit> process(Document document,
			HltContentContainer hltContentContainer) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IDocumentHltContentProcessor#processAsync(adept.common.Document
	 * , adept.common.HltContentContainer)
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
	public Boolean tryGetResult(long requestId,
			List<InterPausalUnit> hltContents) {
		// TODO Auto-generated method stub
		return null;
	}

}

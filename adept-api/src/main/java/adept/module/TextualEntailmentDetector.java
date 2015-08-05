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

import adept.common.Entailment;
import adept.common.Passage;


/**
 * The Class TextualEntailmentDetector.
 */
public abstract class TextualEntailmentDetector extends AbstractModule implements
		IPassagePairProcessor<Entailment> {

	/**
	 * Instantiates a new textual entailment detector.
	 */
	public TextualEntailmentDetector() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IPassagePairProcessor#process(adept.common.Passage,
	 * adept.common.Passage)
	 */
	@Override
	public List<Entailment> process(Passage text, Passage hypothesis) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * adept.module.IPassagePairProcessor#processAsync(adept.common.Passage,
	 * adept.common.Passage)
	 */
	@Override
	public long processAsync(Passage text, Passage hypothesis) {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see adept.module.IPassagePairProcessor#tryGetResult(long,
	 * java.util.List)
	 */
	@Override
	public Boolean tryGetResult(long requestId, List<Entailment> entailments) {
		// TODO Auto-generated method stub
		return null;
	}

}

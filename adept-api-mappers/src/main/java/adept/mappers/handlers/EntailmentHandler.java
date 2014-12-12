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
package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class Entailment.
 */
public class EntailmentHandler extends HltContentHandler implements EntailmentService.Iface {

	private Entailment myEntailment;

	/**
	 * Instantiates a new entailment.
	 * 
	 * @param entailmentId
	 *            the entailment id
	 */
	public EntailmentHandler(long entailmentId) {
		super();
		myEntailment = new Entailment();
		myEntailment.entailmentId = entailmentId;
		myEntailment.id = myItem.id;
		myEntailment.value = myItem.value;
	}

	/**
	 * Gets the text.
	 * 
	 * @return myEntailment.the text
	 */
	public Passage getText() {
		return myEntailment.text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(Passage text) {
		myEntailment.text = text;
	}

	/**
	 * Gets the hypothesis.
	 * 
	 * @return myEntailment.the hypothesis
	 */
	public Passage getHypothesis() {
		return myEntailment.hypothesis;
	}

	/**
	 * Sets the hypothesis.
	 * 
	 * @param hypothesis
	 *            the new hypothesis
	 */
	public void setHypothesis(Passage hypothesis) {
		myEntailment.hypothesis = hypothesis;
	}

	/**
	 * Gets the entailment id.
	 * 
	 * @return myEntailment.the entailment id
	 */
	public long getEntailmentId() {
		return myEntailment.entailmentId;
	}

	/**
	 * Gets the judgment distribution.
	 * 
	 * @return myEntailment.the judgment distribution
	 */
	public Map<EntailmentJudgment, Double> getJudgmentDistribution() {
		return myEntailment.judgmentDistribution;
	}

	/**
	 * Sets the judgment distribution.
	 * 
	 * @param judgmentDistribution
	 *            the judgment distribution
	 */
	public void setJudgmentDistribution(
			Map<EntailmentJudgment, Double> judgmentDistribution) {
		myEntailment.judgmentDistribution = judgmentDistribution;
	}

	/**
	 * Adds the judgment confidence pair.
	 * 
	 * @param judgment
	 *            the judgment
	 * @param confidence
	 *            the confidence
	 * @return myEntailment.true, if successful
	 */
	public boolean addJudgmentConfidencePair(EntailmentJudgment judgment, double confidence) {
		myEntailment.judgmentDistribution.put(judgment, confidence);
		return true;
	}

	/**
	 * Gets the best judgment.
	 * 
	 * @return myEntailment.the best judgment
	 */
	public ChunkUnion getBestJudgment() {
		// TODO:
		throw new RuntimeException("TODO: ");
	}

	public Entailment getEntailment() {
		return myEntailment;
	}

}

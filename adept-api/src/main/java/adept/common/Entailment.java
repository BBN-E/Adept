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
package adept.common;

import static com.google.common.base.Preconditions.checkArgument;

// TODO: Auto-generated Javadoc
/**
 * The Class Entailment.
 */
public class Entailment extends HltContent {

	/** The entailment id. */
	private final long entailmentId;

	/**
	 * The Enum Judgment.
	 */
	public enum Judgment {

		/** The entails. */
		ENTAILS,
		/** The contradicts. */
		CONTRADICTS,
		/** The not related. */
		NOT_RELATED
	}

	/** The judgment distribution. */
	private BoundedList<Pair<Judgment, Float>> judgmentDistribution;

	/** The text. */
	private Passage text;

	/** The hypothesis. */
	private Passage hypothesis;

	/**
	 * Instantiates a new entailment.
	 * 
	 * @param entailmentId
	 *            the entailment id
	 */
	public Entailment(long entailmentId) {
		this.entailmentId = entailmentId;
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public Passage getText() {
		return text;
	}

	/**
	 * Sets the text.
	 * 
	 * @param text
	 *            the new text
	 */
	public void setText(Passage text) {
                checkArgument(text!=null);
		this.text = text;
	}

	/**
	 * Gets the hypothesis.
	 * 
	 * @return the hypothesis
	 */
	public Passage getHypothesis() {
		return hypothesis;
	}

	/**
	 * Sets the hypothesis.
	 * 
	 * @param hypothesis
	 *            the new hypothesis
	 */
	public void setHypothesis(Passage hypothesis) {
                checkArgument(hypothesis!=null);
		this.hypothesis = hypothesis;
	}

	/**
	 * Gets the entailment id.
	 * 
	 * @return the entailment id
	 */
	public long getEntailmentId() {
		return entailmentId;
	}

	/**
	 * Gets the judgment distribution.
	 * 
	 * @return the judgment distribution
	 */
	public BoundedList<Pair<Judgment, Float>> getJudgmentDistribution() {
		return judgmentDistribution;
	}

	/**
	 * Sets the judgment distribution.
	 * 
	 * @param judgmentDistribution
	 *            the judgment distribution
	 */
	public void setJudgmentDistribution(
			BoundedList<Pair<Judgment, Float>> judgmentDistribution) {
                checkArgument(judgmentDistribution!=null);
		this.judgmentDistribution = judgmentDistribution;
	}

	/**
	 * Adds the judgment confidence pair.
	 * 
	 * @param judgment
	 *            the judgment
	 * @param confidence
	 *            the confidence
	 * @return true, if successful
	 */
	public boolean addJudgmentConfidencePair(Judgment judgment, float confidence) {
                checkArgument(judgment!=null);
		Pair<Judgment, Float> pair = new Pair<Judgment, Float>(judgment,
				confidence);
		return judgmentDistribution.add(pair);
	}

	/**
	 * Gets the best judgment.
	 * 
	 * @return the best judgment
	 */
	public Chunk getBestJudgment() {
		// TODO:
		throw new RuntimeException("TODO: ");
	}

}

/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

package adept.common;

import static com.google.common.base.Preconditions.checkArgument;


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
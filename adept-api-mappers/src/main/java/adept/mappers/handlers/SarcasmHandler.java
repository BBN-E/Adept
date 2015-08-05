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

package adept.mappers.handlers;

import thrift.adept.common.*;


/**
 * The Class Sarcasm.
 */
public class SarcasmHandler extends ChunkHandler implements SarcasmService.Iface {

	private Sarcasm mySarcasm;

	/**
	 * The Enum Judgment.
	 */
	public enum Judgment {

		/** The positive. */
		POSITIVE,
		/** The negative. */
		NEGATIVE,
		/** The none. */
		NONE,
	}

	/**
	 * Instantiates a new sarcasm.
	 * 
	 * @param sarcasmId
	 *            the sarcasm id
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param judgment
	 *            the judgment
	 */
	public SarcasmHandler(long sarcasmId, TokenOffset tokenOffset,
			TokenStream tokenStream, SarcasmJudgment judgment) {
		super(tokenOffset, tokenStream);
		mySarcasm = new Sarcasm();
		mySarcasm.sarcasmId = sarcasmId;
		mySarcasm.judgment = judgment;
		mySarcasm.id = myItem.id;
		mySarcasm.value = myItem.value;
		mySarcasm.tokenOffset = myChunk.tokenOffset;
		mySarcasm.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the sarcasm id.
	 * 
	 * @return mySarcasm.the sarcasm id
	 */
	public long getSarcasmId() {
		return mySarcasm.sarcasmId;
	}

	/**
	 * Gets the judgment.
	 * 
	 * @return mySarcasm.the judgment
	 */
	public SarcasmJudgment getJudgment() {
		return mySarcasm.judgment;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return mySarcasm.the confidence
	 */
	public double getConfidence() {
		return mySarcasm.confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(double confidence) {
		mySarcasm.confidence = confidence;
	}

	public Sarcasm getSarcasm() {
		return mySarcasm;
	}

}
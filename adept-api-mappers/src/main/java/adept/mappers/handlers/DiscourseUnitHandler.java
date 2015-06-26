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

// TODO: Auto-generated Javadoc
/**
 * The Class DiscourseUnit.
 */
public class DiscourseUnitHandler extends ChunkHandler implements DiscourseUnitService.Iface {

	private DiscourseUnit myDiscourseUnit;

	/**
	 * Instantiates a new discourse unit.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param sequenceId
	 *            the sequence id
	 * @param discourceType
	 *            the discource type
	 */
	public DiscourseUnitHandler(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, String discourceType) {
		super(tokenOffset, tokenStream);
		myDiscourseUnit = new DiscourseUnit();
		myDiscourseUnit.sequenceId = sequenceId;
		myDiscourseUnit.discourceType = discourceType;
		myDiscourseUnit.id = myItem.id;
		myDiscourseUnit.value = myItem.value;
		myDiscourseUnit.tokenOffset = myChunk.tokenOffset;
		myDiscourseUnit.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the uncertainty confidence.
	 * 
	 * @return myDiscourseUnit.the uncertainty confidence
	 */
	public double getUncertaintyConfidence() {
		return myDiscourseUnit.uncertaintyConfidence;
	}

	/**
	 * Sets the uncertainty confidence.
	 * 
	 * @param uncertaintyConfidence
	 *            the new uncertainty confidence
	 */
	public void setUncertaintyConfidence(double uncertaintyConfidence) {
		myDiscourseUnit.uncertaintyConfidence = uncertaintyConfidence;
	}

	/**
	 * Gets the novelty confidence.
	 * 
	 * @return myDiscourseUnit.the novelty confidence
	 */
	public double getNoveltyConfidence() {
		return myDiscourseUnit.noveltyConfidence;
	}

	/**
	 * Sets the novelty confidence.
	 * 
	 * @param noveltyConfidence
	 *            the new novelty confidence
	 */
	public void setNoveltyConfidence(double noveltyConfidence) {
		myDiscourseUnit.noveltyConfidence = noveltyConfidence;
	}

	/**
	 * Gets the discource type.
	 * 
	 * @return myDiscourseUnit.the discource type
	 */
	public String getDiscourceType() {
		return myDiscourseUnit.discourceType;
	}

	/**
	 * Sets the discource type.
	 * 
	 * @param discourceType
	 *            the new discource type
	 */
	public void setDiscourceType(String discourceType) {
		myDiscourseUnit.discourceType = discourceType;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myDiscourseUnit.the sequence id
	 */
	public long getSequenceId() {
		return myDiscourseUnit.sequenceId;
	}

	public DiscourseUnit getDiscourseUnit() {
		return myDiscourseUnit;
	}

}
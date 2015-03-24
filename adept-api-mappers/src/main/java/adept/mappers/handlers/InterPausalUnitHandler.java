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

import java.util.Map;

// TODO: Auto-generated Javadoc
/**
 * The Class InterPausalUnit.
 */
public class InterPausalUnitHandler extends HltContentHandler implements InterPausalUnitService.Iface {

	private InterPausalUnit myInterPausalUnit;

	/**
	 * Instantiates a new inter pausal unit.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param ipuAudioOffset
	 *            the ipu audio offset
	 */
	public InterPausalUnitHandler(long sequenceId, AudioOffset ipuAudioOffset) {
		super();
		myInterPausalUnit = new InterPausalUnit();
		myInterPausalUnit.ipuAudioOffset = ipuAudioOffset;
		myInterPausalUnit.sequenceId = sequenceId;
		myInterPausalUnit.id = myItem.id;
		myInterPausalUnit.value = myItem.value;
	}

	/**
	 * Gets the acoustic features.
	 * 
	 * @return myInterPausalUnit.the acoustic features
	 */
	public Map<String, Double> getAcousticFeatures() {
		return myInterPausalUnit.acousticFeatures;
	}

	/**
	 * Sets the acoustic features.
	 * 
	 * @param acousticFeatures
	 *            the acoustic features
	 */
	public void setAcousticFeatures(Map<String, Double> acousticFeatures) {
		myInterPausalUnit.acousticFeatures = acousticFeatures;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return myInterPausalUnit.the sequence id
	 */
	public long getSequenceId() {
		return myInterPausalUnit.sequenceId;
	}

	/**
	 * Gets the ipu audio offset.
	 * 
	 * @return myInterPausalUnit.the ipu audio offset
	 */
	public AudioOffset getIpuAudioOffset() {
		return myInterPausalUnit.ipuAudioOffset;
	}

	public InterPausalUnit getInterPausalUnit() {
		return myInterPausalUnit;
	}

}
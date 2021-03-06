package adept.common;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

import static com.google.common.base.Preconditions.checkArgument;
import java.io.Serializable;

import java.util.Map;


/**
 * The Class InterPausalUnit.
 */
public class InterPausalUnit extends HltContent implements Serializable {

	private static final long serialVersionUID = 6192147750687589992L;

	/** The sequence id. */
	private final long sequenceId;

	/** The ipu audio offset. */
	private final AudioOffset ipuAudioOffset;

	/** The acoustic features. */
	Map<String, Float> acousticFeatures;

	/**
	 * Instantiates a new inter pausal unit.
	 * 
	 * @param sequenceId
	 *            the sequence id
	 * @param ipuAudioOffset
	 *            the ipu audio offset
	 */
	public InterPausalUnit(long sequenceId, AudioOffset ipuAudioOffset) {
		super();
		this.ipuAudioOffset = ipuAudioOffset;
		this.sequenceId = sequenceId;
	}

	/**
	 * Gets the acoustic features.
	 * 
	 * @return the acoustic features
	 */
	public Map<String, Float> getAcousticFeatures() {
		return acousticFeatures;
	}

	/**
	 * Sets the acoustic features.
	 * 
	 * @param acousticFeatures
	 *            the acoustic features
	 */
	public void setAcousticFeatures(Map<String, Float> acousticFeatures) {
                // TODO: Check for null?
		this.acousticFeatures = acousticFeatures;
	}

	/**
	 * Gets the sequence id.
	 * 
	 * @return the sequence id
	 */
	public long getSequenceId() {
		return sequenceId;
	}

	/**
	 * Gets the ipu audio offset.
	 * 
	 * @return the ipu audio offset
	 */
	public AudioOffset getIpuAudioOffset() {
		return ipuAudioOffset;
	}

}

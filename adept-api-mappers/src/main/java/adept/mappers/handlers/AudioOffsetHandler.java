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
 * The Class AudioOffset.
 */
public final class AudioOffsetHandler implements AudioOffsetService.Iface {

	private AudioOffset myAudioOffset;

	/**
	 * Instantiates a new audio offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public AudioOffsetHandler(double beginIndex, double endIndex) {
		myAudioOffset = new AudioOffset();
		myAudioOffset.beginIndex = beginIndex;
		myAudioOffset.endIndex = endIndex;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return myAudioOffset.the begin
	 */
	public double getBegin() {
		return myAudioOffset.beginIndex;
	}

	/**
	 * Gets the end.
	 * 
	 * @return myAudioOffset.the end
	 */
	public double getEnd() {
		return myAudioOffset.endIndex;
	}

	public AudioOffset getAudioOffset() {
		return myAudioOffset;
	}

}
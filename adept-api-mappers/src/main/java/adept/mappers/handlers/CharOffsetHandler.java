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
 * Offset class captures begin and end integer positions of character or token
 * spans. This class is immutable..
 */
public final class CharOffsetHandler implements CharOffsetService.Iface {

	private CharOffset myCharOffset;

	/**
	 * Instantiates a new offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public CharOffsetHandler(int beginIndex, int endIndex) {
		myCharOffset = new CharOffset();
		myCharOffset.beginIndex = beginIndex;
		myCharOffset.endIndex = endIndex;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return myCharOffset.the begin
	 */
	public int getBegin() {
		return myCharOffset.beginIndex;
	}

	/**
	 * Gets the end.
	 * 
	 * @return myCharOffset.the end
	 */
	public int getEnd() {
		return myCharOffset.endIndex;
	}

	public CharOffset getCharOffset() {
		return myCharOffset;
	}

}
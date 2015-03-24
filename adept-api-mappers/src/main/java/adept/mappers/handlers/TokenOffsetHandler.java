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
public final class TokenOffsetHandler implements TokenOffsetService.Iface {

	private TokenOffset myTokenOffset;

	/**
	 * Instantiates a new offset.
	 * 
	 * @param begin
	 *            the begin
	 * @param end
	 *            the end
	 */
	public TokenOffsetHandler(long beginIndex, long endIndex) {
		myTokenOffset = new TokenOffset();
		myTokenOffset.beginIndex = beginIndex;
		myTokenOffset.endIndex = endIndex;
	}

	/**
	 * Gets the begin.
	 * 
	 * @return myTokenOffset.the begin
	 */
	public long getBegin() {
		return myTokenOffset.beginIndex;
	}

	/**
	 * Gets the end.
	 * 
	 * @return myTokenOffset.the end
	 */
	public long getEnd() {
		return myTokenOffset.endIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(TokenOffsetObject obj) {
		if (obj == null || !(obj instanceof TokenOffsetObject))
			return false;
		TokenOffsetObject tokenOffset = (TokenOffsetObject) obj;
		return (tokenOffset.getTokenOffset().beginIndex == myTokenOffset.beginIndex && tokenOffset
				.getTokenOffset().endIndex == myTokenOffset.endIndex);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		String code = String.format("%d_%d", myTokenOffset.beginIndex, myTokenOffset.endIndex);
		return code.hashCode();
	}

	public TokenOffset getTokenOffset() {
		return myTokenOffset;
	}

}
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
 * The Class Opinion.
 */
public class OpinionHandler extends ChunkHandler implements OpinionService.Iface {

	private Opinion myOpinion;

	/**
	 * Instantiates a new opinion.
	 * 
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 * @param subjectivity
	 *            the subjectivity
	 * @param polarity
	 *            the polarity
	 */
	public OpinionHandler(TokenOffset tokenOffset, TokenStream tokenStream, Subjectivity subjectivity, Polarity polarity) {
		super(tokenOffset, tokenStream);
		myOpinion = new Opinion();
		myOpinion.subjectivity = subjectivity;
		myOpinion.polarity = polarity;
		myOpinion.id = myItem.id;
		myOpinion.value = myItem.value;
		myOpinion.tokenOffset = myChunk.tokenOffset;
		myOpinion.tokenStream = myChunk.tokenStream;
	}

	/**
	 * Gets the polarity.
	 * 
	 * @return myOpinion.the polarity
	 */
	public Polarity getPolarity() {
		return myOpinion.polarity;
	}

	/**
	 * Gets the subjectivity.
	 * 
	 * @return myOpinion.the subjectivity
	 */
	public Subjectivity getSubjectivity() {
		return myOpinion.subjectivity;
	}

	public Opinion getOpinion() {
		return myOpinion;
	}

}
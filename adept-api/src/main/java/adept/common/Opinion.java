/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

/*
 * 
 */
package adept.common;

import static com.google.common.base.Preconditions.checkArgument;


/**
 * The Class Opinion.
 */
public class Opinion extends Chunk {
	
	/** The opinion id. */
	private final long opinionId;

	/** The subjectivity. */
	private final Subjectivity subjectivity;

	/** The polarity. */
	private final Polarity polarity;

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
	public Opinion(long opinionId, TokenOffset tokenOffset, TokenStream tokenStream, Subjectivity subjectivity, Polarity polarity) {
		super(tokenOffset, tokenStream);
		this.opinionId = opinionId;
                // TODO: add non null checks?
		this.subjectivity = subjectivity;
		this.polarity = polarity;
	}

	/**
	 * Gets the polarity.
	 * 
	 * @return the polarity
	 */
	public Polarity getPolarity() {
		return polarity;
	}

	/**
	 * Gets the subjectivity.
	 * 
	 * @return the subjectivity
	 */
	public Subjectivity getSubjectivity() {
		return subjectivity;
	}

}

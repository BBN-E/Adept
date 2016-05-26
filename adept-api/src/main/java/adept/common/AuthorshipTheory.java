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
 * The Class AuthorshipTheory.
 */
public class AuthorshipTheory extends Chunk {

	/** The sarcasm id. */
	private final Entity author;

	/** The confidence. */
	private float confidence;

	private final String authorString = "AUTHOR ENTITY";

	/**
	 * Instantiates a new authorship theory.
	 * 
	 * @param author
	 *            the author
	 * @param tokenOffset
	 *            the token offset
	 * @param tokenStream
	 *            the token stream
	 */
	public AuthorshipTheory(TokenOffset tokenOffset,
			TokenStream tokenStream, Entity author) {
		super(tokenOffset, tokenStream);
		checkArgument(author!=null);
		author.value = this.authorString;
		this.author = author;
	}

	/**
	 * Gets the sarcasm id.
	 * 
	 * @return the sarcasm id
	 */
	public Entity getAuthor() {
		return author;
	}

	/**
	 * Gets the confidence.
	 * 
	 * @return the confidence
	 */
	public float getConfidence() {
		return confidence;
	}

	/**
	 * Sets the confidence.
	 * 
	 * @param confidence
	 *            the new confidence
	 */
	public void setConfidence(float confidence) {
		this.confidence = confidence;
	}

}

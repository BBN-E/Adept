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


/**
 * The Class Opinion.
 */
public class Opinion extends Chunk implements Serializable {

	private static final long serialVersionUID = -5843192056673079052L;

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (int) (opinionId ^ (opinionId >>> 32));
		result = prime * result + ((polarity == null) ? 0 : polarity.hashCode());
		result = prime * result + ((subjectivity == null) ? 0 : subjectivity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Opinion other = (Opinion) obj;
		if (opinionId != other.opinionId)
			return false;
		if (polarity != other.polarity)
			return false;
		if (subjectivity != other.subjectivity)
			return false;
		return true;
	}
}

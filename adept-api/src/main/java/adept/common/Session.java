/*
* ------
* Adept
* -----
* Copyright (C) 2012-2017 Raytheon BBN Technologies Corp.
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

import java.util.List;


/**
 * The Class Session.
 */
public class Session extends Chunk implements Serializable {

	private static final long serialVersionUID = 1912502890650293706L;

	/** The sequence id. */
	private final long sequenceId;

	/** The content type. */
	private final String contentType;

	/** The sentences. */
	private final List<Sentence> sentences;
	

	/**
	 * Instantiates a new session.
	 *
	 * @param tokenOffset the token offset
	 * @param tokenStream the token stream
	 * @param sequenceId the sequence id
	 * @param contentType the content type
	 * @param sentences the sentences
	 */
	public Session(TokenOffset tokenOffset, TokenStream tokenStream,
			long sequenceId, String contentType, List<Sentence> sentences) {
		super(tokenOffset, tokenStream);
		this.sequenceId = sequenceId;
                checkArgument(contentType!=null && contentType.trim().length()>0);
                checkArgument(sentences!=null);
                
		this.contentType = contentType;
		this.sentences = sentences;
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
	 * Gets the content type.
	 *
	 * @return the content type
	 */
	public String getContentType() {
		return contentType;
	}

	/**
	 * Gets the sentences.
	 *
	 * @return the sentences
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((contentType == null) ? 0 : contentType.hashCode());
		result = prime * result + ((sentences == null) ? 0 : sentences.hashCode());
		result = prime * result + (int) (sequenceId ^ (sequenceId >>> 32));
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
		Session other = (Session) obj;
		if (contentType == null) {
			if (other.contentType != null)
				return false;
		} else if (!contentType.equals(other.contentType))
			return false;
		if (sentences == null) {
			if (other.sentences != null)
				return false;
		} else if (!sentences.equals(other.sentences))
			return false;
		if (sequenceId != other.sequenceId)
			return false;
		return true;
	}
	
}
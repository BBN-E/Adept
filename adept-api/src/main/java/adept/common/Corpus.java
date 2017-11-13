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
import static com.google.common.base.Preconditions.checkNotNull;
import java.io.Serializable;

/**
 * The Class Corpus.
 */
public final class Corpus implements Serializable {

	private static final long serialVersionUID = 7080823363358334734L;

	/** The id. */
	private final ID id;

	/** The corpus id. */
	private final String corpusId;

	/** The type. */
	private final String type;

	/** The name. */
	private final String name;

	/** The uri. */
	private final String uri;

	/**
	 * Instantiates a new corpus.
	 * 
	 * @param corpusId
	 *            the corpus id
	 * @param type
	 *            the type
	 * @param name
	 *            the name
	 * @param uri
	 *            the uri
	 */
	public Corpus(String corpusId, String type, String name, String uri) {
		this.id = new ID();
                checkArgument(corpusId!=null && corpusId.trim().length()>0);
		this.corpusId = corpusId;
		this.type = type;
                checkArgument(name!=null && name.trim().length()>0);
		this.name = name;
		this.uri = uri;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public ID getId() {
		return id;
	}

	/**
	 * Gets the id string.
	 * 
	 * @return the id string
	 */
	public String getIdString() {
		return this.id.getIdString();
	}

	/**
	 * Gets the corpus id.
	 * 
	 * @return the corpus id
	 */
	public String getCorpusId() {
		return corpusId;
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the name.
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the uri.
	 * 
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}
}
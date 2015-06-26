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
 * The Class Corpus.
 */
public final class CorpusHandler implements CorpusService.Iface {

	private Corpus myCorpus;

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
	public CorpusHandler(String corpusId, String type, String name, String uri) {
		IDHandler idHandler = new IDHandler();
		myCorpus = new Corpus();
		myCorpus.id = idHandler.getID();
		myCorpus.corpusId = corpusId;
		myCorpus.type = type;
		myCorpus.name = name;
		myCorpus.uri = uri;
	}

	/**
	 * Gets the id.
	 * 
	 * @return myCorpus.the id
	 */
	public ID getId() {
		return myCorpus.id;
	}

	/**
	 * Gets the id string.
	 * 
	 * @return myCorpus.the id string
	 */
	public String getIdString() {
		return myCorpus.id.idStr;
	}

	/**
	 * Gets the corpus id.
	 * 
	 * @return myCorpus.the corpus id
	 */
	public String getCorpusId() {
		return myCorpus.corpusId;
	}

	/**
	 * Gets the type.
	 * 
	 * @return myCorpus.the type
	 */
	public String getType() {
		return myCorpus.type;
	}

	/**
	 * Gets the name.
	 * 
	 * @return myCorpus.the name
	 */
	public String getName() {
		return myCorpus.name;
	}

	/**
	 * Gets the uri.
	 * 
	 * @return myCorpus.the uri
	 */
	public String getUri() {
		return myCorpus.uri;
	}
	public Corpus getCorpus() {
		return myCorpus;
	}

}
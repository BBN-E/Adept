/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.mappers.handlers;

import thrift.adept.common.*;


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

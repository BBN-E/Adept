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
package adept.common;

// TODO: Auto-generated Javadoc
/**
 * The Class Corpus.
 */
public final class Corpus {

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
		this.corpusId = corpusId;
		this.type = type;
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

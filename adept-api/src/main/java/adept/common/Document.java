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

package adept.common;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Document, which represents a piece of written,
 * printed, or electronic matter that provides information 
 * or evidence or that serves as an official record.
 */
public class Document extends Item {

	/** The doc id. */
	private final String docId;

	/** The corpus. */
	private final Corpus corpus;

	/** The doc type. */
	private final String docType;

	/** The uri. */
	private final String uri;

	/** The language. */
	private final String language;

	/** The token stream list. */
	private List<TokenStream> tokenStreamList;

	/** The token lattice. */
	private List<TokenLattice> tokenLattice;
	
	/** The genre. */
	private String genre;
	
	/** The audio. */
	private Audio audio;

	/** The headline. */
	private String headline;	
	
	/** The captureDate. */
	private String captureDate;
	
	/** The publicationDate. */
	private String publicationDate;
	
	/** The name. */
	private String name;
	
	/** The splitId. */
	private String splitId;
	
	/** The source URI. */
	private String sourceUri;
	
	/** The source language. */
	private String sourceLanguage;
	

    /**
     * Instantiates a new document.
     *
     * @param docId the doc id
     * @param corpus the corpus
     * @param docType the doc type
     * @param uri the uri
     * @param language the language
     */
    //    private String rawValue;

	/**
	 * Instantiates a new text document.
	 * 
	 * @param docId
	 *            the doc id
	 * @param corpus
	 *            the corpus
	 * @param docType
	 *            the doc type
	 * @param uri
	 *            the uri
	 * @param language
	 *            the language
	 */
	public Document(String docId, Corpus corpus, String docType, String uri,
			String language) {
		this.docId = docId;
		this.corpus = corpus;
		this.docType = docType;
		this.uri = uri;
		this.language = language;
		this.tokenStreamList = null;
	}

	/**
	 * Gets the token stream list.
	 *
	 * @return the token stream list
	 */
	public List<TokenStream> getTokenStreamList() {
		return tokenStreamList;
	}

	/**
	 * Sets the token stream list.
	 *
	 * @param tokenStreamList the new token stream list
	 */
	public void setTokenStreamList(List<TokenStream> tokenStreamList) {
		this.tokenStreamList = tokenStreamList;
	}

	/**
	 * Sets the value.
	 *
	 * @param value the new value
	 */
    public void setValue(String value) {
		this.value = value;
    }

	/**
	 * Gets the doc id.
	 *
	 * @return the doc id
	 */
	public String getDocId() {
		return docId;
	}

	/**
	 * Gets the corpus.
	 *
	 * @return the corpus
	 */
	public Corpus getCorpus() {
		return corpus;
	}

	/**
	 * Gets the doc type.
	 *
	 * @return the doc type
	 */
	public String getDocType() {
		return docType;
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Gets the language.
	 *
	 * @return the language
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Gets the headline.
	 *
	 * @return the headline
	 */
	public String getHeadline() {
		return headline;
	}

	/**
	 * Sets the headline.
	 *
	 * @param headline the new headline
	 */
	public void setHeadline(String headline) {
		this.headline = headline;
	}
	
	/**
	 * Gets the captureDate.
	 *
	 * @return the captureDate
	 */
	public String getCaptureDate() {
		return captureDate;
	}

	/**
	 * Sets the captureDate.
	 *
	 * @param captureDate the new captureDate
	 */
	public void setCaptureDate(String captureDate) {
		this.captureDate = captureDate;
	}
	
	/**
	 * Gets the publicationDate.
	 *
	 * @return the publicationDate
	 */
	public String getPublicationDate() {
		return publicationDate;
	}

	/**
	 * Sets the publicationDate.
	 *
	 * @param publicationDate the new publicationDate
	 */
	public void setPublicationDate(String publicationDate) {
		this.publicationDate = publicationDate;
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
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the splitId.
	 *
	 * @return the splitId
	 */
	public String getSplitId() {
		return splitId;
	}

	/**
	 * Sets the splitId.
	 *
	 * @param splitId the new splitId
	 */
	public void setSplitId(String splitId) {
		this.splitId = splitId;
	}
	
	/**
	 * Gets the sourceLanguage.
	 *
	 * @return the sourceLanguage
	 */
	public String getSourceLanguage() {
		return sourceLanguage;
	}

	/**
	 * Sets the sourceLanguage.
	 *
	 * @param sourceLanguage the new sourceLanguage
	 */
	public void setSourceLanguage(String sourceLanguage) {
		this.sourceLanguage = sourceLanguage;
	}
	
	/**
	 * Gets the sourceUri.
	 *
	 * @return the sourceUri
	 */
	public String getSourceUri() {
		return sourceUri;
	}

	/**
	 * Sets the sourceUri.
	 *
	 * @param sourceUri the new sourceUri
	 */
	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}

	/**
	 * Adds the token stream.
	 *
	 * @param tokenStream the token stream
	 */
	public void addTokenStream(TokenStream tokenStream) {
		if (tokenStreamList == null) {
			tokenStreamList = new ArrayList<TokenStream>(1);
		}
		// add reference to this document in the tokenstream
//		tokenStream.setDocument(this);
		tokenStreamList.add(tokenStream);
	}

	/**
	 * Gets the default token stream.
	 *
	 * @return the token stream
	 */
	public TokenStream getDefaultTokenStream() {
		return getTokenStream( TokenizerType.STANFORD_CORENLP );
	}
	
	/**
	 * Gets the token stream.
	 *
	 * @param tokenizerType the tokenizer type
	 * @return the token stream
	 */
	public TokenStream getTokenStream(TokenizerType tokenizerType) {
		TokenStream outTokenStream = null;
		if ( tokenStreamList != null ) {
			for (TokenStream tokenStream : tokenStreamList) {
				if (tokenStream.getTokenizerType() == tokenizerType) {
					outTokenStream = tokenStream;
				}
			}
		}
		return outTokenStream;
	}

	/**
	 * Gets the genre.
	 *
	 * @return the genre
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * Sets the genre.
	 *
	 * @param genre the new genre
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * Gets the audio.
	 *
	 * @return the audio
	 */
	public Audio getAudio() {
		return audio;
	}

	/**
	 * Sets the audio.
	 *
	 * @param audio the new audio
	 */
	public void setAudio(Audio audio) {
		this.audio = audio;
	}

	/**
	 * Gets the token lattice.
	 *
	 * @return the token lattice
	 */
	public List<TokenLattice> getTokenLattice() {
		return tokenLattice;
	}

	/**
	 * Sets the token lattice.
	 *
	 * @param tokenLattice the new token lattice
	 */
	public void setTokenLattice(List<TokenLattice> tokenLattice) {
		this.tokenLattice = tokenLattice;
	}

	/**
	 * Gets the rawValue.
	 * 
	 * @return the rawValue
	 */
    /*	public String getRawValue() {
		return rawValue;
        }*/

	/**
	 * Sets the rawValue.
	 * 
	 * @param rawValue
	 *            the new rawValue
	 */
    /*	public void setRawValue(String rawValue) {
		this.rawValue = rawValue;
        }*/

}
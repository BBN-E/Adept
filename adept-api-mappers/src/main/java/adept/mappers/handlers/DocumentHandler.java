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

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class TextDocument.
 */
public class DocumentHandler extends ItemHandler implements DocumentService.Iface {

	private Document myDocument;

	/** The token stream list. */
	private List<TokenStream> tokenStreamList;

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
	public DocumentHandler(String docId, Corpus corpus, String docType, String uri,
			String language) {
		super();
		myDocument = new Document();
		myDocument.docId = docId;
		myDocument.corpus = corpus;
		myDocument.docType = docType;
		myDocument.uri = uri;
		myDocument.language = language;
		this.tokenStreamList = null;
		myDocument.id = myItem.id;
		myDocument.value = myItem.value;
	}

	/**
	 * Gets the token stream list.
	 * 
	 * @return myDocument.the token stream list
	 */
	public List<TokenStream> getTokenStreamList() {
		return this.tokenStreamList;
	}

	/**
	 * Sets the token stream list.
	 * 
	 * @param tokenStreamList
	 *            the new token stream list
	 */
	public void setTokenStreamList(List<TokenStream> tokenStreamList) {
		this.tokenStreamList = tokenStreamList;
	}

	/**
	 * Gets the audio uri.
	 * 
	 * @return myDocument.the audio uri
	 */
	public String getAudioUri() {
		return myDocument.audioUri;
	}

	/**
	 * Sets the audio uri.
	 * 
	 * @param audioUri
	 *            the new audio uri
	 */
	public void setAudioUri(String audioUri) {
		myDocument.audioUri = audioUri;
	}

	/**
	 * Sets the raw text value for myDocument document.
	 * 
	 * @param value
	 *            the new value
	 */
	public void setValue(String value) {
		myDocument.value = value;
	}

	/**
	 * Gets the doc id.
	 * 
	 * @return myDocument.the doc id
	 */
	public String getDocId() {
		return myDocument.docId;
	}

	/**
	 * Gets the corpus.
	 * 
	 * @return myDocument.the corpus
	 */
	public Corpus getCorpus() {
		return myDocument.corpus;
	}

	/**
	 * Gets the doc type.
	 * 
	 * @return myDocument.the doc type
	 */
	public String getDocType() {
		return myDocument.docType;
	}

	/**
	 * Gets the uri.
	 * 
	 * @return myDocument.the uri
	 */
	public String getUri() {
		return myDocument.uri;
	}

	/**
	 * Gets the language.
	 * 
	 * @return myDocument.the language
	 */
	public String getLanguage() {
		return myDocument.language;
	}

	/**
	 * Gets the headline.
	 * 
	 * @return myDocument.the headline
	 */
	public String getHeadline() {
		return myDocument.headline;
	}

	/**
	 * Sets the headline.
	 * 
	 * @param headline
	 *            the new headline
	 */
	public void setHeadline(String headline) {
		myDocument.headline = headline;
	}

	/**
	 * Adds the token stream.
	 * 
	 * @param tokenStream
	 *            the token stream
	 */
	public void addTokenStream(TokenStream tokenStream) {
		if (tokenStreamList == null) {
			this.tokenStreamList = new ArrayList<TokenStream>(1);
		}
		// add reference to myDocument document in the tokenstream
		tokenStream.setDocument(myDocument);
		this.tokenStreamList.add(tokenStream);
	}

	/**
	 * Gets the token stream.
	 * 
	 * @param tokenizerType
	 *            the tokenizer type
	 * @return myDocument.the token stream
	 */
	public TokenStream getTokenStream(TokenizerType tokenizerType) {
		TokenStream outTokenStream = null;
		for (TokenStream tokenStream : tokenStreamList) {
			if (tokenStream.getTokenizerType() == tokenizerType) {
				outTokenStream = tokenStream;
			}
		}
		return outTokenStream;
	}

	/**
	 * Gets the genre.
	 * 
	 * @return myDocument.the genre
	 */
	public String getGenre() {
		return myDocument.genre;
	}

	/**
	 * Sets the genre.
	 * 
	 * @param genre
	 *            the new genre
	 */
	public void setGenre(String genre) {
		myDocument.genre = genre;
	}

	public Document getDocument() {
		return myDocument;
	}

}
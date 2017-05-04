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

import adept.io.Reader;
import adept.utilities.DocumentMaker;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;


/**
 * The Class TokenStream, which is a list of tokens 
 * and represents the tokenized form of a entire document.  
 * If multiple tokenizers are used then a document 
 * may contain multiple TokenStream objects.
 */
public class TokenStream extends ArrayList<Token> implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 2407040331925099456L;

	/** The tokenizer type. */
	private final TokenizerType tokenizerType;

	/** The content type. */
	private final TranscriptType transcriptType;

	/** The language. */
	private final String language;

	/** The channel name. */
	private final ChannelName channelName;

	/** The content type. */
	private final ContentType contentType;

	/** The asr name. */
	private AsrName asrName;

	/** The speech unit. */
	private SpeechUnit speechUnit;

	/** The translator name. */
	private TranslatorName translatorName;

	/** The text value. */
	private String textValue;

	/** The document. */
	private final Document document;

	private static BiMap<String, String> treeBankBracketCodesToLiterals;
	static {
		treeBankBracketCodesToLiterals = HashBiMap.create();
		treeBankBracketCodesToLiterals.put("-LRB-", "(");
		treeBankBracketCodesToLiterals.put("-RRB-", ")");
		treeBankBracketCodesToLiterals.put("-LCB-", "{");
		treeBankBracketCodesToLiterals.put("-RCB-", "}");
		treeBankBracketCodesToLiterals.put("-LSB-", "[");
		treeBankBracketCodesToLiterals.put("-RSB-", "]");
	}

	/**
	 * private constructor.
	 * 
	 * @param tokenizerType
	 *            the tokenizer type
	 * @param transcriptType
	 *            the transcript type
	 * @param language
	 *            the language
	 * @param channelName
	 *            the channel name
	 * @param contentType
	 *            the content type
	 * @param textValue
	 *            the text value
	 */
	/*
	 * private TokenStream() { this(null,null,null,null,null,null); }
	 */

	/**
	 * Instantiates a new token stream.
	 * 
	 * @param tokenizerType
	 *            the tokenizer type
	 * @param transcriptType
	 *            the transcript type
	 * @param language
	 *            the language
	 * @param channelName
	 *            the channel name
	 * @param contentType
	 *            the content type
	 * @param document
	 *            the document
	 */
	public TokenStream(TokenizerType tokenizerType,
						TranscriptType transcriptType, 
						String language,
						ChannelName channelName, 
						ContentType contentType, 
						Document document) {
		super();

        checkArgument(tokenizerType!=null);           
		this.tokenizerType = tokenizerType;

                //TODO: null checks
		this.language = language;
		this.channelName = channelName;
		this.contentType = contentType;
		this.document = document;
        this.transcriptType = transcriptType;
//		document.addTokenStream(this);
		this.textValue = (null != document ? document.getValue() : null);
	}

	/**
	 * Gets the asr name.
	 * 
	 * @return the asr name
	 */
	public AsrName getAsrName() {
		return asrName;
	}

	/**
	 * Sets the asr name.
	 * 
	 * @param asrName
	 *            the new asr name
	 */
	public void setAsrName(AsrName asrName) {
                // TODO: null check
		this.asrName = asrName;
	}

	/**
	 * Gets the speech unit.
	 * 
	 * @return the speech unit
	 */
	public SpeechUnit getSpeechUnit() {
		return speechUnit;
	}

	/**
	 * Sets the speech unit.
	 * 
	 * @param speechUnit
	 *            the new speech unit
	 */
	public void setSpeechUnit(SpeechUnit speechUnit) {
                //TODO: null check
		this.speechUnit = speechUnit;
	}

	/**
	 * Gets the translator name.
	 * 
	 * @return the translator name
	 */
	public TranslatorName getTranslatorName() {
		return translatorName;
	}

	/**
	 * Sets the translator name.
	 * 
	 * @param translatorName
	 *            the new translator name
	 */
	public void setTranslatorName(TranslatorName translatorName) {
                //TODO: null check
		this.translatorName = translatorName;
	}


	/**
	 * Gets the text value.
	 * 
	 * @return the text value
	 */
	public String getTextValue() {
		return textValue;
	}

	/**
	 * Sets the text value.
	 *
	 * @param textValue
	 *            the new text value
	 */
	public void setTextValue(String textValue) {
		checkNotNull(this.textValue = textValue);
	}

	/**
	 * Gets the serialversionuid.
	 * 
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * Gets the tokenizer type.
	 * 
	 * @return the tokenizer type
	 */
	public TokenizerType getTokenizerType() {
		return tokenizerType;
	}

	/**
	 * Gets the transcript type.
	 * 
	 * @return the transcript type
	 */
	public TranscriptType getTranscriptType() {
		return transcriptType;
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
	 * Gets the channel name.
	 * 
	 * @return the channel name
	 */
	public ChannelName getChannelName() {
		return channelName;
	}

	/**
	 * Gets the content type.
	 * 
	 * @return the content type
	 */
	public ContentType getContentType() {
		return contentType;
	}

	/**
	 * Gets the document.
	 * 
	 * @return the document
	 */
	public Document getDocument() {
		return document;
	}

	/**
	 * Find the token offset of the given phrase, which represents a list of tokens for which to search.
	 * This method first attempts to do a token-value-to-token-value match of the tokenized phrase's token values
	 * to this stream's token values. If that fails, it will fall back to performing a string-based search for the
	 * phrase itself inside a single string generated from this stream's token values. The result of the fall-back
	 * attempt, if present, may represent a case where the beginning and/or end of the phrase intersects the token
	 * value(s) at the boundary(ies) of the returned offset.
	 * @param phrase the phrase for which to search, which represents a list of tokens for which to search.
	 * @return the token offset that represents the result, or <code>null</code> if the phrase was not found
	 */
	public TokenOffset findPhrase(String phrase) {
		return findPhrase(phrase, new TokenOffset(0, this.size() - 1));
	}
	
	/**
	 * Find the token offset of the given phrase, which represents a list of tokens for which to search.
	 * This method first attempts to do a token-value-to-token-value match of the tokenized phrase's token values
	 * to this stream's token values. If that fails, it will fall back to performing a string-based search for the
	 * phrase itself inside a single string generated from this stream's token values. The result of the fall-back
	 * attempt, if present, may represent a case where the beginning and/or end of the phrase intersects the token
	 * value(s) at the boundary(ies) of the returned offset.
	 * @param phrase the phrase for which to search, which represents a list of tokens for which to search.
	 * @param tokenOffset a token offset that limits the search space
	 * @return the token offset that represents the result, or <code>null</code> if the phrase was not found
	 */
	public TokenOffset findPhrase(String phrase, TokenOffset tokenOffset) {
		// normalize the whitespace around the phrase so that we can try the trimmed version first, and later try
		// adding a space at the end if needed (see comment below)
		phrase = phrase.trim();
		TokenOffset ret = findPhraseImpl(phrase, tokenOffset);
		if (ret == null) {
			// the Stanford tokenizer may process the last token differently depending on the presence of trailing
			// whitespace, so try again with a space at the end of the phrase
			ret = findPhraseImpl(phrase + " ", tokenOffset);
		}
		if (ret == null) {
			// this usually occurs when this stream uses a tokenization other than Stanford's; fall back to a
			// string-based search
			ret = findPhraseFallBack(phrase, tokenOffset);
		}
		return ret;
	}

	private TokenOffset findPhraseImpl(String phrase, TokenOffset tokenOffset) {
		List<String> phraseTokenValues = DocumentMaker.getInstance().tokenize(phrase, null).tokenValues();
		int index = Collections.indexOfSubList(this.tokenValues(tokenOffset), phraseTokenValues);
		return index == -1 ? null : new TokenOffset(index + tokenOffset.getBegin(), index + phraseTokenValues.size() - 1 + tokenOffset.getBegin());
	}

	private static String applyReplacementMappings(String s, Map<String, String> targetsToReplacements) {
		for (String target : targetsToReplacements.keySet()) {
			s = s.replace(target, targetsToReplacements.get(target));
		}
		return s;
	}

	private TokenOffset findPhraseFallBack(String phrase, TokenOffset tokenOffset) {
		TokenOffset ret = findPhraseFallBackImpl(applyReplacementMappings(phrase, treeBankBracketCodesToLiterals), tokenOffset);
		if (ret == null) {
			ret = findPhraseFallBackImpl(applyReplacementMappings(phrase, treeBankBracketCodesToLiterals.inverse()), tokenOffset);
		}
		return ret;
	}

	private TokenOffset findPhraseFallBackImpl(String phrase, TokenOffset tokenOffset) {
		// Represents the token values from the stream where whitespace has been removed
		// For example, if the token values are ["one", "two three", "four"] then this will
		// become "onetwothreefour".
		StringBuilder condensedSubstreamBuilder = new StringBuilder();
		// Maps to the condensedSubstream, where the values represent the token index
		// where the character came from. Continuing the previous example, this would be:
		// [0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 2, 2, 2 ,2]
		List<Integer> tokenValueCharacterTokenPositions = new ArrayList<>();

		List<String> tokenValues = this.tokenValues(tokenOffset);
		for (int i = 0; i < tokenValues.size(); i++) {
			String tokenValue = tokenValues.get(i);
			String[] tokenCharacters = tokenValue.replaceAll("\\s", "").split("");
			for (String tokenCharacter : tokenCharacters) {
				condensedSubstreamBuilder.append(tokenCharacter);
				tokenValueCharacterTokenPositions.add(i);
			}
		}
		String condensedSubstream = condensedSubstreamBuilder.toString();
		String condensedPhrase = phrase.replaceAll("\\s", "");

		int index = condensedSubstream.indexOf(condensedPhrase);
		if (index == -1) {
			return null;
		}
		return new TokenOffset(
				tokenOffset.getBegin() + tokenValueCharacterTokenPositions.get(index),
				tokenOffset.getBegin() + tokenValueCharacterTokenPositions.get(index + (condensedPhrase.length() - 1))
		);
	}

	public List<String> tokenValues() {
		return tokenValues(new TokenOffset(0, this.size() - 1));
	}

	public List<String> tokenValues(TokenOffset tokenOffset) {
		List<String> values = new ArrayList<>(tokenOffset.getEnd() - tokenOffset.getBegin() + 1);
		for (Token t : this.subList(tokenOffset.getBegin(), tokenOffset.getEnd() + 1)) {
			values.add(t.getValue());
		}
		return values;
	}

	/**
	 * Given a string, return a corresponding regex pattern string where '\s*' is present
	 * between each non-whitespace character (and where each original character in the
	 * string is placed inside regex literal markers). For example:
	 * buildWhitespaceSaturatedRegexPattern("foo bar") -> "\Qf\E\s*\Qo\E\s*\Qo\E\s*\Qb\E\s*\Qa\E\s*\Qr\E"
	 * @param s The string to whitespace-saturate
	 * @return The corresponding white-space saturated regex pattern string
	 */
	private static String buildWhitespaceSaturatedRegexPattern(String s) {
		StringBuilder sb = new StringBuilder();
		String[] parts = s.replaceAll("\\s*", "").split("");
		for (int i = 0; i < parts.length - 1; i++) {
			String part = parts[i];
			if (!part.isEmpty()) {
				sb.append(Pattern.quote(part)).append("\\s*");
			}
		}
		sb.append(Pattern.quote(parts[parts.length - 1]));
		return sb.toString();
	}

	/**
	 * Locate each token in this stream by its value (as it appears as a substring in {@link #getTextValue()}),
	 * and shift its charOffset to align with the corresponding substring in the given equivalent text (i.e., the
	 * text must consist of the same token values in the same order, but may differ with regard to whitespace characters
	 * around/between the tokens).
	 * Also then set this stream's text value to be the argument.
	 */
	public void reAlignToText(String text) {
		int currentSearchIndex = 0;
		for (int i = 0; i < this.size(); i++) {
			Token searchToken = this.get(i);
			String tokenValue = this.getTextValue().substring(searchToken.getCharOffset().getBegin(), searchToken.getCharOffset().getEnd());

			// In practice, this method is used to align tokens obtained from the tokenization of an XML document to
			// a "raw" XML text value. In this case, the tokens in this stream are obtained by processing the file
			// using an XML parser, which unescapes any special XML characters that may be present.
			// However, the text argument will be the *raw* text of the same document (i.e., still including escaped
			// XML special characters, but where not ALL the special characters are necessarily in escaped form in
			// original file).
			// Therefore, since we don't know whether a given special XML character is actually escaped in the given
			// text, we need to be able to search for all unescaped/escaped combinations.
			List<String> candidateSearchTargets = new LinkedList<>();
			// loop through all possible combinations of unescaped vs. escaped for each special character
			for (byte specialCharacterBooleanByte = 0; specialCharacterBooleanByte < 32; specialCharacterBooleanByte++) {
				String template  = tokenValue;
				// read the result of the combination and make the appropriate replacements
				for (int specialCharacterIdentifier = 0; specialCharacterIdentifier <= 4; specialCharacterIdentifier++) {
					if ((specialCharacterBooleanByte >> specialCharacterIdentifier) % 2 == 1) {
						// The "&" -> "&amp;" replacement must be first; otherwise the "&" in other escape sequences
						// (e.g., "&lt;") would erroneously itself be escaped
						if (specialCharacterIdentifier == 0)  { template = template.replace("&", "&amp;"); }
						if (specialCharacterIdentifier == 1)  { template = template.replace("<", "&lt;"); }
						if (specialCharacterIdentifier == 2)  { template = template.replace(">", "&gt;"); }
						if (specialCharacterIdentifier == 3)  { template = template.replace("\"", "&quot;"); }
						if (specialCharacterIdentifier == 4)  { template = template.replace("'", "&apos;"); }
					}
				}

				String candidateSearchTarget = buildWhitespaceSaturatedRegexPattern(template);
				if (!candidateSearchTargets.contains(candidateSearchTarget)) {
					// Ensure that for any template that ends in an ampersand that the escaped version appears before the
					// unescaped one. This is so that we never inadvertently short-match it.
					// For example, assuming the text "Jane &amp; Doe", we want to front-load searching for "Jane &amp;"
					// as opposed to "Jane &".
					int insertionIndex = template.endsWith("&amp;") ? 0 : candidateSearchTargets.size();
					candidateSearchTargets.add(insertionIndex, candidateSearchTarget);
				}
			}

			CharOffset bestSearchResult = null;
			for (String candidateSearchTarget : candidateSearchTargets) {
				CharOffset searchResult = Reader.searchByRegex(text, currentSearchIndex, candidateSearchTarget);
				if (searchResult != null) {
					if (bestSearchResult == null || searchResult.getBegin() < bestSearchResult.getBegin()) {
						bestSearchResult = searchResult;
					}
				}
			}
			if (bestSearchResult == null) {
				throw new NoSuchElementException(tokenValue);
			}
			this.set(i, Token.modifyCharOffset(searchToken, bestSearchResult));
			currentSearchIndex = bestSearchResult.getEnd();
		}
		setTextValue(text);
	}
}

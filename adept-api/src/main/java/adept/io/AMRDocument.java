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

package adept.io;

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

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

import adept.common.Corpus;
import adept.common.Document;
import adept.common.Passage;
import adept.common.Sentence;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.utilities.StanfordTokenizer;

/**
 * The class AMRDocument.
 */
public final class AMRDocument
{
  private static final String SENTENCE_PREFIX = "# ::snt";
  private static final String FILE_PREFIX = "::file";

  /** The ADEPT document. */
  private Document document;

  /** Sentence start indices. */
  private List<Integer> startIndices = Lists.newLinkedList();

  /** Sentence end indices. */
  private List<Integer> endIndices = Lists.newLinkedList();

  private List<Sentence> sentences = Lists.newArrayList();
  private List<Passage> passages = Lists.newArrayList();


  /**
   * Instantiates a new AMR document.
   */
  public AMRDocument(String fullText) {
    this.document = createAdeptDocument();
    parseFullText(fullText);
  }

  /**
   * Gets the document.
   *
   * @return the document
   */
  public Document getDocument()
  {
    return document;
  }

  /**
   * Gets the Sentence list.
   *
   * @return the sentence list
   */
  public List<Sentence> getSentences() { return sentences; }

  /**
   * Get the Passage list.
   *
   * @return the passage list
   */
  public List<Passage> getPassages() { return passages; }


  /**
   * Creates the ADEPT document.
   *
   * @return the document
   */
  private Document createAdeptDocument()
  {
    String headline = "";
    //Document constructor fields:
    String docId = "000"; //hardcoded for now
    String docType = "AMR";
    String uri = ""; //empty for now
    Corpus corpus = null; //empty for now
    String language = "English"; //hardcoded for now

    return new Document(docId, corpus, docType, uri, language);
  }

  /**
   * Parses the string representing the full text of the AMR
   * and sets values for document and tokens.
   */
  private void parseFullText(String fullText)  {
    List<String> sentenceStrings = new ArrayList<>();
    String newline = "\n";
    if (fullText.contains("\r\n")) {
      newline = "\r\n";
    }
    String[] lines = fullText.split(newline);
    for (int x = 0; x < lines.length; x++) {
      String line = lines[x].trim();
      if (line.startsWith(SENTENCE_PREFIX)) {
        String cleanText = line.replace(SENTENCE_PREFIX, "").trim();
        sentenceStrings.add(cleanText);
      }
    }

    String text = createDocText(sentenceStrings);
    document.setValue(text);
    document.addTokenStream(StanfordTokenizer.getInstance().tokenize(document.getValue(), document));
    sentences = createSentences();
    passages = createPassages();
  }

  /**
   *
   *
   * @return text
   */
  private String createDocText(List<String> sentenceStrings) {
    StringBuilder textBuffer = new StringBuilder();
    for (String s : sentenceStrings) {
      startIndices.add(textBuffer.length());
      textBuffer.append(s).append("\n");
      endIndices.add(textBuffer.length()-1);
    }
    return textBuffer.toString();
  }

  private List<Sentence> createSentences() {
    List<Sentence> sentences = new ArrayList<>();

    TokenStream tokenStream = document.getDefaultTokenStream();
    int tokenIndex = 0;

    for (int sentenceIndex = 0; sentenceIndex < startIndices.size(); sentenceIndex++) {
      int sentenceStartOffset = startIndices.get(sentenceIndex);
      int sentenceEndOffset = endIndices.get(sentenceIndex);
      boolean haveBegun = false;
      int begin = 0;
      int end = 0;

      for (int i = tokenIndex; i < tokenStream.size(); i++) {
        Token token = tokenStream.get(i);
        if (!haveBegun && token.getCharOffset().getBegin() >= sentenceStartOffset) {
          begin = i;
          end = i;
          haveBegun = true;
        }
        if (token.getCharOffset().getEnd() > sentenceEndOffset) {
          tokenIndex = i;
          break;
        }
        end = i;
      }
      if (haveBegun) {
        TokenOffset tokenOffset = new TokenOffset(begin, end);
        Sentence sentence = new Sentence(sentenceIndex, tokenOffset, tokenStream);
        sentences.add(sentence);
      }
    }

    return sentences;
  }

  private List<Passage> createPassages() {
    List<Passage> passages = new ArrayList<>();

    TokenStream tokenStream = document.getDefaultTokenStream();
    int tokenIndex = 0;

    for (int passageIndex = 0; passageIndex < startIndices.size(); passageIndex++) {
      int passageStartOffset = startIndices.get(passageIndex);
      int passageEndOffset = endIndices.get(passageIndex);
      boolean haveBegun = false;
      int begin = 0;
      int end = 0;

      for (int i = tokenIndex; i < tokenStream.size(); i++) {
        Token token = tokenStream.get(i);
        if (!haveBegun && token.getCharOffset().getBegin() >= passageStartOffset) {
          begin = i;
          end = i;
          haveBegun = true;
        }
        if (token.getCharOffset().getEnd() > passageEndOffset) {
          tokenIndex = i;
          break;
        }
        end = i;
      }
      if (haveBegun) {
        TokenOffset tokenOffset = new TokenOffset(begin, end);
        Passage passage = new Passage(passageIndex, tokenOffset, tokenStream);
        passages.add(passage);
      }
    }

    return passages;
  }
}
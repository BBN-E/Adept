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

import java.io.*;
import java.util.*;
import adept.common.*;
import adept.utilities.DocumentMaker;


/**
 * The Class CoNLLDocument.
 */
public class CoNLLDocument
{
    /** The document. */
    private Document document;
    
    /** The full text. */
    private String fullText;
    
    /** The newline. */
    private final String NEWLINE;

    /** The word nums. */
    private List<List<Integer>> wordNums;
    
    /** The tokens. */
    private List<List<String>> tokens;
    
    /** The PO ss. */
    private List<List<String>> POSs;
    
    /** The named entities. */
    private List<List<Pair<String,Long>>> namedEntities;
    
    /** The corefs. */
    private List<List<Set<Long>>> corefs;

    /** The tokens to po ss. */
    private Map<Token,String> tokensToPOSs;

    /** The wordnum. */
    private static final int WORDNUM = 2;
    
    /** The token. */
    private static final int TOKEN = 3;
    
    /** The pos. */
    private static final int POS = 4;
    
    /** The nentity. */
    private static final int NENTITY = 10;


    /**
     * Instantiates a new co nll document.
     *
     * @param fullText the full text
     */
    public CoNLLDocument(String fullText)
    {
        this.fullText = fullText;
        this.document = null;
        if(fullText.contains("\r\n"))
            NEWLINE = "\r\n";
        else
            NEWLINE = "\n";
        wordNums = new ArrayList<List<Integer>>();
        tokens = new ArrayList<List<String>>();
        POSs = new ArrayList<List<String>>();
        namedEntities = new ArrayList<List<Pair<String,Long>>>();
        corefs = new ArrayList<List<Set<Long>>>();
        tokensToPOSs = new HashMap<Token, String>();
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
     * Gets the full text.
     *
     * @return the full text
     */
    public String getFullText()
    {
        return fullText;
    }

    /**
     * Gets the named entities.
     *
     * @return the named entities
     */
    public List<List<Pair<String,Long>>> getNamedEntities()
    {
        return namedEntities;
    }

    /**
     * Gets the corefs.
     *
     * @return the corefs
     */
    public List<List<Set<Long>>> getCorefs()
    {
        return corefs;
    }

    /**
     * Gets the tokens.
     *
     * @return the tokens
     */
    public List<List<String>> getTokens()
    {
        return tokens;
    }

    /**
     * Gets the pOS by token.
     *
     * @param t the t
     * @return the pOS by token
     */
    public String getPOSByToken(Token t)
    {
        return tokensToPOSs.get(t);
    }

    /**
     * Process header.
     *
     * @param line the line
     * @return the string
     */
    public String processHeader(String line)
    {
        String[] parts = line.split(" ");
        if(parts.length < 3)
            return "DocId";
        return parts[2];
    }

    /**
     * Parses the full text.
     *
     * @return the string
     */
    public String parseFullText()
    {
        fullText = fullText.replaceAll(" +", "\t");
        String[] lines = fullText.split(NEWLINE);
        String line;
        String docId = "";
        List<Integer> theseWordNums = new ArrayList<Integer>();
        List<String> theseTokens = new ArrayList<String>();
        List<String> thesePOSs = new ArrayList<String>();
        List<Pair<String,Long>> theseNamedEntities = new ArrayList<Pair<String,Long>>();
        List<Set<Long>> theseCorefs = new ArrayList<Set<Long>>();
        Set<Long> currCorefs = new HashSet<Long>();
        boolean hasNamedEntity = false;
        String currNamedEntity = "";
        int wordCount = 0;
        long entityNum = -1;
        for(int x = 0; x < lines.length; x++)
        {
            line = lines[x].trim();
            String[] parts = line.split("\t");
            int COREF = parts.length - 1;
            if(parts.length < 12)
            {
                if(line.length() == 0)
                { //finish off the sentences
                    wordNums.add(theseWordNums);
                    tokens.add(theseTokens);
                    POSs.add(thesePOSs);
                    namedEntities.add(theseNamedEntities);
                    corefs.add(theseCorefs);
                    theseWordNums = new ArrayList<Integer>();
                    theseTokens = new ArrayList<String>();
                    thesePOSs = new ArrayList<String>();
                    theseNamedEntities = new ArrayList<Pair<String,Long>>();
                    theseCorefs = new ArrayList<Set<Long>>();
                    continue;
                }
                else if(line.contains("#begin"))
                {
                    docId = processHeader(line);
                    continue;
                }
                break;
            }
            theseWordNums.add(wordCount++);
            theseTokens.add(parts[TOKEN]);
            thesePOSs.add(parts[POS]);

            //named entity
            if(parts[NENTITY].trim().charAt(0) == '(')
            {
                currNamedEntity = parts[NENTITY].replace("(","").replace(")","").trim();
                hasNamedEntity = true;
                entityNum += 1;
            }
            if(hasNamedEntity)
                theseNamedEntities.add(new Pair<String,Long>(currNamedEntity, entityNum));
            else
                theseNamedEntities.add(new Pair<String,Long>("",-1L));
            if(parts[NENTITY].trim().charAt(parts[NENTITY].length()-1) == ')')
                hasNamedEntity = false;

            //coreference
            List<String> corefs = new ArrayList<String>();
            if(parts[COREF].contains("|"))
            {
                corefs = Arrays.asList(parts[COREF].trim().split("\\|"));
            }
            else
                corefs.add(parts[COREF]);
            for(String coref : corefs)
            { 
                if(coref.charAt(0) == '(')
                    currCorefs.add(Long.parseLong(coref.replace("(","").replace(")","")));
                theseCorefs.add(new HashSet<Long>(currCorefs));
                if(coref.charAt(coref.length()-1) == ')')
                    currCorefs.remove(Long.parseLong(coref.replace("(","").replace(")","")));
            }
        }
        return docId;
    }

    /**
     * Gets the tag by index.
     *
     * @param index the index
     * @return the tag by index
     */
    public String getTagByIndex(int index)
    {
        int sentence = 0;
        System.out.println("sentence: " + sentence + " index: " + index + " out of: " + POSs.size());
        while(POSs.get(sentence).size() <= index)
        {
            index -= POSs.get(sentence).size();
            sentence++;
            System.out.println("sentence: " + sentence + " index: " + index + " out of: " + POSs.size());
        }

        return POSs.get(sentence).get(index);
    }

    /**
     * Populates a map of starting offsets for text in document.
     *
     * @return text
     */
    public String createDocText()
    {
        StringBuilder textBuffer = new StringBuilder();
        for(int x = 0; x < tokens.size(); x++)
        {
            for(int y = 0; y < tokens.get(x).size(); y++)
            {
                textBuffer.append(getSpace(tokens.get(x).get(y)));
                textBuffer.append(tokens.get(x).get(y));
            }
        }
        return textBuffer.toString();
    }    

    /**
     * Gets the space.
     *
     * @param token the token
     * @return the space
     */
    private String getSpace(String token)
    {
        char firstChar = token.charAt(0);
        if((firstChar >= 65 && firstChar <= 90) 
           || (firstChar >= 97 && firstChar <= 122) 
           || firstChar == 40 || firstChar == 91 || firstChar == 123
           || token.equals("n't"))
            return " ";
        return "";
    }

    /**
     * Tokenize.
     *
     * @param text the text
     * @return the token stream
     */
    private TokenStream tokenize(String text)
    {
        TokenStream ts = new TokenStream(TokenizerType.STANFORD_CORENLP, 
                                         TranscriptType.SOURCE,
                                         "English",
                                         ChannelName.NONE,
                                         ContentType.TEXT,
                                         document);
        int charOffset = 0;
        for(int x = 0; x < tokens.size(); x++)
        {
            for(int y = 0; y < tokens.get(x).size(); y++)
            {
                String tokenString = tokens.get(x).get(y);
                charOffset += getSpace(tokenString).length();
                Token t = new Token((long)(x+y), new CharOffset(charOffset,charOffset+tokenString.length()), tokenString);
                charOffset += tokenString.length();
                tokensToPOSs.put(t, POSs.get(x).get(y));
                ts.add(t);
            }
        }
//        ts.setDocument(document);
        return ts;
    }

    /**
     * Creates the document.
     *
     * @return the document
     */
    public Document createDocument()
    {
        String headline = "";
        //Document constructor fields:
        String docId = "000"; //hardcoded for now
        String docType = "CoNLL";
        String uri = ""; //empty for now
        Corpus corpus = null; //empty for now
        String language = "English"; //hardcoded for now

        parseFullText();

        document = new Document(docId, corpus, docType, uri, language);
        String text = createDocText();
        document.setValue(text);
        document.addTokenStream(tokenize(text));
                //                document.setRawValue(fullText);
        //        setRawCharOffsets();
        //        createCharToTokenOffset();
        return document;
    }

}
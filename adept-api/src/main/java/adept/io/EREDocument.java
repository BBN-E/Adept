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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import adept.utilities.DocumentMaker;
import adept.utilities.StanfordSentenceSegmenter;
import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.Corpus;
import adept.common.CharOffset;
import adept.common.TokenStream;
import adept.common.Token;
import adept.common.EntityMention;
import adept.common.Event;


/**
 * The Class EREDocument
 * A wrapper for Document containing format information for ERE files.
 */
public class EREDocument
{

    /** The document. */
    private HltContentContainer hltcc;
    
    /** The document. */
    private Document document;
    
    /** The full text. */
    private String fullText;

    /** The char mapping. */
    private HashMap<Long,Long> charMapping; 
    
    /** The char to token offset. */
    private HashMap<Long,Long> charToTokenOffset;
    
    /** The start indices. */
    private LinkedList<Integer> startIndices;
    
    /** The end indices. */
    private LinkedList<Integer> endIndices;
    
    /** The entity mentions by id. */
    private HashMap<Long,EntityMention> entityMentionsById;
    
    /** The canonical entity mentions by id. */
    private HashMap<Long,EntityMention> canonicalEntityMentionsById;

    /** The is proxy. */
    private boolean isProxy;

    /** This is discussion forum. */
    private boolean isDiscussionForum;

    /** The events by id. */
    private HashMap<Long,ArrayList<Event>> eventsById;

    /**
     * Instantiates a new eRE document.
     *
     * @param fullText the full text
     */
    public EREDocument(String fullText, HltContentContainer hltcc)
    {
        this.fullText = fullText;
        this.hltcc = hltcc;
        this.document = null;
        this.charMapping = new HashMap<Long,Long>();
        this.charToTokenOffset = new HashMap<Long,Long>();
        this.startIndices = new LinkedList<Integer>();
        this.endIndices = new LinkedList<Integer>();
        this.entityMentionsById = new HashMap<Long,EntityMention>();
        this.canonicalEntityMentionsById = new HashMap<Long,EntityMention>();
        this.eventsById = new HashMap<Long,ArrayList<Event>>();
        this.isProxy = false;
        this.isDiscussionForum = false;
        if(!this.fullText.contains("TEXT:") && !this.fullText.contains("(ENDALL)"))
        {
            if(!this.fullText.contains("</post>") && !this.fullText.contains("</POST>"))
            {
                System.out.println("Identified Proxy File");
                this.fullText = this.fullText.replaceAll("\r","");
                this.isProxy = true;
            }
            else
            {
                System.out.println("Identified Discussion Forum File");
                this.fullText = this.fullText.replaceAll("\r","");
                this.isDiscussionForum = true;
            }
        }
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
     * Gets the hltcccontentcontainer.
     *
     * @return the hltcontentcontainer
     */
    public HltContentContainer getHltContentContainer()
    {
        return hltcc;
    }

    /**
     * Finds indices for text outside brackets.
     */
    private void findBrackets()
    {

        Pattern pattern = Pattern.compile("\\[[^\\[|\\]]*\\]");
        int lastBlock = startIndices.size();
        for(int index = 0; index < lastBlock; index++)
        {
            Matcher matcher = pattern.matcher(fullText);
            while (matcher.find())
            {
                if(matcher.start() >= startIndices.get(index) &&
                   matcher.end() < endIndices.get(index))
                {
                    startIndices.add(matcher.end());
                    endIndices.add(matcher.start());
                }
            }
        }
    }

    /**
     * Finds indices for text within TEXT: and (MORE)/(ENDALL).
     *
     * @param quitIndex the quit index
     */
    private void findTextBlocks(int quitIndex)
    {
        int textIndex = 0;
        textIndex = fullText.indexOf("TEXT:", textIndex+7);
        while(textIndex > 0 && textIndex < quitIndex)
        {
            int endBlockIndex = fullText.indexOf("(MORE)", textIndex+7);
            int finalBlockIndex = fullText.indexOf("(ENDALL)", textIndex + 7);
            if(finalBlockIndex < endBlockIndex || endBlockIndex < 0)
                endBlockIndex = finalBlockIndex;
            int nextTextIndex = fullText.indexOf("TEXT:", textIndex+7);
            if((endBlockIndex < nextTextIndex && endBlockIndex >= 0) || (nextTextIndex < 1 && endBlockIndex >= 0))
            {
               startIndices.add(textIndex+7);
            }
            textIndex = nextTextIndex;
        }
        int moreIndex = fullText.indexOf("(MORE)");
        while(moreIndex > 0 && moreIndex < quitIndex)
        {
            endIndices.add(moreIndex);
            moreIndex = fullText.indexOf("(MORE)", moreIndex+8);
        }
        endIndices.add(quitIndex);
    }

    /**
     * Finds indices for text within SUMMARY: and BODY:.
     */
    private void findProxyBlocks()
    {
        /* IT TURNS OUT THE TAGS MAY BE ENTITIES
        int country = fullText.indexOf("COUNTRY:");
        int topic = fullText.indexOf("TOPIC:");
        int summary = fullText.indexOf("SUMMARY:");
        int body = fullText.indexOf("BODY:");

        startIndices.add(country + 9);
        startIndices.add(topic + 7);
        startIndices.add(summary + 9);
        startIndices.add(body + 6);
        endIndices.add(topic);
        endIndices.add(summary);
        endIndices.add(body);
        endIndices.add(fullText.length());*/
        startIndices.add(0);
        endIndices.add(fullText.length());
    }

    /**
     * Finds indices of all document text.
     */
    private void findTextOffsets()
    {
        if(!isProxy && !isDiscussionForum)
        {
            findTextBlocks(fullText.indexOf("(ENDALL)"));
            findBrackets();
        }
        else //TODO proxy blocks are the same as discussion forum blocks
            findProxyBlocks();
        Collections.sort(startIndices);
        Collections.sort(endIndices);
    }

    /**
     * Populates a map of starting offsets for text in document.
     *
     * @return text
     */
    public String createDocText()
    {
        StringBuilder textBuffer = new StringBuilder();
        if(startIndices.isEmpty())
            findTextOffsets();
        for(int index = 0; index < startIndices.size(); index++)
        {
            textBuffer.append(fullText.substring(startIndices.get(index),endIndices.get(index)).replaceAll("\r",""));
        }
        return textBuffer.toString();
    }

    /**
     * Creates the char to token offset.
     */
    public void createCharToTokenOffset()
    {
        TokenStream tStream = document.getTokenStreamList().get(0);
        for(int x = 0; x < tStream.size(); x++)
        {
            CharOffset charOffset = tStream.get(x).getCharOffset();
            for(int offset = charOffset.getBegin(); offset <= charOffset.getEnd(); offset++)
            {
                charToTokenOffset.put((long)offset,(long)x);
            }
        }
    }

    /**
     * Creates the char mapping.
     */
    public void createCharMapping()
    {
        if(startIndices.isEmpty())
            findTextOffsets();
        int max = -1;
        for(int index = 0; index < startIndices.size(); index++)
        {
            for(int x = startIndices.get(index); x < endIndices.get(index); x++)
            {
                if(fullText.charAt(x) != '\r')
                    charMapping.put((long)x,(long)++max);
            }
        }
    }

    /*    public void setRawCharOffsets()
    {
        HashMap<Long,Long> revCharMapping = new HashMap<Long,Long>();
        for(Long key : charMapping.keySet())
        {
            revCharMapping.put(charMapping.get(key), key);
        }

        TokenStream tStream = document.getTokenStreamList().get(0);
        for(Token t : tStream)
        {
            int oldBegin = t.getCharOffset().getBegin();
            int oldEnd = t.getCharOffset().getEnd();
            CharOffset rawOffset = new CharOffset((int)(long)revCharMapping.get((long)oldBegin),(int)(long)revCharMapping.get((long)oldEnd));
            t.setRawCharOffset(rawOffset);
        }
        }*/

    /**
     * Creates the document.
     *
     * @param docId the doc id
     * @return the document
     */
    public Document createDocument(String docId, String language, String filename)
    {
        String[] lines = fullText.split("\r\n");
        String line;
        String headline = "";
        //Document constructor fields:
        String docType = "UNKNOWN";
        String uri = ""; //empty for now
        Corpus corpus = null; //empty for now
        if(language == null)
            language = "English"; //hardcoded for now

        if(!isDiscussionForum)
        {
            for(int x = 0; x < lines.length; x++)
            {
                line = lines[x];
                String[] parts = line.split(":");
                //process base on parts[0]
                if("SERIAL".equals(parts[0]) && !isProxy)
                    docId = parts[1].trim();
                else if("TITLE".equals(parts[0]) && !isProxy)
                    headline = parts[1].trim();
                else if("CLASSIF".equals(parts[0]) && !isProxy)
                    docType = parts[1].trim();
                else if("TEXT".equals(parts[0]) && docId.length() > 0 && !isProxy)
                    break;
                else if("TOPIC".equals(parts[0]) && isProxy)
                    headline = parts[1].trim();
                else if("COUNTRY".equals(parts[0]) && isProxy)
                    docType = parts[1].trim();
                else if("BODY".equals(parts[0]) && isProxy)
                    break;
            }
            document = new Document(docId, corpus, docType, uri, language);
            document.setHeadline(headline);
            String text = createDocText();
            document.setValue(text);
            document.addTokenStream(DocumentMaker.getInstance().tokenize(text, document));
            hltcc.setSentences(StanfordSentenceSegmenter.getInstance().getSentences(document.getValue(),document.getTokenStreamList().get(0)));
        }
        else
        {
//            document = DocumentMaker.getInstance().createCleanedXMLDocument(docId, corpus, "Discussion Forum", uri, language, filename, hltcc, true);
        }
        //        document.setRawValue(fullText);
        //        setRawCharOffsets();
        createCharToTokenOffset();
        return document;
    }

    /**
     * Returns calculated mapping for offset from ERE transcription doc.
     *
     * @param rawOffset offset to map
     * @return mapped offset or -1 if rawOffset isn't mapped
     */
    public long getCalculatedOffset(long rawOffset)
    {
        return charMapping.containsKey(rawOffset) ? charMapping.get(rawOffset) : -1;
    }

    /**
     * Gets the token offset.
     *
     * @param charOffset the char offset
     * @return the token offset
     */
    public long getTokenOffset(long charOffset)
    {
        return charToTokenOffset.containsKey(charOffset) ? charToTokenOffset.get(charOffset) : -1;
    }

    /**
     * Gets the entity mention by id.
     *
     * @param id the id
     * @return the entity mention by id
     */
    public EntityMention getEntityMentionById(long id)
    {
        return entityMentionsById.get(id);
    }

    /**
     * Gets the entity mentions by id.
     *
     * @return the entity mentions by id
     */
    public HashMap<Long,EntityMention> getEntityMentionsById()
    {
        return entityMentionsById;
    }

    /**
     * Gets the event by id.
     *
     * @param id the id
     * @return the event by id
     */
    public ArrayList<Event> getEventById(long id)
    {
        return eventsById.get(id);
    }

    /**
     * Gets the events by id.
     *
     * @return the events by id
     */
    public HashMap<Long,ArrayList<Event>> getEventsById()
    {
        return eventsById;
    }

    /**
     * Sets the event by id.
     *
     * @param id the id
     * @param e the e
     * @return the hash map
     */
    public HashMap<Long,ArrayList<Event>> setEventById(long id, Event e)
    {
        if(eventsById.containsKey(id))
            eventsById.get(id).add(e);
        else
        {
            ArrayList<Event> events = new ArrayList<Event>();
            events.add(e);
            eventsById.put(id, events);
        }
        return eventsById;
    }

    /**
     * Gets the canonical entity mention by id.
     *
     * @param id the id
     * @return the canonical entity mention by id
     */
    public EntityMention getCanonicalEntityMentionById(long id)
    {
        return canonicalEntityMentionsById.get(id);
    }

    /**
     * Put canonical entity mention by id.
     *
     * @param id the id
     * @param emId the em id
     * @return the entity mention
     */
    public EntityMention putCanonicalEntityMentionById(long id, long emId)
    {
        if(canonicalEntityMentionsById.get(id) != null)
            return canonicalEntityMentionsById.get(id);
        EntityMention em = getEntityMentionById(emId);
        canonicalEntityMentionsById.put(id, em);
        return em;
    }
}

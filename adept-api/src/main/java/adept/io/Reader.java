/*
* Copyright (C) 2016 Raytheon BBN Technologies Corp.
*
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
*
*/

package adept.io;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.input.BOMInputStream;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import adept.common.Document;
import adept.common.OntTypeFactory;
import adept.common.TokenStream;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.Type;
import adept.common.HltContentContainer;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.Event;
import adept.common.EventRelations;
import adept.common.Relation;
import adept.common.Argument;
import adept.common.Coreference;
import adept.common.Chunk;
import adept.common.Pair;
import adept.common.PartOfSpeech;
import adept.common.Passage;
import adept.common.Sentence;
import adept.utilities.DocumentMaker;
import adept.utilities.PassageAttributes;

import java.util.List;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;



//import org.w3c.dom.Document; //conflicts with adept.common.Document
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;




/**
 * The Class Reader.
 */
public class Reader {

    /**
     * The instance.
     */
    private static Reader instance;

    /**
     * Gets the single instance of Reader.
     *
     * @return single instance of Reader
     */
    public static Reader getInstance() {
        if (instance == null)
            instance = new Reader();
        return instance;
    }

    /**
     * Reads specified XML file into a DOM object.
     *
     * @param path the path
     * @return the document
     */
    public org.w3c.dom.Document readXML(String path) {
        try {
            InputStream is = findStreamInClasspathOrFileSystem(path);
	    BOMInputStream bis = new BOMInputStream(is, false);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(bis);
            // TODO - might have backslashes.
            doc.setDocumentURI(path);
            return doc;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads a Conversation document in JSON format as specified by IHMC.
     *
     * @param path       the path
     * @param utterances the utterances
     * @param speakers   the speakers
     * @param title      the title
     * @return the string
     */
    public String readConversationFile(String path, List<String> utterances, List<String> speakers,
                                       String title) {
        try {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(new FileReader(path));
            JSONObject jsonObject = (JSONObject) obj;

            JSONObject conversation = (JSONObject) jsonObject.get("conversation");
            title = (String) conversation.get("name");
            //System.out.println("Title is: " + title);

            StringBuffer conversationText = new StringBuffer();
            JSONArray utt = (JSONArray) conversation.get("utterances");
            if (utt != null) {
                for (Object o : utt) {
                    JSONObject utterance = (JSONObject) o;
                    speakers.add((String) utterance.get("name"));

                    String uttText = (String) utterance.get("utterance");
                    utterances.add(uttText);
                    conversationText.append(uttText + "\n");
                }
            }

            return conversationText.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Reads specified ERE file into an EREDocument (Document object wrapper).
     *
     * @param path  Path to ere file to read.
     * @param docId the doc id
     * @return an EREDocument with fields derived from file at path.
     */
    public EREDocument readEREFile(String path, String docId, String language) {
        String filestring = readFileIntoString(path);
        HltContentContainer hltcc = new HltContentContainer();
        EREDocument ereDoc = new EREDocument(filestring, hltcc);
        ereDoc.createCharMapping();
        ereDoc.createDocument(docId, language, path);
        return ereDoc;
    }


    /**
     * Gets the coreferences.
     *
     * @param ereDoc the ere doc
     * @param xmlDoc the xml doc
     * @param emList the em list
     * @return the coreferences
     */
    private List<Coreference> getCoreferences(EREDocument ereDoc, org.w3c.dom.Document xmlDoc, List<EntityMention> emList) {
        List<Coreference> coreferenceList = new ArrayList<Coreference>();
        List<Entity> entitiesList = new ArrayList<Entity>();
        List<EntityMention> resolvedEntityMentions = emList;

        String coreferenceIdString = xmlDoc.getElementsByTagName("deft_ere").item(0).getAttributes().getNamedItem("docid").getNodeValue();
        long coreferenceId = 0;
        try //this'll work for ids like 'fbis_eng_20010901.0003. If it fails, we have guid, so handle that instead
        {
            coreferenceId = Long.parseLong(coreferenceIdString.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            coreferenceId = Long.parseLong(coreferenceIdString.replaceAll("[^\\d]", "").substring(0, 10));//TODO
            //            coreferenceId = UUID.fromString(coreferenceIdString).getLeastSignificantBits();
        }

        Coreference cr = new Coreference(coreferenceId);

        NodeList entities = xmlDoc.getElementsByTagName("entity");
        for (int x = 0; x < entities.getLength(); x++) {
            String entityIdString = entities.item(x).getAttributes().getNamedItem("id").getNodeValue();
            long id = Long.parseLong(entityIdString.replaceAll("ent-", ""));
            EntityMention canonicalMention = ereDoc.getCanonicalEntityMentionById(id);
            String entityType = entities.item(x).getAttributes().getNamedItem("type").getNodeValue();
            Entity e = new Entity(id, new Type(entityType));
            if (canonicalMention != null)
                e.setCanonicalMentions(canonicalMention);
            else //if this entity does not figure into any relation
            {
                String emString = ((Element) entities.item(x)).getElementsByTagName("entity_mention").item(0).getAttributes().getNamedItem("id").getNodeValue();
                long emId = Long.parseLong(emString.replaceAll("m-", ""));
                //TODO because of issue in Discussion Forum data, added this try catch
                try {
                    e.setCanonicalMentions(ereDoc.getEntityMentionById(emId));
                } catch (NullPointerException ex) {
                    System.out.println("can't add: " + emId);
                }
            }
            entitiesList.add(e);
        }

        cr.setEntities(entitiesList);
        cr.setResolvedMentions(resolvedEntityMentions);
        coreferenceList.add(cr);
        return coreferenceList.isEmpty() ? null : coreferenceList;
    }


    /**
     * Gets the coreferences.
     *
     * @param adeptDoc the ere doc
     * @param xmlDoc   the xml doc
     * @param emList   the em list
     * @return the coreferences
     */
    private List<Coreference> getCoreferences(Document adeptDoc, org.w3c.dom.Document xmlDoc, List<EntityMention> emList) {
        List<Coreference> coreferenceList = new ArrayList<Coreference>();
        List<Entity> entitiesList = new ArrayList<Entity>();
        List<EntityMention> resolvedEntityMentions = emList;
        Map<Long, EntityMention> mentionsById = new HashMap<Long, EntityMention>();
        for (EntityMention mention : emList) {
            mentionsById.put(mention.getSequenceId(), mention);
        }

        String coreferenceIdString = xmlDoc.getElementsByTagName("deft_ere").item(0).getAttributes().getNamedItem("docid").getNodeValue();
        long coreferenceId = Long.parseLong(coreferenceIdString.replaceAll("[^\\d]", ""));
        Coreference cr = new Coreference(coreferenceId);

        NodeList entities = xmlDoc.getElementsByTagName("entity");
        for (int x = 0; x < entities.getLength(); x++) {
            String entityIdString = entities.item(x).getAttributes().getNamedItem("id").getNodeValue();
            long id = Long.parseLong(entityIdString.replaceAll("ent-", ""));
            String entityType = entities.item(x).getAttributes().getNamedItem("type").getNodeValue();
            Entity e = new Entity(id, new Type(entityType));
            String emString = ((Element) entities.item(x)).getElementsByTagName("entity_mention").item(0).getAttributes().getNamedItem("id").getNodeValue();
            long emId = Long.parseLong(emString.replaceAll("m-", ""));
            e.setCanonicalMentions(mentionsById.get(emId));
            entitiesList.add(e);
        }

        cr.setEntities(entitiesList);
        cr.setResolvedMentions(resolvedEntityMentions);
        coreferenceList.add(cr);
        return coreferenceList.isEmpty() ? null : coreferenceList;
    }

    /**
     * Gets the relations.
     *
     * @param ereDoc the ere doc
     * @param xmlDoc the xml doc
     * @return the relations
     */
    private List<Relation> getRelations(EREDocument ereDoc, org.w3c.dom.Document xmlDoc) {
        List<Relation> relationList = new ArrayList<Relation>();
        long relationId = 0;

        NodeList relation_mentions = xmlDoc.getElementsByTagName("relation_mention");
        for (int x = 0; x < relation_mentions.getLength(); x++) {
            String relationType = relation_mentions.item(x).getParentNode().getAttributes().getNamedItem("type").getNodeValue();
            String relationSubtype = relation_mentions.item(x).getParentNode().getAttributes().getNamedItem("subtype").getNodeValue();
            float confidence = 1;

            Relation relation = new Relation(relationId++, new Type(relationType + ":" + relationSubtype));
            relation.setConfidence(confidence);

            NodeList arguments = ((Element) relation_mentions.item(x)).getElementsByTagName("arg");
            boolean redundant = false; //check for relations between mentions of same entity
            long prevEntityId = -1;
            for (int y = 0; y < arguments.getLength(); y++) {
                String argumentType = arguments.item(y).getAttributes().getNamedItem("type").getNodeValue();
                int distributionSize = 1;
                Argument argument = new Argument(new Type(argumentType), distributionSize);
                String emIdString = arguments.item(y).getAttributes().getNamedItem("entity_mention_id").getNodeValue();
                long emId = Long.parseLong(emIdString.replaceAll("m-", ""));
                String entityIdString = arguments.item(y).getAttributes().getNamedItem("entity_id").getNodeValue();
                long entityId = Long.parseLong(entityIdString.replaceAll("ent-", ""));
                if (entityId == prevEntityId) {
                    redundant = true;
                    break;
                }
                //sets entity mention id of this argument to that of canonical if it exists
                //otherwise, sets canonical mention to this mention
                //emId = ereDoc.putCanonicalEntityMentionById(entityId,emId).getSequenceId();
                Chunk distribution = ereDoc.getEntityMentionById(emId);
                Pair<Chunk, Float> argumentDistribution = new Pair<Chunk, Float>(distribution, 1F);
                argument.addArgumentConfidencePair(distribution, confidence);
                relation.addArgument(argument);
                prevEntityId = entityId;
            }
            if (!redundant)
                relationList.add(relation);
        }
        return relationList.isEmpty() ? null : relationList;
    }

    /**
     * Gets the relations.
     *
     * @param adeptDoc the adept doc
     * @param xmlDoc   the xml doc
     * @return the relations
     */
    private List<Relation> getRelations(Document adeptDoc, org.w3c.dom.Document xmlDoc, List<EntityMention> entityMentions) {
        List<Relation> relationList = new ArrayList<Relation>();
        long relationId = 0;

        NodeList relation_mentions = xmlDoc.getElementsByTagName("relation_mention");
        for (int x = 0; x < relation_mentions.getLength(); x++) {
            String relationType = relation_mentions.item(x).getParentNode().getAttributes().getNamedItem("type").getNodeValue();
            String relationSubtype = relation_mentions.item(x).getParentNode().getAttributes().getNamedItem("subtype").getNodeValue();
            float confidence = 1;

            Relation relation = new Relation(relationId++, new Type(relationType + ":" + relationSubtype));
            relation.setConfidence(confidence);

            NodeList arguments = ((Element) relation_mentions.item(x)).getElementsByTagName("arg");
            boolean redundant = false; //check for relations between mentions of same entity
            long prevEntityId = -1;
            for (int y = 0; y < arguments.getLength(); y++) {
                String argumentType = arguments.item(y).getAttributes().getNamedItem("type").getNodeValue();
                int distributionSize = 1;
                Argument argument = new Argument(new Type(argumentType), distributionSize);
                String emIdString = arguments.item(y).getAttributes().getNamedItem("entity_mention_id").getNodeValue();
                long emId = Long.parseLong(emIdString.replaceAll("m-", ""));
                String entityIdString = arguments.item(y).getAttributes().getNamedItem("entity_id").getNodeValue();
                long entityId = Long.parseLong(entityIdString.replaceAll("ent-", ""));
                if (entityId == prevEntityId) {
                    redundant = true;
                    break;
                }
                //sets entity mention id of this argument to that of canonical if it exists
                //otherwise, sets canonical mention to this mention
                //emId = ereDoc.putCanonicalEntityMentionById(entityId,emId).getSequenceId();

                Map<Long, EntityMention> mentionsById = new HashMap<Long, EntityMention>();
                for (EntityMention mention : entityMentions) {
                    mentionsById.put(mention.getSequenceId(), mention);
                }

                Chunk distribution = mentionsById.get(emId);
                Pair<Chunk, Float> argumentDistribution = new Pair<Chunk, Float>(distribution, 1F);
                argument.addArgumentConfidencePair(distribution, confidence);
                relation.addArgument(argument);
                prevEntityId = entityId;
            }
            if (!redundant)
                relationList.add(relation);
        }
        return relationList.isEmpty() ? null : relationList;
    }

    /**
     * Gets the doc id.
     *
     * @param xmlDoc the xml doc
     * @return the doc id
     */
    private String getDocId(org.w3c.dom.Document xmlDoc) {
        List<EntityMention> entityMentionList = new ArrayList<EntityMention>();

        NodeList deft_eres = xmlDoc.getElementsByTagName("deft_ere");
        for (int x = 0; x < deft_eres.getLength(); x++) {
            return deft_eres.item(x).getAttributes().getNamedItem("docid").getNodeValue();
        }
        return "none";
    }

    /**
     * Gets the entity mentions.
     *
     * @param ereDoc the ere doc
     * @param xmlDoc the xml doc
     * @return the entity mentions
     */
    private List<EntityMention> getEntityMentions(EREDocument ereDoc, org.w3c.dom.Document xmlDoc) {
        List<EntityMention> entityMentionList = new ArrayList<EntityMention>();

        NodeList entity_mentions = xmlDoc.getElementsByTagName("entity_mention");
        for (int x = 0; x < entity_mentions.getLength(); x++) {
            String entityType = entity_mentions.item(x).getParentNode().getAttributes().getNamedItem("type").getNodeValue();
            if (entityType.equals("NA"))
                entityType = "UNKNOWN";

            String entityIdString = entity_mentions.item(x).getParentNode().getAttributes().getNamedItem("id").getNodeValue();
            long entityId = Long.parseLong(entityIdString.replaceAll("ent-", ""));
            String mentionType = entity_mentions.item(x).getAttributes().getNamedItem("noun_type").getNodeValue();
            String idString = entity_mentions.item(x).getAttributes().getNamedItem("id").getNodeValue();
            long sequenceId = Long.parseLong(idString.replaceAll("m-", ""));

            long offset = Long.parseLong(entity_mentions.item(x).getAttributes().getNamedItem("offset").getNodeValue());
            long length = Long.parseLong(entity_mentions.item(x).getAttributes().getNamedItem("length").getNodeValue());

            long tokenStart = ereDoc.getTokenOffset(ereDoc.getCalculatedOffset(offset));
            long tokenEnd = ereDoc.getTokenOffset(ereDoc.getCalculatedOffset(offset + length - 1));

            TokenOffset to = new TokenOffset((int) tokenStart, (int) tokenEnd);

            if (tokenStart == -1 || tokenEnd == -1) {
                System.out.println((int) offset + " " + (int) (offset + length));
                System.out.println(offset + " Entity mention token range " + tokenStart + " to " + tokenEnd + " is out of bounds: " + ereDoc.getFullText().substring((int) offset, (int) (offset + length)));
                //TODO return null, don't continue
                continue;
                //                return null;
            }
            EntityMention em = new EntityMention(sequenceId, to, ereDoc.getDocument().getTokenStreamList().get(0));
            em.setMentionType(OntTypeFactory.getInstance().getType("MENTION_ERE", mentionType));
            em.setEntityType(new Type(entityType));
            HashMap<Long, Float> entityIdDistribution = new HashMap<Long, Float>();
            entityIdDistribution.put(entityId, 1F);
            em.setEntityIdDistribution(entityIdDistribution);
            ereDoc.getEntityMentionsById().put(sequenceId, em);

            entityMentionList.add(em);
        }
        return entityMentionList.isEmpty() ? null : entityMentionList;
    }

    /**
     * Gets the entity mentions.
     *
     * @param adeptDoc the ere doc
     * @param xmlDoc   the xml doc
     * @return the entity mentions
     */
    private List<EntityMention> getEntityMentions(Document adeptDoc, org.w3c.dom.Document xmlDoc) {
        List<EntityMention> entityMentionList = new ArrayList<EntityMention>();

        NodeList entity_mentions = xmlDoc.getElementsByTagName("entity_mention");

        TokenStream tokenStream = adeptDoc.getDefaultTokenStream();

        for (int x = 0; x < entity_mentions.getLength(); x++) {
            String entityType = entity_mentions.item(x).getParentNode().getAttributes().getNamedItem("type").getNodeValue();
            if (entityType.equals("NA"))
                entityType = "UNKNOWN";

            String entityIdString = entity_mentions.item(x).getParentNode().getAttributes().getNamedItem("id").getNodeValue();
            long entityId = Long.parseLong(entityIdString.replaceAll("ent-", ""));
            String mentionType = entity_mentions.item(x).getAttributes().getNamedItem("noun_type").getNodeValue();
            String idString = entity_mentions.item(x).getAttributes().getNamedItem("id").getNodeValue();
            long sequenceId = Long.parseLong(idString.replaceAll("m-", ""));

            long offset = Long.parseLong(entity_mentions.item(x).getAttributes().getNamedItem("offset").getNodeValue());
            long length = Long.parseLong(entity_mentions.item(x).getAttributes().getNamedItem("length").getNodeValue());

            long tokenStart = -1;
            long tokenEnd = 10000000;
            boolean exactStart = false;
            boolean exactEnd = false;

            for (Token token : tokenStream) {
//		System.out.println("Testing token: " + token.getSequenceId() + token.getValue() + " " + token.getCharOffset().getBegin() + " " + token.getCharOffset().getEnd());
                if (!exactStart && token.getCharOffset().getBegin() == offset) {
                    tokenStart = token.getSequenceId();
                } else if (!exactStart && token.getCharOffset().getBegin() < offset && token.getCharOffset().getBegin() > tokenStart) {
                    tokenStart = token.getSequenceId();
                }

                if (!exactEnd && token.getCharOffset().getEnd() == offset + length) {
                    tokenEnd = token.getSequenceId();
                } else if (!exactEnd && token.getCharOffset().getEnd() > offset + length && token.getCharOffset().getEnd() < tokenEnd) {
                    tokenEnd = token.getSequenceId();
                }

            }

            long endOffset = offset + length - 1;

            TokenOffset to = new TokenOffset((int) tokenStart, (int) tokenEnd);

            if (tokenStart == -1 || tokenEnd == 10000000) {
                System.out.println("Token range " + tokenStart + " to " + tokenEnd + " is out of bounds.");
                return null;
            }
            EntityMention em = new EntityMention(sequenceId, to, tokenStream);
            em.setMentionType(OntTypeFactory.getInstance().getType("MENTION_ERE", mentionType));
            em.setEntityType(new Type(entityType));
            HashMap<Long, Float> entityIdDistribution = new HashMap<Long, Float>();
            entityIdDistribution.put(entityId, 1F);
            em.setEntityIdDistribution(entityIdDistribution);

            entityMentionList.add(em);
        }
        return entityMentionList.isEmpty() ? null : entityMentionList;
    }

    /**
     * Gets the named entities.
     *
     * @param entityMentions the entity mentions
     * @return the named entities
     */
    private List<EntityMention> getNamedEntities(List<EntityMention> entityMentions) {
        List<EntityMention> namedEntities = new ArrayList<EntityMention>();
        for (EntityMention em : entityMentions) {
            String mentionType = em.getMentionType().getType();
            String entityType = em.getEntityType().getType();
            if (mentionType.equals("NAM") && (entityType.equals("GPE") || entityType.equals("ORG")
                    || entityType.equals("PER")))
                namedEntities.add(em);
        }
        return namedEntities;
    }

    /**
     * Gets the event relations.
     *
     * @param ereDoc the ere doc
     * @return the event relations
     */
    private List<EventRelations> getEventRelations(EREDocument ereDoc) {
        List<EventRelations> eventRelations = new ArrayList<EventRelations>();
        for (long id : ereDoc.getEventsById().keySet()) {
            EventRelations er = new EventRelations();
            er.setCoreferences(ereDoc.getEventById(id));
            eventRelations.add(er);
        }
        return eventRelations.isEmpty() ? null : eventRelations;
    }

    /**
     * Gets the events.
     *
     * @param ereDoc the ere doc
     * @param xmlDoc the xml doc
     * @return the events
     */
    private List<Event> getEvents(EREDocument ereDoc, org.w3c.dom.Document xmlDoc) {
        List<Event> eventList = new ArrayList<Event>();

        NodeList evms = xmlDoc.getElementsByTagName("event_mention");
        for (int x = 0; x < evms.getLength(); x++) {
            String eventIdString = evms.item(x).getParentNode().getAttributes().getNamedItem("id").getNodeValue();
            String evmIdString = evms.item(x).getAttributes().getNamedItem("id").getNodeValue();
            long eventId = Long.parseLong(eventIdString.replaceAll("e-", ""));
            long evmId = Long.parseLong(evmIdString.replaceAll("evm-", ""));
            String evmType = evms.item(x).getAttributes().getNamedItem("type").getNodeValue();
            String evmSubtype = evms.item(x).getAttributes().getNamedItem("subtype").getNodeValue();

            //create new Event from this event_mention
            Event e = new Event(eventId, OntTypeFactory.getInstance().getType("EVENT_ERE", evmType + "." + evmSubtype));
            if (e.getType().contains("OTH"))
                System.out.println("OTHER:" + evmType + "." + evmSubtype);

            NodeList triggers = ((Element) evms.item(x)).getElementsByTagName("trigger");
            for (int y = 0; y < triggers.getLength(); y++) {
                String argType = "trigger";
                long offset = Long.parseLong(triggers.item(y).getAttributes().getNamedItem("offset").getNodeValue());
                long length = Long.parseLong(triggers.item(y).getAttributes().getNamedItem("length").getNodeValue());
                TokenOffset to = tokenOffsetFromOffset(offset, length, ereDoc);
                if (to == null)
                    return null;

                //add this as an argument, its type to e's attributes list
                Chunk trigger = new Chunk(to, ereDoc.getDocument().getTokenStreamList().get(0));
                Argument argument = new Argument(OntTypeFactory.getInstance().getType("EVENT_ERE", argType), 1);
                argument.addArgumentConfidencePair(trigger, 1.0F);
                e.addArgument(argument);
            }

            NodeList args = ((Element) evms.item(x)).getElementsByTagName("arg");
            for (int y = 0; y < args.getLength(); y++) {
                String argType = args.item(y).getAttributes().getNamedItem("type").getNodeValue();
                String emIdString = args.item(y).getAttributes().getNamedItem("entity_mention_id").getNodeValue();
                long emId = Long.parseLong(emIdString.replaceAll("m-", ""));
                //add this as an argument, its type to e's attributes list
                EntityMention em = ereDoc.getEntityMentionById(emId);
                Argument argument = new Argument(new Type(argType), 1);
                argument.addArgumentConfidencePair(em, 1.0F);
                e.addArgument(argument);
            }

            NodeList places = ((Element) evms.item(x)).getElementsByTagName("place");
            for (int y = 0; y < places.getLength(); y++) {
                String argType = "place";
                String emIdString = places.item(y).getAttributes().getNamedItem("entity_mention_id").getNodeValue();
                long emId = Long.parseLong(emIdString.replaceAll("m-", ""));
                //add this as an argument, its type to e's attributes list
                EntityMention em = ereDoc.getEntityMentionById(emId);
                Argument argument = new Argument(OntTypeFactory.getInstance().getType("EVENT_ERE", argType), 1);
                argument.addArgumentConfidencePair(em, 1.0F);
                e.addArgument(argument);
            }
            NodeList dates = ((Element) evms.item(x)).getElementsByTagName("date");
            for (int y = 0; y < dates.getLength(); y++) {
                String argType = "date";
                NodeList date_extents = ((Element) dates.item(y)).getElementsByTagName("date_extent");
                for (int z = 0; z < date_extents.getLength(); z++) {
                    long offset = 0;
                    long length = 0;
                    try {
                        offset = Long.parseLong(date_extents.item(z).getAttributes().getNamedItem("offset").getNodeValue());
                        length = Long.parseLong(date_extents.item(z).getAttributes().getNamedItem("length").getNodeValue());
                    } catch (NumberFormatException nfe) {
                        continue;
                    }
                    TokenOffset to = tokenOffsetFromOffset(offset, length, ereDoc);
                    if (to == null)
                        return null;

                    //add this as an argument, its type to e's attributes list
                    Chunk date = new Chunk(to, ereDoc.getDocument().getTokenStreamList().get(0));
                    Argument argument = new Argument(OntTypeFactory.getInstance().getType("EVENT_ERE", argType), 1);
                    argument.addArgumentConfidencePair(date, 1.0F);
                    e.addArgument(argument);
                }
            }
            ereDoc.setEventById(eventId, e);
            eventList.add(e);
        }

        return eventList.isEmpty() ? null : eventList;
    }

    /**
     * Gets the events.
     *
     * @param adeptDoc the adept doc
     * @param xmlDoc   the xml doc
     * @return the events
     */
    private List<Event> getEvents(Document adeptDoc, org.w3c.dom.Document xmlDoc, List<EntityMention> emList) {
        List<Event> eventList = new ArrayList<Event>();

        Map<Long, EntityMention> mentionsById = new HashMap<Long, EntityMention>();
        for (EntityMention mention : emList) {
            mentionsById.put(mention.getSequenceId(), mention);
        }

        NodeList evms = xmlDoc.getElementsByTagName("event_mention");
        for (int x = 0; x < evms.getLength(); x++) {
            String eventIdString = evms.item(x).getParentNode().getAttributes().getNamedItem("id").getNodeValue();
            String evmIdString = evms.item(x).getAttributes().getNamedItem("id").getNodeValue();
            long eventId = Long.parseLong(eventIdString.replaceAll("e-", ""));
            long evmId = Long.parseLong(evmIdString.replaceAll("evm-", ""));
            String evmType = evms.item(x).getAttributes().getNamedItem("type").getNodeValue();
            String evmSubtype = evms.item(x).getAttributes().getNamedItem("subtype").getNodeValue();

            //create new Event from this event_mention
            Event e = new Event(eventId, OntTypeFactory.getInstance().getType("EVENT_ERE", evmType + "." + evmSubtype));
            if (e.getType().contains("OTH"))
                System.out.println("OTHER:" + evmType + "." + evmSubtype);

            NodeList triggers = ((Element) evms.item(x)).getElementsByTagName("trigger");
            for (int y = 0; y < triggers.getLength(); y++) {
                String argType = "trigger";
                long offset = Long.parseLong(triggers.item(y).getAttributes().getNamedItem("offset").getNodeValue());
                long length = Long.parseLong(triggers.item(y).getAttributes().getNamedItem("length").getNodeValue());
                TokenOffset to = tokenOffsetFromOffset(offset, length, adeptDoc);
                if (to == null)
                    return null;

                //add this as an argument, its type to e's attributes list
                Chunk trigger = new Chunk(to, adeptDoc.getDefaultTokenStream());
                Argument argument = new Argument(OntTypeFactory.getInstance().getType("EVENT_ERE", argType), 1);
                argument.addArgumentConfidencePair(trigger, 1.0F);
                e.addArgument(argument);
            }

            NodeList args = ((Element) evms.item(x)).getElementsByTagName("arg");
            for (int y = 0; y < args.getLength(); y++) {
                String argType = args.item(y).getAttributes().getNamedItem("type").getNodeValue();
                String emIdString = args.item(y).getAttributes().getNamedItem("entity_mention_id").getNodeValue();
                long emId = Long.parseLong(emIdString.replaceAll("m-", ""));
                //add this as an argument, its type to e's attributes list
                EntityMention em = mentionsById.get(emId);
                Argument argument = new Argument(new Type(argType), 1);
                argument.addArgumentConfidencePair(em, 1.0F);
                e.addArgument(argument);
            }

            NodeList places = ((Element) evms.item(x)).getElementsByTagName("place");
            for (int y = 0; y < places.getLength(); y++) {
                String argType = "place";
                String emIdString = places.item(y).getAttributes().getNamedItem("entity_mention_id").getNodeValue();
                long emId = Long.parseLong(emIdString.replaceAll("m-", ""));
                //add this as an argument, its type to e's attributes list
                EntityMention em = mentionsById.get(emId);
                Argument argument = new Argument(OntTypeFactory.getInstance().getType("EVENT_ERE", argType), 1);
                argument.addArgumentConfidencePair(em, 1.0F);
                e.addArgument(argument);
            }
            NodeList dates = ((Element) evms.item(x)).getElementsByTagName("date");
            for (int y = 0; y < dates.getLength(); y++) {
                String argType = "date";
                NodeList date_extents = ((Element) dates.item(y)).getElementsByTagName("date_extent");
                for (int z = 0; z < date_extents.getLength(); z++) {
                    long offset = 0;
                    long length = 0;
                    try {
                        offset = Long.parseLong(date_extents.item(z).getAttributes().getNamedItem("offset").getNodeValue());
                        length = Long.parseLong(date_extents.item(z).getAttributes().getNamedItem("length").getNodeValue());
                    } catch (NumberFormatException nfe) {
                        continue;
                    }
                    TokenOffset to = tokenOffsetFromOffset(offset, length, adeptDoc);
                    if (to == null)
                        return null;

                    //add this as an argument, its type to e's attributes list
                    Chunk date = new Chunk(to, adeptDoc.getDefaultTokenStream());
                    Argument argument = new Argument(OntTypeFactory.getInstance().getType("EVENT_ERE", argType), 1);
                    argument.addArgumentConfidencePair(date, 1.0F);
                    e.addArgument(argument);
                }
            }
            eventList.add(e);
        }

        return eventList.isEmpty() ? null : eventList;
    }

    /**
     * Token offset from offset.
     *
     * @param offset the offset
     * @param length the length
     * @param ereDoc the ere doc
     * @return the token offset
     */
    private TokenOffset tokenOffsetFromOffset(long offset, long length, EREDocument ereDoc) {
        long tokenStart = ereDoc.getTokenOffset(ereDoc.getCalculatedOffset(offset));
        long tokenEnd = ereDoc.getTokenOffset(ereDoc.getCalculatedOffset(offset + length - 1));

        TokenOffset to = new TokenOffset((int) tokenStart, (int) tokenEnd);

        if (tokenStart == -1 || tokenEnd == -1) {
            System.out.println(ereDoc.getFullText().substring((int) offset, (int) (offset + length)));
            System.out.println("Token range " + tokenStart + " to " + tokenEnd + " is out of bounds.");
            return null;
        }
        return to;
    }

    /**
     * Token offset from offset.
     *
     * @param offset   the offset
     * @param length   the length
     * @param adeptDoc the adept doc
     * @return the token offset
     */
    private TokenOffset tokenOffsetFromOffset(long offset, long length, Document adeptDoc) {

        TokenStream tokenStream = adeptDoc.getDefaultTokenStream();

        long tokenStart = -1;
        long tokenEnd = 10000000;
        boolean exactStart = false;
        boolean exactEnd = false;

        for (Token token : tokenStream) {
            if (!exactStart && token.getCharOffset().getBegin() == offset) {
                tokenStart = token.getSequenceId();
            } else if (!exactStart && token.getCharOffset().getBegin() < offset && token.getCharOffset().getBegin() > tokenStart) {
                tokenStart = token.getSequenceId();
            }

            if (!exactEnd && token.getCharOffset().getEnd() == offset + length - 1) {
                tokenEnd = token.getSequenceId();
            } else if (!exactEnd && token.getCharOffset().getEnd() > offset + length - 1 && token.getCharOffset().getEnd() < tokenEnd) {
                tokenEnd = token.getSequenceId();
            }

        }
        TokenOffset to = new TokenOffset((int) tokenStart, (int) tokenEnd);

        if (tokenStart == -1 || tokenEnd == -1) {
            System.out.println("Token range " + tokenStart + " to " + tokenEnd + " is out of bounds.");
            return null;
        }
        return to;
    }

    /**
     * Creates HltContentContainer corresponding to ERE transcription and annotation file.
     *
     * @param EREPath path to ERE transcription file
     * @param XMLPath path to ERE annotation file or null
     * @return an HLTContentContainer
     */
    public HltContentContainer EREtoHltContentContainer(String EREPath, String XMLPath, String language) {
        org.w3c.dom.Document xmlDoc = readXML(XMLPath);
        String docId = getDocId(xmlDoc);
        EREDocument ereDoc = readEREFile(EREPath, docId, language);
        HltContentContainer hltcc = ereDoc.getHltContentContainer();
        if (xmlDoc != null) {
            List<EntityMention> entityMentions = getEntityMentions(ereDoc, xmlDoc);
            if (entityMentions != null) {
                hltcc.setEntityMentions(entityMentions);
                hltcc.setNamedEntities(getNamedEntities(entityMentions));
                hltcc.setRelations(getRelations(ereDoc, xmlDoc));
                hltcc.setCoreferences(getCoreferences(ereDoc, xmlDoc, entityMentions));
                List<Event> eventList = getEvents(ereDoc, xmlDoc);
                hltcc.setEvents(eventList);
                hltcc.setEventRelations(getEventRelations(ereDoc));
            }
        } else System.out.println("XMLDoc is NULL");

        return hltcc;
    }

    /**
     * Gets the pO ss.
     *
     * @param conllDoc the conll doc
     * @return the pO ss
     */
    private List<PartOfSpeech> getPOSs(CoNLLDocument conllDoc) {
        List<PartOfSpeech> POSs = new ArrayList<PartOfSpeech>();
        TokenStream ts = conllDoc.getDocument().getTokenStreamList().get(0);
        for (int x = 0; x < ts.size(); x++) {
            PartOfSpeech POS = new PartOfSpeech((long) x, new TokenOffset(x, x), ts);
            POS.setPosTag(new Type(conllDoc.getPOSByToken(ts.get(x))));
            POSs.add(POS);
        }
        return POSs;
    }

    /**
     * Gets the entities.
     *
     * @param conllDoc the conll doc
     * @return the entities
     */
    private HashMap<Integer, Entity> getEntities(CoNLLDocument conllDoc) {
        List<List<Pair<String, Long>>> entList = conllDoc.getNamedEntities();
        TokenStream ts = conllDoc.getDocument().getTokenStreamList().get(0);
        HashMap<Integer, Entity> entityLocations = new HashMap<Integer, Entity>();
        int runningSum = 0;
        for (int x = 0; x < entList.size(); x++) {
            int entityStart = 0;
            int entityEnd = 0;
            long entityId = (long) entList.get(x).get(entityEnd).getR();
            ;
            while (entityEnd <= entList.get(x).size()) {
                if (entityId >= 0) {
                    if (entityEnd == entList.get(x).size()
                            || (long) entList.get(x).get(entityEnd).getR() != entityId) {
                        EntityMention em = new EntityMention(entityId, new TokenOffset(runningSum + entityStart, runningSum + entityEnd - 1), ts);
                        Map<Long, Float> distribution = new HashMap<Long, Float>();
                        distribution.put(entityId, 1F);
                        em.setMentionType(new Type(entList.get(x).get(entityStart).getL().substring(0, 3)));
                        em.setEntityType(new Type("NAM"));
                        Entity e = new Entity(entityId, new Type("NAM"));
                        e.setCanonicalMentions(em);
                        for (int y = entityStart; y < entityEnd; y++)
                            entityLocations.put(runningSum + y, e);
                    }
                }
                if (entityEnd == entList.get(x).size())
                    break;
                long prevId = entityId;
                entityId = (long) entList.get(x).get(entityEnd).getR();
                if (entityId != prevId)
                    entityStart = entityEnd;
                entityEnd++;
            }
            runningSum += entList.get(x).size();
        }
        return entityLocations;
    }

    /**
     * Gets the entity mentions.
     *
     * @param conllDocument the conll document
     * @param entities      the entities
     * @return the entity mentions
     */
    private List<EntityMention> getEntityMentions(CoNLLDocument conllDocument, List<Entity> entities) {
        List<EntityMention> entityMentions = new ArrayList<EntityMention>();
        for (Entity e : entities)
            entityMentions.add(e.getCanonicalMention());
        return entityMentions;
    }

    /**
     * Gets the coreferences.
     *
     * @param conllDocument  the conll document
     * @param entityMap      the entity map
     * @param entityMentions the entity mentions
     * @return the coreferences
     */
    private List<Coreference> getCoreferences(CoNLLDocument conllDocument, Map<Integer, Entity> entityMap, List<EntityMention> entityMentions) {
        Map<Long, Set<Entity>> corefEntityMap = new HashMap<Long, Set<Entity>>();
        List<List<Set<Long>>> corefIds = conllDocument.getCorefs();
        List<Coreference> corefs = new ArrayList<Coreference>();

        for (int x = 0; x < corefIds.size(); x++) {
            for (int y = 0; y < corefIds.get(x).size(); y++) {
                for (long corefId : corefIds.get(x).get(y)) {
                    if (corefEntityMap.get(corefId) == null) {
                        Set<Entity> entities = new HashSet<Entity>();
                        corefEntityMap.put(corefId, entities);
                    }
                    if (entityMap.get(x + y) != null)
                        corefEntityMap.get(corefId).add(entityMap.get(x + y));
                }
            }
        }

        for (long corefId : corefEntityMap.keySet()) {
            Coreference c = new Coreference(corefId);
            c.setResolvedMentions(entityMentions);
            c.setEntities(new ArrayList<Entity>(corefEntityMap.get(corefId)));
            corefs.add(c);
        }

        return corefs;
    }

    /**
     * Gets the sentences.
     *
     * @param conllDoc the conll doc
     * @return the sentences
     */
    public List<Sentence> getSentences(CoNLLDocument conllDoc) {
        List<List<String>> tokens = conllDoc.getTokens();
        List<Sentence> sentences = new ArrayList<Sentence>();

        int runningOffset = 0;
        for (int x = 0; x < tokens.size(); x++) {
            sentences.add(new Sentence((long) x, new TokenOffset(runningOffset, runningOffset + tokens.get(x).size() - 1), conllDoc.getDocument().getTokenStreamList().get(0)));
            runningOffset += tokens.get(x).size();
        }
        return sentences;
    }

    /**
     * Reads specified CONLL file into an CONLLDocument (Document object wrapper).
     *
     * @param path Path to conll file to read.
     * @return an CONLLDocument with fields derived from file at path.
     */
    public CoNLLDocument readCoNLLFile(String path) {
        String filestring = readFileIntoString(path);
        CoNLLDocument conllDoc = new CoNLLDocument(filestring);
        conllDoc.createDocument();
        return conllDoc;
    }

    /**
     * Creates HltContentContainer corresponding to CoNLL 2011 file.
     *
     * @param filepath the filepath
     * @return an HLTContentContainer
     */
    public HltContentContainer CoNLLtoHltContentContainer(String filepath) {
        CoNLLDocument conllDoc = readCoNLLFile(filepath);
        HltContentContainer hltcc = new HltContentContainer();

        HashMap<Integer, Entity> entityMap = getEntities(conllDoc);
        HashSet<Entity> entitySet = new HashSet<Entity>();
        for (Integer i : entityMap.keySet())
            entitySet.add(entityMap.get(i));
        List<Entity> entities = new ArrayList<Entity>(entitySet);
        List<EntityMention> entityMentions = getEntityMentions(conllDoc, entities);

        hltcc.setSentences(getSentences(conllDoc));
        hltcc.setPartOfSpeechs(getPOSs(conllDoc));
        hltcc.setEntityMentions(entityMentions);
        hltcc.setNamedEntities(entityMentions);
        hltcc.setCoreferences(getCoreferences(conllDoc, entityMap, entityMentions));
        return hltcc;
    }

    /**
     * Creates HltContentContainer corresponding to CoNLL 2011 file.
     *
     * @param filepath the filepath
     * @return an HLTContentContainer
     */
    public HltContentContainer LDCForumtoHltContentContainer(String filepath, String XMLPath, String language) {
    	try
    	{
    		org.w3c.dom.Document xmlDoc = readXML(XMLPath);

            // This HLTCC is discarded.
            HltContentContainer hltContentContainer = new HltContentContainer();
            Document document = DocumentMaker.getInstance().createDefaultDocument(filepath, hltContentContainer);
            String text = null;
            text = fileToString(filepath);
            document.setValue(text);
            List<PassageAttributes> passageAttributesList = new ArrayList<PassageAttributes>();
//    	Pair<List<Tag>,adept.common.Document> documentPairs = LDCCorpusReader.getInstance()
//    								.readCorpus(text, passageAttributesList, null,
//    													null,
//    													null);
            HltContentContainer hltcc = LDCCorpusReader.getInstance().readPosts(text, passageAttributesList, null,
                    null,
                    language);
            if (hltcc.getPassages() == null) {
                hltcc.setPassages(new ArrayList<Passage>());
            }
            if (hltcc.getSentences() == null) {
                hltcc.setSentences(new ArrayList<Sentence>());
            }


            if (xmlDoc != null) {
                List<EntityMention> entityMentions = getEntityMentions(document, xmlDoc);
                if (entityMentions != null) {
                    hltcc.setEntityMentions(entityMentions);
                    hltcc.setNamedEntities(getNamedEntities(entityMentions));
                    hltcc.setRelations(getRelations(document, xmlDoc, entityMentions));
                    hltcc.setCoreferences(getCoreferences(document, xmlDoc, entityMentions));
                    List<Event> eventList = getEvents(document, xmlDoc, entityMentions);
                    hltcc.setEvents(eventList);
//                    hltcc.setEventRelations(getEventRelations(document));
                }
            }
            return hltcc;
    	}
        catch(Exception e)
        {
        	e.printStackTrace();
        	return null;
        }
    }

    /**
     * Find stream in classpath or file system.
     *
     * @param name the name
     * @return the input stream
     * @throws FileNotFoundException the file not found exception
     */
    public static BOMInputStream findStreamInClasspathOrFileSystem(String name) throws FileNotFoundException {
        InputStream is = Reader.class.getClassLoader().getResourceAsStream(name);
	BOMInputStream bis = new BOMInputStream(is, false);
        if (is == null) {
            is = Reader.class.getClassLoader().getResourceAsStream(name.replaceAll("\\\\", "/"));
	    bis = new BOMInputStream(is, false);
            if (is == null) {
                File f = new File(name);
                if (!f.exists()) {
                    System.out.println("Warning - creating FileInputStream: " + f.getAbsolutePath());
                }
                is = new FileInputStream(name);
		bis = new BOMInputStream(is, false);
                //System.out.println("Reading InputStream: " + f.getAbsolutePath());
            } else {
                System.out.println("InputStream found in Class Loader after adjusting separators.");
            }
        } else {
            //System.out.println("InputStream found in Class Loader.");
        }
        return bis;
    }

    /**
     * Gets the absolute path from classpath or file system.
     *
     * @param name the name
     * @return the absolute path from classpath or file system
     * @throws FileNotFoundException the file not found exception
     */
    public static String getAbsolutePathFromClasspathOrFileSystem(String name) throws FileNotFoundException {
        if (name.startsWith("/")) return name;
        //
        // System.out.println("To find the absolute path of: " + name);
        URL url = null;
        try {
            url = Reader.class.getClassLoader().getResource(name);
            if (url != null) return url.toURI().getPath();
            else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path  Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException the uRI syntax exception
     * @throws IOException        Signals that an I/O exception has occurred.
     * @author Greg Briggs
     */
    public static String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {

        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            /* A file path: easy enough */
            return new File(dirURL.toURI()).list();
        }

        if (dirURL == null) {
	        /* 
	         * In case of a jar file, we can't actually find a directory.
	         * Have to assume the same jar as clazz.
	         */
            String me = clazz.getName().replace(".", "/") + ".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (dirURL.getProtocol().equals("jar")) {
	        /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
            while (entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    String entry = name.substring(path.length());
                    int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory name
                        entry = entry.substring(0, checkSubdir);
                    }
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL " + dirURL);
    }


    /**
     * Reads specified file into a string.
     *
     * @param path the path
     * @return the string
     */
    public String readFileIntoString(String path) {
        try {
            String absolutePath = path;
            //			String absolutePath = getAbsolutePathFromClasspathOrFileSystem(path);
            FileInputStream stream = new FileInputStream(new File(absolutePath));
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
				/* Instead of using default, pass in a decoder. */
                return Charset.forName("UTF-8").decode(bb).toString();
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Reads specified file into a byte array.
     *
     * @param path the path
     * @return the byte[]
     */
    public byte[] readFileIntoByteArray(String path) {
        try {
            RandomAccessFile f = new RandomAccessFile(path, "r");
            byte[] b = new byte[(int) f.length()];
            f.read(b);
            f.close();
            return b;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Read file into lines.
     *
     * @param filename the filename
     * @param lines    the lines
     * @return the string
     */
    public String readFileIntoLines(String filename, List<String> lines) {
        if (lines == null)
            lines = new ArrayList<String>();
        String line = "";
        StringBuffer sb = new StringBuffer();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(new File(filename)), false), "UTF-8"));
            while ((line = in.readLine()) != null) {
                if (!line.isEmpty()) {
                    String surrogatesRemoved = checkSurrogates(line);
                    lines.add(surrogatesRemoved);
                    sb.append(surrogatesRemoved);
                    sb.append("\n");
                }
            }
            in.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return sb.toString();

    }

    /**
     * File to lines.
     *
     * @param filename the filename
     * @return the list
     */
    public List<String> fileToLines(String filename) {
        List<String> lines = new LinkedList<String>();
        String line = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(new File(filename)), false), "UTF-8"));
            while ((line = in.readLine()) != null) {
                lines.add(checkSurrogates(line));
            }
            in.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return lines;
    }

    /**
     * Convert stream to string.
     *
     * @param is the is
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String convertStreamToString(java.io.InputStream is) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }

        reader.close();

        return out.toString();
    }

    /**
     * Removes surrogate pairs
     *
     * @param text
     * @return
     */
    public static String checkSurrogates(String text) {
        StringBuffer buffer = new StringBuffer();
        char[] chars = text.toCharArray();
        for (Character c : chars) {
            if (Character.isHighSurrogate(c) || Character.isLowSurrogate(c)) {
                System.out.println("WARNING -- invalid xml character " + c + " removed");
            } else {
                buffer.append(c);
            }
        }

        return buffer.toString();
    }

    /**
     * File to string.
     *
     * @param filename the filename
     * @return the list
     */
    private String fileToString(String filename) {
        String lines = "";
        String line = "";
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new BOMInputStream(new FileInputStream(new File(filename)), false), "UTF-8"));
            while ((line = in.readLine()) != null) {
                lines = lines + line + "\n";
            }
            in.close();
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return lines;
    }

}


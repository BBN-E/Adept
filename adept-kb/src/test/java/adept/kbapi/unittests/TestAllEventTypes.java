package adept.kbapi.unittests;

/*-
 * #%L
 * adept-kb
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


import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import adept.common.ChannelName;
import adept.common.CharOffset;
import adept.common.Chunk;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.DocumentEvent;
import adept.common.DocumentEventArgument;
import adept.common.DocumentRelation;
import adept.common.DocumentRelationArgument;
import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.EventMention;
import adept.common.EventMentionArgument;
import adept.common.EventText;
import adept.common.GenericThing;
import adept.common.HltContentContainer;
import adept.common.Item;
import adept.common.OntType;
import adept.common.RelationMention;
import adept.common.TemporalResolution;
import adept.common.TimePhrase;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.Type;
import adept.common.XSDDate;
import adept.io.Reader;
import adept.kbapi.KBDate;
import adept.kbapi.KBEntity;
import adept.kbapi.KBEvent;
import adept.kbapi.KBGenericThing;
import adept.kbapi.KBOntologyMap;
import adept.kbapi.KBOntologyModel;
import adept.kbapi.KBPredicateArgument;
import adept.kbapi.KBQueryException;
import adept.kbapi.KBUpdateException;
import adept.metadata.SourceAlgorithm;
import adept.utilities.DocumentMaker;

import com.google.common.base.Optional;

public class TestAllEventTypes extends KBUnitTest {
    private static final HashMap<String, String> rereEntityReverseMap = new HashMap<String, String>();
    static {
        rereEntityReverseMap.put("Person","per");
        rereEntityReverseMap.put("GeoPoliticalEntity","gpe");
        rereEntityReverseMap.put("Organization","org");
        rereEntityReverseMap.put("Location","loc");
        rereEntityReverseMap.put("Facility","fac");
    }
    
    private static final HashMap<String, List<String>> rereEventTypesWithArguments = new HashMap<String, List<String>>();
    static {
        rereEventTypesWithArguments.put("startorg",Arrays.asList("agent","org","place","time"));
        rereEventTypesWithArguments.put("endorg",Arrays.asList("org","place","time"));
        rereEventTypesWithArguments.put("mergeorg",Arrays.asList("org","time","place"));
        rereEventTypesWithArguments.put("declarebankruptcy",Arrays.asList("org","time","place"));        
        rereEventTypesWithArguments.put("attack",Arrays.asList("attacker","target","instrument","time","place"));
        rereEventTypesWithArguments.put("demonstrate",Arrays.asList("entity","time","place"));
        rereEventTypesWithArguments.put("contact",Arrays.asList("entity","time","place"));
        rereEventTypesWithArguments.put("meet",Arrays.asList("entity","time","place"));
        rereEventTypesWithArguments.put("correspondence",Arrays.asList("entity","time","place"));
        rereEventTypesWithArguments.put("broadcast",Arrays.asList("entity","audience","time","place"));        
        rereEventTypesWithArguments.put("artifact",Arrays.asList("agent","artifact","instrument","time","place"));        
        rereEventTypesWithArguments.put("beborn",Arrays.asList("person","time","place"));
        rereEventTypesWithArguments.put("marry",Arrays.asList("person","time","place"));
        rereEventTypesWithArguments.put("divorce",Arrays.asList("person","time","place"));
        rereEventTypesWithArguments.put("injure",Arrays.asList("agent","victim","instrument","time","place"));
        rereEventTypesWithArguments.put("die",Arrays.asList("agent","victim","instrument","time","place"));    
        rereEventTypesWithArguments.put("transportperson",Arrays.asList("agent","person","instrument","time","origin","destination"));
        rereEventTypesWithArguments.put("transportartifact",Arrays.asList("agent","artifact","instrument","time","origin","destination"));        
        rereEventTypesWithArguments.put("startposition",Arrays.asList("entity","person","position","time","place"));
        rereEventTypesWithArguments.put("endposition",Arrays.asList("entity","person","position","time","place"));
        rereEventTypesWithArguments.put("nominate",Arrays.asList("agent","person","position","time","place"));
        rereEventTypesWithArguments.put("elect",Arrays.asList("agent","person","position","time","place"));
        rereEventTypesWithArguments.put("elect",Arrays.asList("entity","person","position","time","place"));
        rereEventTypesWithArguments.put("transaction",Arrays.asList("giver","recipient","beneficiary","time","place"));
        rereEventTypesWithArguments.put("transferownership",Arrays.asList("thing","giver","recipient","beneficiary","time","place"));
        rereEventTypesWithArguments.put("transfermoney",Arrays.asList("money","giver","recipient","beneficiary","time","place"));        
        rereEventTypesWithArguments.put("arrestjail",Arrays.asList("person","crime","agent","time","place"));
        rereEventTypesWithArguments.put("releaseparole",Arrays.asList("person","crime","agent","time","place"));
        rereEventTypesWithArguments.put("trialhearing",Arrays.asList("prosecutor","adjudicator","defendant","crime","time","place"));
        rereEventTypesWithArguments.put("sentence",Arrays.asList("sentence","adjudicator","defendant","crime","time","place"));
        rereEventTypesWithArguments.put("fine",Arrays.asList("adjudicator","entity","money","crime","time","place"));
        rereEventTypesWithArguments.put("chargeindict",Arrays.asList("prosecutor","adjudicator","defendant","crime","time","place"));
        rereEventTypesWithArguments.put("sue",Arrays.asList("plaintiff","adjudicator","defendant","crime","time","place"));
        rereEventTypesWithArguments.put("extradite",Arrays.asList("agent","person","crime","time","origin","place"));
        rereEventTypesWithArguments.put("acquit",Arrays.asList("adjudicator","defendant","crime","time","place"));
        rereEventTypesWithArguments.put("convict",Arrays.asList("adjudicator","defendant","crime","time","place"));
        rereEventTypesWithArguments.put("appeal",Arrays.asList("prosecutor","adjudicator","defendant","crime","time","place"));
        rereEventTypesWithArguments.put("execute",Arrays.asList("agent","person","crime","time","place"));
        rereEventTypesWithArguments.put("pardon",Arrays.asList("adjudicator","defendant","crime","time","place"));
    }
    
    private static final HashMap<String, List<String>> tacRelationTypesWithArguments = new HashMap<String, List<String>>();
    static {
        tacRelationTypesWithArguments.put("per:date_of_birth",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:city_of_birth",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:stateorprovince_of_birth",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:country_of_birth",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:date_of_death",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:cause_of_death",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:city_of_death",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:stateorprovince_of_death",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:country_of_death",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:date_founded",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("org:date_dissolved",Arrays.asList("arg-1","arg-2"));
        tacRelationTypesWithArguments.put("per:charges",Arrays.asList("arg-1","arg-2"));
    }
    
    List<String> entityTypes = Arrays.asList("Entity", "Person", "GeoPoliticalEntity", "Organization", "Location", "Facility");
    
    @Test
    public void testAllRereEventTypes() throws Exception {   
        for (String eventType : rereEventTypesWithArguments.keySet()) {
            KBEvent relation = createTestEventWithArguments(eventType);
            confirmEventWithTypeExists(eventType, KBOntologyMap.getRichEREOntologyMap());
            kb.deleteKBObject(relation.getKBID());
        }
    }
    
    @Test
    public void testAllTacRelationTypes() throws Exception {   
        for (String relationType : tacRelationTypesWithArguments.keySet()) {
            KBEvent relation = createTestEventFromTacRelationWithArguments(relationType);
            confirmEventWithTypeExists(relationType, KBOntologyMap.getTACOntologyMap());
            kb.deleteKBObject(relation.getKBID());
        }
    }
    
    private void confirmEventWithTypeExists(String eventType, KBOntologyMap ontologyMap) throws KBQueryException, InvalidPropertiesFormatException, FileNotFoundException, IOException {
        OntType adeptType = (ontologyMap.getKBTypeForType(new Type(eventType))).get();
        
        List<KBEvent> events = kb.getEventsByType(adeptType);

        assertTrue("No events returned.", events.size() > 0);
        assertTrue("More than one event returned.", events.size() == 1);

        for (KBEvent relation : events) {
            if (!relation.getType().getType().equals(adeptType.getType())) {
                Assert.fail("Invalid event type returned from getEventsByType().");
            }
        }
    }
    
    private KBEvent createTestEventWithArguments(String eventType) {
        try {            
            float eventConfidence = 0.9f;
            float eventMentionConfidence = 0.8f;

            HashMap<Item, KBPredicateArgument> argumentMap = new HashMap<Item, KBPredicateArgument>();
            
            Type actualEventType = new Type(eventType);
            OntType adeptType = KBOntologyMap.getRichEREOntologyMap().getKBTypeForType(actualEventType).get();
            
            // create event mention
            EventMention.Builder eventMentionBuilder = EventMention.builder(actualEventType);            
            Chunk mentionTextChunk = new Chunk(testTokenOffset, testTokenStream);
            eventMentionBuilder.setScore(eventMentionConfidence);
            eventMentionBuilder.setProvenance(EventText.builder(actualEventType, mentionTextChunk).setScore(.23f).build());

            // create document event
            DocumentEvent.Builder documentEventBuilder = DocumentEvent.builder(actualEventType);            
            documentEventBuilder.setScore(eventConfidence);
            
            int entityId = 1;
            for (String rereArgument : rereEventTypesWithArguments.get(eventType)) {
                OntType kbArgument = KBOntologyMap.getRichEREOntologyMap().getKBTypeForType(new Type(eventType+"."+rereArgument)).get();
                String kbArgType = KBOntologyModel.instance().getRelationArgumentTypes().get(adeptType.getType()).get(kbArgument.getType());
                if (kbArgType.equals("Date")) {
                    HltContentContainer hltContentContainer = new HltContentContainer();
                    Document document = DocumentMaker.getInstance().createDocument("sample_date.txt",
                                    null, "Text", "sample_date_1.txt", "English",
                                    Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_date.txt"),
                                    hltContentContainer);
                    CharOffset testCharOff = new CharOffset(16, 24);
                    String testText = "9/1/2015";
                    Token testToken = new Token(0, testCharOff, testText);
                    TokenStream testDateTokenStream = new TokenStream(TokenizerType.WHITESPACE,
                                    TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
                    TokenOffset testDateTokenOffset = new TokenOffset(0, 0);
                    testDateTokenStream.add(testToken);

                    SourceAlgorithm testSourceAlgorithm = new SourceAlgorithm("UnitTestAlgorithm", "BBN");

                    XSDDate xsdDate = XSDDate.fromString("2015-09-01");

                    TimePhrase timePhrase = new TimePhrase(testDateTokenOffset, testDateTokenStream, null);

                    TemporalResolution xsdTemporalResolution = TemporalResolution.builder()
                                    .setResolvedTemporalValue(xsdDate).setConfidence(0.5f).setTimePhrase(timePhrase)
                                    .setSourceAlgorithm(testSourceAlgorithm).build();

                    Set<TemporalResolution> xsdTemporalResolutions = new HashSet<TemporalResolution>();
                    xsdTemporalResolutions.add(xsdTemporalResolution);
                    KBDate.InsertionBuilder xsdDateInsertionBuilder = KBDate.xsdDateInsertionBuilder(xsdDate,
                                    xsdTemporalResolutions);
                    KBDate kbXsdDate = xsdDateInsertionBuilder.insert(kb);
                    
                    argumentMap.put(xsdDate, kbXsdDate);
                                        
                    EventMentionArgument mentionArg = EventMentionArgument
				.builder(actualEventType, new Type(rereArgument), new Chunk(testTokenOffset, testTokenStream)).setScore(.30f).build();
                    eventMentionBuilder.addArgument(mentionArg);
                    
                    DocumentEventArgument.Builder argBuilder = DocumentEventArgument.builder(actualEventType,
				new Type(rereArgument), DocumentEventArgument.Filler.fromTemporalValue(xsdDate)).setScore(0.52f);
                    argBuilder.addProvenance(DocumentEventArgument.Provenance.fromStandaloneEventMentionArgument(mentionArg));
                    documentEventBuilder.addArgument(argBuilder.build());  
                } else if (entityTypes.contains(kbArgType)) {
                    String type = null;
                    if (kbArgType.equals("Entity")) {
                        type = "per";
                    } else {
                        type = rereEntityReverseMap.get(kbArgType);
                    }
                    Entity entityArgument = new Entity(entityId++, new Type(type));

                    List<EntityMention> entityMentions = new ArrayList<EntityMention>();
                    EntityMention mention = new EntityMention(0, testTokenOffset, testTokenStream);
                    mention.addEntityConfidencePair(entityArgument.getEntityId(), 0.9f);
                    mention.setMentionType(new Type("NAME"));
                    mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
                    entityMentions.add(mention);

                    entityArgument.setCanonicalMentions(mention);
                    entityArgument.setEntityConfidence(0.8f);
                    entityArgument.setCanonicalMentionConfidence(0.63);

                    KBEntity.InsertionBuilder insertionBuilder = KBEntity.entityInsertionBuilder(
                                    entityArgument, entityMentions,
                                    KBOntologyMap.getRichEREOntologyMap());
                    KBEntity kbEntity = insertionBuilder.insert(kb);

                    argumentMap.put(entityArgument, kbEntity);
                    
                    EventMentionArgument mentionArg = EventMentionArgument
                            .builder(actualEventType, new Type(rereArgument), new Chunk(testTokenOffset, testTokenStream))
                            .setScore(.32f).build();
                    eventMentionBuilder.addArgument(mentionArg);
                    
                    DocumentEventArgument.Builder argBuilder = DocumentEventArgument.builder(actualEventType,
                            new Type(rereArgument), DocumentEventArgument.Filler.fromEntity(entityArgument)).setScore(0.51f);
                    argBuilder.addProvenance(DocumentEventArgument.Provenance.fromStandaloneEventMentionArgument(mentionArg));
                    documentEventBuilder.addArgument(argBuilder.build());
                } else if (!kbArgType.equals("Number") && !kbArgType.equals("Date")) {
                    // Generic thing
                    Type type = null;
                    if (kbArgType.equals("Thing")) {
                        type = new Type("crime");
                    } else {
                        type = new Type(kbArgType);
                    }
                    
                    GenericThing thing = new GenericThing(type, kbArgument.getType());
                    KBGenericThing.InsertionBuilder thingBuilder = KBGenericThing
				.genericThingInsertionBuilder(thing, Collections.singletonList(new Chunk(testTokenOffset, testTokenStream)),
						KBOntologyMap.getRichEREOntologyMap());
                    KBGenericThing kbThing = thingBuilder.insert(kb);
                    
                    argumentMap.put(thing, kbThing);
                    
                    EventMentionArgument mentionArg = EventMentionArgument
				.builder(actualEventType, new Type(rereArgument), new Chunk(testTokenOffset, testTokenStream)).setScore(.30f).build();
                    eventMentionBuilder.addArgument(mentionArg);
                    
                    DocumentEventArgument.Builder argBuilder = DocumentEventArgument.builder(actualEventType,
				new Type(rereArgument), DocumentEventArgument.Filler.fromGenericThing(thing)).setScore(0.52f);
                    argBuilder.addProvenance(DocumentEventArgument.Provenance.fromStandaloneEventMentionArgument(mentionArg));
                    documentEventBuilder.addArgument(argBuilder.build());
                }
            }
            
            EventMention eventMention = eventMentionBuilder.build();
            
            documentEventBuilder.addProvenanceFromEventMention(eventMention);
            
            DocumentEvent documentEvent = documentEventBuilder.build();
            
            KBEvent.InsertionBuilder insertionBuilder = KBEvent.eventInsertionBuilder(documentEvent, argumentMap, KBOntologyMap.getRichEREOntologyMap());
            return insertionBuilder.insert(kb);
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail();
            return null;
        }
    }
    
    private KBEvent createTestEventFromTacRelationWithArguments(String relationType) throws KBUpdateException, InvalidPropertiesFormatException, FileNotFoundException, IOException, URISyntaxException {
            float relationConfidence = 0.9f;
            float relationMentionConfidence = 0.8f;

            HashMap<Item, KBPredicateArgument> argumentMap = new HashMap<Item, KBPredicateArgument>();
            
            Type actualEventType = new Type(relationType);
            OntType adeptType = KBOntologyMap.getTACOntologyMap().getKBTypeForType(actualEventType).get();
            
            // create relation mention
            RelationMention.Builder relationMentionBuilder = RelationMention.builder(actualEventType);
            relationMentionBuilder.setConfidence(relationMentionConfidence);
            relationMentionBuilder.addJustification(new Chunk(testTokenOffset, testTokenStream));

            // create document relation
            DocumentRelation.Builder documentRelationBuilder = DocumentRelation.builder(actualEventType);		
            documentRelationBuilder.setConfidence(relationConfidence);
            
            int entityId = 1;
            for (String rereArgument : tacRelationTypesWithArguments.get(relationType)) {
                OntType kbArgument = KBOntologyMap.getTACOntologyMap().getKBTypeForType(new Type(relationType+"."+rereArgument)).get();
                String kbArgType = KBOntologyModel.instance().getRelationArgumentTypes().get(adeptType.getType()).get(kbArgument.getType());
                if (kbArgType.equals("Date")) {
                    HltContentContainer hltContentContainer = new HltContentContainer();
                    Document document = DocumentMaker.getInstance().createDocument("sample_date.txt",
                                    null, "Text", "sample_date_1.txt", "English",
                                    Reader.getAbsolutePathFromClasspathOrFileSystem("adept/kbapi/sample_date.txt"),
                                    hltContentContainer);
                    CharOffset testCharOff = new CharOffset(16, 24);
                    String testText = "9/1/2015";
                    Token testToken = new Token(0, testCharOff, testText);
                    TokenStream testDateTokenStream = new TokenStream(TokenizerType.WHITESPACE,
                                    TranscriptType.SOURCE, "English", ChannelName.NONE, ContentType.TEXT, document);
                    TokenOffset testDateTokenOffset = new TokenOffset(0, 0);
                    testDateTokenStream.add(testToken);

                    SourceAlgorithm testSourceAlgorithm = new SourceAlgorithm("UnitTestAlgorithm", "BBN");

                    XSDDate xsdDate = XSDDate.fromString("2015-09-01");

                    TimePhrase timePhrase = new TimePhrase(testDateTokenOffset, testDateTokenStream, null);

                    TemporalResolution xsdTemporalResolution = TemporalResolution.builder()
                                    .setResolvedTemporalValue(xsdDate).setConfidence(0.5f).setTimePhrase(timePhrase)
                                    .setSourceAlgorithm(testSourceAlgorithm).build();

                    Set<TemporalResolution> xsdTemporalResolutions = new HashSet<TemporalResolution>();
                    xsdTemporalResolutions.add(xsdTemporalResolution);
                    KBDate.InsertionBuilder xsdDateInsertionBuilder = KBDate.xsdDateInsertionBuilder(xsdDate,
                                    xsdTemporalResolutions);
                    KBDate kbXsdDate = xsdDateInsertionBuilder.insert(kb);
                    
                    argumentMap.put(xsdDate, kbXsdDate);
                                        
                    RelationMention.Filler argFiller = RelationMention.Filler.fromTimePhrase(timePhrase, new Type(rereArgument), .30f);
                    relationMentionBuilder.addArgument(argFiller);
                    
                    DocumentRelationArgument.Builder argBuilder = DocumentRelationArgument.builder(new Type(rereArgument), 
                                DocumentRelationArgument.Filler.fromTemporalValue(xsdDate), 0.55f);
                    argBuilder.addProvenance(argFiller);
                    documentRelationBuilder.addArgument(argBuilder.build());
                } else if (entityTypes.contains(kbArgType)) {
                    String type = null;
                    if (kbArgType.equals("Entity")) {
                        type = "per";
                    } else {
                        type = rereEntityReverseMap.get(kbArgType);
                    }
                    Entity entityArgument = new Entity(entityId++, new Type(type));

                    List<EntityMention> entityMentions = new ArrayList<EntityMention>();
                    EntityMention mention = new EntityMention(0, testTokenOffset, testTokenStream);
                    mention.addEntityConfidencePair(entityArgument.getEntityId(), 0.9f);
                    mention.setMentionType(new Type("NAME"));
                    mention.setSourceAlgorithm(new SourceAlgorithm("Example", "BBN"));
                    entityMentions.add(mention);

                    entityArgument.setCanonicalMentions(mention);
                    entityArgument.setEntityConfidence(0.8f);
                    entityArgument.setCanonicalMentionConfidence(0.63);

                    KBEntity.InsertionBuilder insertionBuilder = KBEntity.entityInsertionBuilder(
                                    entityArgument, entityMentions,
                                    KBOntologyMap.getTACOntologyMap());
                    KBEntity kbEntity = insertionBuilder.insert(kb);

                    argumentMap.put(entityArgument, kbEntity);
                    
                    RelationMention.Filler argFiller = RelationMention.Filler.fromEntityMention(mention, new Type(rereArgument), .30f);
                    relationMentionBuilder.addArgument(argFiller);
                    
                    DocumentRelationArgument.Builder argBuilder = DocumentRelationArgument.builder(new Type(rereArgument), 
                                DocumentRelationArgument.Filler.fromEntity(entityArgument), 0.55f);
                    argBuilder.addProvenance(argFiller);
                    documentRelationBuilder.addArgument(argBuilder.build());
                } else if (!kbArgType.equals("Number") && !kbArgType.equals("Date")) {
                    // Generic thing
                    Type type = null;
                    if (kbArgType.equals("Thing")) {
                        type = new Type("crime");
                    } else {
                        type = new Type(kbArgType);
                    }
                    
                    GenericThing thing = new GenericThing(type, kbArgument.getType());
                    KBGenericThing.InsertionBuilder thingBuilder = KBGenericThing
				.genericThingInsertionBuilder(thing, Collections.singletonList(new Chunk(testTokenOffset, testTokenStream)),
						KBOntologyMap.getTACOntologyMap());
                    KBGenericThing kbThing = thingBuilder.insert(kb);
                    
                    argumentMap.put(thing, kbThing);
                    
                    RelationMention.Filler argFiller = RelationMention.Filler.fromGenericChunk(new Chunk(testTokenOffset, testTokenStream), new Type(rereArgument), 0.55f);
                        relationMentionBuilder.addArgument(argFiller);

                    DocumentRelationArgument.Builder argBuilder = DocumentRelationArgument.builder(new Type(rereArgument), 
                                DocumentRelationArgument.Filler.fromGenericThing(thing), 0.55f);
                        argBuilder.addProvenance(argFiller);
                        documentRelationBuilder.addArgument(argBuilder.build());
                }
            }
            
            List<RelationMention> provenances = new ArrayList<RelationMention>();
            provenances.add(relationMentionBuilder.build());

            documentRelationBuilder.addProvenances(provenances);

            DocumentRelation documentRelation = documentRelationBuilder.build();
            
            KBEvent.InsertionBuilder insertionBuilder = KBEvent.eventInsertionBuilder(documentRelation, argumentMap, KBOntologyMap.getTACOntologyMap());
            return insertionBuilder.insert(kb);
    }
}

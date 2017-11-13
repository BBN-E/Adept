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
import adept.metadata.SourceAlgorithm;
import adept.serialization.XMLStringSerializer;
import adept.utilities.DocumentMaker;
import com.google.common.base.Optional;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.TestCase;
import org.apache.commons.lang.SerializationUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TestHltccSerialization extends TestCase {
    private static Document document = null;
    
    /* (non-Javadoc)
     * @see junit.framework.TestCase#setUp()
     */
    @Before
    public void setUp() throws Exception {
        document = DocumentMaker.getInstance().createDocument("sample.txt",
                                                null, "Text", "sample.txt", "English",
                                                Reader.getAbsolutePathFromClasspathOrFileSystem("adept/test/sample.txt"),
                                                null);
    }

    /* (non-Javadoc)
     * @see junit.framework.TestCase#tearDown()
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test.
     */
    @Test
    public void test() throws UnsupportedEncodingException {
        TestHltccSerialization testHltccSerialization = new TestHltccSerialization();
        HltContentContainer hltcc = testHltccSerialization.createHltcc();
        byte[] hltccSerialized = SerializationUtils.serialize(hltcc);
        HltContentContainer deserializedHltcc = (HltContentContainer)SerializationUtils.deserialize(hltccSerialized);
        
        // Convert to xml strings, compare output
        XMLStringSerializer xmlStringSerializer = new XMLStringSerializer();
        String before = xmlStringSerializer.serializeToString(hltcc);
        String after = xmlStringSerializer.serializeToString(deserializedHltcc);
        System.out.println("HLTCC Successfully serializes? "+before.equals(after));
        
        Assert.assertEquals(before, after);
    }
        
    private HltContentContainer createHltcc() {
        HltContentContainer hltcc = new HltContentContainer();
        hltcc.setAnomalousTexts(createAnamolousTexts());
        hltcc.setAuthorshipTheories(createAuthorshipTheories());
        
        hltcc.setBeliefMentions(createBeliefMentions());
        hltcc.setCommittedBeliefs(createCommitedBeliefs());
        hltcc.setConversationElements(createConversationElements());
        hltcc.setCoreferences(createCoreferences());
        hltcc.setDeceptionTheories(createDeceptionTheories());
        hltcc.setDependencies(createDependencies());
        
        List<DocumentBelief> documentBeliefs = createDocumentBeliefs();
        hltcc.setDocumentBeliefs(documentBeliefs);
        hltcc.addBeliefToKBBeliefMap(documentBeliefs.get(0), new KBID("testObjectId", "testKbNamespace"), 0.9f);
        
        List<DocumentSentiment> documentSentiments = createDocumentSentiments();
        hltcc.setDocumentSentiments(documentSentiments);
        hltcc.addSentimentToKBSentimentMap(documentSentiments.get(0), new KBID("testObjectId", "testKbNamespace"), 1.0f);
        
        List<DocumentRelation> documentRelations = createDocumentRelations();
        hltcc.setDocumentRelations(documentRelations);
        hltcc.addRelationToKBRelationMap(documentRelations.get(0), new KBID("testObjectId", "testKbNamespace"), 0.75f);
        
        hltcc.addEntityToKBEntityMap(createDummyEntity(), new KBID("testObjectId", "testKbNamespace"), 0.43f);
        
        hltcc.setDocumentEventArguments(createDocumentEventArguments());
        hltcc.setDocumentEvents(createDocumentEvents());       
        hltcc.setEntailments(createEntailments());
        hltcc.setEntityMentions(Collections.singletonList(createDummyEntityMention()));
        hltcc.setEventMentionArguments(Collections.singletonList(createDummyEventMentionArgument()));
        hltcc.setEventMentions(Collections.singletonList(createDummyEventMention()));
        hltcc.setEventPhrase(createEventPhrases());
        hltcc.setEventRelations(createEventRelations());
        hltcc.setEventTextSets(createEventTextSets());
        hltcc.setEventTexts(createEventTexts());
        hltcc.setEvents(createEvents());
        hltcc.setInterPausalUnits(createInterPausalUnits());
        hltcc.setJointRelationCoreferences(createJointRelationCoreferences());
        hltcc.setNamedEntities(Collections.singletonList(createDummyEntityMention()));
        hltcc.setOpinions(createOpinions());
        hltcc.setParaphrases(createParaphrases());
        hltcc.setPartOfSpeechs(createPartsOfSpeech());
        hltcc.setPassages(createPassages());
        hltcc.setProsodicPhrases(createProsodicPhrases());
        hltcc.setRelationMentions(Collections.singletonList(createDummyRelationMention()));
        hltcc.setRelations(createRelations());
        hltcc.setSarcasms(createSarcasms());
        hltcc.setSentenceSimilarities(createSentenceSimilarities());
        hltcc.setSentences(Collections.singletonList(createSentence()));
        hltcc.setSentimentMentions(createSentimentMentions());
        hltcc.setSessions(createSessions());
        hltcc.setSyntacticChunks(createSyntacticChunks());
        hltcc.setTaggedChunks(createTaggedChunks());
        hltcc.setTemporalResolutions(createTemporalResolutions());
        hltcc.setTimePhrases(Collections.singletonList(createTimePhrase()));
        hltcc.setTriples(createTriples());
        hltcc.setUtterances(createUtterances());
        
        return hltcc;
    }
    
    private List<DocumentEvent> createDocumentEvents() {
        List<DocumentEvent> documentEvents = new ArrayList<DocumentEvent>();
        DocumentEvent.Builder deBuilder = DocumentEvent.builder(new Type("testType"));
        deBuilder.addArguments(createDocumentEventArguments());
        deBuilder.addAttribute(new Type("testAttr"), 0.34f);
        deBuilder.addProvenance(DocumentEvent.Provenance.fromEventMention(createDummyEventMention()));
        deBuilder.setScore(0.65f);
        
        documentEvents.add(deBuilder.build());        
        return documentEvents;
    }
    
    private List<DocumentEventArgument> createDocumentEventArguments() {
        List<DocumentEventArgument> documentEventArguments = new ArrayList<DocumentEventArgument>();
        DocumentEventArgument.Builder deaBuilder = 
                DocumentEventArgument.builder(new Type("testType"), new Type("testRole"), DocumentEventArgument.Filler.fromEntity(createDummyEntity()));
        deaBuilder.addAttribute(new Type("testAttr"), 0.5f);
        deaBuilder.setScore(0.9f);
        deaBuilder.addProvenance(DocumentEventArgument.Provenance.fromStandaloneEventMentionArgument(createDummyEventMentionArgument()));
        
        documentEventArguments.add(deaBuilder.build());
        return documentEventArguments;
    }
    
    private List<Utterance> createUtterances() {
        List<Utterance> utterances = new ArrayList<Utterance>();
        Utterance utterance = new Utterance(new TokenOffset(0, 1), document.getDefaultTokenStream(), 1, "testSpeaker", "testUtterance");
        utterance.setAnnotation("testAnnotation");
        utterances.add(utterance);
        return utterances;
    }
    
    private List<Triple> createTriples() {
        List<Triple> triples = new ArrayList<Triple>();
        triples.add(new Triple(createDummyEntity(), new Slot(1, "testValue"), new Value(1)));
        return triples;
    }
    
    private List<TemporalResolution> createTemporalResolutions() {
        List<TemporalResolution> temporalResolutions = new ArrayList<TemporalResolution>();
        
        TemporalResolution.Builder temporalResolution1Builder = TemporalResolution.builder();
        temporalResolution1Builder.setConfidence(0.8f);
        temporalResolution1Builder.setResolvedTemporalValue(XSDDate.fromString("2015-09-01"));
        temporalResolution1Builder.setSourceAlgorithm(new SourceAlgorithm("testAlgName", "testContributingSiteName"));              
        temporalResolution1Builder.setTimePhrase(createTimePhrase());
        
        TemporalResolution.Builder temporalResolution2Builder = TemporalResolution.builder();
        temporalResolution2Builder.setConfidence(0.8f);
        temporalResolution2Builder.setResolvedTemporalValue(TimexValue.fromString("2015-09-01"));
        temporalResolution2Builder.setSourceAlgorithm(new SourceAlgorithm("testAlgName", "testContributingSiteName"));      
        temporalResolution2Builder.setTimePhrase(createTimePhrase());
        
        temporalResolutions.add(temporalResolution1Builder.build());
        temporalResolutions.add(temporalResolution2Builder.build());
        temporalResolutions.add(temporalResolution1Builder.build());
        return temporalResolutions;
    }
    
    private TimePhrase createTimePhrase() {
        TimePhrase timePhrase = new TimePhrase(new TokenOffset(0, 1), document.getDefaultTokenStream(), new Type("testType"));
        timePhrase.setResolution("testResolution");
        timePhrase.setResolutionType(new Type("testResolutionType"));  
        return timePhrase;
    }
    
    private List<TaggedChunk> createTaggedChunks() {
        List<TaggedChunk> taggedChunks = new ArrayList<TaggedChunk>();
        TaggedChunk taggedChunk1 = new TaggedChunk(1, "testTag1", new TokenOffset(0, 1), document.getDefaultTokenStream());
        TaggedChunk taggedChunk2 = new TaggedChunk(2, "testTag2", new TokenOffset(2, 3), document.getDefaultTokenStream());
        taggedChunks.add(taggedChunk1);
        taggedChunks.add(taggedChunk2);        
        return taggedChunks;
    }
    
    private List<SyntacticChunk> createSyntacticChunks() {
        List<SyntacticChunk> syntacticChunks = new ArrayList<SyntacticChunk>();
        SyntacticChunk syntacticChunk1 = new SyntacticChunk(1, new Type("testScType1"), new TokenOffset(0, 1), document.getDefaultTokenStream());
        SyntacticChunk syntacticChunk2 = new SyntacticChunk(2, new Type("testScType2"), new TokenOffset(2, 3), document.getDefaultTokenStream());
        syntacticChunks.add(syntacticChunk1);
        syntacticChunks.add(syntacticChunk2);
        return syntacticChunks;
    }
    
    private List<Session> createSessions() {
        List<Session> sessions = new ArrayList<Session>();
        Session session1 = new Session(new TokenOffset(0, 1), document.getDefaultTokenStream(), 1, "testContentType1", Collections.singletonList(createSentence()));
        Session session2 = new Session(new TokenOffset(3, 4), document.getDefaultTokenStream(), 2, "testContentType2", Collections.singletonList(createSentence()));
        sessions.add(session1);
        sessions.add(session2);
        return sessions;
    }
    
    private Sentence createSentence() {
        Sentence sentence = new Sentence(1, new TokenOffset(0, 1), document.getDefaultTokenStream());
        sentence.setNoveltyConfidence(0.8f);
        sentence.setPunctuation("testPunctuation");
        sentence.setType(SentenceType.QUESTION);
        sentence.setUncertaintyConfidence(0.3f);
        
        MorphSentence.Builder morphSentenceBuilder = MorphSentence.builder();
        MorphTokenSequence.Builder morphTokenSequenceBuilder = MorphTokenSequence.builder(new SourceAlgorithm("test", "test"));
        morphTokenSequenceBuilder.setConfidence(0.8f);
        
        MorphToken.Builder morphTokenBuilder = MorphToken.builder("testText", document.getDefaultTokenStream());
        morphTokenBuilder.setConfidence(0.5f);
        morphTokenBuilder.setHeadToken(new TokenOffset(0, 0));
        morphTokenBuilder.setHeadTokenAndTokenOffsets(new TokenOffset(1, 1));
        morphTokenBuilder.setLemma("testLemma");
        morphTokenBuilder.setNotes("testNotes");
        morphTokenBuilder.setPos("testPos");
        morphTokenBuilder.setRoots(Collections.singletonList("testRoot"));
        morphTokenBuilder.setTokenOffsets(Collections.singletonList(new TokenOffset(0, 1)));
        
        morphTokenBuilder.setFeatures(Collections.singletonList(MorphFeature.create("testProperty", "testValue")));
        
        Morph.Builder morphBuilder = Morph.builder("testForm", MorphType.create("testType"));
        morphBuilder.setFeatures(Collections.singletonList(MorphFeature.create("testProperty", "testValue")));
        morphBuilder.setSingleSourceOffset(new CharOffset(0, 1));
        morphBuilder.setSourceOffsets(Collections.singletonList(new CharOffset(0, 1)));
        morphTokenBuilder.setMorphs(Collections.singletonList(morphBuilder.build()));
        
        morphTokenSequenceBuilder.addToken(morphTokenBuilder.build());
        morphSentenceBuilder.addSequence(morphTokenSequenceBuilder.build());
        sentence.setMorphSentence(morphSentenceBuilder.build());
                
        return sentence;
    }
    
    private List<SentenceSimilarity> createSentenceSimilarities() {
        List<SentenceSimilarity> sentenceSimilarities = new ArrayList<SentenceSimilarity>();
        Map<String, Float> similarity = new HashMap<String, Float>();
        similarity.put("test1", 0.5f);
        similarity.put("test2", 0.4f);
        SentenceSimilarity sentenceSimilarity = new SentenceSimilarity(similarity, createSentence(), createSentence());        
        sentenceSimilarities.add(sentenceSimilarity);
        return sentenceSimilarities;
    }
    
    private List<Sarcasm> createSarcasms() {
        List<Sarcasm> sarcasms = new ArrayList<Sarcasm>();
        Sarcasm sarcasm = new Sarcasm(1, new TokenOffset(0, 1), document.getDefaultTokenStream(), Sarcasm.Judgment.NEGATIVE);
        sarcasm.setConfidence(0.88f);
        sarcasms.add(sarcasm);
        return sarcasms;
    }
    
    private List<Relation> createRelations() {
        List<Relation> relations = new ArrayList<Relation>();
        Relation relation = new Relation(1, new Type("Spouse"));
        relation.addArgument(new Argument(new Type("testArg"), 4, 0.3f));
        relation.addJustification(createDummyChunk());
        relation.addRelationClusterConfidencePair(1, 0.5f);
        relation.setAttribute(new Type("testAttr"));
        
        Map<Long, Float> relationClusterIdDistribution = new HashMap<Long, Float>();
        relationClusterIdDistribution.put(new Long(1), 0.5f);
        relation.setRelationClusterIdDistribution(relationClusterIdDistribution);
        
        relations.add(relation);
        
        return relations;
    }
    
    private List<ProsodicPhrase> createProsodicPhrases() {
        List<ProsodicPhrase> prosodicPhrases = new ArrayList<ProsodicPhrase>();
        ProsodicPhrase prosodicPhrase = new ProsodicPhrase(new TokenOffset(0, 1), document.getDefaultTokenStream(), 1);
        prosodicPhrase.setConfidence(0.3f);
        prosodicPhrase.setNoveltyConfidence(0.25f);
        prosodicPhrase.setType("TestType");
        prosodicPhrase.setUncertaintyConfidence(0.6f);
        prosodicPhrases.add(prosodicPhrase);
        return prosodicPhrases;
    }
    
    private List<Passage> createPassages() {
        List<Passage> passages = new ArrayList<Passage>();
        Passage passage = new Passage(1, new TokenOffset(0, 1), document.getDefaultTokenStream());
        passage.setAudioOffset(new AudioOffset(0, 1));
        passage.setContentType("testContentType");
        passage.setSpeaker("testSpeaker");
        passages.add(passage);
        return passages;
    }
    
    private List<PartOfSpeech> createPartsOfSpeech() {
        List<PartOfSpeech> partsOfSpeech = new ArrayList<PartOfSpeech>();
        PartOfSpeech pos = new PartOfSpeech(1, new TokenOffset(0, 1), document.getDefaultTokenStream());
        pos.setPosTag(new Type("testType"));
        partsOfSpeech.add(pos);        
        return partsOfSpeech;
    }
    
    private List<Paraphrase> createParaphrases() {
        List<Paraphrase> paraphrases = new ArrayList<Paraphrase>();
        paraphrases.add(new Paraphrase(createDummyChunk(), "test", Optional.fromNullable(new Type("Test1")), Optional.fromNullable(new Type("Test2")), 0.45f));    
        return paraphrases;
    }
    
    private List<Opinion> createOpinions() {
        List<Opinion> opinions = new ArrayList<Opinion>();
        opinions.add(new Opinion(1, new TokenOffset(0, 1), document.getDefaultTokenStream(), Subjectivity.SUBJECTIVE, Polarity.POSITIVE));
        opinions.add(new Opinion(2, new TokenOffset(1, 2), document.getDefaultTokenStream(), Subjectivity.OBJECTIVE, Polarity.NEGATIVE));
        return opinions;
    }
    
    private List<JointRelationCoreference> createJointRelationCoreferences() {
        List<JointRelationCoreference> jointRelationCoreferences = new ArrayList<JointRelationCoreference>();
        JointRelationCoreference jrc = new JointRelationCoreference();
        jrc.setAlgorithmName("testAlgName");
        jrc.setSourceAlgorithm(new SourceAlgorithm("algName", "contributingSiteName"));
        jrc.setCoreference(createCoreferences().get(0));
        
        jrc.setRelations(createRelations());
        
        jointRelationCoreferences.add(jrc);
        return jointRelationCoreferences;
    }
    
    private List<InterPausalUnit> createInterPausalUnits() {
        List<InterPausalUnit> interPausalUnits = new ArrayList<InterPausalUnit>();
        InterPausalUnit interPausalUnit = new InterPausalUnit(1, new AudioOffset(0, 1));
        interPausalUnit.setAlgorithmName("testAlgName");
        interPausalUnit.setSourceAlgorithm(new SourceAlgorithm("algName", "contributingSiteName"));
        
        Map<String, Float> acousticFeatures = new HashMap<String, Float>();
        acousticFeatures.put("test", 0.5f);
        interPausalUnit.setAcousticFeatures(acousticFeatures);
        
        interPausalUnits.add(interPausalUnit);
        return interPausalUnits;
    }
    
    private List<EventTextSet> createEventTextSets() {
        List<EventTextSet> eventTextSets = new ArrayList<EventTextSet>();
        EventTextSet.Builder eventTextSetBuilder = EventTextSet.builder(createEventTexts());
        eventTextSetBuilder.addAttribute(new Type("test"), 0.35f);
        eventTextSetBuilder.setScore(0.66f);
        eventTextSets.add(eventTextSetBuilder.build());
        return eventTextSets;
    }
    
    private List<EventText> createEventTexts() {
        List<EventText> eventTexts = new ArrayList<EventText>();
        EventText.Builder eventTextBuilder = EventText.builder(new Type("testEvent"), createDummyChunk());
        eventTextBuilder.addAttribute(new Type("testAttribute"), 0.4f);
        eventTextBuilder.setScore(0.8f);
        
        eventTexts.add(eventTextBuilder.build());
        return eventTexts;
    }
    
    private List<EventRelations> createEventRelations() {
        List<EventRelations> eventRelations = new ArrayList<EventRelations>();
        EventRelations eventRelation = new EventRelations();
        eventRelation.setCoreferences(createEvents());
        eventRelations.add(eventRelation);        
        return eventRelations;
    }
    
    private List<Event> createEvents() {
        List<Event> events = new ArrayList<Event>();
        Event event = new Event(1, new Type("Death"));
        event.addJustification(createDummyChunk());
        event.setAlgorithmName("testAlgName");
        event.setAttribute(new Type("testAttribute"));
        event.setConfidence(0.8f);
        event.setSourceAlgorithm(new SourceAlgorithm("test1", "test2"));
        event.addArgument(new Argument(new Type("test"), 2, 0.65f));
        events.add(event);
        return events;
    }
    
    private List<EventPhrase> createEventPhrases() {
        List<EventPhrase> eventPhrases = new ArrayList<EventPhrase>();
        eventPhrases.add(new EventPhrase(new TokenOffset(0, 1), document.getDefaultTokenStream(), 
                new Type("testTense1"), new Type("TestAspect1"), new Type("testEvent1")));
        eventPhrases.add(new EventPhrase(new TokenOffset(1, 2), document.getDefaultTokenStream(), 
                new Type("testTense2"), new Type("TestAspect2"), new Type("testEvent2")));        
        return eventPhrases;
    }
    
    private List<Entailment> createEntailments() {
        List<Entailment> entailments = new ArrayList<Entailment>();
        Entailment e1 = new Entailment(1);
        
        BoundedList<Pair<Entailment.Judgment, Float>> judgementConfidencePairs = new BoundedList<Pair<Entailment.Judgment, Float>>(1);
        judgementConfidencePairs.add(new Pair<Entailment.Judgment, Float>(Entailment.Judgment.ENTAILS, 0.5f));
        e1.setJudgmentDistribution(judgementConfidencePairs);
        
        e1.setAlgorithmName("testAlgorithmName");
        e1.setSourceAlgorithm(new SourceAlgorithm("testAlgName", "TestContributingSiteName"));
        e1.setHypothesis(new Passage(1, new TokenOffset(0, 1), document.getDefaultTokenStream()));
        e1.setText(new Passage(2, new TokenOffset(1, 2), document.getDefaultTokenStream()));
        entailments.add(e1);
        return entailments;
    }
    
    private List<DocumentRelation> createDocumentRelations() {
        List<DocumentRelation> documentRelations = new ArrayList<DocumentRelation>();
        DocumentRelation.Builder drBuilder = DocumentRelation.builder(new Type("Spouse"));
        
        DocumentRelationArgument.Builder arg1 = 
                DocumentRelationArgument.builder(new Type("testRole"), 
                        DocumentRelationArgument.Filler.fromEntity(createDummyEntity()), 0.9f);        
        drBuilder.addArgument(arg1.build());
        
        DocumentRelationArgument.Builder arg2 = 
                DocumentRelationArgument.builder(new Type("testRole"), 
                        DocumentRelationArgument.Filler.fromGenericThing(new GenericThing(new Type("testType"), "testValue")), 0.9f);        
        drBuilder.addArgument(arg2.build());
        
        DocumentRelationArgument.Builder arg3 = 
                DocumentRelationArgument.builder(new Type("testRole"), 
                        DocumentRelationArgument.Filler.fromNumericValue(new NumericValue(2)), 0.9f);        
        drBuilder.addArgument(arg3.build());
        
        DocumentRelationArgument.Builder arg4 = 
                DocumentRelationArgument.builder(new Type("testRole"), 
                        DocumentRelationArgument.Filler.fromTemporalValue(XSDDate.fromString("2015-09-01")), 0.9f);        
        drBuilder.addArgument(arg4.build());
        
        drBuilder.addProvenance(createDummyRelationMention());
        drBuilder.addAttribute(new Type("testAttribute"), 0.5f);
        
        ArgumentTuple argumentTuple = new ArgumentTuple(new Type("testArgumentTuple"));
        Argument argument = new Argument(new Type("testArg"), 5, 0.3f);
        argument.addArgumentConfidencePair(createDummyChunk(), 0.45f);
        argument.addKBArgumentConfidencePair(new KBID("test1", "test2"), 0.33f);
        argument.setAttribute(new Type("testType"));
        argumentTuple.addArgument(argument);
        argumentTuple.addJustification(createDummyChunk());
        argumentTuple.setAttribute(new Type("testAttribute"));
        argumentTuple.setConfidence(0.3f);        
        drBuilder.addProvenanceFromArgumentTuple(argumentTuple);
        
        drBuilder.setConfidence(0.5f);        
        
        documentRelations.add(drBuilder.build());
        return documentRelations;
    }
    
    private List<DocumentSentiment> createDocumentSentiments() {
        List<DocumentSentiment> documentSentiments = new ArrayList<DocumentSentiment>();
        DocumentSentiment.Builder dsBuilder = DocumentSentiment.builder();
        
        DocumentMentalStateArgument.Builder sourceArgBuilder = 
                DocumentMentalStateArgument.builder(new Type("source"), DocumentMentalStateArgument.Filler.createSourceArgument(createDummyEntity()), 0.5f);
        dsBuilder.addArgument(sourceArgBuilder.build());
        
        DocumentMentalStateArgument.Builder strengthArgBuilder = 
                DocumentMentalStateArgument.builder(new Type("strength"), DocumentMentalStateArgument.Filler.createStrengthArgument(new NumericValue(1)), 0.7f);
        dsBuilder.addArgument(strengthArgBuilder.build());
        
        DocumentMentalStateArgument.Builder target1ArgBuilder = 
                DocumentMentalStateArgument.builder(new Type("target"), DocumentMentalStateArgument.Filler.createTargetArgument(createDummyEntity()), 0.2f);
        dsBuilder.addArgument(target1ArgBuilder.build());
        
        dsBuilder.addProvenances(createSentimentMentions());
        
        documentSentiments.add(dsBuilder.build());
        return documentSentiments;
    }
    
    private List<DocumentBelief> createDocumentBeliefs() {
        List<DocumentBelief> documentBeliefs = new ArrayList<DocumentBelief>();
        DocumentBelief.Builder dbBuilder = DocumentBelief.builder();
        
        DocumentMentalStateArgument.Builder sourceArgBuilder = 
                DocumentMentalStateArgument.builder(new Type("source"), DocumentMentalStateArgument.Filler.createSourceArgument(createDummyEntity()), 0.5f);
        dbBuilder.addArgument(sourceArgBuilder.build());
        
        DocumentMentalStateArgument.Builder strengthArgBuilder = 
                DocumentMentalStateArgument.builder(new Type("strength"), DocumentMentalStateArgument.Filler.createStrengthArgument(new NumericValue(1)), 0.7f);
        dbBuilder.addArgument(strengthArgBuilder.build());
        
        DocumentMentalStateArgument.Builder target1ArgBuilder = 
                DocumentMentalStateArgument.builder(new Type("target"), DocumentMentalStateArgument.Filler.createTargetArgument(createDummyEntity()), 0.2f);
        dbBuilder.addArgument(target1ArgBuilder.build());
        
        dbBuilder.addProvenances(createBeliefMentions());
        
        documentBeliefs.add(dbBuilder.build());
        return documentBeliefs;
    }
    
    private List<Dependency> createDependencies() {
        List<Dependency> dependencies = new ArrayList<Dependency>();
        Dependency d1 = new Dependency(createDummyChunk(), createDummyChunk(), "test1");
        Dependency d2 = new Dependency(createDummyChunk(), createDummyChunk(), "test2");
        dependencies.add(d1);
        dependencies.add(d2);
        return dependencies;
    }
    
    private List<DeceptionTheory> createDeceptionTheories() {
        List<DeceptionTheory> deceptionTheories = new ArrayList<DeceptionTheory>();
        DeceptionTheory dt1 = new DeceptionTheory(createDummyChunk(), 0.8f, true);
        DeceptionTheory dt2 = new DeceptionTheory(createDummyChunk(), 0.67f, false);
        deceptionTheories.add(dt1);
        deceptionTheories.add(dt2);
        return deceptionTheories;
    }
    
    private List<Coreference> createCoreferences() {
        List<Coreference> coreferences = new ArrayList<Coreference>();
        Coreference coreference = new Coreference(1);
        coreference.setAlgorithmName("TestAlgorithmName");
        coreference.setSourceAlgorithm(new SourceAlgorithm("Test", "test"));
        coreference.setEntities(Collections.singletonList(createDummyEntity()));
        coreference.setResolvedMentions(Collections.singletonList(createDummyEntityMention()));
        coreferences.add(coreference);
        return coreferences;
    }
    
    private List<ConversationElement> createConversationElements() {
        List<ConversationElement> conversationElements = new ArrayList<ConversationElement>();
        ConversationElement ce1 = new ConversationElement(new TokenOffset(0, 1), document.getDefaultTokenStream());
        ce1.setAuthorId("testAuthorId");
        ce1.setAuthoredTime("testAuthoredTime");
        
        Map<IType, List<Entity>> conversationElementEntityRelations = new HashMap<IType, List<Entity>>();
        conversationElementEntityRelations.put(new Type("TestType"), Collections.singletonList(createDummyEntity()));
        ce1.setConversationElementEntityRelations(conversationElementEntityRelations);
        
        Map<IType, ConversationElementTag> conversationElementTagAttributes = new HashMap<IType, ConversationElementTag>();
        Map<String,Token> attributes = new HashMap<String,Token>();
        attributes.put("test", new Token(1, new CharOffset(0, 1), "testing"));
        conversationElementTagAttributes.put(new Type("TestType"), new ConversationElementTag("testName", attributes, 0, 1));
        ce1.setConversationElementTagAttributes(conversationElementTagAttributes);
        
        Map<IType, String> freeTextAttributes = new HashMap<IType, String>();
        freeTextAttributes.put(new Type("Test"), "testing");
        ce1.setFreeTextAttributes(freeTextAttributes);
        
        Map<IType, IType> ontologizedAttributes = new HashMap<IType, IType>();
        ontologizedAttributes.put(new Type("Test"), new OntType("testNamespace", "testValue"));
        ce1.setOntologizedAttributes(ontologizedAttributes);
        
        conversationElements.add(ce1);
        return conversationElements;
    }
    
    private List<CommittedBelief> createCommitedBeliefs() {
        List<CommittedBelief> commitedBeliefs = new ArrayList<CommittedBelief>();
        CommittedBelief cb1 = new CommittedBelief(1, new TokenOffset(0, 1), document.getDefaultTokenStream(), Modality.ABILITY);
        CommittedBelief cb2 = new CommittedBelief(2, new TokenOffset(0, 1), document.getDefaultTokenStream(), Modality.COMMITTED_BELIEF);
        commitedBeliefs.add(cb1);
        commitedBeliefs.add(cb2);
        return commitedBeliefs;
    }
    
    private List<SentimentMention> createSentimentMentions() {
        List<SentimentMention> sentimentMentions = new ArrayList<SentimentMention>();
        SentimentMention.Builder builder = SentimentMention.builder();
        builder.confidence = 0.9f;
        builder.justification = createDummyChunk();
               
        builder.addArgument(MentalStateMention.Filler.createSourceArgument(createDummyEntityMention(), 0.8f));
        builder.addArgument(MentalStateMention.Filler.createStrengthArgument(createDummyNumberPhrase(), 0.75f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyEntityMention(), 0.7f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyEventMention(), 0.77f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyRelationMention(), 0.63f));
        
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyEventMention(), createDummyEventMentionArgument(), 0.33f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyRelationMention(), 
                RelationMention.Filler.fromGenericChunk(createDummyChunk(), new Type("testType"), 0.65f), 0.45f));
        
        sentimentMentions.add(builder.build());
        return sentimentMentions;
    }
    
    private List<BeliefMention> createBeliefMentions() {
        List<BeliefMention> beliefMentions = new ArrayList<BeliefMention>();
        BeliefMention.Builder builder = BeliefMention.builder();
        builder.confidence = 0.9f;
        builder.justification = createDummyChunk();
               
        builder.addArgument(MentalStateMention.Filler.createSourceArgument(createDummyEntityMention(), 0.8f));
        builder.addArgument(MentalStateMention.Filler.createStrengthArgument(createDummyNumberPhrase(), 0.75f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyEntityMention(), 0.7f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyEventMention(), 0.77f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyRelationMention(), 0.63f));
        
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyEventMention(), createDummyEventMentionArgument(), 0.33f));
        builder.addArgument(MentalStateMention.Filler.createTargetArgument(createDummyRelationMention(), 
                RelationMention.Filler.fromGenericChunk(createDummyChunk(), new Type("testType"), 0.65f), 0.45f));
        
        beliefMentions.add(builder.build());
        return beliefMentions;
    }
    
    private RelationMention createDummyRelationMention() {
        RelationMention.Builder relationMentionBuilder = RelationMention.builder(new Type("Spouse"));
        relationMentionBuilder.addJustification(createDummyChunk());
        relationMentionBuilder.setConfidence(0.88f);
        relationMentionBuilder.addArgument(RelationMention.Filler.fromEntityMention(createDummyEntityMention(), new Type("testType"), 0.45f));
        return relationMentionBuilder.build();
    }
    
    private EventMention createDummyEventMention() {
        EventMention.Builder eventMentionBuilder = EventMention.builder(new Type("Death"));
        eventMentionBuilder.setScore(0.5f);
        eventMentionBuilder.setProvenance(createDummyEventText());
        eventMentionBuilder.addAttribute(new Type("testAttribute"), 0.55f);
        eventMentionBuilder.addArgument(createDummyEventMentionArgument());
        return eventMentionBuilder.build();
    }
    
    private EventMentionArgument createDummyEventMentionArgument() {
        EventMentionArgument.Builder eventMentionArgumentBuilder = EventMentionArgument.builder(new Type("Death"), new Type("TestRole"), createDummyChunk());
        eventMentionArgumentBuilder.setScore(0.3f);
        eventMentionArgumentBuilder.addAttribute(new Type("testAttribute"), 0.53f);
        return eventMentionArgumentBuilder.build();
    }
    
    private EventText createDummyEventText() {
        EventText.Builder eventTextBuilder = EventText.builder(new Type("Death"), createDummyChunk());
        eventTextBuilder.setScore(0.2f);
        eventTextBuilder.addAttribute(new Type("testAttribute"), 0.85f);
        return eventTextBuilder.build();
    }
    
    private NumberPhrase createDummyNumberPhrase() {
        NumberPhrase numberPhrase = new NumberPhrase(new TokenOffset(0, 1), document.getDefaultTokenStream());
        numberPhrase.setAlgorithmName("testAlgorithmName");
        numberPhrase.setSourceAlgorithm(new SourceAlgorithm("testAlgorithmName", "testContributingSite"));
        numberPhrase.setNumber(1);
        return numberPhrase;
    }
    
    private Chunk createDummyChunk() {
        Chunk chunk = new Chunk(new TokenOffset(0, 1), document.getDefaultTokenStream());
        chunk.setAlgorithmName("testAlgorithm");
        chunk.setSourceAlgorithm(new SourceAlgorithm("testAlgorithmName", "testContributingSiteName"));        
        return chunk;
    }
    
    private List<AuthorshipTheory> createAuthorshipTheories() {
        List<AuthorshipTheory> authorshipTheories = new ArrayList<AuthorshipTheory>();
        
        AuthorshipTheory at1 = new AuthorshipTheory(new TokenOffset(0, 1), document.getDefaultTokenStream(), createDummyEntity());
        at1.setConfidence(0.9f);
        
        AuthorshipTheory at2 = new AuthorshipTheory(new TokenOffset(3, 4), document.getDefaultTokenStream(), createDummyEntity());
        at2.setConfidence(0.9f);
        
        authorshipTheories.add(at1);
        authorshipTheories.add(at2);
        return authorshipTheories;
    }
    
    private Entity createDummyEntity() {
        Entity entity = new Entity(1, new Type("testType"));
        entity.setAlgorithmName("testAlgorithmName");
        entity.setCanonicalMentionConfidence(0.75f);
        entity.setEmailAddress("testEmailAddress");
        entity.setEntityConfidence(0.9f);
        entity.setNativeLanguage(new Type("testNativeLanguage"));
        entity.setSourceAlgorithm(new SourceAlgorithm("testAlgorithmName", "testContributingSiteName"));                
        entity.setCanonicalMentions(createDummyEntityMention());
        
        
        Map<IType, String> freeTextAttributes = new HashMap<IType, String>();
        freeTextAttributes.put(new Type("testType"), "testAttribute");
        entity.setFreeTextAttributes(freeTextAttributes);
        
         
        Map<IType, IType> ontologizedTextAttributes = new HashMap<IType, IType>();
        ontologizedTextAttributes.put(new Type("testType"), new OntType("testNamespace", "testOntType"));
        entity.setOntologizedAttributes(ontologizedTextAttributes);
        
        
        Set<String> attributeValues = new HashSet<String>();
        attributeValues.add("testValue1");
        attributeValues.add("testValue2");
        entity.setTypeAttribute("testAttribute", attributeValues);
        
        HashMap<String, Set<String>> mapStringToSetString = new HashMap<String, Set<String>>();
        Set<String> setString = new HashSet<String>();
        setString.add("testValue1");
        setString.add("testValue2");
        mapStringToSetString.put("testString", setString);
        entity.setTypeInformation(mapStringToSetString);
        
        return entity;
    }
    
    private EntityMention createDummyEntityMention() {
        EntityMention entityMention = new EntityMention(1, new TokenOffset(0, 1), document.getDefaultTokenStream());
        entityMention.setAlgorithmName("testAlgorithmName");
        entityMention.setAttribute("testAttribute", "testValue");
        entityMention.setDocId("testDocId");
        
        Map<Long, Float> entityIdDistribution = new HashMap<Long, Float>();
        entityIdDistribution.put(new Long(1), 0.9f);
        entityMention.setEntityIdDistribution(entityIdDistribution);
        
        entityMention.setEntityType(new Type("Person"));
        entityMention.setMentionType(new Type("Person"));
        entityMention.setSourceAlgorithm(new SourceAlgorithm("testSourceAlgorithm", "testContributingSiteName"));
        entityMention.setTokenizerType(TokenizerType.ADEPT);
        
        entityMention.setContext();
        return entityMention;        
    }
    
    private List<AnomalousText> createAnamolousTexts() {
        List<AnomalousText> anomalousTexts = new ArrayList<AnomalousText>();
        
        AnomalousText at1 = new AnomalousText(1.0f, document);
        at1.setExplanation("This is a test explanation.");
        
        AnomalousText at2 = new AnomalousText(0.8f, document);
        at2.setExplanation("This is a test explanation.");
        
        anomalousTexts.add(at1);
        anomalousTexts.add(at2);
        return anomalousTexts;
    }
    
    private Token createDummyToken() {
        Token token = new Token(0, new CharOffset(0, 1), "testValue");
        token.setAudioOffset(new AudioOffset(0, 1));
        token.setConfidence(1.0f);
        token.setLemma("testLemma");
        token.setTokenType(TokenType.WORD);
        return token;
    }
}
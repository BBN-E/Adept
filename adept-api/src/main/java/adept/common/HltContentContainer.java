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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import java.io.Serializable;


// TODO: Check for nulls if needed in future.
/**
 * The Class HltContentContainer, which contains the 
 * full annotation of an input Document, including 
 * objects such as Coreference, EntityMention, Event, 
 * Passage, Relation and Sentence.
 */
public class HltContentContainer extends HltContent implements Serializable {

	private static final long serialVersionUID = -1948855332958545063L;

	/** The coreferences. */
	// Keep these first so that XSLT will find complete EntityMention elements
	// inside the <resolvedEntityMentions> element, rather than only references to EntityMentions.
	private List<Coreference> coreferences;
	
	/** The sentence similarities */
	// Keep these before sentences for the same reason as above comment
	private ImmutableList<SentenceSimilarity> sentenceSimilarities;

	/** The sentences. */
	private ImmutableList<Sentence> sentences;

	/** The passages. */
	private ImmutableList<Passage> passages;

	/** The entity mentions. */
	private ImmutableList<EntityMention> entityMentions;

	/** The named entities. */
	private ImmutableList<EntityMention> namedEntities;

	/** The part of speechs. */
	private ImmutableList<PartOfSpeech> partOfSpeechs;

	/** The dependencies. */
	private ImmutableList<Dependency> dependencies;

	/** The syntactic chunks. */
	private ImmutableList<SyntacticChunk> syntacticChunks;

	/** The tagged chunks. */
	private ImmutableList<TaggedChunk> taggedChunks;
	
	/** The relations. */
	private ImmutableList<Relation> relations;
        
	/** The joint relation coreferences. */
	private ImmutableList<JointRelationCoreference> jointRelationCoreferences;

	/** The opinions. */
	private ImmutableList<Opinion> opinions;


	/** The prosodic phrases. */
	private ImmutableList<ProsodicPhrase> prosodicPhrases;

	/** The sarcasms. */
	private ImmutableList<Sarcasm> sarcasms;
	
	private ImmutableList<DeceptionTheory> deceptionTheories;

	/** The committed beliefs. */
	private ImmutableList<CommittedBelief> committedBeliefs;

	/** Utterances. */
	private ImmutableList<Utterance> utterances;


	/** The inter pausal units. */
	private ImmutableList<InterPausalUnit> interPausalUnits;

	private ImmutableList<AnomalousText> anomalousTexts;

	private ImmutableList<Entailment> entailments;

    private List<Event> events;

	/** The event mentions */
	private List<EventMention> eventMentions;

    /** The event provenances */
    private ImmutableList<EventText> eventTexts;

    private ImmutableList<DocumentEvent> documentEvents;
    private ImmutableList<DocumentEventArgument> documentEventArguments;
    private ImmutableList<EventMentionArgument> eventMentionArguments;
    private ImmutableList<EventTextSet> eventTextSets;

	/** The events relations. */
	private ImmutableList<EventRelations> eventRelations;
	
	/** The events phrases. */
	private ImmutableList<EventPhrase> eventPhrases;

	/** The sessions. */
	private ImmutableList<Session> sessions;

    /** The triples */
    private ImmutableList<Triple> triples;

    /** The paraphrases */
    private ImmutableList<Paraphrase> paraphrases;

	/** The time phrases. */
	private ImmutableList<TimePhrase> timePhrases;

	/** The temporal resolutions. */
	private ImmutableList<TemporalResolution> temporalResolutions;
	
	/** The document relations */
	private ImmutableList<DocumentRelation> documentRelations;
	
	/** The relation mentions */
	private ImmutableList<RelationMention> relationMentions;
        
        /** The document sentiments */
	private ImmutableList<DocumentSentiment> documentSentiments;
	
	/** The relation mentions */
	private ImmutableList<SentimentMention> sentimentMentions;
        
        /** The document beliefs */
	private ImmutableList<DocumentBelief> documentBeliefs;
	
	/** The belief mentions */
	private ImmutableList<BeliefMention> beliefMentions;
	
	/** The conversations. */
	private ImmutableList<ConversationElement> conversationElements;
	
	/** The authorship theories. */
	private ImmutableList<AuthorshipTheory> authorshipTheories;
	
	/** Document entity to KBEntity map. */
	private Map<Entity, Map<KBID,Float>> documentEntityToKBEntityMap;
	
	/** Document relation to KBRelation map. */
	private Map<DocumentRelation, Map<KBID,Float>> documentRelationToKBRelationMap;
        
        /** Document sentiment to KBSentiment map. */
	private Map<DocumentSentiment, Map<KBID,Float>> documentSentimentToKBSentimentMap;
        
        /** Document belief to KBBelief map. */
	private Map<DocumentBelief, Map<KBID,Float>> documentBeliefToKBBeliefMap;
	
	/**
	 * Instantiates a new hlt content container.
	 */
	public HltContentContainer() {
        this.eventMentions = ImmutableList.of();
        this.eventTexts = ImmutableList.of();
        
        documentEntityToKBEntityMap = new LinkedHashMap<Entity, Map<KBID,Float>>();
        documentRelationToKBRelationMap = new LinkedHashMap<DocumentRelation, Map<KBID,Float>>();
        documentSentimentToKBSentimentMap = new HashMap<DocumentSentiment, Map<KBID,Float>>();
        documentBeliefToKBBeliefMap = new HashMap<DocumentBelief, Map<KBID,Float>>();
	}

	/**
	 * Gets the named entities.
	 * 
	 * @return the named entities
	 */
	public List<EntityMention> getNamedEntities() {
		return namedEntities;
	}

	/**
	 * Sets the named entities.
	 * 
	 * @param namedEntities
	 *            the new named entities
	 */
	public void setNamedEntities(List<EntityMention> namedEntities) {
		this.namedEntities = ImmutableList.copyOf(namedEntities);
	}

	/**
	 * Gets the entity mentions.
	 * 
	 * @return the entity mentions
	 */
	public List<EntityMention> getEntityMentions() {
		return entityMentions;
	}

	/**
	 * Sets the entity mentions.
	 * 
	 * @param entityMentions
	 *            the new entity mentions
	 */
	public void setEntityMentions(List<EntityMention> entityMentions) {
		this.entityMentions = ImmutableList.copyOf(entityMentions);
	}

	/**
	 * Gets the opinions.
	 * 
	 * @return the opinions
	 */
	public List<Opinion> getOpinions() {
		return opinions;
	}

	/**
	 * Sets the opinions.
	 * 
	 * @param opinions
	 *            the new opinions
	 */
	public void setOpinions(List<Opinion> opinions) {
		this.opinions = ImmutableList.copyOf(opinions);
	}


	/**
	 * Gets the prosodic phrases.
	 * 
	 * @return the prosodic phrases
	 */
	public List<ProsodicPhrase> getProsodicPhrases() {
		return prosodicPhrases;
	}

	/**
	 * Sets the prosodic phrases.
	 * 
	 * @param prosodicPhrases
	 *            the new prosodic phrases
	 */
	public void setProsodicPhrases(List<ProsodicPhrase> prosodicPhrases) {
		this.prosodicPhrases = ImmutableList.copyOf(prosodicPhrases);
	}

	/**
	 * Gets the sarcasms.
	 * 
	 * @return the sarcasms
	 */
	public List<Sarcasm> getSarcasms() {
		return sarcasms;
	}

	/**
	 * Sets the sarcasms.
	 * 
	 * @param sarcasms
	 *            the new sarcasms
	 */
	public void setSarcasms(List<Sarcasm> sarcasms) {
		this.sarcasms = ImmutableList.copyOf(sarcasms);
	}
	
	/**
	 * Gets the event mentions.
	 * 
	 * @return the events
	 */
	public List<EventMention> getEventMentions() {
		return eventMentions;
	}

	/**
	 * Sets the events.
	 *
	 * @param events the new events
	 */
	public void setEvents(Iterable<Event> events) {
        this.events = Lists.newArrayList(events);
	}

    public List<Event> getEvents() {
        return events;
    }

    public List<DocumentEvent> getDocumentEvents() {
        return documentEvents;
    }

	public void setDocumentEvents(Iterable<DocumentEvent> events) {
        this.documentEvents = ImmutableList.copyOf(events);
    }

	public void setEventMentions(List<EventMention> eventMentions) {
		this.eventMentions = new ArrayList<EventMention>(eventMentions);
	}

    public List<EventText> getEventTexts() {
        return eventTexts;
    }

    public void setEventTexts(List<EventText> eventTexts) {
        this.eventTexts = ImmutableList.copyOf(eventTexts);
    }


    public void setEventMentionArguments(Iterable<EventMentionArgument> eventMentionArguments) {
        this.eventMentionArguments = ImmutableList.copyOf(eventMentionArguments);
    }

    public List<EventMentionArgument> getEventMentionArguments() {
        return eventMentionArguments;
    }

    public void setDocumentEventArguments(Iterable<DocumentEventArgument> documentEventArguments) {
        this.documentEventArguments = ImmutableList.copyOf(documentEventArguments);
    }


    public Iterable<DocumentEventArgument> getDocumentEventArguments() {
        return documentEventArguments;
    }

    public List<EventTextSet> getEventTextSets() {
        return eventTextSets;
    }

    public void setEventTextSets(Iterable<EventTextSet> eventTextSets) {
        this.eventTextSets = ImmutableList.copyOf(eventTextSets);
    }

    /**
	 * Gets the sentences.
	 * 
	 * @return the sentences
	 */
	public List<Sentence> getSentences() {
		return sentences;
	}

	/**
	 * Sets the sentences.
	 * 
	 * @param sentences
	 *            the new sentences
	 */
	public void setSentences(List<Sentence> sentences) {
		this.sentences = ImmutableList.copyOf(sentences);
	}

	/**
	 * Gets the relations.
	 * 
	 * @return the relations
	 */
	public List<Relation> getRelations() {
		return relations;
	}

	/**
	 * Sets the relations.
	 * 
	 * @param relations
	 *            the new relations
	 */
	public void setRelations(List<Relation> relations) {
		this.relations = ImmutableList.copyOf(relations);
	}
	
	/**
	 * Gets the coreferences.
	 * 
	 * @return the coreferences
	 */
	public List<Coreference> getCoreferences() {
		return coreferences;
	}

	/**
	 * Sets the coreferences.
	 * 
	 * @param coreferences
	 *            the new coreferences
	 */
	public void setCoreferences(List<Coreference> coreferences) {
		this.coreferences = new ArrayList<Coreference>(coreferences);
	}

	/**
	 * Gets the passages.
	 * 
	 * @return the passages
	 */
	public List<Passage> getPassages() {
		return passages;
	}

	/**
	 * Sets the passages.
	 * 
	 * @param passages
	 *            the new passages
	 */
	public void setPassages(List<Passage> passages) {
		this.passages = ImmutableList.copyOf(passages);
	}

	/**
	 * Gets the joint relation coreferences.
	 * 
	 * @return the joint relation coreferences
	 */
	public List<JointRelationCoreference> getJointRelationCoreferences() {
		return jointRelationCoreferences;
	}

	/**
	 * Sets the joint relation coreferences.
	 * 
	 * @param jointRelationCoreferences
	 *            the new joint relation coreferences
	 */
	public void setJointRelationCoreferences(
			List<JointRelationCoreference> jointRelationCoreferences) {
		this.jointRelationCoreferences = ImmutableList.copyOf(jointRelationCoreferences);
	}

	/**
	 * Gets the part of speechs.
	 * 
	 * @return the part of speechs
	 */
	public List<PartOfSpeech> getPartOfSpeechs() {
		return partOfSpeechs;
	}

	/**
	 * Sets the part of speechs.
	 * 
	 * @param partOfSpeechs
	 *            the new part of speechs
	 */
	public void setPartOfSpeechs(List<PartOfSpeech> partOfSpeechs) {
		this.partOfSpeechs = ImmutableList.copyOf(partOfSpeechs);
	}

	/**
	 * Gets the syntactic chunks.
	 * 
	 * @return the syntactic chunks
	 */
	public List<SyntacticChunk> getSyntacticChunks() {
		return syntacticChunks;
	}

	/**
	 * Sets the syntactic chunks.
	 * 
	 * @param syntacticChunks
	 *            the new syntactic chunks
	 */
	public void setSyntacticChunks(List<SyntacticChunk> syntacticChunks) {
		this.syntacticChunks = ImmutableList.copyOf(syntacticChunks);
	}

	/**
	 * Gets the tagged chunks.
	 * 
	 * @return the tagged chunks
	 */
	public List<TaggedChunk> getTaggedChunks() {
		return taggedChunks;
	}

	/**
	 * Sets the tagged chunks.
	 * 
	 * @param taggedChunks
	 *            the new tagged chunks
	 */
	public void setTaggedChunks(List<TaggedChunk> taggedChunks) {
		this.taggedChunks = ImmutableList.copyOf(taggedChunks);
	}
	
	/**
	 * Gets the dependencies.
	 * 
	 * @return the dependencies
	 */
	public List<Dependency> getDependencies() {
		return dependencies;
	}

	/**
	 * Sets the dependencies.
	 * 
	 * @param dependencies
	 *            the new dependencies
	 */
	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = ImmutableList.copyOf(dependencies);
	}

	/**
	 * Gets the committed beliefs.
	 *
	 * @return the committed beliefs
	 */
	public List<CommittedBelief> getCommittedBeliefs() {
		return committedBeliefs;
	}

	/**
	 * Sets the committed beliefs.
	 *
	 * @param committedBeliefs the new committed beliefs
	 */
	public void setCommittedBeliefs(List<CommittedBelief> committedBeliefs) {
		this.committedBeliefs = ImmutableList.copyOf(committedBeliefs);
	}
	
	/**
	 * Gets the utterances.
	 *
	 * @return the utterances
	 */
	public List<Utterance> getUtterances() {
		return utterances;
	}
	
	/**
	 * Sets the utterances.
	 *
	 * @param utterances the utterances
	 */
	public void setUtterances(List<Utterance> utterances) {
		this.utterances = ImmutableList.copyOf(utterances);
	}

	/**
	 * Gets the inter pausal units.
	 *
	 * @return the inter pausal units
	 */
	public List<InterPausalUnit> getInterPausalUnits() {
		return interPausalUnits;
	}


	/**
	 * Sets the inter pausal units.
	 *
	 * @param interPausalUnits the new inter pausal units
	 */

	public void setInterPausalUnits(List<InterPausalUnit> interPausalUnits) {
		this.interPausalUnits = ImmutableList.copyOf(interPausalUnits);
	}

	/**
	 * Gets the anomalous texts.
	 * @return The anomalous texts.
	 */
	public List<AnomalousText> getAnomalousTexts() {
		return anomalousTexts;
	}

	/**
	 * Sets the anomalous texts.
	 * @param anomalousTexts The new anomalous texts.
	 */
	public void setAnomalousTexts(List<AnomalousText> anomalousTexts) {
		this.anomalousTexts = ImmutableList.copyOf(anomalousTexts);
	}

	/**
	 * Gets the entailments.
	 * @return The entailments.
	 */
	public List<Entailment> getEntailments() {
		return entailments;
	}

	/**
	 * Sets the entailments.
	 * @param entailments The new entailments.
	 */
	public void setEntailments(List<Entailment> entailments) {
		this.entailments = ImmutableList.copyOf(entailments);
	}

	/**
	 * Gets the sessions.
	 *
	 * @return the sessions
	 */
	public List<Session> getSessions() {
		return sessions;
	}

	/**
	 * Sets the sessions.
	 *
	 * @param sessions the new sessions
	 */
	public void setSessions(List<Session> sessions) {
		this.sessions = ImmutableList.copyOf(sessions);
	}


         /**
         * Gets the triples.
         *
         * @return the triples
         */
        public List<Triple> getTriples() {
                return triples;
        }

        /**
         * Sets the triples.
         *
         * @param triples
         *            the new entity mentions
         */
        public void setTriples(List<Triple> triples) {
                this.triples = ImmutableList.copyOf(triples);
        }

        /**
         * Gets the paraphrases.
         *
         * @return the paraphrases
         */
        public List<Paraphrase> getParaphrases() {
                return paraphrases;
        }

        /**
         * Sets the paraphrases.
         *
         * @param paraphrases
         *            the new entity mentions
         */
        public void setParaphrases(List<Paraphrase> paraphrases) {
                this.paraphrases = ImmutableList.copyOf(paraphrases);
        } 

	/**
	 * Gets the event relations.
	 *
     * @deprecated
	 * @return the event relations
	 */
    @Deprecated
	public List<EventRelations> getEventRelations() {
		return eventRelations;
	}

	/**
	 * Sets the eventRelations.
	 *
	 * @param eventRelations the new eventRelations
     * @deprecated
	 */
    @Deprecated
	public void setEventRelations(List<EventRelations> eventRelations) {
		this.eventRelations = ImmutableList.copyOf(eventRelations);
	}
    
    /**
	 * Gets the event phrases.
	 *
     * @deprecated
	 * @return the event phrases
	 */
    @Deprecated
	public List<EventPhrase> getEventPhrases() {
		return eventPhrases;
	}

	/**
	 * Sets the event phrases.
	 *
	 * @param eventPhrases the new event phrases
     * @deprecated
	 */
    @Deprecated
	public void setEventPhrase(List<EventPhrase> eventPhrases) {
		this.eventPhrases = ImmutableList.copyOf(eventPhrases);
	}

	/**
	 * Gets the time phrases.
	 *
	 * @return the time phrases
	 */
	public List<TimePhrase> getTimePhrases() {
		return timePhrases;
	}

	/**
	 * Sets the timePhrases.
	 *
	 * @param timePhrases the new timePhrases
	 */
	public void setTimePhrases(List<TimePhrase> timePhrases) {
		this.timePhrases = ImmutableList.copyOf(timePhrases);
	}

	/**
	 * Gets the temporal resolutions.
	 *
	 * @return the temporal resolutions
	 */
	public List<TemporalResolution> getTemporalResolutions() {
		return temporalResolutions;
	}

	/**
	 * Sets the temporalResolutions.
	 *
	 * @param temporalResolutions the new temporalResolutions
	 */
	public void setTemporalResolutions(List<TemporalResolution> temporalResolutions) {
		this.temporalResolutions = ImmutableList.copyOf(temporalResolutions);
	}
	
	/**
	 * Gets the document.
	 *
	 * @return the document
	 */
	public Document getDocument() {
		Document document = null;
		if (this.passages != null && this.passages.size() > 0) {
			document = this.passages.get(0).tokenStream.getDocument();
		} else if (this.sentences != null && this.sentences.size() > 0) {
			document = this.sentences.get(0).tokenStream.getDocument();
		} else if (this.conversationElements != null && this.conversationElements.size() > 0) {
			document = this.conversationElements.get(0).getMessageChunk().getTokenStream().getDocument();
		}
		return document;
	}

	/**
	 * Gets the document id.
	 *
	 * @return the document id
	 */
	public String getDocumentId() {
		String docId = null;
		if (this.passages != null && this.passages.size() > 0) {
			docId = this.passages.get(0).tokenStream.getDocument().getDocId();
		} else if (this.sentences != null && this.sentences.size() > 0) {
			docId = this.sentences.get(0).tokenStream.getDocument().getDocId();
		}
		return docId;		
	}

	public List<SentenceSimilarity> getSentenceSimilarities() {
		return sentenceSimilarities;
	}

	public void setSentenceSimilarities(List<SentenceSimilarity> sentenceSimilarities) {
		this.sentenceSimilarities = ImmutableList.copyOf(sentenceSimilarities);
	}
	
	public List<ConversationElement> getConversationElements() {
		return conversationElements;
	}

	public void setConversationElements(List<ConversationElement> conversationElements) {
		this.conversationElements = ImmutableList.copyOf(conversationElements);
	}
	
	/** get document relations */
	public List<DocumentRelation> getDocumentRelations() {
		return documentRelations;
	}
	
	/** set document relations */
	public void setDocumentRelations(List<DocumentRelation> documentRelations) {
		this.documentRelations = ImmutableList.copyOf(documentRelations);
	}
	
	/** get relation mentions */
	public List<RelationMention> getRelationMentions() {
		return relationMentions;
	}
	
	/** set relation mentions */
	public void setRelationMentions(List<RelationMention> relationMentions) {
		this.relationMentions = ImmutableList.copyOf(relationMentions);
	}
	
        
        /** get document sentiments */
	public List<DocumentSentiment> getDocumentSentiments() {
		return documentSentiments;
	}
	
	/** set document sentiments */
	public void setDocumentSentiments(List<DocumentSentiment> documentSentiments) {
		this.documentSentiments = ImmutableList.copyOf(documentSentiments);
	}
	
	/** get sentiments mentions */
	public List<SentimentMention> getSentimentMentions() {
		return sentimentMentions;
	}
	
	/** set sentiments mentions */
	public void setSentimentMentions(List<SentimentMention> sentimentMentions) {
		this.sentimentMentions = ImmutableList.copyOf(sentimentMentions);
	}
        
        /** get document beliefs */
	public List<DocumentBelief> getDocumentBeliefs() {
		return documentBeliefs;
	}
	
	/** set document beliefs */
	public void setDocumentBeliefs(List<DocumentBelief> documentBeliefs) {
		this.documentBeliefs = ImmutableList.copyOf(documentBeliefs);
	}
	
	/** get beliefs mentions */
	public List<BeliefMention> getBeliefMentions() {
		return beliefMentions;
	}
	
	/** set beliefs mentions */
	public void setBeliefMentions(List<BeliefMention> beliefMentions) {
		this.beliefMentions = ImmutableList.copyOf(beliefMentions);
	}
	
	/** get authorship theories */
	public List<AuthorshipTheory> getAuthorshipTheories() {
		return authorshipTheories;
	}
	
	/** set authorship theories */
	public void setAuthorshipTheories(List<AuthorshipTheory> authorshipTheories) {
		this.authorshipTheories = ImmutableList.copyOf(authorshipTheories);
	}
	
	
	/** getter methods related to KB object maps */
	
	/**
	 * Add a KB Entity mapping to a document entity
	 */
	public void addEntityToKBEntityMap(Entity entity, KBID kbEntity, float confidence)
	{
		if(documentEntityToKBEntityMap == null)
		{
			documentEntityToKBEntityMap = new LinkedHashMap<Entity, Map<KBID,Float>>();
		}
		if(documentEntityToKBEntityMap.containsKey(entity))
		{
			Map<KBID,Float> kbEntities = documentEntityToKBEntityMap.get(entity);
			kbEntities.put(kbEntity,confidence);
		}
		else
		{
			Map<KBID,Float> kbEntities = new LinkedHashMap<KBID,Float>();
			documentEntityToKBEntityMap.put(entity, kbEntities);
			kbEntities.put(kbEntity, confidence);
		}
		
	}
	
	/**
	 * Add KB relation mapping to a document relation.
	 */
	public void addRelationToKBRelationMap(DocumentRelation docRelation, KBID kbRelation, float confidence)
	{
		if(documentRelationToKBRelationMap == null)
		{
			documentRelationToKBRelationMap = new LinkedHashMap<DocumentRelation, Map<KBID,Float>>();
		}
		if(documentRelationToKBRelationMap.containsKey(docRelation))
		{
			Map<KBID,Float> kbRelations = documentRelationToKBRelationMap.get(docRelation);
			kbRelations.put(kbRelation,confidence);
		}
		else
		{
			Map<KBID,Float> kbRelations = new LinkedHashMap<KBID,Float>();
			documentRelationToKBRelationMap.put(docRelation, kbRelations);
			kbRelations.put(kbRelation,confidence);
		}
		
	}
        
        /**
	 * Add KB sentiment mapping to a document sentiment.
	 */
	public void addSentimentToKBSentimentMap(DocumentSentiment docSentiment, KBID kbSentiment, float confidence)
	{
		if(documentSentimentToKBSentimentMap == null)
		{
			documentSentimentToKBSentimentMap = new HashMap<DocumentSentiment, Map<KBID,Float>>();
		}
		if(documentSentimentToKBSentimentMap.containsKey(docSentiment))
		{
			Map<KBID,Float> kbSentiments = documentSentimentToKBSentimentMap.get(docSentiment);
			kbSentiments.put(kbSentiment,confidence);
		}
		else
		{
			Map<KBID,Float> kbSentiments = new HashMap<KBID,Float>();
			documentSentimentToKBSentimentMap.put(docSentiment, kbSentiments);
			kbSentiments.put(kbSentiment,confidence);
		}
	}
        
        /**
	 * Add KB belief mapping to a document belief.
	 */
	public void addBeliefToKBBeliefMap(DocumentBelief docBelief, KBID kbBelief, float confidence)
	{
		if(documentBeliefToKBBeliefMap == null)
		{
			documentBeliefToKBBeliefMap = new HashMap<DocumentBelief, Map<KBID,Float>>();
		}
		if(documentBeliefToKBBeliefMap.containsKey(docBelief))
		{
			Map<KBID,Float> kbBeliefs = documentBeliefToKBBeliefMap.get(docBelief);
			kbBeliefs.put(kbBelief,confidence);
		}
		else
		{
			Map<KBID,Float> kbBeliefs = new HashMap<KBID,Float>();
			documentBeliefToKBBeliefMap.put(docBelief, kbBeliefs);
			kbBeliefs.put(kbBelief,confidence);
		}
	}
	
	/**
	 * Get KB entity map for all document entities
	 */
	public Map<Entity, Map<KBID,Float>> getKBEntityMapForDocEntities()
	{
		return this.documentEntityToKBEntityMap;
	}
	
	/**
	 * Get KB relation map for all document relations
	 */
	public Map<DocumentRelation, Map<KBID,Float>> getKBRelationMapForDocRelations()
	{
		return this.documentRelationToKBRelationMap;
	}
        
        /**
	 * Get KB sentiment map for all document sentiments
	 */
	public Map<DocumentSentiment, Map<KBID,Float>> getKBSentimentMapForDocSentiments()
	{
		return this.documentSentimentToKBSentimentMap;
	}
        
        /**
	 * Get KB belief map for all document beliefs
	 */
	public Map<DocumentBelief, Map<KBID,Float>> getKBBeliefMapForDocBeliefs()
	{
		return this.documentBeliefToKBBeliefMap;
	}
	
	/**
	 * Get KB entity map for specific document entity
	 */
	public Map<KBID,Float> getKBEntityMapForEntity(Entity e)
	{
	    return this.documentEntityToKBEntityMap.get(e);
	}
	
	/**
	 * Get KB relation map for specific document relation
	 */
	public Map<KBID, Float> getKBRelationMapForRelation(DocumentRelation r)
	{
	    return this.documentRelationToKBRelationMap.get(r);
	}
        
        /**
	 * Get KB sentiment map for specific document sentiment
	 */
	public Map<KBID, Float> getKBSentimentMapForSentiment(DocumentSentiment s)
	{
	    return this.documentSentimentToKBSentimentMap.get(s);
	}
        
        /**
	 * Get KB belief map for specific document belief
	 */
	public Map<KBID, Float> getKBBeliefMapForBelief(DocumentBelief b)
	{
	    return this.documentBeliefToKBBeliefMap.get(b);
	}
	
	/**
	 * Gets the DeceptionTheories.
	 * 
	 * @return the deception theories
	 */
	public List<DeceptionTheory> getDeceptionTheories() {
		return deceptionTheories;
	}

	/**
	 * Sets the deception theories.
	 * 
	 * @param deceptionTheories
	 */
	public void setDeceptionTheories(List<DeceptionTheory> deceptionTheories) {
		this.deceptionTheories = ImmutableList.copyOf(deceptionTheories);
	}
}


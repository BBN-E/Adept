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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class HltContentContainer, which contains the 
 * full annotation of an input Document, including 
 * objects such as Coreference, EntityMention, Event, 
 * Passage, Relation and Sentence.
 */
public class HltContentContainer extends HltContent {
       
        /** The coreferences. */
	// Keep these first so that XSLT will find complete EntityMention elements
	// inside the <resolvedEntityMentions> element, rather than only references to EntityMentions.
	private List<Coreference> coreferences;

	/** The sentences. */
	private List<Sentence> sentences;

	/** The passages. */
	private List<Passage> passages;

	/** The entity mentions. */
	private List<EntityMention> entityMentions;

	/** The named entities. */
	private List<EntityMention> namedEntities;

	/** The part of speechs. */
	private List<PartOfSpeech> partOfSpeechs;

	/** The dependencies. */
	private List<Dependency> dependencies;

	/** The syntactic chunks. */
	private List<SyntacticChunk> syntacticChunks;

	/** The tagged chunks. */
	private List<TaggedChunk> taggedChunks;
	
	/** The relations. */
	private List<Relation> relations;

	/** The joint relation coreferences. */
	private List<JointRelationCoreference> jointRelationCoreferences;

	/** The opinions. */
	private List<Opinion> opinions;

	/** The posts. */
	private List<Post> posts;

	/** The prosodic phrases. */
	private List<ProsodicPhrase> prosodicPhrases;

	/** The sarcasms. */
	private List<Sarcasm> sarcasms;

	/** The committed beliefs. */
	private List<CommittedBelief> committedBeliefs;

	/** Utterances. */
	private List<Utterance> utterances;

    /** The messages. */
    private List<Message> messages;

	/** The inter pausal units. */
	private List<InterPausalUnit> interPausalUnits;

    private List<Event> events;

	/** The event mentions */
	private List<EventMention> eventMentions;

    /** The event provenances */
    private List<EventText> eventTexts;

    private ImmutableList<DocumentEvent> documentEvents;
    private ImmutableList<DocumentEventArgument> documentEventArguments;
    private ImmutableList<EventMentionArgument> eventMentionArguments;
    private ImmutableList<EventTextSet> eventTextSets;

	/** The events relations. */
	private List<EventRelations> eventRelations;

	/** The sessions. */
	private List<Session> sessions;

        /** The triples */
        private List<Triple> triples;

        /** The paraphrases */
        private List<Paraphrase> paraphrases;

	/** The time phrases. */
	private List<TimePhrase> timePhrases;
		
	private List<SentenceSimilarity> sentenceSimilarities;
	
	/**
	 * Instantiates a new hlt content container.
	 */
	public HltContentContainer() {
        this.eventMentions = ImmutableList.of();
        this.eventTexts = ImmutableList.of();
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
		this.namedEntities = namedEntities;
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
		this.entityMentions = entityMentions;
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
		this.opinions = opinions;
	}

	/**
	 * Gets the posts.
	 * 
	 * @return the posts
	 */
	public List<Post> getPosts() {
		return posts;
	}

	/**
	 * Sets the posts.
	 * 
	 * @param posts
	 *            the new posts
	 */
	public void setPosts(List<Post> posts) {
		this.posts = posts;
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
		this.prosodicPhrases = prosodicPhrases;
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
		this.sarcasms = sarcasms;
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
		this.eventMentions = new ArrayList(eventMentions);
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
		this.sentences = sentences;
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
		this.relations = relations;
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
		this.coreferences = coreferences;
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
		this.passages = passages;
	}

	/**
	 * Gets the messages.
	 * 
	 * @return the messages
	 */
	public List<Message> getMessages() {
		return messages;
	}

	/**
	 * Sets the messages.
	 *
	 * @param messages the new messages
	 * @meram messages
	 * the new messages
	 */
	public void setMessages(List<Message> messages) {
		this.messages = messages;
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
		this.jointRelationCoreferences = jointRelationCoreferences;
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
		this.partOfSpeechs = partOfSpeechs;
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
		this.syntacticChunks = syntacticChunks;
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
		this.taggedChunks = taggedChunks;
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
		this.dependencies = dependencies;
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
		this.committedBeliefs = committedBeliefs;
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
		this.utterances = utterances;
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
		this.interPausalUnits = interPausalUnits;
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
		this.sessions = sessions;
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
                this.triples = triples;
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
                this.paraphrases = paraphrases;
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
		this.eventRelations = eventRelations;
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
		this.timePhrases = timePhrases;
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
		} else if (this.posts!=null && this.posts.size() > 0) {
			document = this.posts.get(0).tokenStream.getDocument();
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
		this.sentenceSimilarities = sentenceSimilarities;
	}
	

}
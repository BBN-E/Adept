/*******************************************************************************
 * Raytheon BBN Technologies Corp., March 2013
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2013 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/*
 * 
 */
package adept.mappers.handlers;

import thrift.adept.common.*;

import java.util.List;


/**
 * The Class HltContentContainer.
 */
public class HltContentContainerHandler extends HltContentHandler implements HltContentContainerService.Iface {

	private HltContentContainer myHltContentContainer;


	/**
	 * Instantiates a new hlt content container.
	 */
	public HltContentContainerHandler() {
		super();
		myHltContentContainer = new HltContentContainer();
		myHltContentContainer.id = myItem.id;
		myHltContentContainer.value = myItem.value;

	}

	/**
	 * Gets the named entities.
	 * 
	 * @return myHltContentContainer.the named entities
	 */
	public List<EntityMention> getNamedEntities() {
		return myHltContentContainer.namedEntities;
	}

	/**
	 * Sets the named entities.
	 * 
	 * @param namedEntities
	 *            the new named entities
	 */
	public void setNamedEntities(List<EntityMention> namedEntities) {
		myHltContentContainer.namedEntities = namedEntities;
	}

	/**
	 * Gets the entity mentions.
	 * 
	 * @return myHltContentContainer.the entity mentions
	 */
	public List<EntityMention> getEntityMentions() {
		return myHltContentContainer.entityMentions;
	}

	/**
	 * Sets the entity mentions.
	 * 
	 * @param entityMentions
	 *            the new entity mentions
	 */
	public void setEntityMentions(List<EntityMention> entityMentions) {
		myHltContentContainer.entityMentions = entityMentions;
	}

	/**
	 * Gets the opinions.
	 * 
	 * @return myHltContentContainer.the opinions
	 */
	public List<Opinion> getOpinions() {
		return myHltContentContainer.opinions;
	}

	/**
	 * Sets the opinions.
	 * 
	 * @param opinions
	 *            the new opinions
	 */
	public void setOpinions(List<Opinion> opinions) {
		myHltContentContainer.opinions = opinions;
	}

	/**
	 * Gets the prosodic phrases.
	 * 
	 * @return myHltContentContainer.the prosodic phrases
	 */
	public List<ProsodicPhrase> getProsodicPhrases() {
		return myHltContentContainer.prosodicPhrases;
	}

	/**
	 * Sets the prosodic phrases.
	 * 
	 * @param prosodicPhrases
	 *            the new prosodic phrases
	 */
	public void setProsodicPhrases(List<ProsodicPhrase> prosodicPhrases) {
		myHltContentContainer.prosodicPhrases = prosodicPhrases;
	}

	/**
	 * Gets the sarcasms.
	 * 
	 * @return myHltContentContainer.the sarcasms
	 */
	public List<Sarcasm> getSarcasms() {
		return myHltContentContainer.sarcasms;
	}

	/**
	 * Sets the sarcasms.
	 * 
	 * @param sarcasms
	 *            the new sarcasms
	 */
	public void setSarcasms(List<Sarcasm> sarcasms) {
		myHltContentContainer.sarcasms = sarcasms;
	}

	/**
	 * Gets the sentences.
	 * 
	 * @return myHltContentContainer.the sentences
	 */
	public List<Sentence> getSentences() {
		return myHltContentContainer.sentences;
	}

	/**
	 * Sets the sentences.
	 * 
	 * @param sentences
	 *            the new sentences
	 */
	public void setSentences(List<Sentence> sentences) {
		myHltContentContainer.sentences = sentences;
	}

	/**
	 * Gets the mentions.
	 * 
	 * @return myHltContentContainer.the mentions
	 */
	public List<EntityMention> getMentions() {
		return myHltContentContainer.entityMentions;
	}

	/**
	 * Sets the mentions.
	 * 
	 * @param entityMentions
	 *            the new mentions
	 */
	public void setMentions(List<EntityMention> entityMentions) {
		myHltContentContainer.entityMentions = entityMentions;
	}

	/**
	 * Gets the relations.
	 * 
	 * @return myHltContentContainer.the relations
	 */
	public List<Relation> getRelations() {
		return myHltContentContainer.relations;
	}

	/**
	 * Sets the relations.
	 * 
	 * @param relations
	 *            the new relations
	 */
	public void setRelations(List<Relation> relations) {
		myHltContentContainer.relations = relations;
	}

	/**
	 * Gets the coreferences.
	 * 
	 * @return myHltContentContainer.the coreferences
	 */
	public List<Coreference> getCoreferences() {
		return myHltContentContainer.coreferences;
	}

	/**
	 * Sets the coreferences.
	 * 
	 * @param coreferences
	 *            the new coreferences
	 */
	public void setCoreferences(List<Coreference> coreferences) {
		myHltContentContainer.coreferences = coreferences;
	}

	/**
	 * Gets the passages.
	 * 
	 * @return myHltContentContainer.the passages
	 */
	public List<Passage> getPassages() {
		return myHltContentContainer.passages;
	}

	/**
	 * Sets the passages.
	 * 
	 * @param passages
	 *            the new passages
	 */
	public void setPassages(List<Passage> passages) {
		myHltContentContainer.passages = passages;
	}

	/**
	 * Gets the joint relation coreferences.
	 * 
	 * @return myHltContentContainer.the joint relation coreferences
	 */
	public List<JointRelationCoreference> getJointRelationCoreferences() {
		return myHltContentContainer.jointRelationCoreferences;
	}

	/**
	 * Sets the joint relation coreferences.
	 * 
	 * @param jointRelationCoreferences
	 *            the new joint relation coreferences
	 */
	public void setJointRelationCoreferences(
			List<JointRelationCoreference> jointRelationCoreferences) {
		myHltContentContainer.jointRelationCoreferences = jointRelationCoreferences;
	}

	/**
	 * Gets the part of speechs.
	 * 
	 * @return myHltContentContainer.the part of speechs
	 */
	public List<PartOfSpeech> getPartOfSpeechs() {
		return myHltContentContainer.partOfSpeechs;
	}

	/**
	 * Sets the part of speechs.
	 * 
	 * @param partOfSpeechs
	 *            the new part of speechs
	 */
	public void setPartOfSpeechs(List<PartOfSpeech> partOfSpeechs) {
		myHltContentContainer.partOfSpeechs = partOfSpeechs;
	}

	/**
	 * Gets the syntactic chunks.
	 * 
	 * @return myHltContentContainer.the syntactic chunks
	 */
	public List<SyntacticChunk> getSyntacticChunks() {
		return myHltContentContainer.syntacticChunks;
	}

	/**
	 * Sets the syntactic chunks.
	 * 
	 * @param syntacticChunks
	 *            the new syntactic chunks
	 */
	public void setSyntacticChunks(List<SyntacticChunk> syntacticChunks) {
		myHltContentContainer.syntacticChunks = syntacticChunks;
	}

	/**
	 * Gets the dependencies.
	 * 
	 * @return myHltContentContainer.the dependencies
	 */
	public List<Dependency> getDependencies() {
		return myHltContentContainer.dependencies;
	}

	/**
	 * Sets the dependencies.
	 * 
	 * @param dependencies
	 *            the new dependencies
	 */
	public void setDependencies(List<Dependency> dependencies) {
		myHltContentContainer.dependencies = dependencies;
	}

	/**
	 * Gets the committed beliefs.
	 *
	 * @return myHltContentContainer.the committed beliefs
	 */
	public List<CommittedBelief> getCommittedBeliefs() {
		return myHltContentContainer.committedBeliefs;
	}

	/**
	 * Sets the committed beliefs.
	 *
	 * @param committedBeliefs the new committed beliefs
	 */
	public void setCommittedBeliefs(List<CommittedBelief> committedBeliefs) {
		myHltContentContainer.committedBeliefs = committedBeliefs;
	}
	
	/**
	 * Gets the utterances.
	 *
	 * @return myHltContentContainer.the utterances
	 */
//	public List<Utterance> getUtterances() {
//		return myHltContentContainer.utterances;
//	}
	
	/**
	 * Sets the utterances.
	 *
	 * @param utterances the utterances
	 */
//	public void setUtterances(List<Utterance> utterances) {
//		myHltContentContainer.utterances = utterances;
//	}

	/**
	 * Gets the inter pausal units.
	 *
	 * @return myHltContentContainer.the inter pausal units
	 */
//	public List<InterPausalUnit> getInterPausalUnits() {
//		return myHltContentContainer.interPausalUnits;
//	}

	/**
	 * Sets the inter pausal units.
	 *
	 * @param interPausalUnits the new inter pausal units
	 */
//	public void setInterPausalUnits(List<InterPausalUnit> interPausalUnits) {
//		myHltContentContainer.interPausalUnits = interPausalUnits;
//	}

	public HltContentContainer getHltContentContainer() {
		return myHltContentContainer;
	}

}

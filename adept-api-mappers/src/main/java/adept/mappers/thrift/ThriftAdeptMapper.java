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

/*******************************************************************************
<<<<<<< HEAD
 * Raytheon BBN Technologies Corp., October 2013
=======
 * Raytheon BBN Technologies Corp., September 2013
>>>>>>> origin/staging
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

package adept.mappers.thrift;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;

import org.dozer.DozerBeanMapper;

public class ThriftAdeptMapper {

	private static DozerBeanMapper mapper;

	public ThriftAdeptMapper(String path) {
		String file = "file:" + path;
		mapper = new DozerBeanMapper(Arrays.asList(new String[]{file}));
	}

	public ThriftAdeptMapper() {
		
		mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
	}
	
	public thrift.adept.common.AnomalousText convert(adept.common.AnomalousText anomalousText) {
		thrift.adept.common.AnomalousText retAnomalousText = mapper.map(anomalousText, thrift.adept.common.AnomalousText.class);
		return retAnomalousText;
	}

	public thrift.adept.common.Argument convert(adept.common.Argument argument) {
		thrift.adept.common.Argument retArgument = mapper.map(argument, thrift.adept.common.Argument.class);
		return retArgument;
	}

	public thrift.adept.common.AudioOffset convert(adept.common.AudioOffset audioOffset) {
		thrift.adept.common.AudioOffset retAudioOffset = mapper.map(audioOffset, thrift.adept.common.AudioOffset.class);
		return retAudioOffset;
	}

	public thrift.adept.common.CharOffset convert(adept.common.CharOffset charOffset) {
		thrift.adept.common.CharOffset retCharOffset = mapper.map(charOffset, thrift.adept.common.CharOffset.class);
		return retCharOffset;
	}

	public thrift.adept.common.Chunk convert(adept.common.Chunk chunk) {
		thrift.adept.common.Chunk retChunk = mapper.map(chunk, thrift.adept.common.Chunk.class);
		return retChunk;
	}

	public thrift.adept.common.CommittedBelief convert(adept.common.CommittedBelief committedBelief) {
		thrift.adept.common.CommittedBelief retCommittedBelief = mapper.map(committedBelief, thrift.adept.common.CommittedBelief.class);
		retCommittedBelief.setTokenStream(convert(committedBelief.getTokenStream()));
		return retCommittedBelief;
	}
/*
	public thrift.adept.common.Conversation convert(adept.common.Conversation conversation) {
		thrift.adept.common.Conversation retConversation = mapper.map(conversation, thrift.adept.common.Conversation.class);
		return retConversation;
	}
*/
	public thrift.adept.common.Coreference convert(adept.common.Coreference coreference) {
		thrift.adept.common.Coreference retCoreference = mapper.map(coreference, thrift.adept.common.Coreference.class);
		return retCoreference;
	}

	public thrift.adept.common.Corpus convert(adept.common.Corpus corpus) {
		thrift.adept.common.Corpus retCorpus = mapper.map(corpus, thrift.adept.common.Corpus.class);
		return retCorpus;
	}

	public thrift.adept.common.Dependency convert(adept.common.Dependency dependency) {
		thrift.adept.common.Dependency retDependency = mapper.map(dependency, thrift.adept.common.Dependency.class);
		thrift.adept.common.ChunkUnion dependent = new thrift.adept.common.ChunkUnion();
		dependent.setChunk(mapper.map(dependency.getDependent(), thrift.adept.common.Chunk.class));
		retDependency.setGovernor(dependent);
		thrift.adept.common.ChunkUnion governor = new thrift.adept.common.ChunkUnion();
		governor.setChunk(mapper.map(dependency.getGovernor(), thrift.adept.common.Chunk.class));
		retDependency.setGovernor(governor);
		return retDependency;
	}

	public thrift.adept.common.DiscourseUnit convert(adept.common.DiscourseUnit discourseUnit) {
		thrift.adept.common.DiscourseUnit retDiscourseUnit = mapper.map(discourseUnit, thrift.adept.common.DiscourseUnit.class);
		retDiscourseUnit.setTokenStream(convert(discourseUnit.getTokenStream()));
		return retDiscourseUnit;
	}

	public thrift.adept.common.Document convert(adept.common.Document document) {
		thrift.adept.common.Document retDocument = mapper.map(document, thrift.adept.common.Document.class);
		return retDocument;
	}

	public thrift.adept.common.DocumentList convert(adept.common.DocumentList documentList) {
		thrift.adept.common.DocumentList retDocumentList = mapper.map(documentList, thrift.adept.common.DocumentList.class);
		return retDocumentList;
	}

	public thrift.adept.common.Entailment convert(adept.common.Entailment entailment) {
		thrift.adept.common.Entailment retEntailment = mapper.map(entailment, thrift.adept.common.Entailment.class);
		return retEntailment;
	}

	public thrift.adept.common.Entity convert(adept.common.Entity entity) {
		thrift.adept.common.Entity retEntity = mapper.map(entity, thrift.adept.common.Entity.class);
		return retEntity;
	}

	public thrift.adept.common.EntityMention convert(adept.common.EntityMention entityMention) {
		thrift.adept.common.EntityMention retEntityMention = mapper.map(entityMention, thrift.adept.common.EntityMention.class);
		retEntityMention.setTokenStream(convert(entityMention.getTokenStream()));
		return retEntityMention;
	}

	public thrift.adept.common.HltContent convert(adept.common.HltContent hltContent) {
		thrift.adept.common.HltContent retHltContent = mapper.map(hltContent, thrift.adept.common.HltContent.class);
		return retHltContent;
	}

	public thrift.adept.common.HltContentContainer convert(adept.common.HltContentContainer hltContentContainer) {
		thrift.adept.common.HltContentContainer retHltContentContainer = mapper.map(hltContentContainer, thrift.adept.common.HltContentContainer.class);
		return retHltContentContainer;
	}

	public thrift.adept.common.HltContentContainerList convert(adept.common.HltContentContainerList hltContentContainerList) {
		thrift.adept.common.HltContentContainerList retHltContentContainerList = mapper.map(hltContentContainerList, thrift.adept.common.HltContentContainerList.class);
		return retHltContentContainerList;
	}

	public thrift.adept.common.ID convert(adept.common.ID id) {
		thrift.adept.common.ID retID = mapper.map(id, thrift.adept.common.ID.class);
		return retID;
	}

	public thrift.adept.common.InterPausalUnit convert(adept.common.InterPausalUnit interPausalUnit) {
		thrift.adept.common.InterPausalUnit retInterPausalUnit = mapper.map(interPausalUnit, thrift.adept.common.InterPausalUnit.class);
		return retInterPausalUnit;
	}

	public thrift.adept.common.Item convert(adept.common.Item item) {
		thrift.adept.common.Item retItem = mapper.map(item, thrift.adept.common.Item.class);
		return retItem;
	}

	public thrift.adept.common.JointRelationCoreference convert(adept.common.JointRelationCoreference jointRelationCoreference) {
		thrift.adept.common.JointRelationCoreference retJointRelationCoreference = mapper.map(jointRelationCoreference, thrift.adept.common.JointRelationCoreference.class);
		return retJointRelationCoreference;
	}

	public thrift.adept.common.Opinion convert(adept.common.Opinion opinion) {
		thrift.adept.common.Opinion retOpinion = mapper.map(opinion, thrift.adept.common.Opinion.class);
		retOpinion.setTokenStream(convert(opinion.getTokenStream()));
		return retOpinion;
	}

	public thrift.adept.common.Paraphrase convert(adept.common.Paraphrase paraphrase) {
		thrift.adept.common.Paraphrase retParaphrase = mapper.map(paraphrase, thrift.adept.common.Paraphrase.class);
		return retParaphrase;
	}

	public thrift.adept.common.PartOfSpeech convert(adept.common.PartOfSpeech partOfSpeech) {
		thrift.adept.common.PartOfSpeech retPartOfSpeech= mapper.map(partOfSpeech, thrift.adept.common.PartOfSpeech.class);
		retPartOfSpeech.setTokenStream(convert(partOfSpeech.getTokenStream()));
		return retPartOfSpeech;
	}

	public thrift.adept.common.Passage convert(adept.common.Passage passage) {
		thrift.adept.common.Passage retPassage= mapper.map(passage, thrift.adept.common.Passage.class);
		retPassage.setTokenStream(convert(passage.getTokenStream()));
		return retPassage;
	}

	public thrift.adept.common.ProsodicPhrase convert(adept.common.ProsodicPhrase prosodicPhrase) {
		thrift.adept.common.ProsodicPhrase retProsodicPhrase= mapper.map(prosodicPhrase, thrift.adept.common.ProsodicPhrase.class);
		retProsodicPhrase.setTokenStream(convert(prosodicPhrase.getTokenStream()));
		return retProsodicPhrase;
	}

	public thrift.adept.common.Relation convert(adept.common.Relation relation) {
		thrift.adept.common.Relation retRelation = mapper.map(relation, thrift.adept.common.Relation.class);
		return retRelation;
	}

	public thrift.adept.common.Sarcasm convert(adept.common.Sarcasm sarcasm) {
		thrift.adept.common.Sarcasm retSarcasm = mapper.map(sarcasm, thrift.adept.common.Sarcasm.class);
		retSarcasm.setTokenStream(convert(sarcasm.getTokenStream()));
		return retSarcasm;
	}

	public thrift.adept.common.Sentence convert(adept.common.Sentence sentence) {
		thrift.adept.common.Sentence retSentence = mapper.map(sentence, thrift.adept.common.Sentence.class);
		retSentence.setTokenStream(convert(sentence.getTokenStream()));
		return retSentence;
	}

	public thrift.adept.common.SentenceSimilarity convert(adept.common.SentenceSimilarity sentenceSimilarity) {
		thrift.adept.common.SentenceSimilarity retSentenceSimilarity = mapper.map(sentenceSimilarity, thrift.adept.common.SentenceSimilarity.class);
		return retSentenceSimilarity;
	}

	public thrift.adept.common.Slot convert(adept.common.Slot slot) {
		thrift.adept.common.Slot retSlot = mapper.map(slot, thrift.adept.common.Slot.class);
		return retSlot;
	}

	public thrift.adept.common.Story convert(adept.common.Story story) {
		thrift.adept.common.Story retStory = mapper.map(story, thrift.adept.common.Story.class);
		retStory.setTokenStream(convert(story.getTokenStream()));
		return retStory;
	}

	public thrift.adept.common.SyntacticChunk convert(adept.common.SyntacticChunk syntacticChunk) {
		thrift.adept.common.SyntacticChunk retSyntacticChunk = mapper.map(syntacticChunk, thrift.adept.common.SyntacticChunk.class);
		return retSyntacticChunk;
	}

	public thrift.adept.common.Token convert(adept.common.Token token) {
		thrift.adept.common.Token retToken = mapper.map(token, thrift.adept.common.Token.class);
		return retToken;
	}

	public thrift.adept.common.TokenOffset convert(adept.common.TokenOffset tokenOffset) {
		thrift.adept.common.TokenOffset retTokenOffset = mapper.map(tokenOffset, thrift.adept.common.TokenOffset.class);
		return retTokenOffset;
	}

	public thrift.adept.common.TokenStream convert(adept.common.TokenStream tokenStream) {
		thrift.adept.common.TokenStream retTokenStream = mapper.map(tokenStream, thrift.adept.common.TokenStream.class);	

		List<thrift.adept.common.Token> tokenList = new ArrayList<thrift.adept.common.Token>();

		for (int i=0; i<tokenStream.size(); i++) {
			adept.common.Token token = tokenStream.get(i);
			thrift.adept.common.Token thriftToken = convert(token);
			tokenList.add(thriftToken);
		}

		retTokenStream.setTokenList(tokenList);

		return retTokenStream;
	}

	public thrift.adept.common.Topic convert(adept.common.Topic topic) {
		thrift.adept.common.Topic retTopic = mapper.map(topic, thrift.adept.common.Topic.class);
		return retTopic;
	}

	public thrift.adept.common.Translation convert(adept.common.Translation translation) {
		thrift.adept.common.Translation retTranslation = mapper.map(translation, thrift.adept.common.Translation.class);
		thrift.adept.common.ChunkUnion sourceChunk = new thrift.adept.common.ChunkUnion();
		sourceChunk.setChunk(mapper.map(translation.getSourceChunk(), thrift.adept.common.Chunk.class));
		retTranslation.setSourceChunk(sourceChunk);
		thrift.adept.common.ChunkUnion targetChunk = new thrift.adept.common.ChunkUnion();
		targetChunk.setChunk(mapper.map(translation.getTargetChunk(), thrift.adept.common.Chunk.class));
		retTranslation.setTargetChunk(targetChunk);
		return retTranslation;
	}

	public thrift.adept.common.Triple convert(adept.common.Triple triple) {
		thrift.adept.common.Triple retTriple = mapper.map(triple, thrift.adept.common.Triple.class);
		return retTriple;
	}

	public thrift.adept.common.Type convert(adept.common.Type type) {
		thrift.adept.common.Type retType = mapper.map(type, thrift.adept.common.Type.class);
		return retType;
	}

	public thrift.adept.common.Utterance convert(adept.common.Utterance utterance) {
		thrift.adept.common.Utterance retUtterance = mapper.map(utterance, thrift.adept.common.Utterance.class);
		retUtterance.setTokenStream(convert(utterance.getTokenStream()));
		return retUtterance;
	}

	public thrift.adept.common.Value convert(adept.common.Value value) {
		thrift.adept.common.Value retValue = mapper.map(value, thrift.adept.common.Value.class);
		return retValue;
	}

	public thrift.adept.common.Viewpoint convert(adept.common.Viewpoint viewpoint) {
		thrift.adept.common.Viewpoint retViewpoint = mapper.map(viewpoint, thrift.adept.common.Viewpoint.class);
		return retViewpoint;
	}

	public adept.common.AnomalousText convert(thrift.adept.common.AnomalousText anomalousText) {
		adept.common.AnomalousText retAnomalousText = mapper.map(anomalousText, adept.common.AnomalousText.class);
		return retAnomalousText;
	}

	public adept.common.Argument convert(thrift.adept.common.Argument argument) {
		adept.common.Argument retArgument = mapper.map(argument, adept.common.Argument.class);
		return retArgument;
	}

	public adept.common.AudioOffset convert(thrift.adept.common.AudioOffset audioOffset) {
		adept.common.AudioOffset retAudioOffset = mapper.map(audioOffset, adept.common.AudioOffset.class);
		return retAudioOffset;
	}

	public adept.common.CharOffset convert(thrift.adept.common.CharOffset charOffset) {
		adept.common.CharOffset retCharOffset = mapper.map(charOffset, adept.common.CharOffset.class);
		return retCharOffset;
	}

	public adept.common.Chunk convert(thrift.adept.common.Chunk chunk) {
		adept.common.Chunk retChunk = mapper.map(chunk, adept.common.Chunk.class);
		return retChunk;
	}

	public adept.common.CommittedBelief convert(thrift.adept.common.CommittedBelief committedBelief) {
		adept.common.CommittedBelief retCommittedBelief = mapper.map(committedBelief, adept.common.CommittedBelief.class);
		retCommittedBelief.setTokenStream(convert(committedBelief.getTokenStream()));
		return retCommittedBelief;
	}
/*
	public adept.common.Conversation convert(thrift.adept.common.Conversation conversation) {
		adept.common.Conversation retConversation = mapper.map(conversation, adept.common.Conversation.class);
		return retConversation;
	}
*/
	public adept.common.Coreference convert(thrift.adept.common.Coreference coreference) {
		adept.common.Coreference retCoreference = mapper.map(coreference, adept.common.Coreference.class);
		return retCoreference;
	}

	public adept.common.Corpus convert(thrift.adept.common.Corpus corpus) {
		adept.common.Corpus retCorpus = mapper.map(corpus, adept.common.Corpus.class);
		return retCorpus;
	}

	public adept.common.Dependency convert(thrift.adept.common.Dependency dependency) {
		adept.common.Chunk dependent = mapper.map(dependency.getDependent().getChunk(), adept.common.Chunk.class);
		adept.common.Chunk governor = mapper.map(dependency.getGovernor().getChunk(), adept.common.Chunk.class);
		adept.common.Dependency retDependency = new adept.common.Dependency(governor, dependent, dependency.getDependencyType());
		return retDependency;
	}

	public adept.common.DiscourseUnit convert(thrift.adept.common.DiscourseUnit discourseUnit) {
		adept.common.DiscourseUnit retDiscourseUnit = mapper.map(discourseUnit, adept.common.DiscourseUnit.class);
		retDiscourseUnit.setTokenStream(convert(discourseUnit.getTokenStream()));
		return retDiscourseUnit;
	}

	public adept.common.Document convert(thrift.adept.common.Document document) {
		adept.common.Document retDocument = mapper.map(document, adept.common.Document.class);
		return retDocument;
	}

	public adept.common.DocumentList convert(thrift.adept.common.DocumentList documentList) {
		adept.common.DocumentList retDocumentList = mapper.map(documentList, adept.common.DocumentList.class);
		return retDocumentList;
	}

	public adept.common.Entailment convert(thrift.adept.common.Entailment entailment) {
		adept.common.Entailment retEntailment = mapper.map(entailment, adept.common.Entailment.class);
		retEntailment.setJudgmentDistribution(new adept.common.BoundedList<adept.common.Pair<adept.common.Entailment.Judgment, Float>>(entailment.getJudgmentDistribution().size()));
		for (thrift.adept.common.EntailmentJudgment key : entailment.getJudgmentDistribution().keySet()) {
			retEntailment.addJudgmentConfidencePair(mapper.map(key, adept.common.Entailment.Judgment.class), (entailment.getJudgmentDistribution().get(key).floatValue()));
		}
		return retEntailment;
	}

	public adept.common.Entity convert(thrift.adept.common.Entity entity) {
		adept.common.Entity retEntity = mapper.map(entity, adept.common.Entity.class);
		return retEntity;
	}

	public adept.common.EntityMention convert(thrift.adept.common.EntityMention entityMention) {
		adept.common.EntityMention retEntityMention = mapper.map(entityMention, adept.common.EntityMention.class);
		retEntityMention.setTokenStream(convert(entityMention.getTokenStream()));
		return retEntityMention;
	}

	public adept.common.HltContent convert(thrift.adept.common.HltContent hltContent) {
		adept.common.HltContent retHltContent = mapper.map(hltContent, adept.common.HltContent.class);
		return retHltContent;
	}

	public adept.common.HltContentContainer convert(thrift.adept.common.HltContentContainer hltContentContainer) {
		adept.common.HltContentContainer retHltContentContainer = mapper.map(hltContentContainer, adept.common.HltContentContainer.class);
		return retHltContentContainer;
	}

	public adept.common.HltContentContainerList convert(thrift.adept.common.HltContentContainerList hltContentContainerList) {
		adept.common.HltContentContainerList retHltContentContainerList = mapper.map(hltContentContainerList, adept.common.HltContentContainerList.class);
		return retHltContentContainerList;
	}

	public adept.common.ID convert(thrift.adept.common.ID id) {
		adept.common.ID retID = mapper.map(id, adept.common.ID.class);
		return retID;
	}

	public adept.common.InterPausalUnit convert(thrift.adept.common.InterPausalUnit interPausalUnit) {
		adept.common.InterPausalUnit retInterPausalUnit = mapper.map(interPausalUnit, adept.common.InterPausalUnit.class);
		return retInterPausalUnit;
	}

	public adept.common.Item convert(thrift.adept.common.Item item) {
		adept.common.Item retItem = mapper.map(item, adept.common.Item.class);
		return retItem;
	}

	public adept.common.JointRelationCoreference convert(thrift.adept.common.JointRelationCoreference jointRelationCoreference) {
		adept.common.JointRelationCoreference retJointRelationCoreference = mapper.map(jointRelationCoreference, adept.common.JointRelationCoreference.class);
		return retJointRelationCoreference;
	}

	public adept.common.Opinion convert(thrift.adept.common.Opinion opinion) {
		adept.common.Opinion retOpinion = mapper.map(opinion, adept.common.Opinion.class);
		retOpinion.setTokenStream(convert(opinion.getTokenStream()));
		return retOpinion;
	}

	public adept.common.Paraphrase convert(thrift.adept.common.Paraphrase paraphrase) {
		adept.common.Paraphrase retParaphrase = mapper.map(paraphrase, adept.common.Paraphrase.class);
		return retParaphrase;
	}

	public adept.common.PartOfSpeech convert(thrift.adept.common.PartOfSpeech partOfSpeech) {
		adept.common.PartOfSpeech retPartOfSpeech= mapper.map(partOfSpeech, adept.common.PartOfSpeech.class);
		retPartOfSpeech.setTokenStream(convert(partOfSpeech.getTokenStream()));
		return retPartOfSpeech;
	}

	public adept.common.Passage convert(thrift.adept.common.Passage passage) {
		adept.common.Passage retPassage= mapper.map(passage, adept.common.Passage.class);
		retPassage.setTokenStream(convert(passage.getTokenStream()));
		return retPassage;
	}

	public adept.common.ProsodicPhrase convert(thrift.adept.common.ProsodicPhrase prosodicPhrase) {
		adept.common.ProsodicPhrase retProsodicPhrase= mapper.map(prosodicPhrase, adept.common.ProsodicPhrase.class);
		retProsodicPhrase.setTokenStream(convert(prosodicPhrase.getTokenStream()));
		return retProsodicPhrase;
	}

	public adept.common.Relation convert(thrift.adept.common.Relation relation) {
		adept.common.Relation retRelation = mapper.map(relation, adept.common.Relation.class);
		return retRelation;
	}

	public adept.common.Sarcasm convert(thrift.adept.common.Sarcasm sarcasm) {
		adept.common.Sarcasm retSarcasm = mapper.map(sarcasm, adept.common.Sarcasm.class);
		retSarcasm.setTokenStream(convert(sarcasm.getTokenStream()));
		return retSarcasm;
	}

	public adept.common.Sentence convert(thrift.adept.common.Sentence sentence) {
		adept.common.Sentence retSentence = mapper.map(sentence, adept.common.Sentence.class);
		retSentence.setTokenStream(convert(sentence.getTokenStream()));
		return retSentence;
	}

	public adept.common.SentenceSimilarity convert(thrift.adept.common.SentenceSimilarity sentenceSimilarity) {
		adept.common.SentenceSimilarity retSentenceSimilarity = mapper.map(sentenceSimilarity, adept.common.SentenceSimilarity.class);
		return retSentenceSimilarity;
	}

	public adept.common.Slot convert(thrift.adept.common.Slot slot) {
		adept.common.Slot retSlot = mapper.map(slot, adept.common.Slot.class);
		return retSlot;
	}

	public adept.common.Story convert(thrift.adept.common.Story story) {
		adept.common.Story retStory = mapper.map(story, adept.common.Story.class);
		retStory.setTokenStream(convert(story.getTokenStream()));
		return retStory;
	}

	public adept.common.SyntacticChunk convert(thrift.adept.common.SyntacticChunk syntacticChunk) {
		adept.common.SyntacticChunk retSyntacticChunk = mapper.map(syntacticChunk, adept.common.SyntacticChunk.class);
		return retSyntacticChunk;
	}

	public adept.common.Token convert(thrift.adept.common.Token token) {
		adept.common.Token retToken = mapper.map(token, adept.common.Token.class);
		return retToken;
	}

	public adept.common.TokenOffset convert(thrift.adept.common.TokenOffset tokenOffset) {
		adept.common.TokenOffset retTokenOffset = mapper.map(tokenOffset, adept.common.TokenOffset.class);
		return retTokenOffset;
	}

	public adept.common.TokenStream convert(thrift.adept.common.TokenStream tokenStream) {
		adept.common.TokenStream retTokenStream = mapper.map(tokenStream, adept.common.TokenStream.class);

		List<thrift.adept.common.Token> tokenList = tokenStream.getTokenList();

		for (int i=0; i<tokenList.size(); i++) {
			thrift.adept.common.Token token = tokenList.get(i);
			adept.common.Token adeptToken = convert(token);
			retTokenStream.add(adeptToken);
		}

		return retTokenStream;
	}

	public adept.common.Topic convert(thrift.adept.common.Topic topic) {
		adept.common.Topic retTopic = mapper.map(topic, adept.common.Topic.class);
		return retTopic;
	}

	public adept.common.Translation convert(thrift.adept.common.Translation translation) {
		adept.common.Chunk sourceChunk = mapper.map(translation.getSourceChunk().getChunk(), adept.common.Chunk.class);
		adept.common.Chunk targetChunk = mapper.map(translation.getTargetChunk().getChunk(), adept.common.Chunk.class);
		adept.common.Translation retTranslation = new adept.common.Translation(sourceChunk, targetChunk);
		return retTranslation;
	}

	public adept.common.Triple convert(thrift.adept.common.Triple triple) {
		adept.common.Triple retTriple = mapper.map(triple, adept.common.Triple.class);
		return retTriple;
	}

	public adept.common.Type convert(thrift.adept.common.Type type) {
		adept.common.Type retType = mapper.map(type, adept.common.Type.class);
		return retType;
	}

	public adept.common.Utterance convert(thrift.adept.common.Utterance utterance) {
		adept.common.Utterance retUtterance = mapper.map(utterance, adept.common.Utterance.class);
		retUtterance.setTokenStream(convert(utterance.getTokenStream()));
		return retUtterance;
	}

	public adept.common.Value convert(thrift.adept.common.Value value) {
		adept.common.Value retValue = mapper.map(value, adept.common.Value.class);
		return retValue;
	}

	public adept.common.Viewpoint convert(thrift.adept.common.Viewpoint viewpoint) {
		adept.common.Viewpoint retViewpoint = mapper.map(viewpoint, adept.common.Viewpoint.class);
		return retViewpoint;
	}
}
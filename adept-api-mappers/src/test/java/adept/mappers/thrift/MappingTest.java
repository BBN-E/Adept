/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

package adept.mappers.thrift;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.io.PrintWriter;
import java.io.IOException;
import adept.mappers.thrift.ThriftAdeptMapper;
  
public class MappingTest {

    public static void main(String[] args) {

	ThriftAdeptMapper mapper = new ThriftAdeptMapper("/d4m/deft/users/cstokes/deft/adept/adept-api-mappers/src/main/resources/adept/mappers/thrift/ThriftAdeptMappings.xml");
	
	try {
	PrintWriter writer = new PrintWriter("./mapping_test_out.txt", "UTF-8");

	writer.println("Testing Adept to Thrift Conversion...");
	System.out.println("Testing Adept to Thrift Conversion...");

	adept.common.Corpus corpusAdept = new adept.common.Corpus("Corpus001", "AToTType", "AToT Corpus", "AtoT uri");

	adept.common.Document documentAdept = new adept.common.Document("Doc001", corpusAdept, "AToTDocType", "AToTDoc uri", "AToT English");
	//documentAdept.setAudioUri("ATtoTDoc audio uri");
	documentAdept.setGenre("AToT genre");
	documentAdept.setHeadline("AToT headlne");
	documentAdept.setValue("Token1 Token2");
	
	adept.common.AnomalousText anomalousTextAdept = new adept.common.AnomalousText((float)0.1, documentAdept);
	anomalousTextAdept.setExplanation("AToT explanation");

	adept.common.AudioOffset audioOffsetAdept = new adept.common.AudioOffset((float)0, (float)6);

	adept.common.CharOffset charOffsetAdept = new adept.common.CharOffset(0, 6);

	adept.common.TokenOffset tokenOffsetAdept = new adept.common.TokenOffset(0, 1);

	adept.common.Token token1Adept = new adept.common.Token(1, charOffsetAdept, "Token1");
	token1Adept.setConfidence((float)0.1);
	token1Adept.setLemma("AToT lemma 1");
	token1Adept.setAudioOffset(audioOffsetAdept);

	adept.common.Token token2Adept = new adept.common.Token(2, charOffsetAdept, "Token2");
	token2Adept.setConfidence((float)0.2);
	token2Adept.setLemma("AToT lemma 2");
	token2Adept.setAudioOffset(audioOffsetAdept);

	adept.common.TokenStream tokenStreamAdept = new adept.common.TokenStream(adept.common.TokenizerType.ADEPT, adept.common.TranscriptType.SOURCE, "AToT English", adept.common.ChannelName.MONO, adept.common.ContentType.TEXT, documentAdept);
	tokenStreamAdept.setAsrName(adept.common.AsrName.BBN);
//	tokenStreamAdept.setDocument(documentAdept);
	tokenStreamAdept.setSpeechUnit(adept.common.SpeechUnit.NONE);
	tokenStreamAdept.setTranslatorName(adept.common.TranslatorName.NONE);
	tokenStreamAdept.add(token1Adept);
	tokenStreamAdept.add(token2Adept);
	
	adept.common.Chunk chunkAdept = new adept.common.Chunk(tokenOffsetAdept, tokenStreamAdept);
	
	adept.common.CommittedBelief committedBeliefAdept = new adept.common.CommittedBelief(1, tokenOffsetAdept, tokenStreamAdept, adept.common.Modality.ABILITY);
	
	adept.common.EntityMention entityMentionAdept = new adept.common.EntityMention(1, tokenOffsetAdept, tokenStreamAdept);
	entityMentionAdept.addEntityConfidencePair(1, (float)0.1);
	entityMentionAdept.setEntityType(new adept.common.Type("AToT Entity Type"));
	entityMentionAdept.setMentionType(new adept.common.Type("AToT Mention Type"));

	adept.common.Entity entityAdept = new adept.common.Entity(1, new adept.common.Type("AToT Entity Type"));
	entityAdept.setCanonicalMentions(entityMentionAdept);
	
	adept.common.Coreference coreferenceAdept = new adept.common.Coreference(1);
	List<adept.common.Entity> entitiesAdept = new ArrayList<adept.common.Entity>();
	entitiesAdept.add(entityAdept);
	List<adept.common.EntityMention> resolvedEntityMentionsAdept = new ArrayList<adept.common.EntityMention>();
	resolvedEntityMentionsAdept.add(entityMentionAdept);
	coreferenceAdept.setEntities(entitiesAdept);
	coreferenceAdept.setResolvedMentions(resolvedEntityMentionsAdept);

	adept.common.Dependency dependencyAdept = new adept.common.Dependency(chunkAdept, chunkAdept, "AToT dependency Type");
	
	adept.common.DiscourseUnit discourseUnitAdept = new adept.common.DiscourseUnit(tokenOffsetAdept, tokenStreamAdept, 1, "AToT Discourse Type");
	discourseUnitAdept.setNoveltyConfidence((float)0.1);
	discourseUnitAdept.setUncertaintyConfidence((float)0.1);
	
	adept.common.Passage passageAdept = new adept.common.Passage(1, tokenOffsetAdept, tokenStreamAdept);
	passageAdept.setContentType("AToT content Type");

	adept.common.Entailment entailmentAdept = new adept.common.Entailment(1);
	entailmentAdept.setHypothesis(passageAdept);
	adept.common.Pair<adept.common.Entailment.Judgment, Float> pair = new adept.common.Pair<adept.common.Entailment.Judgment, Float>(adept.common.Entailment.Judgment.ENTAILS, (float)0.1);
	adept.common.BoundedList<adept.common.Pair<adept.common.Entailment.Judgment, Float>> judgmentDistributionAdept = new adept.common.BoundedList<adept.common.Pair<adept.common.Entailment.Judgment, Float>>(1);
	judgmentDistributionAdept.add(pair);
	entailmentAdept.setJudgmentDistribution(judgmentDistributionAdept);
	entailmentAdept.setText(passageAdept);

	adept.common.Relation relationAdept = new adept.common.Relation(0,new adept.common.Type("AToT Relation Type"));
	relationAdept.setConfidence((float)0.1);

	adept.common.JointRelationCoreference jointRelationCoreferenceAdept = new adept.common.JointRelationCoreference();
	jointRelationCoreferenceAdept.setCoreference(coreferenceAdept);
	List<adept.common.Relation> relationsAdept = new ArrayList<adept.common.Relation>();
	relationsAdept.add(relationAdept);
	
	adept.common.Opinion opinionAdept = new adept.common.Opinion(0, tokenOffsetAdept, tokenStreamAdept, adept.common.Subjectivity.OBJECTIVE, adept.common.Polarity.NEUTRAL);
	
	adept.common.PartOfSpeech partOfSpeechAdept = new adept.common.PartOfSpeech(1, tokenOffsetAdept, tokenStreamAdept);
	partOfSpeechAdept.setPosTag(new adept.common.Type("AToT Pos Type"));
	
	adept.common.ProsodicPhrase prosodicPhraseAdept = new adept.common.ProsodicPhrase(tokenOffsetAdept, tokenStreamAdept, 1);
	prosodicPhraseAdept.setNoveltyConfidence((float)0.1);
	prosodicPhraseAdept.setUncertaintyConfidence((float)0.1);
	prosodicPhraseAdept.setConfidence((float)0.1);
	prosodicPhraseAdept.setType("AToT Prosodic Phrase Type");
	
	adept.common.Sarcasm sarcasmAdept = new adept.common.Sarcasm(1, tokenOffsetAdept, tokenStreamAdept, adept.common.Sarcasm.Judgment.NONE);
	sarcasmAdept.setConfidence((float)0.1);
	
	adept.common.Sentence sentenceAdept = new adept.common.Sentence(1, tokenOffsetAdept, tokenStreamAdept);
	sentenceAdept.setUncertaintyConfidence((float)0.1);
	sentenceAdept.setNoveltyConfidence((float)0.1);
	sentenceAdept.setPunctuation("AToT Punctuation");
	sentenceAdept.setType(adept.common.SentenceType.NONE);

	adept.common.HltContentContainer hltContentContainerAdept = new adept.common.HltContentContainer();
	List<adept.common.CommittedBelief> committedBeliefsAdept = new ArrayList<adept.common.CommittedBelief>();
	committedBeliefsAdept.add(committedBeliefAdept);
	hltContentContainerAdept.setCommittedBeliefs(committedBeliefsAdept);
	List<adept.common.Coreference> coreferencesAdept = new ArrayList<adept.common.Coreference>();
	coreferencesAdept.add(coreferenceAdept);
	hltContentContainerAdept.setCoreferences(coreferencesAdept);
	List<adept.common.Dependency> dependenciesAdept = new ArrayList<adept.common.Dependency>();
	dependenciesAdept.add(dependencyAdept);
	hltContentContainerAdept.setDependencies(dependenciesAdept);
	hltContentContainerAdept.setEntityMentions(resolvedEntityMentionsAdept);
	hltContentContainerAdept.setNamedEntities(resolvedEntityMentionsAdept);
	List<adept.common.Opinion> opinionsAdept = new ArrayList<adept.common.Opinion>();
	opinionsAdept.add(opinionAdept);
	hltContentContainerAdept.setOpinions(opinionsAdept);
	List<adept.common.PartOfSpeech> partOfSpeechsAdept = new ArrayList<adept.common.PartOfSpeech>();
	partOfSpeechsAdept.add(partOfSpeechAdept);
	hltContentContainerAdept.setPartOfSpeechs(partOfSpeechsAdept);
	List<adept.common.Passage> passagesAdept = new ArrayList<adept.common.Passage>();
	passagesAdept.add(passageAdept);
	hltContentContainerAdept.setPassages(passagesAdept);
	List<adept.common.ProsodicPhrase> prosodicPhrasesAdept = new ArrayList<adept.common.ProsodicPhrase>();
	prosodicPhrasesAdept.add(prosodicPhraseAdept);
	hltContentContainerAdept.setProsodicPhrases(prosodicPhrasesAdept);
	hltContentContainerAdept.setRelations(relationsAdept);
	List<adept.common.Sarcasm> sarcasmsAdept = new ArrayList<adept.common.Sarcasm>();
	sarcasmsAdept.add(sarcasmAdept);
	hltContentContainerAdept.setSarcasms(sarcasmsAdept);
	List<adept.common.Sentence> sentencesAdept = new ArrayList<adept.common.Sentence>();
	sentencesAdept.add(sentenceAdept);
	hltContentContainerAdept.setSentences(sentencesAdept);

	adept.common.HltContentContainerList hltContentContainerListAdept = new adept.common.HltContentContainerList();

	adept.common.InterPausalUnit interPausalUnitAdept = new adept.common.InterPausalUnit(1, audioOffsetAdept);
	Map<String, Float> acousticFeaturesAdept = new HashMap<String, Float>();
	acousticFeaturesAdept.put("AToT Acoustic Feature", (float)0.1);

	adept.common.Paraphrase paraphraseAdept = new adept.common.Paraphrase("AToT Paraphrase", (float)0.1);
	paraphraseAdept.setPosTag(new adept.common.Type("AToT Pos Type"));
    Map<String,Float> similarityscore = new HashMap<String,Float>();
    similarityscore.put("SCORE", new Float(0.1));
	adept.common.SentenceSimilarity sentenceSimilarityAdept = new adept.common.SentenceSimilarity(similarityscore, sentenceAdept, sentenceAdept);
	
	adept.common.Slot slotAdept = new adept.common.Slot(1, "AToT Slot Value");
	
	ArrayList<String> topicLabelsAdept = new ArrayList<String>();
	topicLabelsAdept.add("AToT Topic Label");
	adept.common.Story storyAdept = new adept.common.Story(tokenOffsetAdept, tokenStreamAdept, 1, topicLabelsAdept);

	adept.common.Viewpoint viewpointAdept = new adept.common.Viewpoint("AToT Speaker ID", "AToT Viewpoint Belief");

	adept.common.Topic topicAdept = new adept.common.Topic(1, "AToT Topic");
	topicAdept.setBelief("AToT Topic Belief");
	topicAdept.setPolarity(adept.common.Topic.Polarity.NONE);
	List<adept.common.Viewpoint> viewpointsAdept = new ArrayList<adept.common.Viewpoint>();
	viewpointsAdept.add(viewpointAdept);

	adept.common.Translation translationAdept = new adept.common.Translation(chunkAdept, chunkAdept);

	adept.common.Value valueAdept = new adept.common.Value(1);

	adept.common.Triple tripleAdept = new adept.common.Triple(entityAdept, slotAdept, valueAdept);

	adept.common.Type typeAdept = new adept.common.Type("AToT Type");

	adept.common.Utterance utteranceAdept = new adept.common.Utterance(tokenOffsetAdept, tokenStreamAdept, 1L, "AToT Speaker Id", "AToT Utterance Annotaion");

	int invalidConversions = 0;

	thrift.adept.common.AnomalousText anomalousTextThrift = mapper.convert(anomalousTextAdept);
	writer.println("\nAdept AnomalousText: Confidence: " + anomalousTextAdept.getConfidence() + " Document: " + anomalousTextAdept.getDocument() + " Explanation: " + anomalousTextAdept.getExplanation());
	writer.println("Thrift AnomalousText: Confidence: " + anomalousTextThrift.getConfidence() + " Document: " + anomalousTextThrift.getDocument() + " Explanation: " + anomalousTextThrift.getExplanation());
	if (anomalousTextAdept.getExplanation().toString() != anomalousTextThrift.getExplanation().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.AudioOffset audioOffsetThrift = mapper.convert(audioOffsetAdept);
	writer.println("\nAdept AudioOffset: Begin: " + audioOffsetAdept.getBegin() + " End: " + audioOffsetAdept.getEnd());
	writer.println("Thrift AudioOffset: BeginIndex: " + audioOffsetThrift.getBeginIndex() + " EndIndex: " + audioOffsetThrift.getEndIndex());
	if (audioOffsetAdept.getEnd() != audioOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.CharOffset charOffsetThrift = mapper.convert(charOffsetAdept);
	writer.println("\nAdept CharOffset: Begin: " + charOffsetAdept.getBegin() + " End: " + charOffsetAdept.getEnd());
	writer.println("Thrift CharOffset: BeginIndex: " + charOffsetThrift.getBeginIndex() + " EndIndex: " + charOffsetThrift.getEndIndex());
	if (charOffsetAdept.getEnd() != charOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Chunk chunkThrift = mapper.convert(chunkAdept);
	writer.println("\nAdept Chunk: Value: " + chunkAdept.getValue());
	writer.println("Thrift Chunk: Value: " + chunkThrift.getValue());
	if (chunkAdept.getValue().toString() != chunkThrift.getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.CommittedBelief committedBeliefThrift = mapper.convert(committedBeliefAdept);
	writer.println("\nAdept CommittedBelief: Modality: " + committedBeliefAdept.getModality());
	writer.println("Thrift CommittedBelief: Modality: " + committedBeliefThrift.getModality());
	if (committedBeliefAdept.getModality().toString() != committedBeliefThrift.getModality().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Coreference coreferenceThrift = mapper.convert(coreferenceAdept);
	writer.println("\nAdept Coreference: First Entity ID: " + coreferenceAdept.getEntities().get(0).getEntityId());
	writer.println("Thrift Coreference: First Entity ID: " + coreferenceThrift.getEntities().get(0).getEntityId());
	if (coreferenceAdept.getEntities().get(0).getEntityId() != coreferenceThrift.getEntities().get(0).getEntityId()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Corpus corpusThrift = mapper.convert(corpusAdept);
	writer.println("\nAdept Corpus: Name: " + corpusAdept.getName());
	writer.println("Thrift Corpus: Name: " + corpusThrift.getName());
	if (corpusAdept.getName().toString() != corpusThrift.getName().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Dependency dependencyThrift = mapper.convert(dependencyAdept);
	writer.println("\nAdept Dependency: Governor Value: " + dependencyAdept.getGovernor().getValue());
	writer.println("Thrift Dependency: Governor Value: " + dependencyThrift.getGovernor().getChunk().getValue());
	if (dependencyAdept.getGovernor().getValue().toString() != dependencyThrift.getGovernor().getChunk().getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Entailment entailmentThrift = mapper.convert(entailmentAdept);
	writer.println("\nAdept Entailment: Text: " + entailmentAdept.getText().getValue());
	writer.println("Thrift Entailment: Text: " + entailmentThrift.getText().getValue());
	if (entailmentAdept.getText().getValue().toString() != entailmentThrift.getText().getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Entity entityThrift = mapper.convert(entityAdept);
	writer.println("\nAdept Entity: Canonical mention: " + entityAdept.getCanonicalMention().getValue());
	writer.println("Thrift Entity: Canonical mention: " + entityThrift.getCanonicalMention().getValue());
	if (entityAdept.getCanonicalMention().getValue().toString() != entityThrift.getCanonicalMention().getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.EntityMention entityMentionThrift = mapper.convert(entityMentionAdept);
	writer.println("\nAdept EntityMention: Distribution: " + entityMentionAdept.getEntityIdDistribution());
	writer.println("Thrift EntityMention: Distribution: " + entityMentionThrift.getEntityIdDistribution());
	if (entityMentionAdept.getEntityType().getType().toString() != entityMentionThrift.getEntityType().getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.InterPausalUnit interPausalUnitThrift = mapper.convert(interPausalUnitAdept);
	writer.println("\nAdept InterPausalUnit: Audio Offset End: " + interPausalUnitAdept.getIpuAudioOffset().getEnd());
	writer.println("Thrift InterPausalUnit: Audio Offset End: " + interPausalUnitThrift.getIpuAudioOffset().getEndIndex());
	if (interPausalUnitAdept.getIpuAudioOffset().getEnd() != interPausalUnitThrift.getIpuAudioOffset().getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.JointRelationCoreference jointRelationCoreferenceThrift = mapper.convert(jointRelationCoreferenceAdept);
	writer.println("\nAdept JointRelationCoreference: Coreference Id: " + jointRelationCoreferenceAdept.getCoreference().getCoreferenceId());
	writer.println("Thrift JointRelationCoreference: Coreference Id: " + jointRelationCoreferenceThrift.getCoreference().getCoreferenceId());
	if (jointRelationCoreferenceAdept.getCoreference().getCoreferenceId() != jointRelationCoreferenceThrift.getCoreference().getCoreferenceId()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Opinion opinionThrift = mapper.convert(opinionAdept);
	writer.println("\nAdept Opinion: Polarity: " + opinionAdept.getPolarity());
	writer.println("Thrift Opinion: Polarity: " + opinionThrift.getPolarity());
	if (opinionAdept.getPolarity().toString() != opinionThrift.getPolarity().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Paraphrase paraphraseThrift = mapper.convert(paraphraseAdept);
	writer.println("\nAdept Paraphrase: Value: " + paraphraseAdept.getValue());
	writer.println("Thrift Paraphrase: Value: " + paraphraseThrift.getValue());
	if (paraphraseAdept.getValue().toString() != paraphraseThrift.getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.PartOfSpeech partOfSpeechThrift = mapper.convert(partOfSpeechAdept);
	writer.println("\nAdept PartOfSpeech: PosTag: " + partOfSpeechAdept.getPosTag().getType());
	writer.println("Thrift PartOfSpeech: PosTag: " + partOfSpeechThrift.getPosTag().getType());
	if (partOfSpeechAdept.getPosTag().getType().toString() != partOfSpeechThrift.getPosTag().getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Passage passageThrift = mapper.convert(passageAdept);
	writer.println("\nAdept Passage: Content Type: " + passageAdept.getContentType());
	writer.println("Thrift Passage: Content Type: " + passageThrift.getContentType());
	if (passageAdept.getContentType().toString() != passageThrift.getContentType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.ProsodicPhrase prosodicPhraseThrift = mapper.convert(prosodicPhraseAdept);
	writer.println("\nAdept ProsodicPhrase: Type: " + prosodicPhraseAdept.getType());
	writer.println("Thrift ProsodicPhrase: Type: " + prosodicPhraseThrift.getType());
	if (prosodicPhraseAdept.getType().toString() != prosodicPhraseThrift.getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Relation relationThrift = mapper.convert(relationAdept);
	writer.println("\nAdept Relation: Relation Type: " + relationAdept.getRelationType());
	writer.println("Thrift Relation: Relation Type: " + relationThrift.getType().getType());
	if (relationAdept.getRelationType().toString() != relationThrift.getType().getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Sarcasm sarcasmThrift = mapper.convert(sarcasmAdept);
	writer.println("\nAdept Sarcasm: Judgment: " + sarcasmAdept.getJudgment());
	writer.println("Thrift Sarcasm: Judgment: " + sarcasmThrift.getJudgment());
	if (sarcasmAdept.getJudgment().toString() != sarcasmThrift.getJudgment().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Sentence sentenceThrift = mapper.convert(sentenceAdept);
	writer.println("\nAdept Sentence: Punctuation: " + sentenceAdept.getPunctuation());
	writer.println("Thrift Sentence: Punctuation: " + sentenceThrift.getPunctuation());
	if (sentenceAdept.getPunctuation().toString() != sentenceThrift.getPunctuation().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.SentenceSimilarity sentenceSimilarityThrift = mapper.convert(sentenceSimilarityAdept);
	writer.println("\nAdept SentenceSimilarity: Sentence 1 Punctuation: " + sentenceSimilarityAdept.getSentence1().getPunctuation());
	writer.println("Thrift SentenceSimilarity: Sentence 1 Punctuation: " + sentenceSimilarityThrift.getSentence1().getPunctuation());
	if (sentenceSimilarityAdept.getSentence1().getPunctuation().toString() != sentenceSimilarityThrift.getSentence1().getPunctuation().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Story storyThrift = mapper.convert(storyAdept);
	writer.println("\nAdept Story: Topic Labels: " + storyAdept.getTopicLabels().get(0));
	writer.println("Thrift Story: Topic Labels: " + storyThrift.getTopicLabels().get(0));
	if (storyAdept.getTopicLabels().get(0).toString() != storyThrift.getTopicLabels().get(0).toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Token token1Thrift = mapper.convert(token1Adept);
	writer.println("\nAdept Token: Topic Labels: " + token1Adept.getLemma());
	writer.println("Thrift Token: Topic Labels: " + token1Thrift.getLemma());
	if (token1Adept.getLemma().toString() != token1Thrift.getLemma().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.TokenOffset tokenOffsetThrift = mapper.convert(tokenOffsetAdept);
	writer.println("\nAdept TokenOffset: End: " + tokenOffsetAdept.getEnd());
	writer.println("Thrift TokenOffset: EndIndex: " + tokenOffsetThrift.getEndIndex());
	if (tokenOffsetAdept.getEnd() != tokenOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.TokenStream tokenStreamThrift = mapper.convert(tokenStreamAdept);
	writer.println("\nAdept TokenStream: Value: " + tokenStreamAdept.getTextValue() + " Language: " + tokenStreamAdept.getLanguage());
	writer.println("Thrift TokenStream: Value: " + tokenStreamThrift.getTextValue() + " Language: " + tokenStreamThrift.getLanguage());
	if (tokenOffsetAdept.getEnd() != tokenOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	thrift.adept.common.Topic topicThrift = mapper.convert(topicAdept);
	writer.println("\nAdept Topic: Polarity: " + topicAdept.getPolarity());
	writer.println("Thrift Topic: Polarity: " + topicThrift.getPolarity());
	if (topicAdept.getPolarity().toString() != topicThrift.getPolarity().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	writer.println("Invalid Adept to Thrift Conversions: " + invalidConversions);
	System.out.println("Invalid Adept to Thrift Conversions: " + invalidConversions);

///////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////

	writer.println("\nTesting Thrift to Adept Conversion...");
	System.out.println("\nTesting Thrift to Adept Conversion...");

	corpusThrift = new thrift.adept.common.Corpus("Corpus002", "TToAType", "TToA Corpus", "AtoT uri");

	thrift.adept.common.Document documentThrift = new thrift.adept.common.Document("Doc002", corpusThrift, "TToADocType", "TToADoc uri", "TToA English");
	documentThrift.setAudioUri("TToADoc audio uri");
	documentThrift.setGenre("TToA genre");
	documentThrift.setHeadline("TToA headlne");
	documentThrift.setValue("Token1 Token2");
	
	anomalousTextThrift = new thrift.adept.common.AnomalousText((float)0.2, documentThrift);
	anomalousTextThrift.setExplanation("TToA explanation");

	audioOffsetThrift = new thrift.adept.common.AudioOffset((float)7, (float)13);

	charOffsetThrift = new thrift.adept.common.CharOffset(7, 13);

	tokenOffsetThrift = new thrift.adept.common.TokenOffset(1, 2);

	token1Thrift = new thrift.adept.common.Token(3, charOffsetThrift, "Token1");
	token1Thrift.setConfidence((float)0.2);
	token1Thrift.setLemma("TToA lemma 3");
	token1Thrift.setAudioOffset(audioOffsetThrift);

	thrift.adept.common.Token token2Thrift = new thrift.adept.common.Token(4, charOffsetThrift, "Token2");
	token2Thrift.setConfidence((float)0.2);
	token2Thrift.setLemma("TToA lemma 4");
	token2Thrift.setAudioOffset(audioOffsetThrift);

	tokenStreamThrift = new thrift.adept.common.TokenStream(thrift.adept.common.TokenizerType.ADEPT, thrift.adept.common.TranscriptType.SOURCE, "TToA English", thrift.adept.common.ChannelName.NONE, thrift.adept.common.ContentType.TEXT, "Token3 Token4");
	tokenStreamThrift.setAsrName(thrift.adept.common.AsrName.BBN);
	tokenStreamThrift.setDocument(documentThrift);
	tokenStreamThrift.setSpeechUnit(thrift.adept.common.SpeechUnit.NONE);
	tokenStreamThrift.setTranslatorName(thrift.adept.common.TranslatorName.NONE);
	List<thrift.adept.common.Token> tokenList = new ArrayList<thrift.adept.common.Token>();
	tokenList.add(token1Thrift);
	tokenList.add(token2Thrift);
	tokenStreamThrift.setTokenList(tokenList);
	
	chunkThrift = new thrift.adept.common.Chunk(tokenOffsetThrift, tokenStreamThrift);
	chunkThrift.setValue("Token4");
	
	committedBeliefThrift = new thrift.adept.common.CommittedBelief(2, tokenOffsetThrift, tokenStreamThrift, thrift.adept.common.Modality.ABILITY);
	
	entityMentionThrift = new thrift.adept.common.EntityMention(2, tokenOffsetThrift, tokenStreamThrift);
	Map<Long, Double> hashMap = new HashMap<Long, Double>();
	hashMap.put(2L, (double)0.2);
	entityMentionThrift.setEntityIdDistribution(hashMap);
	entityMentionThrift.setEntityType(new thrift.adept.common.Type("TToA Entity Type"));
	entityMentionThrift.setMentionType(new thrift.adept.common.Type("TToA Mention Type"));
	entityMentionThrift.setValue("Token4");

	entityThrift = new thrift.adept.common.Entity(2, new thrift.adept.common.Type("TToA Entity Type"));
	entityThrift.setCanonicalMention(entityMentionThrift);
	entityThrift.setValue("Token4");
	entityThrift.setEntityType(new thrift.adept.common.Type("TToA Entity Type"));
	
	coreferenceThrift = new thrift.adept.common.Coreference(2);
	List<thrift.adept.common.Entity> entitiesThrift = new ArrayList<thrift.adept.common.Entity>();
	entitiesThrift.add(entityThrift);
	List<thrift.adept.common.EntityMention> resolvedEntityMentionsThrift = new ArrayList<thrift.adept.common.EntityMention>();
	resolvedEntityMentionsThrift.add(entityMentionThrift);
	coreferenceThrift.setEntities(entitiesThrift);
	coreferenceThrift.setResolvedEntityMentions(resolvedEntityMentionsThrift);

	thrift.adept.common.ChunkUnion chunkUnion = thrift.adept.common.ChunkUnion.chunk(chunkThrift);

	dependencyThrift = new thrift.adept.common.Dependency(chunkUnion, chunkUnion, "TToA dependency Type");
	
	thrift.adept.common.DiscourseUnit discourseUnitThrift = new thrift.adept.common.DiscourseUnit(tokenOffsetThrift, tokenStreamThrift, 2, "TToA Discourse Type");
	discourseUnitThrift.setNoveltyConfidence((float)0.2);
	discourseUnitThrift.setUncertaintyConfidence((float)0.2);
	
	passageThrift = new thrift.adept.common.Passage(2, tokenOffsetThrift, tokenStreamThrift);
	passageThrift.setContentType("TToA content Type");
	passageThrift.setValue("Token4");

	entailmentThrift = new thrift.adept.common.Entailment(2);
	entailmentThrift.setHypothesis(passageThrift);
	Map<thrift.adept.common.EntailmentJudgment, Double> judgmentDistributionThrift = new HashMap<thrift.adept.common.EntailmentJudgment, Double>();
	judgmentDistributionThrift.put(thrift.adept.common.EntailmentJudgment.ENTAILS, (double)0.2);
	entailmentThrift.setJudgmentDistribution(judgmentDistributionThrift);
	entailmentThrift.setText(passageThrift);

	relationThrift = new thrift.adept.common.Relation(2, new thrift.adept.common.Type("TToA Relation Type"));
	relationThrift.setConfidence((float)0.2);

	jointRelationCoreferenceThrift = new thrift.adept.common.JointRelationCoreference();
	jointRelationCoreferenceThrift.setCoreference(coreferenceThrift);
	List<thrift.adept.common.Relation> relationsThrift = new ArrayList<thrift.adept.common.Relation>();
	relationsThrift.add(relationThrift);
	
	opinionThrift = new thrift.adept.common.Opinion(tokenOffsetThrift, tokenStreamThrift, thrift.adept.common.Subjectivity.OBJECTIVE, thrift.adept.common.Polarity.NEUTRAL);
	
	partOfSpeechThrift = new thrift.adept.common.PartOfSpeech(2, tokenOffsetThrift, tokenStreamThrift);
	partOfSpeechThrift.setPosTag(new thrift.adept.common.Type("TToA Pos Type"));
	
	prosodicPhraseThrift = new thrift.adept.common.ProsodicPhrase(tokenOffsetThrift, tokenStreamThrift, 2);
	prosodicPhraseThrift.setNoveltyConfidence((float)0.2);
	prosodicPhraseThrift.setUncertaintyConfidence((float)0.2);
	prosodicPhraseThrift.setConfidence((float)0.2);
	prosodicPhraseThrift.setType("TToA Prosodic Phrase Type");
	
	sarcasmThrift = new thrift.adept.common.Sarcasm(2, tokenOffsetThrift, tokenStreamThrift, thrift.adept.common.SarcasmJudgment.NONE);
	sarcasmThrift.setConfidence((float)0.2);
	
	sentenceThrift = new thrift.adept.common.Sentence(2, tokenOffsetThrift, tokenStreamThrift);
	sentenceThrift.setUncertaintyConfidence((float)0.2);
	sentenceThrift.setNoveltyConfidence((float)0.2);
	sentenceThrift.setPunctuation("TToA Punctuation");
	sentenceThrift.setType(thrift.adept.common.SentenceType.NONE);

	thrift.adept.common.HltContentContainer hltContentContainerThrift = new thrift.adept.common.HltContentContainer();
	List<thrift.adept.common.CommittedBelief> committedBeliefsThrift = new ArrayList<thrift.adept.common.CommittedBelief>();
	committedBeliefsThrift.add(committedBeliefThrift);
	hltContentContainerThrift.setCommittedBeliefs(committedBeliefsThrift);
	List<thrift.adept.common.Coreference> coreferencesThrift = new ArrayList<thrift.adept.common.Coreference>();
	coreferencesThrift.add(coreferenceThrift);
	hltContentContainerThrift.setCoreferences(coreferencesThrift);
	List<thrift.adept.common.Dependency> dependenciesThrift = new ArrayList<thrift.adept.common.Dependency>();
	dependenciesThrift.add(dependencyThrift);
	hltContentContainerThrift.setDependencies(dependenciesThrift);
	hltContentContainerThrift.setEntityMentions(resolvedEntityMentionsThrift);
	hltContentContainerThrift.setNamedEntities(resolvedEntityMentionsThrift);
	List<thrift.adept.common.Opinion> opinionsThrift = new ArrayList<thrift.adept.common.Opinion>();
	opinionsThrift.add(opinionThrift);
	hltContentContainerThrift.setOpinions(opinionsThrift);
	List<thrift.adept.common.PartOfSpeech> partOfSpeechsThrift = new ArrayList<thrift.adept.common.PartOfSpeech>();
	partOfSpeechsThrift.add(partOfSpeechThrift);
	hltContentContainerThrift.setPartOfSpeechs(partOfSpeechsThrift);
	List<thrift.adept.common.Passage> passagesThrift = new ArrayList<thrift.adept.common.Passage>();
	passagesThrift.add(passageThrift);
	hltContentContainerThrift.setPassages(passagesThrift);
	List<thrift.adept.common.ProsodicPhrase> prosodicPhrasesThrift = new ArrayList<thrift.adept.common.ProsodicPhrase>();
	prosodicPhrasesThrift.add(prosodicPhraseThrift);
	hltContentContainerThrift.setProsodicPhrases(prosodicPhrasesThrift);
	hltContentContainerThrift.setRelations(relationsThrift);
	List<thrift.adept.common.Sarcasm> sarcasmsThrift = new ArrayList<thrift.adept.common.Sarcasm>();
	sarcasmsThrift.add(sarcasmThrift);
	hltContentContainerThrift.setSarcasms(sarcasmsThrift);
	List<thrift.adept.common.Sentence> sentencesThrift = new ArrayList<thrift.adept.common.Sentence>();
	sentencesThrift.add(sentenceThrift);
	hltContentContainerThrift.setSentences(sentencesThrift);

	thrift.adept.common.HltContentContainerList hltContentContainerListThrift = new thrift.adept.common.HltContentContainerList();

	interPausalUnitThrift = new thrift.adept.common.InterPausalUnit(1, audioOffsetThrift);
	Map<String, Float> acousticFeaturesThrift = new HashMap<String, Float>();
	acousticFeaturesThrift.put("TToA Acoustic Feature", (float)0.2);

	paraphraseThrift = new thrift.adept.common.Paraphrase("TToA Paraphrase", (float)0.2);
	thrift.adept.common.Type posTagType = new thrift.adept.common.Type();
	posTagType.setType("TToA Pos Type");
	paraphraseThrift.setPosTag(posTagType);

	sentenceSimilarityThrift = new thrift.adept.common.SentenceSimilarity((float)0.2, sentenceThrift, sentenceThrift);
	
	thrift.adept.common.Slot slotThrift = new thrift.adept.common.Slot(2);
	
	ArrayList<String> topicLabelsThrift = new ArrayList<String>();
	topicLabelsThrift.add("TToA Topic Label");
	storyThrift = new thrift.adept.common.Story(tokenOffsetThrift, tokenStreamThrift, 1, topicLabelsThrift);

	thrift.adept.common.Viewpoint viewpointThrift = new thrift.adept.common.Viewpoint("TToA Speaker ID", "TToA Viewpoint Belief");

	topicThrift = new thrift.adept.common.Topic(2, "TToA Topic");
	topicThrift.setBelief("TToA Topic Belief");
	topicThrift.setPolarity(thrift.adept.common.TopicPolarity.NONE);
	List<thrift.adept.common.Viewpoint> viewpointsThrift = new ArrayList<thrift.adept.common.Viewpoint>();
	viewpointsThrift.add(viewpointThrift);

	thrift.adept.common.Translation translationThrift = new thrift.adept.common.Translation(chunkUnion, chunkUnion);

	thrift.adept.common.Value valueThrift = new thrift.adept.common.Value(2);

	thrift.adept.common.Triple tripleThrift = new thrift.adept.common.Triple(entityThrift, slotThrift, "TToA Value");

	thrift.adept.common.Type typeThrift = new thrift.adept.common.Type("TToA Type");

	thrift.adept.common.Utterance utteranceThrift = new thrift.adept.common.Utterance(tokenOffsetThrift, tokenStreamThrift, 2, 2, "TToA Utterance Annotaion");

	invalidConversions = 0;

	anomalousTextAdept = mapper.convert(anomalousTextThrift);
	writer.println("\nAdept AnomalousText: Confidence: " + anomalousTextAdept.getConfidence() + " Document: " + anomalousTextAdept.getDocument() + " Explanation: " + anomalousTextAdept.getExplanation());
	writer.println("Thrift AnomalousText: Confidence: " + anomalousTextThrift.getConfidence() + " Document: " + anomalousTextThrift.getDocument() + " Explanation: " + anomalousTextThrift.getExplanation());
	if (anomalousTextAdept.getExplanation().toString() != anomalousTextThrift.getExplanation().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	audioOffsetAdept = mapper.convert(audioOffsetThrift);
	writer.println("\nAdept AudioOffset: Begin: " + audioOffsetAdept.getBegin() + " End: " + audioOffsetAdept.getEnd());
	writer.println("Thrift AudioOffset: BeginIndex: " + audioOffsetThrift.getBeginIndex() + " EndIndex: " + audioOffsetThrift.getEndIndex());
	if (audioOffsetAdept.getEnd() != audioOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	charOffsetAdept = mapper.convert(charOffsetThrift);
	writer.println("\nAdept CharOffset: Begin: " + charOffsetAdept.getBegin() + " End: " + charOffsetAdept.getEnd());
	writer.println("Thrift CharOffset: BeginIndex: " + charOffsetThrift.getBeginIndex() + " EndIndex: " + charOffsetThrift.getEndIndex());
	if (charOffsetAdept.getEnd() != charOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	chunkAdept = mapper.convert(chunkThrift);
	writer.println("\nAdept Chunk: Value: " + chunkAdept.getValue());
	writer.println("Thrift Chunk: Value: " + chunkThrift.getValue());
	if (chunkAdept.getValue().toString() != chunkThrift.getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	committedBeliefAdept = mapper.convert(committedBeliefThrift);
	writer.println("\nAdept CommittedBelief: Modality: " + committedBeliefAdept.getModality());
	writer.println("Thrift CommittedBelief: Modality: " + committedBeliefThrift.getModality());
	if (committedBeliefAdept.getModality().toString() != committedBeliefThrift.getModality().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	coreferenceAdept = mapper.convert(coreferenceThrift);
	writer.println("\nAdept Coreference: First Entity ID: " + coreferenceAdept.getEntities().get(0).getEntityId());
	writer.println("Thrift Coreference: First Entity ID: " + coreferenceThrift.getEntities().get(0).getEntityId());
	if (coreferenceAdept.getEntities().get(0).getEntityId() != coreferenceThrift.getEntities().get(0).getEntityId()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	corpusAdept = mapper.convert(corpusThrift);
	writer.println("\nAdept Corpus: Name: " + corpusAdept.getName());
	writer.println("Thrift Corpus: Name: " + corpusThrift.getName());
	if (corpusAdept.getName().toString() != corpusThrift.getName().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	dependencyAdept = mapper.convert(dependencyThrift);
	writer.println("\nAdept Dependency: Governor Value: " + dependencyAdept.getGovernor().getValue());
	writer.println("Thrift Dependency: Governor Value: " + dependencyThrift.getGovernor().getChunk().getValue());
	if (dependencyAdept.getGovernor().getValue().toString() != dependencyThrift.getGovernor().getChunk().getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	entailmentAdept = mapper.convert(entailmentThrift);
	writer.println("\nAdept Entailment: Text: " + entailmentAdept.getText().getValue());
	writer.println("Thrift Entailment: Text: " + entailmentThrift.getText().getValue());
	if (entailmentAdept.getText().getValue().toString() != entailmentThrift.getText().getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	entityAdept = mapper.convert(entityThrift);
	writer.println("\nAdept Entity: Canonical mention: " + entityAdept.getCanonicalMention().getValue());
	writer.println("Thrift Entity: Canonical mention: " + entityThrift.getCanonicalMention().getValue());
	if (entityAdept.getCanonicalMention().getValue().toString() != entityThrift.getCanonicalMention().getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	entityMentionAdept = mapper.convert(entityMentionThrift);
	writer.println("\nAdept EntityMention: Distribution: " + entityMentionAdept.getEntityIdDistribution());
	writer.println("Thrift EntityMention: Distribution: " + entityMentionThrift.getEntityIdDistribution());
	if (entityMentionAdept.getEntityType().getType().toString() != entityMentionThrift.getEntityType().getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	interPausalUnitAdept = mapper.convert(interPausalUnitThrift);
	writer.println("\nAdept InterPausalUnit: Audio Offset End: " + interPausalUnitAdept.getIpuAudioOffset().getEnd());
	writer.println("Thrift InterPausalUnit: Audio Offset End: " + interPausalUnitThrift.getIpuAudioOffset().getEndIndex());
	if (interPausalUnitAdept.getIpuAudioOffset().getEnd() != interPausalUnitThrift.getIpuAudioOffset().getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	jointRelationCoreferenceAdept = mapper.convert(jointRelationCoreferenceThrift);
	writer.println("\nAdept JointRelationCoreference: Coreference Id: " + jointRelationCoreferenceAdept.getCoreference().getCoreferenceId());
	writer.println("Thrift JointRelationCoreference: Coreference Id: " + jointRelationCoreferenceThrift.getCoreference().getCoreferenceId());
	if (jointRelationCoreferenceAdept.getCoreference().getCoreferenceId() != jointRelationCoreferenceThrift.getCoreference().getCoreferenceId()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	opinionAdept = mapper.convert(opinionThrift);
	writer.println("\nAdept Opinion: Polarity: " + opinionAdept.getPolarity());
	writer.println("Thrift Opinion: Polarity: " + opinionThrift.getPolarity());
	if (opinionAdept.getPolarity().toString() != opinionThrift.getPolarity().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	paraphraseAdept = mapper.convert(paraphraseThrift);
	writer.println("\nAdept Paraphrase: Value: " + paraphraseAdept.getValue());
	writer.println("Thrift Paraphrase: Value: " + paraphraseThrift.getValue());
	if (paraphraseAdept.getValue().toString() != paraphraseThrift.getValue().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	partOfSpeechAdept = mapper.convert(partOfSpeechThrift);
	writer.println("\nAdept PartOfSpeech: PosTag: " + partOfSpeechAdept.getPosTag().getType());
	writer.println("Thrift PartOfSpeech: PosTag: " + partOfSpeechThrift.getPosTag().getType());
	if (partOfSpeechAdept.getPosTag().getType().toString() != partOfSpeechThrift.getPosTag().getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	passageAdept = mapper.convert(passageThrift);
	writer.println("\nAdept Passage: Content Type: " + passageAdept.getContentType());
	writer.println("Thrift Passage: Content Type: " + passageThrift.getContentType());
	if (passageAdept.getContentType().toString() != passageThrift.getContentType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}


	prosodicPhraseAdept = mapper.convert(prosodicPhraseThrift);
	writer.println("\nAdept ProsodicPhrase: Type: " + prosodicPhraseAdept.getType());
	writer.println("Thrift ProsodicPhrase: Type: " + prosodicPhraseThrift.getType());
	if (prosodicPhraseAdept.getType().toString() != prosodicPhraseThrift.getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	relationAdept = mapper.convert(relationThrift);
	writer.println("\nAdept Relation: Relation Type: " + relationAdept.getRelationType());
	writer.println("Thrift Relation: Relation Type: " + relationThrift.getType().getType());
	if (relationAdept.getRelationType().toString() != relationThrift.getType().getType().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	sarcasmAdept = mapper.convert(sarcasmThrift);
	writer.println("\nAdept Sarcasm: Judgment: " + sarcasmAdept.getJudgment());
	writer.println("Thrift Sarcasm: Judgment: " + sarcasmThrift.getJudgment());
	if (sarcasmAdept.getJudgment().toString() != sarcasmThrift.getJudgment().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	sentenceAdept = mapper.convert(sentenceThrift);
	writer.println("\nAdept Sentence: Punctuation: " + sentenceAdept.getPunctuation());
	writer.println("Thrift Sentence: Punctuation: " + sentenceThrift.getPunctuation());
	if (sentenceAdept.getPunctuation().toString() != sentenceThrift.getPunctuation().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	sentenceSimilarityAdept = mapper.convert(sentenceSimilarityThrift);
	writer.println("\nAdept SentenceSimilarity: Sentence 1 Punctuation: " + sentenceSimilarityAdept.getSentence1().getPunctuation());
	writer.println("Thrift SentenceSimilarity: Sentence 1 Punctuation: " + sentenceSimilarityThrift.getSentence1().getPunctuation());
	if (sentenceSimilarityAdept.getSentence1().getPunctuation().toString() != sentenceSimilarityThrift.getSentence1().getPunctuation().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	storyAdept = mapper.convert(storyThrift);
	writer.println("\nAdept Story: Topic Labels: " + storyAdept.getTopicLabels().get(0));
	writer.println("Thrift Story: Topic Labels: " + storyThrift.getTopicLabels().get(0));
	if (storyAdept.getTopicLabels().get(0).toString() != storyThrift.getTopicLabels().get(0).toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	token1Adept = mapper.convert(token1Thrift);
	writer.println("\nAdept Token: Topic Labels: " + token1Adept.getLemma());
	writer.println("Thrift Token: Topic Labels: " + token1Thrift.getLemma());
	if (token1Adept.getLemma().toString() != token1Thrift.getLemma().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	tokenOffsetAdept = mapper.convert(tokenOffsetThrift);
	writer.println("\nAdept TokenOffset: End: " + tokenOffsetAdept.getEnd());
	writer.println("Thrift TokenOffset: EndIndex: " + tokenOffsetThrift.getEndIndex());
	if (tokenOffsetAdept.getEnd() != tokenOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	tokenStreamAdept = mapper.convert(tokenStreamThrift);
	writer.println("\nAdept TokenStream: Value: " + tokenStreamAdept.getTextValue() + " Language: " + tokenStreamAdept.getLanguage());
	writer.println("Thrift TokenStream: Value: " + tokenStreamThrift.getTextValue() + " Language: " + tokenStreamThrift.getLanguage());
	if (tokenOffsetAdept.getEnd() != tokenOffsetThrift.getEndIndex()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	topicAdept = mapper.convert(topicThrift);
	writer.println("\nAdept Topic: Polarity: " + topicAdept.getPolarity());
	writer.println("Thrift Topic: Polarity: " + topicThrift.getPolarity());
	if (topicAdept.getPolarity().toString() != topicThrift.getPolarity().toString()) {
		writer.println("Objects differ");
		invalidConversions++;
	}

	writer.println("Invalid Thrift to Adept Conversions: " + invalidConversions);
	writer.close();
	System.out.println("Invalid Thrift to Adept Conversions: " + invalidConversions);
	} catch (IOException e) {
		e.printStackTrace();
	}
	System.out.println("See mapping_test_out.txt for details");
		

    }
}

	

	

	

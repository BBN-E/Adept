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


package adept.mappers.concrete;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import edu.jhu.hlt.concrete.Concrete;

import org.dozer.DozerBeanMapper;

public class ConcreteAdeptMapper {

	private static DozerBeanMapper mapper;

	public ConcreteAdeptMapper(String path) {
		String file = "file:" + path;
		mapper = new DozerBeanMapper(Arrays.asList(new String[]{file}));
	}

	public ConcreteAdeptMapper() {
		
		mapper = new DozerBeanMapper(Arrays.asList(new String[]{"adept/mappers/concrete/ConcreteAdeptMappings.xml"}));
	}

	/** Concrete to Adept mapping methods
	 *  Many Adept objects don't have empty default constructors assumed
	 *  by Dozer, so dummy objects need to be initialized
	 */
	public adept.common.ID map(edu.jhu.hlt.concrete.Concrete.UUID uuid) {
		adept.common.ID id = new adept.common.ID();
		adept.mappers.concrete.ID tempId = new adept.mappers.concrete.ID();
		tempId.setId(uuid.getHigh(), uuid.getLow());		
		mapper.map(tempId, id);
		return id;
	}

	public adept.common.CharOffset map(edu.jhu.hlt.concrete.Concrete.TextSpan textSpan) {
		adept.common.CharOffset charOffset = new adept.common.CharOffset(0, 0);
		mapper.map(textSpan, charOffset);
		return charOffset;
	}

	public adept.common.Audio map(edu.jhu.hlt.concrete.Concrete.Sound sound) {
		adept.common.Audio audio = new adept.common.Audio();
		if (sound.hasWav()) {
			audio.setAudioType(adept.common.AudioFileType.WAV);
			audio.setAudioBuffer(sound.getWav().toByteArray());
		} else if (sound.hasSph()) { 
			audio.setAudioType(adept.common.AudioFileType.SPH);
			audio.setAudioBuffer(sound.getSph().toByteArray());
		} else if (sound.hasMp3()) { 
			audio.setAudioType(adept.common.AudioFileType.MP3);
			audio.setAudioBuffer(sound.getMp3().toByteArray());
		}
		mapper.map(sound, audio);
		return audio;
	}

	public adept.common.AudioOffset map(edu.jhu.hlt.concrete.Concrete.AudioSpan audioSpan) {
		adept.common.AudioOffset audioOffset = new adept.common.AudioOffset(0, 0);
		mapper.map(audioSpan, audioOffset);
		return audioOffset;
	}

	public adept.common.Session map(edu.jhu.hlt.concrete.Concrete.SentenceSegmentation sentenceSegmentation) {
		adept.common.Session session = new adept.common.Session(null, null, (long)sentenceSegmentation.getUuid().getLow(), null, null);
		mapper.map(sentenceSegmentation, session);
		return session;
	}

	public adept.common.Sentence map(edu.jhu.hlt.concrete.Concrete.Sentence concreteSentence) {
		adept.common.Sentence sentence = new adept.common.Sentence((long)concreteSentence.getUuid().getLow(), null, map(concreteSentence.getTokenization(0)));
		mapper.map(concreteSentence, sentence);
		return sentence;
	}

	public adept.common.Token map(edu.jhu.hlt.concrete.Concrete.Token concreteToken) {
		adept.common.Token token = new adept.common.Token(0, null, null);
		mapper.map(concreteToken, token);
		return token;
	}

	public adept.common.Token map(edu.jhu.hlt.concrete.Token concreteToken) {
		adept.common.Token token = new adept.common.Token(0, new adept.common.CharOffset(0,1), "test");
		mapper.map(concreteToken, token);
		return token;
	}


	public adept.common.HltContentContainer map(edu.jhu.hlt.concrete.Concrete.Communication communication) {
		adept.common.HltContentContainer hltContentContainer = new adept.common.HltContentContainer();
		List<adept.common.Sentence> sentences = new ArrayList<adept.common.Sentence>();
		List<adept.common.Session> sessions = new ArrayList<adept.common.Session>();
		for (edu.jhu.hlt.concrete.Concrete.SectionSegmentation sectionSegmentation : communication.getSectionSegmentationList()) {
			for (edu.jhu.hlt.concrete.Concrete.Section section : sectionSegmentation.getSectionList()) {
				for (edu.jhu.hlt.concrete.Concrete.SentenceSegmentation sentenceSegmentation : section.getSentenceSegmentationList()) {
					sessions.add(map(sentenceSegmentation));
					for (edu.jhu.hlt.concrete.Concrete.Sentence sentence : sentenceSegmentation.getSentenceList()) {
						sentences.add(map(sentence));
					}
				}
			}
		}
		List<adept.common.EntityMention> entityMentions = new ArrayList<adept.common.EntityMention>();
		for (edu.jhu.hlt.concrete.Concrete.EntityMentionSet entityMentionSet : communication.getEntityMentionSetList()) {
			for (edu.jhu.hlt.concrete.Concrete.EntityMention entityMention : entityMentionSet.getMentionList()) {
//				entityMentions.add(map(communication, entityMention));
			}
		}
					
		mapper.map(communication, hltContentContainer);
		hltContentContainer.setSessions(sessions);
		hltContentContainer.setSentences(sentences);
		return hltContentContainer;
	}

	public adept.common.LanguageIdentification map(edu.jhu.hlt.concrete.Concrete.LanguageIdentification concreteLanguageIdentification) {
		adept.common.LanguageIdentification languageIdentification = new adept.common.LanguageIdentification();
		mapper.map(concreteLanguageIdentification, languageIdentification);
		Map<String, Float> languageProbabilityDistribution = new HashMap<String, Float>();
		for (edu.jhu.hlt.concrete.Concrete.LanguageIdentification.LanguageProb languageProb : concreteLanguageIdentification.getLanguageList()) {
			languageProbabilityDistribution.put(languageProb.getLanguage(), languageProb.getProbability());
		}
		languageIdentification.setLanguageProbabilityDistribution(languageProbabilityDistribution);
			
		return languageIdentification;
	}

/*	public adept.common.Message map(edu.jhu.hlt.concrete.Concrete.EmailCommunicationInfo emailCommunicationInfo) {
		adept.common.Message message = new adept.common.Message();
		mapper.map(emailCommunicationInfo, message);
		return message;
	}
*/

	public adept.common.EntityMention map(edu.jhu.hlt.concrete.Communication communication, edu.jhu.hlt.concrete.EntityMention concreteEntityMention) {
	        edu.jhu.hlt.concrete.Tokenization tkn = communication.getSectionSegmentations().get(0)
	                                .getSectionList().get(0)
	                                .getSentenceSegmentation().get(0)
	                                .getSentenceList().get(0)
        	                        .getTokenizationList().get(0);
        	edu.jhu.hlt.concrete.TokenRefSequence trs = concreteEntityMention.getTokens();
        	List<Integer> tokenIdList = trs.getTokenIndexList();	
        	adept.common.TokenStream tokenStream = map(tkn);
		adept.common.TokenOffset tokenOffset = new adept.common.TokenOffset(tokenIdList.get(0), tokenIdList.get(tokenIdList.size()-1));
		adept.common.Corpus corpus = new adept.common.Corpus("ID", null, "Name", null);
		adept.common.Document document = new adept.common.Document("ID", corpus, "Type", null, "language");
		document.setValue(communication.getText());
//		tokenStream.setDocument(document);
		adept.common.EntityMention retEntityMention = new adept.common.EntityMention(1, tokenOffset, tokenStream);
		mapper.map(concreteEntityMention, retEntityMention);
		return retEntityMention;
	}

	public adept.common.EntityMention map(edu.jhu.hlt.concrete.ConcreteEntityMention concreteEntityMention) {

        	List<edu.jhu.hlt.concrete.Concrete.Token> tokenList = concreteEntityMention.getTokens();
		String value = "";
		for  (String tokString : concreteEntityMention.getTokensAsStrings()) {
			value += tokString + " ";
		}
		adept.common.TokenOffset tokenOffset = new adept.common.TokenOffset(tokenList.get(0).getTokenIndex()-1, tokenList.get(tokenList.size()-1).getTokenIndex()-1);
		adept.common.Corpus corpus = new adept.common.Corpus(null, null, null, null);
		adept.common.Document document = new adept.common.Document(null, corpus, null, null, null);
		document.setValue(value);
        	adept.common.TokenStream tokenStream = new adept.common.TokenStream(null, null, null, null, null, document);
        	for (edu.jhu.hlt.concrete.Concrete.Token tok : tokenList)
        	    tokenStream.add(map(tok));
		adept.common.EntityMention retEntityMention = new adept.common.EntityMention(1, tokenOffset, tokenStream);
		return retEntityMention;
	}

	

	public adept.common.EntityMention map(edu.jhu.hlt.concrete.Concrete.Communication communication, edu.jhu.hlt.concrete.Concrete.SituationMention situationMention) {
	        edu.jhu.hlt.concrete.Concrete.Tokenization tkn = communication.getSectionSegmentation(0)
        	                        .getSection(0)
        	                        .getSentenceSegmentation(0)
        	                        .getSentence(0)
        	                        .getTokenization(0);
        	edu.jhu.hlt.concrete.Concrete.TokenRefSequence trs = situationMention.getTokens();
        	List<Integer> tokenIdList = trs.getTokenIndexList();
        	adept.common.TokenStream tokenStream = map(tkn);

		adept.common.TokenOffset tokenOffset = new adept.common.TokenOffset(tokenIdList.get(0), tokenIdList.get(tokenIdList.size()-1));
		adept.common.Corpus corpus = new adept.common.Corpus(null, null, null, null);
		adept.common.Document document = new adept.common.Document(null, corpus, null, null, null);
		document.setValue(communication.getText());
//		tokenStream.setDocument(document);
		adept.common.EntityMention retEntityMention = new adept.common.EntityMention(1, tokenOffset, tokenStream);
		mapper.map(situationMention, retEntityMention);
		return retEntityMention;
	}

	public adept.common.TokenStream map(edu.jhu.hlt.concrete.Concrete.Tokenization tokenization) {
		String value = "";
        	for (edu.jhu.hlt.concrete.Concrete.Token tok : tokenization.getTokenList()) {
	            value += tok.getText() + " ";
		}
		adept.common.Corpus corpus = new adept.common.Corpus(null, null, null, null);
		adept.common.Document document = new adept.common.Document(null, corpus, null, null, null);
		document.setValue(value);

		adept.common.TokenStream tokenStream = new adept.common.TokenStream(null, null, "", null, null, document);
		mapper.map(tokenization, tokenStream);
                List<edu.jhu.hlt.concrete.Concrete.Token> tokenList = tokenization.getTokenList();
        	for (edu.jhu.hlt.concrete.Concrete.Token tok : tokenList) {
        	    tokenStream.add(map(tok));
		}
		adept.common.TokenOffset tokenOffset = new adept.common.TokenOffset(tokenList.get(0).getTokenIndex()-1, tokenList.get(tokenList.size()-1).getTokenIndex()-1);
		return tokenStream;
	}

	public adept.common.TokenStream map(edu.jhu.hlt.concrete.Tokenization tokenization) {
		String value = "";
        	for (edu.jhu.hlt.concrete.Token tok : tokenization.getTokenList().getTokens()) {
	            value += tok.getText() + " ";
		}
		adept.common.Corpus corpus = new adept.common.Corpus("ID", null, "Name", null);
		adept.common.Document document = new adept.common.Document("ID", corpus, "Type", null, "language");
		document.setValue(value);

		adept.common.TokenStream tokenStream = new adept.common.TokenStream(adept.common.TokenizerType.OTHER, null, "", null, null, document);
		mapper.map(tokenization, tokenStream);
                List<edu.jhu.hlt.concrete.Token> tokenList = tokenization.getTokenList().getTokens();
        	for (edu.jhu.hlt.concrete.Token tok : tokenList) {
        	    tokenStream.add(map(tok));
		}
		adept.common.TokenOffset tokenOffset = new adept.common.TokenOffset(tokenList.get(0).getTokenIndex()-1, tokenList.get(tokenList.size()-1).getTokenIndex()-1);
		return tokenStream;
	}

	public adept.common.TimePhrase mapTimePhrase(edu.jhu.hlt.concrete.Concrete.Entity entity) {
		adept.common.Document document = new adept.common.Document("", null, "", "", "");
		document.setValue("");
		adept.common.TokenStream tokenStream = new adept.common.TokenStream(null, null, null, null, null, document);
		tokenStream.add(new adept.common.Token(0, new adept.common.CharOffset(0,0), null));
		adept.common.TimePhrase timePhrase = new adept.common.TimePhrase(new adept.common.TokenOffset(0, 0), tokenStream, null);
		mapper.map(entity, timePhrase);
		
		try {
			timePhrase.setType(adept.common.EntityTypeFactory.getInstance().getType(entity.getEntityType().toString()));
		}
		catch (Exception e) {
		}

		timePhrase.setResolution(entity.getCanonicalName());
		return timePhrase;
	}

	public adept.common.EventPhrase mapEventPhrase(edu.jhu.hlt.concrete.Concrete.Situation situation) {
		adept.common.Document document = new adept.common.Document("", null, "", "", "");
		document.setValue("");
		adept.common.TokenStream tokenStream = new adept.common.TokenStream(null, null, null, null, null, document);
		tokenStream.add(new adept.common.Token(0, new adept.common.CharOffset(0,0), null));


		adept.common.IType tense = adept.common.OntTypeFactory.getInstance().getType("EVENTPHRASE", situation.getTimemlTense().toString());

		adept.common.IType aspect = adept.common.OntTypeFactory.getInstance().getType("EVENTPHRASE", situation.getTimemlAspect().toString());

		adept.common.IType eventClass = adept.common.OntTypeFactory.getInstance().getType("EVENTPHRASE", situation.getTimemlClass().toString());

		adept.common.EventPhrase eventPhrase = new adept.common.EventPhrase(new adept.common.TokenOffset(0, 0), tokenStream, tense, aspect, eventClass);
		mapper.map(situation, eventPhrase);
		return eventPhrase;
	}
		
	/** Adept to Concrete mapping methods
	 *  Concrete objects use builder pattern and don't have empty default
	 *  constructors assumed by Dozer, so default objects are initialized
	 */
	public edu.jhu.hlt.concrete.Concrete.UUID map(adept.common.ID id) {
		edu.jhu.hlt.concrete.Concrete.UUID uuid = edu.jhu.hlt.concrete.Concrete.UUID.getDefaultInstance();
		adept.mappers.concrete.ID tempId = new adept.mappers.concrete.ID();
		tempId.setId(id.getId());		
		mapper.map(tempId, uuid);
		return uuid;
	}

	public edu.jhu.hlt.concrete.Concrete.UUID map(java.util.UUID id) {
		edu.jhu.hlt.concrete.Concrete.UUID.Builder uuidBuilder = edu.jhu.hlt.concrete.Concrete.UUID.newBuilder();
		uuidBuilder.setHigh(id.getMostSignificantBits());
		uuidBuilder.setLow(id.getLeastSignificantBits());
		edu.jhu.hlt.concrete.Concrete.UUID uuid = uuidBuilder.build();
		return uuid;
	}

	public edu.jhu.hlt.concrete.Concrete.CommunicationGUID map(String corpusId, String docId) {
		edu.jhu.hlt.concrete.Concrete.CommunicationGUID guid = edu.jhu.hlt.concrete.Concrete.CommunicationGUID.newBuilder()
			.setCorpusName(corpusId)
			.setCommunicationId(docId)
			.build();
		return guid;
	}

	public edu.jhu.hlt.concrete.Concrete.Communication map(adept.common.HltContentContainer hltContentContainer, adept.common.Audio audio, List<adept.common.LanguageIdentification> languageIdentifications, List<adept.common.EntityMention> entitySituationMentions) {
		edu.jhu.hlt.concrete.Concrete.Communication prototype = map(hltContentContainer);
		List<edu.jhu.hlt.concrete.Concrete.LanguageIdentification> languageIdentificationList = new ArrayList<edu.jhu.hlt.concrete.Concrete.LanguageIdentification>();
		for (adept.common.LanguageIdentification languageIdentification : languageIdentifications) {
			languageIdentificationList.add(map(languageIdentification));
		}

		List<edu.jhu.hlt.concrete.Concrete.SituationMention> situationMentions = new ArrayList<edu.jhu.hlt.concrete.Concrete.SituationMention>();
		for (adept.common.EntityMention entitySituationMention : entitySituationMentions) {
			situationMentions.add(mapSituationMention(entitySituationMention));
		}
		edu.jhu.hlt.concrete.Concrete.SituationMentionSet situationMentionSet = edu.jhu.hlt.concrete.Concrete.SituationMentionSet.newBuilder()
			.setUuid(map(new adept.common.ID()))
			.addAllMention(situationMentions)
			.build();

		List<edu.jhu.hlt.concrete.Concrete.SituationMentionSet> situationMentionSets = new ArrayList<edu.jhu.hlt.concrete.Concrete.SituationMentionSet>();
		situationMentionSets.add(situationMentionSet);

		edu.jhu.hlt.concrete.Concrete.Communication communication = edu.jhu.hlt.concrete.Concrete.Communication.newBuilder(prototype)
			.setAudio(map(audio))
			.setStartTime((long) audio.getAudioOffset().getBegin())
			.setEndTime((long) audio.getAudioOffset().getEnd())
			.addAllLanguageId(languageIdentificationList)
			.addAllSituationMentionSet(situationMentionSets)
			.build();
		return communication;
	}

	public edu.jhu.hlt.concrete.Concrete.Communication map(adept.common.HltContentContainer hltContentContainer) {
		String corpusId = hltContentContainer.getSentences().get(0).getTokenStream().getDocument().getCorpus().getCorpusId();
		String docId = hltContentContainer.getSentences().get(0).getTokenStream().getDocument().getDocId();
		edu.jhu.hlt.concrete.Concrete.CommunicationGUID guid = map(corpusId, docId);
		List<edu.jhu.hlt.concrete.Concrete.SectionSegmentation> sectionSegmentations = new ArrayList<edu.jhu.hlt.concrete.Concrete.SectionSegmentation>();
		for (adept.common.Session session : hltContentContainer.getSessions()) {
			edu.jhu.hlt.concrete.Concrete.SentenceSegmentation sentenceSegmentation = map(session);
			edu.jhu.hlt.concrete.Concrete.Section section = edu.jhu.hlt.concrete.Concrete.Section.newBuilder()
				.setUuid(map(new adept.common.ID()))
				.addSentenceSegmentation(sentenceSegmentation)
				.build();
			edu.jhu.hlt.concrete.Concrete.SectionSegmentation sectionSegmentation = edu.jhu.hlt.concrete.Concrete.SectionSegmentation.newBuilder()
				.setUuid(map(new adept.common.ID()))
				.addSection(section)
				.build();
			sectionSegmentations.add(sectionSegmentation);
		}

		List<edu.jhu.hlt.concrete.Concrete.EntityMention> entityMentions = new ArrayList<edu.jhu.hlt.concrete.Concrete.EntityMention>();
		for (adept.common.EntityMention adeptEntityMention : hltContentContainer.getEntityMentions()) {
			edu.jhu.hlt.concrete.Concrete.EntityMention entityMention = map(adeptEntityMention);
			entityMentions.add(entityMention);
		}

		edu.jhu.hlt.concrete.Concrete.EntityMentionSet entityMentionSet = edu.jhu.hlt.concrete.Concrete.EntityMentionSet.newBuilder()
			.setUuid(map(new adept.common.ID()))
			.addAllMention(entityMentions)
			.build();

		edu.jhu.hlt.concrete.Concrete.Communication communication = edu.jhu.hlt.concrete.Concrete.Communication.newBuilder()
			.setGuid(guid)
			.setUuid(map(new adept.common.ID()))
			.setText(hltContentContainer.getValue())
			.addAllSectionSegmentation(sectionSegmentations)
			.addEntityMentionSet(entityMentionSet)
			.build();
		return communication;
	}

	public edu.jhu.hlt.concrete.Concrete.LanguageIdentification map(adept.common.LanguageIdentification languageIdentification) {
		edu.jhu.hlt.concrete.Concrete.LanguageIdentification.Builder languageIdentificationBuilder = edu.jhu.hlt.concrete.Concrete.LanguageIdentification.newBuilder();
		for (String language : languageIdentification.getLanguageProbabilityDistribution().keySet()) {
			edu.jhu.hlt.concrete.Concrete.LanguageIdentification.LanguageProb languageProb = edu.jhu.hlt.concrete.Concrete.LanguageIdentification.LanguageProb.newBuilder()
				.setLanguage(language)
				.setProbability(languageIdentification.getLanguageProbabilityDistribution().get(language))
				.build();
			languageIdentificationBuilder = languageIdentificationBuilder.addLanguage(languageProb);
		}

		edu.jhu.hlt.concrete.Concrete.LanguageIdentification retLanguageIdentification = languageIdentificationBuilder.build();
		mapper.map(languageIdentification, retLanguageIdentification);
		return retLanguageIdentification;
	}

	public edu.jhu.hlt.concrete.Concrete.EmailAddress map(adept.common.EmailAddress emailAddress) {
		edu.jhu.hlt.concrete.Concrete.EmailAddress retEmailAddress = edu.jhu.hlt.concrete.Concrete.EmailAddress.getDefaultInstance();
		mapper.map(emailAddress, retEmailAddress);
		return retEmailAddress;
	}


	public edu.jhu.hlt.concrete.Concrete.TextSpan map(adept.common.CharOffset charOffset) {
		edu.jhu.hlt.concrete.Concrete.TextSpan textSpan = edu.jhu.hlt.concrete.Concrete.TextSpan.getDefaultInstance();
		mapper.map(charOffset, textSpan);
		return textSpan;
	}

	public edu.jhu.hlt.concrete.Concrete.Sentence map(adept.common.Sentence adeptSentence) {
		edu.jhu.hlt.concrete.Concrete.Sentence prototype = edu.jhu.hlt.concrete.Concrete.Sentence.getDefaultInstance();
		mapper.map(adeptSentence, prototype);
		edu.jhu.hlt.concrete.Concrete.TextSpan textSpan = edu.jhu.hlt.concrete.Concrete.TextSpan.newBuilder()
			.setStart(adeptSentence.getTokenStream().get(0).getCharOffset().getBegin())
			.setEnd(adeptSentence.getTokenStream().get(adeptSentence.getTokenStream().size()-1).getCharOffset().getEnd())
			.build();

		edu.jhu.hlt.concrete.Concrete.Sentence sentence = edu.jhu.hlt.concrete.Concrete.Sentence.newBuilder(prototype)
			.addTokenization(map(adeptSentence.getTokenStream()))
			.setTextSpan(textSpan)
			.build();
		return sentence;
	}

	public edu.jhu.hlt.concrete.Concrete.AudioSpan map(adept.common.AudioOffset audioOffset) {
		edu.jhu.hlt.concrete.Concrete.AudioSpan audioSpan = edu.jhu.hlt.concrete.Concrete.AudioSpan.getDefaultInstance();
		mapper.map(audioOffset, audioSpan);
		return audioSpan;
	}

	public edu.jhu.hlt.concrete.Concrete.SentenceSegmentation map(adept.common.Session session) {
		edu.jhu.hlt.concrete.Concrete.SentenceSegmentation sentenceSegmentation = edu.jhu.hlt.concrete.Concrete.SentenceSegmentation.getDefaultInstance();
		mapper.map(session, sentenceSegmentation);
		return sentenceSegmentation;
	}

	public edu.jhu.hlt.concrete.Concrete.Token map(adept.common.Token adeptToken) {
		edu.jhu.hlt.concrete.Concrete.Token token = edu.jhu.hlt.concrete.Concrete.Token.getDefaultInstance();
		mapper.map(adeptToken, token);
		return token;
	}

	public edu.jhu.hlt.concrete.Concrete.Sound map(adept.common.Audio audio) {
		edu.jhu.hlt.concrete.Concrete.Sound.Builder soundBuilder = edu.jhu.hlt.concrete.Concrete.Sound.newBuilder();
		switch (audio.getAudioType()) {
			case WAV: soundBuilder = soundBuilder.setWav(com.google.protobuf.ByteString.copyFrom(audio.getAudioBuffer()));
				break;
			case SPH: soundBuilder = soundBuilder.setSph(com.google.protobuf.ByteString.copyFrom(audio.getAudioBuffer()));
				break;
			case MP3: soundBuilder = soundBuilder.setMp3(com.google.protobuf.ByteString.copyFrom(audio.getAudioBuffer()));
				break;
		}
		edu.jhu.hlt.concrete.Concrete.Sound sound = soundBuilder.build();
		mapper.map(audio, sound);
		return sound;
	}

	public edu.jhu.hlt.concrete.Concrete.EntityMention map(adept.common.EntityMention adeptEntityMention) {
		adept.common.TokenOffset tokenOffset = adeptEntityMention.getTokenOffset();
		edu.jhu.hlt.concrete.Concrete.TokenRefSequence.Builder tokenRefSequenceBuilder = edu.jhu.hlt.concrete.Concrete.TokenRefSequence.newBuilder();
		for (int i = (int)tokenOffset.getBegin(); i <= (int)tokenOffset.getEnd(); i++) {
			tokenRefSequenceBuilder.addTokenIndex(i);
		}

		edu.jhu.hlt.concrete.Concrete.TokenRefSequence tokenRefSequence = tokenRefSequenceBuilder.build();
	
		edu.jhu.hlt.concrete.Concrete.EntityMention.Builder entityMentionBuilder = edu.jhu.hlt.concrete.Concrete.EntityMention.newBuilder();
		entityMentionBuilder.setTokens(tokenRefSequence);
		entityMentionBuilder.setText(adeptEntityMention.getValue());
		entityMentionBuilder.setUuid(map(adeptEntityMention.getId()));
		edu.jhu.hlt.concrete.Concrete.EntityMention entityMention = entityMentionBuilder.build();
		return entityMention;
	}

	public edu.jhu.hlt.concrete.Concrete.Tokenization map(adept.common.TokenStream tokenStream) {
                edu.jhu.hlt.concrete.Concrete.TokenTagging.TaggedToken taggedToken = edu.jhu.hlt.concrete.Concrete.TokenTagging.TaggedToken.newBuilder()
			.setTag(tokenStream.getLanguage())
			.build();
		List<edu.jhu.hlt.concrete.Concrete.TokenTagging.TaggedToken> taggedTokenList = new ArrayList<edu.jhu.hlt.concrete.Concrete.TokenTagging.TaggedToken>();
		taggedTokenList.add(taggedToken);
                edu.jhu.hlt.concrete.Concrete.TokenTagging tokenTagging = edu.jhu.hlt.concrete.Concrete.TokenTagging.newBuilder()
			.setUuid(map(new adept.common.ID()))
			.addAllTaggedToken(taggedTokenList)
			.build();
                edu.jhu.hlt.concrete.Concrete.Tokenization prototype = edu.jhu.hlt.concrete.Concrete.Tokenization.newBuilder()
			.setUuid(map(new adept.common.ID()))
			.setLangIds(0, tokenTagging)
			.build();
		mapper.map(tokenStream, prototype);
		List<edu.jhu.hlt.concrete.Concrete.Token> tokenList = new ArrayList<edu.jhu.hlt.concrete.Concrete.Token>();
		for (adept.common.Token tok : tokenStream) {
			tokenList.add(map(tok));
		}
                edu.jhu.hlt.concrete.Concrete.Tokenization tokenization = edu.jhu.hlt.concrete.Concrete.Tokenization.newBuilder(prototype)
			.addAllToken(tokenList)
			.setKind(edu.jhu.hlt.concrete.Concrete.Tokenization.Kind.TOKEN_LIST)
			.build();
		
		return tokenization;
	}

	public edu.jhu.hlt.concrete.Concrete.SituationMention mapSituationMention(adept.common.EntityMention entityMention) {
		edu.jhu.hlt.concrete.Concrete.SituationMention prototype = edu.jhu.hlt.concrete.Concrete.SituationMention.getDefaultInstance();
		edu.jhu.hlt.concrete.Concrete.Tokenization tokenization = map(entityMention.getTokenStream());
		edu.jhu.hlt.concrete.Concrete.TextSpan textSpan = edu.jhu.hlt.concrete.Concrete.TextSpan.newBuilder()
			.setStart(entityMention.getTokenStream().get(0).getCharOffset().getBegin())
			.setEnd(entityMention.getTokenStream().get(entityMention.getTokenStream().size()-1).getCharOffset().getEnd())
			.build();
		List<Integer> tokenIndices = new ArrayList<Integer>();
		int i = 1;
		for (adept.common.Token token : entityMention.getTokenStream()) {
			tokenIndices.add(i);
			i++;
		}
		edu.jhu.hlt.concrete.Concrete.TokenRefSequence tokenRefSequence = edu.jhu.hlt.concrete.Concrete.TokenRefSequence.newBuilder()
			.setTokenizationId(tokenization.getUuid())
			.setTextSpan(textSpan)
			.addAllTokenIndex(tokenIndices)
			.build();

		mapper.map(entityMention, prototype);
		edu.jhu.hlt.concrete.Concrete.SituationMention situationMention = edu.jhu.hlt.concrete.Concrete.SituationMention.newBuilder(prototype)
			.setTokens(tokenRefSequence)
			.build();

		return situationMention;
	}

	public edu.jhu.hlt.concrete.Concrete.Situation map(adept.common.EventPhrase eventPhrase) {
		edu.jhu.hlt.concrete.Concrete.Situation prototype = edu.jhu.hlt.concrete.Concrete.Situation.getDefaultInstance();
		mapper.map(eventPhrase, prototype);

		for (edu.jhu.hlt.concrete.Concrete.Situation.TimeMLClass classVal : edu.jhu.hlt.concrete.Concrete.Situation.TimeMLClass.values()) {

			String s = eventPhrase.getEventClass().toLowerCase();
			s = s.substring(s.lastIndexOf("#") + 1);
			if (s.equals(classVal.toString().toLowerCase())) {
				prototype = edu.jhu.hlt.concrete.Concrete.Situation.newBuilder(prototype)
					.setUuid(prototype.getUuid())
					.setTimemlClass(edu.jhu.hlt.concrete.Concrete.Situation.TimeMLClass.valueOf(s.toUpperCase()))
					.build();
			}
			
		}

		for (edu.jhu.hlt.concrete.Concrete.Situation.TimeMLTense classVal : edu.jhu.hlt.concrete.Concrete.Situation.TimeMLTense.values()) {

			String s = eventPhrase.getTense().toLowerCase();
			s = s.substring(s.lastIndexOf("#") + 1);
			if (s.equals(classVal.toString().toLowerCase())) {
				prototype = edu.jhu.hlt.concrete.Concrete.Situation.newBuilder(prototype)
					.setUuid(prototype.getUuid())
					.setTimemlTense(edu.jhu.hlt.concrete.Concrete.Situation.TimeMLTense.valueOf(s.toUpperCase()))
					.build();
			}
		}

		for (edu.jhu.hlt.concrete.Concrete.Situation.TimeMLAspect classVal : edu.jhu.hlt.concrete.Concrete.Situation.TimeMLAspect.values()) {

			String s = eventPhrase.getAspect().toLowerCase();
			s = s.substring(s.lastIndexOf("#") + 1);
			if (s.equals(classVal.toString().toLowerCase())) {
				prototype = edu.jhu.hlt.concrete.Concrete.Situation.newBuilder(prototype)
					.setUuid(prototype.getUuid())
					.setTimemlAspect(edu.jhu.hlt.concrete.Concrete.Situation.TimeMLAspect.valueOf(s.toUpperCase()))
					.build();
			}
		}

		return prototype;
	}

	public edu.jhu.hlt.concrete.Concrete.Entity map(adept.common.TimePhrase timePhrase) {
		edu.jhu.hlt.concrete.Concrete.Entity prototype = edu.jhu.hlt.concrete.Concrete.Entity.getDefaultInstance();
		mapper.map(timePhrase, prototype);

		for (edu.jhu.hlt.concrete.Concrete.Entity.Type typeVal : edu.jhu.hlt.concrete.Concrete.Entity.Type.values()) {

			String s = timePhrase.getType().toLowerCase();
			s = s.substring(s.lastIndexOf("#") + 1);
			if (s.equals(typeVal.toString().toLowerCase())) {
				prototype = edu.jhu.hlt.concrete.Concrete.Entity.newBuilder(prototype)
					.setUuid(prototype.getUuid())
					.setEntityType(edu.jhu.hlt.concrete.Concrete.Entity.Type.valueOf(s.toUpperCase()))
					.build();
			}
		}

		return prototype;
	}
		
}
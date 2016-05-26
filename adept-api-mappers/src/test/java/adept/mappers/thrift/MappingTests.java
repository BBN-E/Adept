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

package adept.mappers.thrift;

import com.google.common.base.Charsets;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.io.Files;

import org.apache.thrift.TDeserializer;
import org.apache.thrift.TException;
import org.apache.thrift.TSerializer;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import adept.common.Morph;
import adept.common.MorphFeature;
import adept.common.MorphToken;
import adept.common.MorphType;
import adept.common.Sentence;
import adept.common.Token;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenType;
import adept.common.TokenizerType;
import adept.metadata.SourceAlgorithm;
import adept.serialization.SerializationType;
import adept.serialization.XMLSerializer;

import static org.junit.Assert.assertTrue;

public final class MappingTests {

  private static final Logger log = LoggerFactory.getLogger(MappingTests.class);

  public static adept.common.TokenStream generateTokenStream() {
    adept.common.Document documentAdept = generateDocument();
    adept.common.CharOffset charOffsetAdept = new adept.common.CharOffset(0, 6);

    adept.common.Token token1Adept = new adept.common.Token(1, charOffsetAdept, "Token1");
    token1Adept.setConfidence((float) 0.1);
    token1Adept.setLemma("AToT lemma 1");
    token1Adept.setTokenType(TokenType.OTHER);

    adept.common.Token token2Adept = new adept.common.Token(2, charOffsetAdept, "Token2");
    token2Adept.setConfidence((float) 0.2);
    token2Adept.setLemma("AToT lemma 2");
    token2Adept.setTokenType(TokenType.OTHER);

    adept.common.TokenStream tokenStreamAdept =
        new adept.common.TokenStream(adept.common.TokenizerType.ADEPT,
            adept.common.TranscriptType.SOURCE, "AToT English", adept.common.ChannelName.MONO,
            adept.common.ContentType.TEXT, documentAdept);
    tokenStreamAdept.setAsrName(adept.common.AsrName.BBN);
    tokenStreamAdept.setSpeechUnit(adept.common.SpeechUnit.NONE);
    tokenStreamAdept.setTranslatorName(adept.common.TranslatorName.NONE);
    tokenStreamAdept.add(token1Adept);
    tokenStreamAdept.add(token2Adept);

    return tokenStreamAdept;
  }

  public static adept.common.TokenStream generateEmptyTokenStream() {
    adept.common.Document documentAdept = generateDocument();
    adept.common.TokenStream tokenStreamAdept =
        new adept.common.TokenStream(adept.common.TokenizerType.ADEPT,
            adept.common.TranscriptType.SOURCE, "AToT English", adept.common.ChannelName.MONO,
            adept.common.ContentType.TEXT, documentAdept);
    return tokenStreamAdept;
  }

  public static adept.common.TokenOffset generateTokenOffset() {
    return new adept.common.TokenOffset(0, 0);
  }

  private static ImmutableList<adept.common.MorphFeature> generateMorphFeatures() {
    final ImmutableList.Builder<adept.common.MorphFeature> ret = ImmutableList.builder();
    ret.add(MorphFeature.create("prop1", "val1"));
    ret.add(MorphFeature.create("prop2", "val2"));
    return ret.build();
  }

  private static ImmutableList<Morph> generateMorphs(final TokenStream tokenStream) {
    final String TEST = "test";
    final String TEXT = "Hello worlds.";
    final String HELLO = "Hello";
    final String WORLDS = "worlds";
    final String PERIOD = ".";
    final String WORLDS_LEMMA = "world";
    final String WORLDS_NOTE = "world s";
    final String NOUN = "NOUN";
    final String ROOT = "ROOT";
    final String SUFFIX = "SUFFIX";

    final MorphType root = MorphType.create(ROOT);
    final MorphType suffix = MorphType.create(SUFFIX);

    // Basic text
    final MorphToken mt1 = MorphToken.builder(HELLO, tokenStream).build();

    // With fields filled
    final TokenOffset worldsOffset = new TokenOffset(1, 1);
    final MorphFeature feature = MorphFeature.create("NUM", "PL");
    final Morph stemMorph = Morph.builder(WORLDS_LEMMA, root).build();
    final Morph suffixMorph = Morph.builder("s", suffix)
        .setFeatures(ImmutableList.of(feature))
        .build();

    return ImmutableList.of(stemMorph, suffixMorph);
  }

  private static adept.common.MorphToken generateMorphToken(final Token t,
      final TokenStream tokenStream) {
    adept.common.MorphToken.Builder ret =
        adept.common.MorphToken.builder(t.getValue(), tokenStream);
    ret.setConfidence(1.0f);
    if (t.getLemma() != null) {
      ret.setLemma(t.getLemma());
    }
    ret.setFeatures(generateMorphFeatures());
    ret.setHeadToken(new TokenOffset(1, 1));
    ret.setMorphs(generateMorphs(tokenStream));
    ret.setNotes("notes");
    ret.setPos("pos!");
    ret.setRoots(generateRoots());

    return ret.build();
  }

  private static Iterable<String> generateRoots() {
    return ImmutableList.of("world", "s");
  }


  public static adept.common.MorphTokenSequence generateMorphTokenSequence(
      final TokenStream tokenStream) {
    final adept.common.MorphTokenSequence.Builder ret = adept.common.MorphTokenSequence.builder(new SourceAlgorithm("t","t"));
    ret.setConfidence(1.0f);
    for (final adept.common.Token t : tokenStream) {
      ret.addToken(generateMorphToken(t, tokenStream));
    }
    return ret.build();
  }

  public static adept.common.MorphSentence generateMorphSentence(final TokenStream tokenStream) {
    final adept.common.MorphSentence.Builder ret = adept.common.MorphSentence.builder();
    ret.addSequence(generateMorphTokenSequence(tokenStream));
    return ret.build();
  }

  public static adept.common.Sentence generateSentence() {
    adept.common.TokenStream tokenStream = generateTokenStream();
    adept.common.Sentence sentenceAdept =
        new adept.common.Sentence(1, generateTokenOffset(), tokenStream);
    sentenceAdept.setUncertaintyConfidence((float) 0.1);
    sentenceAdept.setNoveltyConfidence((float) 0.1);
    sentenceAdept.setPunctuation("AToT Punctuation");
    sentenceAdept.setType(adept.common.SentenceType.NONE);
    sentenceAdept.setMorphSentence(generateMorphSentence(tokenStream));

    return sentenceAdept;
  }

  public static adept.common.Corpus generateCorpus() {
    return new adept.common.Corpus("Corpus001", "AToTType", "AToT Corpus", "AtoT uri");
  }

  public static adept.common.Document generateDocument() {
    adept.common.Corpus corpusAdept = generateCorpus();
    adept.common.Document documentAdept =
        new adept.common.Document("Doc001", corpusAdept, "AToTDocType", "AToTDoc uri",
            "AToT English");
    documentAdept.setGenre("AToT genre");
    documentAdept.setHeadline("AToT headlne");
    documentAdept.setValue("Token1 Token2");
    return documentAdept;
  }

  public static adept.common.EntityMention generateEntityMentions() {
    final adept.common.EntityMention entityMentionAdept =
        new adept.common.EntityMention(1, generateTokenOffset(), generateTokenStream());
    entityMentionAdept.addEntityConfidencePair(1, (float) 0.1);
    entityMentionAdept.setEntityType(new adept.common.Type("AToT Entity Type"));
    entityMentionAdept.setMentionType(new adept.common.Type("AToT Mention Type"));
    entityMentionAdept.setTokenizerType(TokenizerType.OTHER);
    return entityMentionAdept;
  }

  public static adept.common.EntityMention generateLaxEntityMentions() {
    final adept.common.EntityMention entityMentionAdept =
        new adept.common.EntityMention(1, generateTokenOffset(), generateTokenStream());
    entityMentionAdept.setEntityType(new adept.common.Type("AToT Entity Type"));
    entityMentionAdept.setMentionType(new adept.common.Type("AToT Mention Type"));
    entityMentionAdept.setTokenizerType(TokenizerType.OTHER);
    return entityMentionAdept;
  }

  public static adept.common.HltContentContainer generateContainer() {
    adept.common.EntityMention entityMentionAdept = generateEntityMentions();

    List<adept.common.EntityMention> entityMentionsAdepts = Lists.newArrayList();
    entityMentionsAdepts.add(entityMentionAdept);

    adept.common.Sentence sentenceAdept = generateSentence();
    adept.common.HltContentContainer hltContentContainerAdept =
        new adept.common.HltContentContainer();

    List<adept.common.Sentence> sentencesAdept = new ArrayList<Sentence>();
    sentencesAdept.add(sentenceAdept);
    hltContentContainerAdept.setSentences(sentencesAdept);
    hltContentContainerAdept.setEntityMentions(entityMentionsAdepts);
    return hltContentContainerAdept;
  }

  public static adept.common.HltContentContainer generateContainerWithOnlyLaxMentions() {
    adept.common.EntityMention entityMentionAdept = generateLaxEntityMentions();
    List<adept.common.EntityMention> entityMentionsAdepts = Lists.newArrayList();
    entityMentionsAdepts.add(entityMentionAdept);
    adept.common.HltContentContainer hltContentContainerAdept =
        new adept.common.HltContentContainer();
    hltContentContainerAdept.setEntityMentions(entityMentionsAdepts);
    return hltContentContainerAdept;
  }

  public static adept.common.HltContentContainer generateContainerWithOnlySentences() {
    adept.common.Sentence sentenceAdept = generateSentence();
    adept.common.HltContentContainer hltContentContainerAdept =
        new adept.common.HltContentContainer();

    List<adept.common.Sentence> sentencesAdept = new ArrayList<Sentence>();
    sentencesAdept.add(sentenceAdept);
    hltContentContainerAdept.setSentences(sentencesAdept);
    return hltContentContainerAdept;
  }

  public static <X, Y> boolean areTheSameByString(final X a, final Y b) {
    if (a == null || b == null) {
      return a == null && b == null;
    }
    return a.toString().equals(b.toString());
  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.CharOffset adept,
      final thrift.adept.common.CharOffset thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }
    return thrift.getBeginIndex() == adept.getBegin() && thrift.getEndIndex() == adept.getEnd();
  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.TokenOffset adept,
      final thrift.adept.common.TokenOffset thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }
    return thrift.getBeginIndex() == adept.getBegin() && thrift.getEndIndex() == adept.getEnd();
  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.Token adept,
      final thrift.adept.common.Token thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }
    return thrift.getSequenceId() == adept.getSequenceId()
        && areTheSameByLimitedThriftScope(adept.getCharOffset(), thrift.getCharOffset())
        && areTheSameByString(adept.getLemma(), thrift.getLemma())
        && areTheSameByString(adept.getTokenType(), thrift.getTokenType())
        && areTheSameByString(adept.getValue(), thrift.getValue());
  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.TokenStream adept,
      final thrift.adept.common.TokenStream thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }
    if (adept.size() != thrift.getTokenListSize()) {
      return false;
    }

    return areTheSameByString(adept.getChannelName(), thrift.getChannelName())
        && areTheSameByString(adept.getContentType(), thrift.getContentType())
        && areTheSameByLimitedThriftScope(adept.getDocument(), thrift.getDocument())
        && tokensAreTheSame(adept, thrift.getTokenList());
  }

  public static boolean tokensAreTheSame(final List<adept.common.Token> adepts,
      final List<thrift.adept.common.Token> thrifts) {
    if (adepts.size() != thrifts.size()) {
      return false;
    }
    for (int i = 0; i < adepts.size() && i < thrifts.size(); i++) {
      if (!areTheSameByLimitedThriftScope(adepts.get(i),
          thrifts.get(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.EntityMention adept,
      final thrift.adept.common.EntityMention thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }
    return areTheSameByLimitedThriftScope(adept.getCharOffset(), thrift.getCharOffset())
        && areTheSameByLimitedThriftScope(adept.getTokenOffset(), thrift.getTokenOffset())
        && areTheSameByLimitedThriftScope(adept.getTokenStream(), thrift.getTokenStream())
        && areTheSameByString(adept.getMentionType().getType(), thrift.getMentionType().getType())
        && areTheSameByString(adept.getEntityType().getType(), thrift.getEntityType().getType())
        && thrift.getSequenceId() == adept.getSequenceId();
  }

  // can't generify without generifying an "areTheSameByLimitedThriftScope" which is undesirable.
  public static boolean entityMentionsAreTheSame(final List<adept.common.EntityMention> adepts,
      List<thrift.adept.common.EntityMention> thrifts) {
    if (thrifts == null || adepts == null) {
      return adepts == null && thrifts == null;
    }
    final Set<thrift.adept.common.EntityMention> found = Sets.newHashSet();
    // check that every adept object has a mapping
    for (final adept.common.EntityMention adept : adepts) {
      boolean foundOne = false;
      for (final thrift.adept.common.EntityMention thrift : thrifts) {
        foundOne = areTheSameByLimitedThriftScope(adept, thrift);
        if (foundOne) {
          found.add(thrift);
          break;
        }
      }
      if (!foundOne) {
        return false;
      }
    }
    // check that every thrift object has a mapping
    return found.size() == thrifts.size();
  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.Document adept,
      final thrift.adept.common.Document thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }
    return areTheSameByLimitedThriftScope(adept.getCorpus(), thrift.getCorpus())
        && areTheSameByString(thrift.getDocId(), adept.getDocId())
        && areTheSameByString(thrift.getDocType(), adept.getDocType())
        && areTheSameByString(thrift.getGenre(), adept.getGenre())
        && areTheSameByString(thrift.getHeadline(), adept.getHeadline())
        && areTheSameByString(thrift.getLanguage(), adept.getLanguage())
        && areTheSameByString(thrift.getUri(), adept.getUri());

  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.Corpus adept,
      final thrift.adept.common.Corpus thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }
    return areTheSameByString(thrift.getCorpusId(), adept.getCorpusId())
        && areTheSameByString(thrift.getName(), adept.getName())
        && areTheSameByString(thrift.getType(), adept.getType())
        && areTheSameByString(thrift.getUri(), adept.getUri());
  }


  public static boolean areTheSameByLimitedThriftScope(final adept.common.HltContentContainer adept,
      final thrift.adept.common.HltContentContainer thrift) {
    if (adept == null) {
      return thrift == null;
    }
    return entityMentionsAreTheSame(adept.getEntityMentions(),
        thrift.getEntityMentions())
        && sentencesAreTheSame(adept.getSentences(), thrift.getSentences());
  }

  public static boolean sentencesAreTheSame(final List<adept.common.Sentence> adepts,
      final List<thrift.adept.common.Sentence> thrifts) {
    if (thrifts == null || adepts == null) {
      return adepts == null && thrifts == null;
    }
    final Set<thrift.adept.common.Sentence> found = Sets.newHashSet();
    // check that every adept object has a mapping
    for (final adept.common.Sentence adept : adepts) {
      boolean foundOne = false;
      for (final thrift.adept.common.Sentence thrift : thrifts) {
        foundOne = areTheSameByLimitedThriftScope(adept, thrift);
        if (foundOne) {
          found.add(thrift);
          break;
        }
      }
      if (!foundOne) {
        return false;
      }
    }
    // check that every thrift object has a mapping
    return found.size() == thrifts.size();


  }

  public static boolean areTheSameByLimitedThriftScope(final adept.common.Sentence adept,
      final thrift.adept.common.Sentence thrift) {
    if (thrift == null || adept == null) {
      return adept == null && thrift == null;
    }

    return areTheSameByString(thrift.getType(), adept.getType())
        && adept.getSequenceId() == thrift.getSequenceId()
        && areTheSameByString(thrift.getAlgorithmName(), adept.getAlgorithmName())
        && areTheSameByLimitedThriftScope(adept.getTokenOffset(), thrift.getTokenOffset())
        && areTheSameByLimitedThriftScope(adept.getTokenStream(), thrift.getTokenStream())
        && areTheSameByLimitedThriftScope(adept.getCharOffset(), thrift.getCharOffset())
        && areTheSameByLimitedThriftScope(adept.getMorphSentence(), thrift.getMorphSentence());
  }

  private static boolean areTheSameByLimitedThriftScope(
      final Optional<adept.common.MorphSentence> adept,
      final thrift.adept.common.MorphSentence thrift) {
    if (!adept.isPresent() || thrift == null) {
      return !adept.isPresent() && thrift == null;
    }
    final List<adept.common.MorphTokenSequence> adeptSequence = adept.get().morphTokenSequences();
    final List<thrift.adept.common.MorphTokenSequence> thriftSequence = thrift.getSequences();
    if(adeptSequence == null || thriftSequence == null) {
      return adeptSequence == null && thriftSequence == null;
    }
    if (adeptSequence.size() != thriftSequence.size()) {
      return false;
    }
    for (int i = 0; i < adeptSequence.size(); i++) {
      if (!areTheSameByLimitedThriftScope(adeptSequence.get(i), thriftSequence.get(i))) {
        return false;
      }
    }
    return true;
  }

  private static boolean areTheSameByLimitedThriftScope(final adept.common.MorphTokenSequence adept,
      final thrift.adept.common.MorphTokenSequence thrift) {
    // not even going to bother with precision issues
    // dozer doesn't set the bitfields for thrift, so we'll just take it on faith that this worked.
//    if (!adept.confidence().isPresent() || !thrift.isSetConfidence()) {
//      return adept.confidence().isPresent() == thrift.isSetConfidence();
//    }

    return morphTokensAreTheSame(adept.tokens(), thrift.getTokens()) &&
        areTheSameByLimitedThriftScope(adept.sourceAlgorithm(), thrift.getSourceAlgorithm());
  }

  private static boolean areTheSameByLimitedThriftScope(final adept.metadata.SourceAlgorithm adept,
      final thrift.adept.common.SourceAlgorithm thrift) {
    return adept.getAlgorithmName().equals(thrift.getAlgorithmName()) &&
        adept.getContributingSiteName().equals(thrift.getContributingSiteName());
  }

  private static boolean morphTokensAreTheSame(final List<adept.common.MorphToken> adepts,
      final List<thrift.adept.common.MorphToken> thrifts) {
    if (adepts.size() != thrifts.size()) {
      return false;
    }
    for (int i = 0; i < adepts.size(); i++) {
      if (!areTheSameByLimitedThriftScope(adepts.get(i), thrifts.get(i))) {
        return false;
      }
    }
    return true;
  }


  private static boolean areTheSameByLimitedThriftScope(final adept.common.MorphToken adept,
      final thrift.adept.common.MorphToken thrift) {
    // not even going to bother with precision issues
    // isSet doesn't appear to work for Dozer mapped objects, so we'll just believe.
//    if (!adept.confidence().isPresent() || !thrift.isSetConfidence()) {
//      return adept.confidence().isPresent() == thrift.isSetConfidence();
//    }

    // check roots
    if (adept.roots().isPresent() && thrift.isSetRoots()) {
      for (int i = 0; i < adept.roots().get().size(); i++) {
        if (!adept.roots().get().get(i).equals(thrift.getRoots().get(i))) {
          return false;
        }
      }
    } else {
      if (adept.roots().isPresent() ^ thrift.isSetRoots()) {
        return false;
      }
    }
    // check morphs
    if (adept.morphs().isPresent() && thrift.isSetMorphs()) {
      for (int i = 0; i < adept.morphs().get().size(); i++) {
        if (!areTheSameByLimitedThriftScope(adept.morphs().get().get(i),
            thrift.getMorphs().get(i))) {
          return false;
        }
      }
    } else {
      if (adept.morphs().isPresent() ^ thrift.isSetMorphs()) {
        return false;
      }
    }
    // check token offsets
    if (adept.tokenOffsets().isPresent() && thrift.isSetTokenOffsets()) {
      for (int i = 0; i < adept.tokenOffsets().get().size(); i++) {
        if (!areTheSameByLimitedThriftScope(adept.tokenOffsets().get().get(i),
            thrift.getTokenOffsets().get(i))) {
          return false;
        }
      }
    } else {
      if (adept.tokenOffsets().isPresent() ^ thrift.isSetTokenOffsets()) {
        return false;
      }
    }

    // check tokenstream
    // check features
    // check text
    // check lemma
    // check headToken
    // check pos
    // check notes
    return areTheSameByLimitedThriftScope(adept.tokenStream(), thrift.getTokenStream()) &&
        morphFeaturesAreTheSame(adept.features(), thrift.getFeatures()) &&
        areTheSameByString(adept.text(), thrift.getText()) &&
        areTheSameByString(adept.lemma().orNull(), thrift.getLemma()) &&
        areTheSameByLimitedThriftScope(adept.headTokenOffset().orNull(), thrift.getHeadTokenOffset()) &&
        areTheSameByString(adept.pos().orNull(), thrift.getPos()) &&
        areTheSameByString(adept.notes().orNull(), thrift.getNotes());
  }

  private static boolean areTheSameByLimitedThriftScope(final adept.common.Morph adept,
      final thrift.adept.common.Morph thrift) {

    return morphFeaturesAreTheSame(adept.features(), thrift.getFeatures()) &&
        areTheSameByString(adept.form(), thrift.getForm()) &&
        areTheSameByLimitedThriftScope(adept.morphType(), thrift.morphType);
  }

  private static boolean areTheSameByLimitedThriftScope(final adept.common.MorphType adept,
      final thrift.adept.common.MorphType thrift) {
    if(adept == null || thrift == null) {
      return adept == null && thrift == null;
    }
    return adept.type().equals(thrift.getType());
  }

  private static boolean morphFeaturesAreTheSame(
      final Optional<ImmutableSet<adept.common.MorphFeature>> adepts,
      final Set<thrift.adept.common.MorphFeature> thrifts) {
    if (!adepts.isPresent() || thrifts == null) {
      return !adepts.isPresent() && thrifts == null;
    }
    final Set<thrift.adept.common.MorphFeature> mappedThrift = Sets.newHashSet();
    final Set<adept.common.MorphFeature> mappedAdept = Sets.newHashSet();
    for (final adept.common.MorphFeature adept : adepts.get()) {
      boolean got = false;
      for (final thrift.adept.common.MorphFeature thrift : thrifts) {
        if (areTheSameByLimitedThriftScope(adept, thrift)) {
          got = true;
          mappedThrift.add(thrift);
          mappedAdept.add(adept);
        }
      }
      if (!got) {
        return false;
      }
    }
    return mappedAdept.size() == mappedThrift.size() && mappedAdept.size() == adepts.get().size();
  }

  private static boolean areTheSameByLimitedThriftScope(final adept.common.MorphFeature adept,
      final thrift.adept.common.MorphFeature thrift) {
    return areTheSameByString(adept.property(), thrift.getProperty()) &&
        areTheSameByString(adept.value(), thrift.getValue());
  }

  @Test
  public void MorphConversionTest() {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    final adept.common.Morph adept = generateMorphs(generateTokenStream()).get(0);
    final thrift.adept.common.Morph thrift = mapper.convert(adept);
    final adept.common.Morph roundTrippedadept = mapper.convert(thrift);
    assertTrue(areTheSameByLimitedThriftScope(adept, thrift));
    assertTrue(areTheSameByLimitedThriftScope(roundTrippedadept, thrift));
  }

  @Test
  public void MorphTokenConversionTest() {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    final adept.common.MorphToken adept = generateMorphToken(generateTokenStream().get(0), generateTokenStream());
    final thrift.adept.common.MorphToken thrift = mapper.convert(adept);
    final adept.common.MorphToken roundTrippedAdept = mapper.convert(thrift);
    assertTrue(areTheSameByLimitedThriftScope(adept, thrift));
    assertTrue(areTheSameByLimitedThriftScope(roundTrippedAdept, thrift));
  }

  @Test
  public void MorphSentenceConversionTest() {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    final adept.common.MorphSentence morphSentence = generateMorphSentence(generateTokenStream());
    final thrift.adept.common.MorphSentence morphSentenceThrift = mapper.convert(morphSentence);
    final adept.common.MorphSentence roundTrippedSentence = mapper.convert(morphSentenceThrift);
    assertTrue(areTheSameByLimitedThriftScope(Optional.of(morphSentence), morphSentenceThrift));
    assertTrue(
        areTheSameByLimitedThriftScope(Optional.of(roundTrippedSentence), morphSentenceThrift));
  }

  @Test
  public void SentenceConversionTest() {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    final adept.common.Sentence sentenceAdept = generateSentence();
    final thrift.adept.common.Sentence sentenceThrift = mapper.convert(sentenceAdept);
    final adept.common.Sentence roundTrippedSentenceAdept = mapper.convert(sentenceThrift);
    assertTrue(areTheSameByLimitedThriftScope(sentenceAdept, sentenceThrift));
    assertTrue(areTheSameByLimitedThriftScope(roundTrippedSentenceAdept, sentenceThrift));
  }

  @Test
  public void TokenStreamConversionTest() {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    final adept.common.TokenStream tokenStreamAdept = generateTokenStream();
    final thrift.adept.common.TokenStream tokenStreamThrift = mapper.convert(tokenStreamAdept);
    final adept.common.TokenStream roundTrippedTokenStreamAdept = mapper.convert(tokenStreamThrift);
    assertTrue(areTheSameByLimitedThriftScope(tokenStreamAdept, tokenStreamThrift));
    assertTrue(areTheSameByLimitedThriftScope(roundTrippedTokenStreamAdept, tokenStreamThrift));
  }

  @Test
  public void EntityMentionConversionTest() {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    final adept.common.EntityMention entityMentionAdept = generateEntityMentions();
    final thrift.adept.common.EntityMention entityMentionThrift =
        mapper.convert(entityMentionAdept);
    final adept.common.EntityMention roundTrippedEntityMentionAdept =
        mapper.convert(entityMentionThrift);
    assertTrue(areTheSameByLimitedThriftScope(entityMentionAdept, entityMentionThrift));
    assertTrue(areTheSameByLimitedThriftScope(roundTrippedEntityMentionAdept, entityMentionThrift));
  }

  @Test
  public void HLTContainerTest() {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    final adept.common.HltContentContainer hltAdept = generateContainer();
    final thrift.adept.common.HltContentContainer hltThrift = mapper.convert(hltAdept);
    final adept.common.HltContentContainer roundTrippedContainer = mapper.convert(hltThrift);

    // sentences
    assertTrue(areTheSameByLimitedThriftScope(hltAdept.getSentences().get(0),
        hltThrift.getSentences().get(0)));
    assertTrue(sentencesAreTheSame(hltAdept.getSentences(), hltThrift.getSentences()));
    assertTrue(sentencesAreTheSame(roundTrippedContainer.getSentences(), hltThrift.getSentences()));

    // entity mentions
    assertTrue(
        entityMentionsAreTheSame(hltAdept.getEntityMentions(), hltThrift.getEntityMentions()));
    assertTrue(entityMentionsAreTheSame(roundTrippedContainer.getEntityMentions(),
        hltThrift.getEntityMentions()));

    // test both ways
    assertTrue(areTheSameByLimitedThriftScope(hltAdept, hltThrift));
    assertTrue(areTheSameByLimitedThriftScope(roundTrippedContainer, hltThrift));

    log.info("successfully converted to and from adept and thrift hlt content objects");
  }

  @Test
  public void CompleteHLTContainerSerializationTest() throws TException, IOException {
    SerializationTests(generateContainer());
  }

  @Test
  public void PartialHLTContainerSerializationTest() throws TException, IOException {
    SerializationTests(generateContainerWithOnlyLaxMentions());
    SerializationTests(generateContainerWithOnlySentences());
  }

  private void SerializationTests(final adept.common.HltContentContainer hltAdept)
      throws TException, IOException {
    final XMLSerializer xmlSerializer = new XMLSerializer(SerializationType.XML);
    final TSerializer thriftSerializer = new TSerializer(new TBinaryProtocol.Factory());
    final TDeserializer tdeserializer = new TDeserializer(new TBinaryProtocol.Factory());
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();

    // thrift round tripping through serialization
    final thrift.adept.common.HltContentContainer hltThrift = mapper.convert(hltAdept);
    final byte[] thriftBytes = thriftSerializer.serialize(hltThrift);
    final File thriftFile = File.createTempFile("thriftBytes", "thrift");
    thriftFile.deleteOnExit();
    Files.asByteSink(thriftFile).write(thriftBytes);
    final thrift.adept.common.HltContentContainer serializationTrippedThrift =
        new thrift.adept.common.HltContentContainer();
    tdeserializer.deserialize(serializationTrippedThrift, Files.asByteSource(thriftFile).read());

    // adept fun times
    final String adeptString = xmlSerializer.serializeAsString(hltAdept);
    final File adeptFile = File.createTempFile("adeptString", "xml");
    adeptFile.deleteOnExit();
    Files.asCharSink(adeptFile, Charsets.UTF_8).write(adeptString);
    final adept.common.HltContentContainer serializedTrippedAdept =
        (adept.common.HltContentContainer) xmlSerializer
            .deserializeString(Files.asCharSource(adeptFile, Charsets.UTF_8).read(),
                adept.common.HltContentContainer.class);

    assertTrue(areTheSameByLimitedThriftScope(hltAdept, hltThrift));
    // check that to/from serialization does the right thing
    assertTrue(areTheSameByLimitedThriftScope(serializedTrippedAdept, hltThrift));
    assertTrue(areTheSameByLimitedThriftScope(hltAdept, serializationTrippedThrift));
    assertTrue(areTheSameByLimitedThriftScope(serializedTrippedAdept, serializationTrippedThrift));
  }

}

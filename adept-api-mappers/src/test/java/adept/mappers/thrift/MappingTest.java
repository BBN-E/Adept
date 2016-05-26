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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adept.common.TokenType;
import adept.common.TokenizerType;

public class MappingTest {

  public static void main(String[] args) {

    ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    try {
      PrintWriter writer = new PrintWriter("./mapping_test_out.txt", "UTF-8");

      writer.println("Testing Adept to Thrift Conversion...");
      System.out.println("Testing Adept to Thrift Conversion...");

      adept.common.Corpus corpusAdept =
          new adept.common.Corpus("Corpus001", "AToTType", "AToT Corpus", "AtoT uri");

      adept.common.Document documentAdept =
          new adept.common.Document("Doc001", corpusAdept, "AToTDocType", "AToTDoc uri",
              "AToT English");
      documentAdept.setGenre("AToT genre");
      documentAdept.setHeadline("AToT headlne");
      documentAdept.setValue("Token1 Token2");

      adept.common.CharOffset charOffsetAdept = new adept.common.CharOffset(0, 6);

      adept.common.TokenOffset tokenOffsetAdept = new adept.common.TokenOffset(0, 0);

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

      adept.common.EntityMention entityMentionAdept =
          new adept.common.EntityMention(1, tokenOffsetAdept, tokenStreamAdept);
      entityMentionAdept.addEntityConfidencePair(1, (float) 0.1);
      entityMentionAdept.setEntityType(new adept.common.Type("AToT Entity Type"));
      entityMentionAdept.setMentionType(new adept.common.Type("AToT Mention Type"));
      entityMentionAdept.setTokenizerType(TokenizerType.OTHER);

      adept.common.Entity entityAdept =
          new adept.common.Entity(1, new adept.common.Type("AToT Entity Type"));
      entityAdept.setCanonicalMentions(entityMentionAdept);

      adept.common.Sentence sentenceAdept =
          new adept.common.Sentence(1, tokenOffsetAdept, tokenStreamAdept);
      sentenceAdept.setUncertaintyConfidence((float) 0.1);
      sentenceAdept.setNoveltyConfidence((float) 0.1);
      sentenceAdept.setPunctuation("AToT Punctuation");
      sentenceAdept.setType(adept.common.SentenceType.NONE);

      adept.common.HltContentContainer hltContentContainerAdept =
          new adept.common.HltContentContainer();

      List<adept.common.Sentence> sentencesAdept = new ArrayList<adept.common.Sentence>();
      sentencesAdept.add(sentenceAdept);
      hltContentContainerAdept.setSentences(sentencesAdept);

      // adept to thrift

      int invalidConversions = 0;

      thrift.adept.common.Document documentThrift = mapper.convert(documentAdept);
      if(documentAdept.getCorpus().getIdString().equals(documentThrift.getCorpus().getCorpusId())) {
        writer.println("corpus ids differ post conversion");
        invalidConversions++;
      }
      if(!documentAdept.getHeadline().equals(documentThrift.getHeadline())) {
        writer.println("headlines differ post conversion");
        invalidConversions++;
      }

      thrift.adept.common.CharOffset charOffsetThrift = mapper.convert(charOffsetAdept);
      writer.println(
          "\nAdept CharOffset: Begin: " + charOffsetAdept.getBegin() + " End: " + charOffsetAdept
              .getEnd());
      writer.println(
          "Thrift CharOffset: BeginIndex: " + charOffsetThrift.getBeginIndex() + " EndIndex: "
              + charOffsetThrift.getEndIndex());
      if (charOffsetAdept.getEnd() != charOffsetThrift.getEndIndex()) {
        writer.println("Objects differ");
        invalidConversions++;
      }

      thrift.adept.common.EntityMention entityMentionThrift = mapper.convert(entityMentionAdept);
      writer.println(
          "\nAdept EntityMention: Distribution: " + entityMentionAdept.getEntityIdDistribution());
      writer.println(
          "Thrift EntityMention: Distribution: " + entityMentionThrift.getEntityIdDistribution());
      if (!entityMentionAdept.getEntityType().getType().equals(entityMentionThrift
          .getEntityType().getType())) {
        writer.println("Objects differ");
        invalidConversions++;
      }

//      thrift.adept.common.Entity entityThrift = mapper.convert(entityAdept);
//      writer.println(
//          "\nAdept Entity: Canonical mention: " + entityAdept.getCanonicalMention().getValue());
//      writer.println(
//          "Thrift Entity: Canonical mention: " + entityThrift.getCanonicalMention().getValue());
//      if (entityAdept.getCanonicalMention().getValue().equals(entityThrift
//          .getCanonicalMention().getValue())) {
//        writer.println("Objects differ");
//        invalidConversions++;
//      }



      thrift.adept.common.Sentence sentenceThrift = mapper.convert(sentenceAdept);
      writer.println("\nAdept Sentence: Punctuation: " + sentenceAdept.getPunctuation());
      writer.println("Thrift Sentence: Punctuation: " + sentenceThrift.getPunctuation());
      if (!sentenceAdept.getPunctuation().equals(sentenceThrift.getPunctuation())) {
        writer.println("Objects differ");
        invalidConversions++;
      }

      thrift.adept.common.Token token1Thrift = mapper.convert(token1Adept);
      writer.println("\nAdept Token: Topic Labels: " + token1Adept.getLemma());
      writer.println("Thrift Token: Topic Labels: " + token1Thrift.getLemma());
      if (!token1Adept.getLemma().equals(token1Thrift.getLemma())) {
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
      writer.println(
          "\nAdept TokenStream: Value: " + tokenStreamAdept.getTextValue() + " Language: "
              + tokenStreamAdept.getLanguage());
      writer.println(
          "Thrift TokenStream: Value: " + tokenStreamThrift.getTextValue() + " Language: "
              + tokenStreamThrift.getLanguage());
      if (tokenOffsetAdept.getEnd() != tokenOffsetThrift.getEndIndex()) {
        writer.println("Objects differ");
        invalidConversions++;
      }


      writer.println("Invalid Adept to Thrift Conversions: " + invalidConversions);
      System.out.println("Invalid Adept to Thrift Conversions: " + invalidConversions);

///////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////

      writer.println("\nTesting Thrift to Adept Conversion...");
      System.out.println("\nTesting Thrift to Adept Conversion...");

      charOffsetThrift = new thrift.adept.common.CharOffset(7, 13);

      tokenOffsetThrift = new thrift.adept.common.TokenOffset(1, 1);

      token1Thrift = new thrift.adept.common.Token(3, charOffsetThrift, "Token1");
      token1Thrift.setConfidence((float) 0.2);
      token1Thrift.setLemma("TToA lemma 3");
      token1Thrift.setTokenType(thrift.adept.common.TokenType.GARBAGE);

      thrift.adept.common.Token token2Thrift =
          new thrift.adept.common.Token(4, charOffsetThrift, "Token2");
      token2Thrift.setConfidence((float) 0.2);
      token2Thrift.setLemma("TToA lemma 4");
      token2Thrift.setTokenType(thrift.adept.common.TokenType.GARBAGE);

      tokenStreamThrift =
          new thrift.adept.common.TokenStream(thrift.adept.common.TokenizerType.ADEPT,
              thrift.adept.common.TranscriptType.SOURCE, "TToA English",
              thrift.adept.common.ChannelName.NONE, thrift.adept.common.ContentType.TEXT,
              "Token3 Token4");
      tokenStreamThrift.setAsrName(thrift.adept.common.AsrName.BBN);
      tokenStreamThrift.setSpeechUnit(thrift.adept.common.SpeechUnit.NONE);
      tokenStreamThrift.setTranslatorName(thrift.adept.common.TranslatorName.NONE);
      tokenStreamThrift.setDocument(documentThrift);
      List<thrift.adept.common.Token> tokenList = new ArrayList<thrift.adept.common.Token>();
      tokenList.add(token1Thrift);
      tokenList.add(token2Thrift);
      tokenStreamThrift.setTokenList(tokenList);

      entityMentionThrift =
          new thrift.adept.common.EntityMention(2, tokenOffsetThrift, tokenStreamThrift);
      Map<Long, Double> hashMap = new HashMap<Long, Double>();
      hashMap.put(2L, 0.2);
      entityMentionThrift.setEntityIdDistribution(hashMap);
      entityMentionThrift.setEntityType(new thrift.adept.common.Type("TToA Entity Type"));
      entityMentionThrift.setMentionType(new thrift.adept.common.Type("TToA Mention Type"));
      entityMentionThrift.setValue("Token4");
      entityMentionThrift.setCharOffset(charOffsetThrift);

//      q.setEntityType(new thrift.adept.common.Type("TToA Entity Type"));

      sentenceThrift = new thrift.adept.common.Sentence(2, tokenOffsetThrift, tokenStreamThrift);
      sentenceThrift.setUncertaintyConfidence((float) 0.2);
      sentenceThrift.setNoveltyConfidence((float) 0.2);
      sentenceThrift.setPunctuation("TToA Punctuation");
      sentenceThrift.setType(thrift.adept.common.SentenceType.NONE);

      thrift.adept.common.HltContentContainer hltContentContainerThrift =
          new thrift.adept.common.HltContentContainer();
      List<thrift.adept.common.Sentence> sentencesThrift =
          new ArrayList<thrift.adept.common.Sentence>();
      sentencesThrift.add(sentenceThrift);
      hltContentContainerThrift.setSentences(sentencesThrift);

      invalidConversions = 0;

      charOffsetAdept = mapper.convert(charOffsetThrift);
      writer.println(
          "\nAdept CharOffset: Begin: " + charOffsetAdept.getBegin() + " End: " + charOffsetAdept
              .getEnd());
      writer.println(
          "Thrift CharOffset: BeginIndex: " + charOffsetThrift.getBeginIndex() + " EndIndex: "
              + charOffsetThrift.getEndIndex());
      if (charOffsetAdept.getEnd() != charOffsetThrift.getEndIndex()) {
        writer.println("Objects differ");
        invalidConversions++;
      }

      entityMentionAdept = mapper.convert(entityMentionThrift);
      writer.println(
          "\nAdept EntityMention: Distribution: " + entityMentionAdept.getEntityIdDistribution());
      writer.println(
          "Thrift EntityMention: Distribution: " + entityMentionThrift.getEntityIdDistribution());
      if (!entityMentionAdept.getEntityType().getType().equals(entityMentionThrift
          .getEntityType().getType())) {
        writer.println("Objects differ");
        invalidConversions++;
      }

//      entityAdept = mapper.convert(entityThrift);
//      writer.println(
//          "\nAdept Entity: Canonical mention: " + entityAdept.getCanonicalMention().getValue());
//      writer.println(
//          "Thrift Entity: Canonical mention: " + entityThrift.getCanonicalMention().getValue());
//      if (entityAdept.getCanonicalMention().getValue().equals(entityThrift
//          .getCanonicalMention().getValue())) {
//        writer.println("Objects differ");
//        invalidConversions++;
//      }

      sentenceAdept = mapper.convert(sentenceThrift);
      writer.println("\nAdept Sentence: Punctuation: " + sentenceAdept.getPunctuation());
      writer.println("Thrift Sentence: Punctuation: " + sentenceThrift.getPunctuation());
      if (!sentenceAdept.getPunctuation().equals(sentenceThrift.getPunctuation())) {
        writer.println("Objects differ");
        invalidConversions++;
      }

      token1Adept = mapper.convert(token1Thrift);
      writer.println("\nAdept Token: Topic Labels: " + token1Adept.getLemma());
      writer.println("Thrift Token: Topic Labels: " + token1Thrift.getLemma());
      if (!token1Adept.getLemma().equals(token1Thrift.getLemma())) {
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
      writer.println(
          "\nAdept TokenStream: Value: " + tokenStreamAdept.getTextValue() + " Language: "
              + tokenStreamAdept.getLanguage());
      writer.println(
          "Thrift TokenStream: Value: " + tokenStreamThrift.getTextValue() + " Language: "
              + tokenStreamThrift.getLanguage());
      if (tokenOffsetAdept.getEnd() != tokenOffsetThrift.getEndIndex()) {
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







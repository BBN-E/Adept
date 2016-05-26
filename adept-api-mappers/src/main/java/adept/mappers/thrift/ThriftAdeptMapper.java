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

/*******************************************************************************
 * Raytheon BBN Technologies Corp., January 2016
 *
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF 
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * Copyright 2016 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
package adept.mappers.thrift;

import org.dozer.DozerBeanMapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import adept.common.TokenizerType;
import adept.mappers.thrift.builders.DocumentBuilder;
import adept.mappers.thrift.builders.EntityMentionBuilder;
import adept.mappers.thrift.builders.SentenceBuilder;
import adept.mappers.thrift.builders.TokenBuilder;
import adept.mappers.thrift.builders.TokenStreamBuilder;
import thrift.adept.common.AsrName;

import static com.google.common.base.Preconditions.checkNotNull;

public class ThriftAdeptMapper {

  private DozerBeanMapper mapper = null;
  private static ThriftAdeptMapper instance = null;

  public ThriftAdeptMapper(String path) {
    String file = "file:" + path;
    mapper = new DozerBeanMapper(Arrays.asList(new String[]{file}));
  }

  private ThriftAdeptMapper() {
    mapper = new DozerBeanMapper(
        Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
  }

  public static ThriftAdeptMapper getInstance() {
    if (instance == null) {
      instance = new ThriftAdeptMapper();
      return instance;
    } else {
      return instance;
    }
  }

  public thrift.adept.common.AudioOffset convert(adept.common.AudioOffset audioOffset) {
    thrift.adept.common.AudioOffset retAudioOffset =
        mapper.map(audioOffset, thrift.adept.common.AudioOffset.class);
    return retAudioOffset;
  }

  public thrift.adept.common.CharOffset convert(adept.common.CharOffset charOffset) {
    thrift.adept.common.CharOffset retCharOffset =
        mapper.map(charOffset, thrift.adept.common.CharOffset.class);
    return retCharOffset;
  }

  public thrift.adept.common.Chunk convert(adept.common.Chunk chunk) {
    thrift.adept.common.Chunk retChunk = mapper.map(chunk, thrift.adept.common.Chunk.class);
    return retChunk;
  }


  public thrift.adept.common.Corpus convert(adept.common.Corpus corpus) {
    if (corpus == null) {
      return null;
    }
    thrift.adept.common.Corpus retCorpus = mapper.map(corpus, thrift.adept.common.Corpus.class);
    return retCorpus;
  }

  public thrift.adept.common.Document convert(adept.common.Document document) {
    thrift.adept.common.Document retDocument =
        new thrift.adept.common.Document(); //mapper.map(document, thrift.adept.common.Document.class);
    retDocument.setCorpus(convert(document.getCorpus()));
    retDocument.setDocId(document.getDocId());
    retDocument.setDocType(document.getDocType());
    retDocument.setValue(document.getValue());
    retDocument.setLanguage(document.getLanguage());
    retDocument.setHeadline(document.getHeadline());
    retDocument.setGenre(document.getGenre());
    retDocument.setUri(document.getUri());
    return retDocument;
  }

  public thrift.adept.common.DocumentList convert(adept.common.DocumentList documentList) {
    thrift.adept.common.DocumentList retDocumentList =
        mapper.map(documentList, thrift.adept.common.DocumentList.class);
    return retDocumentList;
  }

  public thrift.adept.common.EntityMention convert(adept.common.EntityMention entityMention) {
    if (entityMention == null) {
      return null;
    }
    thrift.adept.common.EntityMention retEntityMention =
        mapper.map(entityMention, thrift.adept.common.EntityMention.class);
    if (entityMention.getEntityType() != null) {
      retEntityMention
          .setEntityType(new thrift.adept.common.Type(entityMention.getEntityType().getType()));
    }
    if (entityMention.getMentionType() != null) {
      retEntityMention
          .setMentionType(new thrift.adept.common.Type(entityMention.getMentionType().getType()));
    }
    if (entityMention.getTokenStream() != null) {
      retEntityMention.setTokenStream(convert(entityMention.getTokenStream()));
    }
    return retEntityMention;
  }

  public thrift.adept.common.HltContent convert(adept.common.HltContent hltContent) {
    thrift.adept.common.HltContent retHltContent =
        mapper.map(hltContent, thrift.adept.common.HltContent.class);
    return retHltContent;
  }

  public thrift.adept.common.HltContentContainer convert(
      adept.common.HltContentContainer hltContentContainer) {
    thrift.adept.common.HltContentContainer retHltContentContainer =
        mapper.map(hltContentContainer, thrift.adept.common.HltContentContainer.class);
    return retHltContentContainer;
  }

  public thrift.adept.common.HltContentContainerList convert(
      adept.common.HltContentContainerList hltContentContainerList) {
    thrift.adept.common.HltContentContainerList retHltContentContainerList =
        mapper.map(hltContentContainerList, thrift.adept.common.HltContentContainerList.class);
    return retHltContentContainerList;
  }

  public thrift.adept.common.ID convert(adept.common.ID id) {
    thrift.adept.common.ID retID = mapper.map(id, thrift.adept.common.ID.class);
    return retID;
  }

  public thrift.adept.common.Item convert(adept.common.Item item) {
    thrift.adept.common.Item retItem = mapper.map(item, thrift.adept.common.Item.class);
    return retItem;
  }

  public thrift.adept.common.PartOfSpeech convert(adept.common.PartOfSpeech partOfSpeech) {
    thrift.adept.common.PartOfSpeech retPartOfSpeech =
        mapper.map(partOfSpeech, thrift.adept.common.PartOfSpeech.class);
    retPartOfSpeech.setTokenStream(convert(partOfSpeech.getTokenStream()));
    return retPartOfSpeech;
  }

  public thrift.adept.common.Sentence convert(adept.common.Sentence sentence) {
    thrift.adept.common.Sentence retSentence =
        mapper.map(sentence, thrift.adept.common.Sentence.class);
    retSentence.setTokenStream(convert(sentence.getTokenStream()));
    return retSentence;
  }


  public thrift.adept.common.Token convert(adept.common.Token token) {
    thrift.adept.common.Token retToken = mapper.map(token, thrift.adept.common.Token.class);
    // solves a mysterious error when deserializing thrift objects
    retToken.setSequenceId(token.getSequenceId());
    return retToken;
  }

  public thrift.adept.common.TokenOffset convert(adept.common.TokenOffset tokenOffset) {
    thrift.adept.common.TokenOffset retTokenOffset =
        mapper.map(tokenOffset, thrift.adept.common.TokenOffset.class);
    return retTokenOffset;
  }

  public thrift.adept.common.TokenStream convert(adept.common.TokenStream tokenStream) {
    thrift.adept.common.TokenStream retTokenStream =
        mapper.map(tokenStream, thrift.adept.common.TokenStream.class);

    List<thrift.adept.common.Token> tokenList = new ArrayList<thrift.adept.common.Token>();

    for (int i = 0; i < tokenStream.size(); i++) {
      adept.common.Token token = tokenStream.get(i);
      thrift.adept.common.Token thriftToken = convert(token);
      tokenList.add(thriftToken);
    }

    retTokenStream.setTokenList(tokenList);

    return retTokenStream;
  }


  public thrift.adept.common.Type convert(adept.common.Type type) {
    thrift.adept.common.Type retType = mapper.map(type, thrift.adept.common.Type.class);
    return retType;
  }


  public thrift.adept.common.Value convert(adept.common.Value value) {
    thrift.adept.common.Value retValue = mapper.map(value, thrift.adept.common.Value.class);
    return retValue;
  }


  public thrift.adept.common.MorphFeature convert(adept.common.MorphFeature feature) {
    thrift.adept.common.MorphFeature ret = mapper.map(feature, thrift.adept.common.MorphFeature.class);
    return ret;
  }

  public thrift.adept.common.Morph convert(adept.common.Morph morph) {
    thrift.adept.common.Morph ret = mapper.map(morph, thrift.adept.common.Morph.class);
    return ret;
  }

  public thrift.adept.common.MorphToken convert(adept.common.MorphToken morphToken) {
    thrift.adept.common.MorphToken ret =
        mapper.map(morphToken, thrift.adept.common.MorphToken.class);
    return ret;
  }

  public thrift.adept.common.MorphTokenSequence convert(
      adept.common.MorphTokenSequence morphTokenSequence) {
    thrift.adept.common.MorphTokenSequence ret = mapper.map(morphTokenSequence, thrift.adept.common.MorphTokenSequence.class);
    return ret;
  }

  public thrift.adept.common.MorphSentence convert(adept.common.MorphSentence morphSentence) {
    thrift.adept.common.MorphSentence ret = mapper.map(morphSentence, thrift.adept.common.MorphSentence.class);
    return ret;
  }

  public adept.common.AudioOffset convert(thrift.adept.common.AudioOffset audioOffset) {
    adept.common.AudioOffset retAudioOffset =
        mapper.map(audioOffset, adept.common.AudioOffset.class);
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


  public adept.common.Corpus convert(thrift.adept.common.Corpus corpus) {
    adept.common.Corpus retCorpus =
        mapper.map(corpus, adept.mappers.thrift.builders.CorpusBuilder.class).build();
    return retCorpus;
  }


  public adept.common.Document convert(thrift.adept.common.Document document) {
    checkNotNull(document, "attempted to map a null document");
    DocumentBuilder builder = new DocumentBuilder();
    // minimal set
    builder.setDocId(document.getDocId());
    builder.setDocType(document.getDocType());
    builder.setLanguage(document.getLanguage());
    builder.setValue(document.getValue());
    builder.setHeadline(document.getHeadline());
    builder.setGenre(document.getGenre());
    builder.setUri(document.getUri());
    if (document.getCorpus() != null) {
      builder.setCorpus(convert(document.getCorpus()));
    }
    return builder.build();
  }

  public adept.common.DocumentList convert(thrift.adept.common.DocumentList documentList) {
    adept.common.DocumentList retDocumentList =
        mapper.map(documentList, adept.common.DocumentList.class);
    return retDocumentList;
  }


  public adept.common.EntityMention convert(thrift.adept.common.EntityMention entityMention) {
    if (entityMention == null) {
      return null;
    }
    checkNotNull(entityMention, "passed a null entity mention to convert");
    EntityMentionBuilder builder = mapper.map(entityMention, EntityMentionBuilder.class);
    // these are not properly translated by Dozer or any of the converters
    if (entityMention.getEntityType() != null) {
      builder.setEntityType(convert(entityMention.getEntityType()));
    }
    if (entityMention.getMentionType() != null) {
      builder.setMentionType(convert(entityMention.getMentionType()));
    }
    return builder.build();
  }

  public adept.common.HltContent convert(thrift.adept.common.HltContent hltContent) {
    adept.common.HltContent retHltContent = mapper.map(hltContent, adept.common.HltContent.class);
    return retHltContent;
  }

  public adept.common.HltContentContainer convert(
      thrift.adept.common.HltContentContainer hltContentContainer) {
    adept.common.HltContentContainer retHltContentContainer =
        mapper.map(hltContentContainer, adept.common.HltContentContainer.class);
    return retHltContentContainer;
  }

  public adept.common.HltContentContainerList convert(
      thrift.adept.common.HltContentContainerList hltContentContainerList) {
    adept.common.HltContentContainerList retHltContentContainerList =
        mapper.map(hltContentContainerList, adept.common.HltContentContainerList.class);
    return retHltContentContainerList;
  }

  public adept.common.ID convert(thrift.adept.common.ID id) {
    adept.common.ID retID = mapper.map(id, adept.common.ID.class);
    return retID;
  }


  public adept.common.Item convert(thrift.adept.common.Item item) {
    adept.common.Item retItem = mapper.map(item, adept.common.Item.class);
    return retItem;
  }


  public adept.common.PartOfSpeech convert(thrift.adept.common.PartOfSpeech partOfSpeech) {
    adept.common.PartOfSpeech retPartOfSpeech =
        mapper.map(partOfSpeech, adept.common.PartOfSpeech.class);
    retPartOfSpeech.setTokenStream(convert(partOfSpeech.getTokenStream()));
    return retPartOfSpeech;
  }


  public adept.common.Sentence convert(thrift.adept.common.Sentence sentence) {
    SentenceBuilder builder =
        mapper.map(sentence, adept.mappers.thrift.builders.SentenceBuilder.class);
    builder.setTokenStream(convert(sentence.tokenStream));
    return builder.build();
  }


  public adept.common.Token convert(thrift.adept.common.Token token) {
    adept.common.Token retToken = mapper.map(token, TokenBuilder.class).build();
    return retToken;
  }

  public adept.common.TokenOffset convert(thrift.adept.common.TokenOffset tokenOffset) {
    adept.common.TokenOffset retTokenOffset =
        mapper.map(tokenOffset, adept.common.TokenOffset.class);
    return retTokenOffset;
  }

  public adept.common.TokenStream convert(thrift.adept.common.TokenStream tokenStream) {
    TokenStreamBuilder tokenStreamBuilder = mapper.map(tokenStream, TokenStreamBuilder.class);
    tokenStreamBuilder.setLanguage(tokenStream.getLanguage());
    tokenStreamBuilder.setTokenizerType(convert(tokenStream.getTokenizerType()));
    tokenStreamBuilder.setDocument(convert(tokenStream.getDocument()));
    adept.common.TokenStream retTokenStream = tokenStreamBuilder.build();

    if (tokenStream.getTokenList() != null) {
      List<thrift.adept.common.Token> tokenList = tokenStream.getTokenList();

      for (int i = 0; i < tokenList.size(); i++) {
        thrift.adept.common.Token token = tokenList.get(i);
        adept.common.Token adeptToken = convert(token);
        retTokenStream.add(adeptToken);
      }
    }

    return retTokenStream;
  }

  private TokenizerType convert(final AsrName asrName) {
    return null;
  }

  private adept.common.TokenizerType convert(
      final thrift.adept.common.TokenizerType tokenizerType) {
    return mapper.map(tokenizerType, adept.common.TokenizerType.class);
  }

  public adept.common.Type convert(thrift.adept.common.Type type) {
    adept.common.Type retType = new adept.common.Type(type.getType());
    return retType;
  }

  public adept.common.Value convert(thrift.adept.common.Value value) {
    adept.common.Value retValue = mapper.map(value, adept.common.Value.class);
    return retValue;
  }

  public adept.common.MorphFeature convert(thrift.adept.common.MorphFeature feature) {
    adept.common.MorphFeature ret = mapper.map(feature, adept.common.MorphFeature.class);
    return ret;
  }

  public adept.common.Morph convert(thrift.adept.common.Morph morph) {
    adept.common.Morph.Builder ret = mapper.map(morph, adept.common.Morph.Builder.class);
    return ret.build();
  }

  public adept.common.MorphToken convert(thrift.adept.common.MorphToken morphToken) {
    adept.common.MorphToken.Builder ret =
        mapper.map(morphToken, adept.common.MorphToken.Builder.class);
    return ret.build();
  }

  public adept.common.MorphTokenSequence convert(
      thrift.adept.common.MorphTokenSequence morphTokenSequence) {
    adept.common.MorphTokenSequence.Builder ret = mapper.map(morphTokenSequence, adept.common.MorphTokenSequence.Builder.class);
    return ret.build();
  }

  public adept.common.MorphSentence convert(thrift.adept.common.MorphSentence morphSentence) {
    adept.common.MorphSentence.Builder ret = mapper.map(morphSentence, adept.common.MorphSentence.Builder.class);
    return ret.build();
  }
}

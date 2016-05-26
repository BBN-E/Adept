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

package adept.mappers.thrift.builders;

import java.util.Map;

import adept.common.EntityMention;
import adept.common.IType;
import adept.common.TokenOffset;
import adept.common.TokenStream;
import adept.common.TokenizerType;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jdeyoung on 10/28/15.
 */
public final class EntityMentionBuilder {

  private IType mentionType;
  private IType entityType;
  private long sequenceId;
  private Map<Long, Float> entityIdDistribution;
  private String docId;
  private TokenizerType tokenizerType;
  private Map<String, String> attributes;
  private TokenOffset tokenOffset;
  private TokenStream tokenStream;

  public EntityMentionBuilder() {

  }

  public EntityMentionBuilder setMentionType(final IType mentionType) {
    this.mentionType = mentionType;
    return this;
  }

  public EntityMentionBuilder setEntityType(final IType entityType) {
    this.entityType = entityType;
    return this;
  }

  public EntityMentionBuilder setSequenceId(final long sequenceId) {
    this.sequenceId = sequenceId;
    return this;
  }

  public EntityMentionBuilder setEntityIdDistribution(final Map<Long, Float> entityIdDistribution) {
    this.entityIdDistribution = entityIdDistribution;
    return this;
  }

  public EntityMentionBuilder setDocId(final String docId) {
    this.docId = docId;
    return this;
  }

  public EntityMentionBuilder setTokenizerType(final TokenizerType tokenizerType) {
    this.tokenizerType = tokenizerType;
    return this;
  }

  public EntityMentionBuilder setAttributes(final Map<String, String> attributes) {
    this.attributes = attributes;
    return this;
  }

  public EntityMention build() {
    checkNotNull(tokenStream, "tokenstream must be defined for Chunk Initialization");
    checkNotNull(tokenStream.getDocument(), "tokenstream document must be defined for Chunk initialization");
    checkNotNull(tokenStream.getDocument().getValue(), "tokenstream document text must be defined for Chunk initialization");
    checkNotNull(tokenStream.getDocument().getValue(), "tokenstream document text must be defined for Chunk initialization");
    final EntityMention em = new EntityMention(sequenceId, tokenOffset, tokenStream);
    if (docId != null) {
      em.setDocId(docId);
    }
    if (entityType != null) {
      em.setEntityType(entityType);
    }
    if (mentionType != null) {
      em.setMentionType(mentionType);
    }
    if (entityIdDistribution != null) {
      em.setEntityIdDistribution(entityIdDistribution);
    }
    if (tokenizerType != null) {
      em.setTokenizerType(tokenizerType);
    }
    if (attributes != null) {
      em.setAttributes(attributes);
    }
    return em;
  }

  public EntityMentionBuilder setTokenOffset(TokenOffset tokenOffset) {
    this.tokenOffset = tokenOffset;
    return this;
  }

  public EntityMentionBuilder setTokenStream(TokenStream tokenStream) {
    this.tokenStream = tokenStream;
    return this;
  }
}

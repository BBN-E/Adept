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

import adept.common.Entity;
import adept.common.EntityMention;
import adept.common.IType;
import adept.mappers.thrift.ThriftAdeptMapper;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jdeyoung on 10/29/15.
 */
public final class EntityBuilder {

  private long entityId;
  private EntityMention canonicalMention;
  private IType entityType;
//  private Map<IType, Double> typeConfidences = new HashMap<IType, Double>();
  private double confidence = 1.0;
  private double canonicalMentionConfidence = 1.0;
//  private Map<IType, IType> ontologizedAttributes = new HashMap<IType, IType>();
//  private Map<IType, String> freeTextAttributes = new HashMap<IType, String>();
//  private Map< String, Set< String >> typeInformation;

  public EntityBuilder() {

  }

  public Entity build() {
    checkNotNull(entityType, "entity type must be defined to appease adept");
    final Entity ret = new Entity(entityId, entityType);
    ret.setCanonicalMentionConfidence(canonicalMentionConfidence);
    ret.setCanonicalMentions(canonicalMention);
    return ret;
  }

  public EntityBuilder setEntityId(final long entityId) {
    this.entityId = entityId;
    return this;
  }

  public EntityBuilder setCanonicalMention(final EntityMention canonicalMention) {
    this.canonicalMention = canonicalMention;
    return this;
  }

  public EntityBuilder setCanonicalMention(final thrift.adept.common.EntityMention entityMention) {
    final ThriftAdeptMapper mapper = ThriftAdeptMapper.getInstance();
    this.canonicalMention = mapper.convert(entityMention);
    return this;
  }

  public EntityBuilder setCanonicalMention(final EntityMentionBuilder builder) {
    this.canonicalMention = builder.build();
    return this;
  }

  public EntityBuilder setEntityType(final IType entityType) {
    this.entityType = entityType;
    return this;
  }

//  public EntityBuilder setTypeConfidences(final Map<IType, Double> typeConfidences) {
//    this.typeConfidences = typeConfidences;
//    return this;
//  }

  public EntityBuilder setConfidence(final double confidence) {
    this.confidence = confidence;
    return this;
  }

  public EntityBuilder setCanonicalMentionConfidence(final double canonicalMentionConfidence) {
    this.canonicalMentionConfidence = canonicalMentionConfidence;
    return this;
  }

  //  public EntityBuilder setOntologizedAttributes(final Map<IType, IType> ontologizedAttributes) {
//    this.ontologizedAttributes = ontologizedAttributes;
//    return this;
//  }
//
//  public EntityBuilder setFreeTextAttributes(final Map<IType, String> freeTextAttributes) {
//    this.freeTextAttributes = freeTextAttributes;
//    return this;
//  }
//
//  public EntityBuilder setTypeInformation(final Map<String, Set<String>> typeInformation) {
//    this.typeInformation = typeInformation;
//    return this;
//  }

}

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

package adept.mappers.thrift.converters;

import org.dozer.DozerConverter;

import adept.mappers.thrift.ThriftAdeptMapper;
import thrift.adept.common.EntityMention;

/**
 * Created by jdeyoung on 11/3/15.
 */
public class EntityMentionConverter
    extends DozerConverter<adept.common.EntityMention, thrift.adept.common.EntityMention> {

  private final static ThriftAdeptMapper instance = ThriftAdeptMapper.getInstance();

  public EntityMentionConverter() {
    super(adept.common.EntityMention.class, thrift.adept.common.EntityMention.class);
  }

  @Override
  public EntityMention convertTo(final adept.common.EntityMention entityMention,
      final EntityMention entityMention2) {
    if (entityMention == null) {
      return null;
    }
    return instance.convert(entityMention);
  }

  @Override
  public adept.common.EntityMention convertFrom(final EntityMention entityMention,
      final adept.common.EntityMention entityMention2) {
    if (entityMention == null) {
      return null;
    }
    return instance.convert(entityMention);
  }
}

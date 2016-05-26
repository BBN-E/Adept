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

import com.google.common.collect.Lists;

import java.util.List;

// type erasure and limitations of Dozer means we can't have a Converter<List<X>, List<Y>>
public final class EntityMentionListConverter implements org.dozer.CustomConverter {

  private static final EntityMentionConverter emConverter = new EntityMentionConverter();

  @Override
  public Object convert(final Object dest, final Object source, final Class<?> destClass,
      final Class<?> sourceClass) {
    if (source instanceof List && ((List) source).size() > 0) {
      Object o = ((List) source).get(0);
      if (o instanceof thrift.adept.common.EntityMention) {
        final List<adept.common.EntityMention> ret = Lists.newArrayList();
        for (final Object em : (List) source) {
          ret.add(emConverter.convertFrom((thrift.adept.common.EntityMention) em, null));
        }
        return ret;
      } else if (o instanceof adept.common.EntityMention) {
        final List<thrift.adept.common.EntityMention> ret = Lists.newArrayList();
        for (final Object em : (List) source) {
          ret.add(emConverter.convertTo((adept.common.EntityMention) em, null));
        }
        return ret;
      } else {
        throw new org.dozer.MappingException("Converter EntityMentionListConverter "
            + "used incorrectly. Arguments passed in were:"
            + destClass + " and " + source + " of " + o.getClass());
      }
    } else {
      throw new org.dozer.MappingException("Converter EntityMentionListConverter "
          + "used incorrectly. Arguments passed in were:"
          + destClass + " and " + source);
    }

  }
}

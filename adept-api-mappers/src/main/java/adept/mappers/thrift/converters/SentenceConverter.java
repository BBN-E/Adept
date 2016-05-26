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

import org.dozer.DozerBeanMapper;

import java.util.Arrays;
import java.util.List;

import adept.mappers.thrift.ThriftAdeptMapper;

import static com.google.common.base.Preconditions.checkNotNull;

public class SentenceConverter implements org.dozer.CustomConverter {

  // the ThriftAdeptMapper supports nice, sane, conversion methods, but the Dozer bean mapper only supports simple changes.
  private final static DozerBeanMapper mapper = new DozerBeanMapper(
      Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
  private final static ThriftAdeptMapper generalMapper = ThriftAdeptMapper.getInstance();

  private List<adept.common.Sentence> fromThrift(final List<Object> source) {
    final List<adept.common.Sentence> ret = Lists.newArrayList();
    for (final Object o : source) {
      ret.add(fromThrift(o));
    }
    return ret;
  }

  private adept.common.Sentence fromThrift(final Object source) {
    thrift.adept.common.Sentence sentence = (thrift.adept.common.Sentence) source;
    adept.common.Sentence ret =
        mapper.map(sentence, adept.mappers.thrift.builders.SentenceBuilder.class).build();
    checkNotNull(ret);
    return ret;
  }


  private List<thrift.adept.common.Sentence> fromAdept(final List<Object> source) {
    final List<thrift.adept.common.Sentence> ret = Lists.newArrayList();
    for (final Object o : source) {
      ret.add(fromAdept(o));
    }
    return ret;
  }

  private thrift.adept.common.Sentence fromAdept(final Object source) {
    adept.common.Sentence sentence = (adept.common.Sentence) source;
    thrift.adept.common.Sentence ret = mapper.map(sentence, thrift.adept.common.Sentence.class);
    thrift.adept.common.TokenStream stream = generalMapper.convert(sentence.getTokenStream());
    checkNotNull(stream);
    checkNotNull(stream.getTokenList());
    ret.setTokenStream(stream);
    checkNotNull(ret);
    return ret;
  }

  @Override
  public Object convert(final Object dest, final Object source, final Class<?> destClass,
      final Class<?> sourceClass) {

    if (source instanceof adept.common.Sentence) {
      return fromAdept(source);
    } else if (source instanceof thrift.adept.common.Sentence) {
      return fromThrift(source);
    } else if (source instanceof java.util.ArrayList || source instanceof java.util.List) {
      Object o = ((List) source).get(0);
      if (o instanceof adept.common.Sentence) {
        return fromAdept((List) source);
      } else if (o instanceof thrift.adept.common.Sentence) {
        return fromThrift((List) source);
      } else {
        throw new org.dozer.MappingException("Converter SentenceConverter "
            + "used incorrectly. Arguments passed in were:"
            + destClass + " and " + source + " of " + o.getClass());
      }
    } else {
      throw new org.dozer.MappingException("Converter SentenceConverter "
          + "used incorrectly. Arguments passed in were:"
          + destClass + " and " + source);
    }
  }
}

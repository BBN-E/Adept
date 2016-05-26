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

import org.dozer.DozerBeanMapper;

import java.util.Arrays;

import adept.mappers.thrift.ThriftAdeptMapper;

/**
 * Created by jdeyoung on 11/11/15.
 */
public final class MorphFeatureConverter implements org.dozer.CustomConverter {

  // the ThriftAdeptMapper supports nice, sane, conversion methods, but the Dozer bean mapper only supports simple changes.
  private final static DozerBeanMapper mapper = new DozerBeanMapper(
      Arrays.asList(new String[]{"adept/mappers/thrift/ThriftAdeptMappings.xml"}));
  private final static ThriftAdeptMapper generalMapper = ThriftAdeptMapper.getInstance();

  @Override
  public Object convert(final Object dest, final Object source, final Class<?> destClass,
      final Class<?> sourceClass) {
    if (source instanceof adept.common.MorphFeature) {
        adept.common.MorphFeature adept = (adept.common.MorphFeature) source;
      thrift.adept.common.MorphFeature thrift = new thrift.adept.common.MorphFeature();
      thrift.setProperty(adept.property());
      thrift.setValue(adept.value());
      return thrift;
    } else if (source instanceof thrift.adept.common.MorphFeature) {
      thrift.adept.common.MorphFeature thrift = (thrift.adept.common.MorphFeature) source;
      return adept.common.MorphFeature.create(thrift.getProperty(), thrift.getValue());
    } else {
      throw new org.dozer.MappingException("Converter MorphFeatureConverter "
          + "used incorrectly. Arguments passed in were:"
          + destClass + " and " + source);
    }
  }
}

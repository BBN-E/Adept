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

import org.dozer.CustomConverter;
import org.dozer.MappingException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Inspired by http://stackoverflow.com/questions/8267499/dozer-string-to-enum-mapping
 */
public class EnumEnumConverter implements CustomConverter {

  @Override
  public Object convert(final Object dest, final Object source, final Class<?> destClass,
      final Class<?> sourceClass) {
    if (source == null) {
      return null;
    }
    if (destClass.isEnum() && sourceClass.isEnum()) {
      try {
        final Method m = destClass.getMethod("valueOf", String.class);
        return m.invoke(destClass.getClass(), source.toString());
      } catch (NoSuchMethodException e) {
        e.printStackTrace();
        throw new MappingException("no method valueOf for " + destClass);
      } catch (InvocationTargetException e) {
        e.printStackTrace();
        throw new MappingException(
            "invocation of valueOf failed for " + source + " for target class " + destClass);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        throw new MappingException(
            "invocation of valueOf failed for " + source + " for target class " + destClass);
      }
    } else {
      throw new MappingException(
          "Cannot map from " + sourceClass + " to " + destClass + " source: " + source
              + " is not an enum ");
    }
  }
}

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

import adept.common.Document;
import adept.mappers.thrift.ThriftAdeptMapper;

public final class DocumentConverter
    extends DozerConverter<thrift.adept.common.Document, adept.common.Document> {

  private final static ThriftAdeptMapper instance = ThriftAdeptMapper.getInstance();

  public DocumentConverter() {
    super(thrift.adept.common.Document.class, adept.common.Document.class);
  }

  @Override
  public Document convertTo(final thrift.adept.common.Document document,
      final adept.common.Document document2) {
    if(document == null) {
      return null;
    }
    return instance.convert(document);
  }

  @Override
  public thrift.adept.common.Document convertFrom(final adept.common.Document document,
      final thrift.adept.common.Document document2) {
    if(document == null) {
      return null;
    }
    return instance.convert(document);
  }
}

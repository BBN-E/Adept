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

import adept.common.Corpus;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jdeyoung on 11/2/15.
 */
public final class CorpusBuilder {

  private String corpusId;
  private String type;
  private String name;
  private String uri;


  public CorpusBuilder() {

  }

  public CorpusBuilder setCorpusId(final String corpusId) {
    this.corpusId = corpusId;
    return this;
  }

  public CorpusBuilder setType(final String type) {
    this.type = type;
    return this;
  }

  public CorpusBuilder setName(final String name) {
    this.name = name;
    return this;
  }

  public CorpusBuilder setUri(final String uri) {
    this.uri = uri;
    return this;
  }

  public Corpus build() {
    checkNotNull(type, "corpus type may not be null");
    checkNotNull(name, "corpus name may not be null");
    return new Corpus(corpusId, type, name, uri);
  }
}

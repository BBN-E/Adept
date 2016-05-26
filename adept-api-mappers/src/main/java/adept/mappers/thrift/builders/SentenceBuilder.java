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

import adept.common.MorphSentence;
import adept.common.Sentence;
import adept.common.SentenceType;
import adept.common.TokenOffset;
import adept.common.TokenStream;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jdeyoung on 10/30/15.
 */
public final class SentenceBuilder {

  private long sequenceId;
  private SentenceType type;
  private String punctuation;
  private float uncertaintyConfidence;
  private float noveltyConfidence;
  private TokenStream tokenStream;
  private TokenOffset tokenOffset;
  private MorphSentence morphSentence;

  public SentenceBuilder() {

  }

  public SentenceBuilder setSequenceId(final long sequenceId) {
    this.sequenceId = sequenceId;
    return this;
  }

  public SentenceBuilder setType(final SentenceType type) {
    this.type = type;
    return this;
  }

  public SentenceBuilder setPunctuation(final String punctuation) {
    this.punctuation = punctuation;
    return this;
  }

  public SentenceBuilder setUncertaintyConfidence(final float uncertaintyConfidence) {
    this.uncertaintyConfidence = uncertaintyConfidence;
    return this;
  }

  public SentenceBuilder setNoveltyConfidence(final float noveltyConfidence) {
    this.noveltyConfidence = noveltyConfidence;
    return this;
  }

  public SentenceBuilder setTokenStream(final TokenStream tokenStream) {
    this.tokenStream = tokenStream;
    return this;
  }

  public SentenceBuilder setTokenOffset(final TokenOffset tokenOffset) {
    this.tokenOffset = tokenOffset;
    return this;
  }

  public void setMorphSentence(final MorphSentence morphSentence) {
    this.morphSentence = morphSentence;
  }

  public Sentence build() {
    checkNotNull(tokenOffset, "token offset must be defined for chunks");
    checkNotNull(tokenStream, "token stream must be defined for chunks");
    final Sentence ret = new Sentence(sequenceId, tokenOffset, tokenStream);
    ret.setNoveltyConfidence(noveltyConfidence);
    ret.setUncertaintyConfidence(uncertaintyConfidence);
    ret.setPunctuation(punctuation);
    if (type != null) {
      ret.setType(type);
    }
    if(morphSentence != null) {
      ret.setMorphSentence(morphSentence);
    }
    return ret;
  }


}

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

import adept.common.AudioOffset;
import adept.common.CharOffset;
import adept.common.Token;
import adept.common.TokenType;

/**
 * Created by jdeyoung on 10/29/15.
 */
public final class TokenBuilder {

  private CharOffset charOffset;

  /** The sequence id. */
  private long sequenceId;

  /** The token type. */
  private TokenType tokenType;

  /** The lemma. */
  private String lemma;

  /** The audio offset. */
  private AudioOffset audioOffset;

  /** The confidence. */
  private float confidence;

  private String value;



  public TokenBuilder() {

  }

  public void setCharOffset(final CharOffset charOffset) {
    this.charOffset = charOffset;
  }

  public void setSequenceId(final long sequenceId) {
    this.sequenceId = sequenceId;
  }

  public void setTokenType(final TokenType tokenType) {
    this.tokenType = tokenType;
  }

  public void setLemma(final String lemma) {
    this.lemma = lemma;
  }

  public void setAudioOffset(final AudioOffset audioOffset) {
    this.audioOffset = audioOffset;
  }

  public void setConfidence(final float confidence) {
    this.confidence = confidence;
  }

  public Token build() {
    Token ret = new Token(sequenceId,charOffset, value);
    ret.setAudioOffset(audioOffset);
    ret.setConfidence(confidence);
    ret.setTokenType(tokenType);
    ret.setLemma(lemma);
    return ret;
  }
}

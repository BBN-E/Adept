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

import adept.common.AsrName;
import adept.common.ChannelName;
import adept.common.ContentType;
import adept.common.Document;
import adept.common.SpeechUnit;
import adept.common.TokenStream;
import adept.common.TokenizerType;
import adept.common.TranscriptType;
import adept.common.TranslatorName;

/**
 * Created by jdeyoung on 10/29/15.
 */
public class TokenStreamBuilder {

  private TokenizerType tokenizerType;
  private TranscriptType transcriptType;
  private String language;
  private AsrName asrName;
  private ChannelName channelName;
  private SpeechUnit speechUnit;
  private ContentType contentType;
  private TranslatorName translatorName;
  private Document document;

  public TokenStreamBuilder() {

  }

  public TokenStreamBuilder setTokenizerType(final TokenizerType tokenizerType) {
    this.tokenizerType = tokenizerType;
    return this;
  }

  public TokenStreamBuilder setTranscriptType(final TranscriptType transcriptType) {
    this.transcriptType = transcriptType;
    return this;
  }

  public TokenStreamBuilder setLanguage(final String language) {
    this.language = language;
    return this;
  }

  public void setAsrName(final AsrName asrName) {
    this.asrName = asrName;
  }

  public TokenStreamBuilder setChannelName(final ChannelName channelName) {
    this.channelName = channelName;
    return this;
  }

  public TokenStreamBuilder setContentType(final ContentType contentType) {
    this.contentType = contentType;
    return this;
  }

  public TokenStreamBuilder setTranslatorName(final TranslatorName translatorName) {
    this.translatorName = translatorName;
    return this;
  }

  public void setSpeechUnit(final SpeechUnit speechUnit) {
    this.speechUnit = speechUnit;
  }

  public TokenStreamBuilder setDocument(final Document document) {
    this.document = document;
    return this;
  }

  public TokenStream build() {
    final TokenStream ret =
        new TokenStream(tokenizerType, transcriptType, language, channelName, contentType,
            document);
    if (translatorName != null) {
      ret.setTranslatorName(translatorName);
    }
    if(asrName != null) {
      ret.setAsrName(asrName);
    }
    if(speechUnit != null) {
      ret.setSpeechUnit(speechUnit);
    }
    return ret;
  }
}

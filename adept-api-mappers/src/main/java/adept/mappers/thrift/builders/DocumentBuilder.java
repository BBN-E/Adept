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
import adept.common.Document;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jdeyoung on 10/29/15.
 */
public class DocumentBuilder {


  private String docId;
  private Corpus corpus;
  private String docType;
  private String uri;
  private String language;
//  private List<TokenStream> tokenStreamList;
//  private List<TokenLattice> tokenLattice;
  private String genre;
  private String headline;
//  private String captureDate;
//  private String publicationDate;
//  private String name;
//  private String splitId;
//  private String sourceUri;
//  private String sourceLanguage;
//  private TokenStream defaultTokenStream;
  private String value;

  public DocumentBuilder setDocId(final String docId) {
    this.docId = docId;
    return this;
  }

  public DocumentBuilder setCorpus(final Corpus corpus) {
    this.corpus = corpus;
    return this;
  }

  public DocumentBuilder setDocType(final String docType) {
    this.docType = docType;
    return this;
  }

  public DocumentBuilder setUri(final String uri) {
    this.uri = uri;
    return this;
  }

  public DocumentBuilder setLanguage(final String language) {
    this.language = language;
    return this;
  }

//  public DocumentBuilder setTokenStreamList(final List<TokenStream> tokenStreamList) {
//    this.tokenStreamList = tokenStreamList;
//    return this;
//  }
//
//  public DocumentBuilder setTokenLattice(final List<TokenLattice> tokenLattice) {
//    this.tokenLattice = tokenLattice;
//    return this;
//  }

  public DocumentBuilder setGenre(final String genre) {
    this.genre = genre;
    return this;
  }

  public DocumentBuilder setHeadline(final String headline) {
    this.headline = headline;
    return this;
  }

//  public DocumentBuilder setCaptureDate(final String captureDate) {
//    this.captureDate = captureDate;
//    return this;
//  }
//
//  public DocumentBuilder setPublicationDate(final String publicationDate) {
//    this.publicationDate = publicationDate;
//    return this;
//  }
//
//  public DocumentBuilder setName(final String name) {
//    this.name = name;
//    return this;
//  }
//
//  public DocumentBuilder setSplitId(final String splitId) {
//    this.splitId = splitId;
//    return this;
//  }
//
//  public DocumentBuilder setSourceUri(final String sourceUri) {
//    this.sourceUri = sourceUri;
//    return this;
//  }
//
//  public DocumentBuilder setSourceLanguage(final String sourceLanguage) {
//    this.sourceLanguage = sourceLanguage;
//    return this;
//  }
//
//  public DocumentBuilder setDefaultTokenStream(final TokenStream defaultTokenStream) {
//    this.defaultTokenStream = defaultTokenStream;
//    return this;
//  }

  public DocumentBuilder setValue(String value) {
    this.value = value;
    return this;
  }

  public Document build() {
    checkNotNull(docId);
    checkNotNull(docType);
    checkNotNull(language);
    checkNotNull(value, "value must be set for Document to be of any use");
    final Document ret =
        new Document(this.docId, this.corpus, this.docType, this.uri, this.language);
//    if (tokenStreamList != null) {
//      ret.setTokenStreamList(tokenStreamList);
//    }
//    if (tokenLattice != null) {
//      ret.setTokenLattice(tokenLattice);
//    }
    if (genre != null) {
      ret.setGenre(genre);
    }
    if (headline != null) {
      ret.setHeadline(headline);
    }
//    if (captureDate != null) {
//      ret.setCaptureDate(captureDate);
//    }
//    if (publicationDate != null) {
//      ret.setPublicationDate(publicationDate);
//    }
//    if (name != null) {
//      ret.setName(name);
//    }
//    if (splitId != null) {
//      ret.setSplitId(splitId);
//    }
//    if (sourceUri != null) {
//      ret.setSourceUri(sourceUri);
//    }
//    if (sourceLanguage != null) {
//      ret.setSourceLanguage(sourceLanguage);
//    }
//    if (defaultTokenStream != null) {
//      ret.setDefaultTokenStream(defaultTokenStream);
//    }
    if (value != null) {
      ret.setValue(value);
    }
    return ret;
  }


}

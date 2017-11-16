package adept.io;

/*-
 * #%L
 * adept-api
 * %%
 * Copyright (C) 2012 - 2017 Raytheon BBN Technologies
 * %%
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
 * #L%
 */

import com.sun.org.apache.commons.logging.Log;
import com.sun.org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adept.common.Document;
import adept.common.HltContentContainer;
import adept.common.Passage;
import adept.common.Sentence;
import adept.common.TokenOffset;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

/**
 * The class AMRDocumentReader.
 */
public class AMRDocumentReader {

  private static final Log log = LogFactory.getLog(AMRDocumentReader.class);

  /**
   * Singleton instance.
   */
  private static AMRDocumentReader instance;

  /**
   * Gets the single instance of AMRDocumentReader.
   *
   * @return single instance of AMRDocumentReader
   */
  public static AMRDocumentReader getInstance() {
    if (instance == null)
      instance = new AMRDocumentReader();
    return instance;
  }

  /**
   * Private constructor to enforce singleton pattern.
   */
  private AMRDocumentReader() {}

  public Document loadAMRContentContainer(String filename, HltContentContainer hltcc) {
    log.info("Creating ADEPT document from file: " + filename);

    log.info("Loading input document as AMR-formatted file");
    AMRDocument amrDoc = loadAMRDocument(filename);
    adept.common.Document adeptDocument = amrDoc.getDocument();
    if (adeptDocument == null) {
      log.error("Unable to read document.");
      return null;
    }

    List<Sentence> sentenceList = amrDoc.getSentences();
    if (!sentenceList.isEmpty()) {
      hltcc.setSentences(sentenceList);
    }

    List<Passage> passageList = amrDoc.getPassages();
    if (!passageList.isEmpty()) {
      hltcc.setPassages(passageList);
    }

    return adeptDocument;

  }

  public HltContentContainer createAMRContentContainer(String filename) {
    log.info("Creating ADEPT document from file: " + filename);

    log.info("Loading input document as AMR-formatted file");
    AMRDocument amrDoc = loadAMRDocument(filename);
    adept.common.Document adeptDocument = amrDoc.getDocument();
    if (adeptDocument == null) {
      log.error("Unable to read document.");
      return null;
    }

    HltContentContainer hltcc = new HltContentContainer();
    List<Passage> passageList = amrDoc.getPassages();
    if (!passageList.isEmpty()) {
      hltcc.setPassages(passageList);
    }
    List<Sentence> sentenceList = amrDoc.getSentences();
    if (!sentenceList.isEmpty()) {
      hltcc.setSentences(sentenceList);
    }


    return hltcc;

  }

  /**
   * Reads specified AMR file into an AMRDocument (Document object wrapper).
   *
   * @param path Path to amr file to read.
   * @return an AMRDocument with fields derived from file at path.
   */
  public AMRDocument loadAMRDocument(String path) {
    String fileString;
    try {
      if (path.startsWith("hdfs:")) { // (Hadoop File System)
        Path p = new Path(path);
        FileSystem fs = FileSystem.get(p.toUri(), new Configuration());
        fileString = Reader.getInstance().convertStreamToString(fs.open(p));
      } else {
        fileString = Reader.getInstance().readFileIntoString(path);
      }
    } catch (IOException e) {
        throw new RuntimeException(e);
    }
    return new AMRDocument(fileString);
  }

  /**
   * Gets the sentences.
   *
   * @param conllDoc the conll doc
   * @return the sentences
   */
  public List<Sentence> getSentences(CoNLLDocument conllDoc) {
    List<List<String>> tokens = conllDoc.getTokens();
    List<Sentence> sentences = new ArrayList<Sentence>();

    int runningOffset = 0;
    for (int x = 0; x < tokens.size(); x++) {
      sentences.add(new Sentence((long) x, new TokenOffset(runningOffset, runningOffset + tokens.get(x).size() - 1), conllDoc.getDocument().getTokenStreamList().get(0)));
      runningOffset += tokens.get(x).size();
    }
    return sentences;
  }

}

/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

include "../common/common.thrift"

namespace java thrift.adept.io
namespace cpp thrift.adept.io
namespace perl thrift.adept.io

struct EREDocument {
  1: required string fullText,
  2: common.Document document,
  3: map<i64, i64> charMapping,
  4: map<i64, i64> charToTokenOffset,
  5: list<i32> startIndices,
  6: list<i32> endIndices,
  7: map<i64, common.EntityMention> entityMentionsById,
  8: map<i64, common.EntityMention> canonicalEntityMentionsById,
  9: bool isProxy,
  10: map<i64, list<common.Event>> eventsById
}

struct CoNLLDocument {
  1: required string fullText,
  2: common.Document document,
  3: list<list<i32>> wordNums,
  4: list<list<string>> tokens,
  5: list<list<string>> POSs,
  6: list<map<string, i64>> namedEntities,
  7: list<list<list<i64>>> corefs,
  8: map<common.Token, string> tokensToPOSs
}

service Reader {
  string readConversationFile(1:string path, 2:list<common.Utterance> utterances, 3:list<string> speakers, 4:string title),
  EREDocument readEREFile(1:string path, 2:string docId),
  common.HltContentContainer EREtoHltContentContainer(1:string EREPath, 2:string XMLPath),
  list<common.Sentence> getSentences(1:CoNLLDocument conllDoc),
  CoNLLDocument readCoNLLFile(1:string path),
  common.HltContentContainer CoNLLtoHltContentContainer(1:string filepath),
  string getAbsolutePathFromClasspathOrFileSystem(1:string name),
  string readFileIntoString(1:string path),
  string readFileIntoLines(1:string filename, 2:list<string> lines),
  list<string> fileToLines(1:string filename),
  string checkSurrogates (1:string text)
}

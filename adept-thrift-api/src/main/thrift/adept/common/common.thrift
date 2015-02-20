/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
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
* -------
*/


namespace perl thrift.adept.common
namespace cpp thrift.adept.common
namespace java thrift.adept.common

enum Polarity {
  POSITIVE = 0,
  NEGATIVE = 1,
  NEUTRAL = 2
}

enum TopicPolarity {
  POSITIVE = 0,
  NEGATIVE = 1,
  NONE = 2,
}

enum AsrName {
  NONE = 0,
  LDC = 1,
  BBN = 2
}

enum AudioFileType {
  WAV = 0,
  MP3 = 1,
  SPH = 2
}


enum ChannelName {
  NONE = 0,
  LEFT_STEREO = 1,
  RIGHT_STEREO =2,
  MONO = 3
}


enum ContentType {
  TEXT = 0,
  SPEECH = 1
}


enum EntailmentJudgment {
  ENTAILS = 0,
  CONTRADICTS = 1,
  NOT_RELATED = 2
}


enum Modality {
  ABILITY = 0,
  EFFORT = 1,
  INTENTION = 2,
  SUCCESS = 3,
  WANT = 4,
  COMMITTED_BELIEF = 5,
  NON_COMMITTED_BELIEF = 6,
  NON_ATTRIBUTABLE_BELIEF = 7,
}


enum SarcasmJudgment {
  POSITIVE = 0,
  NEGATIVE = 1,
  NONE = 2
}


enum SentenceType {
  NONE = 0,
  STATEMENT = 1,
  QUESTION = 2,
  INCOMPLETE = 3,
  CLAUSE = 4
}


enum SpeechUnit {
  NONE = 0,
  WORD = 1,
  PHONEME = 2
}


enum Subjectivity {
  SUBJECTIVE = 0,
  OBJECTIVE = 1,
  NONE = 2
}


enum TokenType {
  WORD = 0
  PUNCTUATION = 1,
  GARBAGE = 2,
  LEXEME = 3,
  PAUSE_FILLER = 4,
  LAUGHTER = 5,
  SILENCE = 6,
  MUSIC = 7,
  TAG = 8,
  OTHER = 9
}


enum TokenizerType {
  ADEPT = 0,
  STANFORD_CORENLP = 1,
  UMASS = 2,
  WHITESPACE = 3,
  OTHER = 4
}


enum TranscriptType {
  SOURCE = 0,
  TRANSLATION = 1
}


enum TranslatorName {
  NONE = 0,
  SDL_LW = 1
}


enum TranslatorType {
  NONE = 0,
  SDL_LW = 1
}

struct EmailAddress {
/**
 * The address
 */
  1: string address,
/**
 * The display name
 */
  2: string displayName
}

/**
 * The Class ID defines an identifier for all instances of objects used in the
 * ADEPT framework. The ID is generated as a universally unique identifier
 * (UUID)
 */
struct ID {
/**
 * The id
 */
  1: string id,
/**
 * The id string
 */
  2: string idStr,
/**
 * The maximum possible id
 */
  3: i64 MAX_ID,
/**
 * The minimum possible id
 */
  4: i64 MIN_ID
}

service IDService {
  string getId(),
  string getIdString()
}

struct LanguageIdentification {
/**
 * The language probability distribution
 */
  1: map<string, double> languageProbabilityDistribution
/**
 * The id
 */
  2: ID id,
/**
 * The value
 */
  3: string value
}

/**
 * An abstract definition of an item, which is composed of an id and a value.
 */
struct Item {
/**
 * The id
 */
  1: ID id,
/**
 * The value
 */
  2: string value
}

service ItemService {
  string getId(),
  string getIdString(),
  string getValue()
}

/**
 * Offset class captures begin and end integer positions of character or token
 * spans.
 */
struct TokenOffset {
/**
 * The begin index
 */
  1: required i64 beginIndex,
/**
 * The end index
 */
  2: required i64 endIndex
}

union TokenOffsetObject {
  1: TokenOffset tokenOffset
}

service TokenOffsetService {
  bool equals(1:TokenOffsetObject obj),
  i64 getBegin(),
  i64 getEnd(),
  i32 hashCode()
}

/**
 * The Class Corpus.
 */
struct Corpus {
/**
 * The corpus id
 */
  1: required string corpusId,
/**
 * The type
 */
  2: required string type,
/**
 * The name
 */
  3: required string name,
/**
 * The uri
 */
  4: required string uri,
/**
 * The id
 */
  5: optional ID id
}

/**
 * The Class TextDocument.
 */
struct Document {
/**
 * The doc id
 */
  1: required string docId,
/**
 * The corpus
 */
  2: required Corpus corpus,
/**
 * The doc type
 */
  3: required string docType,
/**
 * The uri to the location of text or transcript.
 */
  4: required string uri,
/**
 * The language
 */
  5: required string language,
/**
 * The audio uri
 */
  6: optional string audioUri,
/**
 * The genre
 */
  7: optional string genre,
/**
 * The headlinemap
 */
  8: optional string headline,
/**
 * The id
 */
  9: optional ID id,
/**
 * The value
 */
  10:optional  string value
}

/**
 * The Class DocumentList.
 */
struct DocumentList {
/**
 * The id
 */
  1: ID id,
/**
 * The Constant serialVersionUID
 */
  2: i64 serialVersionUID = 651655831447893195
}

service DocumentListService {
  string getId(),
  string getIdString()
}

/**
 * Offset class captures begin and end double positions of audio
 * spans.
 */
struct AudioOffset {
/**
 * The begin index
 */
  1: required double beginIndex,
/**
 * The end index
 */
  2: required double endIndex
}

service AudioOffsetService {
  double getBegin(),
  double getEnd()
}


struct Audio {
/**
 * The uri
 */
  1: string uri,
/**
 * The audioBuffer
 */
  2: binary audioBuffer,
/**
 * The audio type
 */
  3: AudioFileType audioType,
/**
 * The time span
 */
  4: AudioOffset timeSpan
}

/**
 * Offset class captures begin and end integer positions of character or token
 * spans.
 */
struct CharOffset {
/**
 * The begin index
 */
  1: required i32 beginIndex,
/**
 * The end index
 */
  2: required i32 endIndex
}

service CharOffsetService {
  i32 getBegin(),
  i32 getEnd()
}

struct Tag {
/**
 * The tag name
 */
  1: required string tagName,
/**
 * The display name
 */
  2: required map<string, string> attributes,
/**
 * The char offset
 */
  3: required CharOffset charOffset
}

/**
 * The Class Token.
 */
struct Token {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The char offset
 */
  2: required CharOffset charOffset,
/**
 * The value
 */
  3: required string value,
/**
 * The audio offset
 */
  4: optional AudioOffset audioOffset,
/**
 * The confidence
 */
  5: optional double confidence,
/**
 * The lemma
 */
  6: optional string lemma,
/**
 * The token type
 */
  7: optional TokenType tokenType,
/**
 * The id
 */
  8: optional ID id
}

service TokenService extends ItemService {
  AudioOffset getAudioOffset(),
  CharOffset getCharOffset(),
  double getConfidence(),
  string getLemma(),
  i64 getSequenceId(),
  TokenType getTokenType(),
  void setAudioOffset(1:AudioOffset audioOffset),
  void setConfidence(1:double confidence),
  void setLemma(1:string lemma),
  void setTokenType(1:TokenType tokentype)
}

/**
 * The Class TokenStream. In thrift, the token stream has
 * a tokenList field rather than extendind List<Token>
 */
struct TokenStream {
/**
 * The tokenizer type
 */
  1: required TokenizerType tokenizerType,
/**
 * The transcript type
 */
  2: required TranscriptType transcriptType,
/**
 * The language
 */
  3: required string language,
/**
 * The channel name
 */
  4: required ChannelName channelName,
/**
 * The content type
 */
  5: required ContentType contentType,
/**
 * The text value
 */
  6: required string textValue,
/**
 * The asr name
 */
  7: optional AsrName asrName,
/**
 * The document the token stream is from
 */
  8: optional Document document,
/**
 * The Constant serialVersionUID
 */
  9: optional i64 serialVersionUID = 2407040331925099456,
/**
 * The speech unit
 */
  10: optional SpeechUnit speechUnit,
/**
 * The translator name
 */
  11: optional  TranslatorName translatorName,
/**
 * The list of tokens in this token stream
 */
  12: optional list<Token> tokenList 
}

service TokenStreamService {
  ChannelName getChannelName(),
  ContentType getContentType(),
  Document getDocument(),
  string getLanguage(),
  i64 getSerialversionuid(),
  SpeechUnit getSpeechUnit(),
  string getTextValue(),
  TokenizerType getTokenizerType(),
  TranscriptType getTranscriptType(),
  TranslatorName getTranslatorName(),
  void setAsrName(1:AsrName asrName),
  void setDocument(1:Document document),
  void setSpeechUnit(1:SpeechUnit speechUnit),
  void setTranslatorName(1:TranslatorName translatorName)
}

struct Arc {
/**
 * The source
 */
  1: i32 source,
/**
 * The destination
 */
  2: i32 destination,
/**
 * The token
 */
  3: Token token,
/**
 * The weight
 */
  4: double weight
}

struct TokenLattice {
/**
 * The start state
 */
  1: i32 startState,
/**
 * The end state
 */
  2: i32 endState,
/**
 * The arcs
 */
  3: list<Arc> arcs
}

/**
 * The Class Chunk is defined as containing one or more tokens. A chunk is
 * assumed to be HLT algorithm generated metadata, and, hence ChunkUnion
 * inherits from HltContent class.
 */
struct Chunk {
/**
 * The token offset which is the index into token stream.
 */
  1: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  2: required TokenStream tokenStream,
/**
 * The id
 */
  3: optional ID id,
/**
 * The value
 */
  4: optional string value,
/**
 * The algorithmName
 */
  5: optional string algorithmName
}

exception AdeptException {
/**
 *The Constant serialVersionUID.
 */
  1: i64 serialVersionUID = 1
}

/**
 * The Class AnomalousText.
 */
struct AnomalousText {
/**
 * The confidence
 */
  1: required double confidence, 
/**
 * The document
 */
  2: required Document document,
/**
 * The explanation
 */
  3: optional string explanation,
/**
 * The id
 */
  4: optional ID id,
/**
 * The value
 */
  5: optional string value,
/**
 * The algorithmName
 */
  6: optional string algorithmName
}

service AnomalousTextService extends ItemService {
  double getConfidence(),
  Document getDocument(),
  string getExplanation(),
  void setExplanation(1:string explanation)
}

/**
 * The Class Type.
 */
struct Type {
/**
 * The type
 */
  1: required string type
}

service TypeService {
  string getType()
}

/**
 * The Class CommittedBelief.
 */
struct CommittedBelief {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The char offset
 */
  4: optional CharOffset charOffset,
/**
 * The modality
 */
  5: required Modality modality,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value,
/**
 * The algorithmName
 */
  8: optional string algorithmName
}

/**
 * The Class EntityMention.
 */
struct EntityMention {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The char offset
 */
  4: optional CharOffset charOffset,
/**
 * The entity id distribution
 */
  5: optional map<i64,double> entityIdDistribution,
/**
 * The entity type
 */
  6: optional Type entityType,
/**
 * The mention type
 */
  7: optional Type mentionType,
/**
 * The context
 */
  8: optional string context,
/**
 * The context size
 */
  9: optional i32 contextSize,
/**
 * The id
 */
  10: optional ID id,
/**
 * The value
 */
  11: optional string value,
/**
 * The algorithmName
 */
  12: optional string algorithmName
}

/**
 * The Class Entity is represented by a globally unique ID and a canonical
 * mention. The argumentConfidenceMap in ResolvedMention provides a distribution
 * over possible entities for a given Mention
 */
struct Entity {
/**
 * The entity id
 */
  1: required i64 entityId,
/**
 * The entity type
 */
  2: required Type entityType,
/**
 * The canonical mention
 */
  3: optional EntityMention canonicalMention,
/**
 * The id
 */
  4: optional ID id,
/**
 * The value
 */
  5: optional string value,
/**
 * The algorithmName
 */
  6: optional string algorithmName
}

union EntityObject {
  1: Entity entity
}

service EntityService extends ItemService {
  bool equals(1:EntityObject obj),
  EntityMention getCanonicalMention(),
  string getEntity(),
  i64 getEntityId(),
  Type getEntityType(),
  i32 hashCode(),
  void setCanonicalMentions(1:EntityMention canonicalMention)
}

/**
 * The Class Coreference.
 */
struct Coreference {
/**
 * The coreference id
 */
  1: required i64 coreferenceId,
/**
 * The entities
 */
  2: optional list<Entity> entities,
/**
 * The resolved entity mentions
 */
  3: optional list<EntityMention> resolvedEntityMentions,
/**
 * The id
 */
  4: optional ID id,
/**
 * The value
 */
  5: optional string value,
/**
 * The algorithmName
 */
  6: optional string algorithmName
}

service CoreferenceService extends ItemService{
  i64 getCoreferenceId(),
  list<Entity> getEntities(),
  list<EntityMention> getResolvedMentions(),
  void setEntities(1:list<Entity> entities),
  void setResolvedMentions(1:list<EntityMention> resolvedEntityMentions)
}


service CorpusService {
  string getCorpusId(),
  ID getId(),
  string getIdString(),
  string getName(),
  string getType(),
  string getUri()
}

/**
 * The Class DiscourseUnit.
 */
struct DiscourseUnit {
/**
 * The token offset which is the index into token stream.
 */
  1: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  2: required TokenStream tokenStream,
/**
 * The char offset
 */
  3: optional CharOffset charOffset,
/**
 * The sequence id
 */
  4: required i64 sequenceId,
/**
 * The discourse type
 */
  5: required string discourceType,
/**
 * The novelty confidence
 */
  6: optional double noveltyConfidence,
/**
 * The uncertainty confidence
 */
  7: optional double uncertaintyConfidence,
/**
 * The id
 */
  8: optional ID id,
/**
 * The value
 */
  9: optional string value,
/**
 * The algorithmName
 */
  10: optional string algorithmName
}

/**
 * The Class Passage.
 */
struct Passage {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The char offset
 */
  4: optional CharOffset charOffset,
/**
 * The content type
 */
  5: optional string contentType,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value,
/**
 * The algorithmName
 */
  8: optional string algorithmName
}

/**
 * The Class Message extends Passage and includes additional
 * fields present in online communications.
 */
struct Message {
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The sender
 */
  4: optional EmailAddress sender,
/**
 * The recipients
 */
  5: optional list<EmailAddress> recipients,
/**
 * The cc recipients
 */
  6: optional list<EmailAddress> ccRecipients,
/**
 * The Bcc recipients
 */
  7: optional list<EmailAddress> bccRecipients,
/**
 * The sent date
 */
  8: optional string sentDate,
/**
 * The priority
 */
  9: optional string priority,
/**
 * The subject
 */
  10: optional string subject,
/**
 * The content type
 */
  11: optional string contentType,
/**
 * The message id
 */
  13: optional string messageId,
/**
 * The user agent
 */
  14: optional string userAgent,
/**
 * The list of addresses the message is in reply to
 */
  15: optional list<string> inReplyTo,
/**
 * The reference
 */
  16: optional list<string> reference,
/**
 * The return path address
 */
  17: optional EmailAddress returnPathAddress,
/**
 * The id
 */
  18: optional ID id,
/**
 * The value
 */
  19: optional string value
}


/**
 * The Class Entailment.
 */
struct Entailment {
/**
 * The entailment id
 */
  1: required i64 entailmentId,
/**
 * The hypothesis
 */
  2: optional Passage hypothesis,
/**
 * The judgment distribution
 */
  3: optional map<EntailmentJudgment, double> judgmentDistribution,
/**
 * The text
 */
  4: optional Passage text,
/**
 * The id
 */
  5: optional ID id,
/**
 * The value
 */
  6: optional string value,
/**
 * The algorithmName
 */
  7: optional string algorithmName
}

/**
 * The Class HltContent is an abstract class that represents all the HLT content
 * generated by ADEPT algorithms. All algorithmic output types inherits from
 * HltContent.
 */
struct HltContent {
/**
 * The id
 */
  1: ID id,
/**
 * The value
 */
  2: string value,
/**
 * The algorithmName
 */
  3: optional string algorithmName
}

service HltContentService extends ItemService {
}

/**
 * The Class InterPausalUnit.
 */
struct InterPausalUnit {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The ipu audio offset
 */
  2: required AudioOffset ipuAudioOffset,
/**
 * The acoustic features
 */
  3: optional map<string, double> acousticFeatures,
/**
 * The id
 */
  4: optional ID id,
/**
 * The value
 */
  5: optional string value,
/**
 * The algorithmName
 */
  6: optional string algorithmName
}

service InterPausalUnitService extends ItemService {
  map<string, double> getAcousticFeatures(),
  AudioOffset getIpuAudioOffset(),
  i64 getSequenceId(),
  void setAcousticFeatures(1:map<string, double> acousticFeatures)
}


/**
 * The Class Opinion.
 */
struct Opinion {
/**
 * The token offset which is the index into token stream.
 */
  1: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  2: required TokenStream tokenStream,
/**
 * The char offset
 */
  3: optional CharOffset charOffset,
/**
 * The subjectivity
 */
  4: required Subjectivity subjectivity,
/**
 * The polarity
 */
  5: required Polarity polarity,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value,
/**
 * The algorithmName
 */
  8: optional string algorithmName
}

/**
 * The Class Paraphrase.
 */
struct Paraphrase {
/**
 * The value
 */
  1: required string value,
/**
 * The confidence
 */
  2: required double confidence,
/**
 * The part of speech tag
 */
  3: optional Type posTag,
/**
 * The id
 */
  4: optional ID id,
/**
 * The algorithmName
 */
  5: optional string algorithmName
}

service ParaphraseService extends ItemService {
  double getConfidence(),
  string getPosTag(),
  void setPosTag(1:Type posTag)
}

/**
 * The Class PartOfSpeech.
 */
struct PartOfSpeech {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The char offset
 */
  4: optional CharOffset charOffset,
/**
 * The part of speech tag
 */
  5: optional Type posTag,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value,
/**
 * The algorithmName
 */
  8: optional string algorithmName
}

service PolarityService {
  Polarity valueOf(1:string name),
  Polarity values()
}

/**
 * The Class PostQuote.
 */
struct PostQuote {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The open tag
 */
  4: required Tag openTag,
/**
 * The close tag
 */
  5: required Tag closeTag,
/**
 * The quotes 
 */
  6: optional list<i32> subQuotes,
/**
 * The char offset
 */
  7: optional CharOffset charOffset,
/**
 * The post id
 */
  8: optional string postId
/**
 * The id
 */
  9: optional ID id,
/**
 * The value
 */
  10: optional string value,
/**
 * The algorithmName
 */
  11: optional string algorithmName
}

/**
 * The Class Post.
 */
struct Post {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The open tag
 */
  4: required Tag openTag,
/**
 * The close tag
 */
  5: required Tag closeTag,
/**
 * The quotes 
 */
  6: optional list<i32> quotes,
/**
 * The sub quotes 
 */
  7: optional list<PostQuote> allSubQuotes,
/**
 * The char offset
 */
  8: optional CharOffset charOffset,
/**
 * The id
 */
  9: optional ID id,
/**
 * The value
 */
  10: optional string value,
/**
 * The algorithmName
 */
  11: optional string algorithmName
}

/**
 * The Class ProsodicPhrase.
 */
struct ProsodicPhrase {
/**
 * The token offset which is the index into token stream.
 */
  1: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  2: required TokenStream tokenStream,
/**
 * The char offset
 */
  3: optional CharOffset charOffset,
/**
 * The sequence id
 */
  4: required i64 sequenceId,
/**
 * The confidence
 */
  5: optional double confidence,
/**
 * The novelty confidence
 */
  6: optional double noveltyConfidence,
/**
 * The type
 */
  7: optional string type,
/**
 * The uncertainty confidence
 */
  8: optional double uncertaintyConfidence,
/**
 * The id
 */
  9: optional ID id,
/**
 * The value
 */
  10: optional string value,
/**
 * The algorithmName
 */
  11: optional string algorithmName
}

service JudgmentService {
  SarcasmJudgment valueOf(1:string name),
  SarcasmJudgment values()
}

/**
 * The Class Sarcasm.
 */
struct Sarcasm {
/**
 * The sarcasm id
 */
  1: required i64 sarcasmId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The char offset
 */
  4: optional CharOffset charOffset,
/**
 * The sarcasm judgment
 */
  5: required SarcasmJudgment judgment,
/**
 * The confidence
 */
  6: optional double confidence,
/**
 * The id
 */
  7: optional ID id,
/**
 * The value
 */
  8: optional string value,
/**
 * The algorithmName
 */
  9: optional string algorithmName
}

/**
 * The Sentence class extends Chunk and represents the output from sentence
 * boundary detection algorithm.
 */
struct Sentence {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The token offset which is the index into token stream.
 */
  2: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  3: required TokenStream tokenStream,
/**
 * The char offset
 */
  4: optional CharOffset charOffset,
/**
 * The novelty confidence
 */
  5: optional double noveltyConfidence,
/**
 * The punctuation
 */
  6: optional string punctuation,
/**
 * The sentence type
 */
  7: optional SentenceType type,
/**
 * The uncertainty confidence
 */
  8: optional double uncertaintyConfidence,
/**
 * The id
 */
  9: optional ID id,
/**
 * The value
 */
  10: optional string value,
/**
 * The algorithmName
 */
  11: optional string algorithmName
}

struct Session {
/**
 * The token offset which is the index into token stream.
 */
  1: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  2: required TokenStream tokenStream,
/**
 * The sequence id
 */
  3: required i64 sequenceId,
/**
 * The sentences
 */
  4: required string contentType,
/**
 * The sentences
 */
  5: required list<Sentence> sentences,
/**
 * The char offset
 */
  6: CharOffset charOffset,
/**
 * The id
 */
  7: optional ID id,
/**
 * The value
 */
  8: optional string value
/**
 * The algorithmName
 */
  9: optional string algorithmName
}

/**
 * The Class SentenceSimilarity.
 */
struct SentenceSimilarity {
/**
 * The similarity
 */
  1: required double similarity,
/**
 * The sentence 1
 */
  2: required Sentence sentence1,
/**
 * The sentence 2
 */
  3: required Sentence sentence2,
/**
 * The id
 */
  4: optional ID id,
/**
 * The value
 */
  5: optional string value,
/**
 * The algorithmName
 */
  6: optional string algorithmName
}

service SentenceSimilarityService extends ItemService {
  Sentence getSentence1(),
  Sentence getSentence2(),
  double getSimilarity()
}
 
/**
 * The Class Slot.
 */
struct Slot {
/**
 * The slot id
 */
  1: required i64 slotId
}

service SlotService {
  string getSlot(),
  i64 getSlotId()
}

/**
 * The Class Story.
 */
struct Story {
/**
 * The token offset which is the index into token stream.
 */
  1: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  2: required TokenStream tokenStream,
/**
 * The char offset
 */
  3: optional CharOffset charOffset,
/**
 * The sequence id
 */
  4: required i64 sequenceId,
/**
 * The topic labels
 */
  5: required list<string> topicLabels,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value,
/**
 * The algorithmName
 */
  8: optional string algorithmName
}

service SubjectivityService {
  Subjectivity valueOf(1:string name),
  Subjectivity values()
}


/**
 * The Class SyntacticChunk.
 */
struct SyntacticChunk {
/**
 * The sequence id
 */
  1: required i64 sequenceId,
/**
 * The syntactic chunk type
 */
  2: required Type scType,
/**
 * The token offset which is the index into token stream.
 */
  3: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  4: required TokenStream tokenStream,
/**
 * The char offset
 */
  5: optional CharOffset charOffset,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value,
/**
 * The algorithmName
 */
  8: optional string algorithmName
}

service DocumentService extends ItemService {
  void addTokenStream(1:TokenStream tokenStream),
  string getAudioUri(),
  Corpus getCorpus(),
  string getDocId(),
  string getDocType(),
  string getGenre(),
  string getHeadline(),
  string getLanguage(),
  TokenStream getTokenStream(1:TokenizerType tokenizerType),
  list<TokenStream> getTokenStreamList(),
  string getUri(),
  void setAudioUri(1:string audioUri),
  void setGenre(1:string genre),
  void setHeadline(1:string headline),
  void setTokenStreamList(1:list<TokenStream> tokenStreamList),
  void setValue(1:string value)
}

struct TokenStreamList {
  1: list<TokenStream> tokenStreamList
}

/**
 * The Class Viewpoint.
 */
struct Viewpoint {
/**
 * The speaker id
 */
  1: required string speakerId,
/**
 * The belief
 */
  2: required string belief
}

service ViewpointService {
  string getBelief(),
  string getSpeakerId()
}

/**
 * The Class Topic.
 */
struct Topic {
/**
 * The topic id
 */
  1: required i64 topicId,
/**
 * The name
 */
  2: required string name,
/**
 * The belief
 */
  3: optional string belief,
/**
 * The topic polarity
 */
  4: optional TopicPolarity polarity,
/**
 * The viewpoints
 */
  5: optional list<Viewpoint> viewpoints
}

service TopicService {
  void addViewpoint(1:Viewpoint viewpoint),
  string getBelief(),
  string getName(),
  TopicPolarity getPolarity(),
  i64 getTopicId(),
  list<Viewpoint> getViewpoints(),
  void setBelief(1:string belief),
  void setPolarity(1:TopicPolarity polarity),
  void setViewpoints(1:list<Viewpoint> viewpoints)
}

/**
 * The Class Value.
 */
struct Value {
/**
 * The value id
 */
  1: required i64 valueId
}

service ValueService {
  string getValue(),
  i64 getValueId()
}

/**
 * The Class Triple.
 */
struct Triple {
/**
 * The entity
 */
  1: required Entity entity,
/**
 * The slot
 */
  2: required Slot slot,
/**
 * The value
 */
  3: required string value
}

service TripleService {
  Entity getEntity(),
  Slot getSlot(),
  string getValue()
}

/**
 * The Class Utterance.
 */
struct Utterance {
/**
 * The token offset which is the index into token stream.
 */
  1: required TokenOffset tokenOffset,
/**
 * The token stream
 */
  2: required TokenStream tokenStream,
/**
 * The char offset
 */
  3: optional CharOffset charOffset,
/**
 * The utterance id
 */
  4: required i64 utteranceId,
/**
 * The speaker id
 */
  5: required i64 speakerId,
/**
 * The annotation
 */
  6: required string annotation,
/**
 * The id
 */
  7: optional ID id,
/**
 * The value
 */
  8: optional string value,
/**
 * The algorithmName
 */
  9: optional string algorithmName
}

service HltContentContainerListService {
  string getId(),
  string getIdString()
}

struct Conversation {
  1: required i64 conversationId,
  2: required string name,
  3: optional bool oneSided,
  4: optional list<Topic> topics,
  5: optional list<Utterance> utterances,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value
}

service ConversationService extends ItemService {
  void addTopic(1:Topic topic),
  void addUtterance(1:Utterance utterance),
  i64 getConversationId(),
  string getName(),
  list<Topic> getTopics(),
  list<Utterance> getUtterances(),
  bool isOneSided(),
  void setOneSided(1:bool oneSided),
  void setTopics(1:list<Topic> topics),
  void setUtterances(1:list<Utterance> utterances)
}

/**
 * The ChunkUnion includes all of the classes that extend
 * Chunk for the purposes of passing through interfaces.
 */
union ChunkUnion {
  1: CommittedBelief committedBelief,
  2: DiscourseUnit discourseUnit,
  3: EntityMention entityMention,
  4: Opinion opinion,
  5: PartOfSpeech partOfSpeech,
  6: Passage passage,
  7: ProsodicPhrase prosodicPhrase,
  8: Sarcasm sarcasm,
  9: Sentence sentence,
  10: Story story,
  11: SyntacticChunk syntacticChunk,
  12: Utterance utterance,
  13: Chunk chunk
}

/**
 * The Class Argument.
 */
struct Argument {
/**
 * The argument type
 */
  1: required Type argumentType,
/**
 * The argument distribution
 */
  2: optional map<ChunkUnion, double> argumentDistribution,
/**
 * The id
 */
  3: optional ID id,
/**
 * The value
 */
  4: optional string value,
/**
 * The algorithm name
 */
  5: optional string algorithmName
}

/**
 * The Class ArgumentTuple.
 */
struct ArgumentTuple {
/**
 * The tuple type
 */
  1: required Type tupleType,
/**
 * The attributes
 */
  2: optional list<Type> attributes,
/**
 * The arguments
 */
  3: optional list<Argument> arguments,
/**
 * The confidence
 */
  4: optional double confidence,
/**
 * The id
 */
  5: optional ID id,
/**
 * The value
 */
  6: optional string value,
/**
 * The algorithm name
 */
  7: optional string algorithmName
}

/**
 * The Class Event.
 */
struct Event {
/**
 * The event id
 */
  1: required i64 eventId,
/**
 * The type
 */
  2: required Type eventType,
/**
 * The attributes
 */
  3: optional list<Type> attributes,
/**
 * The arguments
 */
  4: optional list<Argument> arguments,
/**
 * The confidence
 */
  5: optional double confidence,
/**
 * The id
 */
  6: optional ID id,
/**
 * The value
 */
  7: optional string value,
/**
 * The algorithm name
 */
  8: optional string algorithmName
}

/**
 * The Class Event Relations.
 */
struct EventRelations {
/**
 * The coreferences
 */
  1: optional list<Event> coreferences
}


service ArgumentService {
  bool addArgumentConfidencePair(1:ChunkUnion argument, 2:double confidence),
  map<ChunkUnion, double> getArgumentDistribution(),
  string getArgumentType(),
  ChunkUnion getBestArgument(),
  void setArgumentDistribution(1:map<ChunkUnion, double> argumentDistribution)
}


/**
 * The Class Relation.
 */
struct Relation {
/**
 * The relation id
 */
  1: required i64 relationId,
/**
 * The relation type
 */
  2: required Type type,
/**
 * The arguments
 */
  3: optional list<Argument> arguments,
/**
 * The confidence
 */
  4: optional double confidence,
/**
 * The id
 */
  5: optional ID id,
/**
 * The value
 */
  6: optional string value,
/**
 * The algorithmName
 */
  7: optional string algorithmName
}

service RelationService extends ItemService {
  bool addArgument(1:Argument argument),
  list<Argument> getArguments(),
  double getConfidence(),
  string getRelationType(),
  void setConfidence(1:double confidence)
}

/**
 * The Class JointRelationCoreference represents the container for the output of
 * the algorithm that processes both coreference resolution and relation
 * extraction simultaneously.
 */
struct JointRelationCoreference {
/**
 * The coreference
 */
  1: Coreference coreference,
/**
 * The relations
 */
  2: list<Relation> relations,
/**
 * The id
 */
  3: ID id,
/**
 * The value
 */
  4: string value,
/**
 * The algorithmName
 */
  5: optional string algorithmName
}

service JointRelationCoreferenceService extends ItemService {
  Coreference getCoreference(),
  list<Relation> getRelations(),
  void setCoreference(1:Coreference coreference),
  void setRelations(1:list<Relation> relations)
}

/**
 * The Class Dependency.
 */
struct Dependency {
/**
 * The governor
 */
  1: required ChunkUnion governor,
/**
 * The dependent
 */
  2: required ChunkUnion dependent,
/**
 * The dependency type
 */
  3: required string dependencyType,
/**
 * The id
 */
  4: optional ID id,
/**
 * The value
 */
  5: optional string value,
/**
 * The algorithmName
 */
  6: optional string algorithmName
}

service DependencyService extends ItemService{
  string getDependencyType(),
  ChunkUnion getDependent(),
  ChunkUnion getGovernor()
} 

/**
 * The Class HltContentContainer.
 */
struct HltContentContainer {
/**
 * The headline
 */
  1: list<CommittedBelief> committedBeliefs,
/**
 * The headline
 */
  2: list<Coreference> coreferences,
/**
 * The headline
 */
  3: list<Dependency> dependencies,
/**
 * The entity mentions
 */
  4: list<EntityMention> entityMentions,
/**
 * The joint relation coreferences
 */
  5: list<JointRelationCoreference> jointRelationCoreferences,
/**
 * The named entities
 */
  6: list<EntityMention> namedEntities,
/**
 * The opinions
 */
  7: list<Opinion> opinions,
/**
 * The parts of speech
 */
  8: list<PartOfSpeech> partOfSpeechs,
/**
 * The passages
 */
  9: list<Passage> passages,
/**
 * The posts
 */
  10: list<Post> posts,
/**
 * The prosodic phrases
 */
  11: list<ProsodicPhrase> prosodicPhrases,
/**
 * The relations
 */
  12: list<Relation> relations,
/**
 * The sarcasms
 */
  13: list<Sarcasm> sarcasms,
/**
 * The sentences
 */
  14: list<Sentence> sentences,
/**
 * The syntactic chunks
 */
  15: list<SyntacticChunk> syntacticChunks,
/**
 * The sessions
 */
  16: list<Session> sessions,
/**
 * The utterances
 */
  17: list<Utterance> utterances,
/**
 * The messages
 */
  18: list<Message> messages,
/**
 * The interpausal units
 */
  19: list<InterPausalUnit> interPausalUnits,
/**
 * The events
 */
  20: list<Event> events,
/**
 * The event relations
 */
  21: list<EventRelations> eventRelations,
/**
 * The id
 */
  22: ID id,
/**
 * The value
 */
  23: string value,
/**
 * The algorithmName
 */
  24: optional string algorithmName
}

service HltContentContainerService extends ItemService {
  list<CommittedBelief> getCommittedBeliefs(),
  list<Coreference> getCoreferences(),
  list<Dependency> getDependencies(),
  list<EntityMention> getEntityMentions(),
  list<JointRelationCoreference> getJointRelationCoreferences(),
  list<EntityMention> getMentions(),
  list<EntityMention> getNamedEntities(),
  list<Opinion> getOpinions(),
  list<PartOfSpeech> getPartOfSpeechs(),
  list<Passage> getPassages(),
  list<ProsodicPhrase> getProsodicPhrases(),
  list<Relation> getRelations(),
  list<Sarcasm> getSarcasms(),
  list<Sentence> getSentences(),
  list<SyntacticChunk> getSyntacticChunks(),
  void setCommittedBeliefs(1:list<CommittedBelief> committedBeliefs),
  void setCoreferences(1:list<Coreference> coreferences),
  void setDependencies(1:list<Dependency> dependencies),
  void setEntityMentions(1:list<EntityMention> entityMentions),
  void setJointRelationCoreferences(1:list<JointRelationCoreference> jointRelationCoreferences),
  void setMentions(1:list<EntityMention> entityMentions),
  void setNamedEntities(1:list<EntityMention> namedEntities),
  void setOpinions(1:list<Opinion> opinions),
  void setPartOfSpeechs(1:list<PartOfSpeech> partOfSpeechs),
  void setPassages(1:list<Passage> passages),
  void setProsodicPhrases(1:list<ProsodicPhrase> prosodicPhrases),
  void setRelations(1:list<Relation> relations),
  void setSarcasms(1:list<Sarcasm> sarcasms),
  void setSentences(1:list<Sentence> sentences),
  void setSyntacticChunks(1:list<SyntacticChunk> syntactiChunks)
}

/**
 * The Class HltContentContainerList contains a list of HltContentContainers
 * and a unique ID.
 */
struct HltContentContainerList {
/**
 * The id
 */
  1: ID id,
/**
 * The constant serialVersionUID
 */
  2: i64 serialVersionUID = 651655831447893195,
/**
 * The list of HltContentContainers
 */
  3: list<HltContentContainer> hltContentContainerList
}

service EntailmentService extends ItemService {
  bool addJudgmentConfidencePair(1:EntailmentJudgment judgment, 2:double confidence),
  ChunkUnion getBestJudgment(),
  i64 getEntailmentId(),
  Passage getHypothesis(),
  map<EntailmentJudgment, double> getJudgmentDistribution(),
  Passage getText(),
  void setHypothesis(1:Passage hypothesis),
  void setJudgmentDistribution(1:map<EntailmentJudgment, double> judgmentDistribution),
  void setText(1:Passage text)
}

/**
 * The Class Translation.
 */
struct Translation {
/**
 * The source chunk
 */
  1: required ChunkUnion sourceChunk,
/**
 * The target chunk
 */
  2: required ChunkUnion targetChunk
}

service TranslationService {
  ChunkUnion getSourceChunk(),
  ChunkUnion getTargetChunk()
}

service ChunkService extends ItemService {
  bool Contains(1:ChunkUnion chunk),
  bool equals(1:ChunkUnion obj),
  ChunkUnion getContainingChunk(1:list<ChunkUnion> chunks),
  ChunkUnion getMatchingChunk(1:list<ChunkUnion> chunks),
  TokenOffset getTokenOffset(),
  TokenStream getTokenStream(),
  i32 hashCode(),
  void setTokenStream(1:TokenStream tokenStream)
}

service CommittedBeliefService extends ChunkService {
  Modality getModality(),
  i64 getSequenceId()
}

service EntityMentionService extends ChunkService {
  void addEntityConfidencePair(1:i64 entityId, 2:double confidence),
  bool equals(1:EntityMention obj),
  double getConfidence(1:i64 entityId),
  map<i64,double> getEntityIdDistribution(),
  Type getEntityType(),
  Type getMentionType(),
  i64 getSequenceId(),
  void setEntityIdDistribution(1:map<i64,double> entityIdDistribution),
  void setEntityType(1:Type entityType),
  void setMentionType(1:Type mentionType)
}

service DiscourseUnitService extends ChunkService {
  string getDiscourceType(),
  double getNoveltyConfidence(),
  i64 getSequenceId(),
  double getUncertaintyConfidence(),
  void setDiscourceType(1:string discourceType),
  void setNoveltyConfidence(1:double noveltyConfidence),
  void setUncertaintyConfidence(1:double uncertaintyConfidence)
}

service PassageService extends ChunkService {
  string getContentType(),
  i64 getSequenceId(),
  void setContentType(1:string contentType)
}

service MessageService extends PassageService {
  string getSender(),
  void setSender(1:string sender),
  string getSentDate(),
  void setSentDate(1:string sentDate),
  string getPriority(),
  void setPriority(1:string priority),
  string getSubject(),
  void setSubject(1:string subject),
  list<string> getRecipients(),
  void setRecipients(1:list<string> recipients),
  void addRecipient(1:string recipient),
  list<string> getCcRecipients(),
  void setCcRecipients(1:list<string> ccRecipients),
  void addCcRecipient(1:string ccRecipient),
  list<string> getBccRecipients(),
  void setBccRecipients(1:list<string> bccRecipients),
  void addBccRecipient(1:string bccRecipient)
}

service OpinionService extends ChunkService {
  Polarity getPolarity(),
  Subjectivity getSubjectivity()
}

service PartOfSpeechService extends ChunkService {
  Type getPartOfSpeechTag(),
  Type getPosTag(),
  i64 getSequenceId(),
  void setPosTag(1:Type posTag)
}

service ProsodicPhraseService extends ChunkService {
  double getConfidence(),
  double getNoveltyConfidence(),
  i64 getSequenceId(),
  string getType(),
  double getUncertaintyConfidence(),
  void setConfidence(1:double confidence),
  void setNoveltyConfidence(1:double noveltyConfidence),
  void setType(1:string type),
  void setUncertaintyConfidence(1:double uncertaintyConfidence)
}

service SarcasmService extends ChunkService {
  double getConfidence(),
  SarcasmJudgment getJudgment(),
  i64 getSarcasmId(),
  void setConfidence(1:double confidence)
}

service SentenceService extends ChunkService {
  double getNoveltyConfidence(),
  string getPunctuation(),
  i64 getSequenceId(),
  SentenceType getType(),
  double getUncertaintyConfidence(),
  void setNoveltyConfidence(1:double noveltyConfidence),
  void setPunctuation(1:string punctuation),
  void setType(1:SentenceType type),
  void setUncertaintyConfidence(1:double uncertaintyConfidence)
}

service StoryService extends ChunkService {
  i64 getSequenceId(),
  list<string> getTopicLabels()
}

service SyntacticChunkService extends ChunkService {
  i64 getSequenceId(),
  Type getSyntacticChunkType()
}

service UtteranceService extends ChunkService {
  string getAnnotation(),
  i64 getSpeakerId(),
  i64 getUtteranceId(),
  void setAnnotation(1:string annotation)
}


/**
 * The HltContentUnion includes all of the classes that extend
 * HltContent for the purposes of passing through interfaces.
 */
union HltContentUnion {
  1: AnomalousText anomalousText,
  2: Argument argument,
  3: Chunk chunk,
  4: CommittedBelief committedBelief,
  5: DiscourseUnit discourseUnit,
  6: EntityMention entityMention,
  7: Opinion opinion,
  8: PartOfSpeech partOfSpeech,
  9: Passage passage,
  10: ProsodicPhrase prosodicPhrase,
  11: Sarcasm sarcasm,
  12: Sentence sentence,
  13: Story story,
  14: SyntacticChunk syntacticChunk,
  15: Utterance utterance,
  16: Coreference coreference,
  17: Dependency dependency,
  18: Entity entity,
  19: HltContentContainer hltContentContainer,
  20: InterPausalUnit interPausalUnit,
  21: JointRelationCoreference jointRelationCoreference,
  22: Paraphrase paraphrase,
  23: Relation relation,
  24: SentenceSimilarity sentenceSimilarity,
  25: ChunkUnion chunkUnion
}
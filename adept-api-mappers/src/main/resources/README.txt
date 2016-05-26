For the purposes of the LORELEI program, BBN supports a minimal serialization of
ADEPT objects into corresponding Thrift objects.

BBN supports the following partial two-way conversions, where + is required, ?
is optional (nullable). If there is any doubt about support for a particular 
field, please consult MappingTests.java. 

The constraints listed below are those required for the ADEPT API; they may be
more strict for serialization, and should be considered the source for the 
ADEPT -> Thrift translation. We do not preserve the ADEPT UUIDs when serializing.

This effort grew out of a previous (also somewhat incomplete) ADEPT <-> Thrift
mapping attempt. There are a handful of vestigal Speech fields from that time
left as a convenience for implementation. We may add support for more fields
in the future as these converters are used.

The base object for this conversion is the HLTContentContainer. It can be 
transformed from ADEPT serialized XML to Thrift (json) serialized equivalent
objects. Mappings:
* ADEPT -> Thrift, AdeptToThrift.java <input/adept/xml/file>  <output/thrift/json/file>
* Thrift -> ADEPT, ThriftToAdept.java <input/thrift/json/file> <output/adept/xml/file>


HLTContentContainer (adept.common, thrift.adept.common):
? sentences             :: [Sentence]
? entityMentions        :: [EntityMention]
? namedEntities         :: [EntityMention]

Sentence (adept.common, thrift.adept.common):
+ sequenceId            :: long
+ tokenStream           :: TokenStream
+ tokenOffset           :: TokenOffset
? morphSentence         ;: MorphSentence
? sentenceType          :: enum (SentenceType)
? punctuation           :: String
? uncertaintyConfidence :: float
? noveltyConfidence     :: float

TokenOffset:
+ begin                 :: int
+ end                   :: int

TokenStream (extends ArrayList<Token>):
+ tokenizerType         :: enum (TokenizerType)
+ language              :: String
+ document              :: Document
? transcriptType        :: enum (TranscriptType)
? contentType           :: enum (ContentType)
? channelName           :: enum (ChannelName)
? asrName               :: enum (AsrName)
? speechUnit            :: enum (SpeechUnit)
? translatorName        :: enum (TranslatorName)

Token:
+ sequenceId            :: long
+ charOffset            :: CharOffset
+ value                 :: String
? tokenType             :: enum (TokenType)
? lemma                 :: String
? confidence            :: float

CharOffset:
+ begin                 :: int
+ end                   :: int

Document:
+ docId                 :: String
+ doctype               :: String
+ language              :: String
+ value                 :: String
+ corpus                :: Corpus (ADEPT accidentally treats this as nullable)
? uri                   :: String
? genre                 :: String
? headline              :: String
? captureDate           :: String
? publicationDate       :: String
? name                  :: String

Also note that Documents in ADEPT may have a TokenStreamList. We do not support
this, since Thrift cannot represent anything other than a tree well.

Corpus:
+ corpusId              :: String
+ name                  :: String
? type                  :: String
? uri                   :: String

EntityMention:
+ sequenceId            :: long
+ tokenOffset           :: TokenOffset
+ tokenStream           :: TokenStream
? mentionType           :: IType
? entityType            :: IType
? attributes            :: Map<String, String>
? entityIdDistribution  :: Map<long, Float>

Type implements IType:
+ type                  :: String

MorphSentence
+ sequences             :: [MorphTokenSequence]

MorphTokenSequence
+ tokens                :: [MorphToken]
+ confidence            :: float
+ sourceAlgorithm       :: SourceAlgorithm

MorphToken
+ tokenStream           :: TokenStream
+ text                  :: String
? lemma                 :: String
? confidence            :: float
? roots                 :: [String]
? morphs                :: [Morph]
? features              :: [MorphFeature]
? headTokenOffset       :: TokenOffset
? tokenOffsets          :: [TokenOffset]
? pos                   :: String
? notes                 :: String

Morph
+ form                  :: String
+ morphType             :: MorphType
? features              :: [MorphFeature]
? sourceOffsets         :: [CharOffset]

MorphType
+ type                  :: String

MorphFeature
+ property              :: String
+ value                 :: String

SourceAlgorithm
+ algorithmName         :: String
+ contributingSiteName  :: String

Also note that the entityIdDistribution is relatively meaningless at this point
in time because we do not currently support ADEPT<->Thrift entities. This may
be added in the future.

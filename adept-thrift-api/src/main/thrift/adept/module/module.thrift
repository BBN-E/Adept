include "../common/common.thrift"

namespace java thrift.adept.module
namespace cpp thrift.adept.module
namespace perl thrift.adept.module


struct ModuleConfig {
  1: i64 serialVersionUID,
  2: map<string, string> properties
}

struct ModuleTraits {
  1: string schema,
  2: string trait,
  3: string version
}

service ChunkProcessor {
  list<common.HltContentUnion> process(1:common.ChunkUnion chunk),
  i64 processAsync(1:common.ChunkUnion chunk),
  bool tryGetResult(1:i64 requestId, 2:list<common.HltContentUnion> hltContents)
}

service ConversationProcessor {
  common.Conversation process(1:common.Conversation conversation),
  i64 processAsync(1:common.Conversation conversation),
  bool tryGetResult(1:i64 requestId, 2:common.Conversation conversation)
}

service DocumentHltContentProcessor {
  list<common.HltContentUnion> process(1:common.Document document, 2:common.HltContentContainer hltContentContainer),
  i64 processAsync(1:common.Document document, 2:common.HltContentContainer hltContentContainer),
  bool tryGetResult(1:i64 requestId, 2:list<common.HltContentUnion> hltContents)
}

service DocumentListProcessor {
  common.HltContentContainer process(1:common.DocumentList documentList, 2:common.HltContentContainer hltContentContainer),
  i64 processAsync(1:common.DocumentList documentList, 2:common.HltContentContainer hltContentContainer),
  bool tryGetResult(1:i64 requestId, 2:common.HltContentContainer hltContentContainer)
}

service DocumentProcessor {
  common.HltContentContainer process(1:common.Document document, 2:common.HltContentContainer hltContentContainer),
  i64 processAsync(1:common.Document document, 2:common.HltContentContainer hltContentContainer),
  bool tryGetResult(1:i64 requestId, 2:common.HltContentContainer hltContentContainer)
}

service Module {
  void activate(1:string configFilePath),
  void deactivate(),
  ModuleConfig getModuleConfig(),
  ModuleTraits getModuleTraits()
}


service PassagePairProcessor {
  list<common.HltContentUnion> process(1:common.Passage passage1, 2:common.Passage passage2),
  i64 processAsync(1:common.Passage passage1, 2:common.Passage passage2),
  bool tryGetResult(1:i64 requestId, 2:list<common.HltContentUnion> metaContents)
}


service PassageProcessor {
  list<common.HltContentUnion> process(1:common.Passage passage),
  i64 processAsync(1:common.Passage passage),
  bool tryGetResult(1:i64 requestId, 2:list<common.HltContentUnion> metaContents)
}

service SentencePairProcessor {
  list<common.HltContentUnion> process(1:common.Sentence sentence1, 2:common.Sentence sentence2),
  i64 processAsync(1:common.Sentence sentence1, 2:common.Sentence sentence2),
  bool tryGetResult(1:i64 requestId, 2:list<common.HltContentUnion> hltContents)
}

service SentenceProcessor {
  list<common.HltContentUnion> process(1:common.Sentence sentence),
  i64 processAsync(1:common.Sentence sentence),
  bool tryGetResult(1:i64 requestId, 2:list<common.HltContentUnion> hltContents)
}

service ModuleConfigService {
  void loadModuleConfig(1:string configFilePath)
}

service ModuleTraitsService {
  string getSchema(),
  string getTrait(),
  string getVersion(),
  void setSchema(1:string schema),
  void setTrait(1:string trait),
  void setVersion(1:string version)
}


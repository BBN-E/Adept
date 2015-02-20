/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef ModuleTraitsService_H
#define ModuleTraitsService_H

#include <thrift/TDispatchProcessor.h>
#include "module_types.h"

namespace thrift { namespace adept { namespace module {

class ModuleTraitsServiceIf {
 public:
  virtual ~ModuleTraitsServiceIf() {}
  virtual void getSchema(std::string& _return) = 0;
  virtual void getTrait(std::string& _return) = 0;
  virtual void getVersion(std::string& _return) = 0;
  virtual void setSchema(const std::string& schema) = 0;
  virtual void setTrait(const std::string& trait) = 0;
  virtual void setVersion(const std::string& version) = 0;
};

class ModuleTraitsServiceIfFactory {
 public:
  typedef ModuleTraitsServiceIf Handler;

  virtual ~ModuleTraitsServiceIfFactory() {}

  virtual ModuleTraitsServiceIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(ModuleTraitsServiceIf* /* handler */) = 0;
};

class ModuleTraitsServiceIfSingletonFactory : virtual public ModuleTraitsServiceIfFactory {
 public:
  ModuleTraitsServiceIfSingletonFactory(const boost::shared_ptr<ModuleTraitsServiceIf>& iface) : iface_(iface) {}
  virtual ~ModuleTraitsServiceIfSingletonFactory() {}

  virtual ModuleTraitsServiceIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(ModuleTraitsServiceIf* /* handler */) {}

 protected:
  boost::shared_ptr<ModuleTraitsServiceIf> iface_;
};

class ModuleTraitsServiceNull : virtual public ModuleTraitsServiceIf {
 public:
  virtual ~ModuleTraitsServiceNull() {}
  void getSchema(std::string& /* _return */) {
    return;
  }
  void getTrait(std::string& /* _return */) {
    return;
  }
  void getVersion(std::string& /* _return */) {
    return;
  }
  void setSchema(const std::string& /* schema */) {
    return;
  }
  void setTrait(const std::string& /* trait */) {
    return;
  }
  void setVersion(const std::string& /* version */) {
    return;
  }
};


class ModuleTraitsService_getSchema_args {
 public:

  ModuleTraitsService_getSchema_args() {
  }

  virtual ~ModuleTraitsService_getSchema_args() throw() {}


  bool operator == (const ModuleTraitsService_getSchema_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ModuleTraitsService_getSchema_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_getSchema_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_getSchema_pargs {
 public:


  virtual ~ModuleTraitsService_getSchema_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ModuleTraitsService_getSchema_result__isset {
  _ModuleTraitsService_getSchema_result__isset() : success(false) {}
  bool success;
} _ModuleTraitsService_getSchema_result__isset;

class ModuleTraitsService_getSchema_result {
 public:

  ModuleTraitsService_getSchema_result() : success() {
  }

  virtual ~ModuleTraitsService_getSchema_result() throw() {}

  std::string success;

  _ModuleTraitsService_getSchema_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const ModuleTraitsService_getSchema_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ModuleTraitsService_getSchema_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_getSchema_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ModuleTraitsService_getSchema_presult__isset {
  _ModuleTraitsService_getSchema_presult__isset() : success(false) {}
  bool success;
} _ModuleTraitsService_getSchema_presult__isset;

class ModuleTraitsService_getSchema_presult {
 public:


  virtual ~ModuleTraitsService_getSchema_presult() throw() {}

  std::string* success;

  _ModuleTraitsService_getSchema_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class ModuleTraitsService_getTrait_args {
 public:

  ModuleTraitsService_getTrait_args() {
  }

  virtual ~ModuleTraitsService_getTrait_args() throw() {}


  bool operator == (const ModuleTraitsService_getTrait_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ModuleTraitsService_getTrait_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_getTrait_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_getTrait_pargs {
 public:


  virtual ~ModuleTraitsService_getTrait_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ModuleTraitsService_getTrait_result__isset {
  _ModuleTraitsService_getTrait_result__isset() : success(false) {}
  bool success;
} _ModuleTraitsService_getTrait_result__isset;

class ModuleTraitsService_getTrait_result {
 public:

  ModuleTraitsService_getTrait_result() : success() {
  }

  virtual ~ModuleTraitsService_getTrait_result() throw() {}

  std::string success;

  _ModuleTraitsService_getTrait_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const ModuleTraitsService_getTrait_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ModuleTraitsService_getTrait_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_getTrait_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ModuleTraitsService_getTrait_presult__isset {
  _ModuleTraitsService_getTrait_presult__isset() : success(false) {}
  bool success;
} _ModuleTraitsService_getTrait_presult__isset;

class ModuleTraitsService_getTrait_presult {
 public:


  virtual ~ModuleTraitsService_getTrait_presult() throw() {}

  std::string* success;

  _ModuleTraitsService_getTrait_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class ModuleTraitsService_getVersion_args {
 public:

  ModuleTraitsService_getVersion_args() {
  }

  virtual ~ModuleTraitsService_getVersion_args() throw() {}


  bool operator == (const ModuleTraitsService_getVersion_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ModuleTraitsService_getVersion_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_getVersion_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_getVersion_pargs {
 public:


  virtual ~ModuleTraitsService_getVersion_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ModuleTraitsService_getVersion_result__isset {
  _ModuleTraitsService_getVersion_result__isset() : success(false) {}
  bool success;
} _ModuleTraitsService_getVersion_result__isset;

class ModuleTraitsService_getVersion_result {
 public:

  ModuleTraitsService_getVersion_result() : success() {
  }

  virtual ~ModuleTraitsService_getVersion_result() throw() {}

  std::string success;

  _ModuleTraitsService_getVersion_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const ModuleTraitsService_getVersion_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ModuleTraitsService_getVersion_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_getVersion_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ModuleTraitsService_getVersion_presult__isset {
  _ModuleTraitsService_getVersion_presult__isset() : success(false) {}
  bool success;
} _ModuleTraitsService_getVersion_presult__isset;

class ModuleTraitsService_getVersion_presult {
 public:


  virtual ~ModuleTraitsService_getVersion_presult() throw() {}

  std::string* success;

  _ModuleTraitsService_getVersion_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _ModuleTraitsService_setSchema_args__isset {
  _ModuleTraitsService_setSchema_args__isset() : schema(false) {}
  bool schema;
} _ModuleTraitsService_setSchema_args__isset;

class ModuleTraitsService_setSchema_args {
 public:

  ModuleTraitsService_setSchema_args() : schema() {
  }

  virtual ~ModuleTraitsService_setSchema_args() throw() {}

  std::string schema;

  _ModuleTraitsService_setSchema_args__isset __isset;

  void __set_schema(const std::string& val) {
    schema = val;
  }

  bool operator == (const ModuleTraitsService_setSchema_args & rhs) const
  {
    if (!(schema == rhs.schema))
      return false;
    return true;
  }
  bool operator != (const ModuleTraitsService_setSchema_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_setSchema_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setSchema_pargs {
 public:


  virtual ~ModuleTraitsService_setSchema_pargs() throw() {}

  const std::string* schema;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setSchema_result {
 public:

  ModuleTraitsService_setSchema_result() {
  }

  virtual ~ModuleTraitsService_setSchema_result() throw() {}


  bool operator == (const ModuleTraitsService_setSchema_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ModuleTraitsService_setSchema_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_setSchema_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setSchema_presult {
 public:


  virtual ~ModuleTraitsService_setSchema_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _ModuleTraitsService_setTrait_args__isset {
  _ModuleTraitsService_setTrait_args__isset() : trait(false) {}
  bool trait;
} _ModuleTraitsService_setTrait_args__isset;

class ModuleTraitsService_setTrait_args {
 public:

  ModuleTraitsService_setTrait_args() : trait() {
  }

  virtual ~ModuleTraitsService_setTrait_args() throw() {}

  std::string trait;

  _ModuleTraitsService_setTrait_args__isset __isset;

  void __set_trait(const std::string& val) {
    trait = val;
  }

  bool operator == (const ModuleTraitsService_setTrait_args & rhs) const
  {
    if (!(trait == rhs.trait))
      return false;
    return true;
  }
  bool operator != (const ModuleTraitsService_setTrait_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_setTrait_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setTrait_pargs {
 public:


  virtual ~ModuleTraitsService_setTrait_pargs() throw() {}

  const std::string* trait;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setTrait_result {
 public:

  ModuleTraitsService_setTrait_result() {
  }

  virtual ~ModuleTraitsService_setTrait_result() throw() {}


  bool operator == (const ModuleTraitsService_setTrait_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ModuleTraitsService_setTrait_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_setTrait_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setTrait_presult {
 public:


  virtual ~ModuleTraitsService_setTrait_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _ModuleTraitsService_setVersion_args__isset {
  _ModuleTraitsService_setVersion_args__isset() : version(false) {}
  bool version;
} _ModuleTraitsService_setVersion_args__isset;

class ModuleTraitsService_setVersion_args {
 public:

  ModuleTraitsService_setVersion_args() : version() {
  }

  virtual ~ModuleTraitsService_setVersion_args() throw() {}

  std::string version;

  _ModuleTraitsService_setVersion_args__isset __isset;

  void __set_version(const std::string& val) {
    version = val;
  }

  bool operator == (const ModuleTraitsService_setVersion_args & rhs) const
  {
    if (!(version == rhs.version))
      return false;
    return true;
  }
  bool operator != (const ModuleTraitsService_setVersion_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_setVersion_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setVersion_pargs {
 public:


  virtual ~ModuleTraitsService_setVersion_pargs() throw() {}

  const std::string* version;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setVersion_result {
 public:

  ModuleTraitsService_setVersion_result() {
  }

  virtual ~ModuleTraitsService_setVersion_result() throw() {}


  bool operator == (const ModuleTraitsService_setVersion_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ModuleTraitsService_setVersion_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ModuleTraitsService_setVersion_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ModuleTraitsService_setVersion_presult {
 public:


  virtual ~ModuleTraitsService_setVersion_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

class ModuleTraitsServiceClient : virtual public ModuleTraitsServiceIf {
 public:
  ModuleTraitsServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) :
    piprot_(prot),
    poprot_(prot) {
    iprot_ = prot.get();
    oprot_ = prot.get();
  }
  ModuleTraitsServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, boost::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) :
    piprot_(iprot),
    poprot_(oprot) {
    iprot_ = iprot.get();
    oprot_ = oprot.get();
  }
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getInputProtocol() {
    return piprot_;
  }
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getOutputProtocol() {
    return poprot_;
  }
  void getSchema(std::string& _return);
  void send_getSchema();
  void recv_getSchema(std::string& _return);
  void getTrait(std::string& _return);
  void send_getTrait();
  void recv_getTrait(std::string& _return);
  void getVersion(std::string& _return);
  void send_getVersion();
  void recv_getVersion(std::string& _return);
  void setSchema(const std::string& schema);
  void send_setSchema(const std::string& schema);
  void recv_setSchema();
  void setTrait(const std::string& trait);
  void send_setTrait(const std::string& trait);
  void recv_setTrait();
  void setVersion(const std::string& version);
  void send_setVersion(const std::string& version);
  void recv_setVersion();
 protected:
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> piprot_;
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> poprot_;
  ::apache::thrift::protocol::TProtocol* iprot_;
  ::apache::thrift::protocol::TProtocol* oprot_;
};

class ModuleTraitsServiceProcessor : public ::apache::thrift::TDispatchProcessor {
 protected:
  boost::shared_ptr<ModuleTraitsServiceIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (ModuleTraitsServiceProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_getSchema(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getTrait(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getVersion(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setSchema(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setTrait(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setVersion(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  ModuleTraitsServiceProcessor(boost::shared_ptr<ModuleTraitsServiceIf> iface) :
    iface_(iface) {
    processMap_["getSchema"] = &ModuleTraitsServiceProcessor::process_getSchema;
    processMap_["getTrait"] = &ModuleTraitsServiceProcessor::process_getTrait;
    processMap_["getVersion"] = &ModuleTraitsServiceProcessor::process_getVersion;
    processMap_["setSchema"] = &ModuleTraitsServiceProcessor::process_setSchema;
    processMap_["setTrait"] = &ModuleTraitsServiceProcessor::process_setTrait;
    processMap_["setVersion"] = &ModuleTraitsServiceProcessor::process_setVersion;
  }

  virtual ~ModuleTraitsServiceProcessor() {}
};

class ModuleTraitsServiceProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  ModuleTraitsServiceProcessorFactory(const ::boost::shared_ptr< ModuleTraitsServiceIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::boost::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::boost::shared_ptr< ModuleTraitsServiceIfFactory > handlerFactory_;
};

class ModuleTraitsServiceMultiface : virtual public ModuleTraitsServiceIf {
 public:
  ModuleTraitsServiceMultiface(std::vector<boost::shared_ptr<ModuleTraitsServiceIf> >& ifaces) : ifaces_(ifaces) {
  }
  virtual ~ModuleTraitsServiceMultiface() {}
 protected:
  std::vector<boost::shared_ptr<ModuleTraitsServiceIf> > ifaces_;
  ModuleTraitsServiceMultiface() {}
  void add(boost::shared_ptr<ModuleTraitsServiceIf> iface) {
    ifaces_.push_back(iface);
  }
 public:
  void getSchema(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getSchema(_return);
    }
    ifaces_[i]->getSchema(_return);
    return;
  }

  void getTrait(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getTrait(_return);
    }
    ifaces_[i]->getTrait(_return);
    return;
  }

  void getVersion(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getVersion(_return);
    }
    ifaces_[i]->getVersion(_return);
    return;
  }

  void setSchema(const std::string& schema) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setSchema(schema);
    }
    ifaces_[i]->setSchema(schema);
  }

  void setTrait(const std::string& trait) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setTrait(trait);
    }
    ifaces_[i]->setTrait(trait);
  }

  void setVersion(const std::string& version) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setVersion(version);
    }
    ifaces_[i]->setVersion(version);
  }

};

}}} // namespace

#endif
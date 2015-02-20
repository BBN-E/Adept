/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef CoreferenceService_H
#define CoreferenceService_H

#include <thrift/TDispatchProcessor.h>
#include "common_types.h"
#include "ItemService.h"

namespace thrift { namespace adept { namespace common {

class CoreferenceServiceIf : virtual public ItemServiceIf {
 public:
  virtual ~CoreferenceServiceIf() {}
  virtual int64_t getCoreferenceId() = 0;
  virtual void getEntities(std::vector<Entity> & _return) = 0;
  virtual void getResolvedMentions(std::vector<EntityMention> & _return) = 0;
  virtual void setEntities(const std::vector<Entity> & entities) = 0;
  virtual void setResolvedMentions(const std::vector<EntityMention> & resolvedEntityMentions) = 0;
};

class CoreferenceServiceIfFactory : virtual public ItemServiceIfFactory {
 public:
  typedef CoreferenceServiceIf Handler;

  virtual ~CoreferenceServiceIfFactory() {}

  virtual CoreferenceServiceIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(ItemServiceIf* /* handler */) = 0;
};

class CoreferenceServiceIfSingletonFactory : virtual public CoreferenceServiceIfFactory {
 public:
  CoreferenceServiceIfSingletonFactory(const boost::shared_ptr<CoreferenceServiceIf>& iface) : iface_(iface) {}
  virtual ~CoreferenceServiceIfSingletonFactory() {}

  virtual CoreferenceServiceIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(ItemServiceIf* /* handler */) {}

 protected:
  boost::shared_ptr<CoreferenceServiceIf> iface_;
};

class CoreferenceServiceNull : virtual public CoreferenceServiceIf , virtual public ItemServiceNull {
 public:
  virtual ~CoreferenceServiceNull() {}
  int64_t getCoreferenceId() {
    int64_t _return = 0;
    return _return;
  }
  void getEntities(std::vector<Entity> & /* _return */) {
    return;
  }
  void getResolvedMentions(std::vector<EntityMention> & /* _return */) {
    return;
  }
  void setEntities(const std::vector<Entity> & /* entities */) {
    return;
  }
  void setResolvedMentions(const std::vector<EntityMention> & /* resolvedEntityMentions */) {
    return;
  }
};


class CoreferenceService_getCoreferenceId_args {
 public:

  CoreferenceService_getCoreferenceId_args() {
  }

  virtual ~CoreferenceService_getCoreferenceId_args() throw() {}


  bool operator == (const CoreferenceService_getCoreferenceId_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const CoreferenceService_getCoreferenceId_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_getCoreferenceId_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_getCoreferenceId_pargs {
 public:


  virtual ~CoreferenceService_getCoreferenceId_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _CoreferenceService_getCoreferenceId_result__isset {
  _CoreferenceService_getCoreferenceId_result__isset() : success(false) {}
  bool success;
} _CoreferenceService_getCoreferenceId_result__isset;

class CoreferenceService_getCoreferenceId_result {
 public:

  CoreferenceService_getCoreferenceId_result() : success(0) {
  }

  virtual ~CoreferenceService_getCoreferenceId_result() throw() {}

  int64_t success;

  _CoreferenceService_getCoreferenceId_result__isset __isset;

  void __set_success(const int64_t val) {
    success = val;
  }

  bool operator == (const CoreferenceService_getCoreferenceId_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const CoreferenceService_getCoreferenceId_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_getCoreferenceId_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _CoreferenceService_getCoreferenceId_presult__isset {
  _CoreferenceService_getCoreferenceId_presult__isset() : success(false) {}
  bool success;
} _CoreferenceService_getCoreferenceId_presult__isset;

class CoreferenceService_getCoreferenceId_presult {
 public:


  virtual ~CoreferenceService_getCoreferenceId_presult() throw() {}

  int64_t* success;

  _CoreferenceService_getCoreferenceId_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class CoreferenceService_getEntities_args {
 public:

  CoreferenceService_getEntities_args() {
  }

  virtual ~CoreferenceService_getEntities_args() throw() {}


  bool operator == (const CoreferenceService_getEntities_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const CoreferenceService_getEntities_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_getEntities_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_getEntities_pargs {
 public:


  virtual ~CoreferenceService_getEntities_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _CoreferenceService_getEntities_result__isset {
  _CoreferenceService_getEntities_result__isset() : success(false) {}
  bool success;
} _CoreferenceService_getEntities_result__isset;

class CoreferenceService_getEntities_result {
 public:

  CoreferenceService_getEntities_result() {
  }

  virtual ~CoreferenceService_getEntities_result() throw() {}

  std::vector<Entity>  success;

  _CoreferenceService_getEntities_result__isset __isset;

  void __set_success(const std::vector<Entity> & val) {
    success = val;
  }

  bool operator == (const CoreferenceService_getEntities_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const CoreferenceService_getEntities_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_getEntities_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _CoreferenceService_getEntities_presult__isset {
  _CoreferenceService_getEntities_presult__isset() : success(false) {}
  bool success;
} _CoreferenceService_getEntities_presult__isset;

class CoreferenceService_getEntities_presult {
 public:


  virtual ~CoreferenceService_getEntities_presult() throw() {}

  std::vector<Entity> * success;

  _CoreferenceService_getEntities_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class CoreferenceService_getResolvedMentions_args {
 public:

  CoreferenceService_getResolvedMentions_args() {
  }

  virtual ~CoreferenceService_getResolvedMentions_args() throw() {}


  bool operator == (const CoreferenceService_getResolvedMentions_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const CoreferenceService_getResolvedMentions_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_getResolvedMentions_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_getResolvedMentions_pargs {
 public:


  virtual ~CoreferenceService_getResolvedMentions_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _CoreferenceService_getResolvedMentions_result__isset {
  _CoreferenceService_getResolvedMentions_result__isset() : success(false) {}
  bool success;
} _CoreferenceService_getResolvedMentions_result__isset;

class CoreferenceService_getResolvedMentions_result {
 public:

  CoreferenceService_getResolvedMentions_result() {
  }

  virtual ~CoreferenceService_getResolvedMentions_result() throw() {}

  std::vector<EntityMention>  success;

  _CoreferenceService_getResolvedMentions_result__isset __isset;

  void __set_success(const std::vector<EntityMention> & val) {
    success = val;
  }

  bool operator == (const CoreferenceService_getResolvedMentions_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const CoreferenceService_getResolvedMentions_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_getResolvedMentions_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _CoreferenceService_getResolvedMentions_presult__isset {
  _CoreferenceService_getResolvedMentions_presult__isset() : success(false) {}
  bool success;
} _CoreferenceService_getResolvedMentions_presult__isset;

class CoreferenceService_getResolvedMentions_presult {
 public:


  virtual ~CoreferenceService_getResolvedMentions_presult() throw() {}

  std::vector<EntityMention> * success;

  _CoreferenceService_getResolvedMentions_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _CoreferenceService_setEntities_args__isset {
  _CoreferenceService_setEntities_args__isset() : entities(false) {}
  bool entities;
} _CoreferenceService_setEntities_args__isset;

class CoreferenceService_setEntities_args {
 public:

  CoreferenceService_setEntities_args() {
  }

  virtual ~CoreferenceService_setEntities_args() throw() {}

  std::vector<Entity>  entities;

  _CoreferenceService_setEntities_args__isset __isset;

  void __set_entities(const std::vector<Entity> & val) {
    entities = val;
  }

  bool operator == (const CoreferenceService_setEntities_args & rhs) const
  {
    if (!(entities == rhs.entities))
      return false;
    return true;
  }
  bool operator != (const CoreferenceService_setEntities_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_setEntities_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_setEntities_pargs {
 public:


  virtual ~CoreferenceService_setEntities_pargs() throw() {}

  const std::vector<Entity> * entities;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_setEntities_result {
 public:

  CoreferenceService_setEntities_result() {
  }

  virtual ~CoreferenceService_setEntities_result() throw() {}


  bool operator == (const CoreferenceService_setEntities_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const CoreferenceService_setEntities_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_setEntities_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_setEntities_presult {
 public:


  virtual ~CoreferenceService_setEntities_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _CoreferenceService_setResolvedMentions_args__isset {
  _CoreferenceService_setResolvedMentions_args__isset() : resolvedEntityMentions(false) {}
  bool resolvedEntityMentions;
} _CoreferenceService_setResolvedMentions_args__isset;

class CoreferenceService_setResolvedMentions_args {
 public:

  CoreferenceService_setResolvedMentions_args() {
  }

  virtual ~CoreferenceService_setResolvedMentions_args() throw() {}

  std::vector<EntityMention>  resolvedEntityMentions;

  _CoreferenceService_setResolvedMentions_args__isset __isset;

  void __set_resolvedEntityMentions(const std::vector<EntityMention> & val) {
    resolvedEntityMentions = val;
  }

  bool operator == (const CoreferenceService_setResolvedMentions_args & rhs) const
  {
    if (!(resolvedEntityMentions == rhs.resolvedEntityMentions))
      return false;
    return true;
  }
  bool operator != (const CoreferenceService_setResolvedMentions_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_setResolvedMentions_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_setResolvedMentions_pargs {
 public:


  virtual ~CoreferenceService_setResolvedMentions_pargs() throw() {}

  const std::vector<EntityMention> * resolvedEntityMentions;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_setResolvedMentions_result {
 public:

  CoreferenceService_setResolvedMentions_result() {
  }

  virtual ~CoreferenceService_setResolvedMentions_result() throw() {}


  bool operator == (const CoreferenceService_setResolvedMentions_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const CoreferenceService_setResolvedMentions_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const CoreferenceService_setResolvedMentions_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class CoreferenceService_setResolvedMentions_presult {
 public:


  virtual ~CoreferenceService_setResolvedMentions_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

class CoreferenceServiceClient : virtual public CoreferenceServiceIf, public ItemServiceClient {
 public:
  CoreferenceServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) :
    ItemServiceClient(prot, prot) {}
  CoreferenceServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, boost::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) :
    ItemServiceClient(iprot, oprot) {}
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getInputProtocol() {
    return piprot_;
  }
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getOutputProtocol() {
    return poprot_;
  }
  int64_t getCoreferenceId();
  void send_getCoreferenceId();
  int64_t recv_getCoreferenceId();
  void getEntities(std::vector<Entity> & _return);
  void send_getEntities();
  void recv_getEntities(std::vector<Entity> & _return);
  void getResolvedMentions(std::vector<EntityMention> & _return);
  void send_getResolvedMentions();
  void recv_getResolvedMentions(std::vector<EntityMention> & _return);
  void setEntities(const std::vector<Entity> & entities);
  void send_setEntities(const std::vector<Entity> & entities);
  void recv_setEntities();
  void setResolvedMentions(const std::vector<EntityMention> & resolvedEntityMentions);
  void send_setResolvedMentions(const std::vector<EntityMention> & resolvedEntityMentions);
  void recv_setResolvedMentions();
};

class CoreferenceServiceProcessor : public ItemServiceProcessor {
 protected:
  boost::shared_ptr<CoreferenceServiceIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (CoreferenceServiceProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_getCoreferenceId(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getEntities(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getResolvedMentions(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setEntities(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setResolvedMentions(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  CoreferenceServiceProcessor(boost::shared_ptr<CoreferenceServiceIf> iface) :
    ItemServiceProcessor(iface),
    iface_(iface) {
    processMap_["getCoreferenceId"] = &CoreferenceServiceProcessor::process_getCoreferenceId;
    processMap_["getEntities"] = &CoreferenceServiceProcessor::process_getEntities;
    processMap_["getResolvedMentions"] = &CoreferenceServiceProcessor::process_getResolvedMentions;
    processMap_["setEntities"] = &CoreferenceServiceProcessor::process_setEntities;
    processMap_["setResolvedMentions"] = &CoreferenceServiceProcessor::process_setResolvedMentions;
  }

  virtual ~CoreferenceServiceProcessor() {}
};

class CoreferenceServiceProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  CoreferenceServiceProcessorFactory(const ::boost::shared_ptr< CoreferenceServiceIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::boost::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::boost::shared_ptr< CoreferenceServiceIfFactory > handlerFactory_;
};

class CoreferenceServiceMultiface : virtual public CoreferenceServiceIf, public ItemServiceMultiface {
 public:
  CoreferenceServiceMultiface(std::vector<boost::shared_ptr<CoreferenceServiceIf> >& ifaces) : ifaces_(ifaces) {
    std::vector<boost::shared_ptr<CoreferenceServiceIf> >::iterator iter;
    for (iter = ifaces.begin(); iter != ifaces.end(); ++iter) {
      ItemServiceMultiface::add(*iter);
    }
  }
  virtual ~CoreferenceServiceMultiface() {}
 protected:
  std::vector<boost::shared_ptr<CoreferenceServiceIf> > ifaces_;
  CoreferenceServiceMultiface() {}
  void add(boost::shared_ptr<CoreferenceServiceIf> iface) {
    ItemServiceMultiface::add(iface);
    ifaces_.push_back(iface);
  }
 public:
  int64_t getCoreferenceId() {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getCoreferenceId();
    }
    return ifaces_[i]->getCoreferenceId();
  }

  void getEntities(std::vector<Entity> & _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getEntities(_return);
    }
    ifaces_[i]->getEntities(_return);
    return;
  }

  void getResolvedMentions(std::vector<EntityMention> & _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getResolvedMentions(_return);
    }
    ifaces_[i]->getResolvedMentions(_return);
    return;
  }

  void setEntities(const std::vector<Entity> & entities) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setEntities(entities);
    }
    ifaces_[i]->setEntities(entities);
  }

  void setResolvedMentions(const std::vector<EntityMention> & resolvedEntityMentions) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setResolvedMentions(resolvedEntityMentions);
    }
    ifaces_[i]->setResolvedMentions(resolvedEntityMentions);
  }

};

}}} // namespace

#endif
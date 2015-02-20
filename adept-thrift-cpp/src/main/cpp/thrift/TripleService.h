/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef TripleService_H
#define TripleService_H

#include <thrift/TDispatchProcessor.h>
#include "common_types.h"

namespace thrift { namespace adept { namespace common {

class TripleServiceIf {
 public:
  virtual ~TripleServiceIf() {}
  virtual void getEntity(Entity& _return) = 0;
  virtual void getSlot(Slot& _return) = 0;
  virtual void getValue(std::string& _return) = 0;
};

class TripleServiceIfFactory {
 public:
  typedef TripleServiceIf Handler;

  virtual ~TripleServiceIfFactory() {}

  virtual TripleServiceIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(TripleServiceIf* /* handler */) = 0;
};

class TripleServiceIfSingletonFactory : virtual public TripleServiceIfFactory {
 public:
  TripleServiceIfSingletonFactory(const boost::shared_ptr<TripleServiceIf>& iface) : iface_(iface) {}
  virtual ~TripleServiceIfSingletonFactory() {}

  virtual TripleServiceIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(TripleServiceIf* /* handler */) {}

 protected:
  boost::shared_ptr<TripleServiceIf> iface_;
};

class TripleServiceNull : virtual public TripleServiceIf {
 public:
  virtual ~TripleServiceNull() {}
  void getEntity(Entity& /* _return */) {
    return;
  }
  void getSlot(Slot& /* _return */) {
    return;
  }
  void getValue(std::string& /* _return */) {
    return;
  }
};


class TripleService_getEntity_args {
 public:

  TripleService_getEntity_args() {
  }

  virtual ~TripleService_getEntity_args() throw() {}


  bool operator == (const TripleService_getEntity_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const TripleService_getEntity_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TripleService_getEntity_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class TripleService_getEntity_pargs {
 public:


  virtual ~TripleService_getEntity_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TripleService_getEntity_result__isset {
  _TripleService_getEntity_result__isset() : success(false) {}
  bool success;
} _TripleService_getEntity_result__isset;

class TripleService_getEntity_result {
 public:

  TripleService_getEntity_result() {
  }

  virtual ~TripleService_getEntity_result() throw() {}

  Entity success;

  _TripleService_getEntity_result__isset __isset;

  void __set_success(const Entity& val) {
    success = val;
  }

  bool operator == (const TripleService_getEntity_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const TripleService_getEntity_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TripleService_getEntity_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TripleService_getEntity_presult__isset {
  _TripleService_getEntity_presult__isset() : success(false) {}
  bool success;
} _TripleService_getEntity_presult__isset;

class TripleService_getEntity_presult {
 public:


  virtual ~TripleService_getEntity_presult() throw() {}

  Entity* success;

  _TripleService_getEntity_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class TripleService_getSlot_args {
 public:

  TripleService_getSlot_args() {
  }

  virtual ~TripleService_getSlot_args() throw() {}


  bool operator == (const TripleService_getSlot_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const TripleService_getSlot_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TripleService_getSlot_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class TripleService_getSlot_pargs {
 public:


  virtual ~TripleService_getSlot_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TripleService_getSlot_result__isset {
  _TripleService_getSlot_result__isset() : success(false) {}
  bool success;
} _TripleService_getSlot_result__isset;

class TripleService_getSlot_result {
 public:

  TripleService_getSlot_result() {
  }

  virtual ~TripleService_getSlot_result() throw() {}

  Slot success;

  _TripleService_getSlot_result__isset __isset;

  void __set_success(const Slot& val) {
    success = val;
  }

  bool operator == (const TripleService_getSlot_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const TripleService_getSlot_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TripleService_getSlot_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TripleService_getSlot_presult__isset {
  _TripleService_getSlot_presult__isset() : success(false) {}
  bool success;
} _TripleService_getSlot_presult__isset;

class TripleService_getSlot_presult {
 public:


  virtual ~TripleService_getSlot_presult() throw() {}

  Slot* success;

  _TripleService_getSlot_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class TripleService_getValue_args {
 public:

  TripleService_getValue_args() {
  }

  virtual ~TripleService_getValue_args() throw() {}


  bool operator == (const TripleService_getValue_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const TripleService_getValue_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TripleService_getValue_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class TripleService_getValue_pargs {
 public:


  virtual ~TripleService_getValue_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TripleService_getValue_result__isset {
  _TripleService_getValue_result__isset() : success(false) {}
  bool success;
} _TripleService_getValue_result__isset;

class TripleService_getValue_result {
 public:

  TripleService_getValue_result() : success() {
  }

  virtual ~TripleService_getValue_result() throw() {}

  std::string success;

  _TripleService_getValue_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const TripleService_getValue_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const TripleService_getValue_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TripleService_getValue_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TripleService_getValue_presult__isset {
  _TripleService_getValue_presult__isset() : success(false) {}
  bool success;
} _TripleService_getValue_presult__isset;

class TripleService_getValue_presult {
 public:


  virtual ~TripleService_getValue_presult() throw() {}

  std::string* success;

  _TripleService_getValue_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

class TripleServiceClient : virtual public TripleServiceIf {
 public:
  TripleServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) :
    piprot_(prot),
    poprot_(prot) {
    iprot_ = prot.get();
    oprot_ = prot.get();
  }
  TripleServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, boost::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) :
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
  void getEntity(Entity& _return);
  void send_getEntity();
  void recv_getEntity(Entity& _return);
  void getSlot(Slot& _return);
  void send_getSlot();
  void recv_getSlot(Slot& _return);
  void getValue(std::string& _return);
  void send_getValue();
  void recv_getValue(std::string& _return);
 protected:
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> piprot_;
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> poprot_;
  ::apache::thrift::protocol::TProtocol* iprot_;
  ::apache::thrift::protocol::TProtocol* oprot_;
};

class TripleServiceProcessor : public ::apache::thrift::TDispatchProcessor {
 protected:
  boost::shared_ptr<TripleServiceIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (TripleServiceProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_getEntity(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getSlot(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getValue(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  TripleServiceProcessor(boost::shared_ptr<TripleServiceIf> iface) :
    iface_(iface) {
    processMap_["getEntity"] = &TripleServiceProcessor::process_getEntity;
    processMap_["getSlot"] = &TripleServiceProcessor::process_getSlot;
    processMap_["getValue"] = &TripleServiceProcessor::process_getValue;
  }

  virtual ~TripleServiceProcessor() {}
};

class TripleServiceProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  TripleServiceProcessorFactory(const ::boost::shared_ptr< TripleServiceIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::boost::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::boost::shared_ptr< TripleServiceIfFactory > handlerFactory_;
};

class TripleServiceMultiface : virtual public TripleServiceIf {
 public:
  TripleServiceMultiface(std::vector<boost::shared_ptr<TripleServiceIf> >& ifaces) : ifaces_(ifaces) {
  }
  virtual ~TripleServiceMultiface() {}
 protected:
  std::vector<boost::shared_ptr<TripleServiceIf> > ifaces_;
  TripleServiceMultiface() {}
  void add(boost::shared_ptr<TripleServiceIf> iface) {
    ifaces_.push_back(iface);
  }
 public:
  void getEntity(Entity& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getEntity(_return);
    }
    ifaces_[i]->getEntity(_return);
    return;
  }

  void getSlot(Slot& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getSlot(_return);
    }
    ifaces_[i]->getSlot(_return);
    return;
  }

  void getValue(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getValue(_return);
    }
    ifaces_[i]->getValue(_return);
    return;
  }

};

}}} // namespace

#endif
/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef TypeService_H
#define TypeService_H

#include <thrift/TDispatchProcessor.h>
#include "common_types.h"

namespace thrift { namespace adept { namespace common {

class TypeServiceIf {
 public:
  virtual ~TypeServiceIf() {}
  virtual void getType(std::string& _return) = 0;
};

class TypeServiceIfFactory {
 public:
  typedef TypeServiceIf Handler;

  virtual ~TypeServiceIfFactory() {}

  virtual TypeServiceIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(TypeServiceIf* /* handler */) = 0;
};

class TypeServiceIfSingletonFactory : virtual public TypeServiceIfFactory {
 public:
  TypeServiceIfSingletonFactory(const boost::shared_ptr<TypeServiceIf>& iface) : iface_(iface) {}
  virtual ~TypeServiceIfSingletonFactory() {}

  virtual TypeServiceIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(TypeServiceIf* /* handler */) {}

 protected:
  boost::shared_ptr<TypeServiceIf> iface_;
};

class TypeServiceNull : virtual public TypeServiceIf {
 public:
  virtual ~TypeServiceNull() {}
  void getType(std::string& /* _return */) {
    return;
  }
};


class TypeService_getType_args {
 public:

  TypeService_getType_args() {
  }

  virtual ~TypeService_getType_args() throw() {}


  bool operator == (const TypeService_getType_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const TypeService_getType_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TypeService_getType_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class TypeService_getType_pargs {
 public:


  virtual ~TypeService_getType_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TypeService_getType_result__isset {
  _TypeService_getType_result__isset() : success(false) {}
  bool success;
} _TypeService_getType_result__isset;

class TypeService_getType_result {
 public:

  TypeService_getType_result() : success() {
  }

  virtual ~TypeService_getType_result() throw() {}

  std::string success;

  _TypeService_getType_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const TypeService_getType_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const TypeService_getType_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const TypeService_getType_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _TypeService_getType_presult__isset {
  _TypeService_getType_presult__isset() : success(false) {}
  bool success;
} _TypeService_getType_presult__isset;

class TypeService_getType_presult {
 public:


  virtual ~TypeService_getType_presult() throw() {}

  std::string* success;

  _TypeService_getType_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

class TypeServiceClient : virtual public TypeServiceIf {
 public:
  TypeServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) :
    piprot_(prot),
    poprot_(prot) {
    iprot_ = prot.get();
    oprot_ = prot.get();
  }
  TypeServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, boost::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) :
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
  void getType(std::string& _return);
  void send_getType();
  void recv_getType(std::string& _return);
 protected:
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> piprot_;
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> poprot_;
  ::apache::thrift::protocol::TProtocol* iprot_;
  ::apache::thrift::protocol::TProtocol* oprot_;
};

class TypeServiceProcessor : public ::apache::thrift::TDispatchProcessor {
 protected:
  boost::shared_ptr<TypeServiceIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (TypeServiceProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_getType(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  TypeServiceProcessor(boost::shared_ptr<TypeServiceIf> iface) :
    iface_(iface) {
    processMap_["getType"] = &TypeServiceProcessor::process_getType;
  }

  virtual ~TypeServiceProcessor() {}
};

class TypeServiceProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  TypeServiceProcessorFactory(const ::boost::shared_ptr< TypeServiceIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::boost::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::boost::shared_ptr< TypeServiceIfFactory > handlerFactory_;
};

class TypeServiceMultiface : virtual public TypeServiceIf {
 public:
  TypeServiceMultiface(std::vector<boost::shared_ptr<TypeServiceIf> >& ifaces) : ifaces_(ifaces) {
  }
  virtual ~TypeServiceMultiface() {}
 protected:
  std::vector<boost::shared_ptr<TypeServiceIf> > ifaces_;
  TypeServiceMultiface() {}
  void add(boost::shared_ptr<TypeServiceIf> iface) {
    ifaces_.push_back(iface);
  }
 public:
  void getType(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getType(_return);
    }
    ifaces_[i]->getType(_return);
    return;
  }

};

}}} // namespace

#endif
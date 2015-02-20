/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef DocumentListService_H
#define DocumentListService_H

#include <thrift/TDispatchProcessor.h>
#include "common_types.h"

namespace thrift { namespace adept { namespace common {

class DocumentListServiceIf {
 public:
  virtual ~DocumentListServiceIf() {}
  virtual void getId(std::string& _return) = 0;
  virtual void getIdString(std::string& _return) = 0;
};

class DocumentListServiceIfFactory {
 public:
  typedef DocumentListServiceIf Handler;

  virtual ~DocumentListServiceIfFactory() {}

  virtual DocumentListServiceIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(DocumentListServiceIf* /* handler */) = 0;
};

class DocumentListServiceIfSingletonFactory : virtual public DocumentListServiceIfFactory {
 public:
  DocumentListServiceIfSingletonFactory(const boost::shared_ptr<DocumentListServiceIf>& iface) : iface_(iface) {}
  virtual ~DocumentListServiceIfSingletonFactory() {}

  virtual DocumentListServiceIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(DocumentListServiceIf* /* handler */) {}

 protected:
  boost::shared_ptr<DocumentListServiceIf> iface_;
};

class DocumentListServiceNull : virtual public DocumentListServiceIf {
 public:
  virtual ~DocumentListServiceNull() {}
  void getId(std::string& /* _return */) {
    return;
  }
  void getIdString(std::string& /* _return */) {
    return;
  }
};


class DocumentListService_getId_args {
 public:

  DocumentListService_getId_args() {
  }

  virtual ~DocumentListService_getId_args() throw() {}


  bool operator == (const DocumentListService_getId_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const DocumentListService_getId_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const DocumentListService_getId_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class DocumentListService_getId_pargs {
 public:


  virtual ~DocumentListService_getId_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _DocumentListService_getId_result__isset {
  _DocumentListService_getId_result__isset() : success(false) {}
  bool success;
} _DocumentListService_getId_result__isset;

class DocumentListService_getId_result {
 public:

  DocumentListService_getId_result() : success() {
  }

  virtual ~DocumentListService_getId_result() throw() {}

  std::string success;

  _DocumentListService_getId_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const DocumentListService_getId_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const DocumentListService_getId_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const DocumentListService_getId_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _DocumentListService_getId_presult__isset {
  _DocumentListService_getId_presult__isset() : success(false) {}
  bool success;
} _DocumentListService_getId_presult__isset;

class DocumentListService_getId_presult {
 public:


  virtual ~DocumentListService_getId_presult() throw() {}

  std::string* success;

  _DocumentListService_getId_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class DocumentListService_getIdString_args {
 public:

  DocumentListService_getIdString_args() {
  }

  virtual ~DocumentListService_getIdString_args() throw() {}


  bool operator == (const DocumentListService_getIdString_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const DocumentListService_getIdString_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const DocumentListService_getIdString_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class DocumentListService_getIdString_pargs {
 public:


  virtual ~DocumentListService_getIdString_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _DocumentListService_getIdString_result__isset {
  _DocumentListService_getIdString_result__isset() : success(false) {}
  bool success;
} _DocumentListService_getIdString_result__isset;

class DocumentListService_getIdString_result {
 public:

  DocumentListService_getIdString_result() : success() {
  }

  virtual ~DocumentListService_getIdString_result() throw() {}

  std::string success;

  _DocumentListService_getIdString_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const DocumentListService_getIdString_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const DocumentListService_getIdString_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const DocumentListService_getIdString_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _DocumentListService_getIdString_presult__isset {
  _DocumentListService_getIdString_presult__isset() : success(false) {}
  bool success;
} _DocumentListService_getIdString_presult__isset;

class DocumentListService_getIdString_presult {
 public:


  virtual ~DocumentListService_getIdString_presult() throw() {}

  std::string* success;

  _DocumentListService_getIdString_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

class DocumentListServiceClient : virtual public DocumentListServiceIf {
 public:
  DocumentListServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) :
    piprot_(prot),
    poprot_(prot) {
    iprot_ = prot.get();
    oprot_ = prot.get();
  }
  DocumentListServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, boost::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) :
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
  void getId(std::string& _return);
  void send_getId();
  void recv_getId(std::string& _return);
  void getIdString(std::string& _return);
  void send_getIdString();
  void recv_getIdString(std::string& _return);
 protected:
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> piprot_;
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> poprot_;
  ::apache::thrift::protocol::TProtocol* iprot_;
  ::apache::thrift::protocol::TProtocol* oprot_;
};

class DocumentListServiceProcessor : public ::apache::thrift::TDispatchProcessor {
 protected:
  boost::shared_ptr<DocumentListServiceIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (DocumentListServiceProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_getId(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getIdString(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  DocumentListServiceProcessor(boost::shared_ptr<DocumentListServiceIf> iface) :
    iface_(iface) {
    processMap_["getId"] = &DocumentListServiceProcessor::process_getId;
    processMap_["getIdString"] = &DocumentListServiceProcessor::process_getIdString;
  }

  virtual ~DocumentListServiceProcessor() {}
};

class DocumentListServiceProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  DocumentListServiceProcessorFactory(const ::boost::shared_ptr< DocumentListServiceIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::boost::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::boost::shared_ptr< DocumentListServiceIfFactory > handlerFactory_;
};

class DocumentListServiceMultiface : virtual public DocumentListServiceIf {
 public:
  DocumentListServiceMultiface(std::vector<boost::shared_ptr<DocumentListServiceIf> >& ifaces) : ifaces_(ifaces) {
  }
  virtual ~DocumentListServiceMultiface() {}
 protected:
  std::vector<boost::shared_ptr<DocumentListServiceIf> > ifaces_;
  DocumentListServiceMultiface() {}
  void add(boost::shared_ptr<DocumentListServiceIf> iface) {
    ifaces_.push_back(iface);
  }
 public:
  void getId(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getId(_return);
    }
    ifaces_[i]->getId(_return);
    return;
  }

  void getIdString(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getIdString(_return);
    }
    ifaces_[i]->getIdString(_return);
    return;
  }

};

}}} // namespace

#endif
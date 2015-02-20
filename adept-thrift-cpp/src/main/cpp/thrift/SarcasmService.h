/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef SarcasmService_H
#define SarcasmService_H

#include <thrift/TDispatchProcessor.h>
#include "common_types.h"
#include "ChunkService.h"

namespace thrift { namespace adept { namespace common {

class SarcasmServiceIf : virtual public ChunkServiceIf {
 public:
  virtual ~SarcasmServiceIf() {}
  virtual double getConfidence() = 0;
  virtual SarcasmJudgment::type getJudgment() = 0;
  virtual int64_t getSarcasmId() = 0;
  virtual void setConfidence(const double confidence) = 0;
};

class SarcasmServiceIfFactory : virtual public ChunkServiceIfFactory {
 public:
  typedef SarcasmServiceIf Handler;

  virtual ~SarcasmServiceIfFactory() {}

  virtual SarcasmServiceIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(ItemServiceIf* /* handler */) = 0;
};

class SarcasmServiceIfSingletonFactory : virtual public SarcasmServiceIfFactory {
 public:
  SarcasmServiceIfSingletonFactory(const boost::shared_ptr<SarcasmServiceIf>& iface) : iface_(iface) {}
  virtual ~SarcasmServiceIfSingletonFactory() {}

  virtual SarcasmServiceIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(ItemServiceIf* /* handler */) {}

 protected:
  boost::shared_ptr<SarcasmServiceIf> iface_;
};

class SarcasmServiceNull : virtual public SarcasmServiceIf , virtual public ChunkServiceNull {
 public:
  virtual ~SarcasmServiceNull() {}
  double getConfidence() {
    double _return = (double)0;
    return _return;
  }
  SarcasmJudgment::type getJudgment() {
    SarcasmJudgment::type _return = (SarcasmJudgment::type)0;
    return _return;
  }
  int64_t getSarcasmId() {
    int64_t _return = 0;
    return _return;
  }
  void setConfidence(const double /* confidence */) {
    return;
  }
};


class SarcasmService_getConfidence_args {
 public:

  SarcasmService_getConfidence_args() {
  }

  virtual ~SarcasmService_getConfidence_args() throw() {}


  bool operator == (const SarcasmService_getConfidence_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const SarcasmService_getConfidence_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_getConfidence_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class SarcasmService_getConfidence_pargs {
 public:


  virtual ~SarcasmService_getConfidence_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _SarcasmService_getConfidence_result__isset {
  _SarcasmService_getConfidence_result__isset() : success(false) {}
  bool success;
} _SarcasmService_getConfidence_result__isset;

class SarcasmService_getConfidence_result {
 public:

  SarcasmService_getConfidence_result() : success(0) {
  }

  virtual ~SarcasmService_getConfidence_result() throw() {}

  double success;

  _SarcasmService_getConfidence_result__isset __isset;

  void __set_success(const double val) {
    success = val;
  }

  bool operator == (const SarcasmService_getConfidence_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const SarcasmService_getConfidence_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_getConfidence_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _SarcasmService_getConfidence_presult__isset {
  _SarcasmService_getConfidence_presult__isset() : success(false) {}
  bool success;
} _SarcasmService_getConfidence_presult__isset;

class SarcasmService_getConfidence_presult {
 public:


  virtual ~SarcasmService_getConfidence_presult() throw() {}

  double* success;

  _SarcasmService_getConfidence_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class SarcasmService_getJudgment_args {
 public:

  SarcasmService_getJudgment_args() {
  }

  virtual ~SarcasmService_getJudgment_args() throw() {}


  bool operator == (const SarcasmService_getJudgment_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const SarcasmService_getJudgment_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_getJudgment_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class SarcasmService_getJudgment_pargs {
 public:


  virtual ~SarcasmService_getJudgment_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _SarcasmService_getJudgment_result__isset {
  _SarcasmService_getJudgment_result__isset() : success(false) {}
  bool success;
} _SarcasmService_getJudgment_result__isset;

class SarcasmService_getJudgment_result {
 public:

  SarcasmService_getJudgment_result() : success((SarcasmJudgment::type)0) {
  }

  virtual ~SarcasmService_getJudgment_result() throw() {}

  SarcasmJudgment::type success;

  _SarcasmService_getJudgment_result__isset __isset;

  void __set_success(const SarcasmJudgment::type val) {
    success = val;
  }

  bool operator == (const SarcasmService_getJudgment_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const SarcasmService_getJudgment_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_getJudgment_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _SarcasmService_getJudgment_presult__isset {
  _SarcasmService_getJudgment_presult__isset() : success(false) {}
  bool success;
} _SarcasmService_getJudgment_presult__isset;

class SarcasmService_getJudgment_presult {
 public:


  virtual ~SarcasmService_getJudgment_presult() throw() {}

  SarcasmJudgment::type* success;

  _SarcasmService_getJudgment_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class SarcasmService_getSarcasmId_args {
 public:

  SarcasmService_getSarcasmId_args() {
  }

  virtual ~SarcasmService_getSarcasmId_args() throw() {}


  bool operator == (const SarcasmService_getSarcasmId_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const SarcasmService_getSarcasmId_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_getSarcasmId_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class SarcasmService_getSarcasmId_pargs {
 public:


  virtual ~SarcasmService_getSarcasmId_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _SarcasmService_getSarcasmId_result__isset {
  _SarcasmService_getSarcasmId_result__isset() : success(false) {}
  bool success;
} _SarcasmService_getSarcasmId_result__isset;

class SarcasmService_getSarcasmId_result {
 public:

  SarcasmService_getSarcasmId_result() : success(0) {
  }

  virtual ~SarcasmService_getSarcasmId_result() throw() {}

  int64_t success;

  _SarcasmService_getSarcasmId_result__isset __isset;

  void __set_success(const int64_t val) {
    success = val;
  }

  bool operator == (const SarcasmService_getSarcasmId_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const SarcasmService_getSarcasmId_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_getSarcasmId_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _SarcasmService_getSarcasmId_presult__isset {
  _SarcasmService_getSarcasmId_presult__isset() : success(false) {}
  bool success;
} _SarcasmService_getSarcasmId_presult__isset;

class SarcasmService_getSarcasmId_presult {
 public:


  virtual ~SarcasmService_getSarcasmId_presult() throw() {}

  int64_t* success;

  _SarcasmService_getSarcasmId_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _SarcasmService_setConfidence_args__isset {
  _SarcasmService_setConfidence_args__isset() : confidence(false) {}
  bool confidence;
} _SarcasmService_setConfidence_args__isset;

class SarcasmService_setConfidence_args {
 public:

  SarcasmService_setConfidence_args() : confidence(0) {
  }

  virtual ~SarcasmService_setConfidence_args() throw() {}

  double confidence;

  _SarcasmService_setConfidence_args__isset __isset;

  void __set_confidence(const double val) {
    confidence = val;
  }

  bool operator == (const SarcasmService_setConfidence_args & rhs) const
  {
    if (!(confidence == rhs.confidence))
      return false;
    return true;
  }
  bool operator != (const SarcasmService_setConfidence_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_setConfidence_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class SarcasmService_setConfidence_pargs {
 public:


  virtual ~SarcasmService_setConfidence_pargs() throw() {}

  const double* confidence;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class SarcasmService_setConfidence_result {
 public:

  SarcasmService_setConfidence_result() {
  }

  virtual ~SarcasmService_setConfidence_result() throw() {}


  bool operator == (const SarcasmService_setConfidence_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const SarcasmService_setConfidence_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const SarcasmService_setConfidence_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class SarcasmService_setConfidence_presult {
 public:


  virtual ~SarcasmService_setConfidence_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

class SarcasmServiceClient : virtual public SarcasmServiceIf, public ChunkServiceClient {
 public:
  SarcasmServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) :
    ChunkServiceClient(prot, prot) {}
  SarcasmServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, boost::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) :
    ChunkServiceClient(iprot, oprot) {}
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getInputProtocol() {
    return piprot_;
  }
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getOutputProtocol() {
    return poprot_;
  }
  double getConfidence();
  void send_getConfidence();
  double recv_getConfidence();
  SarcasmJudgment::type getJudgment();
  void send_getJudgment();
  SarcasmJudgment::type recv_getJudgment();
  int64_t getSarcasmId();
  void send_getSarcasmId();
  int64_t recv_getSarcasmId();
  void setConfidence(const double confidence);
  void send_setConfidence(const double confidence);
  void recv_setConfidence();
};

class SarcasmServiceProcessor : public ChunkServiceProcessor {
 protected:
  boost::shared_ptr<SarcasmServiceIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (SarcasmServiceProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_getConfidence(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getJudgment(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getSarcasmId(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setConfidence(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  SarcasmServiceProcessor(boost::shared_ptr<SarcasmServiceIf> iface) :
    ChunkServiceProcessor(iface),
    iface_(iface) {
    processMap_["getConfidence"] = &SarcasmServiceProcessor::process_getConfidence;
    processMap_["getJudgment"] = &SarcasmServiceProcessor::process_getJudgment;
    processMap_["getSarcasmId"] = &SarcasmServiceProcessor::process_getSarcasmId;
    processMap_["setConfidence"] = &SarcasmServiceProcessor::process_setConfidence;
  }

  virtual ~SarcasmServiceProcessor() {}
};

class SarcasmServiceProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  SarcasmServiceProcessorFactory(const ::boost::shared_ptr< SarcasmServiceIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::boost::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::boost::shared_ptr< SarcasmServiceIfFactory > handlerFactory_;
};

class SarcasmServiceMultiface : virtual public SarcasmServiceIf, public ChunkServiceMultiface {
 public:
  SarcasmServiceMultiface(std::vector<boost::shared_ptr<SarcasmServiceIf> >& ifaces) : ifaces_(ifaces) {
    std::vector<boost::shared_ptr<SarcasmServiceIf> >::iterator iter;
    for (iter = ifaces.begin(); iter != ifaces.end(); ++iter) {
      ChunkServiceMultiface::add(*iter);
    }
  }
  virtual ~SarcasmServiceMultiface() {}
 protected:
  std::vector<boost::shared_ptr<SarcasmServiceIf> > ifaces_;
  SarcasmServiceMultiface() {}
  void add(boost::shared_ptr<SarcasmServiceIf> iface) {
    ChunkServiceMultiface::add(iface);
    ifaces_.push_back(iface);
  }
 public:
  double getConfidence() {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getConfidence();
    }
    return ifaces_[i]->getConfidence();
  }

  SarcasmJudgment::type getJudgment() {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getJudgment();
    }
    return ifaces_[i]->getJudgment();
  }

  int64_t getSarcasmId() {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getSarcasmId();
    }
    return ifaces_[i]->getSarcasmId();
  }

  void setConfidence(const double confidence) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setConfidence(confidence);
    }
    ifaces_[i]->setConfidence(confidence);
  }

};

}}} // namespace

#endif
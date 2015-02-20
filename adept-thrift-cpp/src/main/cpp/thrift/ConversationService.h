/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#ifndef ConversationService_H
#define ConversationService_H

#include <thrift/TDispatchProcessor.h>
#include "common_types.h"
#include "ItemService.h"

namespace thrift { namespace adept { namespace common {

class ConversationServiceIf : virtual public ItemServiceIf {
 public:
  virtual ~ConversationServiceIf() {}
  virtual void addTopic(const Topic& topic) = 0;
  virtual void addUtterance(const Utterance& utterance) = 0;
  virtual int64_t getConversationId() = 0;
  virtual void getName(std::string& _return) = 0;
  virtual void getTopics(std::vector<Topic> & _return) = 0;
  virtual void getUtterances(std::vector<Utterance> & _return) = 0;
  virtual bool isOneSided() = 0;
  virtual void setOneSided(const bool oneSided) = 0;
  virtual void setTopics(const std::vector<Topic> & topics) = 0;
  virtual void setUtterances(const std::vector<Utterance> & utterances) = 0;
};

class ConversationServiceIfFactory : virtual public ItemServiceIfFactory {
 public:
  typedef ConversationServiceIf Handler;

  virtual ~ConversationServiceIfFactory() {}

  virtual ConversationServiceIf* getHandler(const ::apache::thrift::TConnectionInfo& connInfo) = 0;
  virtual void releaseHandler(ItemServiceIf* /* handler */) = 0;
};

class ConversationServiceIfSingletonFactory : virtual public ConversationServiceIfFactory {
 public:
  ConversationServiceIfSingletonFactory(const boost::shared_ptr<ConversationServiceIf>& iface) : iface_(iface) {}
  virtual ~ConversationServiceIfSingletonFactory() {}

  virtual ConversationServiceIf* getHandler(const ::apache::thrift::TConnectionInfo&) {
    return iface_.get();
  }
  virtual void releaseHandler(ItemServiceIf* /* handler */) {}

 protected:
  boost::shared_ptr<ConversationServiceIf> iface_;
};

class ConversationServiceNull : virtual public ConversationServiceIf , virtual public ItemServiceNull {
 public:
  virtual ~ConversationServiceNull() {}
  void addTopic(const Topic& /* topic */) {
    return;
  }
  void addUtterance(const Utterance& /* utterance */) {
    return;
  }
  int64_t getConversationId() {
    int64_t _return = 0;
    return _return;
  }
  void getName(std::string& /* _return */) {
    return;
  }
  void getTopics(std::vector<Topic> & /* _return */) {
    return;
  }
  void getUtterances(std::vector<Utterance> & /* _return */) {
    return;
  }
  bool isOneSided() {
    bool _return = false;
    return _return;
  }
  void setOneSided(const bool /* oneSided */) {
    return;
  }
  void setTopics(const std::vector<Topic> & /* topics */) {
    return;
  }
  void setUtterances(const std::vector<Utterance> & /* utterances */) {
    return;
  }
};

typedef struct _ConversationService_addTopic_args__isset {
  _ConversationService_addTopic_args__isset() : topic(false) {}
  bool topic;
} _ConversationService_addTopic_args__isset;

class ConversationService_addTopic_args {
 public:

  ConversationService_addTopic_args() {
  }

  virtual ~ConversationService_addTopic_args() throw() {}

  Topic topic;

  _ConversationService_addTopic_args__isset __isset;

  void __set_topic(const Topic& val) {
    topic = val;
  }

  bool operator == (const ConversationService_addTopic_args & rhs) const
  {
    if (!(topic == rhs.topic))
      return false;
    return true;
  }
  bool operator != (const ConversationService_addTopic_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_addTopic_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_addTopic_pargs {
 public:


  virtual ~ConversationService_addTopic_pargs() throw() {}

  const Topic* topic;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_addTopic_result {
 public:

  ConversationService_addTopic_result() {
  }

  virtual ~ConversationService_addTopic_result() throw() {}


  bool operator == (const ConversationService_addTopic_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_addTopic_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_addTopic_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_addTopic_presult {
 public:


  virtual ~ConversationService_addTopic_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _ConversationService_addUtterance_args__isset {
  _ConversationService_addUtterance_args__isset() : utterance(false) {}
  bool utterance;
} _ConversationService_addUtterance_args__isset;

class ConversationService_addUtterance_args {
 public:

  ConversationService_addUtterance_args() {
  }

  virtual ~ConversationService_addUtterance_args() throw() {}

  Utterance utterance;

  _ConversationService_addUtterance_args__isset __isset;

  void __set_utterance(const Utterance& val) {
    utterance = val;
  }

  bool operator == (const ConversationService_addUtterance_args & rhs) const
  {
    if (!(utterance == rhs.utterance))
      return false;
    return true;
  }
  bool operator != (const ConversationService_addUtterance_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_addUtterance_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_addUtterance_pargs {
 public:


  virtual ~ConversationService_addUtterance_pargs() throw() {}

  const Utterance* utterance;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_addUtterance_result {
 public:

  ConversationService_addUtterance_result() {
  }

  virtual ~ConversationService_addUtterance_result() throw() {}


  bool operator == (const ConversationService_addUtterance_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_addUtterance_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_addUtterance_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_addUtterance_presult {
 public:


  virtual ~ConversationService_addUtterance_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class ConversationService_getConversationId_args {
 public:

  ConversationService_getConversationId_args() {
  }

  virtual ~ConversationService_getConversationId_args() throw() {}


  bool operator == (const ConversationService_getConversationId_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_getConversationId_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getConversationId_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_getConversationId_pargs {
 public:


  virtual ~ConversationService_getConversationId_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getConversationId_result__isset {
  _ConversationService_getConversationId_result__isset() : success(false) {}
  bool success;
} _ConversationService_getConversationId_result__isset;

class ConversationService_getConversationId_result {
 public:

  ConversationService_getConversationId_result() : success(0) {
  }

  virtual ~ConversationService_getConversationId_result() throw() {}

  int64_t success;

  _ConversationService_getConversationId_result__isset __isset;

  void __set_success(const int64_t val) {
    success = val;
  }

  bool operator == (const ConversationService_getConversationId_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ConversationService_getConversationId_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getConversationId_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getConversationId_presult__isset {
  _ConversationService_getConversationId_presult__isset() : success(false) {}
  bool success;
} _ConversationService_getConversationId_presult__isset;

class ConversationService_getConversationId_presult {
 public:


  virtual ~ConversationService_getConversationId_presult() throw() {}

  int64_t* success;

  _ConversationService_getConversationId_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class ConversationService_getName_args {
 public:

  ConversationService_getName_args() {
  }

  virtual ~ConversationService_getName_args() throw() {}


  bool operator == (const ConversationService_getName_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_getName_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getName_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_getName_pargs {
 public:


  virtual ~ConversationService_getName_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getName_result__isset {
  _ConversationService_getName_result__isset() : success(false) {}
  bool success;
} _ConversationService_getName_result__isset;

class ConversationService_getName_result {
 public:

  ConversationService_getName_result() : success() {
  }

  virtual ~ConversationService_getName_result() throw() {}

  std::string success;

  _ConversationService_getName_result__isset __isset;

  void __set_success(const std::string& val) {
    success = val;
  }

  bool operator == (const ConversationService_getName_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ConversationService_getName_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getName_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getName_presult__isset {
  _ConversationService_getName_presult__isset() : success(false) {}
  bool success;
} _ConversationService_getName_presult__isset;

class ConversationService_getName_presult {
 public:


  virtual ~ConversationService_getName_presult() throw() {}

  std::string* success;

  _ConversationService_getName_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class ConversationService_getTopics_args {
 public:

  ConversationService_getTopics_args() {
  }

  virtual ~ConversationService_getTopics_args() throw() {}


  bool operator == (const ConversationService_getTopics_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_getTopics_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getTopics_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_getTopics_pargs {
 public:


  virtual ~ConversationService_getTopics_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getTopics_result__isset {
  _ConversationService_getTopics_result__isset() : success(false) {}
  bool success;
} _ConversationService_getTopics_result__isset;

class ConversationService_getTopics_result {
 public:

  ConversationService_getTopics_result() {
  }

  virtual ~ConversationService_getTopics_result() throw() {}

  std::vector<Topic>  success;

  _ConversationService_getTopics_result__isset __isset;

  void __set_success(const std::vector<Topic> & val) {
    success = val;
  }

  bool operator == (const ConversationService_getTopics_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ConversationService_getTopics_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getTopics_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getTopics_presult__isset {
  _ConversationService_getTopics_presult__isset() : success(false) {}
  bool success;
} _ConversationService_getTopics_presult__isset;

class ConversationService_getTopics_presult {
 public:


  virtual ~ConversationService_getTopics_presult() throw() {}

  std::vector<Topic> * success;

  _ConversationService_getTopics_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class ConversationService_getUtterances_args {
 public:

  ConversationService_getUtterances_args() {
  }

  virtual ~ConversationService_getUtterances_args() throw() {}


  bool operator == (const ConversationService_getUtterances_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_getUtterances_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getUtterances_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_getUtterances_pargs {
 public:


  virtual ~ConversationService_getUtterances_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getUtterances_result__isset {
  _ConversationService_getUtterances_result__isset() : success(false) {}
  bool success;
} _ConversationService_getUtterances_result__isset;

class ConversationService_getUtterances_result {
 public:

  ConversationService_getUtterances_result() {
  }

  virtual ~ConversationService_getUtterances_result() throw() {}

  std::vector<Utterance>  success;

  _ConversationService_getUtterances_result__isset __isset;

  void __set_success(const std::vector<Utterance> & val) {
    success = val;
  }

  bool operator == (const ConversationService_getUtterances_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ConversationService_getUtterances_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_getUtterances_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_getUtterances_presult__isset {
  _ConversationService_getUtterances_presult__isset() : success(false) {}
  bool success;
} _ConversationService_getUtterances_presult__isset;

class ConversationService_getUtterances_presult {
 public:


  virtual ~ConversationService_getUtterances_presult() throw() {}

  std::vector<Utterance> * success;

  _ConversationService_getUtterances_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};


class ConversationService_isOneSided_args {
 public:

  ConversationService_isOneSided_args() {
  }

  virtual ~ConversationService_isOneSided_args() throw() {}


  bool operator == (const ConversationService_isOneSided_args & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_isOneSided_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_isOneSided_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_isOneSided_pargs {
 public:


  virtual ~ConversationService_isOneSided_pargs() throw() {}


  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_isOneSided_result__isset {
  _ConversationService_isOneSided_result__isset() : success(false) {}
  bool success;
} _ConversationService_isOneSided_result__isset;

class ConversationService_isOneSided_result {
 public:

  ConversationService_isOneSided_result() : success(0) {
  }

  virtual ~ConversationService_isOneSided_result() throw() {}

  bool success;

  _ConversationService_isOneSided_result__isset __isset;

  void __set_success(const bool val) {
    success = val;
  }

  bool operator == (const ConversationService_isOneSided_result & rhs) const
  {
    if (!(success == rhs.success))
      return false;
    return true;
  }
  bool operator != (const ConversationService_isOneSided_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_isOneSided_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};

typedef struct _ConversationService_isOneSided_presult__isset {
  _ConversationService_isOneSided_presult__isset() : success(false) {}
  bool success;
} _ConversationService_isOneSided_presult__isset;

class ConversationService_isOneSided_presult {
 public:


  virtual ~ConversationService_isOneSided_presult() throw() {}

  bool* success;

  _ConversationService_isOneSided_presult__isset __isset;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _ConversationService_setOneSided_args__isset {
  _ConversationService_setOneSided_args__isset() : oneSided(false) {}
  bool oneSided;
} _ConversationService_setOneSided_args__isset;

class ConversationService_setOneSided_args {
 public:

  ConversationService_setOneSided_args() : oneSided(0) {
  }

  virtual ~ConversationService_setOneSided_args() throw() {}

  bool oneSided;

  _ConversationService_setOneSided_args__isset __isset;

  void __set_oneSided(const bool val) {
    oneSided = val;
  }

  bool operator == (const ConversationService_setOneSided_args & rhs) const
  {
    if (!(oneSided == rhs.oneSided))
      return false;
    return true;
  }
  bool operator != (const ConversationService_setOneSided_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_setOneSided_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setOneSided_pargs {
 public:


  virtual ~ConversationService_setOneSided_pargs() throw() {}

  const bool* oneSided;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setOneSided_result {
 public:

  ConversationService_setOneSided_result() {
  }

  virtual ~ConversationService_setOneSided_result() throw() {}


  bool operator == (const ConversationService_setOneSided_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_setOneSided_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_setOneSided_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setOneSided_presult {
 public:


  virtual ~ConversationService_setOneSided_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _ConversationService_setTopics_args__isset {
  _ConversationService_setTopics_args__isset() : topics(false) {}
  bool topics;
} _ConversationService_setTopics_args__isset;

class ConversationService_setTopics_args {
 public:

  ConversationService_setTopics_args() {
  }

  virtual ~ConversationService_setTopics_args() throw() {}

  std::vector<Topic>  topics;

  _ConversationService_setTopics_args__isset __isset;

  void __set_topics(const std::vector<Topic> & val) {
    topics = val;
  }

  bool operator == (const ConversationService_setTopics_args & rhs) const
  {
    if (!(topics == rhs.topics))
      return false;
    return true;
  }
  bool operator != (const ConversationService_setTopics_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_setTopics_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setTopics_pargs {
 public:


  virtual ~ConversationService_setTopics_pargs() throw() {}

  const std::vector<Topic> * topics;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setTopics_result {
 public:

  ConversationService_setTopics_result() {
  }

  virtual ~ConversationService_setTopics_result() throw() {}


  bool operator == (const ConversationService_setTopics_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_setTopics_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_setTopics_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setTopics_presult {
 public:


  virtual ~ConversationService_setTopics_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

typedef struct _ConversationService_setUtterances_args__isset {
  _ConversationService_setUtterances_args__isset() : utterances(false) {}
  bool utterances;
} _ConversationService_setUtterances_args__isset;

class ConversationService_setUtterances_args {
 public:

  ConversationService_setUtterances_args() {
  }

  virtual ~ConversationService_setUtterances_args() throw() {}

  std::vector<Utterance>  utterances;

  _ConversationService_setUtterances_args__isset __isset;

  void __set_utterances(const std::vector<Utterance> & val) {
    utterances = val;
  }

  bool operator == (const ConversationService_setUtterances_args & rhs) const
  {
    if (!(utterances == rhs.utterances))
      return false;
    return true;
  }
  bool operator != (const ConversationService_setUtterances_args &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_setUtterances_args & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setUtterances_pargs {
 public:


  virtual ~ConversationService_setUtterances_pargs() throw() {}

  const std::vector<Utterance> * utterances;

  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setUtterances_result {
 public:

  ConversationService_setUtterances_result() {
  }

  virtual ~ConversationService_setUtterances_result() throw() {}


  bool operator == (const ConversationService_setUtterances_result & /* rhs */) const
  {
    return true;
  }
  bool operator != (const ConversationService_setUtterances_result &rhs) const {
    return !(*this == rhs);
  }

  bool operator < (const ConversationService_setUtterances_result & ) const;

  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);
  uint32_t write(::apache::thrift::protocol::TProtocol* oprot) const;

};


class ConversationService_setUtterances_presult {
 public:


  virtual ~ConversationService_setUtterances_presult() throw() {}


  uint32_t read(::apache::thrift::protocol::TProtocol* iprot);

};

class ConversationServiceClient : virtual public ConversationServiceIf, public ItemServiceClient {
 public:
  ConversationServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> prot) :
    ItemServiceClient(prot, prot) {}
  ConversationServiceClient(boost::shared_ptr< ::apache::thrift::protocol::TProtocol> iprot, boost::shared_ptr< ::apache::thrift::protocol::TProtocol> oprot) :
    ItemServiceClient(iprot, oprot) {}
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getInputProtocol() {
    return piprot_;
  }
  boost::shared_ptr< ::apache::thrift::protocol::TProtocol> getOutputProtocol() {
    return poprot_;
  }
  void addTopic(const Topic& topic);
  void send_addTopic(const Topic& topic);
  void recv_addTopic();
  void addUtterance(const Utterance& utterance);
  void send_addUtterance(const Utterance& utterance);
  void recv_addUtterance();
  int64_t getConversationId();
  void send_getConversationId();
  int64_t recv_getConversationId();
  void getName(std::string& _return);
  void send_getName();
  void recv_getName(std::string& _return);
  void getTopics(std::vector<Topic> & _return);
  void send_getTopics();
  void recv_getTopics(std::vector<Topic> & _return);
  void getUtterances(std::vector<Utterance> & _return);
  void send_getUtterances();
  void recv_getUtterances(std::vector<Utterance> & _return);
  bool isOneSided();
  void send_isOneSided();
  bool recv_isOneSided();
  void setOneSided(const bool oneSided);
  void send_setOneSided(const bool oneSided);
  void recv_setOneSided();
  void setTopics(const std::vector<Topic> & topics);
  void send_setTopics(const std::vector<Topic> & topics);
  void recv_setTopics();
  void setUtterances(const std::vector<Utterance> & utterances);
  void send_setUtterances(const std::vector<Utterance> & utterances);
  void recv_setUtterances();
};

class ConversationServiceProcessor : public ItemServiceProcessor {
 protected:
  boost::shared_ptr<ConversationServiceIf> iface_;
  virtual bool dispatchCall(::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, const std::string& fname, int32_t seqid, void* callContext);
 private:
  typedef  void (ConversationServiceProcessor::*ProcessFunction)(int32_t, ::apache::thrift::protocol::TProtocol*, ::apache::thrift::protocol::TProtocol*, void*);
  typedef std::map<std::string, ProcessFunction> ProcessMap;
  ProcessMap processMap_;
  void process_addTopic(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_addUtterance(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getConversationId(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getName(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getTopics(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_getUtterances(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_isOneSided(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setOneSided(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setTopics(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
  void process_setUtterances(int32_t seqid, ::apache::thrift::protocol::TProtocol* iprot, ::apache::thrift::protocol::TProtocol* oprot, void* callContext);
 public:
  ConversationServiceProcessor(boost::shared_ptr<ConversationServiceIf> iface) :
    ItemServiceProcessor(iface),
    iface_(iface) {
    processMap_["addTopic"] = &ConversationServiceProcessor::process_addTopic;
    processMap_["addUtterance"] = &ConversationServiceProcessor::process_addUtterance;
    processMap_["getConversationId"] = &ConversationServiceProcessor::process_getConversationId;
    processMap_["getName"] = &ConversationServiceProcessor::process_getName;
    processMap_["getTopics"] = &ConversationServiceProcessor::process_getTopics;
    processMap_["getUtterances"] = &ConversationServiceProcessor::process_getUtterances;
    processMap_["isOneSided"] = &ConversationServiceProcessor::process_isOneSided;
    processMap_["setOneSided"] = &ConversationServiceProcessor::process_setOneSided;
    processMap_["setTopics"] = &ConversationServiceProcessor::process_setTopics;
    processMap_["setUtterances"] = &ConversationServiceProcessor::process_setUtterances;
  }

  virtual ~ConversationServiceProcessor() {}
};

class ConversationServiceProcessorFactory : public ::apache::thrift::TProcessorFactory {
 public:
  ConversationServiceProcessorFactory(const ::boost::shared_ptr< ConversationServiceIfFactory >& handlerFactory) :
      handlerFactory_(handlerFactory) {}

  ::boost::shared_ptr< ::apache::thrift::TProcessor > getProcessor(const ::apache::thrift::TConnectionInfo& connInfo);

 protected:
  ::boost::shared_ptr< ConversationServiceIfFactory > handlerFactory_;
};

class ConversationServiceMultiface : virtual public ConversationServiceIf, public ItemServiceMultiface {
 public:
  ConversationServiceMultiface(std::vector<boost::shared_ptr<ConversationServiceIf> >& ifaces) : ifaces_(ifaces) {
    std::vector<boost::shared_ptr<ConversationServiceIf> >::iterator iter;
    for (iter = ifaces.begin(); iter != ifaces.end(); ++iter) {
      ItemServiceMultiface::add(*iter);
    }
  }
  virtual ~ConversationServiceMultiface() {}
 protected:
  std::vector<boost::shared_ptr<ConversationServiceIf> > ifaces_;
  ConversationServiceMultiface() {}
  void add(boost::shared_ptr<ConversationServiceIf> iface) {
    ItemServiceMultiface::add(iface);
    ifaces_.push_back(iface);
  }
 public:
  void addTopic(const Topic& topic) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->addTopic(topic);
    }
    ifaces_[i]->addTopic(topic);
  }

  void addUtterance(const Utterance& utterance) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->addUtterance(utterance);
    }
    ifaces_[i]->addUtterance(utterance);
  }

  int64_t getConversationId() {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getConversationId();
    }
    return ifaces_[i]->getConversationId();
  }

  void getName(std::string& _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getName(_return);
    }
    ifaces_[i]->getName(_return);
    return;
  }

  void getTopics(std::vector<Topic> & _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getTopics(_return);
    }
    ifaces_[i]->getTopics(_return);
    return;
  }

  void getUtterances(std::vector<Utterance> & _return) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->getUtterances(_return);
    }
    ifaces_[i]->getUtterances(_return);
    return;
  }

  bool isOneSided() {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->isOneSided();
    }
    return ifaces_[i]->isOneSided();
  }

  void setOneSided(const bool oneSided) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setOneSided(oneSided);
    }
    ifaces_[i]->setOneSided(oneSided);
  }

  void setTopics(const std::vector<Topic> & topics) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setTopics(topics);
    }
    ifaces_[i]->setTopics(topics);
  }

  void setUtterances(const std::vector<Utterance> & utterances) {
    size_t sz = ifaces_.size();
    size_t i = 0;
    for (; i < (sz - 1); ++i) {
      ifaces_[i]->setUtterances(utterances);
    }
    ifaces_[i]->setUtterances(utterances);
  }

};

}}} // namespace

#endif
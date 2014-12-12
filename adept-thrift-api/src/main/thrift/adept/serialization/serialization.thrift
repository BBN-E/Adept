/*******************************************************************************
 * Raytheon BBN Technologies Corp., December 2014
 * 
 * THIS CODE IS PROVIDED "AS IS" WITHOUT WARRANTY OF ANY KIND, EITHER EXPRESS
 * OR IMPLIED, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * Copyright 2014 Raytheon BBN Technologies Corp.  All Rights Reserved.
 ******************************************************************************/
/**
 * 
 */

namespace java thrift.adept.serialization
namespace perl thrift.adept.serialization
namespace cpp thrift.adept.serialization

include "../common/common.thrift"

enum SerializationType {
  BINARY = 0,
  XML = 1,
  JSON = 2
}

service Serializer {
  binary serializeAsByteArray(1:common.HltContentContainer hltContentContainer),
  common.HltContentContainer deserializeByteArray(1:binary data),
  string serializeAsString(1:common.HltContentContainer hltContentContainer),
  common.HltContentContainer deserializeString(1:string data)
}

/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
#include "serialization_types.h"

#include <algorithm>

namespace thrift { namespace adept { namespace serialization {

int _kSerializationTypeValues[] = {
  SerializationType::BINARY,
  SerializationType::XML,
  SerializationType::JSON
};
const char* _kSerializationTypeNames[] = {
  "BINARY",
  "XML",
  "JSON"
};
const std::map<int, const char*> _SerializationType_VALUES_TO_NAMES(::apache::thrift::TEnumIterator(3, _kSerializationTypeValues, _kSerializationTypeNames), ::apache::thrift::TEnumIterator(-1, NULL, NULL));

}}} // namespace
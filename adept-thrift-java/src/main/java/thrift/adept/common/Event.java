/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.adept.common;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class Event.
 */
public class Event implements org.apache.thrift.TBase<Event, Event._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Event");

  private static final org.apache.thrift.protocol.TField EVENT_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("eventId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField EVENT_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("eventType", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField ATTRIBUTES_FIELD_DESC = new org.apache.thrift.protocol.TField("attributes", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField ARGUMENTS_FIELD_DESC = new org.apache.thrift.protocol.TField("arguments", org.apache.thrift.protocol.TType.LIST, (short)4);
  private static final org.apache.thrift.protocol.TField CONFIDENCE_FIELD_DESC = new org.apache.thrift.protocol.TField("confidence", org.apache.thrift.protocol.TType.DOUBLE, (short)5);
  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.STRUCT, (short)6);
  private static final org.apache.thrift.protocol.TField VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("value", org.apache.thrift.protocol.TType.STRING, (short)7);
  private static final org.apache.thrift.protocol.TField ALGORITHM_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("algorithmName", org.apache.thrift.protocol.TType.STRING, (short)8);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new EventStandardSchemeFactory());
    schemes.put(TupleScheme.class, new EventTupleSchemeFactory());
  }

  /**
   * The event id
   */
  public long eventId; // required
  /**
   * The type
   */
  public Type eventType; // required
  /**
   * The attributes
   */
  public List<Type> attributes; // optional
  /**
   * The arguments
   */
  public List<Argument> arguments; // optional
  /**
   * The confidence
   */
  public double confidence; // optional
  /**
   * The id
   */
  public ID id; // optional
  /**
   * The value
   */
  public String value; // optional
  /**
   * The algorithm name
   */
  public String algorithmName; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * The event id
     */
    EVENT_ID((short)1, "eventId"),
    /**
     * The type
     */
    EVENT_TYPE((short)2, "eventType"),
    /**
     * The attributes
     */
    ATTRIBUTES((short)3, "attributes"),
    /**
     * The arguments
     */
    ARGUMENTS((short)4, "arguments"),
    /**
     * The confidence
     */
    CONFIDENCE((short)5, "confidence"),
    /**
     * The id
     */
    ID((short)6, "id"),
    /**
     * The value
     */
    VALUE((short)7, "value"),
    /**
     * The algorithm name
     */
    ALGORITHM_NAME((short)8, "algorithmName");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // EVENT_ID
          return EVENT_ID;
        case 2: // EVENT_TYPE
          return EVENT_TYPE;
        case 3: // ATTRIBUTES
          return ATTRIBUTES;
        case 4: // ARGUMENTS
          return ARGUMENTS;
        case 5: // CONFIDENCE
          return CONFIDENCE;
        case 6: // ID
          return ID;
        case 7: // VALUE
          return VALUE;
        case 8: // ALGORITHM_NAME
          return ALGORITHM_NAME;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __EVENTID_ISSET_ID = 0;
  private static final int __CONFIDENCE_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.ATTRIBUTES,_Fields.ARGUMENTS,_Fields.CONFIDENCE,_Fields.ID,_Fields.VALUE,_Fields.ALGORITHM_NAME};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.EVENT_ID, new org.apache.thrift.meta_data.FieldMetaData("eventId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.EVENT_TYPE, new org.apache.thrift.meta_data.FieldMetaData("eventType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Type.class)));
    tmpMap.put(_Fields.ATTRIBUTES, new org.apache.thrift.meta_data.FieldMetaData("attributes", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Type.class))));
    tmpMap.put(_Fields.ARGUMENTS, new org.apache.thrift.meta_data.FieldMetaData("arguments", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Argument.class))));
    tmpMap.put(_Fields.CONFIDENCE, new org.apache.thrift.meta_data.FieldMetaData("confidence", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ID.class)));
    tmpMap.put(_Fields.VALUE, new org.apache.thrift.meta_data.FieldMetaData("value", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ALGORITHM_NAME, new org.apache.thrift.meta_data.FieldMetaData("algorithmName", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Event.class, metaDataMap);
  }

  public Event() {
  }

  public Event(
    long eventId,
    Type eventType)
  {
    this();
    this.eventId = eventId;
    setEventIdIsSet(true);
    this.eventType = eventType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Event(Event other) {
    __isset_bitfield = other.__isset_bitfield;
    this.eventId = other.eventId;
    if (other.isSetEventType()) {
      this.eventType = new Type(other.eventType);
    }
    if (other.isSetAttributes()) {
      List<Type> __this__attributes = new ArrayList<Type>();
      for (Type other_element : other.attributes) {
        __this__attributes.add(new Type(other_element));
      }
      this.attributes = __this__attributes;
    }
    if (other.isSetArguments()) {
      List<Argument> __this__arguments = new ArrayList<Argument>();
      for (Argument other_element : other.arguments) {
        __this__arguments.add(new Argument(other_element));
      }
      this.arguments = __this__arguments;
    }
    this.confidence = other.confidence;
    if (other.isSetId()) {
      this.id = new ID(other.id);
    }
    if (other.isSetValue()) {
      this.value = other.value;
    }
    if (other.isSetAlgorithmName()) {
      this.algorithmName = other.algorithmName;
    }
  }

  public Event deepCopy() {
    return new Event(this);
  }

  @Override
  public void clear() {
    setEventIdIsSet(false);
    this.eventId = 0;
    this.eventType = null;
    this.attributes = null;
    this.arguments = null;
    setConfidenceIsSet(false);
    this.confidence = 0.0;
    this.id = null;
    this.value = null;
    this.algorithmName = null;
  }

  /**
   * The event id
   */
  public long getEventId() {
    return this.eventId;
  }

  /**
   * The event id
   */
  public Event setEventId(long eventId) {
    this.eventId = eventId;
    setEventIdIsSet(true);
    return this;
  }

  public void unsetEventId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __EVENTID_ISSET_ID);
  }

  /** Returns true if field eventId is set (has been assigned a value) and false otherwise */
  public boolean isSetEventId() {
    return EncodingUtils.testBit(__isset_bitfield, __EVENTID_ISSET_ID);
  }

  public void setEventIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __EVENTID_ISSET_ID, value);
  }

  /**
   * The type
   */
  public Type getEventType() {
    return this.eventType;
  }

  /**
   * The type
   */
  public Event setEventType(Type eventType) {
    this.eventType = eventType;
    return this;
  }

  public void unsetEventType() {
    this.eventType = null;
  }

  /** Returns true if field eventType is set (has been assigned a value) and false otherwise */
  public boolean isSetEventType() {
    return this.eventType != null;
  }

  public void setEventTypeIsSet(boolean value) {
    if (!value) {
      this.eventType = null;
    }
  }

  public int getAttributesSize() {
    return (this.attributes == null) ? 0 : this.attributes.size();
  }

  public java.util.Iterator<Type> getAttributesIterator() {
    return (this.attributes == null) ? null : this.attributes.iterator();
  }

  public void addToAttributes(Type elem) {
    if (this.attributes == null) {
      this.attributes = new ArrayList<Type>();
    }
    this.attributes.add(elem);
  }

  /**
   * The attributes
   */
  public List<Type> getAttributes() {
    return this.attributes;
  }

  /**
   * The attributes
   */
  public Event setAttributes(List<Type> attributes) {
    this.attributes = attributes;
    return this;
  }

  public void unsetAttributes() {
    this.attributes = null;
  }

  /** Returns true if field attributes is set (has been assigned a value) and false otherwise */
  public boolean isSetAttributes() {
    return this.attributes != null;
  }

  public void setAttributesIsSet(boolean value) {
    if (!value) {
      this.attributes = null;
    }
  }

  public int getArgumentsSize() {
    return (this.arguments == null) ? 0 : this.arguments.size();
  }

  public java.util.Iterator<Argument> getArgumentsIterator() {
    return (this.arguments == null) ? null : this.arguments.iterator();
  }

  public void addToArguments(Argument elem) {
    if (this.arguments == null) {
      this.arguments = new ArrayList<Argument>();
    }
    this.arguments.add(elem);
  }

  /**
   * The arguments
   */
  public List<Argument> getArguments() {
    return this.arguments;
  }

  /**
   * The arguments
   */
  public Event setArguments(List<Argument> arguments) {
    this.arguments = arguments;
    return this;
  }

  public void unsetArguments() {
    this.arguments = null;
  }

  /** Returns true if field arguments is set (has been assigned a value) and false otherwise */
  public boolean isSetArguments() {
    return this.arguments != null;
  }

  public void setArgumentsIsSet(boolean value) {
    if (!value) {
      this.arguments = null;
    }
  }

  /**
   * The confidence
   */
  public double getConfidence() {
    return this.confidence;
  }

  /**
   * The confidence
   */
  public Event setConfidence(double confidence) {
    this.confidence = confidence;
    setConfidenceIsSet(true);
    return this;
  }

  public void unsetConfidence() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CONFIDENCE_ISSET_ID);
  }

  /** Returns true if field confidence is set (has been assigned a value) and false otherwise */
  public boolean isSetConfidence() {
    return EncodingUtils.testBit(__isset_bitfield, __CONFIDENCE_ISSET_ID);
  }

  public void setConfidenceIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CONFIDENCE_ISSET_ID, value);
  }

  /**
   * The id
   */
  public ID getId() {
    return this.id;
  }

  /**
   * The id
   */
  public Event setId(ID id) {
    this.id = id;
    return this;
  }

  public void unsetId() {
    this.id = null;
  }

  /** Returns true if field id is set (has been assigned a value) and false otherwise */
  public boolean isSetId() {
    return this.id != null;
  }

  public void setIdIsSet(boolean value) {
    if (!value) {
      this.id = null;
    }
  }

  /**
   * The value
   */
  public String getValue() {
    return this.value;
  }

  /**
   * The value
   */
  public Event setValue(String value) {
    this.value = value;
    return this;
  }

  public void unsetValue() {
    this.value = null;
  }

  /** Returns true if field value is set (has been assigned a value) and false otherwise */
  public boolean isSetValue() {
    return this.value != null;
  }

  public void setValueIsSet(boolean value) {
    if (!value) {
      this.value = null;
    }
  }

  /**
   * The algorithm name
   */
  public String getAlgorithmName() {
    return this.algorithmName;
  }

  /**
   * The algorithm name
   */
  public Event setAlgorithmName(String algorithmName) {
    this.algorithmName = algorithmName;
    return this;
  }

  public void unsetAlgorithmName() {
    this.algorithmName = null;
  }

  /** Returns true if field algorithmName is set (has been assigned a value) and false otherwise */
  public boolean isSetAlgorithmName() {
    return this.algorithmName != null;
  }

  public void setAlgorithmNameIsSet(boolean value) {
    if (!value) {
      this.algorithmName = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case EVENT_ID:
      if (value == null) {
        unsetEventId();
      } else {
        setEventId((Long)value);
      }
      break;

    case EVENT_TYPE:
      if (value == null) {
        unsetEventType();
      } else {
        setEventType((Type)value);
      }
      break;

    case ATTRIBUTES:
      if (value == null) {
        unsetAttributes();
      } else {
        setAttributes((List<Type>)value);
      }
      break;

    case ARGUMENTS:
      if (value == null) {
        unsetArguments();
      } else {
        setArguments((List<Argument>)value);
      }
      break;

    case CONFIDENCE:
      if (value == null) {
        unsetConfidence();
      } else {
        setConfidence((Double)value);
      }
      break;

    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((ID)value);
      }
      break;

    case VALUE:
      if (value == null) {
        unsetValue();
      } else {
        setValue((String)value);
      }
      break;

    case ALGORITHM_NAME:
      if (value == null) {
        unsetAlgorithmName();
      } else {
        setAlgorithmName((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case EVENT_ID:
      return Long.valueOf(getEventId());

    case EVENT_TYPE:
      return getEventType();

    case ATTRIBUTES:
      return getAttributes();

    case ARGUMENTS:
      return getArguments();

    case CONFIDENCE:
      return Double.valueOf(getConfidence());

    case ID:
      return getId();

    case VALUE:
      return getValue();

    case ALGORITHM_NAME:
      return getAlgorithmName();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case EVENT_ID:
      return isSetEventId();
    case EVENT_TYPE:
      return isSetEventType();
    case ATTRIBUTES:
      return isSetAttributes();
    case ARGUMENTS:
      return isSetArguments();
    case CONFIDENCE:
      return isSetConfidence();
    case ID:
      return isSetId();
    case VALUE:
      return isSetValue();
    case ALGORITHM_NAME:
      return isSetAlgorithmName();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Event)
      return this.equals((Event)that);
    return false;
  }

  public boolean equals(Event that) {
    if (that == null)
      return false;

    boolean this_present_eventId = true;
    boolean that_present_eventId = true;
    if (this_present_eventId || that_present_eventId) {
      if (!(this_present_eventId && that_present_eventId))
        return false;
      if (this.eventId != that.eventId)
        return false;
    }

    boolean this_present_eventType = true && this.isSetEventType();
    boolean that_present_eventType = true && that.isSetEventType();
    if (this_present_eventType || that_present_eventType) {
      if (!(this_present_eventType && that_present_eventType))
        return false;
      if (!this.eventType.equals(that.eventType))
        return false;
    }

    boolean this_present_attributes = true && this.isSetAttributes();
    boolean that_present_attributes = true && that.isSetAttributes();
    if (this_present_attributes || that_present_attributes) {
      if (!(this_present_attributes && that_present_attributes))
        return false;
      if (!this.attributes.equals(that.attributes))
        return false;
    }

    boolean this_present_arguments = true && this.isSetArguments();
    boolean that_present_arguments = true && that.isSetArguments();
    if (this_present_arguments || that_present_arguments) {
      if (!(this_present_arguments && that_present_arguments))
        return false;
      if (!this.arguments.equals(that.arguments))
        return false;
    }

    boolean this_present_confidence = true && this.isSetConfidence();
    boolean that_present_confidence = true && that.isSetConfidence();
    if (this_present_confidence || that_present_confidence) {
      if (!(this_present_confidence && that_present_confidence))
        return false;
      if (this.confidence != that.confidence)
        return false;
    }

    boolean this_present_id = true && this.isSetId();
    boolean that_present_id = true && that.isSetId();
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (!this.id.equals(that.id))
        return false;
    }

    boolean this_present_value = true && this.isSetValue();
    boolean that_present_value = true && that.isSetValue();
    if (this_present_value || that_present_value) {
      if (!(this_present_value && that_present_value))
        return false;
      if (!this.value.equals(that.value))
        return false;
    }

    boolean this_present_algorithmName = true && this.isSetAlgorithmName();
    boolean that_present_algorithmName = true && that.isSetAlgorithmName();
    if (this_present_algorithmName || that_present_algorithmName) {
      if (!(this_present_algorithmName && that_present_algorithmName))
        return false;
      if (!this.algorithmName.equals(that.algorithmName))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Event other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Event typedOther = (Event)other;

    lastComparison = Boolean.valueOf(isSetEventId()).compareTo(typedOther.isSetEventId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEventId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.eventId, typedOther.eventId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEventType()).compareTo(typedOther.isSetEventType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEventType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.eventType, typedOther.eventType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAttributes()).compareTo(typedOther.isSetAttributes());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAttributes()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.attributes, typedOther.attributes);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetArguments()).compareTo(typedOther.isSetArguments());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetArguments()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.arguments, typedOther.arguments);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetConfidence()).compareTo(typedOther.isSetConfidence());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetConfidence()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.confidence, typedOther.confidence);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetId()).compareTo(typedOther.isSetId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, typedOther.id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetValue()).compareTo(typedOther.isSetValue());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetValue()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.value, typedOther.value);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetAlgorithmName()).compareTo(typedOther.isSetAlgorithmName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetAlgorithmName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.algorithmName, typedOther.algorithmName);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("Event(");
    boolean first = true;

    sb.append("eventId:");
    sb.append(this.eventId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("eventType:");
    if (this.eventType == null) {
      sb.append("null");
    } else {
      sb.append(this.eventType);
    }
    first = false;
    if (isSetAttributes()) {
      if (!first) sb.append(", ");
      sb.append("attributes:");
      if (this.attributes == null) {
        sb.append("null");
      } else {
        sb.append(this.attributes);
      }
      first = false;
    }
    if (isSetArguments()) {
      if (!first) sb.append(", ");
      sb.append("arguments:");
      if (this.arguments == null) {
        sb.append("null");
      } else {
        sb.append(this.arguments);
      }
      first = false;
    }
    if (isSetConfidence()) {
      if (!first) sb.append(", ");
      sb.append("confidence:");
      sb.append(this.confidence);
      first = false;
    }
    if (isSetId()) {
      if (!first) sb.append(", ");
      sb.append("id:");
      if (this.id == null) {
        sb.append("null");
      } else {
        sb.append(this.id);
      }
      first = false;
    }
    if (isSetValue()) {
      if (!first) sb.append(", ");
      sb.append("value:");
      if (this.value == null) {
        sb.append("null");
      } else {
        sb.append(this.value);
      }
      first = false;
    }
    if (isSetAlgorithmName()) {
      if (!first) sb.append(", ");
      sb.append("algorithmName:");
      if (this.algorithmName == null) {
        sb.append("null");
      } else {
        sb.append(this.algorithmName);
      }
      first = false;
    }
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'eventId' because it's a primitive and you chose the non-beans generator.
    if (eventType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'eventType' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (eventType != null) {
      eventType.validate();
    }
    if (id != null) {
      id.validate();
    }
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class EventStandardSchemeFactory implements SchemeFactory {
    public EventStandardScheme getScheme() {
      return new EventStandardScheme();
    }
  }

  private static class EventStandardScheme extends StandardScheme<Event> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Event struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // EVENT_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.eventId = iprot.readI64();
              struct.setEventIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // EVENT_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.eventType = new Type();
              struct.eventType.read(iprot);
              struct.setEventTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ATTRIBUTES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list220 = iprot.readListBegin();
                struct.attributes = new ArrayList<Type>(_list220.size);
                for (int _i221 = 0; _i221 < _list220.size; ++_i221)
                {
                  Type _elem222; // required
                  _elem222 = new Type();
                  _elem222.read(iprot);
                  struct.attributes.add(_elem222);
                }
                iprot.readListEnd();
              }
              struct.setAttributesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // ARGUMENTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list223 = iprot.readListBegin();
                struct.arguments = new ArrayList<Argument>(_list223.size);
                for (int _i224 = 0; _i224 < _list223.size; ++_i224)
                {
                  Argument _elem225; // required
                  _elem225 = new Argument();
                  _elem225.read(iprot);
                  struct.arguments.add(_elem225);
                }
                iprot.readListEnd();
              }
              struct.setArgumentsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // CONFIDENCE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.confidence = iprot.readDouble();
              struct.setConfidenceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.id = new ID();
              struct.id.read(iprot);
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.value = iprot.readString();
              struct.setValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 8: // ALGORITHM_NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.algorithmName = iprot.readString();
              struct.setAlgorithmNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetEventId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'eventId' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Event struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(EVENT_ID_FIELD_DESC);
      oprot.writeI64(struct.eventId);
      oprot.writeFieldEnd();
      if (struct.eventType != null) {
        oprot.writeFieldBegin(EVENT_TYPE_FIELD_DESC);
        struct.eventType.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.attributes != null) {
        if (struct.isSetAttributes()) {
          oprot.writeFieldBegin(ATTRIBUTES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.attributes.size()));
            for (Type _iter226 : struct.attributes)
            {
              _iter226.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.arguments != null) {
        if (struct.isSetArguments()) {
          oprot.writeFieldBegin(ARGUMENTS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.arguments.size()));
            for (Argument _iter227 : struct.arguments)
            {
              _iter227.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.isSetConfidence()) {
        oprot.writeFieldBegin(CONFIDENCE_FIELD_DESC);
        oprot.writeDouble(struct.confidence);
        oprot.writeFieldEnd();
      }
      if (struct.id != null) {
        if (struct.isSetId()) {
          oprot.writeFieldBegin(ID_FIELD_DESC);
          struct.id.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.value != null) {
        if (struct.isSetValue()) {
          oprot.writeFieldBegin(VALUE_FIELD_DESC);
          oprot.writeString(struct.value);
          oprot.writeFieldEnd();
        }
      }
      if (struct.algorithmName != null) {
        if (struct.isSetAlgorithmName()) {
          oprot.writeFieldBegin(ALGORITHM_NAME_FIELD_DESC);
          oprot.writeString(struct.algorithmName);
          oprot.writeFieldEnd();
        }
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class EventTupleSchemeFactory implements SchemeFactory {
    public EventTupleScheme getScheme() {
      return new EventTupleScheme();
    }
  }

  private static class EventTupleScheme extends TupleScheme<Event> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Event struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.eventId);
      struct.eventType.write(oprot);
      BitSet optionals = new BitSet();
      if (struct.isSetAttributes()) {
        optionals.set(0);
      }
      if (struct.isSetArguments()) {
        optionals.set(1);
      }
      if (struct.isSetConfidence()) {
        optionals.set(2);
      }
      if (struct.isSetId()) {
        optionals.set(3);
      }
      if (struct.isSetValue()) {
        optionals.set(4);
      }
      if (struct.isSetAlgorithmName()) {
        optionals.set(5);
      }
      oprot.writeBitSet(optionals, 6);
      if (struct.isSetAttributes()) {
        {
          oprot.writeI32(struct.attributes.size());
          for (Type _iter228 : struct.attributes)
          {
            _iter228.write(oprot);
          }
        }
      }
      if (struct.isSetArguments()) {
        {
          oprot.writeI32(struct.arguments.size());
          for (Argument _iter229 : struct.arguments)
          {
            _iter229.write(oprot);
          }
        }
      }
      if (struct.isSetConfidence()) {
        oprot.writeDouble(struct.confidence);
      }
      if (struct.isSetId()) {
        struct.id.write(oprot);
      }
      if (struct.isSetValue()) {
        oprot.writeString(struct.value);
      }
      if (struct.isSetAlgorithmName()) {
        oprot.writeString(struct.algorithmName);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Event struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.eventId = iprot.readI64();
      struct.setEventIdIsSet(true);
      struct.eventType = new Type();
      struct.eventType.read(iprot);
      struct.setEventTypeIsSet(true);
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list230 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.attributes = new ArrayList<Type>(_list230.size);
          for (int _i231 = 0; _i231 < _list230.size; ++_i231)
          {
            Type _elem232; // required
            _elem232 = new Type();
            _elem232.read(iprot);
            struct.attributes.add(_elem232);
          }
        }
        struct.setAttributesIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list233 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.arguments = new ArrayList<Argument>(_list233.size);
          for (int _i234 = 0; _i234 < _list233.size; ++_i234)
          {
            Argument _elem235; // required
            _elem235 = new Argument();
            _elem235.read(iprot);
            struct.arguments.add(_elem235);
          }
        }
        struct.setArgumentsIsSet(true);
      }
      if (incoming.get(2)) {
        struct.confidence = iprot.readDouble();
        struct.setConfidenceIsSet(true);
      }
      if (incoming.get(3)) {
        struct.id = new ID();
        struct.id.read(iprot);
        struct.setIdIsSet(true);
      }
      if (incoming.get(4)) {
        struct.value = iprot.readString();
        struct.setValueIsSet(true);
      }
      if (incoming.get(5)) {
        struct.algorithmName = iprot.readString();
        struct.setAlgorithmNameIsSet(true);
      }
    }
  }

}
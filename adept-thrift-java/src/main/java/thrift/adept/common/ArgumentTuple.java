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
 * The Class ArgumentTuple.
 */
public class ArgumentTuple implements org.apache.thrift.TBase<ArgumentTuple, ArgumentTuple._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("ArgumentTuple");

  private static final org.apache.thrift.protocol.TField TUPLE_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("tupleType", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField ATTRIBUTES_FIELD_DESC = new org.apache.thrift.protocol.TField("attributes", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField ARGUMENTS_FIELD_DESC = new org.apache.thrift.protocol.TField("arguments", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField CONFIDENCE_FIELD_DESC = new org.apache.thrift.protocol.TField("confidence", org.apache.thrift.protocol.TType.DOUBLE, (short)4);
  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.STRUCT, (short)5);
  private static final org.apache.thrift.protocol.TField VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("value", org.apache.thrift.protocol.TType.STRING, (short)6);
  private static final org.apache.thrift.protocol.TField ALGORITHM_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("algorithmName", org.apache.thrift.protocol.TType.STRING, (short)7);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ArgumentTupleStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ArgumentTupleTupleSchemeFactory());
  }

  /**
   * The tuple type
   */
  public Type tupleType; // required
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
     * The tuple type
     */
    TUPLE_TYPE((short)1, "tupleType"),
    /**
     * The attributes
     */
    ATTRIBUTES((short)2, "attributes"),
    /**
     * The arguments
     */
    ARGUMENTS((short)3, "arguments"),
    /**
     * The confidence
     */
    CONFIDENCE((short)4, "confidence"),
    /**
     * The id
     */
    ID((short)5, "id"),
    /**
     * The value
     */
    VALUE((short)6, "value"),
    /**
     * The algorithm name
     */
    ALGORITHM_NAME((short)7, "algorithmName");

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
        case 1: // TUPLE_TYPE
          return TUPLE_TYPE;
        case 2: // ATTRIBUTES
          return ATTRIBUTES;
        case 3: // ARGUMENTS
          return ARGUMENTS;
        case 4: // CONFIDENCE
          return CONFIDENCE;
        case 5: // ID
          return ID;
        case 6: // VALUE
          return VALUE;
        case 7: // ALGORITHM_NAME
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
  private static final int __CONFIDENCE_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.ATTRIBUTES,_Fields.ARGUMENTS,_Fields.CONFIDENCE,_Fields.ID,_Fields.VALUE,_Fields.ALGORITHM_NAME};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.TUPLE_TYPE, new org.apache.thrift.meta_data.FieldMetaData("tupleType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
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
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(ArgumentTuple.class, metaDataMap);
  }

  public ArgumentTuple() {
  }

  public ArgumentTuple(
    Type tupleType)
  {
    this();
    this.tupleType = tupleType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public ArgumentTuple(ArgumentTuple other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetTupleType()) {
      this.tupleType = new Type(other.tupleType);
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

  public ArgumentTuple deepCopy() {
    return new ArgumentTuple(this);
  }

  @Override
  public void clear() {
    this.tupleType = null;
    this.attributes = null;
    this.arguments = null;
    setConfidenceIsSet(false);
    this.confidence = 0.0;
    this.id = null;
    this.value = null;
    this.algorithmName = null;
  }

  /**
   * The tuple type
   */
  public Type getTupleType() {
    return this.tupleType;
  }

  /**
   * The tuple type
   */
  public ArgumentTuple setTupleType(Type tupleType) {
    this.tupleType = tupleType;
    return this;
  }

  public void unsetTupleType() {
    this.tupleType = null;
  }

  /** Returns true if field tupleType is set (has been assigned a value) and false otherwise */
  public boolean isSetTupleType() {
    return this.tupleType != null;
  }

  public void setTupleTypeIsSet(boolean value) {
    if (!value) {
      this.tupleType = null;
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
  public ArgumentTuple setAttributes(List<Type> attributes) {
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
  public ArgumentTuple setArguments(List<Argument> arguments) {
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
  public ArgumentTuple setConfidence(double confidence) {
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
  public ArgumentTuple setId(ID id) {
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
  public ArgumentTuple setValue(String value) {
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
  public ArgumentTuple setAlgorithmName(String algorithmName) {
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
    case TUPLE_TYPE:
      if (value == null) {
        unsetTupleType();
      } else {
        setTupleType((Type)value);
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
    case TUPLE_TYPE:
      return getTupleType();

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
    case TUPLE_TYPE:
      return isSetTupleType();
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
    if (that instanceof ArgumentTuple)
      return this.equals((ArgumentTuple)that);
    return false;
  }

  public boolean equals(ArgumentTuple that) {
    if (that == null)
      return false;

    boolean this_present_tupleType = true && this.isSetTupleType();
    boolean that_present_tupleType = true && that.isSetTupleType();
    if (this_present_tupleType || that_present_tupleType) {
      if (!(this_present_tupleType && that_present_tupleType))
        return false;
      if (!this.tupleType.equals(that.tupleType))
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

  public int compareTo(ArgumentTuple other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    ArgumentTuple typedOther = (ArgumentTuple)other;

    lastComparison = Boolean.valueOf(isSetTupleType()).compareTo(typedOther.isSetTupleType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTupleType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.tupleType, typedOther.tupleType);
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
    StringBuilder sb = new StringBuilder("ArgumentTuple(");
    boolean first = true;

    sb.append("tupleType:");
    if (this.tupleType == null) {
      sb.append("null");
    } else {
      sb.append(this.tupleType);
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
    if (tupleType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'tupleType' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (tupleType != null) {
      tupleType.validate();
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

  private static class ArgumentTupleStandardSchemeFactory implements SchemeFactory {
    public ArgumentTupleStandardScheme getScheme() {
      return new ArgumentTupleStandardScheme();
    }
  }

  private static class ArgumentTupleStandardScheme extends StandardScheme<ArgumentTuple> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, ArgumentTuple struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // TUPLE_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.tupleType = new Type();
              struct.tupleType.read(iprot);
              struct.setTupleTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ATTRIBUTES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list204 = iprot.readListBegin();
                struct.attributes = new ArrayList<Type>(_list204.size);
                for (int _i205 = 0; _i205 < _list204.size; ++_i205)
                {
                  Type _elem206; // required
                  _elem206 = new Type();
                  _elem206.read(iprot);
                  struct.attributes.add(_elem206);
                }
                iprot.readListEnd();
              }
              struct.setAttributesIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ARGUMENTS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list207 = iprot.readListBegin();
                struct.arguments = new ArrayList<Argument>(_list207.size);
                for (int _i208 = 0; _i208 < _list207.size; ++_i208)
                {
                  Argument _elem209; // required
                  _elem209 = new Argument();
                  _elem209.read(iprot);
                  struct.arguments.add(_elem209);
                }
                iprot.readListEnd();
              }
              struct.setArgumentsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // CONFIDENCE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.confidence = iprot.readDouble();
              struct.setConfidenceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.id = new ID();
              struct.id.read(iprot);
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.value = iprot.readString();
              struct.setValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // ALGORITHM_NAME
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
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, ArgumentTuple struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.tupleType != null) {
        oprot.writeFieldBegin(TUPLE_TYPE_FIELD_DESC);
        struct.tupleType.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.attributes != null) {
        if (struct.isSetAttributes()) {
          oprot.writeFieldBegin(ATTRIBUTES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.attributes.size()));
            for (Type _iter210 : struct.attributes)
            {
              _iter210.write(oprot);
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
            for (Argument _iter211 : struct.arguments)
            {
              _iter211.write(oprot);
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

  private static class ArgumentTupleTupleSchemeFactory implements SchemeFactory {
    public ArgumentTupleTupleScheme getScheme() {
      return new ArgumentTupleTupleScheme();
    }
  }

  private static class ArgumentTupleTupleScheme extends TupleScheme<ArgumentTuple> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, ArgumentTuple struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      struct.tupleType.write(oprot);
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
          for (Type _iter212 : struct.attributes)
          {
            _iter212.write(oprot);
          }
        }
      }
      if (struct.isSetArguments()) {
        {
          oprot.writeI32(struct.arguments.size());
          for (Argument _iter213 : struct.arguments)
          {
            _iter213.write(oprot);
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
    public void read(org.apache.thrift.protocol.TProtocol prot, ArgumentTuple struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.tupleType = new Type();
      struct.tupleType.read(iprot);
      struct.setTupleTypeIsSet(true);
      BitSet incoming = iprot.readBitSet(6);
      if (incoming.get(0)) {
        {
          org.apache.thrift.protocol.TList _list214 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.attributes = new ArrayList<Type>(_list214.size);
          for (int _i215 = 0; _i215 < _list214.size; ++_i215)
          {
            Type _elem216; // required
            _elem216 = new Type();
            _elem216.read(iprot);
            struct.attributes.add(_elem216);
          }
        }
        struct.setAttributesIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list217 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.arguments = new ArrayList<Argument>(_list217.size);
          for (int _i218 = 0; _i218 < _list217.size; ++_i218)
          {
            Argument _elem219; // required
            _elem219 = new Argument();
            _elem219.read(iprot);
            struct.arguments.add(_elem219);
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
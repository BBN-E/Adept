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
 * The Class Paraphrase.
 */
public class Paraphrase implements org.apache.thrift.TBase<Paraphrase, Paraphrase._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Paraphrase");

  private static final org.apache.thrift.protocol.TField VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("value", org.apache.thrift.protocol.TType.STRING, (short)1);
  private static final org.apache.thrift.protocol.TField CONFIDENCE_FIELD_DESC = new org.apache.thrift.protocol.TField("confidence", org.apache.thrift.protocol.TType.DOUBLE, (short)2);
  private static final org.apache.thrift.protocol.TField POS_TAG_FIELD_DESC = new org.apache.thrift.protocol.TField("posTag", org.apache.thrift.protocol.TType.STRUCT, (short)3);
  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.STRUCT, (short)4);
  private static final org.apache.thrift.protocol.TField ALGORITHM_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("algorithmName", org.apache.thrift.protocol.TType.STRING, (short)5);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ParaphraseStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ParaphraseTupleSchemeFactory());
  }

  /**
   * The value
   */
  public String value; // required
  /**
   * The confidence
   */
  public double confidence; // required
  /**
   * The part of speech tag
   */
  public Type posTag; // optional
  /**
   * The id
   */
  public ID id; // optional
  /**
   * The algorithmName
   */
  public String algorithmName; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * The value
     */
    VALUE((short)1, "value"),
    /**
     * The confidence
     */
    CONFIDENCE((short)2, "confidence"),
    /**
     * The part of speech tag
     */
    POS_TAG((short)3, "posTag"),
    /**
     * The id
     */
    ID((short)4, "id"),
    /**
     * The algorithmName
     */
    ALGORITHM_NAME((short)5, "algorithmName");

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
        case 1: // VALUE
          return VALUE;
        case 2: // CONFIDENCE
          return CONFIDENCE;
        case 3: // POS_TAG
          return POS_TAG;
        case 4: // ID
          return ID;
        case 5: // ALGORITHM_NAME
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
  private _Fields optionals[] = {_Fields.POS_TAG,_Fields.ID,_Fields.ALGORITHM_NAME};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.VALUE, new org.apache.thrift.meta_data.FieldMetaData("value", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.CONFIDENCE, new org.apache.thrift.meta_data.FieldMetaData("confidence", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.DOUBLE)));
    tmpMap.put(_Fields.POS_TAG, new org.apache.thrift.meta_data.FieldMetaData("posTag", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Type.class)));
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ID.class)));
    tmpMap.put(_Fields.ALGORITHM_NAME, new org.apache.thrift.meta_data.FieldMetaData("algorithmName", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Paraphrase.class, metaDataMap);
  }

  public Paraphrase() {
  }

  public Paraphrase(
    String value,
    double confidence)
  {
    this();
    this.value = value;
    this.confidence = confidence;
    setConfidenceIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Paraphrase(Paraphrase other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetValue()) {
      this.value = other.value;
    }
    this.confidence = other.confidence;
    if (other.isSetPosTag()) {
      this.posTag = new Type(other.posTag);
    }
    if (other.isSetId()) {
      this.id = new ID(other.id);
    }
    if (other.isSetAlgorithmName()) {
      this.algorithmName = other.algorithmName;
    }
  }

  public Paraphrase deepCopy() {
    return new Paraphrase(this);
  }

  @Override
  public void clear() {
    this.value = null;
    setConfidenceIsSet(false);
    this.confidence = 0.0;
    this.posTag = null;
    this.id = null;
    this.algorithmName = null;
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
  public Paraphrase setValue(String value) {
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
   * The confidence
   */
  public double getConfidence() {
    return this.confidence;
  }

  /**
   * The confidence
   */
  public Paraphrase setConfidence(double confidence) {
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
   * The part of speech tag
   */
  public Type getPosTag() {
    return this.posTag;
  }

  /**
   * The part of speech tag
   */
  public Paraphrase setPosTag(Type posTag) {
    this.posTag = posTag;
    return this;
  }

  public void unsetPosTag() {
    this.posTag = null;
  }

  /** Returns true if field posTag is set (has been assigned a value) and false otherwise */
  public boolean isSetPosTag() {
    return this.posTag != null;
  }

  public void setPosTagIsSet(boolean value) {
    if (!value) {
      this.posTag = null;
    }
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
  public Paraphrase setId(ID id) {
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
   * The algorithmName
   */
  public String getAlgorithmName() {
    return this.algorithmName;
  }

  /**
   * The algorithmName
   */
  public Paraphrase setAlgorithmName(String algorithmName) {
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
    case VALUE:
      if (value == null) {
        unsetValue();
      } else {
        setValue((String)value);
      }
      break;

    case CONFIDENCE:
      if (value == null) {
        unsetConfidence();
      } else {
        setConfidence((Double)value);
      }
      break;

    case POS_TAG:
      if (value == null) {
        unsetPosTag();
      } else {
        setPosTag((Type)value);
      }
      break;

    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((ID)value);
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
    case VALUE:
      return getValue();

    case CONFIDENCE:
      return Double.valueOf(getConfidence());

    case POS_TAG:
      return getPosTag();

    case ID:
      return getId();

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
    case VALUE:
      return isSetValue();
    case CONFIDENCE:
      return isSetConfidence();
    case POS_TAG:
      return isSetPosTag();
    case ID:
      return isSetId();
    case ALGORITHM_NAME:
      return isSetAlgorithmName();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Paraphrase)
      return this.equals((Paraphrase)that);
    return false;
  }

  public boolean equals(Paraphrase that) {
    if (that == null)
      return false;

    boolean this_present_value = true && this.isSetValue();
    boolean that_present_value = true && that.isSetValue();
    if (this_present_value || that_present_value) {
      if (!(this_present_value && that_present_value))
        return false;
      if (!this.value.equals(that.value))
        return false;
    }

    boolean this_present_confidence = true;
    boolean that_present_confidence = true;
    if (this_present_confidence || that_present_confidence) {
      if (!(this_present_confidence && that_present_confidence))
        return false;
      if (this.confidence != that.confidence)
        return false;
    }

    boolean this_present_posTag = true && this.isSetPosTag();
    boolean that_present_posTag = true && that.isSetPosTag();
    if (this_present_posTag || that_present_posTag) {
      if (!(this_present_posTag && that_present_posTag))
        return false;
      if (!this.posTag.equals(that.posTag))
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

  public int compareTo(Paraphrase other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Paraphrase typedOther = (Paraphrase)other;

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
    lastComparison = Boolean.valueOf(isSetPosTag()).compareTo(typedOther.isSetPosTag());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetPosTag()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.posTag, typedOther.posTag);
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
    StringBuilder sb = new StringBuilder("Paraphrase(");
    boolean first = true;

    sb.append("value:");
    if (this.value == null) {
      sb.append("null");
    } else {
      sb.append(this.value);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("confidence:");
    sb.append(this.confidence);
    first = false;
    if (isSetPosTag()) {
      if (!first) sb.append(", ");
      sb.append("posTag:");
      if (this.posTag == null) {
        sb.append("null");
      } else {
        sb.append(this.posTag);
      }
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
    if (value == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'value' was not present! Struct: " + toString());
    }
    // alas, we cannot check 'confidence' because it's a primitive and you chose the non-beans generator.
    // check for sub-struct validity
    if (posTag != null) {
      posTag.validate();
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

  private static class ParaphraseStandardSchemeFactory implements SchemeFactory {
    public ParaphraseStandardScheme getScheme() {
      return new ParaphraseStandardScheme();
    }
  }

  private static class ParaphraseStandardScheme extends StandardScheme<Paraphrase> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Paraphrase struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.value = iprot.readString();
              struct.setValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // CONFIDENCE
            if (schemeField.type == org.apache.thrift.protocol.TType.DOUBLE) {
              struct.confidence = iprot.readDouble();
              struct.setConfidenceIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // POS_TAG
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.posTag = new Type();
              struct.posTag.read(iprot);
              struct.setPosTagIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.id = new ID();
              struct.id.read(iprot);
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // ALGORITHM_NAME
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
      if (!struct.isSetConfidence()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'confidence' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Paraphrase struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.value != null) {
        oprot.writeFieldBegin(VALUE_FIELD_DESC);
        oprot.writeString(struct.value);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(CONFIDENCE_FIELD_DESC);
      oprot.writeDouble(struct.confidence);
      oprot.writeFieldEnd();
      if (struct.posTag != null) {
        if (struct.isSetPosTag()) {
          oprot.writeFieldBegin(POS_TAG_FIELD_DESC);
          struct.posTag.write(oprot);
          oprot.writeFieldEnd();
        }
      }
      if (struct.id != null) {
        if (struct.isSetId()) {
          oprot.writeFieldBegin(ID_FIELD_DESC);
          struct.id.write(oprot);
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

  private static class ParaphraseTupleSchemeFactory implements SchemeFactory {
    public ParaphraseTupleScheme getScheme() {
      return new ParaphraseTupleScheme();
    }
  }

  private static class ParaphraseTupleScheme extends TupleScheme<Paraphrase> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Paraphrase struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeString(struct.value);
      oprot.writeDouble(struct.confidence);
      BitSet optionals = new BitSet();
      if (struct.isSetPosTag()) {
        optionals.set(0);
      }
      if (struct.isSetId()) {
        optionals.set(1);
      }
      if (struct.isSetAlgorithmName()) {
        optionals.set(2);
      }
      oprot.writeBitSet(optionals, 3);
      if (struct.isSetPosTag()) {
        struct.posTag.write(oprot);
      }
      if (struct.isSetId()) {
        struct.id.write(oprot);
      }
      if (struct.isSetAlgorithmName()) {
        oprot.writeString(struct.algorithmName);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Paraphrase struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.value = iprot.readString();
      struct.setValueIsSet(true);
      struct.confidence = iprot.readDouble();
      struct.setConfidenceIsSet(true);
      BitSet incoming = iprot.readBitSet(3);
      if (incoming.get(0)) {
        struct.posTag = new Type();
        struct.posTag.read(iprot);
        struct.setPosTagIsSet(true);
      }
      if (incoming.get(1)) {
        struct.id = new ID();
        struct.id.read(iprot);
        struct.setIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.algorithmName = iprot.readString();
        struct.setAlgorithmNameIsSet(true);
      }
    }
  }

}
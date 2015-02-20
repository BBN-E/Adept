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
 * The Class DocumentList.
 */
public class DocumentList implements org.apache.thrift.TBase<DocumentList, DocumentList._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("DocumentList");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.STRUCT, (short)1);
  private static final org.apache.thrift.protocol.TField SERIAL_VERSION_UID_FIELD_DESC = new org.apache.thrift.protocol.TField("serialVersionUID", org.apache.thrift.protocol.TType.I64, (short)2);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new DocumentListStandardSchemeFactory());
    schemes.put(TupleScheme.class, new DocumentListTupleSchemeFactory());
  }

  /**
   * The id
   */
  public ID id; // required
  /**
   * The Constant serialVersionUID
   */
  public long serialVersionUID; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * The id
     */
    ID((short)1, "id"),
    /**
     * The Constant serialVersionUID
     */
    SERIAL_VERSION_UID((short)2, "serialVersionUID");

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
        case 1: // ID
          return ID;
        case 2: // SERIAL_VERSION_UID
          return SERIAL_VERSION_UID;
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
  private static final int __SERIALVERSIONUID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ID.class)));
    tmpMap.put(_Fields.SERIAL_VERSION_UID, new org.apache.thrift.meta_data.FieldMetaData("serialVersionUID", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(DocumentList.class, metaDataMap);
  }

  public DocumentList() {
    this.serialVersionUID = 651655831447893195L;

  }

  public DocumentList(
    ID id,
    long serialVersionUID)
  {
    this();
    this.id = id;
    this.serialVersionUID = serialVersionUID;
    setSerialVersionUIDIsSet(true);
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public DocumentList(DocumentList other) {
    __isset_bitfield = other.__isset_bitfield;
    if (other.isSetId()) {
      this.id = new ID(other.id);
    }
    this.serialVersionUID = other.serialVersionUID;
  }

  public DocumentList deepCopy() {
    return new DocumentList(this);
  }

  @Override
  public void clear() {
    this.id = null;
    this.serialVersionUID = 651655831447893195L;

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
  public DocumentList setId(ID id) {
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
   * The Constant serialVersionUID
   */
  public long getSerialVersionUID() {
    return this.serialVersionUID;
  }

  /**
   * The Constant serialVersionUID
   */
  public DocumentList setSerialVersionUID(long serialVersionUID) {
    this.serialVersionUID = serialVersionUID;
    setSerialVersionUIDIsSet(true);
    return this;
  }

  public void unsetSerialVersionUID() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __SERIALVERSIONUID_ISSET_ID);
  }

  /** Returns true if field serialVersionUID is set (has been assigned a value) and false otherwise */
  public boolean isSetSerialVersionUID() {
    return EncodingUtils.testBit(__isset_bitfield, __SERIALVERSIONUID_ISSET_ID);
  }

  public void setSerialVersionUIDIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __SERIALVERSIONUID_ISSET_ID, value);
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((ID)value);
      }
      break;

    case SERIAL_VERSION_UID:
      if (value == null) {
        unsetSerialVersionUID();
      } else {
        setSerialVersionUID((Long)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return getId();

    case SERIAL_VERSION_UID:
      return Long.valueOf(getSerialVersionUID());

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ID:
      return isSetId();
    case SERIAL_VERSION_UID:
      return isSetSerialVersionUID();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof DocumentList)
      return this.equals((DocumentList)that);
    return false;
  }

  public boolean equals(DocumentList that) {
    if (that == null)
      return false;

    boolean this_present_id = true && this.isSetId();
    boolean that_present_id = true && that.isSetId();
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (!this.id.equals(that.id))
        return false;
    }

    boolean this_present_serialVersionUID = true;
    boolean that_present_serialVersionUID = true;
    if (this_present_serialVersionUID || that_present_serialVersionUID) {
      if (!(this_present_serialVersionUID && that_present_serialVersionUID))
        return false;
      if (this.serialVersionUID != that.serialVersionUID)
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(DocumentList other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    DocumentList typedOther = (DocumentList)other;

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
    lastComparison = Boolean.valueOf(isSetSerialVersionUID()).compareTo(typedOther.isSetSerialVersionUID());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetSerialVersionUID()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.serialVersionUID, typedOther.serialVersionUID);
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
    StringBuilder sb = new StringBuilder("DocumentList(");
    boolean first = true;

    sb.append("id:");
    if (this.id == null) {
      sb.append("null");
    } else {
      sb.append(this.id);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("serialVersionUID:");
    sb.append(this.serialVersionUID);
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
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

  private static class DocumentListStandardSchemeFactory implements SchemeFactory {
    public DocumentListStandardScheme getScheme() {
      return new DocumentListStandardScheme();
    }
  }

  private static class DocumentListStandardScheme extends StandardScheme<DocumentList> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, DocumentList struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.id = new ID();
              struct.id.read(iprot);
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // SERIAL_VERSION_UID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.serialVersionUID = iprot.readI64();
              struct.setSerialVersionUIDIsSet(true);
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

    public void write(org.apache.thrift.protocol.TProtocol oprot, DocumentList struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      if (struct.id != null) {
        oprot.writeFieldBegin(ID_FIELD_DESC);
        struct.id.write(oprot);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(SERIAL_VERSION_UID_FIELD_DESC);
      oprot.writeI64(struct.serialVersionUID);
      oprot.writeFieldEnd();
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class DocumentListTupleSchemeFactory implements SchemeFactory {
    public DocumentListTupleScheme getScheme() {
      return new DocumentListTupleScheme();
    }
  }

  private static class DocumentListTupleScheme extends TupleScheme<DocumentList> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, DocumentList struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetId()) {
        optionals.set(0);
      }
      if (struct.isSetSerialVersionUID()) {
        optionals.set(1);
      }
      oprot.writeBitSet(optionals, 2);
      if (struct.isSetId()) {
        struct.id.write(oprot);
      }
      if (struct.isSetSerialVersionUID()) {
        oprot.writeI64(struct.serialVersionUID);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, DocumentList struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(2);
      if (incoming.get(0)) {
        struct.id = new ID();
        struct.id.read(iprot);
        struct.setIdIsSet(true);
      }
      if (incoming.get(1)) {
        struct.serialVersionUID = iprot.readI64();
        struct.setSerialVersionUIDIsSet(true);
      }
    }
  }

}
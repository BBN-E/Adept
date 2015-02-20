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
 * The Class Entity is represented by a globally unique ID and a canonical
 * mention. The argumentConfidenceMap in ResolvedMention provides a distribution
 * over possible entities for a given Mention
 */
public class Entity implements org.apache.thrift.TBase<Entity, Entity._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Entity");

  private static final org.apache.thrift.protocol.TField ENTITY_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("entityId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField ENTITY_TYPE_FIELD_DESC = new org.apache.thrift.protocol.TField("entityType", org.apache.thrift.protocol.TType.STRUCT, (short)2);
  private static final org.apache.thrift.protocol.TField CANONICAL_MENTION_FIELD_DESC = new org.apache.thrift.protocol.TField("canonicalMention", org.apache.thrift.protocol.TType.STRUCT, (short)3);
  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.STRUCT, (short)4);
  private static final org.apache.thrift.protocol.TField VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("value", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField ALGORITHM_NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("algorithmName", org.apache.thrift.protocol.TType.STRING, (short)6);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new EntityStandardSchemeFactory());
    schemes.put(TupleScheme.class, new EntityTupleSchemeFactory());
  }

  /**
   * The entity id
   */
  public long entityId; // required
  /**
   * The entity type
   */
  public Type entityType; // required
  /**
   * The canonical mention
   */
  public EntityMention canonicalMention; // optional
  /**
   * The id
   */
  public ID id; // optional
  /**
   * The value
   */
  public String value; // optional
  /**
   * The algorithmName
   */
  public String algorithmName; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    /**
     * The entity id
     */
    ENTITY_ID((short)1, "entityId"),
    /**
     * The entity type
     */
    ENTITY_TYPE((short)2, "entityType"),
    /**
     * The canonical mention
     */
    CANONICAL_MENTION((short)3, "canonicalMention"),
    /**
     * The id
     */
    ID((short)4, "id"),
    /**
     * The value
     */
    VALUE((short)5, "value"),
    /**
     * The algorithmName
     */
    ALGORITHM_NAME((short)6, "algorithmName");

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
        case 1: // ENTITY_ID
          return ENTITY_ID;
        case 2: // ENTITY_TYPE
          return ENTITY_TYPE;
        case 3: // CANONICAL_MENTION
          return CANONICAL_MENTION;
        case 4: // ID
          return ID;
        case 5: // VALUE
          return VALUE;
        case 6: // ALGORITHM_NAME
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
  private static final int __ENTITYID_ISSET_ID = 0;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.CANONICAL_MENTION,_Fields.ID,_Fields.VALUE,_Fields.ALGORITHM_NAME};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ENTITY_ID, new org.apache.thrift.meta_data.FieldMetaData("entityId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.ENTITY_TYPE, new org.apache.thrift.meta_data.FieldMetaData("entityType", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Type.class)));
    tmpMap.put(_Fields.CANONICAL_MENTION, new org.apache.thrift.meta_data.FieldMetaData("canonicalMention", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, EntityMention.class)));
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ID.class)));
    tmpMap.put(_Fields.VALUE, new org.apache.thrift.meta_data.FieldMetaData("value", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ALGORITHM_NAME, new org.apache.thrift.meta_data.FieldMetaData("algorithmName", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Entity.class, metaDataMap);
  }

  public Entity() {
  }

  public Entity(
    long entityId,
    Type entityType)
  {
    this();
    this.entityId = entityId;
    setEntityIdIsSet(true);
    this.entityType = entityType;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Entity(Entity other) {
    __isset_bitfield = other.__isset_bitfield;
    this.entityId = other.entityId;
    if (other.isSetEntityType()) {
      this.entityType = new Type(other.entityType);
    }
    if (other.isSetCanonicalMention()) {
      this.canonicalMention = new EntityMention(other.canonicalMention);
    }
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

  public Entity deepCopy() {
    return new Entity(this);
  }

  @Override
  public void clear() {
    setEntityIdIsSet(false);
    this.entityId = 0;
    this.entityType = null;
    this.canonicalMention = null;
    this.id = null;
    this.value = null;
    this.algorithmName = null;
  }

  /**
   * The entity id
   */
  public long getEntityId() {
    return this.entityId;
  }

  /**
   * The entity id
   */
  public Entity setEntityId(long entityId) {
    this.entityId = entityId;
    setEntityIdIsSet(true);
    return this;
  }

  public void unsetEntityId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ENTITYID_ISSET_ID);
  }

  /** Returns true if field entityId is set (has been assigned a value) and false otherwise */
  public boolean isSetEntityId() {
    return EncodingUtils.testBit(__isset_bitfield, __ENTITYID_ISSET_ID);
  }

  public void setEntityIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ENTITYID_ISSET_ID, value);
  }

  /**
   * The entity type
   */
  public Type getEntityType() {
    return this.entityType;
  }

  /**
   * The entity type
   */
  public Entity setEntityType(Type entityType) {
    this.entityType = entityType;
    return this;
  }

  public void unsetEntityType() {
    this.entityType = null;
  }

  /** Returns true if field entityType is set (has been assigned a value) and false otherwise */
  public boolean isSetEntityType() {
    return this.entityType != null;
  }

  public void setEntityTypeIsSet(boolean value) {
    if (!value) {
      this.entityType = null;
    }
  }

  /**
   * The canonical mention
   */
  public EntityMention getCanonicalMention() {
    return this.canonicalMention;
  }

  /**
   * The canonical mention
   */
  public Entity setCanonicalMention(EntityMention canonicalMention) {
    this.canonicalMention = canonicalMention;
    return this;
  }

  public void unsetCanonicalMention() {
    this.canonicalMention = null;
  }

  /** Returns true if field canonicalMention is set (has been assigned a value) and false otherwise */
  public boolean isSetCanonicalMention() {
    return this.canonicalMention != null;
  }

  public void setCanonicalMentionIsSet(boolean value) {
    if (!value) {
      this.canonicalMention = null;
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
  public Entity setId(ID id) {
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
  public Entity setValue(String value) {
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
   * The algorithmName
   */
  public String getAlgorithmName() {
    return this.algorithmName;
  }

  /**
   * The algorithmName
   */
  public Entity setAlgorithmName(String algorithmName) {
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
    case ENTITY_ID:
      if (value == null) {
        unsetEntityId();
      } else {
        setEntityId((Long)value);
      }
      break;

    case ENTITY_TYPE:
      if (value == null) {
        unsetEntityType();
      } else {
        setEntityType((Type)value);
      }
      break;

    case CANONICAL_MENTION:
      if (value == null) {
        unsetCanonicalMention();
      } else {
        setCanonicalMention((EntityMention)value);
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
    case ENTITY_ID:
      return Long.valueOf(getEntityId());

    case ENTITY_TYPE:
      return getEntityType();

    case CANONICAL_MENTION:
      return getCanonicalMention();

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
    case ENTITY_ID:
      return isSetEntityId();
    case ENTITY_TYPE:
      return isSetEntityType();
    case CANONICAL_MENTION:
      return isSetCanonicalMention();
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
    if (that instanceof Entity)
      return this.equals((Entity)that);
    return false;
  }

  public boolean equals(Entity that) {
    if (that == null)
      return false;

    boolean this_present_entityId = true;
    boolean that_present_entityId = true;
    if (this_present_entityId || that_present_entityId) {
      if (!(this_present_entityId && that_present_entityId))
        return false;
      if (this.entityId != that.entityId)
        return false;
    }

    boolean this_present_entityType = true && this.isSetEntityType();
    boolean that_present_entityType = true && that.isSetEntityType();
    if (this_present_entityType || that_present_entityType) {
      if (!(this_present_entityType && that_present_entityType))
        return false;
      if (!this.entityType.equals(that.entityType))
        return false;
    }

    boolean this_present_canonicalMention = true && this.isSetCanonicalMention();
    boolean that_present_canonicalMention = true && that.isSetCanonicalMention();
    if (this_present_canonicalMention || that_present_canonicalMention) {
      if (!(this_present_canonicalMention && that_present_canonicalMention))
        return false;
      if (!this.canonicalMention.equals(that.canonicalMention))
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

  public int compareTo(Entity other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Entity typedOther = (Entity)other;

    lastComparison = Boolean.valueOf(isSetEntityId()).compareTo(typedOther.isSetEntityId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntityId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entityId, typedOther.entityId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetEntityType()).compareTo(typedOther.isSetEntityType());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetEntityType()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.entityType, typedOther.entityType);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCanonicalMention()).compareTo(typedOther.isSetCanonicalMention());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCanonicalMention()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.canonicalMention, typedOther.canonicalMention);
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
    StringBuilder sb = new StringBuilder("Entity(");
    boolean first = true;

    sb.append("entityId:");
    sb.append(this.entityId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("entityType:");
    if (this.entityType == null) {
      sb.append("null");
    } else {
      sb.append(this.entityType);
    }
    first = false;
    if (isSetCanonicalMention()) {
      if (!first) sb.append(", ");
      sb.append("canonicalMention:");
      if (this.canonicalMention == null) {
        sb.append("null");
      } else {
        sb.append(this.canonicalMention);
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
    // alas, we cannot check 'entityId' because it's a primitive and you chose the non-beans generator.
    if (entityType == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'entityType' was not present! Struct: " + toString());
    }
    // check for sub-struct validity
    if (entityType != null) {
      entityType.validate();
    }
    if (canonicalMention != null) {
      canonicalMention.validate();
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

  private static class EntityStandardSchemeFactory implements SchemeFactory {
    public EntityStandardScheme getScheme() {
      return new EntityStandardScheme();
    }
  }

  private static class EntityStandardScheme extends StandardScheme<Entity> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Entity struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ENTITY_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.entityId = iprot.readI64();
              struct.setEntityIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // ENTITY_TYPE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.entityType = new Type();
              struct.entityType.read(iprot);
              struct.setEntityTypeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // CANONICAL_MENTION
            if (schemeField.type == org.apache.thrift.protocol.TType.STRUCT) {
              struct.canonicalMention = new EntityMention();
              struct.canonicalMention.read(iprot);
              struct.setCanonicalMentionIsSet(true);
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
          case 5: // VALUE
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.value = iprot.readString();
              struct.setValueIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // ALGORITHM_NAME
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
      if (!struct.isSetEntityId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'entityId' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Entity struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ENTITY_ID_FIELD_DESC);
      oprot.writeI64(struct.entityId);
      oprot.writeFieldEnd();
      if (struct.entityType != null) {
        oprot.writeFieldBegin(ENTITY_TYPE_FIELD_DESC);
        struct.entityType.write(oprot);
        oprot.writeFieldEnd();
      }
      if (struct.canonicalMention != null) {
        if (struct.isSetCanonicalMention()) {
          oprot.writeFieldBegin(CANONICAL_MENTION_FIELD_DESC);
          struct.canonicalMention.write(oprot);
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

  private static class EntityTupleSchemeFactory implements SchemeFactory {
    public EntityTupleScheme getScheme() {
      return new EntityTupleScheme();
    }
  }

  private static class EntityTupleScheme extends TupleScheme<Entity> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Entity struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.entityId);
      struct.entityType.write(oprot);
      BitSet optionals = new BitSet();
      if (struct.isSetCanonicalMention()) {
        optionals.set(0);
      }
      if (struct.isSetId()) {
        optionals.set(1);
      }
      if (struct.isSetValue()) {
        optionals.set(2);
      }
      if (struct.isSetAlgorithmName()) {
        optionals.set(3);
      }
      oprot.writeBitSet(optionals, 4);
      if (struct.isSetCanonicalMention()) {
        struct.canonicalMention.write(oprot);
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
    public void read(org.apache.thrift.protocol.TProtocol prot, Entity struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.entityId = iprot.readI64();
      struct.setEntityIdIsSet(true);
      struct.entityType = new Type();
      struct.entityType.read(iprot);
      struct.setEntityTypeIsSet(true);
      BitSet incoming = iprot.readBitSet(4);
      if (incoming.get(0)) {
        struct.canonicalMention = new EntityMention();
        struct.canonicalMention.read(iprot);
        struct.setCanonicalMentionIsSet(true);
      }
      if (incoming.get(1)) {
        struct.id = new ID();
        struct.id.read(iprot);
        struct.setIdIsSet(true);
      }
      if (incoming.get(2)) {
        struct.value = iprot.readString();
        struct.setValueIsSet(true);
      }
      if (incoming.get(3)) {
        struct.algorithmName = iprot.readString();
        struct.setAlgorithmNameIsSet(true);
      }
    }
  }

}
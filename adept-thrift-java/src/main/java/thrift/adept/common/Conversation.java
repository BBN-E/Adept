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

public class Conversation implements org.apache.thrift.TBase<Conversation, Conversation._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("Conversation");

  private static final org.apache.thrift.protocol.TField CONVERSATION_ID_FIELD_DESC = new org.apache.thrift.protocol.TField("conversationId", org.apache.thrift.protocol.TType.I64, (short)1);
  private static final org.apache.thrift.protocol.TField NAME_FIELD_DESC = new org.apache.thrift.protocol.TField("name", org.apache.thrift.protocol.TType.STRING, (short)2);
  private static final org.apache.thrift.protocol.TField ONE_SIDED_FIELD_DESC = new org.apache.thrift.protocol.TField("oneSided", org.apache.thrift.protocol.TType.BOOL, (short)3);
  private static final org.apache.thrift.protocol.TField TOPICS_FIELD_DESC = new org.apache.thrift.protocol.TField("topics", org.apache.thrift.protocol.TType.LIST, (short)4);
  private static final org.apache.thrift.protocol.TField UTTERANCES_FIELD_DESC = new org.apache.thrift.protocol.TField("utterances", org.apache.thrift.protocol.TType.LIST, (short)5);
  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.STRUCT, (short)6);
  private static final org.apache.thrift.protocol.TField VALUE_FIELD_DESC = new org.apache.thrift.protocol.TField("value", org.apache.thrift.protocol.TType.STRING, (short)7);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new ConversationStandardSchemeFactory());
    schemes.put(TupleScheme.class, new ConversationTupleSchemeFactory());
  }

  public long conversationId; // required
  public String name; // required
  public boolean oneSided; // optional
  public List<Topic> topics; // optional
  public List<Utterance> utterances; // optional
  /**
   * The id
   */
  public ID id; // optional
  /**
   * The value
   */
  public String value; // optional

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    CONVERSATION_ID((short)1, "conversationId"),
    NAME((short)2, "name"),
    ONE_SIDED((short)3, "oneSided"),
    TOPICS((short)4, "topics"),
    UTTERANCES((short)5, "utterances"),
    /**
     * The id
     */
    ID((short)6, "id"),
    /**
     * The value
     */
    VALUE((short)7, "value");

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
        case 1: // CONVERSATION_ID
          return CONVERSATION_ID;
        case 2: // NAME
          return NAME;
        case 3: // ONE_SIDED
          return ONE_SIDED;
        case 4: // TOPICS
          return TOPICS;
        case 5: // UTTERANCES
          return UTTERANCES;
        case 6: // ID
          return ID;
        case 7: // VALUE
          return VALUE;
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
  private static final int __CONVERSATIONID_ISSET_ID = 0;
  private static final int __ONESIDED_ISSET_ID = 1;
  private byte __isset_bitfield = 0;
  private _Fields optionals[] = {_Fields.ONE_SIDED,_Fields.TOPICS,_Fields.UTTERANCES,_Fields.ID,_Fields.VALUE};
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.CONVERSATION_ID, new org.apache.thrift.meta_data.FieldMetaData("conversationId", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I64)));
    tmpMap.put(_Fields.NAME, new org.apache.thrift.meta_data.FieldMetaData("name", org.apache.thrift.TFieldRequirementType.REQUIRED, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.ONE_SIDED, new org.apache.thrift.meta_data.FieldMetaData("oneSided", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.BOOL)));
    tmpMap.put(_Fields.TOPICS, new org.apache.thrift.meta_data.FieldMetaData("topics", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Topic.class))));
    tmpMap.put(_Fields.UTTERANCES, new org.apache.thrift.meta_data.FieldMetaData("utterances", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, Utterance.class))));
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, ID.class)));
    tmpMap.put(_Fields.VALUE, new org.apache.thrift.meta_data.FieldMetaData("value", org.apache.thrift.TFieldRequirementType.OPTIONAL, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(Conversation.class, metaDataMap);
  }

  public Conversation() {
  }

  public Conversation(
    long conversationId,
    String name)
  {
    this();
    this.conversationId = conversationId;
    setConversationIdIsSet(true);
    this.name = name;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public Conversation(Conversation other) {
    __isset_bitfield = other.__isset_bitfield;
    this.conversationId = other.conversationId;
    if (other.isSetName()) {
      this.name = other.name;
    }
    this.oneSided = other.oneSided;
    if (other.isSetTopics()) {
      List<Topic> __this__topics = new ArrayList<Topic>();
      for (Topic other_element : other.topics) {
        __this__topics.add(new Topic(other_element));
      }
      this.topics = __this__topics;
    }
    if (other.isSetUtterances()) {
      List<Utterance> __this__utterances = new ArrayList<Utterance>();
      for (Utterance other_element : other.utterances) {
        __this__utterances.add(new Utterance(other_element));
      }
      this.utterances = __this__utterances;
    }
    if (other.isSetId()) {
      this.id = new ID(other.id);
    }
    if (other.isSetValue()) {
      this.value = other.value;
    }
  }

  public Conversation deepCopy() {
    return new Conversation(this);
  }

  @Override
  public void clear() {
    setConversationIdIsSet(false);
    this.conversationId = 0;
    this.name = null;
    setOneSidedIsSet(false);
    this.oneSided = false;
    this.topics = null;
    this.utterances = null;
    this.id = null;
    this.value = null;
  }

  public long getConversationId() {
    return this.conversationId;
  }

  public Conversation setConversationId(long conversationId) {
    this.conversationId = conversationId;
    setConversationIdIsSet(true);
    return this;
  }

  public void unsetConversationId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CONVERSATIONID_ISSET_ID);
  }

  /** Returns true if field conversationId is set (has been assigned a value) and false otherwise */
  public boolean isSetConversationId() {
    return EncodingUtils.testBit(__isset_bitfield, __CONVERSATIONID_ISSET_ID);
  }

  public void setConversationIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CONVERSATIONID_ISSET_ID, value);
  }

  public String getName() {
    return this.name;
  }

  public Conversation setName(String name) {
    this.name = name;
    return this;
  }

  public void unsetName() {
    this.name = null;
  }

  /** Returns true if field name is set (has been assigned a value) and false otherwise */
  public boolean isSetName() {
    return this.name != null;
  }

  public void setNameIsSet(boolean value) {
    if (!value) {
      this.name = null;
    }
  }

  public boolean isOneSided() {
    return this.oneSided;
  }

  public Conversation setOneSided(boolean oneSided) {
    this.oneSided = oneSided;
    setOneSidedIsSet(true);
    return this;
  }

  public void unsetOneSided() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ONESIDED_ISSET_ID);
  }

  /** Returns true if field oneSided is set (has been assigned a value) and false otherwise */
  public boolean isSetOneSided() {
    return EncodingUtils.testBit(__isset_bitfield, __ONESIDED_ISSET_ID);
  }

  public void setOneSidedIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ONESIDED_ISSET_ID, value);
  }

  public int getTopicsSize() {
    return (this.topics == null) ? 0 : this.topics.size();
  }

  public java.util.Iterator<Topic> getTopicsIterator() {
    return (this.topics == null) ? null : this.topics.iterator();
  }

  public void addToTopics(Topic elem) {
    if (this.topics == null) {
      this.topics = new ArrayList<Topic>();
    }
    this.topics.add(elem);
  }

  public List<Topic> getTopics() {
    return this.topics;
  }

  public Conversation setTopics(List<Topic> topics) {
    this.topics = topics;
    return this;
  }

  public void unsetTopics() {
    this.topics = null;
  }

  /** Returns true if field topics is set (has been assigned a value) and false otherwise */
  public boolean isSetTopics() {
    return this.topics != null;
  }

  public void setTopicsIsSet(boolean value) {
    if (!value) {
      this.topics = null;
    }
  }

  public int getUtterancesSize() {
    return (this.utterances == null) ? 0 : this.utterances.size();
  }

  public java.util.Iterator<Utterance> getUtterancesIterator() {
    return (this.utterances == null) ? null : this.utterances.iterator();
  }

  public void addToUtterances(Utterance elem) {
    if (this.utterances == null) {
      this.utterances = new ArrayList<Utterance>();
    }
    this.utterances.add(elem);
  }

  public List<Utterance> getUtterances() {
    return this.utterances;
  }

  public Conversation setUtterances(List<Utterance> utterances) {
    this.utterances = utterances;
    return this;
  }

  public void unsetUtterances() {
    this.utterances = null;
  }

  /** Returns true if field utterances is set (has been assigned a value) and false otherwise */
  public boolean isSetUtterances() {
    return this.utterances != null;
  }

  public void setUtterancesIsSet(boolean value) {
    if (!value) {
      this.utterances = null;
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
  public Conversation setId(ID id) {
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
  public Conversation setValue(String value) {
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

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case CONVERSATION_ID:
      if (value == null) {
        unsetConversationId();
      } else {
        setConversationId((Long)value);
      }
      break;

    case NAME:
      if (value == null) {
        unsetName();
      } else {
        setName((String)value);
      }
      break;

    case ONE_SIDED:
      if (value == null) {
        unsetOneSided();
      } else {
        setOneSided((Boolean)value);
      }
      break;

    case TOPICS:
      if (value == null) {
        unsetTopics();
      } else {
        setTopics((List<Topic>)value);
      }
      break;

    case UTTERANCES:
      if (value == null) {
        unsetUtterances();
      } else {
        setUtterances((List<Utterance>)value);
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

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case CONVERSATION_ID:
      return Long.valueOf(getConversationId());

    case NAME:
      return getName();

    case ONE_SIDED:
      return Boolean.valueOf(isOneSided());

    case TOPICS:
      return getTopics();

    case UTTERANCES:
      return getUtterances();

    case ID:
      return getId();

    case VALUE:
      return getValue();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case CONVERSATION_ID:
      return isSetConversationId();
    case NAME:
      return isSetName();
    case ONE_SIDED:
      return isSetOneSided();
    case TOPICS:
      return isSetTopics();
    case UTTERANCES:
      return isSetUtterances();
    case ID:
      return isSetId();
    case VALUE:
      return isSetValue();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof Conversation)
      return this.equals((Conversation)that);
    return false;
  }

  public boolean equals(Conversation that) {
    if (that == null)
      return false;

    boolean this_present_conversationId = true;
    boolean that_present_conversationId = true;
    if (this_present_conversationId || that_present_conversationId) {
      if (!(this_present_conversationId && that_present_conversationId))
        return false;
      if (this.conversationId != that.conversationId)
        return false;
    }

    boolean this_present_name = true && this.isSetName();
    boolean that_present_name = true && that.isSetName();
    if (this_present_name || that_present_name) {
      if (!(this_present_name && that_present_name))
        return false;
      if (!this.name.equals(that.name))
        return false;
    }

    boolean this_present_oneSided = true && this.isSetOneSided();
    boolean that_present_oneSided = true && that.isSetOneSided();
    if (this_present_oneSided || that_present_oneSided) {
      if (!(this_present_oneSided && that_present_oneSided))
        return false;
      if (this.oneSided != that.oneSided)
        return false;
    }

    boolean this_present_topics = true && this.isSetTopics();
    boolean that_present_topics = true && that.isSetTopics();
    if (this_present_topics || that_present_topics) {
      if (!(this_present_topics && that_present_topics))
        return false;
      if (!this.topics.equals(that.topics))
        return false;
    }

    boolean this_present_utterances = true && this.isSetUtterances();
    boolean that_present_utterances = true && that.isSetUtterances();
    if (this_present_utterances || that_present_utterances) {
      if (!(this_present_utterances && that_present_utterances))
        return false;
      if (!this.utterances.equals(that.utterances))
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

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(Conversation other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    Conversation typedOther = (Conversation)other;

    lastComparison = Boolean.valueOf(isSetConversationId()).compareTo(typedOther.isSetConversationId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetConversationId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.conversationId, typedOther.conversationId);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetName()).compareTo(typedOther.isSetName());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetName()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.name, typedOther.name);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetOneSided()).compareTo(typedOther.isSetOneSided());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetOneSided()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.oneSided, typedOther.oneSided);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetTopics()).compareTo(typedOther.isSetTopics());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetTopics()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.topics, typedOther.topics);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetUtterances()).compareTo(typedOther.isSetUtterances());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetUtterances()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.utterances, typedOther.utterances);
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
    StringBuilder sb = new StringBuilder("Conversation(");
    boolean first = true;

    sb.append("conversationId:");
    sb.append(this.conversationId);
    first = false;
    if (!first) sb.append(", ");
    sb.append("name:");
    if (this.name == null) {
      sb.append("null");
    } else {
      sb.append(this.name);
    }
    first = false;
    if (isSetOneSided()) {
      if (!first) sb.append(", ");
      sb.append("oneSided:");
      sb.append(this.oneSided);
      first = false;
    }
    if (isSetTopics()) {
      if (!first) sb.append(", ");
      sb.append("topics:");
      if (this.topics == null) {
        sb.append("null");
      } else {
        sb.append(this.topics);
      }
      first = false;
    }
    if (isSetUtterances()) {
      if (!first) sb.append(", ");
      sb.append("utterances:");
      if (this.utterances == null) {
        sb.append("null");
      } else {
        sb.append(this.utterances);
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
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // alas, we cannot check 'conversationId' because it's a primitive and you chose the non-beans generator.
    if (name == null) {
      throw new org.apache.thrift.protocol.TProtocolException("Required field 'name' was not present! Struct: " + toString());
    }
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

  private static class ConversationStandardSchemeFactory implements SchemeFactory {
    public ConversationStandardScheme getScheme() {
      return new ConversationStandardScheme();
    }
  }

  private static class ConversationStandardScheme extends StandardScheme<Conversation> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, Conversation struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // CONVERSATION_ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I64) {
              struct.conversationId = iprot.readI64();
              struct.setConversationIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // NAME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.name = iprot.readString();
              struct.setNameIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // ONE_SIDED
            if (schemeField.type == org.apache.thrift.protocol.TType.BOOL) {
              struct.oneSided = iprot.readBool();
              struct.setOneSidedIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // TOPICS
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list178 = iprot.readListBegin();
                struct.topics = new ArrayList<Topic>(_list178.size);
                for (int _i179 = 0; _i179 < _list178.size; ++_i179)
                {
                  Topic _elem180; // required
                  _elem180 = new Topic();
                  _elem180.read(iprot);
                  struct.topics.add(_elem180);
                }
                iprot.readListEnd();
              }
              struct.setTopicsIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // UTTERANCES
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list181 = iprot.readListBegin();
                struct.utterances = new ArrayList<Utterance>(_list181.size);
                for (int _i182 = 0; _i182 < _list181.size; ++_i182)
                {
                  Utterance _elem183; // required
                  _elem183 = new Utterance();
                  _elem183.read(iprot);
                  struct.utterances.add(_elem183);
                }
                iprot.readListEnd();
              }
              struct.setUtterancesIsSet(true);
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
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      if (!struct.isSetConversationId()) {
        throw new org.apache.thrift.protocol.TProtocolException("Required field 'conversationId' was not found in serialized data! Struct: " + toString());
      }
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, Conversation struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(CONVERSATION_ID_FIELD_DESC);
      oprot.writeI64(struct.conversationId);
      oprot.writeFieldEnd();
      if (struct.name != null) {
        oprot.writeFieldBegin(NAME_FIELD_DESC);
        oprot.writeString(struct.name);
        oprot.writeFieldEnd();
      }
      if (struct.isSetOneSided()) {
        oprot.writeFieldBegin(ONE_SIDED_FIELD_DESC);
        oprot.writeBool(struct.oneSided);
        oprot.writeFieldEnd();
      }
      if (struct.topics != null) {
        if (struct.isSetTopics()) {
          oprot.writeFieldBegin(TOPICS_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.topics.size()));
            for (Topic _iter184 : struct.topics)
            {
              _iter184.write(oprot);
            }
            oprot.writeListEnd();
          }
          oprot.writeFieldEnd();
        }
      }
      if (struct.utterances != null) {
        if (struct.isSetUtterances()) {
          oprot.writeFieldBegin(UTTERANCES_FIELD_DESC);
          {
            oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.utterances.size()));
            for (Utterance _iter185 : struct.utterances)
            {
              _iter185.write(oprot);
            }
            oprot.writeListEnd();
          }
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
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class ConversationTupleSchemeFactory implements SchemeFactory {
    public ConversationTupleScheme getScheme() {
      return new ConversationTupleScheme();
    }
  }

  private static class ConversationTupleScheme extends TupleScheme<Conversation> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, Conversation struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      oprot.writeI64(struct.conversationId);
      oprot.writeString(struct.name);
      BitSet optionals = new BitSet();
      if (struct.isSetOneSided()) {
        optionals.set(0);
      }
      if (struct.isSetTopics()) {
        optionals.set(1);
      }
      if (struct.isSetUtterances()) {
        optionals.set(2);
      }
      if (struct.isSetId()) {
        optionals.set(3);
      }
      if (struct.isSetValue()) {
        optionals.set(4);
      }
      oprot.writeBitSet(optionals, 5);
      if (struct.isSetOneSided()) {
        oprot.writeBool(struct.oneSided);
      }
      if (struct.isSetTopics()) {
        {
          oprot.writeI32(struct.topics.size());
          for (Topic _iter186 : struct.topics)
          {
            _iter186.write(oprot);
          }
        }
      }
      if (struct.isSetUtterances()) {
        {
          oprot.writeI32(struct.utterances.size());
          for (Utterance _iter187 : struct.utterances)
          {
            _iter187.write(oprot);
          }
        }
      }
      if (struct.isSetId()) {
        struct.id.write(oprot);
      }
      if (struct.isSetValue()) {
        oprot.writeString(struct.value);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, Conversation struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      struct.conversationId = iprot.readI64();
      struct.setConversationIdIsSet(true);
      struct.name = iprot.readString();
      struct.setNameIsSet(true);
      BitSet incoming = iprot.readBitSet(5);
      if (incoming.get(0)) {
        struct.oneSided = iprot.readBool();
        struct.setOneSidedIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list188 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.topics = new ArrayList<Topic>(_list188.size);
          for (int _i189 = 0; _i189 < _list188.size; ++_i189)
          {
            Topic _elem190; // required
            _elem190 = new Topic();
            _elem190.read(iprot);
            struct.topics.add(_elem190);
          }
        }
        struct.setTopicsIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list191 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.utterances = new ArrayList<Utterance>(_list191.size);
          for (int _i192 = 0; _i192 < _list191.size; ++_i192)
          {
            Utterance _elem193; // required
            _elem193 = new Utterance();
            _elem193.read(iprot);
            struct.utterances.add(_elem193);
          }
        }
        struct.setUtterancesIsSet(true);
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
    }
  }

}
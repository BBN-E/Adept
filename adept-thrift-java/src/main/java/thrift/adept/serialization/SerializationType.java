/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.adept.serialization;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum SerializationType implements org.apache.thrift.TEnum {
  BINARY(0),
  XML(1),
  JSON(2);

  private final int value;

  private SerializationType(int value) {
    this.value = value;
  }

  /**
   * Get the integer value of this enum value, as defined in the Thrift IDL.
   */
  public int getValue() {
    return value;
  }

  /**
   * Find a the enum type by its integer value, as defined in the Thrift IDL.
   * @return null if the value is not found.
   */
  public static SerializationType findByValue(int value) { 
    switch (value) {
      case 0:
        return BINARY;
      case 1:
        return XML;
      case 2:
        return JSON;
      default:
        return null;
    }
  }
}
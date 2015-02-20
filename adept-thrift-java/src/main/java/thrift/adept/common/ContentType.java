/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package thrift.adept.common;


import java.util.Map;
import java.util.HashMap;
import org.apache.thrift.TEnum;

public enum ContentType implements org.apache.thrift.TEnum {
  TEXT(0),
  SPEECH(1);

  private final int value;

  private ContentType(int value) {
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
  public static ContentType findByValue(int value) { 
    switch (value) {
      case 0:
        return TEXT;
      case 1:
        return SPEECH;
      default:
        return null;
    }
  }
}
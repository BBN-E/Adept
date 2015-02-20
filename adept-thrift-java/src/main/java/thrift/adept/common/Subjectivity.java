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

public enum Subjectivity implements org.apache.thrift.TEnum {
  SUBJECTIVE(0),
  OBJECTIVE(1),
  NONE(2);

  private final int value;

  private Subjectivity(int value) {
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
  public static Subjectivity findByValue(int value) { 
    switch (value) {
      case 0:
        return SUBJECTIVE;
      case 1:
        return OBJECTIVE;
      case 2:
        return NONE;
      default:
        return null;
    }
  }
}
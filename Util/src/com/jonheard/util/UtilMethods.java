package com.jonheard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// UtilMethods - Place for general functionality not belonging to a specific class
public class UtilMethods {
  // Loads the given file (filename) and returns the file's contents as a string
  // Returns - The file's contents as a string, or NULL if the file loading failed
  public static String fileToString(String filename) {
    // bad input check
    if (filename == null) { throw new IllegalArgumentException("Arg1(filename): null"); }

    String result = null;
    try {
      Scanner s = new Scanner(new File(filename));
      s.useDelimiter("\\z");
      result = s.next();
      s.close();
    } catch (Exception e) {}
    return result;
  }

  // Takes the given string (data) and writes its contents to the given file (filename)
  public static boolean stringToFile(String data, String filename) {
    // bad input check
    if (data == null) { throw new IllegalArgumentException("Arg1(data): null"); }
    if (filename == null) { throw new IllegalArgumentException("Arg2(filename): null"); }

    return byteArrayToFile(data.getBytes(), filename);
  }

  // Loads the given file (filename) and returns the file's contents as a byte array
  // Returns - The file's contents as a string, or NULL if the file loading failed
  public static byte[] fileToByteArray(String filename) {
    // bad input check
    if (filename == null) { throw new IllegalArgumentException("Arg1(filename): null"); }

    byte[] result = null;
    try {
      result = Files.readAllBytes(Paths.get(filename));
    } catch (Exception e) {}
    return result;
  }

  // Takes the given byte array (data) and writes its contents to the given file (filename)
  public static boolean byteArrayToFile(byte[] data, String filename) {
    // bad input check
    if (data == null) { throw new IllegalArgumentException("Arg1(data): null"); }
    if (filename == null) { throw new IllegalArgumentException("Arg2(filename): null"); }

    FileOutputStream out;
    try {
      File file = new File(filename);
      if (file.exists()) file.delete();
      out = new FileOutputStream(file);
      out.write(data);
      out.close();
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  // Convert a string to tokens: split by whitespace, except when in quotes
  public static List<String> tokenizeStringByWhitespace(String toTokenize) {
    // bad input check
    if (toTokenize == null) { throw new IllegalArgumentException("Arg1(toTokenize): null"); }

    toTokenize = toTokenize.trim();
    // http://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
    ArrayList<String> result = new ArrayList<String>();
    Matcher m = Pattern.compile("([^\"]\\S*|\"[\\s\\S]+?\")\\s*").matcher(toTokenize);
    while (m.find()) {
      result.add(m.group(1).replace("\"", ""));
    }
    return result;
  }

  // Determine if the given Enum class contains a value matching the given string
  public static <T extends Enum<T>> boolean enumHasValue(Class<T> enumClass, String value) {
    // bad input check
    if (enumClass == null) { throw new IllegalArgumentException("Arg1(enumClass): null"); }
    if (value == null) { throw new IllegalArgumentException("Arg2(value): null"); }

    for (Enum<T> enumVal : enumClass.getEnumConstants()) {
      if (enumVal.name().equals(value)) { return true; }
    }
    return false;
  }

  // Takes a String with contents in constant naming convention and returns a String with the same
  // contents, but in camel case
  public static String constNameToCamelName(String source, boolean startUpper) {
    // bad input check
    if (source == null) { throw new IllegalArgumentException("Arg1(source): null"); }

    StringBuilder result = new StringBuilder();
    int size = source.length();
    for (int i = 0; i < size; i++) {
      if (source.charAt(i) == '_' && i + 1 < size) {
        if (source.charAt(i + 1) != '_') {
          startUpper = true;
        } else {
          result.append('_');
        }
      } else if (startUpper) {
        startUpper = false;
        result.append(Character.toUpperCase(source.charAt(i)));
      } else {
        result.append(Character.toLowerCase(source.charAt(i)));
      }
    }
    return result.toString();
  }

  // Create a method descriptor from a return type & collection of arg types
  public static String buildMethodDescriptor(String type, Collection<String> args) {
    // bad input check
    if (type == null) { throw new IllegalArgumentException("Arg1(type): null"); }
    if (args == null) { throw new IllegalArgumentException("Arg2(args): null"); }

    StringBuilder result = new StringBuilder("(");
    if (args != null) {
      for (String i : args) {
        result.append(i);
      }
    }
    result.append(")");
    result.append(type == null ? "V" : type);
    return result.toString();
  }

  public static final int INVALID_STACK_SIZE = Integer.MAX_VALUE;

  // Gets the number of arguments defined in a method descriptor
  public static int getStackSizeOfMethodDescriptor(String descriptor) {
    // bad input check
    if (descriptor == null) { throw new IllegalArgumentException("Arg1(descriptor): null"); }

    int result = 0;

    /// Check formatting
    if (descriptor.length() == 0) return INVALID_STACK_SIZE;
    if (descriptor.charAt(0) != '(') return INVALID_STACK_SIZE;
    if (!descriptor.contains(")")) return INVALID_STACK_SIZE;

    int charIndex = 1;
    boolean isArray = false;
    while (descriptor.charAt(charIndex) != ')') {
      char cur = descriptor.charAt(charIndex);
      switch (cur) {
        case '[':
          isArray = true;
          break;
        case 'L':
          charIndex = descriptor.indexOf(';', charIndex);
          result++;
          isArray = false;
          break;
        case 'J':
        case 'D':
          result += isArray ? 1 : 2;
          isArray = false;
          break;
        default:
          result++;
          isArray = false;
          break;
      }
      charIndex++;
    }
    return result;
  }

  public static int getStackSizeOfFieldDescriptor(String descriptor) {
    // bad input check
    if (descriptor == null) { throw new IllegalArgumentException("Arg1(descriptor): null"); }

    switch (descriptor) {
      case "":
        return INVALID_STACK_SIZE;
      case "V":
        return 0;
      case "J":
        return 2;
      case "D":
        return 2;
      default:
        return 1;
    }
  }

  public static final short INVALID_MODIFIER_LIST = Short.MAX_VALUE;

  // Turn a list of modifier strings into bitflags stored as a short (as
  // defined in the jvm spec)
  public static short generateFlagsFromModifierList(Collection<String> modifierList) {
    // bad input check
    if (modifierList == null) { throw new IllegalArgumentException("Arg1(modifierList): null"); }

    short result = 0;
    if (modifierList.contains("public")) result |= 0x0001;
    if (modifierList.contains("private")) result |= 0x0002;
    if (modifierList.contains("protected")) result |= 0x0004;
    if (modifierList.contains("static")) result |= 0x0008;
    if (modifierList.contains("final")) result |= 0x0010;
    if (modifierList.contains("synchronized")) result |= 0x0020;
    if (modifierList.contains("super")) result |= 0x0020;
    if (modifierList.contains("bridge")) result |= 0x0040;
    if (modifierList.contains("volatile")) result |= 0x0040;
    if (modifierList.contains("varargs")) result |= 0x0080;
    if (modifierList.contains("transient")) result |= 0x0080;
    if (modifierList.contains("native")) result |= 0x0100;
    if (modifierList.contains("interface")) result |= 0x0200;
    if (modifierList.contains("abstract")) result |= 0x0400;
    if (modifierList.contains("strict")) result |= 0x0800;
    if (modifierList.contains("synthetic")) result |= 0x1000;
    if (modifierList.contains("annotation")) result |= 0x2000;
    if (modifierList.contains("enum")) result |= 0x4000;
    return result;
  }
}

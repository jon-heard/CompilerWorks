
package com.jonheard.compilers.parser_java.ir;

import java.util.Collection;
import java.util.HashSet;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class List_Modifier extends BaseIrType {
  public List_Modifier() {
    super(0, 0);
  }

  public List_Modifier(Parser parser) {
    super(parser);
    while (true) {
      if        (parser.passTokenIfType(TokenType._ABSTRACT)) {
        modifiers.add("abstract");
      } else if (parser.passTokenIfType(TokenType._FINAL)) {
        modifiers.add("final");
      } else if (parser.passTokenIfType(TokenType._INTERFACE)) {
        modifiers.add("interface");
      } else if (parser.passTokenIfType(TokenType._NATIVE)) {
        modifiers.add("native");
      } else if (parser.passTokenIfType(TokenType._PRIVATE)) {
        modifiers.add("private");
      } else if (parser.passTokenIfType(TokenType._PROTECTED)) {
        modifiers.add("protected");
      } else if (parser.passTokenIfType(TokenType._PUBLIC)) {
        modifiers.add("public");
      } else if (parser.passTokenIfType(TokenType._STATIC)) {
        modifiers.add("static");
      } else if (parser.passTokenIfType(TokenType._STRICTFP)) {
        modifiers.add("strictfp");
      } else if (parser.passTokenIfType(TokenType._SYNCHRONIZED)) {
        modifiers.add("synchronized");
      } else if (parser.passTokenIfType(TokenType._TRANSIENT)) {
        modifiers.add("transient");
      } else if (parser.passTokenIfType(TokenType._VOLATILE)) {
        modifiers.add("volatile");
      } else {
        break;
      }
    }
  }

  @Override
  public String getHeaderString() {
    return "value='" + getValue() + "'";
  }

  public String getValue() {
    StringBuilder result = new StringBuilder();
    Collection<String> stringCollection = toStringCollection();
    int counter = 0;
    for (String item : stringCollection) {
      result.append(item);
      if (counter < stringCollection.size() - 1) {
        result.append(" ");
      }
      counter++;
    }
    return result.toString();
  }
  public Collection<String> toStringCollection() { return modifiers; }
  public boolean getIsAbstract()     { return modifiers.contains("abstract"); }
  public boolean getIsFinal()        { return modifiers.contains("final"); }
  public boolean getIsInterface()    { return modifiers.contains("interface"); }
  public boolean getIsNative()       { return modifiers.contains("native"); }
  public boolean getIsPrivate()      { return modifiers.contains("private"); }
  public boolean getIsProtected()    { return modifiers.contains("protected"); }
  public boolean getIsPublic()       { return modifiers.contains("public"); }
  public boolean getIsStatic()       { return modifiers.contains("static"); }
  public boolean getIsStrictfp()     { return modifiers.contains("strictfp"); }
  public boolean getIsSynchronized() { return modifiers.contains("synchronized"); }
  public boolean getIsTransient()    { return modifiers.contains("transient"); }
  public boolean getIsVolatile()     { return modifiers.contains("volatile"); }

  private HashSet<String> modifiers = new HashSet<String>();
}

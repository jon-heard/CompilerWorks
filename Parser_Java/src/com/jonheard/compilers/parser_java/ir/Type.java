
package com.jonheard.compilers.parser_java.ir;

import java.util.ArrayList;
import java.util.Collection;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Type extends BaseIrType {
  public Type(Parser parser) {
    super(parser);
    addChild(new QualifiedId(parser));
    if (parser.passTokenIfType(TokenType.LEFT_TRI)) {
      addChild(new List_QualifiedId(parser));
      parser.requireTokenToBeOfType(TokenType.RIGHT_TRI);
    }
    else {
      addChild(new List_QualifiedId(parser, true));
    }
    while (parser.passTokenIfType(TokenType.LEFT_SQUARE)) {
      parser.requireTokenToBeOfType(TokenType.RIGHT_SQUARE);
      incDimensionCount();
    }
  }

  public void incDimensionCount() {
    dimensionCount++;
  }

  public String toJvmDescriptor() {
    String type = getId().getValue();
    int dimensionCount = getDimensionCount();
    StringBuilder result = new StringBuilder();

    for (int i = 0; i < dimensionCount; i++) {
      result.append("[");
    }
    if (type.equals("void"))
      result.append("V");
    else if (type.equals("byte"))
      result.append("B");
    else if (type.equals("char"))
      result.append("C");
    else if (type.equals("double"))
      result.append("D");
    else if (type.equals("float"))
      result.append("F");
    else if (type.equals("int"))
      result.append("I");
    else if (type.equals("long"))
      result.append("J");
    else if (type.equals("short"))
      result.append("S");
    else if (type.equals("boolean"))
      result.append("Z");
    else {
      result.append("L");
      result.append(type.replace('.', '/'));
      result.append(";");
    }

    return result.toString();
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    return Id.getIsNext(parser);
  }

  @Override
  public String getHeaderString() {
    return
        "id='" + getId() + "' " +
        "generics='" + getGenerics().toString() + "' " +
        "arrayDimensions='" + getDimensionCount() + "'";
  }

  @Override
  public int getFirstPrintedChildIndex() { return 2; }

  public String getValue() {
    StringBuilder result = new StringBuilder();
    result.append(getId().getValue());
    List_QualifiedId generics = getGenerics();
    if (generics.getChildCount() > 0) {
      result.append("<");
      result.append(generics.toString());
      result.append(">");
    }
    for (int i = 0; i < dimensionCount; ++i) {
      result.append("[]");
    }
    return result.toString();
  }

  public QualifiedId getId() { return (QualifiedId)getChild(0); }

  public List_QualifiedId getGenerics() { return (List_QualifiedId)getChild(1); }

  public int getDimensionCount() { return dimensionCount; }

  private int dimensionCount = 0;
}

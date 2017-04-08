
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
      do {
        addChild(new Type(parser));
      } while(parser.passTokenIfType(TokenType.COMMA));
      parser.requireTokenToBeOfType(TokenType.RIGHT_TRI);
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
        "genericCount='" + (getChildCount()-1) + "' " +
        "arrayDimensions='" + getDimensionCount() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 1; }

  public String getValue() {
    StringBuilder result = new StringBuilder();
    result.append(getId());
    int childCount = getChildCount();
    if (childCount > 1) {
      result.append("<");
      for (int i = 1; i < childCount; ++i) {
        Type current = (Type)getChild(i);
        result.append(current.getValue());
        if (i < childCount-1) {
          result.append(", ");
        }
      }
      result.append(">");
    }
    for (int i = 0; i < dimensionCount; ++i) {
      result.append("[]");
    }
    return result.toString();
  }
  public Collection<Type> getGenericTypes()
  {
    ArrayList<Type> result = new ArrayList<>();
    int childCount = getChildCount();
    for (int i = 1; i < childCount; ++i) {
      result.add((Type)getChild(i));
    }
    return result;
  }
  public QualifiedId getId() {
    return (QualifiedId)getChild(0);
  }
  public int getDimensionCount() { return dimensionCount; }

  private int dimensionCount = 0;
}

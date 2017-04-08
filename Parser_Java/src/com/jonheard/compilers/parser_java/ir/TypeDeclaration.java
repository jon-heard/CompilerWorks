package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class TypeDeclaration extends BaseIrType {
  public TypeDeclaration(Parser parser, TokenType identifier) {
    super(parser);
    addChild(new List_Modifier(parser));
    parser.requireTokenToBeOfType(identifier);
    addChild(new Id(parser));
  }

  @Override
  public String getHeaderString() {
    return
        "id='" + getName().getValue() + "' " +
        "modifiers='" + getModifiers().getValue() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 2; }
  public Id getName() { return (Id) getChild(1); }
  public List_Modifier getModifiers() { return (List_Modifier) getChild(0); }

  public static boolean isNext(Parser parser) {
    boolean result = false;
    parser.getTokenQueue().remember();
    new List_Modifier(parser);
    result =
        parser.getIsTokenType(TokenType._CLASS) ||
        parser.getIsTokenType(TokenType._INTERFACE) ||
        parser.getIsTokenType(TokenType._ENUM);
    parser.getTokenQueue().rewind();
    return result;
  }
}

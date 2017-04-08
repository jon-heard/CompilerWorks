package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class TypeDeclaration extends BaseIrType {
  public TypeDeclaration(Parser parser, TokenType identifier) {
    super(parser);
    // bad input check
    if (identifier == null) { throw new IllegalArgumentException("arg2(identifier): null"); }

    addChild(new List_Modifier(parser));
    parser.requireTokenToBeOfType(identifier);
    addChild(new Id(parser));
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

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
}

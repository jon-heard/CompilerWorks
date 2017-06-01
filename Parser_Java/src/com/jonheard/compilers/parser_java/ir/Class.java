package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Class extends TypeDeclaration {
  public Class(Parser parser) {
    super(parser, TokenType._CLASS);
    if (parser.passTokenIfType(TokenType._EXTENDS)) {
      addChild(new QualifiedId(parser));
    } else {
      parser.getTokenQueue().addFirst(new Token(TokenType.IDENTIFIER, 0, 0, "Object"));
      addChild(new QualifiedId(parser));
    }
    if (parser.passTokenIfType(TokenType._IMPLEMENTS)) {
      addChild(new List_QualifiedId(parser));
    } else {
      addChild(new List_QualifiedId(parser, true));
    }
    parser.requireTokenToBeOfType(TokenType.LEFT_CURL);
    while (!parser.passTokenIfType(TokenType.RIGHT_CURL)) {
      addChild(new Member(parser));
    }
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    boolean result = false;
    parser.getTokenQueue().remember();
    new List_Modifier(parser);
    result = parser.getIsTokenType(TokenType._CLASS);
    parser.getTokenQueue().rewind();
    return result;
  }

  @Override
  public String getHeaderString() {
    return
        super.getHeaderString() + " " +
        "super='" + getSuper().getValue() + "' " +
        "interfaces='" + getInterfaces().toString() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 4; }

  public QualifiedId getSuper() { return (QualifiedId) getChild(2); }
  public List_QualifiedId getInterfaces() { return (List_QualifiedId) getChild(3); }
}

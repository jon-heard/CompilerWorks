
package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Package extends BaseIrType {
  public Package(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._PACKAGE);
    addChild(new QualifiedId(parser));
    parser.requireTokenToBeOfType(TokenType.SEMICOLON);
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    return parser.getIsTokenType(TokenType._PACKAGE);
  }

  @Override
  public String getHeaderString() {
    return "id='" + getId().getValue() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 1; }

  public QualifiedId getId() { return (QualifiedId)getChild(0); }
}

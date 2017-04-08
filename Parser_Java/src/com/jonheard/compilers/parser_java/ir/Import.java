
package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Import extends BaseIrType {
  public Import(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._IMPORT);
    if (parser.passTokenIfType(TokenType._STATIC)) {
      isStatic = true;
    }
    addChild(new QualifiedId(parser));
    if (parser.passTokenIfType(TokenType.DOT)) {
      parser.requireTokenToBeOfType(TokenType.STAR);
      isOnDemand = true;
    }
    parser.requireTokenToBeOfType(TokenType.SEMICOLON);
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    return parser.getIsTokenType(TokenType._IMPORT);
  }

  @Override
  public String getHeaderString() {
    return
        "isOnDemand='" + isOnDemand + "' " +
        "isStatic='" + isStatic + "' " +
        "id='" + getId().getValue() + "'";
  }
  @Override
  public int getFirstPrintedChildIndex() { return 1; }

  public QualifiedId getId() { return (QualifiedId)getChild(0); }
  public boolean getIsStatic() { return isStatic; }
  public boolean getIsOnDemand() { return isOnDemand; }

  private boolean isStatic = false;
  private boolean isOnDemand = false;
}

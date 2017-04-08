
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class For extends BaseIrType {
  public For(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._FOR);
    parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
    while (!parser.passTokenIfType(TokenType.RIGHT_PAREN)) {
      parser.getTokenQueue().poll();
    }
    addChild(StatementParser.getNext(parser));
  }

  public static boolean getIsNext(Parser parser) {
    return parser.getIsTokenType(TokenType._FOR);
  }
}

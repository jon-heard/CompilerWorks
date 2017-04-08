
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Member;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class EnhancedFor extends BaseIrType {
  public EnhancedFor(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._FOR);
    parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
    addChild(new Member(parser, true, true, true, true));
    parser.requireTokenToBeOfType(TokenType.COLON);
    addChild(Parser_Expression.parseExpression(parser));
    parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
    addChild(StatementParser.getNext(parser));
  }

  public static boolean getIsNext(Parser parser) {
    boolean result = false;
    parser.getTokenQueue().remember();
    if (parser.passTokenIfType(TokenType._FOR)) {
      parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
      if (Member.getIsNext(parser)) {
        new Member(parser);
      }
      if (parser.getIsTokenType(TokenType.COLON)) {
        result = true;
      }
    }
    parser.getTokenQueue().rewind();
    return result;
  }
}


package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.Member;
import com.jonheard.compilers.parser_java.ir.expression.ExpressionParser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class For extends BaseIrType {
  public For(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._FOR);
    parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
    if (Member.getIsNext(parser)) {
      addChild(new Member(parser, true, true, false, false));
    } else {
      addChild(new List_Expression(parser));
      parser.requireTokenToBeOfType(TokenType.SEMICOLON);
    }
    addChild(ExpressionParser.parseExpressionStatment(parser));
    parser.requireTokenToBeOfType(TokenType.SEMICOLON);
    addChild(new List_Expression(parser));
    parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
    addChild(StatementParser.getNext(parser));
  }

  public static boolean getIsNext(Parser parser) {
    return parser.getIsTokenType(TokenType._FOR);
  }
}

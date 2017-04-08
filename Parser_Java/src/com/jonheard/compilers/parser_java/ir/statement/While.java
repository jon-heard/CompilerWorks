
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.ExpressionParser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class While extends BaseIrType {
  public While(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._WHILE);
    parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
    addChild(ExpressionParser.parseExpression(parser));
    parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
    addChild(StatementParser.getNext(parser));
  }

  public static boolean getIsNext(Parser parser) {
    return parser.getIsTokenType(TokenType._WHILE);
  }
}

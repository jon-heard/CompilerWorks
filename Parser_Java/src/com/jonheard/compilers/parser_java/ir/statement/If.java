
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class If extends BaseIrType {
  public If(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._IF);
    parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
    addChild(Parser_Expression.parseExpression(parser));
    parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
    addChild(StatementParser.getNext(parser));
  }

  public static boolean getIsNext(Parser parser) {
    return parser.getIsTokenType(TokenType._IF);
  }
}

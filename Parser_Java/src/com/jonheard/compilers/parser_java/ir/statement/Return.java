
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.ExpressionParser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Return extends BaseIrType {
  public Return(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType._RETURN);
    if (!parser.getIsTokenType(TokenType.SEMICOLON)) {
      addChild(ExpressionParser.parseExpression(parser));
    }
    parser.requireTokenToBeOfType(TokenType.SEMICOLON);
  }

  public static boolean getIsNext(Parser parser) {
    return parser.getIsTokenType(TokenType._RETURN);
  }
}

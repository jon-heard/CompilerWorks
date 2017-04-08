
package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class List_Expression extends BaseIrType {
  public List_Expression() {
    super(0, 0);
  }

  public List_Expression(Parser parser) {
    super(parser);
    do {
      addChild(Parser_Expression.parseExpression(parser));
    } while (parser.passTokenIfType(TokenType.COMMA));
  }
}

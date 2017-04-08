
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Semicolon extends BaseIrType {
  public Semicolon(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType.SEMICOLON);
  }

  public static boolean getIsNext(Parser parser) {
    return parser.getIsTokenType(TokenType.SEMICOLON);
  }
}

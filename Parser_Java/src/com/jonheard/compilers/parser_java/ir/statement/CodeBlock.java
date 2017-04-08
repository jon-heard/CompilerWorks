
package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Member;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class CodeBlock extends BaseIrType {
  public CodeBlock(Parser parser) {
    super(parser);
    parser.requireTokenToBeOfType(TokenType.LEFT_CURL);
    while (!parser.passTokenIfType(TokenType.RIGHT_CURL)) {
      if (Member.getIsNext(parser)) {
        addChild(new Member(parser, true, true, false, false));
      } else {
        addChild(StatementParser.getNext(parser));
      }
    }
  }

  public static boolean getIsNext(Parser parser) {
    return parser.getIsTokenType(TokenType.LEFT_CURL);
  }
}

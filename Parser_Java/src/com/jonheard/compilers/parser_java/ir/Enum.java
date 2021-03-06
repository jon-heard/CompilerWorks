
package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Enum extends TypeDeclaration {
  public Enum(Parser parser)
  {
    super(parser, TokenType._ENUM);
    do {
      addChild(new Id(parser));
    } while( parser.passTokenIfType(TokenType.COMMA));
    parser.requireTokenToBeOfType(TokenType.RIGHT_CURL);
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    boolean result = false;
    parser.getTokenQueue().remember();
    new List_Modifier(parser);
    result = parser.getIsTokenType(TokenType._ENUM);
    parser.getTokenQueue().rewind();
    return result;
  }
}

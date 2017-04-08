
package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Interface extends TypeDeclaration {
  public Interface(Parser parser) {
    super(parser, TokenType._INTERFACE);
    while (!parser.passTokenIfType(TokenType.CURL_BRACE_RIGHT)) {
      parser.getTokenQueue().poll();
    }
  }

  public static boolean getIsNext(Parser parser) {
    boolean result = false;
    parser.getTokenQueue().remember();
    new List_Modifier(parser);
    result = parser.getIsTokenType(TokenType._INTERFACE);
    parser.getTokenQueue().rewind();
    return result;
  }

  @Override
  public String getHeaderString() {
    return super.getHeaderString();
  }
  @Override
  public int getFirstPrintedChildIndex() { return 2; }
}


package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Id extends BaseIrType {
  public Id(Parser parser) {
    super(parser);
    Token currentToken = parser.peekNextToken();
    if (parser.requireTokenToBeOfType(TokenType.IDENTIFIER)) {
      value = currentToken.getText();
    } else {
      value = "";
    }
  }
  public Id(String value) {
    super(0, 0);
    if (value == null) { throw new IllegalArgumentException("Arg1(value): null"); }
    this.value = value;
  }

  @Override
  public boolean equals(Object rhs) {
    if (!(rhs instanceof Id)) {
      return false;
    }
    return this.value.equals(((Id)rhs).value);
  }

  public static boolean getIsNext(Parser parser) {
    // bad input check
    if (parser == null) { throw new IllegalArgumentException("Arg1(parser): null"); }

    return parser.getIsTokenType(TokenType.IDENTIFIER);
  }

  @Override
  public String getHeaderString() { return "value='" + getValue() + "'"; }

  public String getValue() { return value; }

  private String value = "";
}

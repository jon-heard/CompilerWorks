package com.jonheard.compilers.tokenizer_java;

// TokenType - Each java token is assigned one of the following java token types
public enum TokenType
{
  IDENTIFIER(0),

  STRING(0),
  CHAR(0),
  INTEGER(0),
  LONG(0),
  FLOAT(0),
  DOUBLE(0),

  PLUS_PLUS(2),
  DASH_DASH(2),
  PLUS(1),
  DASH(1),
  TILDE(1),
  EXCLAIM(1),
  STAR(1),
  SLASH(1),
  PERCENT(1),
  LEFT_LEFT(2),
  RIGHT_RIGHT(2),
  RIGHT_RIGHT_RIGHT(3),
  LEFT(1),
  RIGHT(1),
  LEFT_EQUAL(2),
  RIGHT_EQUAL(2),
  EQUAL_EQUAL(2),
  EXCLAIM_EQUAL(2),
  AND(1),
  CARAT(1),
  PIPE(1),
  AND_AND(2),
  PIPE_PIPE(2),
  QUESTION(1),
  COLON(1),
  EQUAL(1),
  PLUS_EQUAL(2),
  DASH_EQUAL(2),
  STAR_EQUAL(2),
  SLASH_EQUAL(2),
  PERCENT_EQUAL(2),
  AND_EQUAL(2),
  CARAT_EQUAL(2),
  PIPE_EQUAL(2),
  LEFT_LEFT_EQUAL(3),
  RIGHT_RIGHT_EQUAL(3),
  RIGHT_RIGHT_RIGHT_EQUAL(4),
  DOT(1),
  SEMICOLON(1),
  COMMA(1),
  CURL_BRACE_LEFT(1),
  CURL_BRACE_RIGHT(1),
  SQUARE_BRACE_LEFT(1),
  SQUARE_BRACE_RIGHT(1),
  PAREN_LEFT(1),
  PAREN_RIGHT(1),

  _ABSTRACT(-1),
  _ASSERT(-1),
  _BREAK(-1),
  _CASE(-1),
  _CATCH(-1),
  _CLASS(-1),
  _CONST(-1),
  _CONTINUE(-1),
  _DEFAULT(-1),
  _DO(-1),
  _ELSE(-1),
  _ENUM(-1),
  _EXTENDS(-1),
  _FALSE(-1),
  _FINAL(-1),
  _FINALLY(-1),
  _FOR(-1),
  _GOTO(-1),
  _IF(-1),
  _IMPLEMENTS(-1),
  _IMPORT(-1),
  _INSTANCEOF(-1),
  _INTERFACE(-1),
  _NATIVE(-1),
  _NEW(-1),
  _NULL(-1),
  _PACKAGE(-1),
  _PRIVATE(-1),
  _PROTECTED(-1),
  _PUBLIC(-1),
  _RETURN(-1),
  _STATIC(-1),
  _STRICTFP(-1),
  _SUPER(-1),
  _SWITCH(-1),
  _SYNCHRONIZED(-1),
  _THIS(-1),
  _THROW(-1),
  _THROWS(-1),
  _TRANSIENT(-1),
  _TRUE(-1),
  _TRY(-1),
  _VOLATILE(-1),
  _WHILE(-1);

  private final int length;
  private TokenType(int length) {
    // bad input check
    if (length < -1) { throw new IllegalArgumentException("Arg1(length): < -1"); }
    if (length == -1) {
      this.length = this.name().length()-1;
    } else {
      this.length = length;
    }
  }
  
  @Override
  public String toString() {
    String result = isKeyword() ? this.name().substring(1) : this.name();
    return result.toLowerCase();
  }

  public boolean isKeyword() {
    return this.name().charAt(0) == '_';
  }
  
  public int getLength() { return length; }
}

package com.jonheard.compilers.tokenizer_java;

// Token - Represents a single token of java source code
public class Token {
  // Constructors
  public Token(TokenType type, int row, int col) {
    this.type = type;
    this.row = row;
    this.column = col;
    this.text = null;
  }
  public Token(TokenType type, int row, int col, String text) {
    this.type = type;
    this.row = row;
    this.column = col;
    this.text = text;
  }
  
  public int getLength() {
    int result = type.getLength();
    if (result == 0) {
      result = text==null ? 1 : text.length();
    }
    return result;
  }

  @Override
  public String toString() {
    if (text == null) {
      return type.toString();
    } else {
      return type.toString() + ":" + text;
    }
  }

  @Override
  public boolean equals(Object rhs) {
    boolean result = false;
    if (rhs instanceof Token) {
      Token rhsToken = (Token) rhs;
      result =
          (rhsToken.type.equals(type)) &&
          (rhsToken.text == text || rhsToken.text.equals(text));
    }
    return result;
  }

  // Accessors
  public TokenType getType() { return type; }
  public String getText() { return text; }
  public int getRow() { return row; }
  public int getColumn() { return column; }


  // Variables
  private TokenType type;
  private String text;
  private int row, column;
}

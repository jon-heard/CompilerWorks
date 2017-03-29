package com.jonheard.compilers.tokenizer_java;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest {

  @Test
  public void main() {
    Token t1 = new Token(TokenType._ABSTRACT, 2, 7);
    Token t2 = new Token(TokenType.AND_EQUAL, 15, 23);
    Token t3 = new Token(TokenType.INTEGER, 15, 23, "5732");

    assertEquals(8, t1.getLength());
    assertEquals(2, t2.getLength());
    assertEquals(4, t3.getLength());

    assertEquals("abstract", t1.toString());
    assertEquals("and_equal", t2.toString());
    assertEquals("integer:5732", t3.toString());

    assertTrue(t1.equals(new Token(TokenType._ABSTRACT, 0, 0)));
    assertTrue(t2.equals(new Token(TokenType.AND_EQUAL, 0, 0)));
    assertTrue(t3.equals(new Token(TokenType.INTEGER, 0, 0, "5732")));
    assertFalse(t1.equals(t2));

    assertEquals(TokenType._ABSTRACT, t1.getType());
    assertEquals(TokenType.AND_EQUAL, t2.getType());
    assertEquals(TokenType.INTEGER, t3.getType());

    assertEquals(null, t1.getText());
    assertEquals(null, t2.getText());
    assertEquals("5732", t3.getText());

    assertEquals(2, t1.getRow());
    assertEquals(15, t2.getRow());
    assertEquals(15, t3.getRow());

    assertEquals(7, t1.getColumn());
    assertEquals(23, t2.getColumn());
    assertEquals(23, t3.getColumn());
  }

}

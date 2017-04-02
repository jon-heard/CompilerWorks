package com.jonheard.compilers.tokenizer_java;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTypeTest {

  @Test
  public void main() {
    TokenType t1 = TokenType._ABSTRACT;
    TokenType t2 = TokenType.PLUS_EQUAL;

    assertTrue(t1.isKeyword());
    assertFalse(t2.isKeyword());

    assertEquals("abstract", t1.toString());
    assertEquals("plus_equal", t2.toString());

    assertEquals(8, t1.getLength());
    assertEquals(2, t2.getLength());
  }
}

package com.jonheard.compilers.tokenizer_java;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTest
{

	@Test
	public void main()
	{
		Token.setCurrentRow(81);
		Token t1 = new Token(TokenType._ABSTRACT, 7);
		Token.incCurrentRow();
		Token t2 = new Token(TokenType.INTEGER, 23, "57");
		
		assertEquals(82, Token.getCurrentRow());
		assertEquals(TokenType._ABSTRACT, t1.getType());
		assertEquals(TokenType.INTEGER, t2.getType());
		assertEquals("", t1.getText());
		assertEquals("57", t2.getText());
		assertEquals(81, t1.getRow());
		assertEquals(82, t2.getRow());
		assertEquals(7, t1.getColumn());
		assertEquals(23, t2.getColumn());
		assertEquals("abstract", t1.toString());
		assertEquals("integer:57", t2.toString());
		assertFalse(t1.equals(t2));
		assertTrue(t2.equals(new Token(TokenType.INTEGER, 13, "57")));
	}

}

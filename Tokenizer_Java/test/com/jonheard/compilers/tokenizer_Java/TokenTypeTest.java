package com.jonheard.compilers.tokenizer_java;

import static org.junit.Assert.*;

import org.junit.Test;

public class TokenTypeTest
{

	@Test
	public void main()
	{
		TokenType t1 = TokenType._ABSTRACT;
		TokenType t2 = TokenType.CARAT;
		
		assertTrue(t1.isIdentifier());
		assertFalse(t2.isIdentifier());
		
		assertEquals("abstract", t1.toString());
		assertEquals("carat", t2.toString());
	}
}

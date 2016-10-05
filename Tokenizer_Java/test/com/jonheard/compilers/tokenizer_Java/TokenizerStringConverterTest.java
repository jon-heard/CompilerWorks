package com.jonheard.compilers.tokenizer_java;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

public class TokenizerStringConverterTest
{
	@Test
	public void tokenizedToString()
	{
		List<Token> source = new ArrayList<Token>();
		source.add(new Token(TokenType._PUBLIC, 0));
		source.add(new Token(TokenType.EQUAL, 0));
		source.add(new Token(TokenType.ID, 0, "t"));
		source.add(new Token(TokenType.INTEGER, 0, "42"));
		
		String expected = "public\nequal\nid:t\ninteger:42\n";

		TokenizerStringConverter c = new TokenizerStringConverter();
		String actual = c.tokenizedToString(source);
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void stringToTokenized()
	{
		String source = "public\nequal\nid:t\ninteger:42\n";
		
		List<Token> expected = new ArrayList<Token>();
		expected.add(new Token(TokenType._PUBLIC, 0));
		expected.add(new Token(TokenType.EQUAL, 0));
		expected.add(new Token(TokenType.ID, 0, "t"));
		expected.add(new Token(TokenType.INTEGER, 0, "42"));
		
		TokenizerStringConverter c = new TokenizerStringConverter();
		List<Token> actual = c.stringToTokenized(source);

		expected.get(2).equals(actual.get(2));
		assertEquals(expected, actual);
	}

	@Test
	public void stringToTokenized_commentsAndEmptyLines()
	{
		String source = "public\nequal\n \nid:t\n // hi\ninteger:42\n";
		
		List<Token> expected = new ArrayList<Token>();
		expected.add(new Token(TokenType._PUBLIC, 0));
		expected.add(new Token(TokenType.EQUAL, 0));
		expected.add(new Token(TokenType.ID, 0, "t"));
		expected.add(new Token(TokenType.INTEGER, 0, "42"));
		
		TokenizerStringConverter c = new TokenizerStringConverter();
		List<Token> actual = c.stringToTokenized(source);

		expected.get(2).equals(actual.get(2));
		assertEquals(expected, actual);		
	}
}

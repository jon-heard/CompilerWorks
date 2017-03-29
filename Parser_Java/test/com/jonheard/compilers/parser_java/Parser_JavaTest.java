package com.jonheard.compilers.parser_java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.compilers.parser_java.ir.CompilationUnit;
import com.jonheard.compilers.parser_java.ir.Import;
import com.jonheard.compilers.parser_java.ir.Package;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.SourceFile;

public class Parser_JavaTest
{

	@Test
	public void basic()
	{
		List<Token> tokenList = makeGenericTokenList();
		Parser_Java parser = new Parser_Java();
		CompilationUnit cUnit = parser.parse(
				new SourceFile("test1.java", "first\nsecond\nthird\n"),
				tokenList);
		String val = cUnit.toString();
		System.out.println(val);
		assertEquals(5, cUnit.getChildCount());
		assertTrue(cUnit.getChild(0) instanceof Package);
		assertTrue(cUnit.getChild(1) instanceof Import);
		assertTrue(cUnit.getChild(2) instanceof Import);
		assertTrue(cUnit.getChild(3) instanceof Import);
		assertTrue(cUnit.getChild(4) instanceof Class);
	}

	private List<Token> makeGenericTokenList()
	{
		List<Token> result = new ArrayList<Token>();

		result.add(new Token(TokenType._PACKAGE, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "com"));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "test"));
		result.add(new Token(TokenType.SEMICOLON, 0));

		result.add(new Token(TokenType._IMPORT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "java"));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "util"));
		result.add(new Token(TokenType.SEMICOLON, 0));
		
		result.add(new Token(TokenType._IMPORT, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "java"));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "lang"));
		result.add(new Token(TokenType.SEMICOLON, 0));

		result.add(new Token(TokenType._IMPORT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "java"));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "swing"));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.STAR, 0));
		result.add(new Token(TokenType.SEMICOLON, 0));

		result.add(new Token(TokenType._PUBLIC, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType._CLASS, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "Test1"));
		result.add(new Token(TokenType.CURL_BRACE_LEFT, 0));
		
		result.add(new Token(TokenType._PUBLIC, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "int"));
		result.add(new Token(TokenType.IDENTIFIER, 0, "var1"));

		result.add(new Token(TokenType._PUBLIC, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "void"));
		result.add(new Token(TokenType.IDENTIFIER, 0, "main"));
		result.add(new Token(TokenType.PAREN_LEFT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "String"));
		result.add(new Token(TokenType.IDENTIFIER, 0, "args"));
		result.add(new Token(TokenType.SQUARE_BRACE_LEFT, 0));
		result.add(new Token(TokenType.SQUARE_BRACE_RIGHT, 0));
		result.add(new Token(TokenType.PAREN_RIGHT, 0));
		result.add(new Token(TokenType.CURL_BRACE_LEFT, 0));

		result.add(new Token(TokenType.IDENTIFIER, 0, "System"));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "out"));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, 0, "println"));
		result.add(new Token(TokenType.PAREN_LEFT, 0));
		result.add(new Token(TokenType.STRING, 0, "Hello world"));
		result.add(new Token(TokenType.PAREN_RIGHT, 0));

		result.add(new Token(TokenType.CURL_BRACE_RIGHT, 0));

		result.add(new Token(TokenType.CURL_BRACE_RIGHT, 0));

		return result;
	}
}

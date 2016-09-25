package com.jonheard.compilers.parser_java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.parser_java.JavaParser;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.compilers.parser_java.ir.CompilationUnit;
import com.jonheard.compilers.parser_java.ir.Import;
import com.jonheard.compilers.parser_java.ir.Package;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class JavaParserTest
{

	@Test
	public void basic()
	{
		List<Token> tokenList = makeGenericTokenList();
		JavaParser parser = new JavaParser(tokenList);
		CompilationUnit cUnit = parser.parse();
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
		result.add(new Token(TokenType.IDENTIFIER, "com", 0));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "test", 0));
		result.add(new Token(TokenType.SEMICOLON, 0));

		result.add(new Token(TokenType._IMPORT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "java", 0));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "util", 0));
		result.add(new Token(TokenType.SEMICOLON, 0));
		
		result.add(new Token(TokenType._IMPORT, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType.IDENTIFIER, "java", 0));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "lang", 0));
		result.add(new Token(TokenType.SEMICOLON, 0));

		result.add(new Token(TokenType._IMPORT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "java", 0));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "swing", 0));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.STAR, 0));
		result.add(new Token(TokenType.SEMICOLON, 0));

		result.add(new Token(TokenType._PUBLIC, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType._CLASS, 0));
		result.add(new Token(TokenType.IDENTIFIER, "Test1", 0));
		result.add(new Token(TokenType.CURL_BRACE_LEFT, 0));
		
		result.add(new Token(TokenType._PUBLIC, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType.IDENTIFIER, "int", 0));
		result.add(new Token(TokenType.IDENTIFIER, "var1", 0));

		result.add(new Token(TokenType._PUBLIC, 0));
		result.add(new Token(TokenType._STATIC, 0));
		result.add(new Token(TokenType.IDENTIFIER, "void", 0));
		result.add(new Token(TokenType.IDENTIFIER, "main", 0));
		result.add(new Token(TokenType.PAREN_LEFT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "String", 0));
		result.add(new Token(TokenType.IDENTIFIER, "args", 0));
		result.add(new Token(TokenType.SQUARE_BRACE_LEFT, 0));
		result.add(new Token(TokenType.SQUARE_BRACE_RIGHT, 0));
		result.add(new Token(TokenType.PAREN_RIGHT, 0));
		result.add(new Token(TokenType.CURL_BRACE_LEFT, 0));

		result.add(new Token(TokenType.IDENTIFIER, "System", 0));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "out", 0));
		result.add(new Token(TokenType.DOT, 0));
		result.add(new Token(TokenType.IDENTIFIER, "println", 0));
		result.add(new Token(TokenType.PAREN_LEFT, 0));
		result.add(new Token(TokenType.STRING, "Hello world", 0));
		result.add(new Token(TokenType.PAREN_RIGHT, 0));

		result.add(new Token(TokenType.CURL_BRACE_RIGHT, 0));

		result.add(new Token(TokenType.CURL_BRACE_RIGHT, 0));

		return result;
	}
}

package com.jonheard.compilers.javaParser;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.javaParser.ir.ClassDeclaration;
import com.jonheard.compilers.javaParser.ir.CompilationUnit;
import com.jonheard.compilers.javaParser.ir.ImportDeclaration;
import com.jonheard.compilers.javaParser.ir.PackageDeclaration;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;

public class JavaParserTest
{

	@Test
	public void basic()
	{
		List<JavaToken> tokenList = makeGenericTokenList();
		JavaParser parser = new JavaParser(tokenList);
		CompilationUnit cUnit = parser.parse();
		String val = cUnit.toString();
		System.out.println(val);
		assertEquals(5, cUnit.getChildCount());
		assertTrue(cUnit.getChild(0) instanceof PackageDeclaration);
		assertTrue(cUnit.getChild(1) instanceof ImportDeclaration);
		assertTrue(cUnit.getChild(2) instanceof ImportDeclaration);
		assertTrue(cUnit.getChild(3) instanceof ImportDeclaration);
		assertTrue(cUnit.getChild(4) instanceof ClassDeclaration);
	}

	private List<JavaToken> makeGenericTokenList()
	{
		List<JavaToken> result = new ArrayList<JavaToken>();

		result.add(new JavaToken(JavaTokenType._PACKAGE, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "com", 0));
		result.add(new JavaToken(JavaTokenType.DOT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "test", 0));
		result.add(new JavaToken(JavaTokenType.SEMICOLON, 0));

		result.add(new JavaToken(JavaTokenType._IMPORT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "java", 0));
		result.add(new JavaToken(JavaTokenType.DOT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "util", 0));
		result.add(new JavaToken(JavaTokenType.SEMICOLON, 0));
		
		result.add(new JavaToken(JavaTokenType._IMPORT, 0));
		result.add(new JavaToken(JavaTokenType._STATIC, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "java", 0));
		result.add(new JavaToken(JavaTokenType.DOT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "lang", 0));
		result.add(new JavaToken(JavaTokenType.SEMICOLON, 0));

		result.add(new JavaToken(JavaTokenType._IMPORT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "java", 0));
		result.add(new JavaToken(JavaTokenType.DOT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "swing", 0));
		result.add(new JavaToken(JavaTokenType.DOT, 0));
		result.add(new JavaToken(JavaTokenType.STAR, 0));
		result.add(new JavaToken(JavaTokenType.SEMICOLON, 0));

		result.add(new JavaToken(JavaTokenType._PUBLIC, 0));
		result.add(new JavaToken(JavaTokenType._STATIC, 0));
		result.add(new JavaToken(JavaTokenType._CLASS, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "Test1", 0));
		result.add(new JavaToken(JavaTokenType.CURL_BRACE_LEFT, 0));
		
		result.add(new JavaToken(JavaTokenType._PUBLIC, 0));
		result.add(new JavaToken(JavaTokenType._STATIC, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "int", 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "var1", 0));

		result.add(new JavaToken(JavaTokenType._PUBLIC, 0));
		result.add(new JavaToken(JavaTokenType._STATIC, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "void", 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "main", 0));
		result.add(new JavaToken(JavaTokenType.PAREN_LEFT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "String", 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "args", 0));
		result.add(new JavaToken(JavaTokenType.SQUARE_BRACE_LEFT, 0));
		result.add(new JavaToken(JavaTokenType.SQUARE_BRACE_RIGHT, 0));
		result.add(new JavaToken(JavaTokenType.PAREN_RIGHT, 0));
		result.add(new JavaToken(JavaTokenType.CURL_BRACE_LEFT, 0));

		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "System", 0));
		result.add(new JavaToken(JavaTokenType.DOT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "out", 0));
		result.add(new JavaToken(JavaTokenType.DOT, 0));
		result.add(new JavaToken(JavaTokenType.IDENTIFIER, "println", 0));
		result.add(new JavaToken(JavaTokenType.PAREN_LEFT, 0));
		result.add(new JavaToken(JavaTokenType.STRING, "Hello world", 0));
		result.add(new JavaToken(JavaTokenType.PAREN_RIGHT, 0));

		result.add(new JavaToken(JavaTokenType.CURL_BRACE_RIGHT, 0));

		result.add(new JavaToken(JavaTokenType.CURL_BRACE_RIGHT, 0));

		return result;
	}
}

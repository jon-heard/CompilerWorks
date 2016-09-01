package com.jonheard.compilers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.compilers.helpers.HelperMethods;

public class JavaCTest
{
	@Test
	public void basics()
	{
		String source =
				"package first.second;\n" +
				"import java.util.List;\n" +
				"import java.swing.*;\n" +
				"public class Test1\n" +
				"{\n" +
				"	public static void main(String[] args)\n" +
				"	{\n" +
				"		System.out.println(\"Hello world\");\n" +
				"	}\n" +
				"}";
		HelperMethods.stringToFile(source, "Test1.java");
		JavaC compiler = new JavaC();

		String expectedTokens = JavaC.HEADER_TEXT +
				"package\nidentifier:first\ndot\nidentifier:second\n" +
				"semicolon\nimport\nidentifier:java\ndot\nidentifier:util\n" +
				"dot\nidentifier:List\nsemicolon\nimport\nidentifier:java\n" +
				"dot\nidentifier:swing\ndot\nstar\nsemicolon\npublic\n" +
				"class\nidentifier:Test1\ncurl_brace_left\npublic\nstatic\n" +
				"void\nidentifier:main\nparen_left\nidentifier:String\n" +
				"square_brace_left\nsquare_brace_right\nidentifier:args\n" +
				"paren_right\ncurl_brace_left\nidentifier:System\ndot\n" +
				"identifier:out\ndot\nidentifier:println\nparen_left\n" +
				"string:Hello world\nparen_right\nsemicolon\n" +
				"curl_brace_right\ncurl_brace_right\n";
		String actualTokens =
				compiler.compile(new String[] {"Test1.java", "-t"});
		assertEquals(expectedTokens, actualTokens);
	}
	
	@Test
	public void help()
	{
		JavaC compiler = new JavaC();
		String expected = JavaC.HEADER_TEXT + JavaC.HELP_TEXT;
		String actual = compiler.compile(new String[] {"-h"});
		assertEquals(expected, actual);
	}
}

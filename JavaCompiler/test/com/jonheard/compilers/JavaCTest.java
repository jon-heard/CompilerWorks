package com.jonheard.compilers;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.util.HelperMethods;

public class JavaCTest
{
	@Test
	public void basics()
	{
		String source =
				"package first.second;\n" +
				"import java.util.*;\n" +
				"import javax.swing.JLabel;\n" +
				"public class Test1\n" +
				"{\n" +
				"	public static void main(String[] args)\n" +
				"	{\n" +
				"		System.out.println(\"Hello world\");\n" +
				"	}\n" +
				"}";
		HelperMethods.stringToFile(source, "Test1.java");
		JavaC compiler = new JavaC();

		/// Tokenizing
		String expectedTokens = JavaC.HEADER_TEXT +
				"package\nidentifier:first\ndot\nidentifier:second\n" +
				"semicolon\nimport\nidentifier:java\ndot\nidentifier:util\n" +
				"dot\nstar\nsemicolon\nimport\nidentifier:javax\ndot\n" +
				"identifier:swing\ndot\nidentifier:JLabel\nsemicolon\n" +
				"public\nclass\nidentifier:Test1\ncurl_brace_left\npublic\n" +
				"static\nidentifier:void\nidentifier:main\nparen_left\n" +
				"identifier:String\nsquare_brace_left\nsquare_brace_right\n" +
				"identifier:args\nparen_right\ncurl_brace_left\n" +
				"identifier:System\ndot\nidentifier:out\ndot\n" +
				"identifier:println\nparen_left\nstring:Hello world\n" +
				"paren_right\nsemicolon\ncurl_brace_right\ncurl_brace_right\n";
		String actualTokens =
				compiler.compile(new String[] {"Test1.java", "-t"});
		assertEquals(expectedTokens, actualTokens);
		
		/// Parsing
		String expectedParsed = JavaC.HEADER_TEXT + 
				"<CompilationUnit importCount='2' typeCount='1'>\n" +
				"	<PackageDeclaration identifier='first.second'/>\n" +
				"	<ImportDeclaration isOnDemaned='true' isStatic='false' identifier='java.util'/>\n" +
				"	<ImportDeclaration isOnDemaned='false' isStatic='false' identifier='javax.swing.JLabel'/>\n" +
				"	<ClassDeclaration name='Test1' modifiers='public'>\n" +
				"		<MemberDeclaration name='main' type='void'  modifiers='public static' isMethod='true'>\n" +
				"			<MethodPart>\n" +
				"				<List_FormalParameters>\n" +
				"					<VariableDeclaration name='args' type='String' arrayDimensions='1'/>\n" +
				"				</List_FormalParameters>\n" +
				"				<CodeBlock/>\n" +
				"			</MethodPart>\n" +
				"		</MemberDeclaration>\n" +
				"	</ClassDeclaration>\n" +
				"</CompilationUnit>\n";
		String actualParsed =
				compiler.compile(new String[] {"Test1.java", "-p"});
		assertEquals(expectedParsed, actualParsed);
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

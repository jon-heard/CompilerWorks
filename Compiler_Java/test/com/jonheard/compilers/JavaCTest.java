package com.jonheard.compilers;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.jonheard.util.UtilMethods;

public class JavaCTest
{
	private static final String TEST_DIR = "testData";
	private static final String TEST_FILENAME_1 = "Test1.java";

	private void setupTestData()
	{
		File testDir = new File(TEST_DIR);
		if(testDir.exists())
		{
			String[]entries = testDir.list();
			for(String s: entries){
			    File currentFile = new File(testDir.getPath(), s);
			    currentFile.delete();
			}
		}
		else
		{
			testDir.mkdir();			
		}
	}

	@Test
	public void basics()
	{
		setupTestData();
		
		String source =
				"package first.second;\n" +
				"import java.lang.*;\n" +
				"import javax.swing.JLabel;\n" +
				"public class Test1\n" +
				"{\n" +
				"	public static void main(java.lang.String[] args)\n" +
				"	{\n" +
				"		System.out.println(\"Hello world\");\n" +
				"	}\n" +
				"}";
		UtilMethods.stringToFile(source, TEST_DIR+"/"+TEST_FILENAME_1);
		JavaC compiler = new JavaC();

		/// Tokenizing
		String expectedTokens = JavaC.HEADER_TEXT +
				"package\nidentifier:first\ndot\nidentifier:second\n" +
				"semicolon\nimport\nidentifier:java\ndot\nidentifier:lang\n" +
				"dot\nstar\nsemicolon\nimport\nidentifier:javax\ndot\n" +
				"identifier:swing\ndot\nidentifier:JLabel\nsemicolon\n" +
				"public\nclass\nidentifier:Test1\ncurl_brace_left\npublic\n" +
				"static\nidentifier:void\nidentifier:main\nparen_left\n" +
				"identifier:java\ndot\nidentifier:lang\ndot\n" +
				"identifier:String\nsquare_brace_left\nsquare_brace_right\n" +
				"identifier:args\nparen_right\ncurl_brace_left\n" +
				"identifier:System\ndot\nidentifier:out\ndot\n" +
				"identifier:println\nparen_left\nstring:Hello world\n" +
				"paren_right\nsemicolon\ncurl_brace_right\ncurl_brace_right\n";
		String actualTokens = compiler.compile(
				new String[] {TEST_DIR+"/"+TEST_FILENAME_1, "-t"});
		assertEquals(expectedTokens, actualTokens);
		
		/// Parsing
		String expectedParsed = JavaC.HEADER_TEXT + 
				"<CompilationUnit line='1' importCount='2' typeCount='1'>\n" +
				"	<Package line='1' identifier='first.second'/>\n" +
				"	<Import line='2' isOnDemand='true' isStatic='false' identifier='java.lang'/>\n" +
				"	<Import line='3' isOnDemand='false' isStatic='false' identifier='javax.swing.JLabel'/>\n" +
				"	<Class line='4' name='Test1' modifiers='public'>\n" +
				"		<Method line='6' name='main' type='void' modifiers='public static'>\n" +
				"			<List_Variable line='6'>\n" +
				"				<Variable line='6' name='args' type='java.lang.String[]' modifiers=''/>\n" +
				"			</List_Variable>\n" +
				"			<CodeBlock line='7'>\n" +
				"				<MethodCall line='8'>\n" +
				"					<QualifiedIdentifier line='8' value='System.out.println'/>\n" +
				"					<List_Expression line='8'>\n" +
				"						<Literal_string line='8' value='Hello world'/>\n" +
				"					</List_Expression>\n" +
				"				</MethodCall>\n" +
				"			</CodeBlock>\n" +
				"		</Method>\n" +
				"	</Class>\n" +
				"</CompilationUnit>\n";
		String actualParsed = compiler.compile(
				new String[] {TEST_DIR+"/"+TEST_FILENAME_1, "-p"});
		assertEquals(expectedParsed, actualParsed);

//		String actualProcessed = compiler.compile(
//				new String[] {TEST_DIR+"/"+TEST_FILENAME_1, "-r"});
		System.out.println(actualParsed);
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

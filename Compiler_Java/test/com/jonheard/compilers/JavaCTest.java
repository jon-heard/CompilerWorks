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
				"import java.util.*;\n" +
				"import javax.swing.JLabel;\n" +
				"public class Test1\n" +
				"{\n" +
				"	static int i = 1;\n" +
				"	public static void main(String[] args)\n" +
				"	{\n" +
				"		int j = 3;\n" +
				"		System.out.println(i + j);\n" +
				"	}\n" +
				"}";
		UtilMethods.stringToFile(source, TEST_DIR+"/"+TEST_FILENAME_1);
		JavaC compiler = new JavaC();

		// Tokenizing
		String expectedTokens = JavaC.HEADER_TEXT +
				"package\nid:first\ndot\nid:second\n" +
				"semicolon\nimport\nid:java\ndot\nid:util\n" +
				"dot\nstar\nsemicolon\nimport\nid:javax\ndot\n" +
				"id:swing\ndot\nid:JLabel\nsemicolon\n" +
				"public\nclass\nid:Test1\ncurl_brace_left\nstatic\nid:int\n" +
				"id:i\nsemicolon\npublic\nstatic\nid:void\nid:main\n" +
				"paren_left\nid:String\nsquare_brace_left\n" +
				"square_brace_right\nid:args\nparen_right\ncurl_brace_left\n" +
				"id:System\ndot\nid:out\ndot\nid:println\nparen_left\nid:i\n" +
				"paren_right\nsemicolon\ncurl_brace_right\ncurl_brace_right\n";
		String actualTokens = compiler.compile(
				new String[] {TEST_DIR+"/"+TEST_FILENAME_1, "-t"});
		//assertEquals(expectedTokens, actualTokens);
		
		// Parsing
		String expectedParsed = JavaC.HEADER_TEXT + 
				"<CompilationUnit line='1' importCount='2' typeCount='1'>\n" +
				"	<Package line='1' id='first.second'/>\n" +
				"	<Import line='2' isOnDemand='true' isStatic='false' id='java.util'/>\n" +
				"	<Import line='3' isOnDemand='false' isStatic='false' id='javax.swing.JLabel'/>\n" +
				"	<Class line='4' name='Test1' modifiers='public'>\n" +
				"		<Variable line='6' name='i' type='int' modifiers='static'/>\n" +
				"		<Method line='7' name='main' type='void' modifiers='public static'>\n" +
				"			<List_Variable line='7'>\n" +
				"				<Variable line='7' name='args' type='String[]' modifiers=''/>\n" +
				"			</List_Variable>\n" +
				"			<CodeBlock line='8'>\n" +
				"				<MethodCall line='9'>\n" +
				"					<QualifiedId line='9' value='System.out.println'/>\n" +
				"					<List_Expression line='9'>\n" +
				"						<VariableReference line='9' name='i'/>\n" +
				"					</List_Expression>\n" +
				"				</MethodCall>\n" +
				"			</CodeBlock>\n" +
				"		</Method>\n" +
				"	</Class>\n" +
				"</CompilationUnit>\n";
		String actualParsed = compiler.compile(
				new String[] {TEST_DIR+"/"+TEST_FILENAME_1, "-p"});
		//assertEquals(expectedParsed, actualParsed);
		System.out.println(actualParsed);

		// Processing
//		String expectedProcessed = JavaC.HEADER_TEXT + 
//				"<CompilationUnit line='1' importCount='2' typeCount='1'>\n" +
//				"	<Package line='1' id='first.second'/>\n" +
//				"	<Import line='2' isOnDemand='true' isStatic='false' id='java.util'/>\n" +
//				"	<Import line='3' isOnDemand='false' isStatic='false' id='javax.swing.JLabel'/>\n" +
//				"	<Class line='4' name='Test1' modifiers='public'>\n" +
//				"		<Method line='6' name='main' type='void' modifiers='public static'>\n" +
//				"			<List_Variable line='6'>\n" +
//				"				<Variable line='6' name='args' type='java.lang.String[]' modifiers=''/>\n" +
//				"			</List_Variable>\n" +
//				"			<CodeBlock line='7'>\n" +
//				"				<MethodCall line='8'>\n" +
//				"					<QualifiedId line='8' value='System.out.println'/>\n" +
//				"					<List_Expression line='8'>\n" +
//				"						<Literal_string line='8' value='Hello world'/>\n" +
//				"					</List_Expression>\n" +
//				"				</MethodCall>\n" +
//				"			</CodeBlock>\n" +
//				"		</Method>\n" +
//				"	</Class>\n" +
//				"</CompilationUnit>\n";
		//String actualProcessed = compiler.compile(
		//		new String[] {TEST_DIR+"/"+TEST_FILENAME_1, "-r"});
		//System.out.println(actualProcessed);
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

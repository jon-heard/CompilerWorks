package com.jonheard.compilers;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.Test;

import com.jonheard.util.UtilMethods;

public class JavaCTest {
  private static final String TEST_DIR = "testData";
  private static final String TEST_FILENAME_1 = "Test1.java";

  private void setupTestData() {
    File testDir = new File(TEST_DIR);
    if (testDir.exists()) {
      String[] entries = testDir.list();
      for (String s : entries) {
        File currentFile = new File(testDir.getPath(), s);
        currentFile.delete();
      }
    } else {
      testDir.mkdir();
    }
  }

  @Test
  public void basics() {
    setupTestData();

    String source = "public class Test1\n" + "{\n" + "	static int var1 = 4;\n"
        + "	public static void main(String[] args)\n" + "	{\n" + "		int var2 = 3;\n"
        + "		System.out.println(var1 + var2);\n" + "	}\n" + "}";
    UtilMethods.stringToFile(source, TEST_DIR + "/" + TEST_FILENAME_1);
    JavaC compiler = new JavaC();

    // Tokenizing
    String expectedTokens = JavaC.APP_INFO_TEXT + "package\nid:first\ndot\nid:second\n"
        + "semicolon\nimport\nid:java\ndot\nid:util\n"
        + "dot\nstar\nsemicolon\nimport\nid:javax\ndot\n" + "id:swing\ndot\nid:JLabel\nsemicolon\n"
        + "public\nclass\nid:Test1\ncurl_brace_left\nstatic\nid:int\n"
        + "id:i\nsemicolon\npublic\nstatic\nid:void\nid:main\n"
        + "paren_left\nid:String\nsquare_brace_left\n"
        + "square_brace_right\nid:args\nparen_right\ncurl_brace_left\n"
        + "id:System\ndot\nid:out\ndot\nid:println\nparen_left\nid:i\n"
        + "paren_right\nsemicolon\ncurl_brace_right\ncurl_brace_right\n";
    String actualTokens = compiler.compile(new String[] { TEST_DIR + "/" + TEST_FILENAME_1, "-t" });
    // assertEquals(expectedTokens, actualTokens);

    // Parsing
    String expectedParsed = JavaC.APP_INFO_TEXT
        + "<CompilationUnit row='1' importCount='2' typeCount='1'>\n"
        + "	<Package row='1' id='first.second'/>\n"
        + "	<Import row='2' isOnDemand='true' isStatic='false' id='java.util'/>\n"
        + "	<Import row='3' isOnDemand='false' isStatic='false' id='javax.swing.JLabel'/>\n"
        + "	<Class row='4' id='Test1' modifiers='public'>\n"
        + "		<Variable row='6' id='i' type='int' modifiers='static'/>\n"
        + "		<Method row='7' id='main' type='void' modifiers='public static'>\n"
        + "			<List_Variable row='7'>\n"
        + "				<Variable row='7' id='args' type='String[]' modifiers=''/>\n"
        + "			</List_Variable>\n" + "			<CodeBlock row='8'>\n"
        + "				<MethodCall row='9'>\n"
        + "					<QualifiedId row='9' value='System.out.println'/>\n"
        + "					<List_Expression row='9'>\n"
        + "						<VariableReference row='9' id='i'/>\n" + "					</List_Expression>\n"
        + "				</MethodCall>\n" + "			</CodeBlock>\n" + "		</Method>\n" + "	</Class>\n"
        + "</CompilationUnit>\n";
    String actualParsed = compiler.compile(new String[] { TEST_DIR + "/" + TEST_FILENAME_1, "-p" });
    // assertEquals(expectedParsed, actualParsed);
    System.out.println(actualParsed);

    // Processing
    String expectedProcessed = JavaC.APP_INFO_TEXT
        + "<CompilationUnit row='1' importCount='2' typeCount='1'>\n"
        + "	<Package row='1' id='first.second'/>\n"
        + "	<Import row='2' isOnDemand='true' isStatic='false' id='java.util'/>\n"
        + "	<Import row='3' isOnDemand='false' isStatic='false' id='javax.swing.JLabel'/>\n"
        + "	<Class row='4' id='Test1' modifiers='public'>\n"
        + "		<Method row='6' id='main' type='void' modifiers='public static'>\n"
        + "			<List_Variable row='6'>\n"
        + "				<Variable row='6' id='args' type='java.lang.String[]' modifiers=''/>\n"
        + "			</List_Variable>\n" + "			<CodeBlock row='7'>\n"
        + "				<MethodCall row='8'>\n"
        + "					<QualifiedId row='8' value='System.out.println'/>\n"
        + "					<List_Expression row='8'>\n"
        + "						<Literal_string row='8' value='Hello world'/>\n"
        + "					</List_Expression>\n" + "				</MethodCall>\n" + "			</CodeBlock>\n"
        + "		</Method>\n" + "	</Class>\n" + "</CompilationUnit>\n";
    String actualProcessed = compiler
        .compile(new String[] { TEST_DIR + "/" + TEST_FILENAME_1, "-r" });
    System.out.println(actualProcessed);

    // Processing
    String expectedGenerated = JavaC.APP_INFO_TEXT + "";
    String actualGenerated = compiler
        .compile(new String[] { TEST_DIR + "/" + TEST_FILENAME_1, "-g" });
    System.out.println(actualGenerated);

    String finalOutput = compiler.compile(new String[] { TEST_DIR + "/" + TEST_FILENAME_1 });
  }

  @Test
  public void help() {
    JavaC compiler = new JavaC();
    String expected = JavaC.APP_INFO_TEXT + JavaC.HELP_TEXT;
    String actual = compiler.compile(new String[] { "-h" });
    assertEquals(expected, actual);
  }
}

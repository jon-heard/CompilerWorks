
package com.jonheard.compilers.javaTokenizer;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.compilers.javaTokenizer.JavaTokenizer;
import com.jonheard.util.Logger;

public class JavaTokenIteratorTest
{
	@Test
	public void basic()
	{
		String source = "public class private";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(3, tokens.size());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(0).getType());
		assertEquals(JavaTokenType._CLASS,			tokens.get(1).getType());
		assertEquals(JavaTokenType._PRIVATE,		tokens.get(2).getType());
	}
	
	@Test
	public void linesRowsCols()
	{
		String source = "public class private \nprotected public\r\n class";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(6, tokens.size());

		assertEquals(JavaTokenType._PUBLIC,			tokens.get(0).getType());
		assertEquals(1, tokens.get(0).getRow());
		assertEquals(0, tokens.get(0).getCol());
		assertEquals("public class private ", tokens.get(0).getLine());
		assertEquals(JavaTokenType._CLASS,			tokens.get(1).getType());
		assertEquals(1, tokens.get(1).getRow());
		assertEquals(7, tokens.get(1).getCol());
		assertEquals("public class private ", tokens.get(1).getLine());
		assertEquals(JavaTokenType._PRIVATE,		tokens.get(2).getType());
		assertEquals(1, tokens.get(2).getRow());
		assertEquals(13, tokens.get(2).getCol());
		assertEquals("public class private ", tokens.get(2).getLine());
		assertEquals(JavaTokenType._PROTECTED,		tokens.get(3).getType());
		assertEquals(2, tokens.get(3).getRow());
		assertEquals(0, tokens.get(3).getCol());
		assertEquals("protected public", tokens.get(3).getLine());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(4).getType());
		assertEquals(2, tokens.get(4).getRow());
		assertEquals(10, tokens.get(4).getCol());
		assertEquals("protected public", tokens.get(4).getLine());
		assertEquals(JavaTokenType._CLASS,			tokens.get(5).getType());
		assertEquals(3, tokens.get(5).getRow());
		assertEquals(1, tokens.get(5).getCol());
		assertEquals(" class", tokens.get(5).getLine());
	}

	@Test
	public void spacing()
	{
		String source = "public  classprivate\tprotected\rpublic\nclass " +
				"\n\r\t\r\n\t private";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(7, tokens.size());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(0).getType());
		assertEquals(JavaTokenType._CLASS,			tokens.get(1).getType());
		assertEquals(JavaTokenType._PRIVATE,		tokens.get(2).getType());
		assertEquals(JavaTokenType._PROTECTED,		tokens.get(3).getType());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(4).getType());
		assertEquals(JavaTokenType._CLASS,			tokens.get(5).getType());
		assertEquals(JavaTokenType._PRIVATE,		tokens.get(6).getType());
	}
	
	@Test
	public void comments()
	{
		String source = "public//1\nclass//2\rprivate//3\r\nprotected//\t" +
				"class\n/*4*/public";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(5, tokens.size());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(0).getType());
		assertEquals(JavaTokenType._CLASS,			tokens.get(1).getType());
		assertEquals(JavaTokenType._PRIVATE,		tokens.get(2).getType());
		assertEquals(JavaTokenType._PROTECTED,		tokens.get(3).getType());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(4).getType());
	}
	
	@Test
	public void identifiersAndOperators()
	{
		String source = "public=t+class/test;";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(8, tokens.size());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(0).getType());
		assertEquals(JavaTokenType.EQUAL,			tokens.get(1).getType());
		assertEquals(JavaTokenType.IDENTIFIER,		tokens.get(2).getType());
			assertEquals("t",						tokens.get(2).getText());
		assertEquals(JavaTokenType.PLUS,			tokens.get(3).getType());
		assertEquals(JavaTokenType._CLASS,			tokens.get(4).getType());
		assertEquals(JavaTokenType.SLASH,			tokens.get(5).getType());
		assertEquals(JavaTokenType.IDENTIFIER,		tokens.get(6).getType());
			assertEquals("test",					tokens.get(6).getText());
		assertEquals(JavaTokenType.SEMICOLON,		tokens.get(7).getType());
	}

	@Test
	public void TextOutput()
	{
		String source = "public=t+class/test;";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		String expected = "public\nequal\nidentifier:t\nplus\nclass\n" +
				"slash\nidentifier:test\nsemicolon\n";
		String actual = iterator.tokenizeToString();
		assertEquals(expected, actual);
	}

	@Test
	public void CharAndString()
	{
		String source = "'a' '~' '\n' \"hello\" \"\" \"\\n\"";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(6, tokens.size());
		assertEquals(JavaTokenType.CHAR,			tokens.get(0).getType());
			assertEquals("a",						tokens.get(0).getText());
		assertEquals(JavaTokenType.CHAR,			tokens.get(1).getType());
			assertEquals("~",						tokens.get(1).getText());
		assertEquals(JavaTokenType.CHAR,			tokens.get(2).getType());
			assertEquals("\n",						tokens.get(2).getText());
		assertEquals(JavaTokenType.STRING,			tokens.get(3).getType());
			assertEquals("hello",					tokens.get(3).getText());
		assertEquals(JavaTokenType.STRING,			tokens.get(4).getType());
			assertEquals("",						tokens.get(4).getText());
		assertEquals(JavaTokenType.STRING,			tokens.get(5).getType());
			assertEquals("\\n",						tokens.get(5).getText());
	}

	@Test
	public void integers()
	{
		String source = "56 56l 056 056L 0x56 0x56l 0 0L";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(8, tokens.size());
		assertEquals(JavaTokenType.INTEGER,			tokens.get(0).getType());
			assertEquals("56",						tokens.get(0).getText());
		assertEquals(JavaTokenType.LONG,			tokens.get(1).getType());
			assertEquals("56",						tokens.get(1).getText());
		assertEquals(JavaTokenType.INTEGER,			tokens.get(2).getType());
			assertEquals("46",						tokens.get(2).getText());
		assertEquals(JavaTokenType.LONG,			tokens.get(3).getType());
			assertEquals("46",						tokens.get(3).getText());
		assertEquals(JavaTokenType.INTEGER,			tokens.get(4).getType());
			assertEquals("86",						tokens.get(4).getText());
		assertEquals(JavaTokenType.LONG,			tokens.get(5).getType());
			assertEquals("86",						tokens.get(5).getText());
		assertEquals(JavaTokenType.INTEGER,			tokens.get(6).getType());
			assertEquals("0",						tokens.get(6).getText());
		assertEquals(JavaTokenType.LONG,			tokens.get(7).getType());
			assertEquals("0",						tokens.get(7).getText());
	}
	
	@Test
	public void floatingPoint()
	{
		String source = "56f 56d 056F 056D " +
				"0.5f 0.5 5e7f 5e7 5.1e7f 5.1e7 " +
				".5f .5";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(12, tokens.size());
		assertEquals(JavaTokenType.FLOAT,			tokens.get( 0).getType());
			assertEquals("56.0",					tokens.get( 0).getText());
		assertEquals(JavaTokenType.DOUBLE,			tokens.get( 1).getType());
			assertEquals("56.0",					tokens.get( 1).getText());
		assertEquals(JavaTokenType.FLOAT,			tokens.get( 2).getType());
			assertEquals("56.0",					tokens.get( 2).getText());
		assertEquals(JavaTokenType.DOUBLE,			tokens.get( 3).getType());
			assertEquals("56.0",					tokens.get( 3).getText());
		assertEquals(JavaTokenType.FLOAT,			tokens.get( 4).getType());
			assertEquals("0.5",						tokens.get( 4).getText());
		assertEquals(JavaTokenType.DOUBLE,			tokens.get( 5).getType());
			assertEquals("0.5",						tokens.get( 5).getText());
		assertEquals(JavaTokenType.FLOAT,			tokens.get( 6).getType());
			assertEquals("5.0E7",					tokens.get( 6).getText());
		assertEquals(JavaTokenType.DOUBLE,			tokens.get( 7).getType());
			assertEquals("5.0E7",					tokens.get( 7).getText());
		assertEquals(JavaTokenType.FLOAT,			tokens.get( 8).getType());
			assertEquals("5.1E7",					tokens.get( 8).getText());
		assertEquals(JavaTokenType.DOUBLE,			tokens.get( 9).getType());
			assertEquals("5.1E7",					tokens.get( 9).getText());
		assertEquals(JavaTokenType.FLOAT,			tokens.get(10).getType());
			assertEquals("0.5",						tokens.get(10).getText());
		assertEquals(JavaTokenType.DOUBLE,			tokens.get(11).getType());
			assertEquals("0.5",						tokens.get(11).getText());
	}
	
	@Test
	public void errors_illegalCharacter()
	{
		Logger.clearLogs();
		String source = "#";
		JavaTokenizer iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		String expected =
				"Test1.java:1: error: illegal character: #\n\t#\n\t^\n";
		assertEquals(expected, Logger.getLogs());
	}
	
	@Test
	public void errors_unclosedCharacterLiteral()
	{
		String source, expected;
		JavaTokenizer iterator;

		Logger.clearLogs();
		source = "a'";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\ta'\n\t ^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "'a";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'a\n\t^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "'aa";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'aa\n\t  ^\n";
		assertEquals(expected, Logger.getLogs());
		
		Logger.clearLogs();
		source = "'\\";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'\\\n\t^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "'\\n";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'\\n\n\t ^\n";
		assertEquals(expected, Logger.getLogs());
		
		Logger.clearLogs();
		source = "'\\na";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'\\na\n\t   ^\n";
		assertEquals(expected, Logger.getLogs());
	}
	
	
	@Test
	public void errors_unclosedStringLiteral()
	{
		String source, expected;
		JavaTokenizer iterator;

		Logger.clearLogs();
		source = "\"abc\n\"";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed string literal\n\t\"abc\n\t^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "\"abc";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed string literal\n\t\"abc\n\t^\n";
		assertEquals(expected, Logger.getLogs());
		
		Logger.clearLogs();
		source = "\"";
		iterator = new JavaTokenizer("Test1.java", source);
		iterator.tokenize();
		expected =
				"Test1.java:1: error: " +
				"unclosed string literal\n\t\"\n\t^\n";
		assertEquals(expected, Logger.getLogs());
	}

	@Test
	public void symbols()
	{
		String source = "++ -- + - ~ ! * / % << >> >>> < > <= >= == != & ^ " +
				"| && || ? : = += -= *= /= %= &= ^= |= <<= >>= >>>= . ; , " +
				"{ } [ ] ( )";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(46, tokens.size());
		assertEquals(JavaTokenType.PLUS_PLUS,		tokens.get( 0).getType());
		assertEquals(JavaTokenType.DASH_DASH,		tokens.get( 1).getType());
		assertEquals(JavaTokenType.PLUS,			tokens.get( 2).getType());
		assertEquals(JavaTokenType.DASH,			tokens.get( 3).getType());
		assertEquals(JavaTokenType.TILDE,			tokens.get( 4).getType());
		assertEquals(JavaTokenType.EXCLAIM,			tokens.get( 5).getType());
		assertEquals(JavaTokenType.STAR,			tokens.get( 6).getType());
		assertEquals(JavaTokenType.SLASH,			tokens.get( 7).getType());
		assertEquals(JavaTokenType.PERCENT,			tokens.get( 8).getType());
		assertEquals(JavaTokenType.LEFT_LEFT,		tokens.get( 9).getType());
		assertEquals(JavaTokenType.RIGHT_RIGHT,		tokens.get(10).getType());
		assertEquals(JavaTokenType.RIGHT_RIGHT_RIGHT,tokens.get(11).getType());
		assertEquals(JavaTokenType.LEFT,			tokens.get(12).getType());
		assertEquals(JavaTokenType.RIGHT,			tokens.get(13).getType());
		assertEquals(JavaTokenType.LEFT_EQUAL,		tokens.get(14).getType());
		assertEquals(JavaTokenType.RIGHT_EQUAL,		tokens.get(15).getType());
		assertEquals(JavaTokenType.EQUAL_EQUAL,		tokens.get(16).getType());
		assertEquals(JavaTokenType.EXCLAIM_EQUAL,	tokens.get(17).getType());
		assertEquals(JavaTokenType.AND,				tokens.get(18).getType());
		assertEquals(JavaTokenType.CARAT,			tokens.get(19).getType());
		assertEquals(JavaTokenType.PIPE,			tokens.get(20).getType());
		assertEquals(JavaTokenType.AND_AND,			tokens.get(21).getType());
		assertEquals(JavaTokenType.PIPE_PIPE,		tokens.get(22).getType());
		assertEquals(JavaTokenType.QUESTION,		tokens.get(23).getType());
		assertEquals(JavaTokenType.COLON,			tokens.get(24).getType());
		assertEquals(JavaTokenType.EQUAL,			tokens.get(25).getType());
		assertEquals(JavaTokenType.PLUS_EQUAL,		tokens.get(26).getType());
		assertEquals(JavaTokenType.DASH_EQUAL,		tokens.get(27).getType());
		assertEquals(JavaTokenType.STAR_EQUAL,		tokens.get(28).getType());
		assertEquals(JavaTokenType.SLASH_EQUAL,		tokens.get(29).getType());
		assertEquals(JavaTokenType.PERCENT_EQUAL,	tokens.get(30).getType());
		assertEquals(JavaTokenType.AND_EQUAL,		tokens.get(31).getType());
		assertEquals(JavaTokenType.CARAT_EQUAL,		tokens.get(32).getType());
		assertEquals(JavaTokenType.PIPE_EQUAL,		tokens.get(33).getType());
		assertEquals(JavaTokenType.LEFT_LEFT_EQUAL,	tokens.get(34).getType());
		assertEquals(JavaTokenType.RIGHT_RIGHT_EQUAL,tokens.get(35).getType());
		assertEquals(
				JavaTokenType.RIGHT_RIGHT_RIGHT_EQUAL,
				tokens.get(36).getType());
		assertEquals(JavaTokenType.DOT,				tokens.get(37).getType());
		assertEquals(JavaTokenType.SEMICOLON,		tokens.get(38).getType());
		assertEquals(JavaTokenType.COMMA,			tokens.get(39).getType());
		assertEquals(JavaTokenType.CURL_BRACE_LEFT,	tokens.get(40).getType());
		assertEquals(JavaTokenType.CURL_BRACE_RIGHT,tokens.get(41).getType());
		assertEquals(JavaTokenType.SQUARE_BRACE_LEFT,tokens.get(42).getType());
		assertEquals(JavaTokenType.SQUARE_BRACE_RIGHT,tokens.get(43).getType());
		assertEquals(JavaTokenType.PAREN_LEFT,      tokens.get(44).getType());
		assertEquals(JavaTokenType.PAREN_RIGHT,     tokens.get(45).getType());
	}

	@Test
	public void keywords()
	{
		String source = "abstract assert break case catch " +
				"class const continue default do else enum extends " +
				"false final finally for goto if implements import " +
				"instanceof interface native new null package private " +
				"protected public return static strictfp super switch " +
				"synchronized this throw throws transient true try " +
				"volatile while";
		JavaTokenizer iterator = new JavaTokenizer("", source);
		List<JavaToken> tokens = iterator.tokenize();
		assertEquals(44, tokens.size());
		assertEquals(JavaTokenType._ABSTRACT,		tokens.get( 0).getType());
		assertEquals(JavaTokenType._ASSERT,			tokens.get( 1).getType());
		assertEquals(JavaTokenType._BREAK,			tokens.get( 2).getType());
		assertEquals(JavaTokenType._CASE,			tokens.get( 3).getType());
		assertEquals(JavaTokenType._CATCH,			tokens.get( 4).getType());
		assertEquals(JavaTokenType._CLASS,			tokens.get( 5).getType());
		assertEquals(JavaTokenType._CONST,			tokens.get( 6).getType());
		assertEquals(JavaTokenType._CONTINUE,		tokens.get( 7).getType());
		assertEquals(JavaTokenType._DEFAULT, 		tokens.get( 8).getType());
		assertEquals(JavaTokenType._DO,				tokens.get( 9).getType());
		assertEquals(JavaTokenType._ELSE,			tokens.get(10).getType());
		assertEquals(JavaTokenType._ENUM,			tokens.get(11).getType());
		assertEquals(JavaTokenType._EXTENDS,		tokens.get(12).getType());
		assertEquals(JavaTokenType._FALSE,			tokens.get(13).getType());
		assertEquals(JavaTokenType._FINAL,			tokens.get(14).getType());
		assertEquals(JavaTokenType._FINALLY,		tokens.get(15).getType());
		assertEquals(JavaTokenType._FOR,			tokens.get(16).getType());
		assertEquals(JavaTokenType._GOTO,			tokens.get(17).getType());
		assertEquals(JavaTokenType._IF,				tokens.get(18).getType());
		assertEquals(JavaTokenType._IMPLEMENTS,		tokens.get(19).getType());
		assertEquals(JavaTokenType._IMPORT,			tokens.get(20).getType());
		assertEquals(JavaTokenType._INSTANCEOF,		tokens.get(21).getType());
		assertEquals(JavaTokenType._INTERFACE,		tokens.get(22).getType());
		assertEquals(JavaTokenType._NATIVE,			tokens.get(23).getType());
		assertEquals(JavaTokenType._NEW,			tokens.get(24).getType());
		assertEquals(JavaTokenType._NULL,			tokens.get(25).getType());
		assertEquals(JavaTokenType._PACKAGE,		tokens.get(26).getType());
		assertEquals(JavaTokenType._PRIVATE,		tokens.get(27).getType());
		assertEquals(JavaTokenType._PROTECTED,		tokens.get(28).getType());
		assertEquals(JavaTokenType._PUBLIC,			tokens.get(29).getType());
		assertEquals(JavaTokenType._RETURN,			tokens.get(30).getType());
		assertEquals(JavaTokenType._STATIC,			tokens.get(31).getType());
		assertEquals(JavaTokenType._STRICTFP,		tokens.get(32).getType());
		assertEquals(JavaTokenType._SUPER,			tokens.get(33).getType());
		assertEquals(JavaTokenType._SWITCH,			tokens.get(34).getType());
		assertEquals(JavaTokenType._SYNCHRONIZED,	tokens.get(35).getType());
		assertEquals(JavaTokenType._THIS,			tokens.get(36).getType());
		assertEquals(JavaTokenType._THROW,			tokens.get(37).getType());
		assertEquals(JavaTokenType._THROWS,			tokens.get(38).getType());
		assertEquals(JavaTokenType._TRANSIENT,		tokens.get(39).getType());
		assertEquals(JavaTokenType._TRUE,			tokens.get(40).getType());
		assertEquals(JavaTokenType._TRY,			tokens.get(41).getType());
		assertEquals(JavaTokenType._VOLATILE,		tokens.get(42).getType());
		assertEquals(JavaTokenType._WHILE,			tokens.get(43).getType());
	}
}

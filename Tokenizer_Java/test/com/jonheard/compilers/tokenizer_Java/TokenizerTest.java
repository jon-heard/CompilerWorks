
package com.jonheard.compilers.tokenizer_java;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.compilers.tokenizer_java.Tokenizer;
import com.jonheard.util.Logger;
import com.jonheard.util.SourceFileInfo;

public class TokenizerTest
{
	public TokenizerTest()
	{
		Logger.setPrintingToConsole(false);
	}

	@Test
	public void basic()
	{
		String source = "public class private";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(3, tokens.size());
		assertEquals(TokenType._PUBLIC,			tokens.get(0).getTokenType());
		assertEquals(TokenType._CLASS,			tokens.get(1).getTokenType());
		assertEquals(TokenType._PRIVATE,		tokens.get(2).getTokenType());
	}
	
	@Test
	public void linesAndCols()
	{
		String source = "public class private \nprotected public\r\n class";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(6, tokens.size());

		assertEquals(TokenType._PUBLIC,			tokens.get(0).getTokenType());
		assertEquals(1, tokens.get(0).getLine());
		assertEquals(0, tokens.get(0).getColumn());
		assertEquals(TokenType._CLASS,			tokens.get(1).getTokenType());
		assertEquals(1, tokens.get(1).getLine());
		assertEquals(7, tokens.get(1).getColumn());
		assertEquals(TokenType._PRIVATE,		tokens.get(2).getTokenType());
		assertEquals(1, tokens.get(2).getLine());
		assertEquals(13, tokens.get(2).getColumn());
		assertEquals(TokenType._PROTECTED,		tokens.get(3).getTokenType());
		assertEquals(2, tokens.get(3).getLine());
		assertEquals(0, tokens.get(3).getColumn());
		assertEquals(TokenType._PUBLIC,			tokens.get(4).getTokenType());
		assertEquals(2, tokens.get(4).getLine());
		assertEquals(10, tokens.get(4).getColumn());
		assertEquals(TokenType._CLASS,			tokens.get(5).getTokenType());
		assertEquals(3, tokens.get(5).getLine());
		assertEquals(1, tokens.get(5).getColumn());
	}

	@Test
	public void spacing()
	{
		String source = "public  classprivate\tprotected\rpublic\nclass " +
				"\n\r\t\r\n\t private";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(7, tokens.size());
		assertEquals(TokenType._PUBLIC,			tokens.get(0).getTokenType());
		assertEquals(TokenType._CLASS,			tokens.get(1).getTokenType());
		assertEquals(TokenType._PRIVATE,		tokens.get(2).getTokenType());
		assertEquals(TokenType._PROTECTED,		tokens.get(3).getTokenType());
		assertEquals(TokenType._PUBLIC,			tokens.get(4).getTokenType());
		assertEquals(TokenType._CLASS,			tokens.get(5).getTokenType());
		assertEquals(TokenType._PRIVATE,		tokens.get(6).getTokenType());
	}
	
	@Test
	public void comments()
	{
		String source = "public//1\nclass//2\rprivate//3\r\nprotected//\t" +
				"class\n/*4*/public";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(5, tokens.size());
		assertEquals(TokenType._PUBLIC,			tokens.get(0).getTokenType());
		assertEquals(TokenType._CLASS,			tokens.get(1).getTokenType());
		assertEquals(TokenType._PRIVATE,		tokens.get(2).getTokenType());
		assertEquals(TokenType._PROTECTED,		tokens.get(3).getTokenType());
		assertEquals(TokenType._PUBLIC,			tokens.get(4).getTokenType());
	}
	
	@Test
	public void idsAndOperators()
	{
		String source = "public=t+class/test;";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(8, tokens.size());
		assertEquals(TokenType._PUBLIC,			tokens.get(0).getTokenType());
		assertEquals(TokenType.EQUAL,			tokens.get(1).getTokenType());
		assertEquals(TokenType.ID,		tokens.get(2).getTokenType());
			assertEquals("t",						tokens.get(2).getText());
		assertEquals(TokenType.PLUS,			tokens.get(3).getTokenType());
		assertEquals(TokenType._CLASS,			tokens.get(4).getTokenType());
		assertEquals(TokenType.SLASH,			tokens.get(5).getTokenType());
		assertEquals(TokenType.ID,		tokens.get(6).getTokenType());
			assertEquals("test",					tokens.get(6).getText());
		assertEquals(TokenType.SEMICOLON,		tokens.get(7).getTokenType());
	}

	@Test
	public void CharAndString()
	{
		String source = "'a' '~' '\n' \"hello\" \"\" \"\\n\"";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(6, tokens.size());
		assertEquals(TokenType.CHAR,			tokens.get(0).getTokenType());
			assertEquals("a",						tokens.get(0).getText());
		assertEquals(TokenType.CHAR,			tokens.get(1).getTokenType());
			assertEquals("~",						tokens.get(1).getText());
		assertEquals(TokenType.CHAR,			tokens.get(2).getTokenType());
			assertEquals("\n",						tokens.get(2).getText());
		assertEquals(TokenType.STRING,			tokens.get(3).getTokenType());
			assertEquals("hello",					tokens.get(3).getText());
		assertEquals(TokenType.STRING,			tokens.get(4).getTokenType());
			assertEquals("",						tokens.get(4).getText());
		assertEquals(TokenType.STRING,			tokens.get(5).getTokenType());
			assertEquals("\\n",						tokens.get(5).getText());
	}

	@Test
	public void integers()
	{
		String source = "56 56l 056 056L 0x56 0x56l 0 0L";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(8, tokens.size());
		assertEquals(TokenType.INTEGER,			tokens.get(0).getTokenType());
			assertEquals("56",						tokens.get(0).getText());
		assertEquals(TokenType.LONG,			tokens.get(1).getTokenType());
			assertEquals("56",						tokens.get(1).getText());
		assertEquals(TokenType.INTEGER,			tokens.get(2).getTokenType());
			assertEquals("46",						tokens.get(2).getText());
		assertEquals(TokenType.LONG,			tokens.get(3).getTokenType());
			assertEquals("46",						tokens.get(3).getText());
		assertEquals(TokenType.INTEGER,			tokens.get(4).getTokenType());
			assertEquals("86",						tokens.get(4).getText());
		assertEquals(TokenType.LONG,			tokens.get(5).getTokenType());
			assertEquals("86",						tokens.get(5).getText());
		assertEquals(TokenType.INTEGER,			tokens.get(6).getTokenType());
			assertEquals("0",						tokens.get(6).getText());
		assertEquals(TokenType.LONG,			tokens.get(7).getTokenType());
			assertEquals("0",						tokens.get(7).getText());
	}
	
	@Test
	public void floatingPoint()
	{
		String source = "56f 56d 056F 056D " +
				"0.5f 0.5 5e7f 5e7 5.1e7f 5.1e7 " +
				".5f .5";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(12, tokens.size());
		assertEquals(TokenType.FLOAT,			tokens.get( 0).getTokenType());
			assertEquals("56.0",					tokens.get( 0).getText());
		assertEquals(TokenType.DOUBLE,			tokens.get( 1).getTokenType());
			assertEquals("56.0",					tokens.get( 1).getText());
		assertEquals(TokenType.FLOAT,			tokens.get( 2).getTokenType());
			assertEquals("56.0",					tokens.get( 2).getText());
		assertEquals(TokenType.DOUBLE,			tokens.get( 3).getTokenType());
			assertEquals("56.0",					tokens.get( 3).getText());
		assertEquals(TokenType.FLOAT,			tokens.get( 4).getTokenType());
			assertEquals("0.5",						tokens.get( 4).getText());
		assertEquals(TokenType.DOUBLE,			tokens.get( 5).getTokenType());
			assertEquals("0.5",						tokens.get( 5).getText());
		assertEquals(TokenType.FLOAT,			tokens.get( 6).getTokenType());
			assertEquals("5.0E7",					tokens.get( 6).getText());
		assertEquals(TokenType.DOUBLE,			tokens.get( 7).getTokenType());
			assertEquals("5.0E7",					tokens.get( 7).getText());
		assertEquals(TokenType.FLOAT,			tokens.get( 8).getTokenType());
			assertEquals("5.1E7",					tokens.get( 8).getText());
		assertEquals(TokenType.DOUBLE,			tokens.get( 9).getTokenType());
			assertEquals("5.1E7",					tokens.get( 9).getText());
		assertEquals(TokenType.FLOAT,			tokens.get(10).getTokenType());
			assertEquals("0.5",						tokens.get(10).getText());
		assertEquals(TokenType.DOUBLE,			tokens.get(11).getTokenType());
			assertEquals("0.5",						tokens.get(11).getText());
	}
	
	@Test
	public void checkForBug_tokensReadingTooFar()
	{
		String source = "5;51;25L;25.6;12.8f;\"hi\";'a';'\n';true;";
		Tokenizer iterator = new Tokenizer();
		List<Token> tokens = iterator.tokenize(new SourceFileInfo("", source));
		assertEquals(18, tokens.size());
		assertEquals(TokenType.INTEGER,			tokens.get( 0).getTokenType());
			assertEquals("5",					    tokens.get( 0).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get( 1).getTokenType());
		assertEquals(TokenType.INTEGER,			tokens.get( 2).getTokenType());
			assertEquals("51",					    tokens.get( 2).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get( 3).getTokenType());
		assertEquals(TokenType.LONG,			tokens.get( 4).getTokenType());
			assertEquals("25",					    tokens.get( 4).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get( 5).getTokenType());
		assertEquals(TokenType.DOUBLE,			tokens.get( 6).getTokenType());
			assertEquals("25.6",				    tokens.get( 6).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get( 7).getTokenType());
		assertEquals(TokenType.FLOAT,			tokens.get( 8).getTokenType());
			assertEquals("12.8",				    tokens.get( 8).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get( 9).getTokenType());
		assertEquals(TokenType.STRING,			tokens.get(10).getTokenType());
			assertEquals("hi",					    tokens.get(10).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get(11).getTokenType());
		assertEquals(TokenType.CHAR,			tokens.get(12).getTokenType());
			assertEquals("a",					    tokens.get(12).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get(13).getTokenType());
		assertEquals(TokenType.CHAR,			tokens.get(14).getTokenType());
			assertEquals("\n",					    tokens.get(14).getText());
			assertEquals(TokenType.SEMICOLON,		tokens.get(15).getTokenType());
		assertEquals(TokenType._TRUE,			tokens.get(16).getTokenType());
			assertEquals(TokenType.SEMICOLON,		tokens.get(17).getTokenType());
	}
	
	@Test
	public void errors_illegalCharacter()
	{
		Logger.clearLogs();
		String source = "#";
		Tokenizer iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		String expected =
				"Test1.java:1: error: illegal character: #\n\t#\n\t^\n";
		assertEquals(expected, Logger.getLogs());
	}
	
	@Test
	public void errors_unclosedCharacterLiteral()
	{
		String source, expected;
		Tokenizer iterator;

		Logger.clearLogs();
		source = "a'";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\ta'\n\t ^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "'a";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'a\n\t^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "'aa";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'aa\n\t  ^\n";
		assertEquals(expected, Logger.getLogs());
		
		Logger.clearLogs();
		source = "'\\";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'\\\n\t^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "'\\n";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'\\n\n\t ^\n";
		assertEquals(expected, Logger.getLogs());
		
		Logger.clearLogs();
		source = "'\\na";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed character literal\n\t'\\na\n\t   ^\n";
		assertEquals(expected, Logger.getLogs());
	}
	
	
	@Test
	public void errors_unclosedStringLiteral()
	{
		String source, expected;
		Tokenizer iterator;

		Logger.clearLogs();
		source = "\"abc\n\"";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed string literal\n\t\"abc\n\t^\n";
		assertEquals(expected, Logger.getLogs());

		Logger.clearLogs();
		source = "\"abc";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
		expected =
				"Test1.java:1: error: " +
				"unclosed string literal\n\t\"abc\n\t^\n";
		assertEquals(expected, Logger.getLogs());
		
		Logger.clearLogs();
		source = "\"";
		iterator = new Tokenizer();
		iterator.tokenize(new SourceFileInfo("Test1.java", source));
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
		Tokenizer tokenizer = new Tokenizer();
		List<Token> tokens = tokenizer.tokenize(new SourceFileInfo("", source));
		assertEquals(46, tokens.size());
		assertEquals(TokenType.PLUS_PLUS,		tokens.get( 0).getTokenType());
		assertEquals(TokenType.DASH_DASH,		tokens.get( 1).getTokenType());
		assertEquals(TokenType.PLUS,			tokens.get( 2).getTokenType());
		assertEquals(TokenType.DASH,			tokens.get( 3).getTokenType());
		assertEquals(TokenType.TILDE,			tokens.get( 4).getTokenType());
		assertEquals(TokenType.EXCLAIM,			tokens.get( 5).getTokenType());
		assertEquals(TokenType.STAR,			tokens.get( 6).getTokenType());
		assertEquals(TokenType.SLASH,			tokens.get( 7).getTokenType());
		assertEquals(TokenType.PERCENT,			tokens.get( 8).getTokenType());
		assertEquals(TokenType.LEFT_LEFT,		tokens.get( 9).getTokenType());
		assertEquals(TokenType.RIGHT_RIGHT,		tokens.get(10).getTokenType());
		assertEquals(TokenType.RIGHT_RIGHT_RIGHT,tokens.get(11).getTokenType());
		assertEquals(TokenType.LEFT,			tokens.get(12).getTokenType());
		assertEquals(TokenType.RIGHT,			tokens.get(13).getTokenType());
		assertEquals(TokenType.LEFT_EQUAL,		tokens.get(14).getTokenType());
		assertEquals(TokenType.RIGHT_EQUAL,		tokens.get(15).getTokenType());
		assertEquals(TokenType.EQUAL_EQUAL,		tokens.get(16).getTokenType());
		assertEquals(TokenType.EXCLAIM_EQUAL,	tokens.get(17).getTokenType());
		assertEquals(TokenType.AND,				tokens.get(18).getTokenType());
		assertEquals(TokenType.CARAT,			tokens.get(19).getTokenType());
		assertEquals(TokenType.PIPE,			tokens.get(20).getTokenType());
		assertEquals(TokenType.AND_AND,			tokens.get(21).getTokenType());
		assertEquals(TokenType.PIPE_PIPE,		tokens.get(22).getTokenType());
		assertEquals(TokenType.QUESTION,		tokens.get(23).getTokenType());
		assertEquals(TokenType.COLON,			tokens.get(24).getTokenType());
		assertEquals(TokenType.EQUAL,			tokens.get(25).getTokenType());
		assertEquals(TokenType.PLUS_EQUAL,		tokens.get(26).getTokenType());
		assertEquals(TokenType.DASH_EQUAL,		tokens.get(27).getTokenType());
		assertEquals(TokenType.STAR_EQUAL,		tokens.get(28).getTokenType());
		assertEquals(TokenType.SLASH_EQUAL,		tokens.get(29).getTokenType());
		assertEquals(TokenType.PERCENT_EQUAL,	tokens.get(30).getTokenType());
		assertEquals(TokenType.AND_EQUAL,		tokens.get(31).getTokenType());
		assertEquals(TokenType.CARAT_EQUAL,		tokens.get(32).getTokenType());
		assertEquals(TokenType.PIPE_EQUAL,		tokens.get(33).getTokenType());
		assertEquals(TokenType.LEFT_LEFT_EQUAL,	tokens.get(34).getTokenType());
		assertEquals(TokenType.RIGHT_RIGHT_EQUAL,tokens.get(35).getTokenType());
		assertEquals(
				TokenType.RIGHT_RIGHT_RIGHT_EQUAL,
				tokens.get(36).getTokenType());
		assertEquals(TokenType.DOT,				tokens.get(37).getTokenType());
		assertEquals(TokenType.SEMICOLON,		tokens.get(38).getTokenType());
		assertEquals(TokenType.COMMA,			tokens.get(39).getTokenType());
		assertEquals(TokenType.CURL_BRACE_LEFT,	tokens.get(40).getTokenType());
		assertEquals(TokenType.CURL_BRACE_RIGHT,tokens.get(41).getTokenType());
		assertEquals(TokenType.SQUARE_BRACE_LEFT,tokens.get(42).getTokenType());
		assertEquals(TokenType.SQUARE_BRACE_RIGHT,tokens.get(43).getTokenType());
		assertEquals(TokenType.PAREN_LEFT,      tokens.get(44).getTokenType());
		assertEquals(TokenType.PAREN_RIGHT,     tokens.get(45).getTokenType());
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
		Tokenizer tokenizer = new Tokenizer();
		List<Token> tokens = tokenizer.tokenize(new SourceFileInfo("", source));
		assertEquals(44, tokens.size());
		assertEquals(TokenType._ABSTRACT,		tokens.get( 0).getTokenType());
		assertEquals(TokenType._ASSERT,			tokens.get( 1).getTokenType());
		assertEquals(TokenType._BREAK,			tokens.get( 2).getTokenType());
		assertEquals(TokenType._CASE,			tokens.get( 3).getTokenType());
		assertEquals(TokenType._CATCH,			tokens.get( 4).getTokenType());
		assertEquals(TokenType._CLASS,			tokens.get( 5).getTokenType());
		assertEquals(TokenType._CONST,			tokens.get( 6).getTokenType());
		assertEquals(TokenType._CONTINUE,		tokens.get( 7).getTokenType());
		assertEquals(TokenType._DEFAULT, 		tokens.get( 8).getTokenType());
		assertEquals(TokenType._DO,				tokens.get( 9).getTokenType());
		assertEquals(TokenType._ELSE,			tokens.get(10).getTokenType());
		assertEquals(TokenType._ENUM,			tokens.get(11).getTokenType());
		assertEquals(TokenType._EXTENDS,		tokens.get(12).getTokenType());
		assertEquals(TokenType._FALSE,			tokens.get(13).getTokenType());
		assertEquals(TokenType._FINAL,			tokens.get(14).getTokenType());
		assertEquals(TokenType._FINALLY,		tokens.get(15).getTokenType());
		assertEquals(TokenType._FOR,			tokens.get(16).getTokenType());
		assertEquals(TokenType._GOTO,			tokens.get(17).getTokenType());
		assertEquals(TokenType._IF,				tokens.get(18).getTokenType());
		assertEquals(TokenType._IMPLEMENTS,		tokens.get(19).getTokenType());
		assertEquals(TokenType._IMPORT,			tokens.get(20).getTokenType());
		assertEquals(TokenType._INSTANCEOF,		tokens.get(21).getTokenType());
		assertEquals(TokenType._INTERFACE,		tokens.get(22).getTokenType());
		assertEquals(TokenType._NATIVE,			tokens.get(23).getTokenType());
		assertEquals(TokenType._NEW,			tokens.get(24).getTokenType());
		assertEquals(TokenType._NULL,			tokens.get(25).getTokenType());
		assertEquals(TokenType._PACKAGE,		tokens.get(26).getTokenType());
		assertEquals(TokenType._PRIVATE,		tokens.get(27).getTokenType());
		assertEquals(TokenType._PROTECTED,		tokens.get(28).getTokenType());
		assertEquals(TokenType._PUBLIC,			tokens.get(29).getTokenType());
		assertEquals(TokenType._RETURN,			tokens.get(30).getTokenType());
		assertEquals(TokenType._STATIC,			tokens.get(31).getTokenType());
		assertEquals(TokenType._STRICTFP,		tokens.get(32).getTokenType());
		assertEquals(TokenType._SUPER,			tokens.get(33).getTokenType());
		assertEquals(TokenType._SWITCH,			tokens.get(34).getTokenType());
		assertEquals(TokenType._SYNCHRONIZED,	tokens.get(35).getTokenType());
		assertEquals(TokenType._THIS,			tokens.get(36).getTokenType());
		assertEquals(TokenType._THROW,			tokens.get(37).getTokenType());
		assertEquals(TokenType._THROWS,			tokens.get(38).getTokenType());
		assertEquals(TokenType._TRANSIENT,		tokens.get(39).getTokenType());
		assertEquals(TokenType._TRUE,			tokens.get(40).getTokenType());
		assertEquals(TokenType._TRY,			tokens.get(41).getTokenType());
		assertEquals(TokenType._VOLATILE,		tokens.get(42).getTokenType());
		assertEquals(TokenType._WHILE,			tokens.get(43).getTokenType());
	}
}

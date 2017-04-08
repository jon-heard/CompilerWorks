
package com.jonheard.compilers.tokenizer_java;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Test;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.compilers.tokenizer_java.Tokenizer;
import com.jonheard.util.Logger;
import com.jonheard.util.SourceFile;

public class TokenizerTest {
  public TokenizerTest() {
    Logger.setPrintingToConsole(false);
  }

  @Test
  public void basic() {
    String source = "public class private";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(3, tokens.size());
    assertEquals(TokenType._PUBLIC, tokens.get(0).getType());
    assertEquals(TokenType._CLASS, tokens.get(1).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(2).getType());
  }

  @Test
  public void RowsAndCols() {
    String source = "public class private \nprotected public\r\n class";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(6, tokens.size());

    assertEquals(TokenType._PUBLIC, tokens.get(0).getType());
    assertEquals(0, tokens.get(0).getRow());
    assertEquals(0, tokens.get(0).getColumn());
    assertEquals(TokenType._CLASS, tokens.get(1).getType());
    assertEquals(0, tokens.get(1).getRow());
    assertEquals(7, tokens.get(1).getColumn());
    assertEquals(TokenType._PRIVATE, tokens.get(2).getType());
    assertEquals(0, tokens.get(2).getRow());
    assertEquals(13, tokens.get(2).getColumn());
    assertEquals(TokenType._PROTECTED, tokens.get(3).getType());
    assertEquals(1, tokens.get(3).getRow());
    assertEquals(0, tokens.get(3).getColumn());
    assertEquals(TokenType._PUBLIC, tokens.get(4).getType());
    assertEquals(1, tokens.get(4).getRow());
    assertEquals(10, tokens.get(4).getColumn());
    assertEquals(TokenType._CLASS, tokens.get(5).getType());
    assertEquals(2, tokens.get(5).getRow());
    assertEquals(1, tokens.get(5).getColumn());
  }

  @Test
  public void spacing() {
    String source = "public  classprivate\tprotected\rpublic\nclass " + "\n\r\t\r\n\t private";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(7, tokens.size());
    assertEquals(TokenType._PUBLIC, tokens.get(0).getType());
    assertEquals(TokenType._CLASS, tokens.get(1).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(2).getType());
    assertEquals(TokenType._PROTECTED, tokens.get(3).getType());
    assertEquals(TokenType._PUBLIC, tokens.get(4).getType());
    assertEquals(TokenType._CLASS, tokens.get(5).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(6).getType());
  }

  @Test
  public void comments() {
    String source = "public//1\nclass//2\r\nprivate/*3*/protected/*4\n5*/class/*4//5\r\n*/public";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(6, tokens.size());
    assertEquals(TokenType._PUBLIC, tokens.get(0).getType());
    assertEquals(TokenType._CLASS, tokens.get(1).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(2).getType());
    assertEquals(TokenType._PROTECTED, tokens.get(3).getType());
    assertEquals(TokenType._CLASS, tokens.get(4).getType());
    assertEquals(TokenType._PUBLIC, tokens.get(5).getType());
  }

  @Test
  public void identifiersAndOperators() {
    String source = "public=t+class/test;";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(8, tokens.size());
    assertEquals(TokenType._PUBLIC, tokens.get(0).getType());
    assertEquals(TokenType.EQUAL, tokens.get(1).getType());
    assertEquals(TokenType.IDENTIFIER, tokens.get(2).getType());
    assertEquals("t", tokens.get(2).getText());
    assertEquals(TokenType.PLUS, tokens.get(3).getType());
    assertEquals(TokenType._CLASS, tokens.get(4).getType());
    assertEquals(TokenType.SLASH, tokens.get(5).getType());
    assertEquals(TokenType.IDENTIFIER, tokens.get(6).getType());
    assertEquals("test", tokens.get(6).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(7).getType());
  }

  @Test
  public void charAndString() {
    String source = "'a' '~' '\\n' '\353' \"hello\" \"\" \"a\\nb\" \"a\\353b\"";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(8, tokens.size());
    assertEquals(TokenType.CHAR, tokens.get(0).getType());
    assertEquals("a", tokens.get(0).getText());
    assertEquals(TokenType.CHAR, tokens.get(1).getType());
    assertEquals("~", tokens.get(1).getText());
    assertEquals(TokenType.CHAR, tokens.get(2).getType());
    assertEquals("\\n", tokens.get(2).getText());
    assertEquals(TokenType.CHAR, tokens.get(3).getType());
    assertEquals("\353", tokens.get(3).getText());
    assertEquals(TokenType.STRING, tokens.get(4).getType());
    assertEquals("hello", tokens.get(4).getText());
    assertEquals(TokenType.STRING, tokens.get(5).getType());
    assertEquals("", tokens.get(5).getText());
    assertEquals(TokenType.STRING, tokens.get(6).getType());
    assertEquals("a\\nb", tokens.get(6).getText());
    assertEquals(TokenType.STRING, tokens.get(7).getType());
    assertEquals("a\\353b", tokens.get(7).getText());
  }

  @Test
  public void integer() {
    String source = "56 56l 056 056L 0x56 0X56l 0 0L 0b1100 0B1010l";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(10, tokens.size());
    assertEquals(TokenType.INTEGER, tokens.get(0).getType());
    assertEquals("56", tokens.get(0).getText());
    assertEquals(TokenType.LONG, tokens.get(1).getType());
    assertEquals("56", tokens.get(1).getText());
    assertEquals(TokenType.INTEGER, tokens.get(2).getType());
    assertEquals("46", tokens.get(2).getText());
    assertEquals(TokenType.LONG, tokens.get(3).getType());
    assertEquals("46", tokens.get(3).getText());
    assertEquals(TokenType.INTEGER, tokens.get(4).getType());
    assertEquals("86", tokens.get(4).getText());
    assertEquals(TokenType.LONG, tokens.get(5).getType());
    assertEquals("86", tokens.get(5).getText());
    assertEquals(TokenType.INTEGER, tokens.get(6).getType());
    assertEquals("0", tokens.get(6).getText());
    assertEquals(TokenType.LONG, tokens.get(7).getType());
    assertEquals("0", tokens.get(7).getText());
    assertEquals(TokenType.INTEGER, tokens.get(8).getType());
    assertEquals("12", tokens.get(8).getText());
    assertEquals(TokenType.LONG, tokens.get(9).getType());
    assertEquals("10", tokens.get(9).getText());
  }

  @Test
  public void floatingPoint() {
    String source = "56f 56d 056F 056D " + "0.5f 0.5 5e7F 5e7d 5.1e7f 5.1e7D " + ".5f .5 " +
        "0x1p3 0x2.p0d 0x.5p1f 0x1.5p2F" + "1e+2 1.5e-1f 0x1p+3 0x2.2p-1f";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(20, tokens.size());
    assertEquals(TokenType.FLOAT, tokens.get(0).getType());
    assertEquals("56.0", tokens.get(0).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(1).getType());
    assertEquals("56.0", tokens.get(1).getText());
    assertEquals(TokenType.FLOAT, tokens.get(2).getType());
    assertEquals("56.0", tokens.get(2).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(3).getType());
    assertEquals("56.0", tokens.get(3).getText());
    assertEquals(TokenType.FLOAT, tokens.get(4).getType());
    assertEquals("0.5", tokens.get(4).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(5).getType());
    assertEquals("0.5", tokens.get(5).getText());
    assertEquals(TokenType.FLOAT, tokens.get(6).getType());
    assertEquals("5.0E7", tokens.get(6).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(7).getType());
    assertEquals("5.0E7", tokens.get(7).getText());
    assertEquals(TokenType.FLOAT, tokens.get(8).getType());
    assertEquals("5.1E7", tokens.get(8).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(9).getType());
    assertEquals("5.1E7", tokens.get(9).getText());
    assertEquals(TokenType.FLOAT, tokens.get(10).getType());
    assertEquals("0.5", tokens.get(10).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(11).getType());
    assertEquals("0.5", tokens.get(11).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(12).getType());
    assertEquals("8.0", tokens.get(12).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(13).getType());
    assertEquals("2.0", tokens.get(13).getText());
    assertEquals(TokenType.FLOAT, tokens.get(14).getType());
    assertEquals("0.625", tokens.get(14).getText());
    assertEquals(TokenType.FLOAT, tokens.get(15).getType());
    assertEquals("5.25", tokens.get(15).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(16).getType());
    assertEquals("100.0", tokens.get(16).getText());
    assertEquals(TokenType.FLOAT, tokens.get(17).getType());
    assertEquals("0.15", tokens.get(17).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(18).getType());
    assertEquals("8.0", tokens.get(18).getText());
    assertEquals(TokenType.FLOAT, tokens.get(19).getType());
    assertEquals("1.0625", tokens.get(19).getText());
  }

  @Test
  public void allSymbols() {
    String source = "++ -- + - ~ ! * / % << >> >>> < > <= >= == != & ^ "
        + "| && || ? : = += -= *= /= %= &= ^= |= <<= >>= >>>= . ; , " + "{ } [ ] ( )";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(46, tokens.size());
    assertEquals(TokenType.PLUS_PLUS, tokens.get(0).getType());
    assertEquals(TokenType.DASH_DASH, tokens.get(1).getType());
    assertEquals(TokenType.PLUS, tokens.get(2).getType());
    assertEquals(TokenType.DASH, tokens.get(3).getType());
    assertEquals(TokenType.TILDE, tokens.get(4).getType());
    assertEquals(TokenType.EXCLAIM, tokens.get(5).getType());
    assertEquals(TokenType.STAR, tokens.get(6).getType());
    assertEquals(TokenType.SLASH, tokens.get(7).getType());
    assertEquals(TokenType.PERCENT, tokens.get(8).getType());
    assertEquals(TokenType.LEFT_LEFT, tokens.get(9).getType());
    assertEquals(TokenType.RIGHT_RIGHT, tokens.get(10).getType());
    assertEquals(TokenType.RIGHT_RIGHT_RIGHT, tokens.get(11).getType());
    assertEquals(TokenType.LEFT_TRI, tokens.get(12).getType());
    assertEquals(TokenType.RIGHT_TRI, tokens.get(13).getType());
    assertEquals(TokenType.LEFT_TRI_EQUAL, tokens.get(14).getType());
    assertEquals(TokenType.RIGHT_TRI_EQUAL, tokens.get(15).getType());
    assertEquals(TokenType.EQUAL_EQUAL, tokens.get(16).getType());
    assertEquals(TokenType.EXCLAIM_EQUAL, tokens.get(17).getType());
    assertEquals(TokenType.AND, tokens.get(18).getType());
    assertEquals(TokenType.CARAT, tokens.get(19).getType());
    assertEquals(TokenType.PIPE, tokens.get(20).getType());
    assertEquals(TokenType.AND_AND, tokens.get(21).getType());
    assertEquals(TokenType.PIPE_PIPE, tokens.get(22).getType());
    assertEquals(TokenType.QUESTION, tokens.get(23).getType());
    assertEquals(TokenType.COLON, tokens.get(24).getType());
    assertEquals(TokenType.EQUAL, tokens.get(25).getType());
    assertEquals(TokenType.PLUS_EQUAL, tokens.get(26).getType());
    assertEquals(TokenType.DASH_EQUAL, tokens.get(27).getType());
    assertEquals(TokenType.STAR_EQUAL, tokens.get(28).getType());
    assertEquals(TokenType.SLASH_EQUAL, tokens.get(29).getType());
    assertEquals(TokenType.PERCENT_EQUAL, tokens.get(30).getType());
    assertEquals(TokenType.AND_EQUAL, tokens.get(31).getType());
    assertEquals(TokenType.CARAT_EQUAL, tokens.get(32).getType());
    assertEquals(TokenType.PIPE_EQUAL, tokens.get(33).getType());
    assertEquals(TokenType.LEFT_LEFT_EQUAL, tokens.get(34).getType());
    assertEquals(TokenType.RIGHT_RIGHT_EQUAL, tokens.get(35).getType());
    assertEquals(TokenType.RIGHT_RIGHT_RIGHT_EQUAL, tokens.get(36).getType());
    assertEquals(TokenType.DOT, tokens.get(37).getType());
    assertEquals(TokenType.SEMICOLON, tokens.get(38).getType());
    assertEquals(TokenType.COMMA, tokens.get(39).getType());
    assertEquals(TokenType.LEFT_CURL, tokens.get(40).getType());
    assertEquals(TokenType.RIGHT_CURL, tokens.get(41).getType());
    assertEquals(TokenType.LEFT_SQUARE, tokens.get(42).getType());
    assertEquals(TokenType.RIGHT_SQUARE, tokens.get(43).getType());
    assertEquals(TokenType.LEFT_PAREN, tokens.get(44).getType());
    assertEquals(TokenType.RIGHT_PAREN, tokens.get(45).getType());
  }

  @Test
  public void allKeywords() {
    String source = "abstract assert break case catch "
        + "class const continue default do else enum extends "
        + "false final finally for goto if implements import "
        + "instanceof interface native new null package private "
        + "protected public return static strictfp super switch "
        + "synchronized this throw throws transient true try " + "volatile while";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(44, tokens.size());
    assertEquals(TokenType._ABSTRACT, tokens.get(0).getType());
    assertEquals(TokenType._ASSERT, tokens.get(1).getType());
    assertEquals(TokenType._BREAK, tokens.get(2).getType());
    assertEquals(TokenType._CASE, tokens.get(3).getType());
    assertEquals(TokenType._CATCH, tokens.get(4).getType());
    assertEquals(TokenType._CLASS, tokens.get(5).getType());
    assertEquals(TokenType._CONST, tokens.get(6).getType());
    assertEquals(TokenType._CONTINUE, tokens.get(7).getType());
    assertEquals(TokenType._DEFAULT, tokens.get(8).getType());
    assertEquals(TokenType._DO, tokens.get(9).getType());
    assertEquals(TokenType._ELSE, tokens.get(10).getType());
    assertEquals(TokenType._ENUM, tokens.get(11).getType());
    assertEquals(TokenType._EXTENDS, tokens.get(12).getType());
    assertEquals(TokenType._FALSE, tokens.get(13).getType());
    assertEquals(TokenType._FINAL, tokens.get(14).getType());
    assertEquals(TokenType._FINALLY, tokens.get(15).getType());
    assertEquals(TokenType._FOR, tokens.get(16).getType());
    assertEquals(TokenType._GOTO, tokens.get(17).getType());
    assertEquals(TokenType._IF, tokens.get(18).getType());
    assertEquals(TokenType._IMPLEMENTS, tokens.get(19).getType());
    assertEquals(TokenType._IMPORT, tokens.get(20).getType());
    assertEquals(TokenType._INSTANCEOF, tokens.get(21).getType());
    assertEquals(TokenType._INTERFACE, tokens.get(22).getType());
    assertEquals(TokenType._NATIVE, tokens.get(23).getType());
    assertEquals(TokenType._NEW, tokens.get(24).getType());
    assertEquals(TokenType._NULL, tokens.get(25).getType());
    assertEquals(TokenType._PACKAGE, tokens.get(26).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(27).getType());
    assertEquals(TokenType._PROTECTED, tokens.get(28).getType());
    assertEquals(TokenType._PUBLIC, tokens.get(29).getType());
    assertEquals(TokenType._RETURN, tokens.get(30).getType());
    assertEquals(TokenType._STATIC, tokens.get(31).getType());
    assertEquals(TokenType._STRICTFP, tokens.get(32).getType());
    assertEquals(TokenType._SUPER, tokens.get(33).getType());
    assertEquals(TokenType._SWITCH, tokens.get(34).getType());
    assertEquals(TokenType._SYNCHRONIZED, tokens.get(35).getType());
    assertEquals(TokenType._THIS, tokens.get(36).getType());
    assertEquals(TokenType._THROW, tokens.get(37).getType());
    assertEquals(TokenType._THROWS, tokens.get(38).getType());
    assertEquals(TokenType._TRANSIENT, tokens.get(39).getType());
    assertEquals(TokenType._TRUE, tokens.get(40).getType());
    assertEquals(TokenType._TRY, tokens.get(41).getType());
    assertEquals(TokenType._VOLATILE, tokens.get(42).getType());
    assertEquals(TokenType._WHILE, tokens.get(43).getType());
  }

  @Test
  public void error_tokensReadingTooFarBug() {
    String source = "5;51;25L;25.6;12.8f;\"hi\";'a';'\n';true;";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(18, tokens.size());
    assertEquals(TokenType.INTEGER, tokens.get(0).getType());
    assertEquals("5", tokens.get(0).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(1).getType());
    assertEquals(TokenType.INTEGER, tokens.get(2).getType());
    assertEquals("51", tokens.get(2).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(3).getType());
    assertEquals(TokenType.LONG, tokens.get(4).getType());
    assertEquals("25", tokens.get(4).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(5).getType());
    assertEquals(TokenType.DOUBLE, tokens.get(6).getType());
    assertEquals("25.6", tokens.get(6).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(7).getType());
    assertEquals(TokenType.FLOAT, tokens.get(8).getType());
    assertEquals("12.8", tokens.get(8).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(9).getType());
    assertEquals(TokenType.STRING, tokens.get(10).getType());
    assertEquals("hi", tokens.get(10).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(11).getType());
    assertEquals(TokenType.CHAR, tokens.get(12).getType());
    assertEquals("a", tokens.get(12).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(13).getType());
    assertEquals(TokenType.CHAR, tokens.get(14).getType());
    assertEquals("\n", tokens.get(14).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(15).getType());
    assertEquals(TokenType._TRUE, tokens.get(16).getType());
    assertEquals(TokenType.SEMICOLON, tokens.get(17).getType());
  }

  @Test
  public void error_FloatingPointUsingWrongExpSymbol() {
    Logger.clearLogs();
    Logger.resetCounts();

    Tokenizer tokenizer = new Tokenizer();
    tokenizer.tokenize(new SourceFile("", "1.5p3"));
    assertEquals(1, Logger.getErrorCount());
    tokenizer.tokenize(new SourceFile("", "0x1.5e3"));
    assertEquals(2, Logger.getErrorCount());

    Logger.clearLogs();
    Logger.resetCounts();
  }

  @Test
  public void errors_illegalCharacter() {
    Logger.clearLogs();
    Logger.resetCounts();

    Tokenizer tokenizer = new Tokenizer();
    tokenizer.tokenize(new SourceFile("Test1.java", "#"));
    assertEquals(1, Logger.getErrorCount());

    Logger.clearLogs();
    Logger.resetCounts();
  }

  @Test
  public void errors_unclosedCharacterLiteral() {
    Logger.clearLogs();
    Logger.resetCounts();

    Tokenizer tokenizer = new Tokenizer();
    tokenizer.tokenize(new SourceFile("Test1.java", "a'"));
    assertEquals(1, Logger.getErrorCount());
    tokenizer.tokenize(new SourceFile("Test1.java", "'a"));
    assertEquals(2, Logger.getErrorCount());
    tokenizer.tokenize(new SourceFile("Test1.java", "'aa"));
    assertEquals(3, Logger.getErrorCount());
    tokenizer.tokenize(new SourceFile("Test1.java", "'\\"));
    assertEquals(5, Logger.getErrorCount());
    tokenizer.tokenize(new SourceFile("Test1.java", "'\\n"));
    assertEquals(6, Logger.getErrorCount());
    tokenizer.tokenize(new SourceFile("Test1.java", "'\\na"));
    assertEquals(7, Logger.getErrorCount());
    
    Logger.clearLogs();
    Logger.resetCounts();
  }

  @Test
  public void errors_unclosedStringLiteral() {
    String source, expected;
    Tokenizer tokenizer;

    Logger.clearLogs();
    source = "\"abc\n\"";
    tokenizer = new Tokenizer();
    tokenizer.tokenize(new SourceFile("Test1.java", source));
    expected = "Test1.java:1: error: unclosed string literal\n" + "\t\"abc\n" + "\t^\n"
        + "Test1.java:2: error: unclosed string literal\n" + "\t\"\n" + "\t^\n";
    assertEquals(expected, Logger.getLogs());

    Logger.clearLogs();
    source = "\"abc";
    tokenizer = new Tokenizer();
    tokenizer.tokenize(new SourceFile("Test1.java", source));
    expected = "Test1.java:1: error: " + "unclosed string literal\n\t\"abc\n\t^\n";
    assertEquals(expected, Logger.getLogs());

    Logger.clearLogs();
    source = "\"";
    tokenizer = new Tokenizer();
    tokenizer.tokenize(new SourceFile("Test1.java", source));
    expected = "Test1.java:1: error: " + "unclosed string literal\n\t\"\n\t^\n";
    assertEquals(expected, Logger.getLogs());
  }
}

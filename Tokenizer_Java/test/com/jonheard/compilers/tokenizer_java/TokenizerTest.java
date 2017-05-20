
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
    int index = -1;
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(++index).getType());
  }

  @Test
  public void RowsAndCols() {
    String source = "public class private \nprotected public\r\n class";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(6, tokens.size());
    int index = -1;
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(0, tokens.get(index).getRow());
    assertEquals(0, tokens.get(index).getColumn());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(0, tokens.get(index).getRow());
    assertEquals(7, tokens.get(index).getColumn());
    assertEquals(TokenType._PRIVATE, tokens.get(++index).getType());
    assertEquals(0, tokens.get(index).getRow());
    assertEquals(13, tokens.get(index).getColumn());
    assertEquals(TokenType._PROTECTED, tokens.get(++index).getType());
    assertEquals(1, tokens.get(index).getRow());
    assertEquals(0, tokens.get(index).getColumn());
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(1, tokens.get(index).getRow());
    assertEquals(10, tokens.get(index).getColumn());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(2, tokens.get(index).getRow());
    assertEquals(1, tokens.get(index).getColumn());
  }

  @Test
  public void spacing() {
    String source = "public  classprivate\tprotected\rpublic\nclass " + "\n\r\t\r\n\t private";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(7, tokens.size());
    int index = -1;
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(++index).getType());
    assertEquals(TokenType._PROTECTED, tokens.get(++index).getType());
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(++index).getType());
  }

  @Test
  public void comments() {
    String source = "public//1\nclass//2\r\nprivate/*3*/protected/*4\n5*/class/*4//5\r\n*/public";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(6, tokens.size());
    int index = -1;
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(++index).getType());
    assertEquals(TokenType._PROTECTED, tokens.get(++index).getType());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
  }

  @Test
  public void identifiersAndOperators() {
    String source = "public=t+class/test;";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(8, tokens.size());
    int index = -1;
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(TokenType.EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.IDENTIFIER, tokens.get(++index).getType());
    assertEquals("t", tokens.get(index).getText());
    assertEquals(TokenType.PLUS, tokens.get(++index).getType());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(TokenType.SLASH, tokens.get(++index).getType());
    assertEquals(TokenType.IDENTIFIER, tokens.get(++index).getType());
    assertEquals("test", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
  }

  @Test
  public void charAndString() {
    String source =
        "'a' '~' '\\n' '\353' \"hello\" \"\" \"a\\nb\" \"a\\353b\"";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(8, tokens.size());
    int index = -1;
    assertEquals(TokenType.CHAR, tokens.get(++index).getType());
    assertEquals("a", tokens.get(index).getText());
    assertEquals(TokenType.CHAR, tokens.get(++index).getType());
    assertEquals("~", tokens.get(index).getText());
    assertEquals(TokenType.CHAR, tokens.get(++index).getType());
    assertEquals("\\n", tokens.get(index).getText());
    assertEquals(TokenType.CHAR, tokens.get(++index).getType());
    assertEquals("\353", tokens.get(index).getText());
    assertEquals(TokenType.STRING, tokens.get(++index).getType());
    assertEquals("hello", tokens.get(index).getText());
    assertEquals(TokenType.STRING, tokens.get(++index).getType());
    assertEquals("", tokens.get(index).getText());
    assertEquals(TokenType.STRING, tokens.get(++index).getType());
    assertEquals("a\\nb", tokens.get(index).getText());
    assertEquals(TokenType.STRING, tokens.get(++index).getType());
    assertEquals("a\\353b", tokens.get(index).getText());
  }

  @Test
  public void integers() {
    String source =
        "56 56l " + "0 0L " + "056 056L " + "0x56 0X56l " +
        "0b1100 0B1010l";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(10, tokens.size());
    int index = -1;
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("56", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("56", tokens.get(index).getText());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("0", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("0", tokens.get(index).getText());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("46", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("46", tokens.get(index).getText());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("86", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("86", tokens.get(index).getText());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("12", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("10", tokens.get(index).getText());
  }

  @Test
  public void integersWithUnderscores() {
    String source =
        "5_63 56_3L " + "0_5__6 05___6l " +
        "0x6___5__6 0x6__5____6L" + "0b1__10___0 0B10_10L";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(8, tokens.size());
    int index = -1;
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("563", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("563", tokens.get(index).getText());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("46", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("46", tokens.get(index).getText());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("1622", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("1622", tokens.get(index).getText());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("12", tokens.get(index).getText());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("10", tokens.get(index).getText());
  }

  @Test
  public void floatingPoints() {
    String source =
        "56f 56d 056F 056D " + "0.5f 0.5 5e7F 5e7d 5.1e7f 5.1e7D " +
        ".5f .5 " + "0x1p3 0x2.p0d 0x.5p1f 0x1.5p2F" +
        "1e+2 1.5e-1f 0x1p+3 0x2.2p-1f";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(20, tokens.size());
    int index = -1;
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.5", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("0.5", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("5.0E7", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("5.0E7", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("5.1E7", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("5.1E7", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.5", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("0.5", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("8.0", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("2.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.625", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("5.25", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("100.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.15", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("8.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("1.0625", tokens.get(index).getText());
  }

  @Test
  public void floatingPointsWithUnderscores() {
    String source =
        "5_6f 5___6d 0_5___6F 0___5_6D " +
        "0.__5f 0__.5 1e1__1F 1__1e2d 5_.__1e7f 5__._1e7D " +
        ".5_5f .5__5 " + "0x1__1p1 0x2__.p1__0d 0x.__5p1f 0x1__._5p2F" +
        "1e+1__1 1.5e-1__1f 0x1p+1__1 0x2_._2p-1__1f";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(20, tokens.size());
    int index = -1;
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("56.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.5", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("0.5", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("9.9999998E10", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("1100.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("5.1E7", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("5.1E7", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.55", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("0.55", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("34.0", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("2048.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.625", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("5.25", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("1.0E11", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("1.5E-11", tokens.get(index).getText());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("2048.0", tokens.get(index).getText());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("0.0010375977", tokens.get(index).getText());
  }

  @Test
  public void allSymbols() {
    String source = "++ -- + - ~ ! * / % << >> >>> < > <= >= == != & ^ "
        + "| && || ? : = += -= *= /= %= &= ^= |= <<= >>= >>>= . ; , " + "{ } [ ] ( )";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(46, tokens.size());
    int index = -1;
    assertEquals(TokenType.PLUS_PLUS, tokens.get(++index).getType());
    assertEquals(TokenType.DASH_DASH, tokens.get(++index).getType());
    assertEquals(TokenType.PLUS, tokens.get(++index).getType());
    assertEquals(TokenType.DASH, tokens.get(++index).getType());
    assertEquals(TokenType.TILDE, tokens.get(++index).getType());
    assertEquals(TokenType.EXCLAIM, tokens.get(++index).getType());
    assertEquals(TokenType.STAR, tokens.get(++index).getType());
    assertEquals(TokenType.SLASH, tokens.get(++index).getType());
    assertEquals(TokenType.PERCENT, tokens.get(++index).getType());
    assertEquals(TokenType.LEFT_LEFT, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_RIGHT, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_RIGHT_RIGHT, tokens.get(++index).getType());
    assertEquals(TokenType.LEFT_TRI, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_TRI, tokens.get(++index).getType());
    assertEquals(TokenType.LEFT_TRI_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_TRI_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.EQUAL_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.EXCLAIM_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.AND, tokens.get(++index).getType());
    assertEquals(TokenType.CARAT, tokens.get(++index).getType());
    assertEquals(TokenType.PIPE, tokens.get(++index).getType());
    assertEquals(TokenType.AND_AND, tokens.get(++index).getType());
    assertEquals(TokenType.PIPE_PIPE, tokens.get(++index).getType());
    assertEquals(TokenType.QUESTION, tokens.get(++index).getType());
    assertEquals(TokenType.COLON, tokens.get(++index).getType());
    assertEquals(TokenType.EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.PLUS_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.DASH_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.STAR_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.SLASH_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.PERCENT_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.AND_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.CARAT_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.PIPE_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.LEFT_LEFT_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_RIGHT_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_RIGHT_RIGHT_EQUAL, tokens.get(++index).getType());
    assertEquals(TokenType.DOT, tokens.get(++index).getType());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.COMMA, tokens.get(++index).getType());
    assertEquals(TokenType.LEFT_CURL, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_CURL, tokens.get(++index).getType());
    assertEquals(TokenType.LEFT_SQUARE, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_SQUARE, tokens.get(++index).getType());
    assertEquals(TokenType.LEFT_PAREN, tokens.get(++index).getType());
    assertEquals(TokenType.RIGHT_PAREN, tokens.get(++index).getType());
  }

  @Test
  public void allKeywords() {
    String source = "abstract assert break case catch "
        + "class const continue default do else enum extends "
        + "false final finally for goto if implements import "
        + "instanceof interface native new null package private "
        + "protected public return static strictfp super switch "
        + "synchronized this throw throws transient true try volatile while";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(44, tokens.size());
    int index = -1;
    assertEquals(TokenType._ABSTRACT, tokens.get(++index).getType());
    assertEquals(TokenType._ASSERT, tokens.get(++index).getType());
    assertEquals(TokenType._BREAK, tokens.get(++index).getType());
    assertEquals(TokenType._CASE, tokens.get(++index).getType());
    assertEquals(TokenType._CATCH, tokens.get(++index).getType());
    assertEquals(TokenType._CLASS, tokens.get(++index).getType());
    assertEquals(TokenType._CONST, tokens.get(++index).getType());
    assertEquals(TokenType._CONTINUE, tokens.get(++index).getType());
    assertEquals(TokenType._DEFAULT, tokens.get(++index).getType());
    assertEquals(TokenType._DO, tokens.get(++index).getType());
    assertEquals(TokenType._ELSE, tokens.get(++index).getType());
    assertEquals(TokenType._ENUM, tokens.get(++index).getType());
    assertEquals(TokenType._EXTENDS, tokens.get(++index).getType());
    assertEquals(TokenType._FALSE, tokens.get(++index).getType());
    assertEquals(TokenType._FINAL, tokens.get(++index).getType());
    assertEquals(TokenType._FINALLY, tokens.get(++index).getType());
    assertEquals(TokenType._FOR, tokens.get(++index).getType());
    assertEquals(TokenType._GOTO, tokens.get(++index).getType());
    assertEquals(TokenType._IF, tokens.get(++index).getType());
    assertEquals(TokenType._IMPLEMENTS, tokens.get(++index).getType());
    assertEquals(TokenType._IMPORT, tokens.get(++index).getType());
    assertEquals(TokenType._INSTANCEOF, tokens.get(++index).getType());
    assertEquals(TokenType._INTERFACE, tokens.get(++index).getType());
    assertEquals(TokenType._NATIVE, tokens.get(++index).getType());
    assertEquals(TokenType._NEW, tokens.get(++index).getType());
    assertEquals(TokenType._NULL, tokens.get(++index).getType());
    assertEquals(TokenType._PACKAGE, tokens.get(++index).getType());
    assertEquals(TokenType._PRIVATE, tokens.get(++index).getType());
    assertEquals(TokenType._PROTECTED, tokens.get(++index).getType());
    assertEquals(TokenType._PUBLIC, tokens.get(++index).getType());
    assertEquals(TokenType._RETURN, tokens.get(++index).getType());
    assertEquals(TokenType._STATIC, tokens.get(++index).getType());
    assertEquals(TokenType._STRICTFP, tokens.get(++index).getType());
    assertEquals(TokenType._SUPER, tokens.get(++index).getType());
    assertEquals(TokenType._SWITCH, tokens.get(++index).getType());
    assertEquals(TokenType._SYNCHRONIZED, tokens.get(++index).getType());
    assertEquals(TokenType._THIS, tokens.get(++index).getType());
    assertEquals(TokenType._THROW, tokens.get(++index).getType());
    assertEquals(TokenType._THROWS, tokens.get(++index).getType());
    assertEquals(TokenType._TRANSIENT, tokens.get(++index).getType());
    assertEquals(TokenType._TRUE, tokens.get(++index).getType());
    assertEquals(TokenType._TRY, tokens.get(++index).getType());
    assertEquals(TokenType._VOLATILE, tokens.get(++index).getType());
    assertEquals(TokenType._WHILE, tokens.get(++index).getType());
  }

  @Test
  public void error_tokensReadingTooFarBug() {
    String source = "5;51;25L;25.6;12.8f;\"hi\";'a';'\n';true;";
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokens = tokenizer.tokenize(new SourceFile("", source));
    assertEquals(18, tokens.size());
    int index = -1;
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("5", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.INTEGER, tokens.get(++index).getType());
    assertEquals("51", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.LONG, tokens.get(++index).getType());
    assertEquals("25", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.DOUBLE, tokens.get(++index).getType());
    assertEquals("25.6", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.FLOAT, tokens.get(++index).getType());
    assertEquals("12.8", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.STRING, tokens.get(++index).getType());
    assertEquals("hi", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.CHAR, tokens.get(++index).getType());
    assertEquals("a", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType.CHAR, tokens.get(++index).getType());
    assertEquals("\n", tokens.get(index).getText());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
    assertEquals(TokenType._TRUE, tokens.get(++index).getType());
    assertEquals(TokenType.SEMICOLON, tokens.get(++index).getType());
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
  
  @Test
  public void errors_underscoreAtStartOrEndOfNumber() {
    
  }
  
  @Test
  public void errors_binaryOrHexWithoutAnyDigits() {
    
  }
}

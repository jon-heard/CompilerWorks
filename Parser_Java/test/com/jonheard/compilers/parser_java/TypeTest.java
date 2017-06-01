
package com.jonheard.compilers.parser_java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.Id;
import com.jonheard.compilers.parser_java.ir.Import;
import com.jonheard.compilers.parser_java.ir.List_QualifiedId;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.parser_java.ir.Type;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.SourceFile;

public class TypeTest {
  @Test
  public void basic() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i1"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i2"));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(Type.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Type.getIsNext(parser));
    Type toTest = new Type(parser);

    assertEquals("i1.i2", toTest.getId().getValue());
    List_QualifiedId generics = toTest.getGenerics();
    assertEquals(0, generics.getChildCount());
    assertEquals(0, toTest.getDimensionCount());
    assertEquals("i1.i2", toTest.getValue());
    assertEquals("Li1/i2;", toTest.toJvmDescriptor());

    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }

  @Test
  public void array() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "int"));
    tokenList.add(new Token(TokenType.LEFT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.RIGHT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.LEFT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.RIGHT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(Type.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Type.getIsNext(parser));
    Type toTest = new Type(parser);

    assertEquals("int", toTest.getId().getValue());
    List_QualifiedId generics = toTest.getGenerics();
    assertEquals(0, generics.getChildCount());
    assertEquals(2, toTest.getDimensionCount());
    assertEquals("int[][]", toTest.getValue());
    assertEquals("[[I", toTest.toJvmDescriptor());

    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }

  @Test
  public void generic() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "boolean"));
    tokenList.add(new Token(TokenType.LEFT_TRI, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g1"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g2"));
    tokenList.add(new Token(TokenType.COMMA, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g3"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g4"));
    tokenList.add(new Token(TokenType.RIGHT_TRI, 0, 0));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(Type.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Type.getIsNext(parser));
    Type toTest = new Type(parser);

    assertEquals("boolean", toTest.getId().getValue());
    List_QualifiedId generics = toTest.getGenerics();
    assertEquals(2, generics.getChildCount());
    assertEquals(0, toTest.getDimensionCount());
    assertEquals("g1.g2", generics.getChildAsQualifiedId(0).getValue());
    assertEquals("g3.g4", generics.getChildAsQualifiedId(1).getValue());
    assertEquals("boolean<g1.g2, g3.g4>", toTest.getValue());
    assertEquals("Z", toTest.toJvmDescriptor());

    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }

  @Test
  public void genericArray() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "float"));
    tokenList.add(new Token(TokenType.LEFT_TRI, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g1"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g2"));
    tokenList.add(new Token(TokenType.COMMA, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g3"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "g4"));
    tokenList.add(new Token(TokenType.RIGHT_TRI, 0, 0));
    tokenList.add(new Token(TokenType.LEFT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.RIGHT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.LEFT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.RIGHT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.LEFT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType.RIGHT_SQUARE, 0, 0));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(Type.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Type.getIsNext(parser));
    Type toTest = new Type(parser);

    assertEquals("float", toTest.getId().getValue());
    List_QualifiedId generics = toTest.getGenerics();
    assertEquals(2, generics.getChildCount());
    assertEquals(3, toTest.getDimensionCount());
    assertEquals("g1.g2", generics.getChildAsQualifiedId(0).getValue());
    assertEquals("g3.g4", generics.getChildAsQualifiedId(1).getValue());
    assertEquals("float<g1.g2, g3.g4>[][][]", toTest.getValue());
    assertEquals("[[[F", toTest.toJvmDescriptor());

    toTest.incDimensionCount();
    assertEquals(4, toTest.getDimensionCount());
    assertEquals("float<g1.g2, g3.g4>[][][][]", toTest.getValue());
    assertEquals("[[[[F", toTest.toJvmDescriptor());

    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }
}

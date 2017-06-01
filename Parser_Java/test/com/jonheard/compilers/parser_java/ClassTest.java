
package com.jonheard.compilers.parser_java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.compilers.parser_java.ir.Id;
import com.jonheard.compilers.parser_java.ir.List_Modifier;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.SourceFile;

public class ClassTest {
  @Test
  public void basic() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType._CLASS, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "c1"));
    tokenList.add(new Token(TokenType.LEFT_CURL, 0, 0));
    tokenList.add(new Token(TokenType.RIGHT_CURL, 0, 0));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);
    assertFalse(Class.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Class.getIsNext(parser));
    Class toTest = new Class(parser);
    assertEquals(3, toTest.getChildCount());
    assertTrue(toTest.getChild(0) instanceof List_Modifier);
    assertEquals("", toTest.getModifiers().getValue());
    assertTrue(toTest.getChild(1) instanceof Id);
    assertEquals("c1", toTest.getName().getValue());
    assertTrue(toTest.getChild(2) instanceof QualifiedId);
    assertEquals("Object", toTest.getSuper().getValue());
  }
}

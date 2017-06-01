
package com.jonheard.compilers.parser_java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.compilers.parser_java.ir.List_Modifier;
import com.jonheard.compilers.parser_java.ir.Member;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.SourceFile;

public class ClassTest {
  @Test
  public void basic_withDefaults() {
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

    assertEquals("", toTest.getModifiers().getValue());
    assertEquals("c1", toTest.getName().getValue());
    assertEquals("Object", toTest.getSuper().getValue());
    List<Member> members = toTest.getMembers();
    assertEquals(0, members.size());
  }

  @Test
  public void basic_noDefaults() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType._PUBLIC, 0, 0));
    tokenList.add(new Token(TokenType._STATIC, 0, 0));
    tokenList.add(new Token(TokenType._CLASS, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "c1"));
    tokenList.add(new Token(TokenType._EXTENDS, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "s1"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "s2"));
    tokenList.add(new Token(TokenType._IMPLEMENTS, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i1"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i2"));
    tokenList.add(new Token(TokenType.COMMA, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i3"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i4"));
    tokenList.add(new Token(TokenType.LEFT_CURL, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "int"));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i"));
    tokenList.add(new Token(TokenType.SEMICOLON, 0, 0));
    tokenList.add(new Token(TokenType.RIGHT_CURL, 0, 0));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(Class.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Class.getIsNext(parser));
    Class toTest = new Class(parser);

    List_Modifier modifiers = toTest.getModifiers();
    assertEquals(2, modifiers.getModifierCount());
    assertTrue(modifiers.getIsPublic());
    assertTrue(modifiers.getIsStatic());
    assertEquals("c1", toTest.getName().getValue());
    assertEquals("s1.s2", toTest.getSuper().getValue());
    assertEquals("i1.i2 i3.i4", toTest.getInterfaces().toString());
    List<Member> members = toTest.getMembers();
    assertEquals(1, members.size());
    
    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }
}

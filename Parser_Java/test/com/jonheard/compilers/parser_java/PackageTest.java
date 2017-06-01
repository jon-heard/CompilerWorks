
package com.jonheard.compilers.parser_java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.Package;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.SourceFile;

public class PackageTest {
  @Test
  public void basic() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType._PACKAGE, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "p1"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "p2"));
    tokenList.add(new Token(TokenType.SEMICOLON, 0, 0));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(Package.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Package.getIsNext(parser));
    Package toTest = new Package(parser);

    assertEquals(1, toTest.getChildCount());
    assertTrue(toTest.getChild(0) instanceof QualifiedId);
    assertEquals("p1.p2", toTest.getId().getValue());

    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }
}

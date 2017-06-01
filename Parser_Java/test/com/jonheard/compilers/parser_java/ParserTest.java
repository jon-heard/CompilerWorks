
package com.jonheard.compilers.parser_java;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.Id;
import com.jonheard.compilers.parser_java.ir.Import;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.SourceFile;

public class ParserTest {
  @Test
  public void basic() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType._ABSTRACT, 0, 0));
    tokenList.add(new Token(TokenType._BREAK, 0, 0));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("f1", "s1"), tokenList);

    assertEquals("null", parser.peekNextToken().toString());
    assertTrue(parser.getIsTokenType(TokenType._NULL));
    assertFalse(parser.getIsTokenType(TokenType.DOT));
    assertFalse(parser.passTokenIfType(TokenType.DOT));
    assertTrue(parser.getIsTokenType(TokenType._NULL));
    assertTrue(parser.passTokenIfType(TokenType._NULL));
    assertTrue(parser.getIsTokenType(TokenType._ABSTRACT));
    assertTrue(parser.requireTokenToBeOfType(TokenType._ABSTRACT));
    assertTrue(parser.getIsTokenType(TokenType._BREAK));
    Logger.resetCounts();
    assertFalse(parser.requireTokenToBeOfType(TokenType._ABSTRACT));
    assertEquals(1, Logger.getErrorCount());
    assertTrue(parser.getIsTokenType(TokenType._BREAK));
    assertEquals("s1", parser.getSource().getSourceCode());
  }
}

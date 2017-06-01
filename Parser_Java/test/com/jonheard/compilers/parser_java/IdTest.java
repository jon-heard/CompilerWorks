
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
import com.jonheard.util.SourceFile;

public class IdTest {
  @Test
  public void basic() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i1"));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(Id.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(Id.getIsNext(parser));
    Id toTest = new Id(parser);

    assertEquals("i1", toTest.getValue());

    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }
}

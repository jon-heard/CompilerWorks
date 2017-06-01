
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

public class QualifiedIdTest {
  @Test
  public void basic() {
    List<Token> tokenList = new ArrayList<>();
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i1"));
    tokenList.add(new Token(TokenType.DOT, 0, 0));
    tokenList.add(new Token(TokenType.IDENTIFIER, 0, 0, "i2"));
    tokenList.add(new Token(TokenType._NULL, 0, 0));
    Parser parser = new Parser(new SourceFile("", ""), tokenList);

    assertFalse(QualifiedId.getIsNext(parser));
    parser.requireTokenToBeOfType(TokenType._NULL);
    assertTrue(QualifiedId.getIsNext(parser));
    QualifiedId toTest = new QualifiedId(parser);

    assertEquals("i1.i2", toTest.getValue());

    assertTrue(parser.getIsTokenType(TokenType._NULL));
  }
}

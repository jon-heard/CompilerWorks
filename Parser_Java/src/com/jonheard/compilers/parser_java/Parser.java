package com.jonheard.compilers.parser_java;

import java.util.List;

import com.jonheard.compilers.parser_java.ir.CompilationUnit;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;
import com.jonheard.util.SourceFile;

// Parser - UI class for the Java Parser system
public class Parser {
  // Get a printable representation of a parsed compilation unit
  public String unparse(CompilationUnit source) {
    return source.toString();
  }
  
  // Parse a list of tokens into a compilation unit
  public CompilationUnit parse(SourceFile source, List<Token> tokens) {
    this.source = source;
    this.tokenQueue = new RewindableQueue<Token>(tokens);
    return new CompilationUnit(this);
  }

  public SourceFile getSource() { return source; }
  public RewindableQueue<Token> getTokenQueue() { return tokenQueue; }

  // Get the next token to parse
  public Token getNextToken() {
    return tokenQueue.peek();
  }

  // Predicate returns true if the next token matches the given type.  Else false.
  public boolean getIsTokenType(TokenType type) {
    if (tokenQueue == null || type == null) return false;
    if (tokenQueue.isEmpty()) return false;
    return getNextToken().getType() == type;
  }

  // If the next token matches the given type, pass by it and return true.  Else false.
  public boolean passTokenIfType(TokenType type) {
    if (getIsTokenType(type)) {
      tokenQueue.poll();
      return true;
    }
    return false;
  }

  // Return if next token matches the given type.  If not, go into error mode.  Try and salvage
  // parse state to continue the parsing (for errors)
  public boolean requireTokenToBeOfType(TokenType type) {
    // No more left to parse, so quit
    if (tokenQueue.isEmpty()) {
      int lastLineIndex = source.getLineCount() - 1;
      Logger.error("reached end of file while parsing", source, lastLineIndex);
      System.exit(0);
      return false;
    }

    // 
    Token next = getNextToken();
    if (next.getType() == type) {
      mustBeHasErrored = false;
      tokenQueue.poll();
      return true;
    } else if (!mustBeHasErrored) {
      mustBeHasErrored = true;
      Logger.error("'" + type.name() + "' expected", source,
          source.getIndex(next.getColumn(), next.getRow()));
      return false;
    } else {
      while (!tokenQueue.isEmpty() && !getIsTokenType(type)) {
        tokenQueue.poll();
      }
      if (passTokenIfType(type)) {
        mustBeHasErrored = false;
      }
      return true;
    }
  }

  private SourceFile source;
  private RewindableQueue<Token> tokenQueue;
  private static boolean mustBeHasErrored = false;
}

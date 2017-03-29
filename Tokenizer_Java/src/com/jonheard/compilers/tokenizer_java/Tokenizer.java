package com.jonheard.compilers.tokenizer_java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.jonheard.util.Logger;
import com.jonheard.util.SourceFile;
import com.jonheard.util.Trie;

// Tokenizer - Represents an algorithm for mapping java source code to a list of java tokens
public class Tokenizer {

  // Setup precalculated data
  public Tokenizer() {
    javaKeywords = initJavaKeywords();
    numericCharSets = initNumericCharSets();
  }
  // Turn a list of tokens into a string
  public String untokenize(List<Token> source) {
    StringBuffer result = new StringBuffer();
    for (Token token : source) {
      result.append(token.toString());
      result.append('\n');
    }
    return result.toString();
  }

  public List<Token> tokenize(SourceFile source) {
    List<Token> result = new ArrayList<>();

    int currentIndex = 0;
    int sourceLength = source.getLength();
    int currentRow = 0;
    int columnStart = 0;

    while (currentIndex < sourceLength) {
      // The type of this token.  If not set, no token will be added
      TokenType nextTokenType = null;
      // The text for this token.  Most tokens have null text.
      String nextTokenText = null;
      // The amount to advance for the next token.  Defaulted to the length of the current token.
      int advance = -1;
      // The current column this token starts on.
      int currentColumn = currentIndex - columnStart;
      // Run through all token options looking for a fit.
      switch (source.getChar(currentIndex)) {
        case ' ':
        case '\t':
          break;
        case '\n':
          ++currentRow;
          columnStart = currentIndex+1;
          break;
        case '~':
          nextTokenType = TokenType.TILDE;
          break;
        case ';':
          nextTokenType = TokenType.SEMICOLON;
          break;
        case '?':
          nextTokenType = TokenType.QUESTION;
          break;
        case ':':
          nextTokenType = TokenType.COLON;
          break;
        case ',':
          nextTokenType = TokenType.COMMA;
          break;
        case '(':
          nextTokenType = TokenType.PAREN_LEFT;
          break;
        case ')':
          nextTokenType = TokenType.PAREN_RIGHT;
          break;
        case '{':
          nextTokenType = TokenType.CURL_BRACE_LEFT;
          break;
        case '}':
          nextTokenType = TokenType.CURL_BRACE_RIGHT;
          break;
        case '[':
          nextTokenType = TokenType.SQUARE_BRACE_LEFT;
          break;
        case ']':
          nextTokenType = TokenType.SQUARE_BRACE_RIGHT;
          break;
        case '=':
          nextTokenType = getTokenTypeFromEqual(source, currentIndex);
          break;
        case '!':
          nextTokenType = getTokenTypeFromExclaim(source, currentIndex);
          break;
        case '*':
          nextTokenType = getTokenTypeFromStar(source, currentIndex);
          break;
        case '%':
          nextTokenType = getTokenTypeFromPercent(source, currentIndex);
          break;
        case '^':
          nextTokenType = getTokenTypeFromCarat(source, currentIndex);
          break;
        case '+':
          nextTokenType = getTokenTypeFromPlus(source, currentIndex);
          break;
        case '-':
          nextTokenType = getTokenTypeFromDash(source, currentIndex);
          break;
        case '&':
          nextTokenType = getTokenTypeFromAnd(source, currentIndex);
          break;
        case '|':
          nextTokenType = getTokenTypeFromPipe(source, currentIndex);
          break;
        case '<':
          nextTokenType = getTokenTypeFromLeft(source, currentIndex);
          break;
        case '>':
          nextTokenType = getTokenTypeFromRight(source, currentIndex);
          break;
        case '/':
          nextTokenType = getTokenTypeFromSlash(source, currentIndex);
          if (nextTokenType == null) {
            currentIndex = skipComment(source, currentIndex);
            continue;
          }
          break;
        case '\'':
          nextTokenType = TokenType.CHAR;
          nextTokenText = getTokenTextFromChar(source, currentIndex);
          advance = nextTokenText.length() + 2;
          break;
        case '\"':
          nextTokenType = TokenType.STRING;
          nextTokenText = getTokenTextFromString(source, currentIndex);
          // If bad string, continue at the end of the line
          if(nextTokenText == null) {
            nextTokenType = null;
            advance = currentIndex;
            while (source.getChar(advance) != '\n' && source.getChar(advance) != 0) {
              ++advance;
            }
            advance -= currentIndex;
          } else {
            advance = nextTokenText.length() + 2;
          }
          break;
        case '.':
          nextTokenType = getTokenTypeFromDot(source, currentIndex);
          if (nextTokenType != null) {
            break;
          }
        case '0':
        case '1':
        case '2':
        case '3':
        case '4':
        case '5':
        case '6':
        case '7':
        case '8':
        case '9':
          Object[] numberInfo = getTokenInfoFromNumber(source, currentIndex);
          nextTokenType = (TokenType)numberInfo[0];
          nextTokenText = (String)numberInfo[1];
          advance = nextTokenText.length();
          nextTokenText = parseNumber(nextTokenType, nextTokenText);
          break;
        default:
          if (isAlpha(source, currentIndex)) {
            nextTokenType = getTokenTypeFromKeyword(source, currentIndex);
            if (nextTokenType == null) {
              nextTokenType = TokenType.IDENTIFIER;
              nextTokenText = getTokenTextFromIdentifier(source, currentIndex);
            }
          } else {
            Logger.error("illegal character: "+source.getChar(currentIndex), source, currentIndex);
          }
          break;
      }
      if (nextTokenType != null) {
        Token toAdd = new Token(nextTokenType, currentRow, currentColumn, nextTokenText);
        result.add(toAdd);
        if (advance == -1) { advance = toAdd.getLength(); }
      } else {
        if (advance == -1) { advance = 1; }
      }
      currentIndex += advance;
    }
    return result;
  }


  protected final static Character[][] numericChars = {
      {'0','1'},
      {'0','1','2','3','4','5','6','7'},
      {'0','1','2','3','4','5','6','7','8','9'},
      {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','A','B','C','D','E','F'},
      {'_','.','e','E','p','P'},
  };
  protected Trie<TokenType> javaKeywords = initJavaKeywords();
  protected List<Set<Character>> numericCharSets;


  // Parse and return an escape character from the given source code starting at the given index
  protected String getEscapeCharacter(SourceFile source, int index) {
    if (source.getChar(index) != '\\') { return ""; }
    int resultLength = -1;
    switch(source.getChar(index+1)) {
    // basic escape character
      case 'b':   case 'n':   case 't':   case 'r':
      case 'f':   case '\'':  case '\"':  case '\\':
        resultLength = 1;
        break;
    // Octal escape character
      case '0':
      case '1':
      case '2':
      case '3':
        resultLength = 3;
      case '4':
      case '5':
      case '6':
      case '7':
        if (resultLength == -1) { resultLength = 2; }
        for (int i = 1; i < resultLength; ++i) {
          int current = source.getChar(index+1 + i);
          if (current < '0' || current > '7')
          {
            resultLength = i;
            break;
          }
        }
        break;
    // Invalid escape character
      default:
        Logger.error("illegal escape character", source, index);
        return null;
    } // end switch
    return source.getChars(index, index+resultLength);
  }

  protected boolean isAlpha(SourceFile source, int index) {
    char current = source.getChar(index);
    return (current >= 'A' && current <= 'Z') || (current >= 'a' && current <= 'z')
        || (current == '_');
  }

  protected boolean isNumeric(SourceFile source, int index, int radix) {
    char current = source.getChar(index);
    if (radix == 2) { return current == '0' || current == '1'; }
    if (radix == 8) {
      return current >= '0' && current <= '7';
    } else if (radix == 16) {
      return (current >= '0' && current <= '9') || (current >= 'A' && current <= 'F')
          || (current >= 'a' && current <= 'f');
    } else {
      return current >= '0' && current <= '9';
    }
  }

  protected boolean isAlphaNumeric(SourceFile source, int index) {
    return isAlpha(source, index) || isNumeric(source, index, 10);
  }


  // == =
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromEqual(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.EQUAL_EQUAL;
    } else {
      return TokenType.EQUAL;
    }
  }

  // != !
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromExclaim(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.EXCLAIM_EQUAL;
    } else {
      return TokenType.EXCLAIM;
    }
  }

  // *= *
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromStar(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.STAR_EQUAL;
    } else {
      return TokenType.STAR;
    }
  }

  // %= %
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromPercent(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.PERCENT_EQUAL;
    } else {
      return TokenType.PERCENT;
    }
  }

  // ^= ^
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromCarat(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.CARAT_EQUAL;
    } else {
      return TokenType.CARAT;
    }
  }

  // += ++ +
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromPlus(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.PLUS_EQUAL;
    } else if (char2 == '+') {
      return TokenType.PLUS_PLUS;
    } else {
      return TokenType.PLUS;
    }
  }

  // -= -- -
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromDash(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.DASH_EQUAL;
    } else if (char2 == '-') {
      return TokenType.DASH_DASH;
    } else {
      return TokenType.DASH;
    }
  }

  // &= && &
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromAnd(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.AND_EQUAL;
    } else if (char2 == '&') {
      return TokenType.AND_AND;
    } else {
      return TokenType.AND;
    }
  }

  // |= || |
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromPipe(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.PIPE_EQUAL;
    } else if (char2 == '|') {
      return TokenType.PIPE_PIPE;
    } else {
      return TokenType.PIPE;
    }
  }

  // / /=
  // Returns - appropriate token type (or null, if starting a comment) 
  protected TokenType getTokenTypeFromSlash(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.SLASH_EQUAL;
    } else if (char2 == '/' || char2 == '*') {
      return null;
    } else {
      return TokenType.SLASH;
    }
  }

  // <= <<= << <
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromLeft(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.LEFT_EQUAL;
    } else if (char2 == '<') {
      char char3 = source.getChar(index + 2);
      if (char3 == '=') {
        return TokenType.LEFT_LEFT_EQUAL;
      } else {
        return TokenType.LEFT_LEFT;
      }
    } else {
      return TokenType.LEFT;
    }
  }

  // >= >>= >>>= >>> >> >
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromRight(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.RIGHT_EQUAL;
    } else if (char2 == '>') {
      char char3 = source.getChar(index + 2);
      if (char3 == '=') {
        return TokenType.RIGHT_RIGHT_EQUAL;
      } else if (char3 == '>') {
        char char4 = source.getChar(index + 3);
        if (char4 == '=') {
          return TokenType.RIGHT_RIGHT_RIGHT_EQUAL;
        } else {
          return TokenType.RIGHT_RIGHT_RIGHT;
        }
      } else {
        return TokenType.RIGHT_RIGHT;
      }
    } else {
      return TokenType.RIGHT;
    }
  }

  // Comments
  // Returns - the end index of the comment
  protected int skipComment(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    index += 2;

    // Single line comment
    if (char2 == '/') {
      char current = source.getChar(index);
      while (current != '\n') {
        if (current == 0) {
          break;
        }
        ++index;
        current = source.getChar(index);
      }
      ++index;

    // Multi-line comment
    } else if (char2 == '*') {
      char current1 = source.getChar(index);
      char current2 = source.getChar(index+1);
      while (!(current1 == '*' && current2 == '/')) {
        if (current1 == 0) {
          Logger.error("unclosed comment", source, index);
          return index;
        } else {
          ++index;
          current1 = source.getChar(index);
          current2 = source.getChar(index+1);
        }
      }
      index += 2;
    }

    return index;
  }

  // '...'
  // Returns - Character's contents (as a string)
  protected String getTokenTextFromChar(SourceFile source, int startIndex) {
    int index = startIndex+1;
    String result = null;
    char current = source.getChar(index);
  // Escape character
    if (current == '\\') {
      result = getEscapeCharacter(source, index);
      if (result == null) {
        result = ""+source.getChar(index+1);
      } else {
        index += result.length();
      }
  // Non-escape character
    } else {
      result = ""+source.getChar(index);
    }
  // Check for closing apostrophe
    if (source.getChar(index+1) != '\'') {
      Logger.error("unclosed character literal", source, startIndex);
    }
    
    return result;
  }

  // "..."
  // Returns - String's contents (as a String)
  protected String getTokenTextFromString(SourceFile source, int startIndex) {
    int index = startIndex+1;
    char current = source.getChar(index);
    while(current != '\"') {
    // New line or end of file
      if (current == '\n' || current == 0) {
        Logger.error("unclosed string literal", source, startIndex);
        return null;
      }
    // escape characters
      if (current == '\\') {
        String escape = getEscapeCharacter(source, index);
        if (escape == null) {
          ++index;
          current = source.getChar(index);
        } else {
          index += escape.length();
          current = source.getChar(index);
        }
    // Non-escape character
      } else {
        ++index;
        current = source.getChar(index);
      }
    }
    return source.getChars(startIndex+1, index);
  }

  protected TokenType getTokenTypeFromDot(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 >= '0' && char2 <= '9') {
      return null;
    } else {
      return TokenType.DOT;
    }
  }
  
  protected TokenType getTokenTypeFromKeyword(SourceFile source, int index) {
    return javaKeywords.getFromEmbeddedKey(source.getSourceCode(), index);
  }

  protected String getTokenTextFromIdentifier(SourceFile source, int index) {
    int endIndex = index+1;
    while (isAlphaNumeric(source, endIndex)) { ++endIndex; }
    return source.getChars(index, endIndex);
  }

  private enum IntOrFloat { INT, FLOAT, UNKNOWN };
  protected Object[] getTokenInfoFromNumber(SourceFile source, int startIndex) {
    Object[] result = new Object[2];
    Set<Character> SpecialNumeric = numericCharSets.get(4); 
    IntOrFloat isIntOrFloat = IntOrFloat.UNKNOWN;
    int dotIndex = -1;
    int expIndex = -1;
    int intBase8ErrorIndex = -1;
    int baseIndex = 2;
    boolean mayBeBase8 = false;
    int index = startIndex;
    char current = source.getChar(index);

    // Start with dot?
    if (current == '.') {
      dotIndex = index;
      isIntOrFloat = IntOrFloat.FLOAT;
      ++index;
    // Different base?
    } else if (current == '0') {
      char char2 = source.getChar(index+1);
      if (char2 == 'b' || char2 == 'B') {
        isIntOrFloat = IntOrFloat.INT;
        baseIndex = 0;
        index += 2;
      } else if (char2 == 'x' || char2 == 'X') {
        baseIndex = 3;
        index += 2;
      } else {
        mayBeBase8 = true;
      }
    }

    current = source.getChar(index);
    while (numericCharSets.get(baseIndex).contains(current) || SpecialNumeric.contains(current)) {
      if (current == '.') {
        if (dotIndex == -1 && expIndex == -1 && isIntOrFloat != IntOrFloat.INT) {
          isIntOrFloat = IntOrFloat.FLOAT;
          dotIndex = index;
        } else {
          break;
        }
      } else if (current == 'e' || current == 'E' || current == 'p' || current == 'P' &&
            isIntOrFloat != IntOrFloat.INT) {
        if (expIndex == -1) {
          isIntOrFloat = IntOrFloat.FLOAT;
          expIndex = index;
        } else {
          break;
        }
      }
      else if (mayBeBase8 && !numericCharSets.get(1).contains(current)) {
        intBase8ErrorIndex = index;
      }
      ++index;
      current = source.getChar(index);
    }
    
    current = source.getChar(index);
    if (isIntOrFloat == IntOrFloat.FLOAT) {
      if (current == 'f' || current == 'F') {
        result[0] = TokenType.FLOAT;
        ++index;
      } else if (current == 'd' || current == 'D') {
        result[0] = TokenType.DOUBLE;
        ++index;
      } else {
        result[0] = TokenType.DOUBLE;
      }
    } else if (isIntOrFloat == IntOrFloat.INT) {
      if (current == 'l' || current == 'L') {
        result[0] = TokenType.LONG;
        ++index;
      } else {
        result[0] = TokenType.INTEGER;
      }
    } else if (isIntOrFloat == IntOrFloat.UNKNOWN) {
      if (current == 'f' || current == 'F') {
        result[0] = TokenType.FLOAT;
        ++index;
      } else if (current == 'd' || current == 'D') {
        result[0] = TokenType.DOUBLE;
        ++index;
      } else if (current == 'l' || current == 'L') {
        isIntOrFloat = IntOrFloat.INT;
        result[0] = TokenType.LONG;
        ++index;
      } else {
        isIntOrFloat = IntOrFloat.INT;
        result[0] = TokenType.INTEGER;
      }
    }

    if (isIntOrFloat == IntOrFloat.INT && intBase8ErrorIndex != -1) {
      index = intBase8ErrorIndex-1;
    }

    result[1] = source.getChars(startIndex, index);
    return result;
  }
  
  protected String parseNumber(TokenType type, String number) {
    char lastChar = number.charAt(number.length()-1);
    if (lastChar > '9') {
      number = number.substring(0, number.length()-1);
    }
    if (type == TokenType.INTEGER) {
      number = "" + (Integer.decode(number));
    } else if (type == TokenType.LONG) {
      number = "" + (Long.decode(number));
    } else if (type == TokenType.FLOAT) {
      number = "" + (Float.parseFloat(number));
    } else if (type == TokenType.DOUBLE) {
      number = "" + (Double.parseDouble(number));
    }
    return number;
  }

/*
  private TokenType handleZero(SourceFile source, int index) {
    char next = sourceString.charAt(index + 1);
    if (next == 'b' || next == 'B') {
      index += 2;
      if (index >= sourceString.length() || !isNumeric(sourceString, index, 2)) {
        Logger.error("binary numbers must contain at least one binary digit", source.getFilename(),
            Token.getCurrentLine(), getColumn(), source.getLine(Token.getCurrentLine() - 1));
        return null;
      }
      return handleNumber(2);
    } else if (next == 'x' || next == 'X') {
      index += 2;
      if (index >= sourceString.length() || !isNumeric(sourceString, index, 16)) {
        Logger.error("binary numbers must contain at least one hex digit", source.getFilename(),
            Token.getCurrentLine(), getColumn(), source.getLine(Token.getCurrentLine() - 1));
        return null;
      }
      return handleNumber(16);
    } else if (isNumeric(sourceString, index + 1, 8) || next == '.') {
      index++;
      return handleNumber(8);
    } else if (next == 'd' || next == 'D') {
      index++;
      return new Token(TokenType.DOUBLE, getColumn(), "0");
    } else if (next == 'f' || next == 'F') {
      index++;
      return new Token(TokenType.FLOAT, getColumn(), "0");
    } else if (next == 'l' || next == 'L') {
      index++;
      return new Token(TokenType.LONG, getColumn(), "0");
    } else {
      return new Token(TokenType.INTEGER, getColumn(), "0");
    }
  }

  private TokenType handleNumber(int base) {
    StringBuffer result = new StringBuffer();
    boolean hasDot = false, hasE = false;
    char current = sourceString.charAt(index);
    while (isNumeric(sourceString, index, base) || current == '_'
        || current == '.' | current == 'e' || current == 'E') {
      if (current == '_') {
        if (!isNumeric(sourceString, index - 1, base)
            && (index + 1 >= sourceString.length()
                || !isNumeric(sourceString, index + 1, base))) {
          Logger.error("illegal underscore", source.getFilename(), Token.getCurrentLine(),
              getColumn(), source.getLine(Token.getCurrentLine() - 1));
          index++;
          break;
        }
        continue;
      }
      if (current == '.') {
        if (hasDot || hasE) {
          index--;
          return new Token(TokenType.FLOAT, getColumn(), result.toString());
        }
        hasDot = true;
      }
      if (current == 'e' || current == 'E') {
        if (hasE) {
          index--;
          return new Token(TokenType.FLOAT, getColumn(), result.toString());
        }
        hasE = true;
      }
      result.append(current);
      index++;
      if (index >= sourceString.length()) {
        current = '\0';
        break;
      }
      current = sourceString.charAt(index);
    }
    if (hasDot || hasE) {
      if (current == 'f' || current == 'F') {
        index++;
        float value = Float.parseFloat(result.toString());
        return new Token(TokenType.FLOAT, getColumn(), Float.toString(value));
      } else if (current == 'd' || current == 'D') {
        index++;
      }
      double value = Double.parseDouble(result.toString());
      return new Token(TokenType.DOUBLE, getColumn(), Double.toString(value));
    } else {
      if (current == 'l' || current == 'L') {
        index++;
        long value = Long.parseLong(result.toString(), base);
        return new Token(TokenType.LONG, getColumn(), Long.toString(value));
      }
      if (current == 'f' || current == 'F') {
        index++;
        float value = Float.parseFloat(result.toString());
        return new Token(TokenType.FLOAT, getColumn(), Float.toString(value));
      }
      if (current == 'd' || current == 'D') {
        index++;
        double value = Double.parseDouble(result.toString());
        return new Token(TokenType.DOUBLE, getColumn(), Double.toString(value));
      }
      int value = Integer.parseInt(result.toString(), base);
      return new Token(TokenType.INTEGER, getColumn(), Integer.toString(value));
    }
  }
*/

  protected Trie<TokenType> initJavaKeywords() {
    Trie<TokenType> result = new Trie<TokenType>();
    for (TokenType i : EnumSet.allOf(TokenType.class)) {
      if (i.isKeyword()) {
        result.put(i.toString(), i);
      }
    }
    return result;
  }
  
  protected List<Set<Character>> initNumericCharSets() {
    List<Set<Character>> result = new ArrayList<Set<Character>>();
    for (int i = 0; i < numericChars.length; ++i) {
      result.add(new HashSet<Character>(Arrays.asList(numericChars[i])));
    }
    return result;
  }
}

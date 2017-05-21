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
    // bad input check
    if (source == null) { throw new IllegalArgumentException("arg1(source): null"); }
    if (source.size() < 1) { throw new IllegalArgumentException("arg1(source): empty"); }

    StringBuilder result = new StringBuilder();
    for (Token token : source) {
      result.append(token.toString());
      result.append('\n');
    }
    return result.toString();
  }

  public List<Token> tokenize(SourceFile source) {
    // bad input check
    if (source == null) { throw new IllegalArgumentException("arg1(source): null"); }

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
      // The amount to advance for the next token.  Defaults to the length of the new token
      int advance = -1;
      // The current column this token starts on.
      int currentColumn = currentIndex - columnStart;
      // Keep previous error count to check for new errors
      int previousErrorCount = Logger.getErrorCount();
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
          nextTokenType = TokenType.LEFT_PAREN;
          break;
        case ')':
          nextTokenType = TokenType.RIGHT_PAREN;
          break;
        case '{':
          nextTokenType = TokenType.LEFT_CURL;
          break;
        case '}':
          nextTokenType = TokenType.RIGHT_CURL;
          break;
        case '[':
          nextTokenType = TokenType.LEFT_SQUARE;
          break;
        case ']':
          nextTokenType = TokenType.RIGHT_SQUARE;
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
            currentIndex = getCommentLength(source, currentIndex);
            continue;
          }
          break;
        case '\'':
          nextTokenText = getTokenTextFromChar(source, currentIndex);
          advance = nextTokenText.length() + 2;
          if (Logger.getErrorCount() == previousErrorCount) {
            nextTokenType = TokenType.CHAR;
          }
          break;
        case '\"':
          nextTokenText = getTokenTextFromString(source, currentIndex);
          advance = nextTokenText.length() + 2;
          if (Logger.getErrorCount() == previousErrorCount) {
            nextTokenType = TokenType.STRING;
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
          nextTokenText = (String)numberInfo[1];
          advance = nextTokenText.length();
          if (Logger.getErrorCount() == previousErrorCount) {
            nextTokenType = (TokenType)numberInfo[0];
            nextTokenText = parseNumber(nextTokenType, nextTokenText);
          }
          break;
        default:
          if (getIsAlphaChar(source.getChar(currentIndex))) {
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
        Token toAdd;
        if (nextTokenText == null) {
          toAdd = new Token(nextTokenType, currentRow, currentColumn);
        } else {
          toAdd = new Token(nextTokenType, currentRow, currentColumn, nextTokenText);
        }
        result.add(toAdd);
        if (advance == -1) { advance = toAdd.getLength(); }
      } else {
        if (advance == -1) { advance = nextTokenText!=null ? nextTokenText.length() : 1; }
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


  protected boolean getIsAlphaChar(char val) {
    return val == '_' || (val >= 'a' && val <= 'z') || (val >= 'A' && val <= 'Z'); 
  }

  protected boolean getIsAlphaNumericChar(char val) {
    return getIsAlphaChar(val) || (val >= '0' && val <= '9'); 
  }

  // Parse and return an escape character from the given source code starting at the given index
  protected int getEscapeCharacterLength(SourceFile source, int startIndex) {
    if (source.getChar(startIndex) != '\\') { return 1; }
    int index = startIndex + 1;
    int octalLength = -1;
    switch (source.getChar(index)) {
    // basic escape character
      case 'b':   case 'n':   case 't':   case 'r':
      case 'f':   case '\'':  case '\"':  case '\\':
        ++index;
        break;
    // Octal escape character
      case '0':
      case '1':
      case '2':
      case '3':
        octalLength = 3;
      case '4':
      case '5':
      case '6':
      case '7':
        if (octalLength == -1) { octalLength = 2; }
        for (int i = 0; i < octalLength; ++i) {
          ++index;
          int current = source.getChar(index);
          if (current < '0' || current > '7')
          {
            break;
          }
        }
        break;
    // Invalid escape character
      default:
        Logger.error("illegal escape character", source, startIndex);
    } // end switch
    return index-startIndex;
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
      return TokenType.LEFT_TRI_EQUAL;
    } else if (char2 == '<') {
      char char3 = source.getChar(index + 2);
      if (char3 == '=') {
        return TokenType.LEFT_LEFT_EQUAL;
      } else {
        return TokenType.LEFT_LEFT;
      }
    } else {
      return TokenType.LEFT_TRI;
    }
  }

  // >= >>= >>>= >>> >> >
  // Returns - appropriate token type 
  protected TokenType getTokenTypeFromRight(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    if (char2 == '=') {
      return TokenType.RIGHT_TRI_EQUAL;
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
      return TokenType.RIGHT_TRI;
    }
  }

  // Comments
  // Returns - the end index of the comment
  protected int getCommentLength(SourceFile source, int index) {
    char char2 = source.getChar(index + 1);
    index += 2;

    // Single row comment
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

    // Multi-row comment
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
    int index = startIndex + 1;
    char current = source.getChar(index);
  // end of file
    if (current == 0) {
  // Escape character
    } else if (current == '\\') {
      index += getEscapeCharacterLength(source, index);
  // Non-escape character
    } else {
      ++index;
    }
  // Check for closing apostrophe
    if (source.getChar(index) != '\'') {
      Logger.error("unclosed character literal", source, startIndex);
    }
    
    return source.getSubstring(startIndex + 1, index);
  }

  // "..."
  // Returns - String's contents (as a String)
  protected String getTokenTextFromString(SourceFile source, int startIndex) {
    int index = startIndex+1;
    char current = source.getChar(index);
    while(current != '\"') {
    // New row or end of file
      if (current == '\n' || current == 0) {
        Logger.error("unclosed string literal", source, startIndex);
        break;
      }
    // escape characters
      if (current == '\\') {
        index += getEscapeCharacterLength(source, index);
    // Non-escape character
      } else {
        ++index;
      }
      current = source.getChar(index);
    }
    return source.getSubstring(startIndex+1, index);
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
    while (getIsAlphaNumericChar(source.getChar(endIndex))) { ++endIndex; }
    return source.getSubstring(index, endIndex);
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
      } else if (current == 'e' || current == 'E' && isIntOrFloat != IntOrFloat.INT) {
        if (baseIndex == 3) { // hex has 'p' for exponent symbol
          Logger.error("malformed floating point literal", source, startIndex);
          break;
        } else if (expIndex == -1) {
          isIntOrFloat = IntOrFloat.FLOAT;
          expIndex = index;
          char char2 = source.getChar(index+1);
          if (char2 == '+' || char2 == '-') { ++index; }
        } else {
          break;
        }
      } else if (current == 'p' || current == 'P' && isIntOrFloat != IntOrFloat.INT) {
        if (baseIndex != 3) { // non-hex has 'e' for exponent symbol
          Logger.error("malformed floating point literal", source, startIndex);
          break;
        } if (expIndex == -1) {
          isIntOrFloat = IntOrFloat.FLOAT;
          expIndex = index;
          char char2 = source.getChar(index+1);
          if (char2 == '+' || char2 == '-') { ++index; }
          baseIndex = 2; // the exponent of a hex float is still in decimal
        } else {
          break;
        }
      } else if (mayBeBase8 && !numericCharSets.get(1).contains(current) && current != '_') {
        intBase8ErrorIndex = index;
      }
      ++index;
      current = source.getChar(index);
    }

    if (source.getChar(index-1) == '_') {
      Logger.error("Illegal underscore", source, index-1);
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

    result[1] = source.getSubstring(startIndex, index);
    return result;
  }
  
  protected String parseNumber(TokenType type, String number) {
    number = number.replace("_", "");
    char lastChar = number.charAt(number.length()-1);
    // Remove any type label
    if (lastChar > '9') {
      number = number.substring(0, number.length()-1);
    }
    if (type == TokenType.INTEGER && number.length() > 2) {
      String prefix = number.substring(0, 2);
      if (prefix.equals("0b") || prefix.equals("0B")) {
        number = "" + (Integer.parseInt(number.substring(2), 2));
      } else if (prefix.equals("0x") || prefix.equals("0X")) {
        number = "" + (Integer.parseInt(number.substring(2), 16));
      } else if (number.charAt(0) == '0') {
        number = "" + (Integer.parseInt(number.substring(1), 8));
      }
    } else if (type == TokenType.LONG && number.length() > 2) {
      String prefix = number.substring(0, 2);
      if (prefix.equals("0b") || prefix.equals("0B")) {
        number = "" + (Long.parseLong(number.substring(2), 2));
      } else if (prefix.equals("0x") || prefix.equals("0X")) {
        number = "" + (Long.parseLong(number.substring(2), 16));
      } else if (number.charAt(0) == '0') {
        number = "" + (Long.parseLong(number.substring(1), 8));
      }
    } else if (type == TokenType.FLOAT) {
      number = "" + (Float.parseFloat(number));
    } else if (type == TokenType.DOUBLE) {
      number = "" + (Double.parseDouble(number));
    }
    return number;
  }


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

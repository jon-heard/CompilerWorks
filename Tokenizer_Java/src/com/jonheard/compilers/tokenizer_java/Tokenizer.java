package com.jonheard.compilers.tokenizer_java;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.jonheard.util.Logger;
import com.jonheard.util.Trie;

public class Tokenizer
{
	public Tokenizer() {}
	public Tokenizer(String filename, String sourceCode)
	{
		sourceFileInfo = new SourceFileInfo(filename, sourceCode);
	}
	
	public String getFilename() { return sourceFileInfo.getFilename(); }
	public String getSourceCode() { return sourceFileInfo.getSourcecode(); }

	public List<Token> tokenize()
	{
		List<Token> result = new ArrayList<Token>();
		
		Token.setCurrentSourceFileInfo(sourceFileInfo);
		Token.setCurrentRow(1);
		
		if(tokenTypeMap == null) initTokenMap();

		String sourceCode = sourceFileInfo.getSourcecode();
		int sourceLength = sourceCode.length();
		currentIndex = 0;
		while(currentIndex < sourceLength)
		{
			Token toAdd = null;
			switch(sourceCode.charAt(currentIndex))
			{
				case ' ':
				case '\t':
					break;
				case '\r':
				case '\n':
					handleLineBreak();
					break;
				case '~':
					toAdd = new Token(TokenType.TILDE, getCol());
					break;
				case ';':
					toAdd = new Token(TokenType.SEMICOLON, getCol());
					break;
				case '?':
					toAdd = new Token(TokenType.QUESTION, getCol());
					break;
				case ':':
					toAdd = new Token(TokenType.COLON, getCol());
					break;
				case ',':
					toAdd = new Token(TokenType.COMMA, getCol());
					break;
				case '(':
					toAdd = new Token(TokenType.PAREN_LEFT, getCol());
					break;
				case ')':
					toAdd = new Token(TokenType.PAREN_RIGHT, getCol());
					break;
				case '{':
					toAdd = new Token(
							TokenType.CURL_BRACE_LEFT, getCol());
					break;
				case '}':
					toAdd = new Token(
							TokenType.CURL_BRACE_RIGHT, getCol());
					break;
				case '[':
					toAdd = new Token(
							TokenType.SQUARE_BRACE_LEFT, getCol());
					break;
				case ']':
					toAdd = new Token(
							TokenType.SQUARE_BRACE_RIGHT, getCol());
					break;
				case '=':
					toAdd = handleEqual();
					break;					
				case '!':
					toAdd = handleExclaim();
					break;
				case '*':
					toAdd = handleStar();
					break;
				case '%':
					toAdd = handlePercent();
					break;
				case '^':
					toAdd = handleCarat();
					break;
				case '+':
					toAdd = handlePlus();
					break;
				case '-':
					toAdd = handleDash();
					break;
				case '&':
					toAdd = handleAnd();
					break;
				case '|':
					toAdd = handlePipe();
					break;
				case '<':
					toAdd = handleLeft();
					break;
				case '>':
					toAdd = handleRight();
					break;
				case '/':
					toAdd = handleSlash();
					break;
				case '\'':
					toAdd = handleChar();
					break;
				case '\"':
					toAdd = handleString();
					break;
				case '.':
					toAdd = handleDot();
					break;
				case '0':
					toAdd = handleZero();
					break;
				case '1': case '2': case '3': case '4': case '5':
				case '6': case '7': case '8': case '9':
					toAdd = handleNumber(10);
					break;
				default:
					if(isAlpha(sourceCode, currentIndex))
					{
						toAdd = handleIdentifier();
					}
					else
					{
						int row = Token.getCurrentRow();
						Logger.error(
								"illegal character: " +
										sourceCode.charAt(currentIndex),
								sourceFileInfo.getFilename(), row, getCol(),
								sourceFileInfo.getLine(row-1));
					}
					break;
			}
			if(toAdd != null) result.add(toAdd);
			currentIndex++;
		}
		return result;
	}

	public String tokenizeToString()
	{
		List<Token> tokenList = tokenize();
		StringBuffer result = new StringBuffer();
		for(Token token : tokenList)
		{
			result.append(token.toString());
			result.append('\n');
		}
		return result.toString();
	}


	private SourceFileInfo sourceFileInfo;
	private int currentIndex;
	private int colStart = 0;

	private int getCol()
	{
		return currentIndex - colStart;
	}

	private boolean isAlpha(String source, int index)
	{
		char current = source.charAt(index);
		return	(current >= 'A' && current <= 'Z') ||
				(current >= 'a' && current <= 'z') ||
				(current == '_');
	}
	private boolean isNumeric(String source, int index, int radix)
	{
		char current = source.charAt(index);
		if(radix == 2)
		{
			return	current == '0' || current == '1';
		}
		if(radix == 8)
		{
			return	current >= '0' && current <= '7';
		}
		else if(radix == 16)
		{
			return	(current >= '0' && current <= '9') ||
					(current >= 'A' && current <= 'F') ||
					(current >= 'a' && current <= 'f');
		}
		else
		{
			return	current >= '0' && current <= '9';
		}
	}
	private boolean isAlphaNumeric(String source, int index)
	{
		return isAlpha(source, index) || isNumeric(source, index, 10);
	}

	private void handleLineBreak()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		while(	currentIndex+1 < sourceCode.length() && 
				(sourceCode.charAt(currentIndex+1) == '\r' ||
				sourceCode.charAt(currentIndex+1) == '\n'))
		{
			currentIndex++;
		}
		colStart = currentIndex+1;
		Token.incCurrentRow();
	}
	private Token handleEqual()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.EQUAL, getCol());
		}
		currentIndex++;
		return new Token(TokenType.EQUAL_EQUAL, getCol());
	}
	private Token handleExclaim()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.EXCLAIM, getCol());
		}
		currentIndex++;
		return new Token(TokenType.EXCLAIM_EQUAL, getCol());
	}
	private Token handleStar()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.STAR, getCol());
		}
		currentIndex++;
		return new Token(TokenType.STAR_EQUAL, getCol());
	}
	private Token handlePercent()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.PERCENT, getCol());
		}
		currentIndex++;
		return new Token(TokenType.PERCENT_EQUAL, getCol());
	}
	private Token handleCarat()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.CARAT, getCol());
		}
		currentIndex++;
		return new Token(TokenType.CARAT_EQUAL, getCol());
	}
	private Token handlePlus()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.PLUS, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.PLUS_EQUAL, getCol());
		}
		if(nextChar == '+')
		{
			currentIndex++;
			return new Token(TokenType.PLUS_PLUS, getCol());
		}
		return new Token(TokenType.PLUS, getCol());
	}
	private Token handleDash()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.DASH, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.DASH_EQUAL, getCol());
		}
		if(nextChar == '-')
		{
			currentIndex++;
			return new Token(TokenType.DASH_DASH, getCol());
		}
		return new Token(TokenType.DASH, getCol());
	}
	private Token handleAnd()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.AND, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.AND_EQUAL, getCol());
		}
		if(nextChar == '&')
		{
			currentIndex++;
			return new Token(TokenType.AND_AND, getCol());
		}
		return new Token(TokenType.AND, getCol());
	}
	private Token handlePipe()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.PIPE, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.PIPE_EQUAL, getCol());
		}
		if(nextChar == '|')
		{
			currentIndex++;
			return new Token(TokenType.PIPE_PIPE, getCol());
		}
		return new Token(TokenType.PIPE, getCol());
	}
	private Token handleLeft()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.LEFT, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.LEFT_EQUAL, getCol());
		}
		if(nextChar == '<')
		{
			currentIndex++;
			if(	currentIndex >= sourceCode.length()-1 ||
					sourceCode.charAt(currentIndex+1) != '=')
			{
				return new Token(TokenType.LEFT_LEFT, getCol());
			}
			currentIndex++;
			return new Token(TokenType.LEFT_LEFT_EQUAL, getCol());
		}
		return new Token(TokenType.LEFT, getCol());
	}
	private Token handleRight()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.RIGHT, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.RIGHT_EQUAL, getCol());
		}
		if(nextChar == '>')
		{
			currentIndex++;
			if(currentIndex >= sourceCode.length()-1)
			{
				return new Token(TokenType.RIGHT_RIGHT, getCol());
			}
			nextChar = sourceCode.charAt(currentIndex+1);
			if(nextChar == '=')
			{
				currentIndex++;
				return new Token(TokenType.RIGHT_RIGHT_EQUAL, getCol());
			}
			if(nextChar == '>')
			{
				currentIndex++;
				if(	currentIndex >= sourceCode.length()-1 ||
						sourceCode.charAt(currentIndex+1) != '=')
				{
					return new Token(
							TokenType.RIGHT_RIGHT_RIGHT, getCol());
				}
				currentIndex++;
				return new Token(
						TokenType.RIGHT_RIGHT_RIGHT_EQUAL, getCol());
			}
			return new Token(TokenType.RIGHT_RIGHT, getCol());
		}
		return new Token(TokenType.RIGHT, getCol());
	}
	private Token handleSlash()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.SLASH, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '/')
		{
			while(currentIndex < sourceCode.length())
			{
				currentIndex++;
				if(	sourceCode.charAt(currentIndex)=='\r' ||
					sourceCode.charAt(currentIndex)=='\n')
				{
					break;
				}
			}
			return null;
		}
		if(nextChar == '*')
		{
			currentIndex++;
			while(currentIndex < sourceCode.length())
			{
				currentIndex++;
				if(sourceCode.charAt(currentIndex) == '*')
				{
					if(	currentIndex+1 < sourceCode.length() &&
						sourceCode.charAt(currentIndex+1) == '/')
					{
						currentIndex++;
						break;
					}
				}
			}
			return null;
		}
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.SLASH_EQUAL, getCol());
		}
		return new Token(TokenType.SLASH, getCol());
	}
	
	private Token handleChar()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex > sourceCode.length()-3)
		{
			Logger.error(
					"unclosed character literal", sourceFileInfo.getFilename(),
					Token.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(Token.getCurrentRow()-1));
			currentIndex+= 3;
			return null;
		}
		currentIndex++;
		StringBuffer text = new StringBuffer();
		text.append(sourceCode.charAt(currentIndex));
		if(text.toString().equals("\\"))
		{
			if(currentIndex > sourceCode.length()-3)
			{
				Logger.error(
					"unclosed character literal", sourceFileInfo.getFilename(),
					Token.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(Token.getCurrentRow()-1));
				currentIndex+= 2;
				return null;
			}
			currentIndex++;
			text.append(sourceCode.charAt(currentIndex));
		}
		currentIndex++;
		if(sourceCode.charAt(currentIndex) != '\'')
		{
			Logger.error(
				"unclosed character literal", sourceFileInfo.getFilename(),
				Token.getCurrentRow(), getCol(),
				sourceFileInfo.getLine(Token.getCurrentRow()-1));
		}
		return new Token(TokenType.CHAR, text.toString(), getCol());
	}
	
	private Token handleString()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		currentIndex++;
		if(currentIndex > sourceCode.length()-1)
		{
			Logger.error(
					"unclosed string literal", sourceFileInfo.getFilename(),
					Token.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(Token.getCurrentRow()-1));
			return null;
		}
		StringBuffer text = new StringBuffer();
		char curChar = sourceCode.charAt(currentIndex);
		while(curChar != '\r' && curChar != '\n' && curChar != '\"')
		{
			text.append(curChar);
			currentIndex++;
			if(currentIndex > sourceCode.length()-1)
			{
				break;
			}
			curChar = sourceCode.charAt(currentIndex);
		}
		if(curChar != '\"')
		{
			Logger.error(
					"unclosed string literal", sourceFileInfo.getFilename(),
					Token.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(Token.getCurrentRow()-1));
			currentIndex+= 3;
		}
		return new Token(TokenType.STRING, text.toString(), getCol());
	}
	
	private Token handleIdentifier()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		Token result = null;
		if(isAlpha(sourceCode, currentIndex))
		{
			TokenType type =
					tokenTypeMap.getFromEmbeddedKey(sourceCode, currentIndex);
			if(type != null)
			{
				result = new Token(type, getCol());
				currentIndex +=
						result.toString().length() - 1;
			}
			else
			{
				int identifierEnd = currentIndex+1;
				while(isAlphaNumeric(sourceCode, identifierEnd))
				{
					identifierEnd++;
				}
				result = new Token(
					TokenType.IDENTIFIER,
					sourceCode.substring(currentIndex, identifierEnd),
					getCol());
				currentIndex=identifierEnd-1;
			}
		}
		return result;
	}
	
	private Token handleDot()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(isNumeric(sourceCode, currentIndex+1, 10))
		{
			return handleNumber(10);
		}
		return new Token(TokenType.DOT, getCol());
	}
	private Token handleZero()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		char next = sourceCode.charAt(currentIndex+1);
		if(next == 'b' || next == 'B')
		{
			currentIndex += 2;
			if(	currentIndex >= sourceCode.length() ||
				!isNumeric(sourceCode, currentIndex, 2))
			{
				Logger.error(
						"binary numbers must contain at least one binary digit",
						sourceFileInfo.getFilename(),
						Token.getCurrentRow(), getCol(),
						sourceFileInfo.getLine(Token.getCurrentRow()-1));
				return null;
			}
			return handleNumber(2);
		}
		else if(next == 'x' || next == 'X')
		{
			currentIndex += 2;
			if(	currentIndex >= sourceCode.length() ||
				!isNumeric(sourceCode, currentIndex, 16))
			{
				Logger.error(
						"binary numbers must contain at least one hex digit",
						sourceFileInfo.getFilename(),
						Token.getCurrentRow(), getCol(),
						sourceFileInfo.getLine(Token.getCurrentRow()-1));
				return null;
			}
			return handleNumber(16);
		}
		else if(isNumeric(sourceCode, currentIndex+1, 8) || next == '.')
		{
			currentIndex ++;
			return handleNumber(8);
		}
		else if(next == 'd' || next == 'D')
		{
			currentIndex++;
			return new Token(TokenType.DOUBLE, "0", getCol());
		}
		else if(next == 'f' || next == 'F')
		{
			currentIndex++;
			return new Token(TokenType.FLOAT, "0", getCol());
		}
		else if(next == 'l' || next == 'L')
		{
			currentIndex++;
			return new Token(TokenType.LONG, "0", getCol());
		}
		else
		{
			return new Token(TokenType.INTEGER, "0", getCol());
		}
	}
	private Token handleNumber(int base)
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		StringBuffer result = new StringBuffer();
		boolean hasDot = false, hasE = false;
		char current = sourceCode.charAt(currentIndex);
		while(	isNumeric(sourceCode, currentIndex, base) ||
				current == '_' || current == '.' |
				current == 'e' || current == 'E')
		{
			if(current == '_')
			{
				if(	!isNumeric(sourceCode, currentIndex-1, base) &&
					(currentIndex+1 >= sourceCode.length() ||
					!isNumeric(sourceCode, currentIndex+1, base)))
				{
					Logger.error(
						"illegal underscore", sourceFileInfo.getFilename(),
						Token.getCurrentRow(), getCol(),
						sourceFileInfo.getLine(Token.getCurrentRow()-1));
					currentIndex++;
					break;
				}
				continue;
			}
			if(current == '.')
			{
				if(hasDot || hasE)
				{
					currentIndex--;
					return new Token(
							TokenType.FLOAT, result.toString(), getCol());
				}
				hasDot = true;
			}
			if(current == 'e' || current == 'E')
			{
				if(hasE)
				{
					currentIndex--;
					return new Token(
							TokenType.FLOAT, result.toString(), getCol());
				}
				hasE = true;
			}
			result.append(current);
			currentIndex++;
			if(currentIndex >= sourceCode.length())
			{
				current = '\0';
				break;
			}
			current = sourceCode.charAt(currentIndex);
		}
		if(hasDot || hasE)
		{
			if(current == 'f' || current == 'F')
			{
				current++;
				float value = Float.parseFloat(result.toString());
				return new Token(
						TokenType.FLOAT, Float.toString(value), getCol());
			}
			else if(current == 'd' || current == 'D')	
			{
				current++;
			}
			double value = Double.parseDouble(result.toString());
			return new Token(
					TokenType.DOUBLE, Double.toString(value), getCol());
		}
		else
		{
			if(current == 'l' || current == 'L')
			{
				current++;
				long value = Long.parseLong(result.toString(), base);
				return new Token(
						TokenType.LONG, Long.toString(value), getCol());
			}
			if(current == 'f' || current == 'F')
			{
				current++;
				float value = Float.parseFloat(result.toString());
				return new Token(
						TokenType.FLOAT, Float.toString(value), getCol());
			}
			if(current == 'd' || current == 'D')
			{
				current++;
				double value = Double.parseDouble(result.toString());
				return new Token(
						TokenType.DOUBLE, Double.toString(value), getCol());
			}
			int value = Integer.parseInt(result.toString(), base);
			return new Token(
					TokenType.INTEGER, Integer.toString(value), getCol());
		}
	}


	private static Trie<TokenType> tokenTypeMap = null;
	private static void initTokenMap()
	{
		tokenTypeMap = new Trie<TokenType>();
		for (TokenType i : EnumSet.allOf(TokenType.class))
        {
			if(i.isIdentifier())
			{
				tokenTypeMap.put(i.toString(), i);
			}
        }
	}
}

package com.jonheard.compilers.tokenizer_java;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.jonheard.util.Logger;
import com.jonheard.util.SourceFileInfo;
import com.jonheard.util.Trie;

public class Tokenizer
{
	public List<Token> tokenize(SourceFileInfo source)
	{
		this.source = source;
		sourceCode = source.getSourcecode();

		List<Token> result = new ArrayList<Token>();
		
		Token.setCurrentLine(1);
		
		if(tokenTypeMap == null) initTokenMap();

		currentIndex = 0;
		int sourceLength = sourceCode.length();
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
					toAdd = new Token(TokenType.TILDE, getColumn());
					break;
				case ';':
					toAdd = new Token(TokenType.SEMICOLON, getColumn());
					break;
				case '?':
					toAdd = new Token(TokenType.QUESTION, getColumn());
					break;
				case ':':
					toAdd = new Token(TokenType.COLON, getColumn());
					break;
				case ',':
					toAdd = new Token(TokenType.COMMA, getColumn());
					break;
				case '(':
					toAdd = new Token(TokenType.PAREN_LEFT, getColumn());
					break;
				case ')':
					toAdd = new Token(TokenType.PAREN_RIGHT, getColumn());
					break;
				case '{':
					toAdd = new Token(
							TokenType.CURL_BRACE_LEFT, getColumn());
					break;
				case '}':
					toAdd = new Token(
							TokenType.CURL_BRACE_RIGHT, getColumn());
					break;
				case '[':
					toAdd = new Token(
							TokenType.SQUARE_BRACE_LEFT, getColumn());
					break;
				case ']':
					toAdd = new Token(
							TokenType.SQUARE_BRACE_RIGHT, getColumn());
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
						toAdd = handleId();
					}
					else
					{
						int line = Token.getCurrentLine();
						Logger.error(
								"illegal character: " +
										sourceCode.charAt(currentIndex),
								source.getFilename(), line, getColumn(),
								source.getLine(line-1));
					}
					break;
			}
			if(toAdd != null) result.add(toAdd);
			currentIndex++;
		}
		return result;
	}

	private int currentIndex;
	private int columnStart = 0;
	private SourceFileInfo source;
	private String sourceCode;

	private int getColumn()
	{
		return currentIndex - columnStart;
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
		while(	currentIndex+1 < sourceCode.length() && 
				(sourceCode.charAt(currentIndex+1) == '\r' ||
				sourceCode.charAt(currentIndex+1) == '\n'))
		{
			currentIndex++;
		}
		columnStart = currentIndex+1;
		Token.incCurrentLine();
	}
	private Token handleEqual()
	{
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.EQUAL, getColumn());
		}
		currentIndex++;
		return new Token(TokenType.EQUAL_EQUAL, getColumn());
	}
	private Token handleExclaim()
	{
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.EXCLAIM, getColumn());
		}
		currentIndex++;
		return new Token(TokenType.EXCLAIM_EQUAL, getColumn());
	}
	private Token handleStar()
	{
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.STAR, getColumn());
		}
		currentIndex++;
		return new Token(TokenType.STAR_EQUAL, getColumn());
	}
	private Token handlePercent()
	{
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.PERCENT, getColumn());
		}
		currentIndex++;
		return new Token(TokenType.PERCENT_EQUAL, getColumn());
	}
	private Token handleCarat()
	{
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new Token(TokenType.CARAT, getColumn());
		}
		currentIndex++;
		return new Token(TokenType.CARAT_EQUAL, getColumn());
	}
	private Token handlePlus()
	{
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.PLUS, getColumn());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.PLUS_EQUAL, getColumn());
		}
		if(nextChar == '+')
		{
			currentIndex++;
			return new Token(TokenType.PLUS_PLUS, getColumn());
		}
		return new Token(TokenType.PLUS, getColumn());
	}
	private Token handleDash()
	{
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.DASH, getColumn());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.DASH_EQUAL, getColumn());
		}
		if(nextChar == '-')
		{
			currentIndex++;
			return new Token(TokenType.DASH_DASH, getColumn());
		}
		return new Token(TokenType.DASH, getColumn());
	}
	private Token handleAnd()
	{
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.AND, getColumn());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.AND_EQUAL, getColumn());
		}
		if(nextChar == '&')
		{
			currentIndex++;
			return new Token(TokenType.AND_AND, getColumn());
		}
		return new Token(TokenType.AND, getColumn());
	}
	private Token handlePipe()
	{
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.PIPE, getColumn());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.PIPE_EQUAL, getColumn());
		}
		if(nextChar == '|')
		{
			currentIndex++;
			return new Token(TokenType.PIPE_PIPE, getColumn());
		}
		return new Token(TokenType.PIPE, getColumn());
	}
	private Token handleLeft()
	{
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.LEFT, getColumn());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.LEFT_EQUAL, getColumn());
		}
		if(nextChar == '<')
		{
			currentIndex++;
			if(	currentIndex >= sourceCode.length()-1 ||
					sourceCode.charAt(currentIndex+1) != '=')
			{
				return new Token(TokenType.LEFT_LEFT, getColumn());
			}
			currentIndex++;
			return new Token(TokenType.LEFT_LEFT_EQUAL, getColumn());
		}
		return new Token(TokenType.LEFT, getColumn());
	}
	private Token handleRight()
	{
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.RIGHT, getColumn());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new Token(TokenType.RIGHT_EQUAL, getColumn());
		}
		if(nextChar == '>')
		{
			currentIndex++;
			if(currentIndex >= sourceCode.length()-1)
			{
				return new Token(TokenType.RIGHT_RIGHT, getColumn());
			}
			nextChar = sourceCode.charAt(currentIndex+1);
			if(nextChar == '=')
			{
				currentIndex++;
				return new Token(TokenType.RIGHT_RIGHT_EQUAL, getColumn());
			}
			if(nextChar == '>')
			{
				currentIndex++;
				if(	currentIndex >= sourceCode.length()-1 ||
						sourceCode.charAt(currentIndex+1) != '=')
				{
					return new Token(
							TokenType.RIGHT_RIGHT_RIGHT, getColumn());
				}
				currentIndex++;
				return new Token(
						TokenType.RIGHT_RIGHT_RIGHT_EQUAL, getColumn());
			}
			return new Token(TokenType.RIGHT_RIGHT, getColumn());
		}
		return new Token(TokenType.RIGHT, getColumn());
	}
	private Token handleSlash()
	{
		if(currentIndex >= sourceCode.length()-1)
		{
			return new Token(TokenType.SLASH, getColumn());
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
			return new Token(TokenType.SLASH_EQUAL, getColumn());
		}
		return new Token(TokenType.SLASH, getColumn());
	}
	
	private Token handleChar()
	{
		if(currentIndex > sourceCode.length()-3)
		{
			Logger.error(
					"unclosed character literal", source.getFilename(),
					Token.getCurrentLine(), getColumn(),
					source.getLine(Token.getCurrentLine()-1));
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
					"unclosed character literal", source.getFilename(),
					Token.getCurrentLine(), getColumn(),
					source.getLine(Token.getCurrentLine()-1));
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
				"unclosed character literal", source.getFilename(),
				Token.getCurrentLine(), getColumn(),
				source.getLine(Token.getCurrentLine()-1));
		}
		return new Token(TokenType.CHAR, getColumn(), text.toString());
	}
	
	private Token handleString()
	{
		int column = getColumn();
		currentIndex++;
		if(currentIndex > sourceCode.length()-1)
		{
			Logger.error(
					"unclosed string literal", source.getFilename(),
					Token.getCurrentLine(), column,
					source.getLine(Token.getCurrentLine()-1));
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
					"unclosed string literal", source.getFilename(),
					Token.getCurrentLine(), column,
					source.getLine(Token.getCurrentLine()-1));
			currentIndex+= 3;
		}
		return new Token(TokenType.STRING, getColumn(), text.toString());
	}
	
	private Token handleId()
	{
		Token result = null;
		if(isAlpha(sourceCode, currentIndex))
		{
			TokenType type =
					tokenTypeMap.getFromEmbeddedKey(sourceCode, currentIndex);
			if(type != null)
			{
				result = new Token(type, getColumn());
				currentIndex +=
						result.toString().length() - 1;
			}
			else
			{
				int idEnd = currentIndex+1;
				while(isAlphaNumeric(sourceCode, idEnd))
				{
					idEnd++;
				}
				result = new Token(
					TokenType.ID,
					getColumn(),
					sourceCode.substring(currentIndex, idEnd));
				currentIndex=idEnd-1;
			}
		}
		return result;
	}
	
	private Token handleDot()
	{
		if(isNumeric(sourceCode, currentIndex+1, 10))
		{
			return handleNumber(10);
		}
		return new Token(TokenType.DOT, getColumn());
	}
	private Token handleZero()
	{
		char next = sourceCode.charAt(currentIndex+1);
		if(next == 'b' || next == 'B')
		{
			currentIndex += 2;
			if(	currentIndex >= sourceCode.length() ||
				!isNumeric(sourceCode, currentIndex, 2))
			{
				Logger.error(
						"binary numbers must contain at least one binary digit",
						source.getFilename(),
						Token.getCurrentLine(), getColumn(),
						source.getLine(Token.getCurrentLine()-1));
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
						source.getFilename(),
						Token.getCurrentLine(), getColumn(),
						source.getLine(Token.getCurrentLine()-1));
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
			return new Token(TokenType.DOUBLE, getColumn(), "0");
		}
		else if(next == 'f' || next == 'F')
		{
			currentIndex++;
			return new Token(TokenType.FLOAT, getColumn(), "0");
		}
		else if(next == 'l' || next == 'L')
		{
			currentIndex++;
			return new Token(TokenType.LONG, getColumn(), "0");
		}
		else
		{
			return new Token(TokenType.INTEGER, getColumn(), "0");
		}
	}
	private Token handleNumber(int base)
	{
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
						"illegal underscore", source.getFilename(),
						Token.getCurrentLine(), getColumn(),
						source.getLine(Token.getCurrentLine()-1));
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
							TokenType.FLOAT, getColumn(), result.toString());
				}
				hasDot = true;
			}
			if(current == 'e' || current == 'E')
			{
				if(hasE)
				{
					currentIndex--;
					return new Token(
							TokenType.FLOAT, getColumn(), result.toString());
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
						TokenType.FLOAT, getColumn(), Float.toString(value));
			}
			else if(current == 'd' || current == 'D')	
			{
				current++;
			}
			double value = Double.parseDouble(result.toString());
			return new Token(
					TokenType.DOUBLE, getColumn(), Double.toString(value));
		}
		else
		{
			if(current == 'l' || current == 'L')
			{
				current++;
				long value = Long.parseLong(result.toString(), base);
				return new Token(
						TokenType.LONG, getColumn(), Long.toString(value));
			}
			if(current == 'f' || current == 'F')
			{
				current++;
				float value = Float.parseFloat(result.toString());
				return new Token(
						TokenType.FLOAT, getColumn(), Float.toString(value));
			}
			if(current == 'd' || current == 'D')
			{
				current++;
				double value = Double.parseDouble(result.toString());
				return new Token(
						TokenType.DOUBLE, getColumn(), Double.toString(value));
			}
			int value = Integer.parseInt(result.toString(), base);
			return new Token(
					TokenType.INTEGER, getColumn(), Integer.toString(value));
		}
	}


	private static Trie<TokenType> tokenTypeMap = null;
	private static void initTokenMap()
	{
		tokenTypeMap = new Trie<TokenType>();
		for (TokenType i : EnumSet.allOf(TokenType.class))
        {
			if(i.isKeyword())
			{
				tokenTypeMap.put(i.toString(), i);
			}
        }
	}
}

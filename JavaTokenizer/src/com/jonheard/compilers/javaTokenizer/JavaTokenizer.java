package com.jonheard.compilers.javaTokenizer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.jonheard.util.Logger;
import com.jonheard.util.Trie;

public class JavaTokenizer
{
	public JavaTokenizer() {}
	public JavaTokenizer(String filename, String sourceCode)
	{
		sourceFileInfo = new SourceFileInfo(filename, sourceCode);
	}
	
	public String getFilename() { return sourceFileInfo.getFilename(); }
	public String getSourceCode() { return sourceFileInfo.getSourcecode(); }

	public List<JavaToken> tokenize()
	{
		List<JavaToken> result = new ArrayList<JavaToken>();
		
		JavaToken.setCurrentSourceFileInfo(sourceFileInfo);
		JavaToken.setCurrentRow(1);
		
		if(tokenTypeMap == null) initTokenMap();

		String sourceCode = sourceFileInfo.getSourcecode();
		int sourceLength = sourceCode.length();
		currentIndex = 0;
		while(currentIndex < sourceLength)
		{
			JavaToken toAdd = null;
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
					toAdd = new JavaToken(JavaTokenType.TILDE, getCol());
					break;
				case ';':
					toAdd = new JavaToken(JavaTokenType.SEMICOLON, getCol());
					break;
				case '?':
					toAdd = new JavaToken(JavaTokenType.QUESTION, getCol());
					break;
				case ':':
					toAdd = new JavaToken(JavaTokenType.COLON, getCol());
					break;
				case ',':
					toAdd = new JavaToken(JavaTokenType.COMMA, getCol());
					break;
				case '(':
					toAdd = new JavaToken(JavaTokenType.PAREN_LEFT, getCol());
					break;
				case ')':
					toAdd = new JavaToken(JavaTokenType.PAREN_RIGHT, getCol());
					break;
				case '{':
					toAdd = new JavaToken(
							JavaTokenType.CURL_BRACE_LEFT, getCol());
					break;
				case '}':
					toAdd = new JavaToken(
							JavaTokenType.CURL_BRACE_RIGHT, getCol());
					break;
				case '[':
					toAdd = new JavaToken(
							JavaTokenType.SQUARE_BRACE_LEFT, getCol());
					break;
				case ']':
					toAdd = new JavaToken(
							JavaTokenType.SQUARE_BRACE_RIGHT, getCol());
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
						int row = JavaToken.getCurrentRow();
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
		List<JavaToken> tokenList = tokenize();
		StringBuffer result = new StringBuffer();
		for(JavaToken token : tokenList)
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
		JavaToken.incCurrentRow();
	}
	private JavaToken handleEqual()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.EQUAL, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.EQUAL_EQUAL, getCol());
	}
	private JavaToken handleExclaim()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.EXCLAIM, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.EXCLAIM_EQUAL, getCol());
	}
	private JavaToken handleStar()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.STAR, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.STAR_EQUAL, getCol());
	}
	private JavaToken handlePercent()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.PERCENT, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.PERCENT_EQUAL, getCol());
	}
	private JavaToken handleCarat()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(	currentIndex >= sourceCode.length()-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.CARAT, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.CARAT_EQUAL, getCol());
	}
	private JavaToken handlePlus()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new JavaToken(JavaTokenType.PLUS, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PLUS_EQUAL, getCol());
		}
		if(nextChar == '+')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PLUS_PLUS, getCol());
		}
		return new JavaToken(JavaTokenType.PLUS, getCol());
	}
	private JavaToken handleDash()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new JavaToken(JavaTokenType.DASH, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.DASH_EQUAL, getCol());
		}
		if(nextChar == '-')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.DASH_DASH, getCol());
		}
		return new JavaToken(JavaTokenType.DASH, getCol());
	}
	private JavaToken handleAnd()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new JavaToken(JavaTokenType.AND, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.AND_EQUAL, getCol());
		}
		if(nextChar == '&')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.AND_AND, getCol());
		}
		return new JavaToken(JavaTokenType.AND, getCol());
	}
	private JavaToken handlePipe()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new JavaToken(JavaTokenType.PIPE, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PIPE_EQUAL, getCol());
		}
		if(nextChar == '|')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PIPE_PIPE, getCol());
		}
		return new JavaToken(JavaTokenType.PIPE, getCol());
	}
	private JavaToken handleLeft()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new JavaToken(JavaTokenType.LEFT, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.LEFT_EQUAL, getCol());
		}
		if(nextChar == '<')
		{
			currentIndex++;
			if(	currentIndex >= sourceCode.length()-1 ||
					sourceCode.charAt(currentIndex+1) != '=')
			{
				return new JavaToken(JavaTokenType.LEFT_LEFT, getCol());
			}
			currentIndex++;
			return new JavaToken(JavaTokenType.LEFT_LEFT_EQUAL, getCol());
		}
		return new JavaToken(JavaTokenType.LEFT, getCol());
	}
	private JavaToken handleRight()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new JavaToken(JavaTokenType.RIGHT, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.RIGHT_EQUAL, getCol());
		}
		if(nextChar == '>')
		{
			currentIndex++;
			if(currentIndex >= sourceCode.length()-1)
			{
				return new JavaToken(JavaTokenType.RIGHT_RIGHT, getCol());
			}
			nextChar = sourceCode.charAt(currentIndex+1);
			if(nextChar == '=')
			{
				currentIndex++;
				return new JavaToken(JavaTokenType.RIGHT_RIGHT_EQUAL, getCol());
			}
			if(nextChar == '>')
			{
				currentIndex++;
				if(	currentIndex >= sourceCode.length()-1 ||
						sourceCode.charAt(currentIndex+1) != '=')
				{
					return new JavaToken(
							JavaTokenType.RIGHT_RIGHT_RIGHT, getCol());
				}
				currentIndex++;
				return new JavaToken(
						JavaTokenType.RIGHT_RIGHT_RIGHT_EQUAL, getCol());
			}
			return new JavaToken(JavaTokenType.RIGHT_RIGHT, getCol());
		}
		return new JavaToken(JavaTokenType.RIGHT, getCol());
	}
	private JavaToken handleSlash()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex >= sourceCode.length()-1)
		{
			return new JavaToken(JavaTokenType.SLASH, getCol());
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
			return new JavaToken(JavaTokenType.SLASH_EQUAL, getCol());
		}
		return new JavaToken(JavaTokenType.SLASH, getCol());
	}
	
	private JavaToken handleChar()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(currentIndex > sourceCode.length()-3)
		{
			Logger.error(
					"unclosed character literal", sourceFileInfo.getFilename(),
					JavaToken.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
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
					JavaToken.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
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
				JavaToken.getCurrentRow(), getCol(),
				sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
		}
		return new JavaToken(JavaTokenType.CHAR, text.toString(), getCol());
	}
	
	private JavaToken handleString()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		currentIndex++;
		if(currentIndex > sourceCode.length()-1)
		{
			Logger.error(
					"unclosed string literal", sourceFileInfo.getFilename(),
					JavaToken.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
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
					JavaToken.getCurrentRow(), getCol(),
					sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
			currentIndex+= 3;
		}
		return new JavaToken(JavaTokenType.STRING, text.toString(), getCol());
	}
	
	private JavaToken handleIdentifier()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		JavaToken result = null;
		if(isAlpha(sourceCode, currentIndex))
		{
			JavaTokenType type =
					tokenTypeMap.getFromEmbeddedKey(sourceCode, currentIndex);
			if(type != null)
			{
				result = new JavaToken(type, getCol());
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
				result = new JavaToken(
					JavaTokenType.IDENTIFIER,
					sourceCode.substring(currentIndex, identifierEnd),
					getCol());
				currentIndex=identifierEnd-1;
			}
		}
		return result;
	}
	
	private JavaToken handleDot()
	{
		String sourceCode = sourceFileInfo.getSourcecode();
		if(isNumeric(sourceCode, currentIndex+1, 10))
		{
			return handleNumber(10);
		}
		return new JavaToken(JavaTokenType.DOT, getCol());
	}
	private JavaToken handleZero()
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
						JavaToken.getCurrentRow(), getCol(),
						sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
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
						JavaToken.getCurrentRow(), getCol(),
						sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
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
			return new JavaToken(JavaTokenType.DOUBLE, "0", getCol());
		}
		else if(next == 'f' || next == 'F')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.FLOAT, "0", getCol());
		}
		else if(next == 'l' || next == 'L')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.LONG, "0", getCol());
		}
		else
		{
			return new JavaToken(JavaTokenType.INTEGER, "0", getCol());
		}
	}
	private JavaToken handleNumber(int base)
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
						JavaToken.getCurrentRow(), getCol(),
						sourceFileInfo.getLine(JavaToken.getCurrentRow()-1));
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
					return new JavaToken(
							JavaTokenType.FLOAT, result.toString(), getCol());
				}
				hasDot = true;
			}
			if(current == 'e' || current == 'E')
			{
				if(hasE)
				{
					currentIndex--;
					return new JavaToken(
							JavaTokenType.FLOAT, result.toString(), getCol());
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
				return new JavaToken(
						JavaTokenType.FLOAT, Float.toString(value), getCol());
			}
			else if(current == 'd' || current == 'D')	
			{
				current++;
			}
			double value = Double.parseDouble(result.toString());
			return new JavaToken(
					JavaTokenType.DOUBLE, Double.toString(value), getCol());
		}
		else
		{
			if(current == 'l' || current == 'L')
			{
				current++;
				long value = Long.parseLong(result.toString(), base);
				return new JavaToken(
						JavaTokenType.LONG, Long.toString(value), getCol());
			}
			if(current == 'f' || current == 'F')
			{
				current++;
				float value = Float.parseFloat(result.toString());
				return new JavaToken(
						JavaTokenType.FLOAT, Float.toString(value), getCol());
			}
			if(current == 'd' || current == 'D')
			{
				current++;
				double value = Double.parseDouble(result.toString());
				return new JavaToken(
						JavaTokenType.DOUBLE, Double.toString(value), getCol());
			}
			int value = Integer.parseInt(result.toString(), base);
			return new JavaToken(
					JavaTokenType.INTEGER, Integer.toString(value), getCol());
		}
	}


	private static Trie<JavaTokenType> tokenTypeMap = null;
	private static void initTokenMap()
	{
		tokenTypeMap = new Trie<JavaTokenType>();
		for (JavaTokenType i : EnumSet.allOf(JavaTokenType.class))
        {
			if(i.isIdentifier())
			{
				String name = i.toString();
				tokenTypeMap.put(name, i);
			}
        }
	}
}

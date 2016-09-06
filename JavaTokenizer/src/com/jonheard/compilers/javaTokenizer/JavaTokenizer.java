package com.jonheard.compilers.javaTokenizer;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import com.jonheard.util.Logger;
import com.jonheard.util.Trie;

public class JavaTokenizer
{
	public JavaTokenizer() {}
	public JavaTokenizer(String filename, String sourcCode)
	{
		setFilename(filename);
		setSourceCode(sourcCode);
	}

	public String getFilename() { return filename; }
	public void setFilename(String filename) { this.filename = filename; }
	public String getSourceCode() { return sourceCode; }
	public void setSourceCode(String sourceCode)
	{
		this.sourceCode = sourceCode;
		this.sourceLength = sourceCode.length();
	}

	public List<JavaToken> tokenize()
	{
		List<JavaToken> result = new ArrayList<JavaToken>();
		
		sourceLines = new ArrayList<String>();
		sourceLines.add(getLine(sourceCode, currentIndex));
		JavaToken.setCurrentSourceLines(sourceLines);
		JavaToken.setCurrentRow(1);
		
		if(tokenTypeMap == null) initTokenMap();
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
				case '0':
					toAdd = handleZero();
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
						Logger.error(
								"illegal character: " +
										sourceCode.charAt(currentIndex),
								filename, JavaToken.getCurrentRow(), getCol(),
								sourceLines.get(JavaToken.getCurrentRow()-1));
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


	private String filename;
	private String sourceCode;
	private int sourceLength;
	private int currentIndex;
	private List<String> sourceLines;
	private int colStart = 0;

	private int getCol()
	{
		return currentIndex - colStart;
	}
	private String getLine(String source, int index)
	{
		int rhsR = source.indexOf("\r", index+1);
		int rhsN = source.indexOf("\n", index+1);
		int rhs = rhsR;
		if(rhs == -1 || rhsN < rhs) rhs = rhsN;
		if(rhs == -1)
		{
			rhs = source.length();
		}
		return source.substring(index, rhs);
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
	private boolean isFloatingNumeric(String source, int index, int radix)
	{
		if(isNumeric(source, index, radix)) return true;
		char current = source.charAt(index);
		return	current == '.' || current == 'e' || current == 'E';
	}

	private void handleLineBreak()
	{
		while(	currentIndex+1 < sourceCode.length() && 
				(sourceCode.charAt(currentIndex+1) == '\r' ||
				sourceCode.charAt(currentIndex+1) == '\n'))
		{
			currentIndex++;
		}
		sourceLines.add(getLine(sourceCode, currentIndex+1));
		colStart = currentIndex+1;
		JavaToken.incCurrentRow();
	}
	private JavaToken handleEqual()
	{
		if(	currentIndex >= sourceLength-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.EQUAL, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.EQUAL_EQUAL, getCol());
	}
	private JavaToken handleExclaim()
	{
		if(	currentIndex >= sourceLength-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.EXCLAIM, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.EXCLAIM_EQUAL, getCol());
	}
	private JavaToken handleStar()
	{
		if(	currentIndex >= sourceLength-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.STAR, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.STAR_EQUAL, getCol());
	}
	private JavaToken handlePercent()
	{
		if(	currentIndex >= sourceLength-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.PERCENT, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.PERCENT_EQUAL, getCol());
	}
	private JavaToken handleCarat()
	{
		if(	currentIndex >= sourceLength-1 ||
			sourceCode.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.CARAT, getCol());
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.CARAT_EQUAL, getCol());
	}
	private JavaToken handlePlus()
	{
		if(currentIndex >= sourceLength-1)
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
		if(currentIndex >= sourceLength-1)
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
		if(currentIndex >= sourceLength-1)
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
		if(currentIndex >= sourceLength-1)
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
		if(currentIndex >= sourceLength-1)
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
			if(	currentIndex >= sourceLength-1 ||
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
		if(currentIndex >= sourceLength-1)
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
			if(currentIndex >= sourceLength-1)
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
				if(	currentIndex >= sourceLength-1 ||
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
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.SLASH, getCol());
		}
		int nextChar = sourceCode.charAt(currentIndex+1);
		if(nextChar == '/')
		{
			while(currentIndex < sourceLength)
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
			while(currentIndex < sourceLength)
			{
				currentIndex++;
				if(sourceCode.charAt(currentIndex) == '*')
				{
					if(	currentIndex+1 < sourceLength &&
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
		if(currentIndex > sourceCode.length()-3)
		{
			Logger.error(
					"unclosed character literal",
					filename, JavaToken.getCurrentRow(), getCol(),
					sourceLines.get(JavaToken.getCurrentRow()-1));
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
						"unclosed character literal", 
						filename, JavaToken.getCurrentRow(), getCol(),
						sourceLines.get(JavaToken.getCurrentRow()-1));
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
					"unclosed character literal",
					filename, JavaToken.getCurrentRow(), getCol(),
					sourceLines.get(JavaToken.getCurrentRow()-1));
		}
		return new JavaToken(JavaTokenType.CHAR, text.toString(), getCol());
	}
	
	private JavaToken handleString()
	{
		int col = getCol();
		currentIndex++;
		if(currentIndex > sourceCode.length()-1)
		{
			Logger.error(
					"unclosed string literal",
					filename, JavaToken.getCurrentRow(), col,
					sourceLines.get(JavaToken.getCurrentRow()-1));
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
					"unclosed string literal",
					filename, JavaToken.getCurrentRow(), col,
					sourceLines.get(JavaToken.getCurrentRow()-1));
			currentIndex+= 3;
		}
		return new JavaToken(JavaTokenType.STRING, text.toString(), getCol());
	}
	
	private JavaToken handleIdentifier()
	{
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
		if(isNumeric(sourceCode, currentIndex+1, 10))
		{
			return handleNumber(10);
		}
		return new JavaToken(JavaTokenType.DOT, getCol());
	}
	private JavaToken handleZero()
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
						filename, JavaToken.getCurrentRow(), getCol(),
						sourceLines.get(JavaToken.getCurrentRow()-1));
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
						filename, JavaToken.getCurrentRow(), getCol(),
						sourceLines.get(JavaToken.getCurrentRow()-1));
				return null;
			}
			return handleNumber(16);
		}
		else if(isNumeric(sourceCode, currentIndex+1, 8))
		{
			currentIndex ++;
			return handleNumber(8);
		}
		return new JavaToken(JavaTokenType.INTEGER, "0", getCol());
	}
	private JavaToken handleNumber(int base)
	{
		StringBuffer result = new StringBuffer();
		boolean hasDot = false, hasE = false;
		char current = sourceCode.charAt(currentIndex);
		while(	isNumeric(sourceCode, currentIndex, base) ||
				(current == '.' || current == 'e' || current == 'E'))
		{
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
			current = sourceCode.charAt(currentIndex);
		}
		//if(current == 'd' || current == 'D')
		//if(current == 'l' || current == 'L' && )
		return new JavaToken(JavaTokenType.INTEGER, result.toString(), getCol());
	}
	private JavaToken handleNumeric()
	{
		int radix = 10;
		if(sourceCode.charAt(currentIndex) == '0')
		{
			radix = 8;
			if(currentIndex+1 < sourceCode.length())
			{
				char next = sourceCode.charAt(currentIndex+1);
				if(next == 'x' || next == 'X')
				{
					radix = 16;
					currentIndex+=2;
				}
			}
		}
		int endIndex = currentIndex;
		boolean isFloat = false;
		boolean isLong = false;
		boolean hasTypeExtension = false;
		while(	endIndex < sourceCode.length() &&
				isNumeric(sourceCode, endIndex, radix))
		{
			endIndex++;
		}
		while(	endIndex < sourceCode.length() &&
				isFloatingNumeric(sourceCode, endIndex, radix))
		{
			isFloat = true;
			isLong = true;
			endIndex++;
		}
		if(endIndex < sourceCode.length())
		{
			char lastChar = sourceCode.charAt(endIndex);
			if(lastChar == 'f' || lastChar == 'F')
			{
				isFloat = true;
				isLong = false;
				hasTypeExtension = true;
			}
			else if(lastChar == 'd' || lastChar == 'D')
			{
				isFloat = true;
				isLong = true;
				hasTypeExtension = true;
			}
			else if(lastChar == 'l' || lastChar == 'L')
			{
				if(isFloat == true)
				{
					// Throw error
				}
				isLong = true;
				hasTypeExtension = true;
			}
		}
		String numberString = sourceCode.substring(currentIndex, endIndex);
		currentIndex = endIndex;
		if(hasTypeExtension) currentIndex++;
		if(isFloat)
		{
			if(isLong)
			{
				double value = Double.parseDouble(numberString);
				return new JavaToken(
						JavaTokenType.DOUBLE, Double.toString(value), getCol());
			}
			else
			{
				float value = Float.parseFloat(numberString);
				return new JavaToken(
						JavaTokenType.FLOAT, Float.toString(value), getCol());
			}
		}
		else
		{
			if(isLong)
			{
				long value = Long.parseLong(numberString, radix);
				return new JavaToken(
						JavaTokenType.LONG, Long.toString(value), getCol());
			}
			else
			{
				int value = Integer.parseInt(numberString, radix);
				return new JavaToken(
						JavaTokenType.INTEGER,
						Integer.toString(value),
						getCol());
			}
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

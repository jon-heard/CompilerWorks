package com.jonheard.compilers.javaTokenizer;

import java.util.ArrayList;
import java.util.List;

public class JavaTokenizer
{
	public JavaTokenizer() {}
	public JavaTokenizer(String source)
	{
		setSource(source);
	}

	public String getSource()
	{
		return source;
	}

	public void setSource(String source)
	{
		this.source = source;
		this.sourceLength = source.length();
	}

	public List<JavaToken> tokenize()
	{
		List<JavaToken> result = new ArrayList<JavaToken>();
		currentIndex = 0;
		while(currentIndex < sourceLength)
		{
			JavaToken toAdd = null;
			switch(source.charAt(currentIndex))
			{
				case ' ':
				case '\t':
				case '\r':
				case '\n':
					break;
				case '~':
					toAdd = new JavaToken(JavaTokenType.TILDE);
					break;
				case ';':
					toAdd = new JavaToken(JavaTokenType.SEMICOLON);
					break;
				case '?':
					toAdd = new JavaToken(JavaTokenType.QUESTION);
					break;
				case ':':
					toAdd = new JavaToken(JavaTokenType.COLON);
					break;
				case '{':
					toAdd = new JavaToken(JavaTokenType.CURL_BRACE_LEFT);
					break;
				case '}':
					toAdd = new JavaToken(JavaTokenType.CURL_BRACE_RIGHT);
					break;
				case '[':
					toAdd = new JavaToken(JavaTokenType.SQUARE_BRACE_LEFT);
					break;
				case ']':
					toAdd = new JavaToken(JavaTokenType.SQUARE_BRACE_RIGHT);
					break;
				case '(':
					toAdd = new JavaToken(JavaTokenType.PAREN_LEFT);
					break;
				case ')':
					toAdd = new JavaToken(JavaTokenType.PAREN_RIGHT);
					break;
				case '\'':
					toAdd = handleChar();
					break;
				case '\"':
					toAdd = handleString();
					break;
				case '+':
					toAdd = handlePlus();
					break;
				case '-':
					toAdd = handleDash();
					break;
				case '!':
					toAdd = handleExclaim();
					break;
				case '*':
					toAdd = handleStar();
					break;
				case '/':
					toAdd = handleSlash();
					break;
				case '%':
					toAdd = handlePercent();
					break;
				case '<':
					toAdd = handleLeft();
					break;
				case '>':
					toAdd = handleRight();
					break;
				case '=':
					toAdd = handleEqual();
					break;
				case '&':
					toAdd = handleAnd();
					break;
				case '^':
					toAdd = handleCarat();
					break;
				case '|':
					toAdd = handlePipe();
					break;
				case '.':
					if(!isNumeric(source, currentIndex+1, 10))
					{
						toAdd = new JavaToken(JavaTokenType.DOT);
						break;
					}
				case '0': case '1': case '2': case '3': case '4':
				case '5': case '6': case '7': case '8': case '9':
					toAdd = handleNumeric();
					break;
				default:
					toAdd = handleIdentifier();
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

	private String source;
	private int sourceLength;
	private int currentIndex;

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

	private JavaToken handleEqual()
	{
		if(	currentIndex >= sourceLength-1 ||
			source.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.EQUAL);			
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.EQUAL_EQUAL);
	}
	private JavaToken handleExclaim()
	{
		if(	currentIndex >= sourceLength-1 ||
			source.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.EXCLAIM);			
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.EXCLAIM_EQUAL);
	}
	private JavaToken handleStar()
	{
		if(	currentIndex >= sourceLength-1 ||
			source.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.STAR);			
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.STAR_EQUAL);
	}
	private JavaToken handlePercent()
	{
		if(	currentIndex >= sourceLength-1 ||
			source.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.PERCENT);			
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.PERCENT_EQUAL);
	}
	private JavaToken handleCarat()
	{
		if(	currentIndex >= sourceLength-1 ||
			source.charAt(currentIndex+1) != '=')
		{
			return new JavaToken(JavaTokenType.CARAT);
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.CARAT_EQUAL);
	}
	private JavaToken handlePlus()
	{
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.PLUS);
		}
		int nextChar = source.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PLUS_EQUAL);
		}
		if(nextChar == '+')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PLUS_PLUS);
		}
		return new JavaToken(JavaTokenType.PLUS);
	}
	private JavaToken handleDash()
	{
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.DASH);
		}
		int nextChar = source.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.DASH_EQUAL);
		}
		if(nextChar == '-')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.DASH_DASH);
		}
		return new JavaToken(JavaTokenType.DASH);
	}
	private JavaToken handleAnd()
	{
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.AND);
		}
		int nextChar = source.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.AND_EQUAL);
		}
		if(nextChar == '&')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.AND_AND);
		}
		return new JavaToken(JavaTokenType.AND);
	}
	private JavaToken handlePipe()
	{
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.PIPE);
		}
		int nextChar = source.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PIPE_EQUAL);
		}
		if(nextChar == '|')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.PIPE_PIPE);
		}
		return new JavaToken(JavaTokenType.PIPE);
	}
	private JavaToken handleLeft()
	{
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.LEFT);
		}
		int nextChar = source.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.LEFT_EQUAL);
		}
		if(nextChar == '<')
		{
			currentIndex++;
			if(	currentIndex >= sourceLength-1 ||
					source.charAt(currentIndex+1) != '=')
			{
				return new JavaToken(JavaTokenType.LEFT_LEFT);			
			}
			currentIndex++;
			return new JavaToken(JavaTokenType.LEFT_LEFT_EQUAL);
		}
		return new JavaToken(JavaTokenType.LEFT);
	}
	private JavaToken handleRight()
	{
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.RIGHT);
		}
		int nextChar = source.charAt(currentIndex+1);
		if(nextChar == '=')
		{
			currentIndex++;
			return new JavaToken(JavaTokenType.RIGHT_EQUAL);
		}
		if(nextChar == '>')
		{
			currentIndex++;
			if(currentIndex >= sourceLength-1)
			{
				return new JavaToken(JavaTokenType.RIGHT_RIGHT);
			}
			nextChar = source.charAt(currentIndex+1);
			if(nextChar == '=')
			{
				currentIndex++;
				return new JavaToken(JavaTokenType.RIGHT_RIGHT_EQUAL);
			}
			if(nextChar == '>')
			{
				currentIndex++;
				if(	currentIndex >= sourceLength-1 ||
						source.charAt(currentIndex+1) != '=')
				{
					return new JavaToken(JavaTokenType.RIGHT_RIGHT_RIGHT);
				}
				currentIndex++;
				return new JavaToken(JavaTokenType.RIGHT_RIGHT_RIGHT_EQUAL);
			}
			return new JavaToken(JavaTokenType.RIGHT_RIGHT);
		}
		return new JavaToken(JavaTokenType.RIGHT);
	}
	private JavaToken handleSlash()
	{		
		if(currentIndex >= sourceLength-1)
		{
			return new JavaToken(JavaTokenType.SLASH);
		}
		int nextChar = source.charAt(currentIndex+1);
		if(nextChar == '/')
		{
			while(currentIndex < sourceLength)
			{
				currentIndex++;
				if(	source.charAt(currentIndex)=='\r' ||
					source.charAt(currentIndex)=='\n')
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
				if(source.charAt(currentIndex) == '*')
				{
					if(	currentIndex+1 < sourceLength &&
						source.charAt(currentIndex+1) == '/')
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
			return new JavaToken(JavaTokenType.SLASH_EQUAL);
		}
		return new JavaToken(JavaTokenType.SLASH);
	}
	
	private JavaToken handleChar()
	{
		currentIndex++;
		StringBuffer text = new StringBuffer();
		text.append(source.charAt(currentIndex));
		if(text.equals("\\"))
		{
			currentIndex++;
			text.append(source.charAt(currentIndex));
		}
		currentIndex++;
		return new JavaToken(JavaTokenType.CHAR, text.toString());
	}
	
	private JavaToken handleString()
	{
		currentIndex++;
		StringBuffer text = new StringBuffer();
		while(source.charAt(currentIndex) != '\"')
		{
			text.append(source.charAt(currentIndex));
			currentIndex++;
		}
		return new JavaToken(JavaTokenType.STRING, text.toString());
	}
	
	private JavaToken handleIdentifier()
	{
		JavaToken result = null;
		if(isAlpha(source, currentIndex))
		{
			result = JavaTokenType.GetNextToken(source, currentIndex);
			if(result != null)
			{
				currentIndex +=
						result.toString().length() - 1;
			}
			else
			{
				int identifierEnd = currentIndex+1;
				while(isAlphaNumeric(source, identifierEnd))
				{
					identifierEnd++;
				}
				result = new JavaToken(
					JavaTokenType.IDENTIFIER,
					source.substring(currentIndex, identifierEnd));
				currentIndex=identifierEnd-1;
			}
		}
		return result;
	}
	
	private JavaToken handleNumeric()
	{
		int radix = 10;
		if(source.charAt(currentIndex) == '0')
		{
			radix = 8;
			if(currentIndex+1 < source.length())
			{
				char next = source.charAt(currentIndex+1);
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
		while(	endIndex < source.length() &&
				isNumeric(source, endIndex, radix))
		{
			endIndex++;
		}
		while(	endIndex < source.length() &&
				isFloatingNumeric(source, endIndex, radix))
		{
			isFloat = true;
			isLong = true;
			endIndex++;
		}
		if(endIndex < source.length())
		{
			char lastChar = source.charAt(endIndex);
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
		String numberString = source.substring(currentIndex, endIndex);
		currentIndex = endIndex;
		if(hasTypeExtension) currentIndex++;
		if(isFloat)
		{
			if(isLong)
			{
				double value = Double.parseDouble(numberString);
				return new JavaToken(
						JavaTokenType.DOUBLE, Double.toString(value));
			}
			else
			{
				float value = Float.parseFloat(numberString);
				return new JavaToken(
						JavaTokenType.FLOAT, Float.toString(value));
			}
		}
		else
		{
			if(isLong)
			{
				long value = Long.parseLong(numberString, radix);
				return new JavaToken(JavaTokenType.LONG, Long.toString(value));
			}
			else
			{
				int value = Integer.parseInt(numberString, radix);
				return new JavaToken(
						JavaTokenType.INTEGER,
						Integer.toString(value));
			}
		}
	}
}

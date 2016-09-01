package com.jonheard.compilers.javaTokenizer;

import java.util.EnumSet;
import java.util.HashMap;

public class JavaToken
{
	private JavaTokenType type;
	private String text;
	
	public JavaToken(JavaTokenType type)
	{
		this.type = type;
		this.text = "";
	}
	public JavaToken(JavaTokenType type, String text)
	{
		this.type = type;
		this.text = text;
	}
	public JavaTokenType getType()
	{
		return type;
	}
	public String getText()
	{
		return text;
	}
	
	@Override
	public String toString()
	{
		if(text.equals(""))
		{
			return type.toString();
		}
		else
		{
			return type.toString() + ":" + text;
		}
	}
}

enum JavaTokenType
{
	IDENTIFIER,

	STRING,
	CHAR,
	INTEGER,
	LONG,
	FLOAT,
	DOUBLE,
	
	PLUS_PLUS,
	DASH_DASH,
	PLUS,
	DASH,
	TILDE,
	EXCLAIM,
	STAR,
	SLASH,
	PERCENT,
	LEFT_LEFT,
	RIGHT_RIGHT,
	RIGHT_RIGHT_RIGHT,
	LEFT,
	RIGHT,
	LEFT_EQUAL,
	RIGHT_EQUAL,
	EQUAL_EQUAL,
	EXCLAIM_EQUAL,
	AND,
	CARAT,
	PIPE,
	AND_AND,
	PIPE_PIPE,
	QUESTION,
	COLON,
	EQUAL,
	PLUS_EQUAL,
	DASH_EQUAL,
	STAR_EQUAL,
	SLASH_EQUAL,
	PERCENT_EQUAL,
	AND_EQUAL,
	CARAT_EQUAL,
	PIPE_EQUAL,
	LEFT_LEFT_EQUAL,
	RIGHT_RIGHT_EQUAL,
	RIGHT_RIGHT_RIGHT_EQUAL,
	DOT,
	SEMICOLON,
	CURL_BRACE_LEFT,
	CURL_BRACE_RIGHT,
	SQUARE_BRACE_LEFT,
	SQUARE_BRACE_RIGHT,
	PAREN_LEFT,
	PAREN_RIGHT,

	_ABSTRACT,
	_ASSERT,
	_BOOLEAN,
	_BREAK,
	_BYTE,
	_CASE,
	_CATCH,
	_CHAR,
	_CLASS,
	_CONST,
	_CONTINUE,
	_DEFAULT,
	_DO,
	_DOUBLE,
	_ELSE,
	_ENUM,
	_EXTENDS,
	_FALSE,
	_FINAL,
	_FINALLY,
	_FLOAT,
	_FOR,
	_GOTO,
	_IF,
	_IMPLEMENTS,
	_IMPORT,
	_INSTANCEOF,
	_INT,
	_INTERFACE,
	_LONG,
	_NATIVE,
	_NEW,
	_PACKAGE,
	_PRIVATE,
	_PROTECTED,
	_PUBLIC,
	_RETURN,
	_SHORT,
	_STATIC,
	_STRICTFP,
	_SUPER,
	_SWITCH,
	_SYNCHRONIZED,
	_THIS,
	_THROW,
	_THROWS,
	_TRANSIENT,
	_TRUE,
	_TRY,
	_VOID,
	_VOLATILE,
	_WHILE;
	
	@Override
	public String toString()
	{
		String result = isIdentifier() ? this.name().substring(1) : this.name();
		return result.toLowerCase();
	}
	
	public boolean isIdentifier()
	{
		return this.name().charAt(0) == '_';
	}
	
	@SuppressWarnings("unchecked")
	public static JavaToken GetNextToken(String src, int startIndex)
	{
		if(startIndex > src.length())
		{
			return null;
		}
		if(JavaTokenType.tokenMap == null)
		{
			initTokenMap();
		}
		char currentChar = src.charAt(startIndex);
		HashMap<Character, Object> currentMap = tokenMap;
		while(currentMap.containsKey(currentChar))
		{
			currentMap =
					(HashMap<Character, Object>)currentMap.get(currentChar);
			startIndex++;
			if(startIndex >= src.length())
			{
				break;
			}
			currentChar = src.charAt(startIndex);
		}
		if(currentMap.containsKey('_'))
		{
			JavaTokenType type = (JavaTokenType)currentMap.get('_');
			return new JavaToken(type);
		}
		return null;
	}

	private static HashMap<Character, Object> tokenMap = null;
	
	@SuppressWarnings("unchecked")
	private static void initTokenMap()
	{
		tokenMap = new HashMap<Character, Object>();
		for (JavaTokenType i : EnumSet.allOf(JavaTokenType.class))
        {
			String name = i.toString();
			if(i.isIdentifier())
			{
				HashMap<Character, Object> currentMap = tokenMap;
				for(int j = 0; j < name.length(); j++)
				{
					if(!currentMap.containsKey(name.charAt(j)))
					{
						currentMap.put(
								name.charAt(j),
								new HashMap<Character, Object>());
					}
					currentMap = (HashMap<Character, Object>)
							currentMap.get(name.charAt(j));
				}
				currentMap.put('_', i);
			}
        }
	}
}

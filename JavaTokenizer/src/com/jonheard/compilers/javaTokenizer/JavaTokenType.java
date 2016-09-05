package com.jonheard.compilers.javaTokenizer;

public enum JavaTokenType
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
	COMMA,
	CURL_BRACE_LEFT,
	CURL_BRACE_RIGHT,
	SQUARE_BRACE_LEFT,
	SQUARE_BRACE_RIGHT,
	PAREN_LEFT,
	PAREN_RIGHT,

	_ABSTRACT,
	_ASSERT,
	_BREAK,
	_CASE,
	_CATCH,
	_CLASS,
	_CONST,
	_CONTINUE,
	_DEFAULT,
	_DO,
	_ELSE,
	_ENUM,
	_EXTENDS,
	_FALSE,
	_FINAL,
	_FINALLY,
	_FOR,
	_GOTO,
	_IF,
	_IMPLEMENTS,
	_IMPORT,
	_INSTANCEOF,
	_INTERFACE,
	_NATIVE,
	_NEW,
	_PACKAGE,
	_PRIVATE,
	_PROTECTED,
	_PUBLIC,
	_RETURN,
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
}

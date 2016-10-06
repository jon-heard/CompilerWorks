package com.jonheard.compilers.tokenizer_java;

public class Token
{
	public Token(TokenType type, int col)
	{
		this.type = type;
		this.text = "";
		this.column = col;
		this.line = currentLine;
	}
	public Token(TokenType type, int col, String text)
	{
		this.type = type;
		this.column = col;
		this.text = text;
		this.line = currentLine;
	}
	public TokenType getTokenType()
	{
		return type;
	}
	public String getText()
	{
		return text;
	}
	public int getLine() { return line; }
	public int getColumn() { return column; }
	
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
	
	@Override
	public boolean equals(Object rhs)
	{
		boolean result = false;
		if(rhs instanceof Token)
		{
			Token rhsToken = (Token)rhs;
			result = (rhsToken.type.equals(type) && rhsToken.text.equals(text));
		}
		return result;
	}
	
	
	public static int getCurrentLine() { return currentLine; }
	public static void setCurrentLine(int line) { Token.currentLine = line; }
	public static void incCurrentLine() { Token.currentLine++; }

	private TokenType type;
	private String text;
	private int line, column;
	
	private static int currentLine;
}

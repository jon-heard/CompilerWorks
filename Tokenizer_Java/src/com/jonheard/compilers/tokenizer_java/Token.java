package com.jonheard.compilers.tokenizer_java;

public class Token
{
	public Token(TokenType type, int col)
	{
		this.type = type;
		this.text = "";
		this.column = col;
		this.row = currentRow;
	}
	public Token(TokenType type, int col, String text)
	{
		this.type = type;
		this.column = col;
		this.text = text;
		this.row = currentRow;
	}
	public TokenType getType()
	{
		return type;
	}
	public String getText()
	{
		return text;
	}
	public int getRow() { return row; }
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
	
	
	public static int getCurrentRow() { return currentRow; }
	public static void setCurrentRow(int row) { Token.currentRow = row; }
	public static void incCurrentRow() { Token.currentRow++; }

	private TokenType type;
	private String text;
	private int row, column;
	
	private static int currentRow;
}

package com.jonheard.compilers.tokenizer_java;

public class Token
{
	public Token(TokenType type, int col)
	{
		this.type = type;
		this.text = "";
		this.col = col;
		this.row = currentRow;
		this.sourceFileInfo = currentSourceFileInfo;
	}
	public Token(TokenType type, String text, int col)
	{
		this.type = type;
		this.text = text;
		this.col = col;
		this.row = currentRow;
		this.sourceFileInfo = currentSourceFileInfo;
	}
	public TokenType getType()
	{
		return type;
	}
	public String getText()
	{
		return text;
	}
	public String getFilename() { return sourceFileInfo.getFilename(); }
	public int getRow() { return row; }
	public int getCol() { return col; }
	public String getLine() { return sourceFileInfo.getLine(row-1); }
	public SourceFileInfo getSourceFileInfo() { return sourceFileInfo; }
	
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
	
	
	public static int getCurrentRow() { return currentRow; }
	public static void setCurrentRow(int row) { Token.currentRow = row; }
	public static void incCurrentRow() { Token.currentRow++; }
	public static void setCurrentSourceFileInfo(SourceFileInfo value)
	{
		Token.currentSourceFileInfo = value;
	}

	private TokenType type;
	private String text;
	private int row, col;
	private SourceFileInfo sourceFileInfo;
	
	private static int currentRow;
	private static SourceFileInfo currentSourceFileInfo;
}

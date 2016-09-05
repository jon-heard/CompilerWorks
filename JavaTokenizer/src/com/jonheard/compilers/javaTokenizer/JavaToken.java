package com.jonheard.compilers.javaTokenizer;

import java.util.List;

public class JavaToken
{
	public JavaToken(JavaTokenType type, int col)
	{
		this.type = type;
		this.text = "";
		this.col = col;
		this.row = currentRow;
		this.sourceLines = currentSourceLines;
	}
	public JavaToken(JavaTokenType type, String text, int col)
	{
		this.type = type;
		this.text = text;
		this.col = col;
		this.row = currentRow;
		this.sourceLines = currentSourceLines;
	}
	public JavaTokenType getType()
	{
		return type;
	}
	public String getText()
	{
		return text;
	}
	public int getRow() { return row; }
	public int getCol() { return col; }
	public String getLine() { return sourceLines.get(row-1); }
	
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
	public static void setCurrentRow(int row) { JavaToken.currentRow = row; }
	public static void incCurrentRow() { JavaToken.currentRow++; }
	public static void setCurrentSourceLines(List<String> currentSourceLines)
	{
		JavaToken.currentSourceLines = currentSourceLines;
	}

	private JavaTokenType type;
	private String text;
	private int row, col;
	private List<String> sourceLines;
	
	private static int currentRow;
	private static List<String> currentSourceLines;
}

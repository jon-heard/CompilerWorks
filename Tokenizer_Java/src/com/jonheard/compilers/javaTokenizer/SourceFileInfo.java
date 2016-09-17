package com.jonheard.compilers.javaTokenizer;

public class SourceFileInfo
{
	public SourceFileInfo(String filename, String sourceCode)
	{
		this.filename = filename;
		this.sourceCode = sourceCode;
		lines = sourceCode.replace('\t', ' ').split("[\\r\\n]");
	}
	
	public String getFilename() { return filename; }
	public String getSourcecode() { return sourceCode; }
	public String getLine(int index) { return lines[index]; } 
	
	private String filename;
	private String[] lines;
	private String sourceCode;
}

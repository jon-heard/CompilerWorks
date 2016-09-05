package com.jonheard.util;

import java.io.PrintStream;

public class Logger
{
	public static void log(String msg)
	{
		out.print(msg + "\n");
	}
	public static void error(
			String msg, String filename, int row, int col, String line)
	{
		String columnSpace = new String(new char[col]).replace('\0', ' ');
		log(	filename + ":" + row + ": error: " + msg + "\n\t" + 
				line + "\n\t" + columnSpace + "^");
		errorCount++;
	}
	public static void warn(
			String msg, String filename, int row, int col, String line)
	{
		String columnSpace = new String(new char[col]).replace('\0', ' ');
		log(	filename + ":" + row + ": warning: " + msg + "\n\t" + 
				line + "\n\t" + columnSpace + "^");
		warningCount++;
	}

	public static void setPrintStream(PrintStream out)
	{
		Logger.out = out;
	}
	public static int getErrorCount() { return errorCount; }
	public static int getWarningCount() { return warningCount; }

	private static PrintStream out = System.out; 
	private static int errorCount = 0;
	private static int warningCount = 0;
}

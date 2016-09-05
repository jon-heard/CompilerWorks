package com.jonheard.util;

import java.io.PrintStream;

public class Logger
{
	public static final String ERROR_MSG = "%s:%d: error: %s\n\t%s\n\t%s^";
	public static final String WARN_MSG =  "%s:%d: warning: %s\n\t%s\n\t%s^";

	public static void log(String msg)
	{
		out.print(msg + "\n");
	}
	public static void error(
			String msg, String filename, int row, int col, String line)
	{
		String columnSpace = new String(new char[col]).replace('\0', ' ');
		out.printf(ERROR_MSG + '\n', filename, row, msg, line, columnSpace);
		errorCount++;
	}
	public static void warn(
			String msg, String filename, int row, int col, String line)
	{
		String columnSpace = new String(new char[col]).replace('\0', ' ');
		out.printf(WARN_MSG + '\n', filename, row, msg, line, columnSpace);
		warnCount++;
	}

	public static void setPrintStream(PrintStream out)
	{
		Logger.out = out;
	}
	public static void resetCounts() { errorCount = warnCount = 0; }

	public static int getErrorCount() { return errorCount; }
	public static int getWarnCount() { return warnCount; }
	

	private static PrintStream out = System.out; 
	private static int errorCount = 0;
	private static int warnCount = 0;
}

package com.jonheard.util;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Logger
{
	public static final String ERROR_MSG = "%s:%d: error: %s\n\t%s\n\t%s^";
	public static final String WARN_MSG =  "%s:%d: warning: %s\n\t%s\n\t%s^";

	public static String getLogs()
	{
		return new String(buffer.toByteArray(), StandardCharsets.UTF_8);
	}
	public static void clearLogs()
	{
		buffer.reset();
	}
	
	public static void resetCounts() { errorCount = warnCount = 0; }
	public static int getErrorCount() { return errorCount; }
	public static int getWarnCount() { return warnCount; }

	public static void log(String msg)
	{
		bufferStream.print(msg + "\n");
	}
	public static void error(
			String msg, String filename, int row, int col, String line)
	{
		String columnSpace = new String(new char[col]).replace('\0', ' ');
		bufferStream.printf(
				ERROR_MSG + '\n', filename, row, msg, line, columnSpace);
		errorCount++;
	}
	public static void warn(
			String msg, String filename, int row, int col, String line)
	{
		String columnSpace = new String(new char[col]).replace('\0', ' ');
		bufferStream.printf(
				WARN_MSG + '\n', filename, row, msg, line, columnSpace);
		warnCount++;
	}

	private static ByteArrayOutputStream buffer; 
	private static PrintStream bufferStream; 
	private static int errorCount = 0;
	private static int warnCount = 0;
	
	static
	{
		buffer = new ByteArrayOutputStream();
		bufferStream = new PrintStream(buffer);
	}
}

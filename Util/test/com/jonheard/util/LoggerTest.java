package com.jonheard.util;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.Test;

public class LoggerTest
{
	@Test
	public void loggingAndPrintStreamSetting()
	{
		Logger.clearLogs();
		Logger.log("Hello");
		Logger.log("World");
		String expected =
				"Hello\nWorld\n";
		assertEquals(expected, Logger.getLogs());
	}
	
	@Test
	public void error()
	{
		Logger.clearLogs();
		Logger.resetCounts();
		assertEquals(0, Logger.getErrorCount());
		assertEquals(0, Logger.getWarnCount());
		Logger.error("Msg test", "file1", 35, 3, "1234567890");
		String expected = String.format(
				Logger.ERROR_MSG, "file1", 35, "Msg test", "1234567890", "   ")
				+ '\n';
		assertEquals(expected, Logger.getLogs());
		assertEquals(1, Logger.getErrorCount());
		assertEquals(0, Logger.getWarnCount());
	}
	
	@Test
	public void warning()
	{
		Logger.clearLogs();
		Logger.resetCounts();
		assertEquals(0, Logger.getErrorCount());
		assertEquals(0, Logger.getWarnCount());
		Logger.warn("Msg test", "file1", 35, 3, "1234567890");
		String expected = String.format(
				Logger.WARN_MSG, "file1", 35, "Msg test", "1234567890", "   ")
				+ '\n';
		assertEquals(expected, Logger.getLogs());
		assertEquals(0, Logger.getErrorCount());
		assertEquals(1, Logger.getWarnCount());
	}
}

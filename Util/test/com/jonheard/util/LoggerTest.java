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
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		Logger.setPrintStream(new PrintStream(buf));
		Logger.log("Hello");
		Logger.log("World");
		String expected =
				"Hello\nWorld\n";
		String actual = new String(buf.toByteArray(), StandardCharsets.UTF_8);
		assertEquals(expected, actual);
	}
	
	@Test
	public void error()
	{
		Logger.resetCounts();
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		assertEquals(0, Logger.getErrorCount());
		assertEquals(0, Logger.getWarnCount());
		Logger.setPrintStream(new PrintStream(buf));
		Logger.error("Msg test", "file1", 35, 3, "1234567890");
		String expected = String.format(
				Logger.ERROR_MSG, "file1", 35, "Msg test", "1234567890", "   ")
				+ '\n';
		String actual = new String(buf.toByteArray(), StandardCharsets.UTF_8);
		assertEquals(expected, actual);
		assertEquals(1, Logger.getErrorCount());
		assertEquals(0, Logger.getWarnCount());
	}
	
	@Test
	public void warning()
	{
		Logger.resetCounts();
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		assertEquals(0, Logger.getErrorCount());
		assertEquals(0, Logger.getWarnCount());
		Logger.setPrintStream(new PrintStream(buf));
		Logger.warn("Msg test", "file1", 35, 3, "1234567890");
		String expected = String.format(
				Logger.WARN_MSG, "file1", 35, "Msg test", "1234567890", "   ")
				+ '\n';
		String actual = new String(buf.toByteArray(), StandardCharsets.UTF_8);
		assertEquals(expected, actual);
		assertEquals(0, Logger.getErrorCount());
		assertEquals(1, Logger.getWarnCount());
	}
}

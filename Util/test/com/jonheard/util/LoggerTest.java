package com.jonheard.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoggerTest {
  public LoggerTest() {
    Logger.setPrintingToConsole(false);
  }

  @Test
  public void loggingAndPrintStreamSetting() {
    Logger.clearLogs();
    Logger.log("Hello");
    Logger.log("World");
    String expected = "Hello\nWorld\n";
    assertEquals(expected, Logger.getLogs());
    Logger.clearLogs();
    assertEquals("", Logger.getLogs());
  }

  @Test
  public void basicErrors() {
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    Logger.error("Msg test", "file1", 35, 3, "1234567890");
    String expected = String.format(Logger.ERROR_MSG, "file1", 36, "Msg test", "1234567890", "   ")
        + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(1, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
  }

  @Test
  public void basicWarnings() {
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    Logger.warning("Msg test", "file1", 35, 3, "1234567890");
    String expected= String.format(Logger.WARNING_MSG, "file1", 36, "Msg test", "1234567890", "   ")
        + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(0, Logger.getErrorCount());
    assertEquals(1, Logger.getWarningCount());
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
  }
  
  @Test
  public void SourceCodeErrors() {
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    SourceFile file = new SourceFile("file1", "abc\ndef\nghi");
    Logger.error("Msg test", file, 5);
    String expected = String.format(Logger.ERROR_MSG, "file1", 2, "Msg test", "def", " ")
        + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(1, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
  }

  @Test
  public void SourceCodeWarnings() {
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    SourceFile file = new SourceFile("file1", "abc\ndef\nghi");
    Logger.warning("Msg test", file, 5);
    String expected = String.format(Logger.WARNING_MSG, "file1", 2, "Msg test", "def", " ")
        + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(0, Logger.getErrorCount());
    assertEquals(1, Logger.getWarningCount());
  }
}

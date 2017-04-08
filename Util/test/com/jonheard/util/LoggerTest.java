package com.jonheard.util;

import static org.junit.Assert.*;

import org.junit.Test;

class LogAccessor {
  public String getErrorFormat() { return Logger.ERROR_FORMAT; }
  public String getWarningFormat() { return Logger.WARNING_FORMAT; }
};

public class LoggerTest {

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
    LogAccessor logAccess = new LogAccessor();    
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    Logger.error("Msg test", "file1", 35, 3, "1234567890");
    String expected = String.format(
        logAccess.getErrorFormat(), "file1", 36, "Msg test", "1234567890", "   ") + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(1, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
  }

  @Test
  public void basicWarnings() {
    LogAccessor logAccess = new LogAccessor();    
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    Logger.warning("Msg test", "file1", 35, 3, "1234567890");
    String expected= String.format(
        logAccess.getWarningFormat(), "file1", 36, "Msg test", "1234567890", "   ") + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(0, Logger.getErrorCount());
    assertEquals(1, Logger.getWarningCount());
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
  }
  
  @Test
  public void SourceCodeErrors() {
    LogAccessor logAccess = new LogAccessor();    
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    SourceFile file = new SourceFile("file1", "abc\ndef\nghi");
    Logger.error("Msg test", file, 5);
    String expected = String.format(
        logAccess.getErrorFormat(), "file1", 2, "Msg test", "def", " ") + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(1, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
  }

  @Test
  public void SourceCodeWarnings() {
    LogAccessor logAccess = new LogAccessor();    
    Logger.clearLogs();
    Logger.resetCounts();
    assertEquals(0, Logger.getErrorCount());
    assertEquals(0, Logger.getWarningCount());
    SourceFile file = new SourceFile("file1", "abc\ndef\nghi");
    Logger.warning("Msg test", file, 5);
    String expected = String.format(
        logAccess.getWarningFormat(), "file1", 2, "Msg test", "def", " ") + '\n';
    assertEquals(expected, Logger.getLogs());
    assertEquals(0, Logger.getErrorCount());
    assertEquals(1, Logger.getWarningCount());
  }
}

package com.jonheard.util;

import java.awt.Point;

// Logger - Handles logging of messages, warnings and errors.  Buffers logs for later retrieval
// and can be set to print them to the console as well.  Also counts the number of warnings and
// errors that have been logged.  
public class Logger {
  public static final String ERROR_MSG = "%s:%d: error: %s\n\t%s\n\t%s^";
  public static final String WARNING_MSG = "%s:%d: warning: %s\n\t%s\n\t%s^";

  // Log buffering
  public static String getLogs() { return buffer.toString(); }
  public static void clearLogs() { buffer.setLength(0); }

  // Error and warning counts
  public static int getErrorCount() { return errorCount; }
  public static int getWarningCount() { return warningCount; }
  public static void resetCounts() { errorCount = warningCount = 0; }

  // Console printing
  public static boolean getIsPrintingToConsole() { return isPrintingToConsole; }
  public static void setPrintingToConsole(boolean value) { isPrintingToConsole = value; }

  // Basic log
  public static void log(String msg) {
    buffer.append(msg + "\n");
    if (isPrintingToConsole) {
      System.out.print(msg + "\n");
    }
  }

  // Error log
  public static void error(String msg, SourceFile source, int sourceIndex) {
    Point position = source.getCharPosition(sourceIndex);
    if (position == null) { position = source.getCharPosition(source.getLength()-1); }
    error(msg, source.getFilename(), position.y, position.x, source.getLine(position.y));
  }
  public static void error(String msg, String filename, int row, int col, String lineText) {
    // setup space before error position (for pointing to error)
    String columnSpace = new String(new char[col]).replace('\0', ' ');
    String toLog = String.format(ERROR_MSG, filename, row+1, msg, lineText, columnSpace);
    log(toLog);
    errorCount++;
  }

  // Warning log
  public static void warning(String msg, SourceFile source, int sourceIndex) {
    Point position = source.getCharPosition(sourceIndex);
    warning(msg, source.getFilename(), position.y, position.x, source.getLine(position.y));
  }
  public static void warning(String msg, String filename, int row, int col, String lineText) {
    // setup space before warning position (for pointing to warning)
    String columnSpace = new String(new char[col]).replace('\0', ' ');
    String toLog = String.format(WARNING_MSG, filename, row+1, msg, lineText, columnSpace);
    log(toLog);
    warningCount++;
  }

  private static StringBuilder buffer = new StringBuilder();
  private static boolean isPrintingToConsole = false;
  private static int errorCount = 0;
  private static int warningCount = 0;
}

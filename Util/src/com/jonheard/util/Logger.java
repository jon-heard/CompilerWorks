package com.jonheard.util;

import java.awt.Point;

// Logger - Handles logging of messages, warnings and errors.  Buffers logs for later retrieval
// and can be set to print them to the console as well.  Also counts the number of warnings and
// errors that have been logged.  
public class Logger {
  // Log buffering
  public static String getLogs() { return logs.toString(); }
  public static void clearLogs() { logs.setLength(0); }

  // Error and warning counts
  public static int getErrorCount() { return errorCount; }
  public static int getWarningCount() { return warningCount; }
  public static void resetCounts() { errorCount = warningCount = 0; }

  // Console printing
  public static boolean getIsPrintingToConsole() { return isPrintingToConsole; }
  public static void setPrintingToConsole(boolean value) { isPrintingToConsole = value; }

  // Basic log
  public static void log(String msg) {
    // bad input check
    if (msg == null) { throw new IllegalArgumentException("Arg1(msg): null"); }

    logs.append(msg + "\n");
    if (isPrintingToConsole) {
      System.out.print(msg + "\n");
    }
  }

  // Error log
  public static void error(String msg, SourceFile source, int index) {
    // bad input check
    if (msg == null) { throw new IllegalArgumentException("Arg1(msg): null"); }
    if (source == null) { throw new IllegalArgumentException("Arg2(source): null"); }
    if (index < 0) { throw new IllegalArgumentException("Arg3(index): < 0"); }
    if (index >= source.getLength()) {
      throw new IllegalArgumentException("Arg3(index): > source.getLength()");
    }

    Point position = source.getCharPosition(index);
    error(msg, source, position.y, position.x);
  }
  public static void error(String msg, SourceFile source, int row, int column) {
    // bad input check
    if (msg == null) { throw new IllegalArgumentException("Arg1(msg): null"); }
    if (source == null) { throw new IllegalArgumentException("Arg2(source): null"); }
    if (row < 0) { throw new IllegalArgumentException("Arg3(row): < 0"); }
    if (column < 0) { throw new IllegalArgumentException("Arg4(column): < 0"); }
    if (row > source.getRowCount()) {
      throw new IllegalArgumentException("Arg3(row): > source.getRowCount()");
    }

    error(msg, source.getFilename(), row, column, source.getRowText(row));
  }
  public static void error(String msg, String filename, int row, int column, String rowText) {
    // bad input check
    if (msg == null) { throw new IllegalArgumentException("Arg1(msg): null"); }
    if (filename == null) { throw new IllegalArgumentException("Arg2(filename): null"); }
    if (row < 0) { throw new IllegalArgumentException("Arg3(row): < 0"); }
    if (column < 0) { throw new IllegalArgumentException("Arg4(column): < 0"); }
    if (rowText == null) { throw new IllegalArgumentException("Arg5(rowText): null"); }

    // setup space before error position (for pointing to error)
    String columnSpace = new String(new char[column]).replace('\0', ' ');
    String toLog = String.format(ERROR_FORMAT, filename, row+1, msg, rowText, columnSpace);
    log(toLog);
    errorCount++;
  }

  // Warning log
  public static void warning(String msg, SourceFile source, int index) {
    // bad input check
    if (msg == null) { throw new IllegalArgumentException("Arg1(msg): null"); }
    if (source == null) { throw new IllegalArgumentException("Arg2(source): null"); }
    if (index < 0) { throw new IllegalArgumentException("Arg3(index): < 0"); }
    if (index >= source.getLength()) {
      throw new IllegalArgumentException("Arg3(index): > source.getLength()");
    }

    Point position = source.getCharPosition(index);
    warning(msg, source, position.y, position.x);
  }
  public static void warning(String msg, SourceFile source, int row, int column) {
    // bad input check
    if (msg == null) { throw new IllegalArgumentException("Arg1(msg): null"); }
    if (source == null) { throw new IllegalArgumentException("Arg2(source): null"); }
    if (row < 0) { throw new IllegalArgumentException("Arg3(row): < 0"); }
    if (column < 0) { throw new IllegalArgumentException("Arg4(column): < 0"); }
    if (row > source.getRowCount()) {
      throw new IllegalArgumentException("Arg3(row): > source.getRowCount()");
    }

    warning(msg, source.getFilename(), row, column, source.getRowText(row));
  }
  public static void warning(String msg, String filename, int row, int column, String rowText) {
    // bad input check
    if (msg == null) { throw new IllegalArgumentException("Arg1(msg): null"); }
    if (filename == null) { throw new IllegalArgumentException("Arg2(filename): null"); }
    if (row < 0) { throw new IllegalArgumentException("Arg3(row): < 0"); }
    if (column < 0) { throw new IllegalArgumentException("Arg4(column): < 0"); }
    if (rowText == null) { throw new IllegalArgumentException("Arg5(rowText): null"); }

    // setup space before warning position (for pointing to warning)
    String columnSpace = new String(new char[column]).replace('\0', ' ');
    String toLog = String.format(WARNING_FORMAT, filename, row+1, msg, rowText, columnSpace);
    log(toLog);
    warningCount++;
  }

  protected static final String ERROR_FORMAT = "%s:%d: error: %s\n\t%s\n\t%s^";
  protected static final String WARNING_FORMAT = "%s:%d: warning: %s\n\t%s\n\t%s^";

  private static StringBuilder logs = new StringBuilder();
  private static boolean isPrintingToConsole = false;
  private static int errorCount = 0;
  private static int warningCount = 0;
}

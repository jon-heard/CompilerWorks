package com.jonheard.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// SourceFile - Represents a source file and provides convenient functionality for it
public class SourceFile {
  // Constructor
  public SourceFile(String filename, String sourceCode) {
    // bad input check
    if (filename == null) { throw new IllegalArgumentException("Arg1(filename): null"); }
    if (sourceCode == null) { throw new IllegalArgumentException("Arg1(sourceCode): null"); }

    this.filename = filename;
    sourceCode = sourceCode.replace("\r", "");
    this.sourceCode = sourceCode;
    rows = initRowRangesForString(sourceCode);
    length = sourceCode.length();
  }
  
  // Get a character from source code
  public char getChar(int index) {
    // Allow out of bounds requests (with 0 value) to simplify eof checks
    if (index < 0 || index > sourceCode.length()-1) { return 0; }
    return sourceCode.charAt(index);
  }
  
  // Allows getting a substring from the source code
  public String getSubstring(int startIndex, int endIndex) {
    // bad input check
    if (startIndex < 0) { throw new IllegalArgumentException("Arg1(startIndex): < 0"); }
    if (endIndex < 0) { throw new IllegalArgumentException("Arg2(endIndex): < 0"); }
    if (startIndex > endIndex) {
      throw new IllegalArgumentException("Arg1(startIndex), Arg2(endIndex): startIndex > endIndex");
    }
    if (endIndex > sourceCode.length()) {
      throw new IllegalArgumentException("Arg2(endIndex): > sourceCode.length()");
    }

    return (new Range(startIndex, endIndex)).getSubstring(sourceCode);
  }
  
  // Get the row and column for the given index.  Given as a point (x=column, y=row)
  public Point getCharPosition(int index) {
    // Allow out of bounds requests (with null value) to simplify eof checks
    if (index < 0 || index > sourceCode.length()-1) { return null; }
    if (index == 0) { return new Point(0,0); }
    for (int i = 0; i < rows.length; ++i) {
      if (rows[i].start > index) {
        return new Point(index - rows[i-1].start, i-1);
      }
    }
    int last = rows.length - 1;
    return new Point(index - rows[last].start, last);
  }
  
  // Get the character index from the given row and column
  public int getIndex(int row, int column) {
    // bad input check
    if (row < 0) { throw new IllegalArgumentException("Arg1(row): < 0"); }
    if (column < 0) { throw new IllegalArgumentException("Arg2(column): < 0"); }
    if (row >= getRowCount()) {
      throw new IllegalArgumentException("Arg1(row): > getRowCount()");
    }

    return rows[row].start + column;
  }

  // Accessors
  public String getFilename() { return filename; }
  public String getSourceCode() { return sourceCode; }
  public int getRowCount() { return rows.length; }
  public String getRowText(int index) { return rows[index].getSubstring(sourceCode); }
  public int getLength() { return length; }


  // Variables
  private String filename;
  private Range[] rows;
  private String sourceCode;
  private int length;
  
  // initLines - Create a list of ranges that segment the given string into lines
  private Range[] initRowRangesForString(String sourceCode) {
    List<Range> result = new ArrayList<>();
    int start = 0;
    int end = sourceCode.indexOf("\n");
    while(end != -1) {
      result.add(new Range(start, end));
      start = end + 1;
      end = sourceCode.indexOf("\n", start);
    }
    result.add(new Range(start, sourceCode.length()));
    return result.toArray(new Range[0]);
  }

  // Helper classes
  private class Range {
    public int start;
    public int end;
    
    public Range(int start, int end) {
      if (start < 0) { throw new IllegalArgumentException("Range can't start negative"); }
      if (end < start) { throw new IllegalArgumentException("Start must come before end"); }
      this.start = start;
      this.end = end;
    }
    
    public String getSubstring(String sourceString) {
      if (sourceString == null) { return ""; }
      return sourceString.substring(start, end);
    }
  }
}

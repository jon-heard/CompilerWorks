package com.jonheard.util;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

// SourceFile - Represents a source file and provides convenient functionality for it
public class SourceFile {
  // Constructor
  public SourceFile(String filename, String sourceCode) {
    if (filename == null || sourceCode == null) { throw new NullPointerException(); }
    this.filename = filename;
    sourceCode = sourceCode.replace("\r", "");
    this.sourceCode = sourceCode;
    lines = initLineRangesForString(sourceCode);
    length = sourceCode.length();
  }
  
  // Get a character from source code
  public char getChar(int index) {
    if (index < 0 || index > sourceCode.length()-1) { return 0; }
    return sourceCode.charAt(index);
  }
  
  // Allows getting a substring from the source code
  public String getSubstring(int start, int end) {
    return (new Range(start, end)).getSubstring(sourceCode);
  }
  
  // Get the row and column for the given index.  Given as a point (x=column, y=row)
  public Point getCharPosition(int index) {
    if (index < 0 || index > sourceCode.length()-1) { return null; }
    if (index == 0) { return new Point(0,0); }
    for (int i = 0; i < lines.length; ++i) {
      if (lines[i].start > index) {
        return new Point(index - lines[i-1].start, i-1);
      }
    }
    int last = lines.length - 1;
    return new Point(index - lines[last].start, last);
  }
  
  // Get the character index from the given row and column
  public int getIndex(int row, int column) {
    return lines[row].start + column;
  }

  // Accessors
  public String getFilename() { return filename; }
  public String getSourceCode() { return sourceCode; }
  public int getLineCount() { return lines.length; }
  public String getLine(int index) { return lines[index].getSubstring(sourceCode); }
  public int getLength() { return length; }


  // Variables
  private String filename;
  private Range[] lines;
  private String sourceCode;
  private int length;
  
  // initLines - Create a list of ranges that segment the given string into lines
  private Range[] initLineRangesForString(String sourceCode) {
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

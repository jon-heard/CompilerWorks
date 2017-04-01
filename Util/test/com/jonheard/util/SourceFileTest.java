package com.jonheard.util;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class SourceFileTest {

  @Test
  public void main() {
    SourceFile s = new SourceFile("t.txt", "first\nsecond\nthird");
    assertEquals('c', s.getChar(8));
    assertEquals("con", s.getSubstring(8, 11));
    assertEquals(new Point(2, 1), s.getCharPosition(8));
    assertEquals("t.txt", s.getFilename());
    assertEquals("first\nsecond\nthird", s.getSourceCode());
    assertEquals(3, s.getLineCount());
    assertEquals("first", s.getLine(0));
    assertEquals("second", s.getLine(1));
    assertEquals("third", s.getLine(2));
    assertEquals(18, s.getLength());
    assertEquals(8, s.getIndex(1, 2));
  }

}

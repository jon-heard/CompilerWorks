package com.jonheard.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class SourceFileInfoTest
{

	@Test
	public void test()
	{
		SourceFileInfo s = new SourceFileInfo("t.txt", "first\nsecond\nthird");
		assertEquals("t.txt", s.getFilename());
		assertEquals("first\nsecond\nthird", s.getSourcecode());
		assertEquals(3, s.getLineCount());
		assertEquals("first", s.getLine(0));
		assertEquals("second", s.getLine(1));
		assertEquals("third", s.getLine(2));
	}

}

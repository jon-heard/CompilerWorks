package com.jonheard.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class RewindableQueueTest {

	@Test
	public void test()
	{
		RewindableQueue<String> test = new RewindableQueue<String>();
		test.add("first");
		test.add("second");
		test.add("third");
		test.add("fourth");
		test.add("fifth");
		assertEquals("[first, second, third, fourth, fifth]", test.toString());
		assertEquals("first", test.poll());
		assertEquals("[second, third, fourth, fifth]", test.toString());
		test.rewind();
		assertEquals("[second, third, fourth, fifth]", test.toString());
		test.remember();
		assertEquals("second", test.poll());
		assertEquals("third", test.poll());
		assertEquals("[fourth, fifth]", test.toString());
		test.rewind();
		assertEquals("[second, third, fourth, fifth]", test.toString());
		assertEquals("second", test.poll());
		assertEquals("[third, fourth, fifth]", test.toString());
		test.rewind();
		assertEquals("[third, fourth, fifth]", test.toString());
	}
}

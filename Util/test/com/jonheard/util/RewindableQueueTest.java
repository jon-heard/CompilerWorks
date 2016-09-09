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

		// Add
		assertEquals("[first, second, third, fourth, fifth]", test.toString());
		
		// Poll without memory
		assertEquals("first", test.poll());
		assertEquals("[second, third, fourth, fifth]", test.toString());
		test.rewind();
		assertEquals("[second, third, fourth, fifth]", test.toString());
		
		// Poll with memory
		test.remember();
		assertEquals("second", test.poll());
		assertEquals("third", test.poll());
		assertEquals("[fourth, fifth]", test.toString());
		test.rewind();
		assertEquals("[second, third, fourth, fifth]", test.toString());
		
		// Calling remember clears memory
		test.remember();
		assertEquals("second", test.poll());
		assertEquals("third", test.poll());
		assertEquals("[fourth, fifth]", test.toString());
		test.remember();
		assertEquals("fourth", test.poll());
		assertEquals("[fifth]", test.toString());
		test.rewind();
		assertEquals("[fourth, fifth]", test.toString());
		
		// Calling rewind stops recording memory
		assertEquals("fourth", test.poll());
		assertEquals("[fifth]", test.toString());
		test.rewind();
		assertEquals("[fifth]", test.toString());
	}
}

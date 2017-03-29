package com.jonheard.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class RewindableQueueTest {

  @Test
  public void basicQueueFunctionality() {
    RewindableQueue<String> test = new RewindableQueue<String>();
    test.add("first");
    test.add("second");
    test.add("third");

    assertEquals("[first, second, third]", test.toString());
    assertFalse(test.isEmpty());

    assertEquals("first", test.poll());
    assertEquals("[second, third]", test.toString());
    assertFalse(test.isEmpty());

    assertEquals("second", test.poll());
    assertEquals("[third]", test.toString());
    assertEquals("third", test.poll());
    assertEquals("[]", test.toString());
    assertTrue(test.isEmpty());
  }

  @Test
  public void singleMemory() {
    RewindableQueue<String> test = new RewindableQueue<String>();
    test.add("first");
    test.add("second");
    test.add("third");
    assertEquals("[first, second, third]", test.toString());

    // Normal remember/rewind
    test.remember();
    assertEquals("first", test.poll());
    assertEquals("second", test.poll());
    assertEquals("[third]", test.toString());
    test.rewind();
    assertEquals("[first, second, third]", test.toString());

    // Rewind without remember
    assertEquals("first", test.poll());
    assertEquals("[second, third]", test.toString());
    test.rewind();
    assertEquals("[second, third]", test.toString());

    // Forget
    test.remember();
    assertEquals("second", test.poll());
    assertEquals("[third]", test.toString());
    test.forget();
    test.rewind();
    assertEquals("[third]", test.toString());
  }

  @Test
  public void multiMemory() {
    RewindableQueue<String> test = new RewindableQueue<String>();
    test.add("first");
    test.add("second");
    test.add("third");
    test.add("fourth");
    test.add("fifth");
    assertEquals("[first, second, third, fourth, fifth]", test.toString());

    // Normal remember/rewind
    test.remember();
    assertEquals("first", test.poll());
    assertEquals("second", test.poll());
    test.remember();
    assertEquals("third", test.poll());
    assertEquals("fourth", test.poll());
    assertEquals("[fifth]", test.toString());
    test.rewind();
    assertEquals("[third, fourth, fifth]", test.toString());
    test.rewind();
    assertEquals("[first, second, third, fourth, fifth]", test.toString());

    // Calling forget clears single memory
    test.remember();
    assertEquals("first", test.poll());
    assertEquals("second", test.poll());
    test.remember();
    assertEquals("third", test.poll());
    assertEquals("fourth", test.poll());
    assertEquals("[fifth]", test.toString());
    test.forget();
    test.rewind();
    assertEquals("[first, second, fifth]", test.toString());
    test.rewind();
    assertEquals("[first, second, fifth]", test.toString());
  }
}

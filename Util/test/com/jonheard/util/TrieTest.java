package com.jonheard.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.util.Trie;

public class TrieTest
{
	@Test
	public void get()
	{
		Trie<String> t = new Trie<String>();
		t.put("apple", "core");
		t.put("avocado", "pit");
		t.put("apricot", "meat");
		t.put("orange", "pulp");
		
		assertEquals("core", t.get("apple"));
		assertEquals("pit", t.get("avocado"));
		assertEquals("meat", t.get("apricot"));
		assertEquals("pulp", t.get("orange"));
	}
	
	@Test
	public void getFromEmbeddedKey()
	{
		Trie<String> t = new Trie<String>();
		t.put("apple", "core");
		t.put("avocado", "pit");
		t.put("apricot", "meat");
		t.put("orange", "pulp");
		
		assertEquals("core", t.getFromEmbeddedKey("appledelicious", 0));
		assertEquals("pit", t.getFromEmbeddedKey("ripeavocado", 4));
		assertEquals("meat", t.getFromEmbeddedKey("apricot fruit", 0));
		assertEquals("pulp", t.getFromEmbeddedKey("orange", 0));
	}
	
	@Test
	public void badKey()
	{
		Trie<String> t = new Trie<String>();
		t.put("apple", "core");
		t.put("avocado", "pit");
		t.put("apricot", "meat");
		t.put("orange", "pulp");

		assertEquals(null, t.get("banana"));
		assertEquals(null, t.getFromEmbeddedKey("bananaripe", 0));
	}
	
	@Test
	public void generics()
	{
		Trie<Integer> t = new Trie<Integer>();
		t.put("one", 1);
		t.put("two", 2);
		t.put("three", 3);

		assertEquals(new Integer(1), t.get("one"));
		assertEquals(new Integer(2), t.get("two"));
		assertEquals(new Integer(3), t.get("three"));

		assertEquals(null, t.get("four"));
		assertEquals(null, t.getFromEmbeddedKey("five", 0));
	}
}

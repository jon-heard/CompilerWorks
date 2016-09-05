package com.jonheard.util;

import java.util.HashMap;

public class Trie <T>
{
	private HashMap<Character, Trie<T>> children =
			new HashMap<Character, Trie<T>>();
	private T value = null;
	
	public void put(String key, T value)
	{
		int currentIndex = 0;
		Trie<T> currentTrie = this;
		while(currentIndex < key.length())
		{
			Character currentChar = key.charAt(currentIndex);
			if(!currentTrie.children.containsKey(currentChar))
			{
				currentTrie.children.put(currentChar, new Trie<T>());
			}
			currentTrie = currentTrie.children.get(currentChar);
			currentIndex++;
		}
		currentTrie.value = value;
	}
	
	public T get(String key)
	{
		int currentIndex = 0;
		Trie<T> currentTrie = this;
		while(currentIndex < key.length())
		{
			Character currentChar = key.charAt(currentIndex);
			if(!currentTrie.children.containsKey(currentChar))
			{
				return null;
			}
			currentTrie = currentTrie.children.get(currentChar);
			currentIndex++;
		}
		return currentTrie.value;
	}

	public T getFromEmbeddedKey(String keySrc, int startIndex)
	{
		T result = null;
		int currentIndex = startIndex;
		Trie<T> currentTrie = this;
		while(currentIndex < keySrc.length())
		{
			Character currentChar = keySrc.charAt(currentIndex);
			if(!currentTrie.children.containsKey(currentChar))
			{
				return result;
			}
			currentTrie = currentTrie.children.get(currentChar);
			currentIndex++;
			if(currentTrie.value != null)
			{
				result = currentTrie.value;
			}
		}
		return result;
	}
}

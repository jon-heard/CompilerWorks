package com.jonheard.util;

import java.util.HashMap;

// Trie - Words are stored in the trie as 'T' (generic) values
public class Trie<T> {
  // Add a word as its String value and the 'T' value to store for the word
  public void put(String key, T value) {
    // bad input check
    if (key == null) { throw new IllegalArgumentException("Arg1(key): null"); }
    if (value == null) { throw new IllegalArgumentException("Arg2(value): null"); }

    int currentIndex = 0;
    Trie<T> currentTrie = this;
    while (currentIndex < key.length()) {
      Character currentChar = key.charAt(currentIndex);
      if (!currentTrie.children.containsKey(currentChar)) {
        currentTrie.children.put(currentChar, new Trie<T>());
      }
      currentTrie = currentTrie.children.get(currentChar);
      currentIndex++;
    }
    currentTrie.value = value;
  }

  // Get a word by it's String value
  public T get(String key) {
    // bad input check
    if (key == null) { throw new IllegalArgumentException("Arg1(key): null"); }

    int currentIndex = 0;
    Trie<T> currentTrie = this;
    while (currentIndex < key.length()) {
      Character currentChar = key.charAt(currentIndex);
      if (!currentTrie.children.containsKey(currentChar)) { return null; }
      currentTrie = currentTrie.children.get(currentChar);
      currentIndex++;
    }
    return currentTrie.value;
  }

  // Returns the longest word matching characters in 'keySrc', starting from the index 'startIndex' 
  public T getFromEmbeddedKey(String keySrc, int startIndex) {
    // bad input check
    if (keySrc == null) { throw new IllegalArgumentException("Arg1(keySrc): null"); }
    if (startIndex < 0) { throw new IllegalArgumentException("Arg2(startIndex): < 0"); }
    if (startIndex > keySrc.length()) {
      throw new IllegalArgumentException("Arg2(startIndex): > keySource.length()");
    }

    T result = null;
    int currentIndex = startIndex;
    Trie<T> currentTrie = this;
    while (currentIndex < keySrc.length()) {
      Character currentChar = keySrc.charAt(currentIndex);
      if (!currentTrie.children.containsKey(currentChar)) { return result; }
      currentTrie = currentTrie.children.get(currentChar);
      currentIndex++;
      if (currentTrie.value != null) {
        result = currentTrie.value;
      }
    }
    return result;
  }

  private HashMap<Character, Trie<T>> children = new HashMap<>();
  private T value = null;
}

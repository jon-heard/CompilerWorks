package com.jonheard.util;

import java.util.HashMap;

// Trie - Words are stored in the trie as 'T' (generic) values
public class Trie<T> {
  // Add a word as its String value and the 'T' value to store for the word
  public void put(String key, T value) {
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

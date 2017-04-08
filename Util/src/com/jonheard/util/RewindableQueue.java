package com.jonheard.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

// RewindableQueue - A queue that can remember dequeued values on request (via remember & forget).
// Setup to remember multiple times (for recursion etc) 
@SuppressWarnings("serial")
public class RewindableQueue<T> extends LinkedList<T> {
  public RewindableQueue() {}

  public RewindableQueue(List<T> source) {
    // bad input check
    if (source == null) { throw new IllegalArgumentException("Arg1(source): null"); }

    for (T item : source) {
      add(item);
    }
  }

  public void remember() {
    memory.push(new Stack<T>());
  }

  public void forget() {
    memory.pop();
  }

  public void rewind() {
    if (!memory.isEmpty()) {
      Stack<T> toRewind = memory.pop();
      while (!toRewind.isEmpty()) {
        addFirst(toRewind.pop());
      }
    }
  }

  @Override
  public T poll() {
    if (!memory.isEmpty()) {
      memory.peek().push(peek());
    }
    return super.poll();
  }

  private Stack<Stack<T>> memory = new Stack<Stack<T>>();
}

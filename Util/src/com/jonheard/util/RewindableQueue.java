package com.jonheard.util;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

@SuppressWarnings("serial")
public class RewindableQueue<T> extends LinkedList<T>
{
	public RewindableQueue(List<T> source)
	{
		for(T item : source)
		{
			add(item);
		}
	}
	
	public void remember()
	{
		remembering = true;
		memory.clear();
	}
	
	public void rewind()
	{
		remembering = false;
		while(!memory.isEmpty())
		{
			addFirst(memory.pop());
		}
	}
	
	@Override
	public T poll()
	{
		if(remembering)
		{
			memory.push(peek());
		}
		return super.poll();
	}
	
	private boolean remembering = false;
	private Stack<T> memory = new Stack<T>();
}

package com.jonheard.compilers.irProcessor_java;

import java.util.HashMap;

public class Scope
{
	public Scope(ScopeType type) {}
	public Scope(ScopeType type, Scope previous)
	{
		this.type = type;
		if(previous != null)
		{
			stackCounter = previous.stackCounter;
		}
	}
	
	public ScopeType getScopeType()
	{
		return type;
	}

	public boolean contains(String name)
	{
		return items.containsKey(name);
	}
	
	public ScopeItem get(String name)
	{
		if(items.containsKey(name))
		{
			return items.get(name);
		}
		return null;
	}
	
	public void add(String name, String javaType, String fullname)
	{
		items.put(name, new ScopeItem(javaType, fullname));
	}
	
	public void add(String name, String javaType)
	{
		System.out.println(javaType + " #" + stackCounter);
		items.put(name, new ScopeItem(javaType, "#" + stackCounter));
		stackCounter++;
	}
	
	private ScopeType type;
	private int stackCounter = 0;
	private HashMap<String, ScopeItem> items = new HashMap<String, ScopeItem>();
}

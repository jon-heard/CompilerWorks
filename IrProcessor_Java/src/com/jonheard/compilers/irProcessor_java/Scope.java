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
	
	public ScopeType getScopeType() { return type; }

	public boolean contains(String name) { return items.containsKey(name); }
	
	public boolean containsJavaType(String name)
	{
		return javaTypes.containsKey(name);
	}
	
	public String get(String name)
	{
		if(items.containsKey(name))
		{
			return items.get(name);
		}
		return null;
	}
	
	public String getJavaType(String name)
	{
		if(javaTypes.containsKey(name))
		{
			return javaTypes.get(name);
		}
		return null;
	}
	
	public String add(String name, String scopedName)
	{
		items.put(name, scopedName);
		return scopedName;
	}

	public String add(String name)
	{
		String scopedName = "#" + stackCounter;
		stackCounter++;
		return add(name, scopedName);
	}
	
	public void addJavaType(String name, String javaType)
	{
		javaTypes.put(name, javaType);
	}

	private ScopeType type;
	private int stackCounter = 0;
	private HashMap<String, String> items = new HashMap<String, String>();
	private HashMap<String, String> javaTypes = new HashMap<String, String>();
}

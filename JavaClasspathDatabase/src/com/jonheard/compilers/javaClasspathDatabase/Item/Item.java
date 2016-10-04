package com.jonheard.compilers.javaClasspathDatabase.Item;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

public class Item implements Iterable<Item>
{
	public Item(String name, Item parent)
	{
		this.name = name;
		this.parent = parent;
		if(parent != null)
		{
			parent.children.put(name, this);
		}
	}
	
	public Item clone(Item newParent)
	{
		Item result = new Item(name, newParent);
		for(String key : children.keySet())
		{
			result.children.put(key, children.get(key));
		}
		return result;
	}
	
	public String getName() { return name; }
	
	public Item getParent() { return parent; }
	
	public boolean hasChild(String name) { return children.containsKey(name); }

	public Item getChild(String name)
	{
		return children.containsKey(name) ? children.get(name) : null;
	}
	
	public Set<String> getChildList() { return children.keySet(); }
	
	public String getJavaAddress()
	{
		if(parent == null || parent instanceof Item_Non)
		{
			return name;
		}
		else
		{
			return parent.getJavaAddress() + "." + name;
		}
	}
	
	public String getFileAddress()
	{
		if(parent == null || parent instanceof Item_Non)
		{
			return name;
		}
		else
		{
			return parent.getFileAddress() + "/" + name;
		}
	}
	
	@Override
	public String toString()
	{
		return "Item(" + getName() + "," + getParent().getName() + ")"; 
	}
	
	private String name;
	private Item parent;
	protected HashMap<String, Item> children =
			new HashMap<String, Item>();
	
	@Override
	public Iterator<Item> iterator()
	{
		return children.values().iterator();
	}
}

package com.jonheard.compilers.javaClassHeirarchy.Item;

import java.util.HashMap;

public class Item
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
	
	public String getName() { return name; }
	
	public Item getParent() { return parent; }
	
	public boolean hasChild(String name)
	{
		return children.containsKey(name);
	}

	public Item getChild(String name)
	{
		return children.containsKey(name) ? children.get(name) : null;
	}
	
	public String getJavaAddress()
	{
		if(parent == null || parent instanceof Item_Folder)
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
		if(parent == null || parent instanceof Item_Folder)
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
}

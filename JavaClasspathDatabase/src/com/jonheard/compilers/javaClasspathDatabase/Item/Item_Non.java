package com.jonheard.compilers.javaClasspathDatabase.Item;

/// Used to represent a non-value.  Specifically:
/// Source's "root", where baseline items are stored (name = source's filename)
/// Representing a primitive type (ie. not in database) (name = type)
public class Item_Non extends Item
{
	public Item_Non(String name) { super(name, null); }
	
	@Override
	public String getJavaAddress()
	{
		return "";
	}

	@Override
	public String getFileAddress()
	{
		return "";
	}
}

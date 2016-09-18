package com.jonheard.compilers.javaClassHeirarchyReader;

public class Item
{
	public Item(String name, ItemType type, Item parent)
	{
		this.name = name;
		this.type = type;
		this.parent = parent;
	}
	
	public String getName()
	{
		return name;
	}
	
	public ItemType getType()
	{
		return type;
	}
	
	public Item getParent()
	{
		return parent;
	}
	
	private String name;
	private ItemType type;
	private Item parent;
}

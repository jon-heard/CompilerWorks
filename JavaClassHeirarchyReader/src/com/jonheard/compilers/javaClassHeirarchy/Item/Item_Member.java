package com.jonheard.compilers.javaClassHeirarchy.Item;

public class Item_Member extends Item
{
	public Item_Member(String name, Item parent, String descriptor)
	{
		super(name, parent);
		this.descriptor = descriptor;
	}
	
	public String getDescriptor() { return descriptor; }
	
	public boolean isMethod()
	{
		return descriptor.startsWith("(");
	}

	public boolean isField()
	{
		return !descriptor.startsWith("(");
	}
	
	private String descriptor;
}

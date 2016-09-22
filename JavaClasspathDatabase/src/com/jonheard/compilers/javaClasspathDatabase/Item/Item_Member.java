package com.jonheard.compilers.javaClasspathDatabase.Item;

public class Item_Member extends Item
{
	public Item_Member(
			String name, Item parent, Item type,
			String descriptor, boolean isStatic)
	{
		super(name, parent);
		this.type = type;
		this.descriptor = descriptor;
		staticMember = isStatic;
	}
	
	@Override
	public boolean hasChild(String name)
	{
		return type.hasChild(name);
	}

	@Override
	public Item getChild(String name)
	{
		return type.getChild(name);
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

	public boolean isStatic()
	{
		return staticMember;
	}

	private String descriptor;
	private boolean staticMember;
	private Item type;
}

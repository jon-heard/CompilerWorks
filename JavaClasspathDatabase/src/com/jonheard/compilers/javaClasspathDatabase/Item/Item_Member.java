package com.jonheard.compilers.javaClasspathDatabase.Item;

public class Item_Member extends Item
{
	public Item_Member(
			String name, Item parent, Item type,
			String descriptor, boolean isStatic)
	{
		super(name, parent);
		this.type = type;
		staticMember = isStatic;
		this.descriptor = descriptor;
		/// Count the type's array index while removing '['s
		int typeIndex = this.descriptor.indexOf(')')+1;
		this.descriptor =
				this.descriptor.substring(0, typeIndex) +
				this.descriptor.substring(typeIndex).replace("[", "");
		arrayDimension = this.descriptor.length() - descriptor.length();
	}

	@Override
	public Item clone(Item newParent)
	{
		Item result = new Item_Member(
				getName(), newParent, type, descriptor, staticMember);
		for(String key : children.keySet())
		{
			result.children.put(key, children.get(key));
		}
		return result;
	}

	@Override
	public boolean hasChild(String name) { return type.hasChild(name); }

	@Override
	public Item getChild(String name)
	{
		return type.getChild(name).clone(this);
	}
	
	public String getDescriptor() { return descriptor; }
	
	public boolean isMethod() { return descriptor.startsWith("("); }

	public boolean isField() { return !descriptor.startsWith("("); }

	public boolean isStatic() { return staticMember; }
	
	public int getArrayDimension() { return arrayDimension; }

	private String descriptor;
	private boolean staticMember;
	private int arrayDimension = 0;
	private Item type;
}

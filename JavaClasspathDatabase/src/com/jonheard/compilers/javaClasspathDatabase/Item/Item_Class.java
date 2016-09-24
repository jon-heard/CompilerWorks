
package com.jonheard.compilers.javaClasspathDatabase.Item;

import com.jonheard.compilers.javaClasspathDatabase.Source;

public class Item_Class extends Item
{
	public Item_Class(String name, Item parent, Source source)
	{
		super(name, parent);
		this.source = source;
	}
	
	@Override
	public Item clone(Item newParent)
	{
		Item result = new Item_Class(getName(), newParent, source);
		for(String key : children.keySet())
		{
			result.children.put(key, children.get(key));
		}
		return result;
	}
	
	@Override
	public boolean hasChild(String name)
	{
		if(source != null)
		{
			loadChildren();
		}
		return children.containsKey(name);
	}

	@Override
	public Item getChild(String name)
	{
		if(source != null)
		{
			loadChildren();
		}
		return children.containsKey(name) ? children.get(name) : null;
	}
	
	@Override
	public String getFileAddress()
	{
		String name = this.getName() + ".class";
		if(getParent() == null || getParent() instanceof Item_Non)
		{
			return name;
		}
		else
		{
			return getParent().getFileAddress() + "/" + name;
		}
	}

	private void loadChildren()
	{
		if(source != null)
		{
			source.loadItemData(this);
			source = null;
		}
	}
	
	private Source source;
}

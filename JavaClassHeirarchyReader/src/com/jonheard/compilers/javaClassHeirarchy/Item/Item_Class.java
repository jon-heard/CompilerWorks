
package com.jonheard.compilers.javaClassHeirarchy.Item;

import com.jonheard.compilers.javaClassHeirarchy.Source;

public class Item_Class extends Item
{
	public Item_Class(String name, Item parent, Source source)
	{
		super(name, parent);
		mySource = source;
	}
	
	@Override
	public boolean hasChild(String name)
	{
		if(mySource != null)
		{
			loadChildren();
		}
		return children.containsKey(name);
	}

	@Override
	public Item getChild(String name)
	{
		if(mySource != null)
		{
			loadChildren();
		}
		return children.containsKey(name) ? children.get(name) : null;
	}
	
	@Override
	public String getFileAddress()
	{
		String name = this.getName() + ".class";
		if(getParent() == null || getParent() instanceof Item_Folder)
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
		if(mySource != null)
		{
			mySource.loadItemData(this);
			mySource = null;
		}
	}
	
	private Source mySource;
}

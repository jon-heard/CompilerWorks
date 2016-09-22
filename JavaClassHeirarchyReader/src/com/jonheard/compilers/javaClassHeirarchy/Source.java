package com.jonheard.compilers.javaClassHeirarchy;

import java.util.HashMap;
import java.util.Set;

import com.jonheard.compilers.javaClassHeirarchy.Item.*;

public abstract class Source
{
	public abstract void loadItemData(Item toLoad);
	protected abstract boolean loadData();
	
	public Source(String filename)
	{
		root = new Item_Folder(filename);
		if(!loadData())
		{
			valid = false;
		}
	}
	
	public Item getHeirarchy(String address)
	{
		Item result = getMemoized(address);
		if(result != null)
		{
			return result;
		}
		
		String[] addressItems = address.split("\\.");
		
		result = root;
		for(int i = 0; i < addressItems.length; i++)
		{
			if(result.hasChild(addressItems[i]))
			{
				result = result.getChild(addressItems[i]);
			}
			else
			{
				return new Item_Err_NotFound(result.getJavaAddress());
			}
		}

		memoization.put(address, result);
		return result;
	}
	
	protected void loadClassItemDataFromByteArray(
			byte[] data, Item classItem)
	{
		ClassFileMemberReader memberReader =
				new ClassFileMemberReader(data);
		Set<String> members = memberReader.getMemberList();
		for(String member : members)
		{
			new Item_Member(
					member, classItem,
					memberReader.getMemberDescriptor(member));
		}
	}
	
	protected Item getMemoized(String address)
	{
		if(memoization.containsKey(address))
		{
			return memoization.get(address);
		}
		return null;
	}
	
	public boolean isValid()
	{
		return valid;
	}

	protected boolean valid = true;
	protected Item_Folder root;
	protected HashMap<String, Item> memoization =
			new HashMap<String, Item>();
}

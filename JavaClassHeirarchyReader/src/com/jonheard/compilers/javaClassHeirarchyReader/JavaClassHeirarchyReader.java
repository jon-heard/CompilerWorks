package com.jonheard.compilers.javaClassHeirarchyReader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JavaClassHeirarchyReader
{
	public void addSource_Jar(String filename)
	{
		sourceList.add(new SourceJar(filename));
		memoization.clear();
	}
	
	public void addSource_Folder(String filename)
	{
		sourceList.add(new SourceFolder(filename));
		memoization.clear();
	}
	
	public Item getHeirarchy(String address)
	{
		if(memoization.containsKey(address))
		{
			return memoization.get(address);
		}
		
		List<Item> resultList = new ArrayList<Item>();
		for(Source source : sourceList)
		{
			Item newResult = source.getHeirarchy(address);
			if(newResult.getType() != ItemType.ERR_NOT_FOUND)
			{
				resultList.add(newResult);
			}
		}
		
		if(resultList.size() == 0)
		{
			Item result = new Item(address, ItemType.ERR_NOT_FOUND, null);
			memoization.put(address, result);
			return result;
		}
		else if(resultList.size() == 1)
		{
			Item result = resultList.get(0);
			memoization.put(address, result);
			return result;
		}
		else
		{
			for(Item resultItem : resultList)
			{
				if(resultItem.getType() != ItemType.PACKAGE)
				{
					Item result =
							new Item(address, ItemType.ERR_CONFLICT, null);
					memoization.put(address, result);
					return result;
				}
			}
			Item result = resultList.get(0);
			memoization.put(address, result);
			return result;
		}
	}
	
	private List<Source> sourceList = new ArrayList<Source>();
	private HashMap<String, Item> memoization = new HashMap<String, Item>();
}

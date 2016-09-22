package com.jonheard.compilers.javaClassHeirarchy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jonheard.compilers.javaClassHeirarchy.Item.*;

public class JavaClassHeirarchy
{
	public boolean addSource_Folder(String filename)
	{
		Source toAdd = new SourceFolder(filename);
		if(!toAdd.isValid())
		{
			return false;
		}
		sourceList.add(toAdd);
		memoization.clear();
		return true;
	}

	public boolean addSource_Jar(String filename)
	{
		Source toAdd = new SourceJar(filename);
		if(!toAdd.isValid())
		{
			return false;
		}
		sourceList.add(toAdd);
		memoization.clear();
		return true;
	}
	
	public Item getHeirarchy(String address)
	{
		if(memoization.containsKey(address))
		{
			return memoization.get(address);
		}
		
		Item result = null;
		Item furthestFound = new Item("", null);
		List<Item> resultList = new ArrayList<Item>();
		for(Source source : sourceList)
		{
			Item newResult = source.getHeirarchy(address);
			if(!(newResult instanceof Item_Err_NotFound))
			{
				resultList.add(newResult);
			}
			else
			{
				if(	newResult.getName().length() >
					furthestFound.getName().length())
				{
					furthestFound = newResult;
				}
			}
		}
		
		if(resultList.size() == 0)
		{
			result = furthestFound;
		}
		else if(resultList.size() == 1)
		{
			result = resultList.get(0);
		}
		else
		{
			result = resultList.get(0);
			for(Item resultItem : resultList)
			{
				if(!(resultItem instanceof Item_Package))
				{
					result = new Item_Err_Conflict();
				}
			}
		}
		memoization.put(address, result);
		return result;
	}
	
	private List<Source> sourceList = new ArrayList<Source>();
	private HashMap<String, Item> memoization = new HashMap<String, Item>();
}

package com.jonheard.compilers.javaClasspathDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jonheard.compilers.javaClasspathDatabase.Item.*;

public class JavaClasspathDatabase
{
	public boolean addSource_Folder(String filename)
	{
		Source toAdd = new SourceFolder(this, filename);
		if(!toAdd.isValid())
		{
			return false;
		}
		sourceList.add(toAdd);
		resetMemoization();
		return true;
	}

	public boolean addSource_Jar(String filename)
	{
		Source toAdd = new SourceJar(this, filename);
		if(!toAdd.isValid())
		{
			return false;
		}
		sourceList.add(toAdd);
		resetMemoization();
		return true;
	}
	
	public Item getValue(String address)
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
			Item newResult = source.getValue(address);
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
	private final String[] JAVA_PRIMITIVES = {
			"byte", "short", "int", "long",
			"float", "double", "boolean", "char"};
	
	private void resetMemoization()
	{
		if(memoization.isEmpty())
		{
			for(String primitive : JAVA_PRIMITIVES)
			{
				memoization.put(primitive, new Item_Non(primitive));
			}
		}
		else
		{
			HashMap<String, Item> newMemoization = new HashMap<String, Item>();
			for(String primitive : JAVA_PRIMITIVES)
			{
				newMemoization.put(primitive, memoization.get(primitive));
			}
			memoization = newMemoization;
		}
	}
}

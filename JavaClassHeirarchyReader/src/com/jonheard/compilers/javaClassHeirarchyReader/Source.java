package com.jonheard.compilers.javaClassHeirarchyReader;

import java.util.HashMap;

public abstract class Source
{
	public abstract Item getHeirarchy(String address);
	
	protected Item getMemoized(String address)
	{
		if(memoization.containsKey(address))
		{
			return memoization.get(address);
		}
		return null;
	}
	
	public boolean isBad()
	{
		return isBad;
	}
	
	protected boolean isBad = false;
	protected HashMap<String, Item> memoization = new HashMap<String, Item>();
}

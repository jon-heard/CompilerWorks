package com.jonheard.compilers.javaClassHeirarchyReader;

class SourceJar extends Source
{
	public SourceJar(String filename)
	{
		
	}
	
	public Item getHeirarchy(String address)
	{
		Item result = getMemoized(address);
		if(result != null)
		{
			return result;
		}
		
		
		return null;
	}
}

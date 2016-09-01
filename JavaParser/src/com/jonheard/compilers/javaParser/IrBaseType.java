package com.jonheard.compilers.javaParser;

import java.util.List;

public abstract class IrBaseType
{
	private List<IrBaseType> children;
	
	public abstract String getHeaderString();
	
	@Override public toString()
	{
		return "";
	}
}

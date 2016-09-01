package com.jonheard.compilers.javaParser;

import java.util.List;
import com.jonheard.compilers.javaTokenizer.JavaToken;

public class IrCompilationUnit extends IrBaseType
{
	private int importCount;
	private int typeCount;
	
	public IrCompilationUnit(List<JavaToken> tokens, int index)
	{
		
	}
	
	@Override
	public String getHeaderString()
	{
		return "";
	}
}

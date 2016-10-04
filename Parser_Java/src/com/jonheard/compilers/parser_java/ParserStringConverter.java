package com.jonheard.compilers.parser_java;

import com.jonheard.compilers.parser_java.ir.CompilationUnit;

public class ParserStringConverter
{
	public String parsedToString(CompilationUnit source)
	{
		return source.toString();
	}
	
	public CompilationUnit stringToParsed(String source)
	{
		return null;
	}
}

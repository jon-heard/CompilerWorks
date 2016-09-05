package com.jonheard.compilers.javaParser;

import java.util.List;

import com.jonheard.compilers.javaParser.ir.CompilationUnit;
import com.jonheard.compilers.javaTokenizer.JavaToken;

public class JavaParser
{
	private List<JavaToken> tokens;

	public JavaParser(List<JavaToken> tokens)
	{
		this.tokens = tokens;
	}

	public CompilationUnit parse()
	{
		return new CompilationUnit(tokens, 0);
	}
	
	public String parseToString()
	{
		CompilationUnit result = parse();
		return result.toString();
	}
}

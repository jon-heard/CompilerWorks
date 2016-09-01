package com.jonheard.compilers.javaParser;

import java.util.List;
import com.jonheard.compilers.javaTokenizer.JavaToken;

public class JavaParser
{
	private List<JavaToken> tokens;

	public JavaParser(List<JavaToken> tokens)
	{
		this.tokens = tokens;
	}

	public IrCompilationUnit parse()
	{
		return new IrCompilationUnit(tokens, 0);
	}
	
	public String parseToString()
	{
		IrCompilationUnit result = parse();
		return result.toString();
	}
}

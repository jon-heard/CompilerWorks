package com.jonheard.compilers.javaParser.ir.expression;

public class NewArray extends Expression
{
	public NewArray(int line)
	{
		super(ExpressionType.NEW_ARRAY, line);
	}
}

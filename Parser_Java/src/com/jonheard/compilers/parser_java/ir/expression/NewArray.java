package com.jonheard.compilers.parser_java.ir.expression;

public class NewArray extends Expression
{
	public NewArray(int line)
	{
		super(ExpressionType.NEW_ARRAY, line);
	}
}

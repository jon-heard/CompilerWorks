package com.jonheard.compilers.parser_java.ir.expression;

public class NewObject extends Expression
{
	public NewObject(int line)
	{
		super(ExpressionType.NEW_OBJECT, line);
	}
}

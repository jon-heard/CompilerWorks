package com.jonheard.compilers.javaParser.ir.expression;

public class NewObject extends Expression
{
	public NewObject(int line)
	{
		super(ExpressionType.NEW_OBJECT, line);
	}
}

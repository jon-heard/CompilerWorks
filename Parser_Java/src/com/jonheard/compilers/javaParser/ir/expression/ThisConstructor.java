package com.jonheard.compilers.javaParser.ir.expression;

import com.jonheard.compilers.javaParser.ir.List_Expression;

public class ThisConstructor extends Expression
{
	public ThisConstructor(int line, List_Expression parameters)
	{
		super(ExpressionType.THIS_CONSTRUCTOR, line);
		addChild(parameters);
	}
}

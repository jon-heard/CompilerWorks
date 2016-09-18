package com.jonheard.compilers.javaParser.ir.expression;

import com.jonheard.compilers.javaParser.ir.List_Expression;

public class SuperConstructor extends Expression
{
	public SuperConstructor(int line, List_Expression parameters)
	{
		super(ExpressionType.SUPER_CONSTRUCTOR, line);
		addChild(parameters);
	}
}

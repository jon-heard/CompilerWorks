package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;

public class SuperConstructor extends Expression
{
	public SuperConstructor(int line, List_Expression parameters)
	{
		super(ExpressionType.SUPER_CONSTRUCTOR, line);
		addChild(parameters);
	}
}

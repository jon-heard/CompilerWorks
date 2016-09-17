package com.jonheard.compilers.javaParser.ir.expression;

import com.jonheard.compilers.javaParser.ir.List_Expression;
import com.jonheard.compilers.javaParser.ir.QualifiedIdentifier;

public class MethodCall extends Expression
{
	public MethodCall(
			int line, QualifiedIdentifier id, List_Expression parameters)
	{
		super(ExpressionType.MethodCall, line);
		addChild(id);
		addChild(parameters);
	}
}

package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedIdentifier;

public class MethodCall extends Expression
{
	public MethodCall(
			int line, QualifiedIdentifier id, List_Expression parameters)
	{
		super(ExpressionType.METHOD_CALL, line);
		addChild(id);
		addChild(parameters);
	}
}

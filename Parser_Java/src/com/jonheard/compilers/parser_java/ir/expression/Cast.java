package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.QualifiedIdentifier;

public class Cast extends Expression
{
	public Cast(int line, QualifiedIdentifier castTo, Expression rhs)
	{
		super(ExpressionType.CAST, line);
		addChild(castTo);
		addChild(rhs);
	}
}

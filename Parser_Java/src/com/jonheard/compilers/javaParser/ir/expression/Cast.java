package com.jonheard.compilers.javaParser.ir.expression;

import com.jonheard.compilers.javaParser.ir.QualifiedIdentifier;

public class Cast extends Expression
{
	public Cast(int line, QualifiedIdentifier castTo, Expression rhs)
	{
		super(ExpressionType.CAST, line);
		addChild(castTo);
		addChild(rhs);
	}
}

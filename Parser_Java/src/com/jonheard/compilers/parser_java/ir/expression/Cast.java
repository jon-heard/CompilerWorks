package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.QualifiedIdentifier;
import com.jonheard.compilers.tokenizer_java.Token;

public class Cast extends Expression
{
	public Cast(Token next, QualifiedIdentifier castTo, Expression rhs)
	{
		super(ExpressionType.CAST, next);
		addChild(castTo);
		addChild(rhs);
	}
}

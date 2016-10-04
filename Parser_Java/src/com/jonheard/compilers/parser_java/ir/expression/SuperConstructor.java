package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.tokenizer_java.Token;

public class SuperConstructor extends Expression
{
	public SuperConstructor(Token next, List_Expression parameters)
	{
		super(ExpressionType.SUPER_CONSTRUCTOR, next);
		addChild(parameters);
	}
}

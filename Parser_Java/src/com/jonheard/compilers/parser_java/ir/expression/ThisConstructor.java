package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.tokenizer_java.Token;

public class ThisConstructor extends Expression
{
	public ThisConstructor(Token next, List_Expression parameters)
	{
		super(ExpressionType.THIS_CONSTRUCTOR, next);
		addChild(parameters);
	}
}

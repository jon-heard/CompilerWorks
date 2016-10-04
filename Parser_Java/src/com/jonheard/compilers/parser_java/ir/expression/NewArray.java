package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.tokenizer_java.Token;

public class NewArray extends Expression
{
	public NewArray(Token next)
	{
		super(ExpressionType.NEW_ARRAY, next);
	}
}

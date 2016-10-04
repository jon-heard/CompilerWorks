package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.tokenizer_java.Token;

public class NewObject extends Expression
{
	public NewObject(Token next)
	{
		super(ExpressionType.NEW_OBJECT, next);
	}
}

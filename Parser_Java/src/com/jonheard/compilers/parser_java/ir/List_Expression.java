package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class List_Expression extends BaseIrType
{
	public List_Expression(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		Token current = tokenQueue.peek();
		do
		{
			addChild(Parser_Expression.parseExpression(tokenQueue));
			current = tokenQueue.peek();
		}
		while(current.getType() == TokenType.COMMA);
	}
}

package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class List_Variable extends BaseIrType
{
	public List_Variable(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		Token current = tokenQueue.peek();
		do
		{
			addChild(new Variable(tokenQueue));
			current = tokenQueue.peek();
		}
		while(current.getType() == TokenType.COMMA);
	}
}

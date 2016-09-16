package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import expression.Parser_Expression;

public class List_Expression extends BaseIrType
{
	public List_Expression(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		JavaToken current = tokenQueue.peek();
		do
		{
			addChild(Parser_Expression.parseExpression(tokenQueue));
			current = tokenQueue.peek();
		}
		while(current.getType() == JavaTokenType.COMMA);
	}
}

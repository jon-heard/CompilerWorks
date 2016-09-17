package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class List_Variable extends BaseIrType
{
	public List_Variable(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		JavaToken current = tokenQueue.peek();
		do
		{
			addChild(new Variable(tokenQueue));
			current = tokenQueue.peek();
		}
		while(current.getType() == JavaTokenType.COMMA);
	}
}
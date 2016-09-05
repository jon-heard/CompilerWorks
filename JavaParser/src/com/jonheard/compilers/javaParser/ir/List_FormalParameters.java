package com.jonheard.compilers.javaParser.ir;

import java.util.Queue;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;

public class List_FormalParameters extends BaseType
{
	public List_FormalParameters(Queue<JavaToken> tokenQueue)
	{
		JavaToken current = tokenQueue.peek();
		do
		{
			children.add(new VariableDeclaration(tokenQueue));
		}
		while(current.getType() == JavaTokenType.COMMA);
	}

	@Override
	public String getHeaderString() { return ""; }
}

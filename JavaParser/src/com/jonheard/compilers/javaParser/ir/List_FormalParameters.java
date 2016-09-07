package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class List_FormalParameters extends BaseType
{
	public List_FormalParameters(RewindableQueue<JavaToken> tokenQueue)
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

package com.jonheard.compilers.javaParser.ir;

import java.util.Queue;

import com.jonheard.compilers.javaTokenizer.JavaToken;

public class MethodPart extends BaseType
{
	public MethodPart(Queue<JavaToken> tokenQueue)
	{
		tokenQueue.poll();
		children.add(new List_FormalParameters(tokenQueue));
		tokenQueue.poll();
		children.add(new CodeBlock(tokenQueue));
	}

	@Override
	public String getHeaderString() { return ""; }
}

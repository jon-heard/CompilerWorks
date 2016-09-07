package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class MethodPart extends BaseType
{
	public MethodPart(RewindableQueue<JavaToken> tokenQueue)
	{
		tokenQueue.poll();
		children.add(new List_FormalParameters(tokenQueue));
		tokenQueue.poll();
		children.add(new CodeBlock(tokenQueue));
	}

	@Override
	public String getHeaderString() { return ""; }
}

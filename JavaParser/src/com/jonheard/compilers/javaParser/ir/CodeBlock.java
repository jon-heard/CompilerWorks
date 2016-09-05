package com.jonheard.compilers.javaParser.ir;

import java.util.Queue;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;

public class CodeBlock extends BaseType
{
	public CodeBlock(Queue<JavaToken> tokenQueue)
	{
		tokenQueue.poll();
		while(tokenQueue.peek().getType() != JavaTokenType.CURL_BRACE_RIGHT)
		{
			tokenQueue.poll();
		}
		tokenQueue.poll();
	}

	@Override
	public String getHeaderString() { return ""; }
}

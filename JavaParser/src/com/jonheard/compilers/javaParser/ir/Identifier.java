package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class Identifier extends BaseType
{
	private String value = "";
	
	public Identifier(RewindableQueue<JavaToken> tokenQueue)
	{
		JavaToken currentToken = tokenQueue.poll();
		if(currentToken.getType() == JavaTokenType.IDENTIFIER)
		{
			value = currentToken.getText();
		}
	}
	
	public String getValue()
	{
		return value;
	}

	@Override
	public String getHeaderString()
	{
		return "value='" + value + "'";
	}
}

package com.jonheard.compilers.javaParser.ir;

import java.util.Queue;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;

public class Identifier extends BaseType
{
	private String value = "";
	
	public Identifier(Queue<JavaToken> tokenQueue)
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

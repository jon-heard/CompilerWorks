package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class QualifiedIdentifier extends BaseType
{
	public QualifiedIdentifier(RewindableQueue<JavaToken> tokenQueue)
	{
		this(tokenQueue, false);
	}
	
	public QualifiedIdentifier(
			RewindableQueue<JavaToken> tokenQueue, boolean ignoreBadEnding)
	{
		JavaToken currentToken = tokenQueue.peek();
		while(currentToken.getType() == JavaTokenType.IDENTIFIER)
		{
			endedWithDot = false;
			children.add(new Identifier(tokenQueue));
			currentToken = tokenQueue.peek();
			if(currentToken.getType() == JavaTokenType.DOT)
			{
				endedWithDot = true;
				tokenQueue.poll();
				currentToken = tokenQueue.peek();
			}
			else
			{
				break;
			}
		}
		if(!ignoreBadEnding && endedWithDot)
		{
			// Error handling on bad ending
		}
	}
	
	@Override
	public String getHeaderString()
	{
		return "value='" + getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 10000000; }
	
	public String getValue()
	{
		StringBuffer result = new StringBuffer();
		for(BaseType child : children)
		{
			result.append(((Identifier)child).getValue());
			result.append(".");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
	
	public boolean isEndedWithDot()
	{
		return endedWithDot;
	}

	private boolean endedWithDot = false;
}

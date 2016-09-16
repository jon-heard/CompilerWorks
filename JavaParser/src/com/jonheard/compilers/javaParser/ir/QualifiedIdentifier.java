package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class QualifiedIdentifier extends BaseIrType
{
	public QualifiedIdentifier(RewindableQueue<JavaToken> tokenQueue)
	{
		this(tokenQueue, false);
	}
	
	public QualifiedIdentifier(
			RewindableQueue<JavaToken> tokenQueue, boolean ignoreBadEnding)
	{
		super(tokenQueue);
		JavaToken currentToken = tokenQueue.peek();
		while(currentToken.getType() == JavaTokenType.IDENTIFIER)
		{
			endedWithDot = false;
			addChild(new Identifier(tokenQueue));
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
		for(int i = 0; i < getChildCount(); i++)
		{
			result.append(((Identifier)getChild(i)).getValue());
			result.append(".");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
	
	public boolean isEndedWithDot()
	{
		return endedWithDot;
	}
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType.IDENTIFIER);
	}

	private boolean endedWithDot = false;
}

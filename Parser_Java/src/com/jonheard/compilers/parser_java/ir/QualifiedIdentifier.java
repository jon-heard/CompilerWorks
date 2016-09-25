package com.jonheard.compilers.parser_java.ir;

import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class QualifiedIdentifier extends BaseIrType
{
	public QualifiedIdentifier(RewindableQueue<Token> tokenQueue)
	{
		this(tokenQueue, false);
	}
	
	public QualifiedIdentifier(
			RewindableQueue<Token> tokenQueue, boolean ignoreBadEnding)
	{
		super(tokenQueue);
		Token currentToken = tokenQueue.peek();
		while(currentToken.getType() == TokenType.IDENTIFIER)
		{
			endedWithDot = false;
			addChild(new Identifier(tokenQueue));
			currentToken = tokenQueue.peek();
			if(currentToken.getType() == TokenType.DOT)
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
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType.IDENTIFIER);
	}

	private boolean endedWithDot = false;
}

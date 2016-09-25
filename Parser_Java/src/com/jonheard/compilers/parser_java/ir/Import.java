package com.jonheard.compilers.parser_java.ir;

import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Import extends BaseIrType
{
	boolean isStatic = false;
	boolean isOnDemand = false;

	public Import(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType._IMPORT);
		if(see(tokenQueue, TokenType._STATIC))
		{
			isStatic = true;
			tokenQueue.poll();
		}
		QualifiedIdentifier identifier =
				new QualifiedIdentifier(tokenQueue, true);
		addChild(identifier);
		if(identifier.isEndedWithDot())
		{
			if(mustBe(tokenQueue, TokenType.STAR))
			{
				isOnDemand = true;
			}
		}
		mustBe(tokenQueue, TokenType.SEMICOLON);
	}

	@Override
	public String getHeaderString()
	{
		return	"isOnDemaned='" + isOnDemand + "' " +
				"isStatic='" + isStatic + "' " +
				"identifier='" + getIdentifier().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedIdentifier getIdentifier()
	{
		return (QualifiedIdentifier)getChild(0);
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType._IMPORT);
	}
}

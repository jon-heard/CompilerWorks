package com.jonheard.compilers.parser_java.ir;

import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Package extends BaseIrType
{
	Package(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		tokenQueue.poll();
		addChild(new QualifiedIdentifier(tokenQueue));
		tokenQueue.poll();
	}

	@Override
	public String getHeaderString()
	{
		return	"identifier='" + getIdentifier().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedIdentifier getIdentifier()
	{
		return (QualifiedIdentifier)getChild(0);
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType._PACKAGE);
	}

}

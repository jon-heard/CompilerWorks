package com.jonheard.compilers.parser_java.ir;

import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Variable extends BaseIrType
{
	public Variable(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		addChild(new List_Modifier(tokenQueue));
		addChild(new Type(tokenQueue));
		addChild(new Identifier(tokenQueue));
		while(have(tokenQueue, TokenType.SQUARE_BRACE_LEFT))
		{
			mustBe(tokenQueue, TokenType.SQUARE_BRACE_RIGHT);
			getType().incDimensionCount();
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "' " +
				"type='" + getType().getValue() + "' " +
				"modifiers='" + getModifiers().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	
	public Type getType()
	{
		return (Type)getChild(1);
	}
	
	public Identifier getName()
	{
		return (Identifier)getChild(2);
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		boolean result = false;
		tokenQueue.remember();
		new List_Modifier(tokenQueue);
		if(Type.isNext(tokenQueue))
		{
			new Type(tokenQueue);
			if(see(tokenQueue, TokenType.IDENTIFIER))
			{
				result = true;
			}
		}
		tokenQueue.rewind();
		return result;
	}
}

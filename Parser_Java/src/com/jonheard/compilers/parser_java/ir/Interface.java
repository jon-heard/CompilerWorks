package com.jonheard.compilers.parser_java.ir;

import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Interface extends BaseIrType
{
	public Interface(
			RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		addChild(new List_Modifier(tokenQueue));
		mustBe(tokenQueue, TokenType._INTERFACE);
		addChild(new Identifier(tokenQueue));
		while(tokenQueue.poll().getType() != TokenType.CURL_BRACE_RIGHT) {}
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "'" +
				" modifiers='" + getModifiers().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 2; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	public Identifier getName()
	{
		return (Identifier)getChild(1);
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		boolean result = false;
		tokenQueue.remember();
		new List_Modifier(tokenQueue);
		result = see(tokenQueue, TokenType._INTERFACE);
		tokenQueue.rewind();
		return result;
	}
}

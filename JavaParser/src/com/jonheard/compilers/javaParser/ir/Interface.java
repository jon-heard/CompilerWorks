package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class Interface extends BaseIrType
{
	public Interface(
			RewindableQueue<JavaToken> tokenQueue)
	{
		addChild(new List_Modifier(tokenQueue));
		mustBe(tokenQueue, JavaTokenType._INTERFACE);
		addChild(new Identifier(tokenQueue));
		while(tokenQueue.poll().getType() != JavaTokenType.CURL_BRACE_RIGHT) {}
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
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._INTERFACE);
	}
}

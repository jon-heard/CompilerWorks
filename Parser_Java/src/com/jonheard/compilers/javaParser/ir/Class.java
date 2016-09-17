package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Class extends BaseIrType
{
	public Class(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		addChild(new List_Modifier(tokenQueue));
		mustBe(tokenQueue, JavaTokenType._CLASS);
		addChild(new Identifier(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.CURL_BRACE_LEFT);
		noEof(tokenQueue);
		while(!have(tokenQueue, JavaTokenType.CURL_BRACE_RIGHT))
		{
			if(Method.isNext(tokenQueue))
			{
				addChild(new Method(tokenQueue));
			}
			else
			{
				addChild(new Variable(tokenQueue));				
			}
		}
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
		boolean result = false;
		tokenQueue.remember();
		new List_Modifier(tokenQueue);
		result = see(tokenQueue, JavaTokenType._CLASS);
		tokenQueue.rewind();
		return result;
	}
}

package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Variable extends BaseIrType
{
	public Variable(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		addChild(new List_Modifier(tokenQueue));
		addChild(new Type(tokenQueue));
		addChild(new Identifier(tokenQueue));
		while(have(tokenQueue, JavaTokenType.SQUARE_BRACE_LEFT))
		{
			mustBe(tokenQueue, JavaTokenType.SQUARE_BRACE_RIGHT);
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
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		boolean result = false;
		tokenQueue.remember();
		new List_Modifier(tokenQueue);
		if(Type.isNext(tokenQueue))
		{
			new Type(tokenQueue);
			if(see(tokenQueue, JavaTokenType.IDENTIFIER))
			{
				result = true;
			}
		}
		tokenQueue.rewind();
		return result;
	}
}

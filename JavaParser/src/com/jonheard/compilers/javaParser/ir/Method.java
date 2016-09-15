package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import statement.CodeBlock;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Method extends BaseIrType
{
	public Method(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		addChild(new List_Modifier(tokenQueue));
		addChild(new Type(tokenQueue));
		addChild(new Identifier(tokenQueue));
		tokenQueue.poll();
		addChild(new List_Variable(tokenQueue));
		tokenQueue.poll();
		addChild(new CodeBlock(tokenQueue));
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "' " +
				"type='" + getType().getValue() + "' " +
				" modifiers='" + getModifiers().getValue();
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	
	public QualifiedIdentifier getType()
	{
		return (QualifiedIdentifier)getChild(1);
	}
	
	public Identifier getName()
	{
		return (Identifier)getChild(2);
	}
	
	public static boolean isNext(
			RewindableQueue<JavaToken> tokenQueue)
	{
		boolean result = false;
		tokenQueue.remember();
		new List_Modifier(tokenQueue);
		if(Type.isNext(tokenQueue))
		{
			new Type(tokenQueue);
			if(Identifier.isNext(tokenQueue))
			{
				new Identifier(tokenQueue);
				if(see(tokenQueue, JavaTokenType.PAREN_LEFT))
				{
					result = true;
				}
			}
		}
		tokenQueue.rewind();
		return result;
	}
}

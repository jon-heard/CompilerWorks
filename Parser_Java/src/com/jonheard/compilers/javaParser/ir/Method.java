package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.javaParser.JavaParser.*;

import com.jonheard.compilers.javaParser.ir.statement.CodeBlock;

public class Method extends BaseIrType
{
	public Method(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		addChild(new List_Modifier(tokenQueue));
		addChild(new Type(tokenQueue));
		addChild(new Identifier(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(new List_Variable(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(new CodeBlock(tokenQueue));
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
	
	public List_Variable getParameterList()
	{
		return (List_Variable)getChild(3);
	}
	
	public String toJvmDescriptor()
	{
		StringBuffer result = new StringBuffer();
		List_Variable params = getParameterList();
		
		result.append('(');
		for(int i = 0; i < params.getChildCount(); i++)
		{
			Type pType = ((Variable)params.getChild(i)).getType();
			result.append(pType.toJvmDescriptor());
		}
		result.append(')');
		result.append(getType().toJvmDescriptor());
		return result.toString();
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

package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class VariableDeclaration extends BaseType
{
	public VariableDeclaration(RewindableQueue<JavaToken> tokenQueue)
	{
		children.add(new QualifiedIdentifier(tokenQueue));
		while(tokenQueue.peek().getType() == JavaTokenType.SQUARE_BRACE_LEFT)
		{
			tokenQueue.poll();
			tokenQueue.poll();
			arrayDimensions++;
		}
		children.add(new Identifier(tokenQueue));
		while(tokenQueue.peek().getType() == JavaTokenType.SQUARE_BRACE_LEFT)
		{
			tokenQueue.poll();
			tokenQueue.poll();
			arrayDimensions++;
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName() + "' " +
				"type='" + getType() + "' " +
				"arrayDimensions='" + getArrayDimensions() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public String getName()
	{
		Identifier nameIdentifier = (Identifier)children.get(1);
		return nameIdentifier.getValue();
	}
	public String getType()
	{
		QualifiedIdentifier nameIdentifier =
				(QualifiedIdentifier)children.get(0);
		return nameIdentifier.getValue();
	}
	public int getArrayDimensions()
	{
		return arrayDimensions;
	}
	
	private int arrayDimensions = 0;
}

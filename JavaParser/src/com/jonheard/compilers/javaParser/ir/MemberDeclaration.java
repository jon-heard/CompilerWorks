package com.jonheard.compilers.javaParser.ir;

import java.util.Queue;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;

public class MemberDeclaration extends BaseType
{
	public MemberDeclaration(Queue<JavaToken> tokenQueue)
	{
		children.add(new List_Modifiers(tokenQueue));
		children.add(new QualifiedIdentifier(tokenQueue));
		children.add(new Identifier(tokenQueue));
		if(tokenQueue.peek().getType() == JavaTokenType.PAREN_LEFT)
		{
			isMethod = true;
			children.add(new MethodPart(tokenQueue));
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName() + "' " +
				"type='" + getType() + "' " +
				" modifiers='" + getModifiers().getHeaderString() + "' " +
				"isMethod='" + isMethod + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public List_Modifiers getModifiers()
	{
		return (List_Modifiers)children.get(0);
	}
	
	public String getType()
	{
		QualifiedIdentifier nameIdentifier =
				(QualifiedIdentifier)children.get(1);
		return nameIdentifier.getValue();
	}
	
	public String getName()
	{
		Identifier nameIdentifier = (Identifier)children.get(2);
		return nameIdentifier.getValue();
	}
	
	private boolean isMethod = false;
}

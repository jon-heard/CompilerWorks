package com.jonheard.compilers.javaParser.ir;

import java.util.Queue;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;

public class ClassDeclaration extends BaseType
{
	public ClassDeclaration(
			Queue<JavaToken> tokenQueue, List_Modifiers mods)
	{
		children.add(mods);
		tokenQueue.poll();
		children.add(new Identifier(tokenQueue));
		tokenQueue.poll();
		while(tokenQueue.peek().getType() != JavaTokenType.CURL_BRACE_RIGHT)
		{
			children.add(new MemberDeclaration(tokenQueue));
		}
		tokenQueue.poll();
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName() + "'" +
				" modifiers='" + getModifiers().getHeaderString() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 2; }
	
	public List_Modifiers getModifiers()
	{
		return (List_Modifiers)children.get(0);
	}
	public String getName()
	{
		Identifier nameIdentifier = (Identifier)children.get(1);
		return nameIdentifier.getValue();
	}
}

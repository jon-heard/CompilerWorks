package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class InterfaceDeclaration extends BaseType
{
	public InterfaceDeclaration(
			RewindableQueue<JavaToken> tokenQueue, List_Modifiers mods)
	{
		children.add(mods);
		tokenQueue.poll();
		children.add(new Identifier(tokenQueue));
		while(tokenQueue.poll().getType() != JavaTokenType.CURL_BRACE_RIGHT) {}
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

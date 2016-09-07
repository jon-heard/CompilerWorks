package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class PackageDeclaration extends BaseType
{
	PackageDeclaration(RewindableQueue<JavaToken> tokenQueue)
	{
		tokenQueue.poll();
		children.add(new QualifiedIdentifier(tokenQueue));
		tokenQueue.poll();
	}

	@Override
	public String getHeaderString()
	{
		return	"identifier='" + getIdentifier() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public String getIdentifier()
	{
		QualifiedIdentifier nameIdentifier =
				(QualifiedIdentifier)children.get(0);
		return nameIdentifier.getValue();
	}
}

package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

public class ImportDeclaration extends BaseType
{
	boolean isStatic = false;
	boolean isOnDemand = false;

	public ImportDeclaration(RewindableQueue<JavaToken> tokenQueue)
	{
		tokenQueue.poll();
		JavaToken next = tokenQueue.peek(); 
		if(next.getType() == JavaTokenType._STATIC)
		{
			isStatic = true;
			tokenQueue.poll();
			next = tokenQueue.peek();
		}
		QualifiedIdentifier identifier =
				new QualifiedIdentifier(tokenQueue, true);
		children.add(identifier);
		if(identifier.isEndedWithDot())
		{
			JavaToken ending = tokenQueue.peek();
			if(ending.getType() == JavaTokenType.STAR)
			{
				tokenQueue.poll();
				isOnDemand = true;
			}
			else
			{
				// error
			}
		}
		tokenQueue.poll();
	}

	@Override
	public String getHeaderString()
	{
		return	"isOnDemaned='" + isOnDemand + "' " +
				"isStatic='" + isStatic + "' " +
				"identifier='" + getIdentifier() + "'";
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

package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Import extends BaseIrType
{
	boolean isStatic = false;
	boolean isOnDemand = false;

	public Import(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		mustBe(tokenQueue, JavaTokenType._IMPORT);
		if(see(tokenQueue, JavaTokenType._STATIC))
		{
			isStatic = true;
			tokenQueue.poll();
		}
		QualifiedIdentifier identifier =
				new QualifiedIdentifier(tokenQueue, true);
		addChild(identifier);
		if(identifier.isEndedWithDot())
		{
			if(mustBe(tokenQueue, JavaTokenType.STAR))
			{
				isOnDemand = true;
			}
		}
		mustBe(tokenQueue, JavaTokenType.SEMICOLON);
	}

	@Override
	public String getHeaderString()
	{
		return	"isOnDemaned='" + isOnDemand + "' " +
				"isStatic='" + isStatic + "' " +
				"identifier='" + getIdentifier().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedIdentifier getIdentifier()
	{
		return (QualifiedIdentifier)getChild(0);
	}
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._IMPORT);
	}
}

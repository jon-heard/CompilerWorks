package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Package extends BaseIrType
{
	Package(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		tokenQueue.poll();
		addChild(new QualifiedIdentifier(tokenQueue));
		tokenQueue.poll();
	}

	@Override
	public String getHeaderString()
	{
		return	"identifier='" + getIdentifier().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedIdentifier getIdentifier()
	{
		return (QualifiedIdentifier)getChild(0);
	}
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._PACKAGE);
	}

}

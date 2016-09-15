package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Type extends BaseIrType
{
	public Type(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		addChild(new QualifiedIdentifier(tokenQueue));
		while(have(tokenQueue, JavaTokenType.SQUARE_BRACE_LEFT))
		{
			mustBe(tokenQueue, JavaTokenType.SQUARE_BRACE_RIGHT);
			incDimensionCount();
		}
	}
	
	@Override
	public String getHeaderString()
	{
		return	"value='" + getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public String getValue()
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < getDimensionCount(); i++)
		{
			result.append("[]");
		}
		return result.toString();
		
	}
	
	public void incDimensionCount()
	{
		dimensionCount++;
	}
	
	public QualifiedIdentifier getBase()
	{
		return (QualifiedIdentifier)getChild(0);
	}

	public int getDimensionCount() { return dimensionCount; }
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType.IDENTIFIER);
	}

	private int dimensionCount = 0;
}

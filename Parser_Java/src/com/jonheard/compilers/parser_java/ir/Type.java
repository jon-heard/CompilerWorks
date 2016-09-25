package com.jonheard.compilers.parser_java.ir;

import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Type extends BaseIrType
{
	public Type(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		addChild(new QualifiedIdentifier(tokenQueue));
		while(have(tokenQueue, TokenType.SQUARE_BRACE_LEFT))
		{
			mustBe(tokenQueue, TokenType.SQUARE_BRACE_RIGHT);
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
		result.append(getBase().getValue());
		for(int i = 0; i < getDimensionCount(); i++)
		{
			result.append("[]");
		}
		return result.toString();
		
	}
	
	public QualifiedIdentifier getBase()
	{
		return (QualifiedIdentifier)getChild(0);
	}

	public int getDimensionCount() { return dimensionCount; }
	
	public String toJvmDescriptor()
	{
		String type = getBase().getValue();
		int dimensionCount = getDimensionCount();
		StringBuffer result = new StringBuffer();

		for(int i = 0; i < dimensionCount; i++)
		{
			result.append("[");
		}
		if(type == "void") result.append("V");
		else if(type == "byte") result.append("B");
		else if(type == "char") result.append("C");
		else if(type == "double") result.append("D");
		else if(type == "float") result.append("F");
		else if(type == "int") result.append("I");
		else if(type == "long") result.append("J");
		else if(type == "short") result.append("S");
		else if(type == "boolean") result.append("Z");
		else
		{
			result.append("L");
			result.append(type.replace('.', '/'));
			result.append(";");
		}
		
		return result.toString();
	}

	public void incDimensionCount()
	{
		dimensionCount++;
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType.IDENTIFIER);
	}

	private int dimensionCount = 0;
}

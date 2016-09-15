package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Identifier extends BaseIrType
{
	private String value = "";
	
	public Identifier(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		JavaToken currentToken = tokenQueue.peek();
		if(mustBe(tokenQueue, JavaTokenType.IDENTIFIER))
		{
			value = currentToken.getText();
		}
		else
		{
			value = "";
		}
	}

	@Override
	public String getHeaderString()
	{
		return "value='" + getValue() + "'";
	}

	public String getValue()
	{
		return value;
	}
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType.IDENTIFIER);
	}
}

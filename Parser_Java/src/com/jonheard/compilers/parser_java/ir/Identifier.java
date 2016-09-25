package com.jonheard.compilers.parser_java.ir;

import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Identifier extends BaseIrType
{
	private String value = "";
	
	public Identifier(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		Token currentToken = tokenQueue.peek();
		if(mustBe(tokenQueue, TokenType.IDENTIFIER))
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
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType.IDENTIFIER);
	}
}

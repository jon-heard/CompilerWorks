package com.jonheard.compilers.parser_java.ir.statement;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class Empty extends BaseIrType
{
	public Empty(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType.SEMICOLON);
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType.SEMICOLON);
	}
}

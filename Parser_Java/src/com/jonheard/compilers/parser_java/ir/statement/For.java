package com.jonheard.compilers.parser_java.ir.statement;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class For extends BaseIrType
{
	public For(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType._FOR);
		mustBe(tokenQueue, TokenType.PAREN_LEFT);
		while(!have(tokenQueue, TokenType.PAREN_RIGHT))
		{
			tokenQueue.poll();
		}
		addChild(Parser_Statement.getNextStatement(tokenQueue));
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType._FOR);
	}
}

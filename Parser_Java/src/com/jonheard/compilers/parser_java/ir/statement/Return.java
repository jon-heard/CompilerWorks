package com.jonheard.compilers.parser_java.ir.statement;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class Return extends BaseIrType
{
	public Return(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType._RETURN);
		if(!see(tokenQueue, TokenType.SEMICOLON))
		{
			addChild(Parser_Expression.parseExpression(tokenQueue));
		}
		mustBe(tokenQueue, TokenType.SEMICOLON);
	}

	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType._RETURN);
	}
}

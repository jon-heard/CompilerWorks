package com.jonheard.compilers.parser_java.ir.statement;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class Switch extends BaseIrType
{
	public Switch(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType._SWITCH);
		mustBe(tokenQueue, TokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, TokenType.PAREN_RIGHT);
		addChild(new CodeBlock(tokenQueue));
	}

	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType._SWITCH);
	}
}

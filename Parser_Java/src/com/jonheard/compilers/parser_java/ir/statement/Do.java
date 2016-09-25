package com.jonheard.compilers.parser_java.ir.statement;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class Do extends BaseIrType
{
	public Do(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType._DO);
		BaseIrType body = Parser_Statement.getNextStatement(tokenQueue);
		mustBe(tokenQueue, TokenType._WHILE);
		mustBe(tokenQueue, TokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, TokenType.PAREN_RIGHT);
		mustBe(tokenQueue, TokenType.SEMICOLON);
		addChild(body);
	}

	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType._DO);
	}
}

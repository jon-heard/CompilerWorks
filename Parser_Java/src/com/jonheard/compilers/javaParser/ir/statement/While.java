package com.jonheard.compilers.javaParser.ir.statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaParser.ir.expression.Parser_Expression;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.javaParser.JavaParser.*;

public class While extends BaseIrType
{
	public While(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, JavaTokenType._WHILE);
		mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		addChild(Parser_Statement.getNextStatement(tokenQueue));
	}

	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._WHILE);
	}
}

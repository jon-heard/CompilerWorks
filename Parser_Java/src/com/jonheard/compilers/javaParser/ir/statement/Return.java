package com.jonheard.compilers.javaParser.ir.statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaParser.ir.expression.Parser_Expression;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;

import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Return extends BaseIrType
{
	public Return(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, JavaTokenType._RETURN);
		if(!see(tokenQueue, JavaTokenType.SEMICOLON))
		{
			addChild(Parser_Expression.parseExpression(tokenQueue));
		}
		mustBe(tokenQueue, JavaTokenType.SEMICOLON);
	}

	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._RETURN);
	}
}

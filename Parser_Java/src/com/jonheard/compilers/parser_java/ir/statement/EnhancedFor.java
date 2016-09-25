package com.jonheard.compilers.parser_java.ir.statement;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Variable;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class EnhancedFor extends BaseIrType
{
	public EnhancedFor(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType._FOR);
		mustBe(tokenQueue, TokenType.PAREN_LEFT);
		addChild(new Variable(tokenQueue));
		mustBe(tokenQueue, TokenType.COLON);
		addChild(Parser_Expression.parseExpression(tokenQueue));
		mustBe(tokenQueue, TokenType.PAREN_RIGHT);
		addChild(Parser_Statement.getNextStatement(tokenQueue));
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		boolean result = false;
		tokenQueue.remember();
		if(have(tokenQueue, TokenType._FOR))
		{
			mustBe(tokenQueue, TokenType.PAREN_LEFT);
			if(Variable.isNext(tokenQueue))
			{
				new Variable(tokenQueue);
			}
			if(see(tokenQueue, TokenType.COLON))
			{
				result = true;
			}
		}
		tokenQueue.rewind();
		return result;
	}
}

package com.jonheard.compilers.parser_java.ir.statement;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Variable;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class CodeBlock extends BaseIrType
{
	public CodeBlock(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, TokenType.CURL_BRACE_LEFT);
		while(!have(tokenQueue, TokenType.CURL_BRACE_RIGHT))
		{
			if(Variable.isNext(tokenQueue))
			{
				addChild(new Variable(tokenQueue));
			}
			else
			{
				addChild(Parser_Statement.getNextStatement(tokenQueue));
			}
		}
	}
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return see(tokenQueue, TokenType.CURL_BRACE_LEFT);
	}
}

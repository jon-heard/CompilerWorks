package com.jonheard.compilers.parser_java;

import java.util.List;

import com.jonheard.compilers.parser_java.ir.CompilationUnit;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;

public class JavaParser
{
	private List<Token> tokens;

	public JavaParser(List<Token> tokens)
	{
		this.tokens = tokens;
	}

	public CompilationUnit parse()
	{
		return new CompilationUnit(tokens, 0);
	}
	
	public String parseToString()
	{
		return parse().toString();
	}
	

	public static boolean see(
			RewindableQueue<Token> tokenQueue, TokenType type)
	{
		if(tokenQueue.isEmpty()) return false;
		return tokenQueue.peek().getType() == type;
	}
	
	public static boolean have(
			RewindableQueue<Token> tokenQueue, TokenType type)
	{
		if(tokenQueue.isEmpty()) return false;
		if(see(tokenQueue, type))
		{
			tokenQueue.poll();
			return true;
		}
		return false;
	}


	public static boolean mustBe(
			RewindableQueue<Token> tokenQueue, TokenType type)
	{
		noEof(tokenQueue);
		Token next = tokenQueue.peek();
		if(next.getType() == type)
		{
			mustBeHasErrored = false;
			tokenQueue.poll();
			return true;
		}
		else if(!mustBeHasErrored)
		{
			mustBeHasErrored = true;
			Logger.error(
				"'" + type.name() + "' expected",
				next.getFilename(), next.getRow(), next.getCol(),
				next.getLine());
			return false;
		}
		else
		{
			while(!tokenQueue.isEmpty() && !see(tokenQueue, type))
			{
				tokenQueue.poll();
			}
			if(have(tokenQueue, type))
			{
				mustBeHasErrored = false;
			}
			return true;
		}
	}
	
	public static boolean noEof(RewindableQueue<Token> tokenQueue)
	{
		if(tokenQueue.isEmpty())
		{
			Logger.error(
					"reached end of file while parsing",
					finalToken.getFilename(), finalToken.getRow(),
					finalToken.getCol(), finalToken.getLine());
			System.exit(0);
			return false;
		}
		else
		{
			return true;
		}
	}


	public static Token finalToken;

	private static boolean mustBeHasErrored = false;
}

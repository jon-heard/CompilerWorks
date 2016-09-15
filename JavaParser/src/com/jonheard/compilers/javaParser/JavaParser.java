package com.jonheard.compilers.javaParser;

import java.util.List;

import com.jonheard.compilers.javaParser.ir.CompilationUnit;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;

public class JavaParser
{
	private List<JavaToken> tokens;

	public JavaParser(List<JavaToken> tokens)
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
			RewindableQueue<JavaToken> tokenQueue, JavaTokenType type)
	{
		if(tokenQueue.isEmpty()) return false;
		return tokenQueue.peek().getType() == type;
	}
	
	public static boolean have(
			RewindableQueue<JavaToken> tokenQueue, JavaTokenType type)
	{
		if(tokenQueue.isEmpty()) return false;
		if(see(tokenQueue, type))
		{
			tokenQueue.poll();
		}
		return false;
	}


	public static boolean mustBe(
			RewindableQueue<JavaToken> tokenQueue, JavaTokenType type)
	{
		noEof(tokenQueue);
		JavaToken next = tokenQueue.peek();
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
	
	public static boolean noEof(RewindableQueue<JavaToken> tokenQueue)
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


	public static JavaToken finalToken;
}

package com.jonheard.compilers.parser_java;

import java.util.List;

import com.jonheard.compilers.parser_java.ir.CompilationUnit;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;
import com.jonheard.util.SourceFileInfo;

public class Parser_Java
{
	public CompilationUnit parse(SourceFileInfo source, List<Token> tokens)
	{
		this.source = source;
		tokenQueue = new RewindableQueue<Token>(tokens);
		return new CompilationUnit(this);
	}
	
	public SourceFileInfo getSource() { return source; }
	public RewindableQueue<Token> getTokenQueue() { return tokenQueue; }

	public int getCurrentLine()
	{
		return tokenQueue.peek().getRow();
	}

	public boolean see(TokenType type)
	{
		if(tokenQueue == null || type == null) return false;
		if(tokenQueue.isEmpty()) return false;
		return tokenQueue.peek().getType() == type;
	}
	
	public boolean have(TokenType type)
	{
		if(see(type))
		{
			tokenQueue.poll();
			return true;
		}
		return false;
	}

	public boolean mustBe(TokenType type)
	{
		if(tokenQueue.isEmpty())
		{
			int lastLineIndex = source.getLineCount() - 1;
			Logger.error(
					"reached end of file while parsing", source.getFilename(),
					0, lastLineIndex, source.getLine(lastLineIndex));
			System.exit(0);
			return false;
		}

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
				"'" + type.name() + "' expected", source.getFilename(),
				next.getRow(), next.getColumn(), source.getLine(next.getRow()));
			return false;
		}
		else
		{
			while(!tokenQueue.isEmpty() && !see(type))
			{
				tokenQueue.poll();
			}
			if(have(type))
			{
				mustBeHasErrored = false;
			}
			return true;
		}
	}

	private SourceFileInfo source;
	private RewindableQueue<Token> tokenQueue;
	private static boolean mustBeHasErrored = false;
}

package com.jonheard.compilers.javaParser.ir.statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Break extends BaseIrType
{
	public Break(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue);
		mustBe(tokenQueue, JavaTokenType._BREAK);
		mustBe(tokenQueue, JavaTokenType.SEMICOLON);
	}
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return see(tokenQueue, JavaTokenType._BREAK);
	}
}
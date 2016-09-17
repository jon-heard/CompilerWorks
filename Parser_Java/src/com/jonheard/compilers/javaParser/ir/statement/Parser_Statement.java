package com.jonheard.compilers.javaParser.ir.statement;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaParser.ir.expression.Parser_Expression;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class Parser_Statement
{
	public static BaseIrType getNextStatement(
			RewindableQueue<JavaToken> tokenQueue)
	{
		BaseIrType result = null;
		if(CodeBlock.isNext(tokenQueue)) { result = new CodeBlock(tokenQueue); } 
		else if(If.isNext(tokenQueue)) { result = new If(tokenQueue); }
		else if(While.isNext(tokenQueue)) { result = new While(tokenQueue); }
		else if(Do.isNext(tokenQueue)) { result = new Do(tokenQueue); }
		else if(Switch.isNext(tokenQueue)) { result = new Switch(tokenQueue); }
		else if(Return.isNext(tokenQueue)) { result = new Return(tokenQueue); }
		else if(Break.isNext(tokenQueue)) { result = new Break(tokenQueue); }
		else if(Empty.isNext(tokenQueue)) { result = new Empty(tokenQueue); }
		else if(For.isNext(tokenQueue))
		{
			if(EnhancedFor.isNext(tokenQueue))
			{
				result = new EnhancedFor(tokenQueue);
			}
			else
			{
				result = new For(tokenQueue);
			}
		}
		else
		{
			result = Parser_Expression.parseExpressionStatment(tokenQueue);
		}
		return result;
	}
}

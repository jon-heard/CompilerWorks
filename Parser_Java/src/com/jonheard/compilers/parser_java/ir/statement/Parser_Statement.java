package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;

public class Parser_Statement
{
	public static BaseIrType getNextStatement(Parser parser)
	{
		BaseIrType result = null;
		if(CodeBlock.isNext(parser)) { result = new CodeBlock(parser); } 
		else if(If.isNext(parser)) { result = new If(parser); }
		else if(While.isNext(parser)) { result = new While(parser); }
		else if(Switch.isNext(parser)) { result = new Switch(parser); }
		else if(For.isNext(parser))
		{
			if(EnhancedFor.isNext(parser))
			{
				result = new EnhancedFor(parser);
			}
			else
			{
				result = new For(parser);
			}
		}
		else if(Do.isNext(parser)) { result = new Do(parser); }
		else if(Return.isNext(parser)) { result = new Return(parser); }
		else if(Break.isNext(parser)) { result = new Break(parser); }
		else if(Empty.isNext(parser)) { result = new Empty(parser); }
		else
		{
			result = Parser_Expression.parseExpressionStatment(parser);
		}
		return result;
	}
}

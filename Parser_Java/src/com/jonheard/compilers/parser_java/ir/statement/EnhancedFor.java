package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.MethodOrVariable;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class EnhancedFor extends BaseIrType
{
	public EnhancedFor(Parser parser)
	{
		super(parser);
		parser.requireTokenToBeOfType(TokenType._FOR);
		parser.requireTokenToBeOfType(TokenType.PAREN_LEFT);
		addChild(new MethodOrVariable(parser));
		parser.requireTokenToBeOfType(TokenType.COLON);
		addChild(Parser_Expression.parseExpression(parser));
		parser.requireTokenToBeOfType(TokenType.PAREN_RIGHT);
		addChild(Parser_Statement.getNextStatement(parser));
	}
	
	public static boolean isNext(Parser parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		if(parser.passTokenIfType(TokenType._FOR))
		{
			parser.requireTokenToBeOfType(TokenType.PAREN_LEFT);
			if(MethodOrVariable.isNext(parser))
			{
				new MethodOrVariable(parser);
			}
			if(parser.getIsTokenType(TokenType.COLON))
			{
				result = true;
			}
		}
		parser.getTokenQueue().rewind();
		return result;
	}
}

package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class For extends BaseIrType
{
	public For(Parser parser)
	{
		super(parser);
		parser.requireTokenToBeOfType(TokenType._FOR);
		parser.requireTokenToBeOfType(TokenType.PAREN_LEFT);
		while(!parser.passTokenIfType(TokenType.PAREN_RIGHT))
		{
			parser.getTokenQueue().poll();
		}
		addChild(Parser_Statement.getNextStatement(parser));
	}
	
	public static boolean isNext(Parser parser)
	{
		return parser.getIsTokenType(TokenType._FOR);
	}
}

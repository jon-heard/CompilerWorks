package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class For extends BaseIrType
{
	public For(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType._FOR);
		parser.mustBe(TokenType.PAREN_LEFT);
		while(!parser.have(TokenType.PAREN_RIGHT))
		{
			parser.getTokenQueue().poll();
		}
		addChild(Parser_Statement.getNextStatement(parser));
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType._FOR);
	}
}

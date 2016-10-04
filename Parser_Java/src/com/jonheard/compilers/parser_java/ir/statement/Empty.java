package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Empty extends BaseIrType
{
	public Empty(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType.SEMICOLON);
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType.SEMICOLON);
	}
}

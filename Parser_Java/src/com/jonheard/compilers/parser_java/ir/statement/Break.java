package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Break extends BaseIrType
{
	public Break(Parser parser)
	{
		super(parser);
		parser.requireTokenToBeOfType(TokenType._BREAK);
		parser.requireTokenToBeOfType(TokenType.SEMICOLON);
	}
	
	public static boolean isNext(Parser parser)
	{
		return parser.getIsTokenType(TokenType._BREAK);
	}
}

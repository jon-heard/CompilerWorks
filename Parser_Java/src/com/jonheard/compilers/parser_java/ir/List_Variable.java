package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class List_Variable extends BaseIrType
{
	public List_Variable(Parser_Java parser)
	{
		super(parser);
		do
		{
			addChild(new MethodOrVariable(parser, true));
		}
		while(parser.have(TokenType.COMMA));
	}
}

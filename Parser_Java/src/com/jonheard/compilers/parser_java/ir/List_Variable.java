package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class List_Variable extends BaseIrType
{
	public List_Variable(Parser parser)
	{
		super(parser);
		do
		{
			addChild(new MethodOrVariable(parser, true, true, true, true));
		}
		while(parser.passTokenIfType(TokenType.COMMA));
	}
}

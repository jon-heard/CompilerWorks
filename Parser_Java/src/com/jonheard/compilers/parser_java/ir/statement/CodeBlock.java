package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Member;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class CodeBlock extends BaseIrType
{
	public CodeBlock(Parser parser)
	{
		super(parser);
		parser.requireTokenToBeOfType(TokenType.CURL_BRACE_LEFT);
		while(!parser.passTokenIfType(TokenType.CURL_BRACE_RIGHT))
		{
			if(Member.getIsNext(parser))
			{
				addChild(new Member(parser, true,true,false,false));
			}
			else
			{
				addChild(Parser_Statement.getNextStatement(parser));
			}
		}
	}
	
	public static boolean isNext(Parser parser)
	{
		return parser.getIsTokenType(TokenType.CURL_BRACE_LEFT);
	}
}

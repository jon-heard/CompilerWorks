package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.MethodOrVariable;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class CodeBlock extends BaseIrType
{
	public CodeBlock(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType.CURL_BRACE_LEFT);
		while(!parser.have(TokenType.CURL_BRACE_RIGHT))
		{
			if(MethodOrVariable.isNext(parser))
			{
				addChild(new MethodOrVariable(parser));
			}
			else
			{
				addChild(Parser_Statement.getNextStatement(parser));
			}
		}
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType.CURL_BRACE_LEFT);
	}
}

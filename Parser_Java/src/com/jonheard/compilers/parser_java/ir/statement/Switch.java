package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Switch extends BaseIrType
{
	public Switch(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType._SWITCH);
		parser.mustBe(TokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(parser));
		parser.mustBe(TokenType.PAREN_RIGHT);
		addChild(new CodeBlock(parser));
	}

	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType._SWITCH);
	}
}

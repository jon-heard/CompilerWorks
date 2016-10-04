package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Do extends BaseIrType
{
	public Do(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType._DO);
		BaseIrType body = Parser_Statement.getNextStatement(parser);
		parser.mustBe(TokenType._WHILE);
		parser.mustBe(TokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(parser));
		parser.mustBe(TokenType.PAREN_RIGHT);
		parser.mustBe(TokenType.SEMICOLON);
		addChild(body);
	}

	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType._DO);
	}
}

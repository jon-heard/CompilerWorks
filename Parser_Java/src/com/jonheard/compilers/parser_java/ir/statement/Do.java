package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Do extends BaseIrType
{
	public Do(Parser parser)
	{
		super(parser);
		parser.requireTokenToBeOfType(TokenType._DO);
		BaseIrType body = Parser_Statement.getNextStatement(parser);
		parser.requireTokenToBeOfType(TokenType._WHILE);
		parser.requireTokenToBeOfType(TokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(parser));
		parser.requireTokenToBeOfType(TokenType.PAREN_RIGHT);
		parser.requireTokenToBeOfType(TokenType.SEMICOLON);
		addChild(body);
	}

	public static boolean isNext(Parser parser)
	{
		return parser.getIsTokenType(TokenType._DO);
	}
}

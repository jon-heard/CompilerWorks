package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class While extends BaseIrType
{
	public While(Parser parser)
	{
		super(parser);
		parser.requireTokenToBeOfType(TokenType._WHILE);
		parser.requireTokenToBeOfType(TokenType.PAREN_LEFT);
		addChild(Parser_Expression.parseExpression(parser));
		parser.requireTokenToBeOfType(TokenType.PAREN_RIGHT);
		addChild(Parser_Statement.getNextStatement(parser));
	}

	public static boolean isNext(Parser parser)
	{
		return parser.getIsTokenType(TokenType._WHILE);
	}
}

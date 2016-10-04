package com.jonheard.compilers.parser_java.ir.statement;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Variable;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class EnhancedFor extends BaseIrType
{
	public EnhancedFor(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType._FOR);
		parser.mustBe(TokenType.PAREN_LEFT);
		addChild(new Variable(parser));
		parser.mustBe(TokenType.COLON);
		addChild(Parser_Expression.parseExpression(parser));
		parser.mustBe(TokenType.PAREN_RIGHT);
		addChild(Parser_Statement.getNextStatement(parser));
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		if(parser.have(TokenType._FOR))
		{
			parser.mustBe(TokenType.PAREN_LEFT);
			if(Variable.isNext(parser))
			{
				new Variable(parser);
			}
			if(parser.see(TokenType.COLON))
			{
				result = true;
			}
		}
		parser.getTokenQueue().rewind();
		return result;
	}
}

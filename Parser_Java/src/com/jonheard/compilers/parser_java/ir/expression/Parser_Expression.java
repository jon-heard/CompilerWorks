package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;

public class Parser_Expression
{
	public static BaseIrType parseExpressionStatment(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression result = parseExpression(parser);
		switch(result.getExpressionType())
		{
			case PRE_INCREMENT:
			case PRE_DECREMENT:
			case POST_INCREMENT:
			case POST_DECREMENT:
			case REFERENCE:
			case SUPER_CONSTRUCTOR:
			case THIS_CONSTRUCTOR:
			case NEW_OBJECT:
			case NEW_ARRAY:
			break;
			default:
				if(result.getExpressionType() == ExpressionType.REFERENCE)
				{
					if(((Reference)result).isMethodCall())
					{
						break;
					}
				}
				Logger.error("not a statement",
						parser.getSource().getFilename(), next.getRow(),
						next.getColumn(),
						parser.getSource().getRowText(next.getRow()));
				break;				
		}
		parser.requireTokenToBeOfType(TokenType.SEMICOLON);
		return result;
	}
	
	public static Expression parseExpression(Parser parser)
	{
		return tryAssignment(parser);
	}

	private static Expression tryAssignment(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryConditional(parser);
		if(parser.passTokenIfType(TokenType.EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT, next,
					lhs, tryAssignment(parser));
		if(parser.passTokenIfType(TokenType.PLUS_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__ADD, next,
					lhs, tryAssignment(parser));
		if(parser.passTokenIfType(TokenType.DASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__SUB, next,
					lhs, tryAssignment(parser));
		if(parser.passTokenIfType(TokenType.STAR_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__MUL, next,
					lhs, tryAssignment(parser));
		if(parser.passTokenIfType(TokenType.SLASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__DIV, next,
					lhs, tryAssignment(parser));
		if(parser.passTokenIfType(TokenType.PERCENT_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__MOD, next,
					lhs, tryAssignment(parser));
		else
			return lhs;
	}
	
	private static Expression tryConditional(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryLogical(parser);
		if(parser.passTokenIfType(TokenType.QUESTION))
		{
			Expression consequent = parseExpression(parser);
			parser.requireTokenToBeOfType(TokenType.COLON);
			Expression alternative = parseExpression(parser);
			lhs = new Expression(
					ExpressionType.CONDITIONAL, next,
					lhs, consequent, alternative);
		}
		return lhs;
	}

	private static Expression tryLogical(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryEquality(parser);
		boolean more = true;
		while(more)
		{
			if(parser.passTokenIfType(TokenType.AND))
				lhs = new Expression(
						ExpressionType.LOGICAL_AND, next,
						lhs, tryEquality(parser));
			else if(parser.passTokenIfType(TokenType.PIPE))
				lhs = new Expression(
						ExpressionType.LOGICAL_OR, next,
						lhs, tryEquality(parser));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryEquality(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryRelational(parser);
		boolean more = true;
		while(more)
		{
			if(parser.passTokenIfType(TokenType.EQUAL_EQUAL))
				lhs = new Expression(
						ExpressionType.EQUALITY, next,
						lhs, tryRelational(parser));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryRelational(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryAdditive(parser);
		if(parser.passTokenIfType(TokenType.RIGHT_TRI))
			lhs = new Expression(
					ExpressionType.GREATER, next,
					lhs, tryAdditive(parser));
		if(parser.passTokenIfType(TokenType.LEFT_TRI))
			lhs = new Expression(
					ExpressionType.LESS, next,
					lhs, tryAdditive(parser));
		if(parser.passTokenIfType(TokenType.RIGHT_TRI_EQUAL))
			lhs = new Expression(
					ExpressionType.GREATER_OR_EQUAL, next,
					lhs, tryAdditive(parser));
		if(parser.passTokenIfType(TokenType.LEFT_TRI_EQUAL))
			lhs = new Expression(
					ExpressionType.LESS_OR_EQUAL, next,
					lhs, tryAdditive(parser));
		return lhs;
	}
	
	private static Expression tryAdditive(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryMultiplicative(parser);
		boolean more = true;
		while(more)
		{
			if(parser.passTokenIfType(TokenType.PLUS))
				lhs = new Expression(
						ExpressionType.ADD, next,
						lhs, tryMultiplicative(parser));
			else if(parser.passTokenIfType(TokenType.DASH))
				lhs = new Expression(
						ExpressionType.SUB, next,
						lhs, tryMultiplicative(parser));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryMultiplicative(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryUnary(parser);
		boolean more = true;
		while(more)
		{
			if(parser.passTokenIfType(TokenType.STAR))
				lhs = new Expression(
						ExpressionType.MUL, next,
						lhs, tryUnary(parser));
			else if(parser.passTokenIfType(TokenType.SLASH))
				lhs = new Expression(
						ExpressionType.DIV, next,
						lhs, tryUnary(parser));
			else if(parser.passTokenIfType(TokenType.PERCENT))
				lhs = new Expression(
						ExpressionType.MOD, next,
						lhs, tryUnary(parser));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryUnary(Parser parser)
	{
		Token next = parser.peekNextToken();
		if(parser.passTokenIfType(TokenType.PLUS_PLUS))
			return new Expression(
					ExpressionType.PRE_INCREMENT, next,
					tryUnary(parser));
		else if(parser.passTokenIfType(TokenType.DASH_DASH))
			return new Expression(
					ExpressionType.PRE_DECREMENT, next,
					tryUnary(parser));
		else if(parser.passTokenIfType(TokenType.PLUS))
			return new Expression(
					ExpressionType.POSITIVE, next,
					tryUnary(parser));
		else if(parser.passTokenIfType(TokenType.DASH))
			return new Expression(
					ExpressionType.NEGATIVE, next,
					tryUnary(parser));
		else
			return trySimpleUnary(parser);
	}
	
	private static Expression trySimpleUnary(Parser parser)
	{
		Token next = parser.peekNextToken();
		if(parser.passTokenIfType(TokenType.EXCLAIM))
			return new Expression(
					ExpressionType.LOGICAL_NOT, next,
					tryUnary(parser));
		else if(seeCast(parser))
		{
			parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
			QualifiedId type = new QualifiedId(parser);
			parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
			return new Cast(next, type, parseExpression(parser));
		}
		else
			return tryPostFix(parser);
	}
	
	private static Expression tryPostFix(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression lhs = tryPrimary(parser);
		boolean more = true;
		while(more)
		{
			if(parser.passTokenIfType(TokenType.PLUS_PLUS))
				lhs = new Expression(ExpressionType.POST_INCREMENT, next, lhs);
			if(parser.passTokenIfType(TokenType.DASH_DASH))
				lhs = new Expression(ExpressionType.POST_DECREMENT, next, lhs);
			//TODO: add dot and lbrack stuff (line 1506) 
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryPrimary(Parser parser)
	{
		Token next = parser.peekNextToken();
		Expression result = null;
		// Parenthesized expression
		if(parser.passTokenIfType(TokenType.LEFT_PAREN))
		{
			result = parseExpression(parser);
			parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
		}
		// this
		else if(parser.passTokenIfType(TokenType._THIS))
		{
			if(parser.passTokenIfType(TokenType.LEFT_PAREN))
			{
				result = new ThisConstructor(
						next, new List_Expression(parser));
				parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
			}
			else
			{
				result = new Expression(ExpressionType.THIS_REFERENCE, next);
			}
		}
		// super
		else if(parser.passTokenIfType(TokenType._SUPER))
		{
			if(parser.passTokenIfType(TokenType.DOT))
			{
				//TODO: Fill in logic for "Super." expression
			}
			else
			{
				parser.requireTokenToBeOfType(TokenType.LEFT_PAREN);
				result = new SuperConstructor(
						next, new List_Expression(parser));
				parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
			}
		}
		// new
		else if(parser.passTokenIfType(TokenType._NEW))
		{
			//TODO: Fill in logic for "New" expression
		}
		// id
		else if(QualifiedId.getIsNext(parser))
		{
			QualifiedId id = new QualifiedId(parser);
			if(parser.passTokenIfType(TokenType.LEFT_PAREN))
			{
				result = new Reference(next, id, new List_Expression(parser));
				parser.requireTokenToBeOfType(TokenType.RIGHT_PAREN);
			}
			else
			{
				result = new Reference(next, id);
			}
		}
		// literal
		else
		{
			Token literalToken = parser.getTokenQueue().poll();
			TokenType literalTokenType = literalToken.getType();
			if(		literalTokenType != TokenType.INTEGER &&
					literalTokenType != TokenType.LONG &&
					literalTokenType != TokenType.FLOAT &&
					literalTokenType != TokenType.DOUBLE &&
					literalTokenType != TokenType.CHAR &&
					literalTokenType != TokenType.STRING &&
					literalTokenType != TokenType._TRUE &&
					literalTokenType != TokenType._FALSE &&
					literalTokenType != TokenType._NULL)
			{
				Logger.error(
						"illegal start of expression",
						parser.getSource().getFilename(), literalToken.getRow(),
						literalToken.getColumn(),
						parser.getSource().getRowText(literalToken.getRow()));
			}
			result = new Literal(next, literalToken);
		}
		return result;
	}

	
	private static boolean seeCast(Parser parser)
	{
		//TODO: fill with cast checking
		return false;
	}
}

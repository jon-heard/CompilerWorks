package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Id;
import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;

public class Parser_Expression
{
	public static BaseIrType parseExpressionStatment(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression result = parseExpression(parser);
		switch(result.getType())
		{
			case PRE_INCREMENT:
			case PRE_DECREMENT:
			case POST_INCREMENT:
			case POST_DECREMENT:
			case METHOD_CALL:
			case SUPER_CONSTRUCTOR:
			case THIS_CONSTRUCTOR:
			case NEW_OBJECT:
			case NEW_ARRAY:
			break;
			default:
				Logger.error("not a statement",
						parser.getSource().getFilename(), next.getLine(),
						next.getColumn(),
						parser.getSource().getLine(next.getLine()));
				break;				
		}
		parser.mustBe(TokenType.SEMICOLON);
		return result;
	}
	
	public static Expression parseExpression(Parser_Java parser)
	{
		return tryAssignment(parser);
	}

	private static Expression tryAssignment(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryConditional(parser);
		if(parser.have(TokenType.EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT, next,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.PLUS_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__ADD, next,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.DASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__SUB, next,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.STAR_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__MUL, next,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.SLASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__DIV, next,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.PERCENT_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__MOD, next,
					lhs, tryAssignment(parser));
		else
			return lhs;
	}
	
	private static Expression tryConditional(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryLogical(parser);
		if(parser.have(TokenType.QUESTION))
		{
			Expression consequent = parseExpression(parser);
			parser.mustBe(TokenType.COLON);
			Expression alternative = parseExpression(parser);
			lhs = new Expression(
					ExpressionType.CONDITIONAL, next,
					lhs, consequent, alternative);
		}
		return lhs;
	}

	private static Expression tryLogical(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryEquality(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.AND))
				lhs = new Expression(
						ExpressionType.LOGICAL_AND, next,
						lhs, tryEquality(parser));
			else if(parser.have(TokenType.PIPE))
				lhs = new Expression(
						ExpressionType.LOGICAL_OR, next,
						lhs, tryEquality(parser));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryEquality(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryRelational(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.EQUAL_EQUAL))
				lhs = new Expression(
						ExpressionType.EQUALITY, next,
						lhs, tryRelational(parser));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryRelational(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryAdditive(parser);
		if(parser.have(TokenType.RIGHT))
			lhs = new Expression(
					ExpressionType.GREATER, next,
					lhs, tryAdditive(parser));
		if(parser.have(TokenType.LEFT))
			lhs = new Expression(
					ExpressionType.LESS, next,
					lhs, tryAdditive(parser));
		if(parser.have(TokenType.RIGHT_EQUAL))
			lhs = new Expression(
					ExpressionType.GREATER_OR_EQUAL, next,
					lhs, tryAdditive(parser));
		if(parser.have(TokenType.LEFT_EQUAL))
			lhs = new Expression(
					ExpressionType.LESS_OR_EQUAL, next,
					lhs, tryAdditive(parser));
		return lhs;
	}
	
	private static Expression tryAdditive(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryMultiplicative(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.PLUS))
				lhs = new Expression(
						ExpressionType.ADD, next,
						lhs, tryMultiplicative(parser));
			else if(parser.have(TokenType.DASH))
				lhs = new Expression(
						ExpressionType.SUB, next,
						lhs, tryMultiplicative(parser));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryMultiplicative(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryUnary(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.STAR))
				lhs = new Expression(
						ExpressionType.MUL, next,
						lhs, tryUnary(parser));
			else if(parser.have(TokenType.SLASH))
				lhs = new Expression(
						ExpressionType.DIV, next,
						lhs, tryUnary(parser));
			else if(parser.have(TokenType.PERCENT))
				lhs = new Expression(
						ExpressionType.MOD, next,
						lhs, tryUnary(parser));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryUnary(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		if(parser.have(TokenType.PLUS_PLUS))
			return new Expression(
					ExpressionType.PRE_INCREMENT, next,
					tryUnary(parser));
		else if(parser.have(TokenType.DASH_DASH))
			return new Expression(
					ExpressionType.PRE_DECREMENT, next,
					tryUnary(parser));
		else if(parser.have(TokenType.PLUS))
			return new Expression(
					ExpressionType.POSITIVE, next,
					tryUnary(parser));
		else if(parser.have(TokenType.DASH))
			return new Expression(
					ExpressionType.NEGATIVE, next,
					tryUnary(parser));
		else
			return trySimpleUnary(parser);
	}
	
	private static Expression trySimpleUnary(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		if(parser.have(TokenType.EXCLAIM))
			return new Expression(
					ExpressionType.LOGICAL_NOT, next,
					tryUnary(parser));
		else if(seeCast(parser))
		{
			parser.mustBe(TokenType.PAREN_LEFT);
			QualifiedId type = new QualifiedId(parser);
			parser.mustBe(TokenType.PAREN_RIGHT);
			return new Cast(next, type, parseExpression(parser));
		}
		else
			return tryPostFix(parser);
	}
	
	private static Expression tryPostFix(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression lhs = tryPrimary(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.PLUS_PLUS))
				lhs = new Expression(ExpressionType.POST_INCREMENT, next, lhs);
			if(parser.have(TokenType.DASH_DASH))
				lhs = new Expression(ExpressionType.POST_DECREMENT, next, lhs);
			//TODO: add dot and lbrack stuff (line 1506) 
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryPrimary(Parser_Java parser)
	{
		Token next = parser.getNextToken();
		Expression result = null;
		// Parenthesized expression
		if(parser.have(TokenType.PAREN_LEFT))
		{
			result = parseExpression(parser);
			parser.mustBe(TokenType.PAREN_RIGHT);
		}
		// this
		else if(parser.have(TokenType._THIS))
		{
			if(parser.have(TokenType.PAREN_LEFT))
			{
				result = new ThisConstructor(
						next, new List_Expression(parser));
				parser.mustBe(TokenType.PAREN_RIGHT);
			}
			else
			{
				result = new Expression(ExpressionType.THIS_REFERENCE, next);
			}
		}
		// super
		else if(parser.have(TokenType._SUPER))
		{
			if(parser.have(TokenType.DOT))
			{
				//TODO: Fill in logic for "Super." expression
			}
			else
			{
				parser.mustBe(TokenType.PAREN_LEFT);
				result = new SuperConstructor(
						next, new List_Expression(parser));
				parser.mustBe(TokenType.PAREN_RIGHT);
			}
		}
		// new
		else if(parser.have(TokenType._NEW))
		{
			//TODO: Fill in logic for "New" expression
		}
		// id
		else if(QualifiedId.isNext(parser))
		{
			QualifiedId id = new QualifiedId(parser);
			if(parser.have(TokenType.PAREN_LEFT))
			{
				result = new MethodCall(
						next, id, new List_Expression(parser));
				parser.mustBe(TokenType.PAREN_RIGHT);
			}
			else if(id.getChildCount() == 1)
			{
				result =new VariableReference(next, (Id)id.getChild(0));
			}
			else
			{
				result = new FieldReference(next, id);
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
						parser.getSource().getFilename(), literalToken.getLine(),
						literalToken.getColumn(),
						parser.getSource().getLine(literalToken.getLine()));
			}
			result = new Literal(next, literalToken);
		}
		return result;
	}

	
	private static boolean seeCast(Parser_Java parser)
	{
		//TODO: fill with cast checking
		return false;
	}
}

package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Identifier;
import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedIdentifier;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;

public class Parser_Expression
{
	public static BaseIrType parseExpressionStatment(Parser_Java parser)
	{
		Token next = parser.getTokenQueue().peek();
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
						parser.getSource().getFilename(), next.getRow(),
						next.getColumn(),
						parser.getSource().getLine(next.getRow()));
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
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryConditional(parser);
		if(parser.have(TokenType.EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT, line,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.PLUS_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__ADD, line,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.DASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__SUB, line,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.STAR_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__MUL, line,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.SLASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__DIV, line,
					lhs, tryAssignment(parser));
		if(parser.have(TokenType.PERCENT_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__MOD, line,
					lhs, tryAssignment(parser));
		else
			return lhs;
	}
	
	private static Expression tryConditional(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryLogical(parser);
		if(parser.have(TokenType.QUESTION))
		{
			Expression consequent = parseExpression(parser);
			parser.mustBe(TokenType.COLON);
			Expression alternative = parseExpression(parser);
			lhs = new Expression(
					ExpressionType.CONDITIONAL, line,
					lhs, consequent, alternative);
		}
		return lhs;
	}

	private static Expression tryLogical(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryEquality(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.AND))
				lhs = new Expression(
						ExpressionType.LOGICAL_AND, line,
						lhs, tryEquality(parser));
			else if(parser.have(TokenType.PIPE))
				lhs = new Expression(
						ExpressionType.LOGICAL_OR, line,
						lhs, tryEquality(parser));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryEquality(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryRelational(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.EQUAL_EQUAL))
				lhs = new Expression(
						ExpressionType.EQUALITY, line,
						lhs, tryRelational(parser));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryRelational(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryAdditive(parser);
		if(parser.have(TokenType.RIGHT))
			lhs = new Expression(
					ExpressionType.GREATER, line,
					lhs, tryAdditive(parser));
		if(parser.have(TokenType.LEFT))
			lhs = new Expression(
					ExpressionType.LESS, line,
					lhs, tryAdditive(parser));
		if(parser.have(TokenType.RIGHT_EQUAL))
			lhs = new Expression(
					ExpressionType.GREATER_OR_EQUAL, line,
					lhs, tryAdditive(parser));
		if(parser.have(TokenType.LEFT_EQUAL))
			lhs = new Expression(
					ExpressionType.LESS_OR_EQUAL, line,
					lhs, tryAdditive(parser));
		return lhs;
	}
	
	private static Expression tryAdditive(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryMultiplicative(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.PLUS))
				lhs = new Expression(
						ExpressionType.ADD, line,
						lhs, tryMultiplicative(parser));
			else if(parser.have(TokenType.DASH))
				lhs = new Expression(
						ExpressionType.SUB, line,
						lhs, tryMultiplicative(parser));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryMultiplicative(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryUnary(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.STAR))
				lhs = new Expression(
						ExpressionType.MUL, line,
						lhs, tryUnary(parser));
			else if(parser.have(TokenType.SLASH))
				lhs = new Expression(
						ExpressionType.DIV, line,
						lhs, tryUnary(parser));
			else if(parser.have(TokenType.PERCENT))
				lhs = new Expression(
						ExpressionType.MOD, line,
						lhs, tryUnary(parser));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryUnary(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		if(parser.have(TokenType.PLUS_PLUS))
			return new Expression(
					ExpressionType.PRE_INCREMENT, line,
					tryUnary(parser));
		else if(parser.have(TokenType.DASH_DASH))
			return new Expression(
					ExpressionType.PRE_DECREMENT, line,
					tryUnary(parser));
		else if(parser.have(TokenType.PLUS))
			return new Expression(
					ExpressionType.POSITIVE, line,
					tryUnary(parser));
		else if(parser.have(TokenType.DASH))
			return new Expression(
					ExpressionType.NEGATIVE, line,
					tryUnary(parser));
		else
			return trySimpleUnary(parser);
	}
	
	private static Expression trySimpleUnary(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		if(parser.have(TokenType.EXCLAIM))
			return new Expression(
					ExpressionType.LOGICAL_NOT, line,
					tryUnary(parser));
		else if(seeCast(parser))
		{
			parser.mustBe(TokenType.PAREN_LEFT);
			QualifiedIdentifier type = new QualifiedIdentifier(parser);
			parser.mustBe(TokenType.PAREN_RIGHT);
			return new Cast(line, type, parseExpression(parser));
		}
		else
			return tryPostFix(parser);
	}
	
	private static Expression tryPostFix(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
		Expression lhs = tryPrimary(parser);
		boolean more = true;
		while(more)
		{
			if(parser.have(TokenType.PLUS_PLUS))
				lhs = new Expression(ExpressionType.POST_INCREMENT, line, lhs);
			if(parser.have(TokenType.DASH_DASH))
				lhs = new Expression(ExpressionType.POST_DECREMENT, line, lhs);
			//TODO: add dot and lbrack stuff (line 1506) 
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryPrimary(Parser_Java parser)
	{
		int line = parser.getTokenQueue().peek().getRow();
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
						line, new List_Expression(parser));
				parser.mustBe(TokenType.PAREN_RIGHT);
			}
			else
			{
				result = new Expression(ExpressionType.THIS_REFERENCE, line);
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
						line, new List_Expression(parser));
				parser.mustBe(TokenType.PAREN_RIGHT);
			}
		}
		// new
		else if(parser.have(TokenType._NEW))
		{
			//TODO: Fill in logic for "New" expression
		}
		// identifier
		else if(QualifiedIdentifier.isNext(parser))
		{
			QualifiedIdentifier id = new QualifiedIdentifier(parser);
			if(parser.have(TokenType.PAREN_LEFT))
			{
				result = new MethodCall(
						line, id, new List_Expression(parser));
				parser.mustBe(TokenType.PAREN_RIGHT);
			}
			else if(id.getChildCount() == 1)
			{
				result = new VariableReference(line, (Identifier)id.getChild(0));
			}
			else
			{
				result = new FieldReference(line, id);
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
						parser.getSource().getLine(literalToken.getRow()));
			}
			result = new Literal(line, literalToken);
		}
		return result;
	}

	
	private static boolean seeCast(Parser_Java parser)
	{
		//TODO: fill with cast checking
		return false;
	}
}

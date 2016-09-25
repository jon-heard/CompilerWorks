package com.jonheard.compilers.parser_java.ir.expression;

import static com.jonheard.compilers.parser_java.JavaParser.*;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.parser_java.ir.Identifier;
import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedIdentifier;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;

public class Parser_Expression
{
	public static BaseIrType parseExpressionStatment(
			RewindableQueue<Token> tokenQueue)
	{
		Token next = tokenQueue.peek();
		Expression result = parseExpression(tokenQueue);
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
						next.getFilename(), next.getRow(), next.getCol(),
						next.getLine());
				break;				
		}
		mustBe(tokenQueue, TokenType.SEMICOLON);
		return result;
	}
	
	public static Expression parseExpression(
			RewindableQueue<Token> tokenQueue)
	{
		return tryAssignment(tokenQueue);
	}

	private static Expression tryAssignment(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryConditional(tokenQueue);
		if(have(tokenQueue, TokenType.EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, TokenType.PLUS_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__ADD, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, TokenType.DASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__SUB, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, TokenType.STAR_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__MUL, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, TokenType.SLASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__DIV, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, TokenType.PERCENT_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__MOD, line,
					lhs, tryAssignment(tokenQueue));
		else
			return lhs;
	}
	
	private static Expression tryConditional(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryLogical(tokenQueue);
		if(have(tokenQueue, TokenType.QUESTION))
		{
			Expression consequent = parseExpression(tokenQueue);
			mustBe(tokenQueue, TokenType.COLON);
			Expression alternative = parseExpression(tokenQueue);
			lhs = new Expression(
					ExpressionType.CONDITIONAL, line,
					lhs, consequent, alternative);
		}
		return lhs;
	}

	private static Expression tryLogical(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryEquality(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, TokenType.AND))
				lhs = new Expression(
						ExpressionType.LOGICAL_AND, line,
						lhs, tryEquality(tokenQueue));
			else if(have(tokenQueue, TokenType.PIPE))
				lhs = new Expression(
						ExpressionType.LOGICAL_OR, line,
						lhs, tryEquality(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryEquality(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryRelational(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, TokenType.EQUAL_EQUAL))
				lhs = new Expression(
						ExpressionType.EQUALITY, line,
						lhs, tryRelational(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryRelational(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryAdditive(tokenQueue);
		if(have(tokenQueue, TokenType.RIGHT))
			lhs = new Expression(
					ExpressionType.GREATER, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, TokenType.LEFT))
			lhs = new Expression(
					ExpressionType.LESS, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, TokenType.RIGHT_EQUAL))
			lhs = new Expression(
					ExpressionType.GREATER_OR_EQUAL, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, TokenType.LEFT_EQUAL))
			lhs = new Expression(
					ExpressionType.LESS_OR_EQUAL, line,
					lhs, tryAdditive(tokenQueue));
		return lhs;
	}
	
	private static Expression tryAdditive(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryMultiplicative(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, TokenType.PLUS))
				lhs = new Expression(
						ExpressionType.ADD, line,
						lhs, tryMultiplicative(tokenQueue));
			else if(have(tokenQueue, TokenType.DASH))
				lhs = new Expression(
						ExpressionType.SUB, line,
						lhs, tryMultiplicative(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryMultiplicative(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryUnary(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, TokenType.STAR))
				lhs = new Expression(
						ExpressionType.MUL, line,
						lhs, tryUnary(tokenQueue));
			else if(have(tokenQueue, TokenType.SLASH))
				lhs = new Expression(
						ExpressionType.DIV, line,
						lhs, tryUnary(tokenQueue));
			else if(have(tokenQueue, TokenType.PERCENT))
				lhs = new Expression(
						ExpressionType.MOD, line,
						lhs, tryUnary(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryUnary(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		if(have(tokenQueue, TokenType.PLUS_PLUS))
			return new Expression(
					ExpressionType.PRE_INCREMENT, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, TokenType.DASH_DASH))
			return new Expression(
					ExpressionType.PRE_DECREMENT, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, TokenType.PLUS))
			return new Expression(
					ExpressionType.POSITIVE, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, TokenType.DASH))
			return new Expression(
					ExpressionType.NEGATIVE, line,
					tryUnary(tokenQueue));
		else
			return trySimpleUnary(tokenQueue);
	}
	
	private static Expression trySimpleUnary(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		if(have(tokenQueue, TokenType.EXCLAIM))
			return new Expression(
					ExpressionType.LOGICAL_NOT, line,
					tryUnary(tokenQueue));
		else if(seeCast(tokenQueue))
		{
			mustBe(tokenQueue, TokenType.PAREN_LEFT);
			QualifiedIdentifier type = new QualifiedIdentifier(tokenQueue);
			mustBe(tokenQueue, TokenType.PAREN_RIGHT);
			return new Cast(line, type, parseExpression(tokenQueue));
		}
		else
			return tryPostFix(tokenQueue);
	}
	
	private static Expression tryPostFix(
			RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryPrimary(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, TokenType.PLUS_PLUS))
				lhs = new Expression(ExpressionType.POST_INCREMENT, line, lhs);
			if(have(tokenQueue, TokenType.DASH_DASH))
				lhs = new Expression(ExpressionType.POST_DECREMENT, line, lhs);
			//TODO: add dot and lbrack stuff (line 1506) 
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryPrimary(RewindableQueue<Token> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression result = null;
		// Parenthesized expression
		if(have(tokenQueue, TokenType.PAREN_LEFT))
		{
			result = parseExpression(tokenQueue);
			mustBe(tokenQueue, TokenType.PAREN_RIGHT);
		}
		// this
		else if(have(tokenQueue, TokenType._THIS))
		{
			if(have(tokenQueue, TokenType.PAREN_LEFT))
			{
				result = new ThisConstructor(
						line, new List_Expression(tokenQueue));
				mustBe(tokenQueue, TokenType.PAREN_RIGHT);
			}
			else
			{
				result = new Expression(ExpressionType.THIS_REFERENCE, line);
			}
		}
		// super
		else if(have(tokenQueue, TokenType._SUPER))
		{
			if(have(tokenQueue, TokenType.DOT))
			{
				//TODO: Fill in logic for "Super." expression
			}
			else
			{
				mustBe(tokenQueue, TokenType.PAREN_LEFT);
				result = new SuperConstructor(
						line, new List_Expression(tokenQueue));
				mustBe(tokenQueue, TokenType.PAREN_RIGHT);
			}
		}
		// new
		else if(have(tokenQueue, TokenType._NEW))
		{
			//TODO: Fill in logic for "New" expression
		}
		// identifier
		else if(QualifiedIdentifier.isNext(tokenQueue))
		{
			QualifiedIdentifier id = new QualifiedIdentifier(tokenQueue);
			if(have(tokenQueue, TokenType.PAREN_LEFT))
			{
				result = new MethodCall(
						line, id, new List_Expression(tokenQueue));
				mustBe(tokenQueue, TokenType.PAREN_RIGHT);
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
			Token literalToken = tokenQueue.poll();
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
						literalToken.getFilename(), literalToken.getRow(),
						literalToken.getCol(), literalToken.getLine());
			}
			result = new Literal(line, literalToken);
		}
		return result;
	}

	
	private static boolean seeCast(RewindableQueue<Token> tokenQueue)
	{
		//TODO: fill with cast checking
		return false;
	}
}

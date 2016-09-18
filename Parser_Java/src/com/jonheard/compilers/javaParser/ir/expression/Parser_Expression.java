package com.jonheard.compilers.javaParser.ir.expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaParser.ir.Identifier;
import com.jonheard.compilers.javaParser.ir.List_Expression;
import com.jonheard.compilers.javaParser.ir.QualifiedIdentifier;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;
import static com.jonheard.compilers.javaParser.JavaParser.*;

public class Parser_Expression
{
	public static BaseIrType parseExpressionStatment(
			RewindableQueue<JavaToken> tokenQueue)
	{
		JavaToken next = tokenQueue.peek();
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
		mustBe(tokenQueue, JavaTokenType.SEMICOLON);
		return result;
	}
	
	public static Expression parseExpression(
			RewindableQueue<JavaToken> tokenQueue)
	{
		return tryAssignment(tokenQueue);
	}

	private static Expression tryAssignment(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryConditional(tokenQueue);
		if(have(tokenQueue, JavaTokenType.EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.PLUS_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__ADD, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.DASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__SUB, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.STAR_EQUAL))
			return new Expression(
					ExpressionType.ASSIGMENT__MUL, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.SLASH_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__DIV, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.PERCENT_EQUAL))
			return new Expression(
					ExpressionType.ASSIGNMENT__MOD, line,
					lhs, tryAssignment(tokenQueue));
		else
			return lhs;
	}
	
	private static Expression tryConditional(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryLogical(tokenQueue);
		if(have(tokenQueue, JavaTokenType.QUESTION))
		{
			Expression consequent = parseExpression(tokenQueue);
			mustBe(tokenQueue, JavaTokenType.COLON);
			Expression alternative = parseExpression(tokenQueue);
			lhs = new Expression(
					ExpressionType.CONDITIONAL, line,
					lhs, consequent, alternative);
		}
		return lhs;
	}

	private static Expression tryLogical(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryEquality(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, JavaTokenType.AND))
				lhs = new Expression(
						ExpressionType.LOGICAL_AND, line,
						lhs, tryEquality(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.PIPE))
				lhs = new Expression(
						ExpressionType.LOGICAL_OR, line,
						lhs, tryEquality(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryEquality(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryRelational(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, JavaTokenType.EQUAL_EQUAL))
				lhs = new Expression(
						ExpressionType.EQUALITY, line,
						lhs, tryRelational(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryRelational(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryAdditive(tokenQueue);
		if(have(tokenQueue, JavaTokenType.RIGHT))
			lhs = new Expression(
					ExpressionType.GREATER, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, JavaTokenType.LEFT))
			lhs = new Expression(
					ExpressionType.LESS, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, JavaTokenType.RIGHT_EQUAL))
			lhs = new Expression(
					ExpressionType.GREATER_OR_EQUAL, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, JavaTokenType.LEFT_EQUAL))
			lhs = new Expression(
					ExpressionType.LESS_OR_EQUAL, line,
					lhs, tryAdditive(tokenQueue));
		return lhs;
	}
	
	private static Expression tryAdditive(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryMultiplicative(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, JavaTokenType.PLUS))
				lhs = new Expression(
						ExpressionType.ADD, line,
						lhs, tryMultiplicative(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.DASH))
				lhs = new Expression(
						ExpressionType.SUB, line,
						lhs, tryMultiplicative(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}

	private static Expression tryMultiplicative(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryUnary(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, JavaTokenType.STAR))
				lhs = new Expression(
						ExpressionType.MUL, line,
						lhs, tryUnary(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.SLASH))
				lhs = new Expression(
						ExpressionType.DIV, line,
						lhs, tryUnary(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.PERCENT))
				lhs = new Expression(
						ExpressionType.MOD, line,
						lhs, tryUnary(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryUnary(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		if(have(tokenQueue, JavaTokenType.PLUS_PLUS))
			return new Expression(
					ExpressionType.PRE_INCREMENT, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, JavaTokenType.DASH_DASH))
			return new Expression(
					ExpressionType.PRE_DECREMENT, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, JavaTokenType.PLUS))
			return new Expression(
					ExpressionType.POSITIVE, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, JavaTokenType.DASH))
			return new Expression(
					ExpressionType.NEGATIVE, line,
					tryUnary(tokenQueue));
		else
			return trySimpleUnary(tokenQueue);
	}
	
	private static Expression trySimpleUnary(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		if(have(tokenQueue, JavaTokenType.EXCLAIM))
			return new Expression(
					ExpressionType.LOGICAL_NOT, line,
					tryUnary(tokenQueue));
		else if(seeCast(tokenQueue))
		{
			mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
			QualifiedIdentifier type = new QualifiedIdentifier(tokenQueue);
			mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
			return new Cast(line, type, parseExpression(tokenQueue));
		}
		else
			return tryPostFix(tokenQueue);
	}
	
	private static Expression tryPostFix(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression lhs = tryPrimary(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, JavaTokenType.PLUS_PLUS))
				lhs = new Expression(ExpressionType.POST_INCREMENT, line, lhs);
			if(have(tokenQueue, JavaTokenType.DASH_DASH))
				lhs = new Expression(ExpressionType.POST_DECREMENT, line, lhs);
			//TODO: add dot and lbrack stuff (line 1506) 
			else
				more = false;
		}
		return lhs;
	}
	
	private static Expression tryPrimary(RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		Expression result = null;
		// Parenthesized expression
		if(have(tokenQueue, JavaTokenType.PAREN_LEFT))
		{
			result = parseExpression(tokenQueue);
			mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
		}
		// this
		else if(have(tokenQueue, JavaTokenType._THIS))
		{
			if(have(tokenQueue, JavaTokenType.PAREN_LEFT))
			{
				result = new ThisConstructor(
						line, new List_Expression(tokenQueue));
				mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
			}
			else
			{
				result = new Expression(ExpressionType.THIS_REFERENCE, line);
			}
		}
		// super
		else if(have(tokenQueue, JavaTokenType._SUPER))
		{
			if(have(tokenQueue, JavaTokenType.DOT))
			{
				//TODO: Fill in logic for "Super." expression
			}
			else
			{
				mustBe(tokenQueue, JavaTokenType.PAREN_LEFT);
				result = new SuperConstructor(
						line, new List_Expression(tokenQueue));
				mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
			}
		}
		// new
		else if(have(tokenQueue, JavaTokenType._NEW))
		{
			//TODO: Fill in logic for "New" expression
		}
		// identifier
		else if(QualifiedIdentifier.isNext(tokenQueue))
		{
			QualifiedIdentifier id = new QualifiedIdentifier(tokenQueue);
			if(have(tokenQueue, JavaTokenType.PAREN_LEFT))
			{
				result = new MethodCall(
						line, id, new List_Expression(tokenQueue));
				mustBe(tokenQueue, JavaTokenType.PAREN_RIGHT);
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
			JavaToken literalToken = tokenQueue.poll();
			JavaTokenType literalTokenType = literalToken.getType();
			if(		literalTokenType != JavaTokenType.INTEGER &&
					literalTokenType != JavaTokenType.LONG &&
					literalTokenType != JavaTokenType.FLOAT &&
					literalTokenType != JavaTokenType.DOUBLE &&
					literalTokenType != JavaTokenType.CHAR &&
					literalTokenType != JavaTokenType.STRING &&
					literalTokenType != JavaTokenType._TRUE &&
					literalTokenType != JavaTokenType._FALSE &&
					literalTokenType != JavaTokenType._NULL)
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

	
	private static boolean seeCast(RewindableQueue<JavaToken> tokenQueue)
	{
		//TODO: fill with cast checking
		return false;
	}
}

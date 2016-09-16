package expression;

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
			case PreIncrement:
			case PreDecrement:
			case PostIncrement:
			case PostDecrement:
			case MethodCall:
			case SuperConstructor:
			case ThisConstructor:
			case NewObject:
			case NewArray:
			break;
			default:
				Logger.error("not a statement",
						next.getFilename(), next.getRow(), next.getCol(),
						next.getLine());
				break;				
		}
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
					ExpressionType.Assignment, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.PLUS_EQUAL))
			return new Expression(
					ExpressionType.Assignment_Add, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.DASH_EQUAL))
			return new Expression(
					ExpressionType.Assignment_Sub, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.STAR_EQUAL))
			return new Expression(
					ExpressionType.Assignment_Mul, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.SLASH_EQUAL))
			return new Expression(
					ExpressionType.Assignment_Div, line,
					lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.PERCENT_EQUAL))
			return new Expression(
					ExpressionType.Assignment_Mod, line,
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
					ExpressionType.Conditional, line,
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
						ExpressionType.LogicalAnd, line,
						lhs, tryEquality(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.PIPE))
				lhs = new Expression(
						ExpressionType.LogicalOr, line,
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
						ExpressionType.Equality, line,
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
					ExpressionType.Greater, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, JavaTokenType.LEFT))
			lhs = new Expression(
					ExpressionType.Less, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, JavaTokenType.RIGHT_EQUAL))
			lhs = new Expression(
					ExpressionType.GreaterOrEqual, line,
					lhs, tryAdditive(tokenQueue));
		if(have(tokenQueue, JavaTokenType.LEFT_EQUAL))
			lhs = new Expression(
					ExpressionType.LessOrEqual, line,
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
						ExpressionType.Add, line,
						lhs, tryMultiplicative(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.DASH))
				lhs = new Expression(
						ExpressionType.Sub, line,
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
						ExpressionType.Mul, line,
						lhs, tryUnary(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.SLASH))
				lhs = new Expression(
						ExpressionType.Div, line,
						lhs, tryUnary(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.PERCENT))
				lhs = new Expression(
						ExpressionType.Mod, line,
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
					ExpressionType.PreIncrement, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, JavaTokenType.DASH_DASH))
			return new Expression(
					ExpressionType.PreDecrement, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, JavaTokenType.PLUS))
			return new Expression(
					ExpressionType.Positive, line,
					tryUnary(tokenQueue));
		else if(have(tokenQueue, JavaTokenType.DASH))
			return new Expression(
					ExpressionType.Negative, line,
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
					ExpressionType.LogicalNot, line,
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
				lhs = new Expression(ExpressionType.PostIncrement, line, lhs);
			if(have(tokenQueue, JavaTokenType.DASH_DASH))
				lhs = new Expression(ExpressionType.PostDecrement, line, lhs);
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
				result = new Expression(ExpressionType.ThisReference, line);
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
				result = new Variable(line, (Identifier)id.getChild(0));
			}
			else
			{
				result = new Field(line, id);
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

package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
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
		BaseIrType result = parseExpression(tokenQueue);
		if(
				!(result instanceof PreIncrement) ||
				!(result instanceof PreDecrement) ||
				!(result instanceof PostIncrement) ||
				!(result instanceof PostDecrement) ||
				!(result instanceof MethodCall) ||
				!(result instanceof SuperConstructor) ||
				!(result instanceof ThisConstructor) ||
				!(result instanceof NewObject) ||
				!(result instanceof NewArray))
		{
			Logger.error("not a statement",
					next.getFilename(), next.getRow(), next.getCol(),
					next.getLine());
		}
		return result;
	}
	
	public static BaseIrType parseExpression(
			RewindableQueue<JavaToken> tokenQueue)
	{
		return tryAssignment(tokenQueue);
	}

	private static BaseIrType tryAssignment(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		BaseIrType lhs = tryConditional(tokenQueue);
		if(have(tokenQueue, JavaTokenType.EQUAL))
			return new Assignment    (line, lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.PLUS_EQUAL))
			return new Assignment_Add(line, lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.DASH_EQUAL))
			return new Assignment_Sub(line, lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.STAR_EQUAL))
			return new Assignment_Mul(line, lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.SLASH_EQUAL))
			return new Assignment_Div(line, lhs, tryAssignment(tokenQueue));
		if(have(tokenQueue, JavaTokenType.PERCENT_EQUAL))
			return new Assignment_Mod(line, lhs, tryAssignment(tokenQueue));
		else
			return lhs;
	}
	
	private static BaseIrType tryConditional(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		BaseIrType lhs = tryLogical(tokenQueue);
		if(have(tokenQueue, JavaTokenType.QUESTION))
		{
			BaseIrType consequent = parseExpression(tokenQueue);
			mustBe(tokenQueue, JavaTokenType.COLON);
			BaseIrType alternative = parseExpression(tokenQueue);
			lhs = new Conditional(line, lhs, consequent, alternative);
		}
		return lhs;
	}

	private static BaseIrType tryLogical(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		BaseIrType lhs = tryEquality(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, JavaTokenType.AND))
				lhs = new LogicalAnd(line, lhs, tryEquality(tokenQueue));
			else if(have(tokenQueue, JavaTokenType.PIPE))
				lhs = new LogicalOr(line, lhs, tryEquality(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}

	private static BaseIrType tryEquality(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		BaseIrType lhs = tryRelational(tokenQueue);
		boolean more = true;
		while(more)
		{
			if(have(tokenQueue, JavaTokenType.EQUAL_EQUAL))
				lhs = new Equality(line, lhs, tryRelational(tokenQueue));
			else
				more = false;
		}
		return lhs;
	}
	
	private static BaseIrType tryRelational(
			RewindableQueue<JavaToken> tokenQueue)
	{
		int line = tokenQueue.peek().getRow();
		BaseIrType lhs = tryAdditive(tokenQueue);
		if(have(tokenQueue, JavaTokenType.RIGHT))
			lhs = new GreaterThan(line, lhs, tryAdditive(tokenQueue));
		else
			more = false;
	}
	
	private static BaseIrType tryAdditive(
			RewindableQueue<JavaToken> tokenQueue)
	{
		return null;
	}
}

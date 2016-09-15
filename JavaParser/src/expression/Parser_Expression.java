package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;
import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;

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
		return tryAssignmentExpression(tokenQueue);
	}

	public static BaseIrType tryAssignmentExpression(
			RewindableQueue<JavaToken> tokenQueue)
	{
		JavaToken next = tokenQueue.peek();
		BaseIrType lhs = conditionalExpression();
		if(have(tokenQueue, ))
		return tryAssignmentExpression(tokenQueue);
	}
}

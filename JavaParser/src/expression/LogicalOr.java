package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class LogicalOr extends BaseBinaryExpression
{

	public LogicalOr(int line, BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

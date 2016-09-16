package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class LogicalAnd extends BaseBinaryExpression
{

	public LogicalAnd(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Assignment extends BaseBinaryExpression
{

	public Assignment(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

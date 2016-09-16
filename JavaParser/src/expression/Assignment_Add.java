package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Assignment_Add extends BaseBinaryExpression
{
	public Assignment_Add(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

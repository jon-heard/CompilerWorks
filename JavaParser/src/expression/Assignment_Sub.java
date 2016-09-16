package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Assignment_Sub extends BaseBinaryExpression
{

	public Assignment_Sub(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

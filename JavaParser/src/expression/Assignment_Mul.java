package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Assignment_Mul extends BaseBinaryExpression
{

	public Assignment_Mul(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Assignment_Div extends BaseBinaryExpression
{

	public Assignment_Div(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

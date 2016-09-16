package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Equality extends BaseBinaryExpression
{

	public Equality(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

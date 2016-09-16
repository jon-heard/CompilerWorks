package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Assignment_Mod extends BaseBinaryExpression
{

	public Assignment_Mod(int line,	BaseIrType lhs, BaseIrType rhs)
	{
		super(line, lhs, rhs);
	}
}

package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public final class PostIncrement extends BaseUnaryExpression
{

	public PostIncrement(int line, BaseIrType lhs)
	{
		super(line, lhs);
	}
}

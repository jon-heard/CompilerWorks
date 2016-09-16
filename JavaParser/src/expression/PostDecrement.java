package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class PostDecrement extends BaseUnaryExpression
{

	public PostDecrement(int line, BaseIrType lhs)
	{
		super(line, lhs);
	}
}

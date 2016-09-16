package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class PreDecrement extends BaseUnaryExpression
{

	public PreDecrement(int line, BaseIrType lhs)
	{
		super(line, lhs);
	}
}

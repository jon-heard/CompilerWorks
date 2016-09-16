package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class PreIncrement extends BaseUnaryExpression
{

	public PreIncrement(int line, BaseIrType lhs)
	{
		super(line, lhs);
	}
}

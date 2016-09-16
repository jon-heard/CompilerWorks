package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class BaseUnaryExpression extends BaseIrType
{
	public BaseUnaryExpression(int line, BaseIrType lhs)
	{
		super(line);
		addChild(lhs);
	}
}

package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class BaseBinaryExpression extends BaseIrType
{
	public BaseBinaryExpression(int line, BaseIrType lhs, BaseIrType rhs)
	{
		super(line);
		addChild(lhs);
		addChild(rhs);
	}
}

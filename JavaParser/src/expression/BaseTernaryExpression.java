package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class BaseTernaryExpression extends BaseIrType
{
	public BaseTernaryExpression(
			int line, BaseIrType lhs, BaseIrType mhs, BaseIrType rhs)
	{
		super(line);
		addChild(lhs);
		addChild(mhs);
		addChild(rhs);
	}
}

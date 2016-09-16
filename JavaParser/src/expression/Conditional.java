package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Conditional extends BaseTernaryExpression
{

	public Conditional(
			int line, BaseIrType condition,
			BaseIrType consequent, BaseIrType alternative)
	{
		super(line, condition, consequent, alternative);
	}
}

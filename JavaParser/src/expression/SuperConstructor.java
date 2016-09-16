package expression;

import com.jonheard.compilers.javaParser.ir.List_Expression;

public class SuperConstructor extends Expression
{
	public SuperConstructor(int line, List_Expression parameters)
	{
		super(ExpressionType.SuperConstructor, line);
		addChild(parameters);
	}
}

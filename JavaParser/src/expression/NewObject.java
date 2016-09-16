package expression;

public class NewObject extends Expression
{
	public NewObject(int line)
	{
		super(ExpressionType.NewObject, line);
	}
}

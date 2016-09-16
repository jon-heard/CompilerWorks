package expression;

import com.jonheard.compilers.javaParser.ir.BaseIrType;

public class Expression extends BaseIrType
{
	public Expression(ExpressionType type, int line)
	{
		super(line);
		this.type = type;
	}
	public Expression(ExpressionType type, int line, Expression lhs)
	{
		this(type, line);
		addChild(lhs);
	}
	public Expression(ExpressionType type, int line, Expression lhs, Expression rhs)
	{
		this(type, line, lhs);
		addChild(rhs);
	}
	public Expression(
			ExpressionType type, int line,
			Expression lhs, Expression mhs, Expression rhs)
	{
		this(type, line, lhs, mhs);
		addChild(rhs);
	}
	
	@Override
	public String getTypeName()
	{
		return getType().name();
	}
	
	public ExpressionType getType()
	{
		return type;
	}
	
	private ExpressionType type;
}

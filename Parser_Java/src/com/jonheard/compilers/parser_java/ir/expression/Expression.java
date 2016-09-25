package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.util.UtilMethods;

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
	public Expression(
			ExpressionType type, int line, Expression lhs, Expression rhs)
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
		return UtilMethods.constNameToCamelName(getType().name(), true);
	}
	
	public ExpressionType getType()
	{
		return type;
	}
	
	private ExpressionType type;
}

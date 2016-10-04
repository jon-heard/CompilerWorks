package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.util.UtilMethods;

public class Expression extends BaseIrType
{
	public Expression(ExpressionType type, Token first)
	{
		super(first.getLine(), first.getColumn());
		this.type = type;
	}
	public Expression(ExpressionType type, Token first, Expression lhs)
	{
		this(type, first);
		addChild(lhs);
	}
	public Expression(
			ExpressionType type, Token first, Expression lhs, Expression rhs)
	{
		this(type, first, lhs);
		addChild(rhs);
	}
	public Expression(
			ExpressionType type, Token first, Expression lhs, Expression mhs,
			Expression rhs)
	{
		this(type, first, lhs, mhs);
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

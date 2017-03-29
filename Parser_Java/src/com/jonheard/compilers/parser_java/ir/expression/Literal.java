package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.tokenizer_java.Token;

public class Literal extends Expression
{
	public Literal(Token next, Token data)
	{
		super(ExpressionType.LITERAL, next);
		this.data = data;
	}
	
	@Override
	public String getIrTypeName()
	{
		return "Literal_" + getLiteralType().toString();
	}
	
	@Override
	public String getHeaderString()
	{
		return "value='" + getLiteralValue() + "'";
	}
	
	public String getLiteralType()
	{
		return data.getType().toString();
	}
	
	public String getLiteralValue()
	{
		return data.getText();
	}
	
	private Token data;
}

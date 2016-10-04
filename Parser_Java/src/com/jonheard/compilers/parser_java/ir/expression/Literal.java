package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Literal extends Expression
{
	public Literal(Token next, Token data)
	{
		super(ExpressionType.LITERAL, next);
		this.data = data;
	}
	
	@Override
	public String getTypeName()
	{
		return "Literal_" + getLiteralType().toString();
	}
	
	@Override
	public String getHeaderString()
	{
		return "value='" + getLiteralValue() + "'";
	}
	
	public TokenType getLiteralType()
	{
		return data.getType();
	}
	
	public String getLiteralValue()
	{
		return data.getText();
	}
	
	private Token data;
}

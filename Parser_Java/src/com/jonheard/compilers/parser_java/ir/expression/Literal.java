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
	public String getIrTypeName()
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
		return data.getTokenType();
	}
	
	public String getLiteralValue()
	{
		return data.getText();
	}
	
	private Token data;
}

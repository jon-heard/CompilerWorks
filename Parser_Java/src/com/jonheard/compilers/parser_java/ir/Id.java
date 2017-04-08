package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Id extends BaseIrType
{
	private String value = "";
	
	public Id(Parser parser)
	{
		super(parser);
		Token currentToken = parser.peekNextToken();
		if(parser.requireTokenToBeOfType(TokenType.IDENTIFIER))
		{
			value = currentToken.getText();
		}
		else
		{
			value = "";
		}
	}
	public Id(String value)
	{
		super(0, 0);
		this.value = value;
	}

	@Override
	public String getHeaderString()
	{
		return "value='" + getValue() + "'";
	}

	public String getValue()
	{
		return value;
	}
	
	public static boolean isNext(Parser parser)
	{
		return parser.getIsTokenType(TokenType.IDENTIFIER);
	}
	
	@Override
	public boolean equals(Object rhs)
	{
		if(!(rhs instanceof Id))
		{
			return false;
		}
		Id other = (Id)rhs;
		return this.value.equals(other.value);
	}
}

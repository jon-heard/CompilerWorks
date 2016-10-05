package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Id extends BaseIrType
{
	private String value = "";
	
	public Id(Parser_Java parser)
	{
		super(parser);
		Token currentToken = parser.getNextToken();
		if(parser.mustBe(TokenType.ID))
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
	
	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType.ID);
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

package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Identifier extends BaseIrType
{
	private String value = "";
	
	public Identifier(Parser_Java parser)
	{
		super(parser);
		Token currentToken = parser.getNextToken();
		if(parser.mustBe(TokenType.IDENTIFIER))
		{
			value = currentToken.getText();
		}
		else
		{
			value = "";
		}
	}
	public Identifier(String value)
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
		return parser.see(TokenType.IDENTIFIER);
	}
}

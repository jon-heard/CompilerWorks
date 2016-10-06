package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Enum extends BaseIrType
{
	public Enum(Parser_Java parser)
	{
		super(parser);
		addChild(new List_Modifier(parser));
		parser.mustBe(TokenType._ENUM);
		addChild(new Id(parser));
		while(!parser.have(TokenType.CURL_BRACE_RIGHT))
		{
			parser.getTokenQueue().poll();
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"id='" + getName().getValue() + "' " +
				"modifiers='" + getModifiers().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 2; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	public Id getName()
	{
		return (Id)getChild(1);
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		new List_Modifier(parser);
		result = parser.see(TokenType._ENUM);
		parser.getTokenQueue().rewind();
		return result;
	}
}

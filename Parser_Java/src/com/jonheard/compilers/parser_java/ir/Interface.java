package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Interface extends BaseIrType
{
	public Interface(Parser_Java parser)
	{
		super(parser);
		addChild(new List_Modifier(parser));
		parser.mustBe(TokenType._INTERFACE);
		addChild(new Identifier(parser));
		while(!parser.have(TokenType.CURL_BRACE_RIGHT))
		{
			parser.getTokenQueue().poll();
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "'" +
				" modifiers='" + getModifiers().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 2; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	public Identifier getName()
	{
		return (Identifier)getChild(1);
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		new List_Modifier(parser);
		result = parser.see(TokenType._INTERFACE);
		parser.getTokenQueue().rewind();
		return result;
	}
}

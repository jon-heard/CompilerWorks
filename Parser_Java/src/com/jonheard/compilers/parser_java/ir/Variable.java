package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Variable extends BaseIrType
{
	public Variable(Parser_Java parser)
	{
		super(parser);
		addChild(new List_Modifier(parser));
		addChild(new Type(parser));
		addChild(new Id(parser));
		while(parser.have(TokenType.SQUARE_BRACE_LEFT))
		{
			parser.mustBe(TokenType.SQUARE_BRACE_RIGHT);
			getType().incDimensionCount();
		}
		parser.mustBe(TokenType.SEMICOLON);
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "' " +
				"type='" + getType().getValue() + "' " +
				"modifiers='" + getModifiers().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	
	public Type getType()
	{
		return (Type)getChild(1);
	}
	
	public Id getName()
	{
		return (Id)getChild(2);
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		new List_Modifier(parser);
		if(Type.isNext(parser))
		{
			new Type(parser);
			if(parser.see(TokenType.ID))
			{
				result = true;
			}
		}
		parser.getTokenQueue().rewind();
		return result;
	}
}

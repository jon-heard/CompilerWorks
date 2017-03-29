package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Class extends BaseIrType
{
	public Class(Parser_Java parser)
	{
		super(parser);
		addChild(new List_Modifier(parser));
		parser.mustBe(TokenType._CLASS);
		addChild(new Id(parser));
		if(parser.have(TokenType._EXTENDS))
		{
			addChild(new QualifiedId(parser));
		}
		else
		{
			parser.getTokenQueue().addFirst(
					new Token(TokenType.IDENTIFIER, 0, "Object"));
			addChild(new QualifiedId(parser));
		}
		parser.mustBe(TokenType.CURL_BRACE_LEFT);
		while(!parser.have(TokenType.CURL_BRACE_RIGHT))
		{
			addChild(new MethodOrVariable(parser));
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"id='" + getName().getValue() + "' " +
				"super='" + getSuper().getValue() + "' " +
				"modifiers='" + getModifiers().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	
	public Id getName()
	{
		return (Id)getChild(1);
	}
	
	public QualifiedId getSuper()
	{
		return (QualifiedId)getChild(2);
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		new List_Modifier(parser);
		result = parser.see(TokenType._CLASS);
		parser.getTokenQueue().rewind();
		return result;
	}
}

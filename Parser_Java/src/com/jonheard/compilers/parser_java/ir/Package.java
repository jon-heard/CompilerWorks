package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Package extends BaseIrType
{
	Package(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType._PACKAGE);
		addChild(new QualifiedIdentifier(parser));
		parser.mustBe(TokenType.SEMICOLON);
	}

	@Override
	public String getHeaderString()
	{
		return	"identifier='" + getIdentifier().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedIdentifier getIdentifier()
	{
		return (QualifiedIdentifier)getChild(0);
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType._PACKAGE);
	}

}

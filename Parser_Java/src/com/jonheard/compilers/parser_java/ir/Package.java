package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Package extends BaseIrType
{
	Package(Parser parser)
	{
		super(parser);
		parser.requireTokenToBeOfType(TokenType._PACKAGE);
		addChild(new QualifiedId(parser));
		parser.requireTokenToBeOfType(TokenType.SEMICOLON);
	}

	@Override
	public String getHeaderString()
	{
		return	"id='" + getId().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedId getId()
	{
		return (QualifiedId)getChild(0);
	}
	
	public static boolean isNext(Parser parser)
	{
		return parser.getIsTokenType(TokenType._PACKAGE);
	}

}

package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Import extends BaseIrType
{
	private boolean flag_isStatic = false;
	private boolean flag_isOnDemand = false;

	public Import(Parser_Java parser)
	{
		super(parser);
		parser.mustBe(TokenType._IMPORT);
		if(parser.have(TokenType._STATIC))
		{
			flag_isStatic = true;
		}
		addChild(new QualifiedId(parser));
		if(parser.have(TokenType.DOT))
		{
			parser.mustBe(TokenType.STAR);
			flag_isOnDemand = true;
		}
		parser.mustBe(TokenType.SEMICOLON);
	}

	@Override
	public String getHeaderString()
	{
		return	"isOnDemand='" + flag_isOnDemand + "' " +
				"isStatic='" + flag_isStatic + "' " +
				"id='" + getId().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedId getId()
	{
		return (QualifiedId)getChild(0);
	}
	
	public boolean isStatic()
	{
		return flag_isStatic;
	}
	
	public boolean isOnDemand()
	{
		return flag_isOnDemand;
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		return parser.see(TokenType._IMPORT);
	}
}

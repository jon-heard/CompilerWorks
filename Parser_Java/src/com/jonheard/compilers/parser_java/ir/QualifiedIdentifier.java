package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class QualifiedIdentifier extends BaseIrType
{
	public QualifiedIdentifier(Parser_Java parser)
	{
		super(parser);
		addChild(new Identifier(parser));
		parser.getTokenQueue().remember();
		while(parser.have(TokenType.DOT))
		{
			if(Identifier.isNext(parser))
			{
				parser.getTokenQueue().rewind();
				parser.mustBe(TokenType.DOT);
				addChild(new Identifier(parser));
				parser.getTokenQueue().remember();
			}
		}
		parser.getTokenQueue().rewind();
	}
	
	@Override
	public String getHeaderString()
	{
		return "value='" + getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 10000000; }
	
	public String getValue()
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < getChildCount(); i++)
		{
			result.append(((Identifier)getChild(i)).getValue());
			result.append(".");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		return Identifier.isNext(parser);
	}
}

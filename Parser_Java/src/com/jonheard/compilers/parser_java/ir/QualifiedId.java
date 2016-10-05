package com.jonheard.compilers.parser_java.ir;

import java.util.List;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class QualifiedId extends BaseIrType
{
	public QualifiedId(Parser_Java parser)
	{
		super(parser);
		addChild(new Id(parser));
		parser.getTokenQueue().remember();
		while(parser.have(TokenType.DOT))
		{
			if(Id.isNext(parser))
			{
				parser.getTokenQueue().rewind();
				parser.mustBe(TokenType.DOT);
				addChild(new Id(parser));
				parser.getTokenQueue().remember();
			}
		}
		parser.getTokenQueue().rewind();
	}
	public QualifiedId(int line, int column, List<Id> children)
	{
		super(line, column);
		for(Id child : children)
		{
			addChild(child);
		}
	}

	@Override
	public String getHeaderString()
	{
		return "value='" + getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 10000000; }
	
	public Id getFirst()
	{
		return (Id)getChild(0);
	}
	
	public String getValue()
	{
		StringBuffer result = new StringBuffer();
		for(int i = 0; i < getChildCount(); i++)
		{
			result.append(((Id)getChild(i)).getValue());
			result.append(".");
		}
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		return Id.isNext(parser);
	}
}
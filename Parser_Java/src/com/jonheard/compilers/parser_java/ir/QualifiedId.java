package com.jonheard.compilers.parser_java.ir;

import java.util.LinkedList;
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
	private QualifiedId(int line, int column, List<Id> children)
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
		if(result.length() > 0)
		{
			result.deleteCharAt(result.length()-1);
		}
		return result.toString();
	}

	public void setValue(String value)
	{
		while(getChildCount() > 0)
		{
			removeChild(0);
		}
		String[] valueItems = value.split("\\.");
		for(int i = 0; i < valueItems.length; i++)
		{
			addChild(new Id(valueItems[i]));
		}
	}

	public void addPrefix(String value)
	{
		if(value.equals("")) return;
		String[] valueItems = value.split("\\.");
		for(int i = valueItems.length-1; i >= 0; i--)
		{
			prependChild(new Id(valueItems[i]));
		}
	}

	public QualifiedId split(int secondStartIndex)
	{
		if(secondStartIndex >= getChildCount()) return null;
		List<Id> transfers = new LinkedList<Id>();
		for(int i = secondStartIndex; i < getChildCount(); i++)
		{
			transfers.add((Id)getChild(i));
		}
		while(getChildCount() > secondStartIndex)
		{
			removeChild(secondStartIndex);
		}
		return new QualifiedId(getLine(), getColumn(), transfers);
	}

	public static boolean isNext(Parser_Java parser)
	{
		return Id.isNext(parser);
	}
}

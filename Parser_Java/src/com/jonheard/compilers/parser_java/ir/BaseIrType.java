package com.jonheard.compilers.parser_java.ir;

import java.util.ArrayList;
import java.util.List;
import com.jonheard.compilers.parser_java.Parser_Java;

public class BaseIrType
{
	public BaseIrType(Parser_Java parser)
	{
		line = parser.getNextToken().getRow();
		column = parser.getNextToken().getColumn();
	}
	public BaseIrType(int line, int column)
	{
		this.line = line;
		this.column = column;
	}

	public int getLine()
	{
		return line;
	}
	
	public int getColumn()
	{
		return column;
	}
	
	public int getChildCount()
	{
		return children.size();
	}

	public BaseIrType getChild(int index)
	{
		if(index < 0 || index >= children.size())
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return children.get(index);
	}
	
	public void replaceChild(int childIndex, BaseIrType newValue)
	{
		if(newValue == children.get(childIndex)) return;
		children.remove(childIndex);
		children.add(childIndex, newValue);
	}

	public int getFirstPrintedChildIndex() { return 0; }

	public String getHeaderString() { return ""; }
	
	public String getIrTypeName()
	{
		String result = this.getClass().getName();
		result = result.substring(result.lastIndexOf('.')+1);
		return result;
	}

	@Override
	public String toString()
	{
		return toString(0);
	}
	
	public String toString(int tabCount)
	{
		StringBuffer result = new StringBuffer();
		String tabs = new String(new char[tabCount]).replace('\0', '	');
		String headerString = getHeaderString();
		if(!headerString.equals("")) headerString = " " + headerString;
		headerString = " line='" + getLine() + "'" + headerString;
		if(children.size() <= getFirstPrintedChildIndex())
		{
			result.append(tabs + "<" + getIrTypeName() + headerString + "/>\n");
		}
		else
		{
			String typeName = getIrTypeName();
			result.append(tabs + "<" + typeName + headerString + ">\n");
			for(int i = getFirstPrintedChildIndex(); i < children.size(); i++)
			{
				result.append(getChild(i).toString(tabCount+1));
			}
			result.append(tabs + "</" + typeName + ">\n");
		}
		return result.toString();
	}

	protected void prependChild(BaseIrType child)
	{
		children.add(0, child);
	}

	protected void addChild(BaseIrType child)
	{
		children.add(child);
	}

	protected void removeChild(int index)
	{
		children.remove(index);
	}

	private int line, column;
	private List<BaseIrType> children = new ArrayList<BaseIrType>();
}

package com.jonheard.compilers.javaParser.ir;

import java.util.ArrayList;
import java.util.List;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;

public abstract class BaseIrType
{
	public abstract String getHeaderString();

	public BaseIrType(JavaToken start)
	{
		line = start.getRow();
	}
	
	public int getLine()
	{
		return line;
	}
	
	public int getChildCount()
	{
		return children.size();
	}
	public BaseIrType getChild(int index)
	{
		if(index < 0 || index > children.size())
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return getChild(index);
	}

	public int getFirstPrintedChildIndex() { return 0; }

	@Override
	public String toString()
	{
		return toString(0);
	}
	
	public String toString(int tabCount)
	{
		StringBuffer result = new StringBuffer();
		String typeName = this.getClass().getName();
		typeName = typeName.substring(typeName.lastIndexOf('.')+1);
		String tabs = new String(new char[tabCount]).replace('\0', '	');
		String headerString = " line='" + getLine() + "' " + getHeaderString();
		if(children.size() <= getFirstPrintedChildIndex())
		{
			result.append(tabs + "<" + typeName + headerString + "/>\n");
		}
		else
		{
			result.append(tabs + "<" + typeName + headerString + ">\n");
			for(int i = getFirstPrintedChildIndex(); i < children.size(); i++)
			{
				result.append(getChild(i).toString(tabCount+1));
			}
			result.append(tabs + "</" + typeName + ">\n");
		}
		return result.toString();
	}

	protected void addChild(BaseIrType child)
	{
		addChild(child);
	}

	private int line;
	private List<BaseIrType> children = new ArrayList<BaseIrType>();
	private static boolean mustBeHasErrored = false;
}

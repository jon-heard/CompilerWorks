package com.jonheard.compilers.javaParser.ir;

import java.util.ArrayList;
import java.util.List;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class BaseIrType
{
	public BaseIrType(RewindableQueue<JavaToken> tokenQueue)
	{
		line = tokenQueue.peek().getRow();
	}
	public BaseIrType(int line)
	{
		this.line = line;
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
		return children.get(index);
	}

	public int getFirstPrintedChildIndex() { return 0; }

	public String getHeaderString() { return ""; }
	
	public String getTypeName()
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
		String headerString = " line='" + getLine() + "' " + getHeaderString();
		if(children.size() <= getFirstPrintedChildIndex())
		{
			result.append(tabs + "<" + getTypeName() + headerString + "/>\n");
		}
		else
		{
			String typeName = getTypeName();
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
		children.add(child);
	}

	private int line;
	private List<BaseIrType> children = new ArrayList<BaseIrType>();
}
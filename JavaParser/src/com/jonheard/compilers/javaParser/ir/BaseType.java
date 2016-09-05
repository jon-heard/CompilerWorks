package com.jonheard.compilers.javaParser.ir;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseType
{
	public abstract String getHeaderString();

	public int getChildCount()
	{
		return children.size();
	}
	public BaseType getChild(int index)
	{
		if(index < 0 || index > children.size())
		{
			throw new ArrayIndexOutOfBoundsException(index);
		}
		return children.get(index);
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
		String headerString = getHeaderString();
		if(!headerString.equals("")) headerString = " " + headerString;
		if(children.size() <= getFirstPrintedChildIndex())
		{
			result.append(tabs + "<" + typeName + headerString + "/>\n");
		}
		else
		{
			result.append(tabs + "<" + typeName + headerString + ">\n");
			for(int i = getFirstPrintedChildIndex(); i < children.size(); i++)
			{
				result.append(children.get(i).toString(tabCount+1));
			}
			result.append(tabs + "</" + typeName + ">\n");
		}
		return result.toString();
	}

	protected List<BaseType> children = new ArrayList<BaseType>();
}

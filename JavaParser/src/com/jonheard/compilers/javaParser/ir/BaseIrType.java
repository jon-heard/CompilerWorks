package com.jonheard.compilers.javaParser.ir;

import java.util.ArrayList;
import java.util.List;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.compilers.javaTokenizer.JavaTokenType;
import com.jonheard.util.Logger;
import com.jonheard.util.RewindableQueue;

import statement.Blank;
import statement.Break;
import statement.CodeBlock;
import statement.Do;
import statement.EnhancedFor;
import statement.For;
import statement.If;
import statement.Return;
import statement.Switch;
import statement.While;

public abstract class BaseIrType
{
	public abstract String getHeaderString();

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

	protected static boolean see(
			RewindableQueue<JavaToken> tokenQueue, JavaTokenType type)
	{
		if(tokenQueue.isEmpty()) return false;
		return tokenQueue.peek().getType() == type;
	}
	
	protected static boolean have(
			RewindableQueue<JavaToken> tokenQueue, JavaTokenType type)
	{
		if(tokenQueue.isEmpty()) return false;
		if(see(tokenQueue, type))
		{
			tokenQueue.poll();
		}
		return false;
	}

	protected static boolean mustBe(
			RewindableQueue<JavaToken> tokenQueue, JavaTokenType type)
	{
		noEof(tokenQueue);
		JavaToken next = tokenQueue.peek();
		if(next.getType() == type)
		{
			mustBeHasErrored = false;
			tokenQueue.poll();
			return true;
		}
		else if(!mustBeHasErrored)
		{
			mustBeHasErrored = true;
			Logger.error(
				"'" + type.name() + "' expected",
				next.getFilename(), next.getRow(), next.getCol(),
				next.getLine());
			return false;
		}
		else
		{
			while(!tokenQueue.isEmpty() && !see(tokenQueue, type))
			{
				tokenQueue.poll();
			}
			if(have(tokenQueue, type))
			{
				mustBeHasErrored = false;
			}
			return true;
		}
	}
	
	protected static boolean noEof(RewindableQueue<JavaToken> tokenQueue)
	{
		if(tokenQueue.isEmpty())
		{
			Logger.error(
					"reached end of file while parsing",
					finalToken.getFilename(), finalToken.getRow(),
					finalToken.getCol(), finalToken.getLine());
			System.exit(0);
			return false;
		}
		else
		{
			return true;
		}
	}

	protected static BaseIrType getNextStatement(
			RewindableQueue<JavaToken> tokenQueue)
	{
		BaseIrType result = null;
		if(CodeBlock.isNext(tokenQueue)) { result = new CodeBlock(tokenQueue); } 
		else if(If.isNext(tokenQueue)) { result = new If(tokenQueue); }
		else if(While.isNext(tokenQueue)) { result = new While(tokenQueue); }
		else if(Do.isNext(tokenQueue)) { result = new Do(tokenQueue); }
		else if(Switch.isNext(tokenQueue)) { result = new Switch(tokenQueue); }
		else if(Return.isNext(tokenQueue)) { result = new Return(tokenQueue); }
		else if(Break.isNext(tokenQueue)) { result = new Break(tokenQueue); }
		else if(Blank.isNext(tokenQueue)) { result = new Blank(tokenQueue); }
		else if(For.isNext(tokenQueue))
		{
			if(EnhancedFor.isNext(tokenQueue))
			{
				result = new EnhancedFor(tokenQueue);
			}
			else
			{
				result = new For(tokenQueue);
			}
		}
		else
		{
			
		}
		return result;
	}

	protected static JavaToken finalToken;

	private List<BaseIrType> children = new ArrayList<BaseIrType>();
	private static boolean mustBeHasErrored = false;
}

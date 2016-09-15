package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class List_Modifier extends BaseIrType
{
	public List_Modifier(RewindableQueue<JavaToken> tokenQueue)
	{
		super(tokenQueue.peek());
		while(true)
		{
			JavaToken currentToken = tokenQueue.peek();
			switch(currentToken.getType())
			{
				case _PUBLIC:
					hasPublic = true;
					break;
				case _PRIVATE:
					hasPrivate = true;
					break;
				case _PROTECTED:
					hasProtected = true;
					break;
				case _STATIC:
					hasStatic = true;
					break;
				case _FINAL:
					hasFinal = true;
					break;
				default:
					return;
			}
			tokenQueue.poll();
		}
	}

	@Override
	public String getHeaderString()
	{
		return "value='" + getValue() + "'";
	}
	
	public String getValue()
	{
		StringBuffer result = new StringBuffer();
		if(hasPublic) result.append("public ");
		if(hasPrivate) result.append("private ");
		if(hasProtected) result.append("protected ");
		if(hasStatic) result.append("static ");
		if(hasFinal) result.append("final ");
		if(result.length() > 0)
		{
			result.deleteCharAt(result.length()-1);
		}
		return result.toString();
	}

	private boolean hasPublic, hasPrivate, hasProtected, hasStatic, hasFinal;
	
	public static boolean isNext(RewindableQueue<JavaToken> tokenQueue)
	{
		return true;
	}
}

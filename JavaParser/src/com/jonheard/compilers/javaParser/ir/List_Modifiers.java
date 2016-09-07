package com.jonheard.compilers.javaParser.ir;

import com.jonheard.compilers.javaTokenizer.JavaToken;
import com.jonheard.util.RewindableQueue;

public class List_Modifiers extends BaseType
{
	public List_Modifiers(RewindableQueue<JavaToken> tokenQueue)
	{
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
				default:
					return;
			}
			tokenQueue.poll();
		}
	}

	@Override
	public String getHeaderString()
	{
		StringBuffer result = new StringBuffer();
		if(hasPublic) result.append("public ");
		if(hasPrivate) result.append("private ");
		if(hasProtected) result.append("protected ");
		if(hasStatic) result.append("static ");
		result.deleteCharAt(result.length()-1);
		return result.toString();
	}

	private boolean hasPublic, hasPrivate, hasProtected, hasStatic;
}

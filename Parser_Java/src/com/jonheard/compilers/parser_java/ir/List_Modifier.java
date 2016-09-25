package com.jonheard.compilers.parser_java.ir;

import java.util.ArrayList;
import java.util.Collection;

import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.util.RewindableQueue;

public class List_Modifier extends BaseIrType
{
	public List_Modifier(RewindableQueue<Token> tokenQueue)
	{
		super(tokenQueue);
		while(true)
		{
			Token currentToken = tokenQueue.peek();
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
		Collection<String> stringCollection = toStringCollection();
		int counter = 0;
		for(String item : stringCollection)
		{
			result.append(item);
			if(counter < stringCollection.size()-1)
			{
				result.append(" ");
			}
			counter++;
		}
		return result.toString();
	}
	
	public Collection<String> toStringCollection()
	{
		Collection<String> result = new ArrayList<String>();
		if(hasPublic) result.add("public");
		if(hasPrivate) result.add("private");
		if(hasProtected) result.add("protected");
		if(hasStatic) result.add("static");
		if(hasFinal) result.add("final");		
		return result;
	}

	private boolean hasPublic, hasPrivate, hasProtected, hasStatic, hasFinal;
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return true;
	}
}

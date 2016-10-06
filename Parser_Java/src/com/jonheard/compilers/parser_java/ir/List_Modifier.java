package com.jonheard.compilers.parser_java.ir;

import java.util.ArrayList;
import java.util.Collection;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.TokenType;
import com.jonheard.util.RewindableQueue;

public class List_Modifier extends BaseIrType
{
	public List_Modifier()
	{
		super(0, 0);
	}
	public List_Modifier(Parser_Java parser)
	{
		super(parser);
		while(true)
		{
			if(     parser.have(TokenType._PUBLIC   )) { hasPublic = true; }
			else if(parser.have(TokenType._PRIVATE  )) { hasPrivate = true; }
			else if(parser.have(TokenType._PROTECTED)) { hasProtected = true; }
			else if(parser.have(TokenType._STATIC   )) { hasStatic = true; }
			else if(parser.have(TokenType._FINAL    )) { hasFinal = true; }
			else { break; }
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
	
	public boolean isPublic() { return hasPublic; }
	public boolean isPrivate() { return hasPrivate; }
	public boolean isProtected() { return hasProtected; }
	public boolean isStatic() { return hasStatic; }
	public boolean isFinal() { return hasFinal; }

	private boolean hasPublic, hasPrivate, hasProtected, hasStatic, hasFinal;
	
	public static boolean isNext(RewindableQueue<Token> tokenQueue)
	{
		return true;
	}
}

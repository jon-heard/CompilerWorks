package com.jonheard.compilers.tokenizer_java;

import java.util.LinkedList;
import java.util.List;

import com.jonheard.util.UtilMethods;

public class TokenizerStringConverter
{
	public String tokenizedToString(List<Token> source)
	{
		StringBuffer result = new StringBuffer();
		for(Token token : source)
		{
			result.append(token.toString());
			result.append('\n');
		}
		return result.toString();
	}
	
	public List<Token> stringToTokenized(String source)
	{
		List<Token> result = new LinkedList<Token>();
		String[] lines = source.split("\n|\r\n|\r");
		try
		{
			for(String line : lines)
			{
				if(line.trim().equals("") || line.trim().startsWith("//"))
				{
					continue;
				}

				String name = "";
				String text = null;
				if(line.indexOf(':') !=-1)
				{
					String[] parts = line.split(":");
					name = parts[0];
					text = parts[1];
				}
				else
				{
					name = line;
				}
				name = name.toUpperCase();
				if(!UtilMethods.enumHasValue(TokenType.class, name))
				{
					name = "_" + name;
					if(!UtilMethods.enumHasValue(TokenType.class, name))
					{
						return null;
					}
				}
				result.add(
						(text==null) ?
						new Token(TokenType.valueOf(name), 0) :
						new Token(TokenType.valueOf(name), 0, text));
			}
		}
		catch(Exception e)
		{
			result = null;
		}
		return result;
	}
}

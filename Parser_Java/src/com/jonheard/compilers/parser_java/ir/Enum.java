package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Enum extends TypeDeclaration
{
	public Enum(Parser parser)
	{
		super(parser, TokenType._ENUM);
		while(!parser.passTokenIfType(TokenType.CURL_BRACE_RIGHT))
		{
			parser.getTokenQueue().poll();
		}
	}
	
	public static boolean isNext(Parser parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		new List_Modifier(parser);
		result = parser.getIsTokenType(TokenType._ENUM);
		parser.getTokenQueue().rewind();
		return result;
	}
}

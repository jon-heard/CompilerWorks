package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.statement.CodeBlock;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Method extends BaseIrType
{
	public Method(Parser_Java parser)
	{
		super(parser);
		addChild(new List_Modifier(parser));
		addChild(new Type(parser));
		addChild(new Identifier(parser));
		parser.mustBe(TokenType.PAREN_LEFT);
		addChild(new List_Variable(parser));
		parser.mustBe(TokenType.PAREN_RIGHT);
		addChild(new CodeBlock(parser));
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "' " +
				"type='" + getType().getValue() + "' " +
				"modifiers='" + getModifiers().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	
	public Type getType()
	{
		return (Type)getChild(1);
	}
	
	public Identifier getName()
	{
		return (Identifier)getChild(2);
	}
	
	public List_Variable getParameterList()
	{
		return (List_Variable)getChild(3);
	}
	
	public String toJvmDescriptor()
	{
		StringBuffer result = new StringBuffer();
		List_Variable params = getParameterList();
		
		result.append('(');
		for(int i = 0; i < params.getChildCount(); i++)
		{
			Type pType = ((Variable)params.getChild(i)).getType();
			result.append(pType.toJvmDescriptor());
		}
		result.append(')');
		result.append(getType().toJvmDescriptor());
		return result.toString();
	}
	
	public static boolean isNext(Parser_Java parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		new List_Modifier(parser);
		if(Type.isNext(parser))
		{
			new Type(parser);
			if(Identifier.isNext(parser))
			{
				new Identifier(parser);
				if(parser.see(TokenType.PAREN_LEFT))
				{
					result = true;
				}
			}
		}
		parser.getTokenQueue().rewind();
		return result;
	}
}

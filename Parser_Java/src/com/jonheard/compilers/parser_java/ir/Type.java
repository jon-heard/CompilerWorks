package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Type extends BaseIrType
{
	public Type(Parser parser)
	{
		super(parser);
		addChild(new QualifiedId(parser));
//		if(parser.have(TokenType.LEFT))
//		{
//			do
//			{
//				addChild(new Type(parser));
//			}
//			while(parser.have(TokenType.COMMA));
//			parser.mustBe(TokenType.RIGHT);
//		}
//		else
//		{
//			addChild(null);
//		}
		while(parser.passTokenIfType(TokenType.SQUARE_BRACE_LEFT))
		{
			parser.requireTokenToBeOfType(TokenType.SQUARE_BRACE_RIGHT);
			incDimensionCount();
		}
	}
	
	@Override
	public String getHeaderString()
	{
		StringBuffer result = new StringBuffer();
		result.append("value='" + getValue() + "'");
		if(getChildCount() > 1)
		{
			result.append(" genericTypes='");
			
			result.append("'");
		}
		return result.toString();
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public String getValue()
	{
		StringBuffer result = new StringBuffer();
		result.append(getId().getValue());
		for(int i = 0; i < getDimensionCount(); i++)
		{
			result.append("[]");
		}
		return result.toString();
	}
	
	public QualifiedId getId()
	{
		return (QualifiedId)getChild(0);
	}

//	public List<Type> getGenericTypes()
//	{
//		List<Type> result = new ArrayList<Type>();
//		for(int i = 1; i < getChildCount(); i++)
//		{
//			BaseIrType child = getChild(i);
//			if(child instanceof Type)
//			{
//				result.add((Type)child);
//			}
//		}
//		return result;
//	}
	
	public int getDimensionCount() { return dimensionCount; }
	
	public String toJvmDescriptor()
	{
		String type = getId().getValue();
		int dimensionCount = getDimensionCount();
		StringBuffer result = new StringBuffer();

		for(int i = 0; i < dimensionCount; i++)
		{
			result.append("[");
		}
		if(type.equals("void")) result.append("V");
		else if(type.equals("byte")) result.append("B");
		else if(type.equals("char")) result.append("C");
		else if(type.equals("double")) result.append("D");
		else if(type.equals("float")) result.append("F");
		else if(type.equals("int")) result.append("I");
		else if(type.equals("long")) result.append("J");
		else if(type.equals("short")) result.append("S");
		else if(type.equals("boolean")) result.append("Z");
		else
		{
			result.append("L");
			result.append(type.replace('.', '/'));
			result.append(";");
		}
		
		return result.toString();
	}

	public void incDimensionCount()
	{
		dimensionCount++;
	}
	
	public static boolean isNext(Parser parser)
	{
		return Id.isNext(parser);
	}

	private int dimensionCount = 0;
}

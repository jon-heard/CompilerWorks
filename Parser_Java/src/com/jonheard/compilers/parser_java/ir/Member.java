package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.expression.Expression;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.parser_java.ir.statement.CodeBlock;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class Member extends BaseIrType
{
	public Member(Parser parser)
	{
		this(parser, false, false, false, false);
	}
	public Member(Parser parser,
			boolean forceVariable, boolean forceNoModifiers,
			boolean forceNoInitializer, boolean forceNoSemicolon)
	{
		super(parser);
		if(forceNoModifiers)
		{
			addChild(new List_Modifier());
		}
		else
		{
			addChild(new List_Modifier(parser));
		}
		addChild(new Type(parser));
		addChild(new Id(parser));
		if(!forceVariable && parser.passTokenIfType(TokenType.PAREN_LEFT))
		{
			_isMethod = true;
			addChild(new List_Variable(parser));
			parser.requireTokenToBeOfType(TokenType.PAREN_RIGHT);
			addChild(new CodeBlock(parser));
		}
		else
		{
			while(parser.passTokenIfType(TokenType.SQUARE_BRACE_LEFT))
			{
				parser.requireTokenToBeOfType(TokenType.SQUARE_BRACE_RIGHT);
				getJavaType().incDimensionCount();
			}
			if(!forceNoInitializer && parser.passTokenIfType(TokenType.EQUAL))
			{
				addChild(Parser_Expression.parseExpression(parser));
			}
			if(!forceNoSemicolon)
			{
				parser.requireTokenToBeOfType(TokenType.SEMICOLON);
			}
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"id='" + getId().getValue() + "' " +
				"type='" + getJavaType().getValue() + "' " +
				"modifiers='" + getModifiers().getValue() + "' " +
				"isMethod='" + isMethod() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 3; }
	
	public List_Modifier getModifiers()
	{
		return (List_Modifier)getChild(0);
	}
	
	public Type getJavaType()
	{
		return (Type)getChild(1);
	}
	
	public Id getId()
	{
		return (Id)getChild(2);
	}
	
	public void setId(Id value)
	{
		replaceChild(2, value);
	}
	
	public boolean isMethod()
	{
		return _isMethod;
	}
	
	public Expression getInitializer()
	{
		if(isMethod()) return null;
		if(getChildCount() < 3) return null;
		return (Expression)getChild(3);
	}
	
	public List_Variable getParameterList()
	{
		if(!isMethod()) return null;
		return (List_Variable)getChild(3);
	}
	
	public CodeBlock getCodeBlock()
	{
		if(!isMethod()) return null;
		return (CodeBlock)getChild(4);
	}
	
	public String toJvmDescriptor()
	{
		StringBuffer result = new StringBuffer();
		if(isMethod())
		{
			List_Variable params = getParameterList();
			result.append('(');
			for(int i = 0; i < params.getChildCount(); i++)
			{
				Type pType = ((Member)params.getChild(i)).getJavaType();
				result.append(pType.toJvmDescriptor());
			}
			result.append(')');
		}
		result.append(getJavaType().toJvmDescriptor());
		return result.toString();
	}
	
	public static boolean isNext(Parser parser)
	{
		boolean result = false;
		parser.getTokenQueue().remember();
		new List_Modifier(parser);
		if(Type.isNext(parser))
		{
			new Type(parser);
			if(Id.isNext(parser))
			{
				result = true;
			}
		}
		parser.getTokenQueue().rewind();
		return result;
	}
	
	private boolean _isMethod = false;
}

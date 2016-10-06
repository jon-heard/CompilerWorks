package com.jonheard.compilers.parser_java.ir;

import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.expression.Expression;
import com.jonheard.compilers.parser_java.ir.expression.Parser_Expression;
import com.jonheard.compilers.parser_java.ir.statement.CodeBlock;
import com.jonheard.compilers.tokenizer_java.TokenType;

public class MethodOrVariable extends BaseIrType
{
	public MethodOrVariable(Parser_Java parser)
	{
		this(parser, false, false, false, false);
	}
	public MethodOrVariable(Parser_Java parser,
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
		if(!forceVariable && parser.have(TokenType.PAREN_LEFT))
		{
			_isMethod = true;
			addChild(new List_Variable(parser));
			parser.mustBe(TokenType.PAREN_RIGHT);
			addChild(new CodeBlock(parser));
		}
		else
		{
			while(parser.have(TokenType.SQUARE_BRACE_LEFT))
			{
				parser.mustBe(TokenType.SQUARE_BRACE_RIGHT);
				getType().incDimensionCount();
			}
			if(!forceNoInitializer && parser.have(TokenType.EQUAL))
			{
				addChild(Parser_Expression.parseExpression(parser));
			}
			if(!forceNoSemicolon)
			{
				parser.mustBe(TokenType.SEMICOLON);
			}
		}
	}

	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "' " +
				"type='" + getType().getValue() + "' " +
				"modifiers='" + getModifiers().getValue() + "' " +
				"isMethod='" + isMethod() + "'";
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
	
	public Id getName()
	{
		return (Id)getChild(2);
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
				Type pType = ((MethodOrVariable)params.getChild(i)).getType();
				result.append(pType.toJvmDescriptor());
			}
			result.append(')');
		}
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

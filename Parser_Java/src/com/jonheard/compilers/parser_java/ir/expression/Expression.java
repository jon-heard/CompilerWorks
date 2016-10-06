package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.BaseIrType;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.util.UtilMethods;

public class Expression extends BaseIrType
{
	public Expression(ExpressionType type, Token first)
	{
		super(first.getLine(), first.getColumn());
		this.expressionType = type;
	}
	public Expression(ExpressionType type, Token first, Expression lhs)
	{
		this(type, first);
		addChild(lhs);
	}
	public Expression(
			ExpressionType type, Token first, Expression lhs, Expression rhs)
	{
		this(type, first, lhs);
		addChild(rhs);
	}
	public Expression(
			ExpressionType type, Token first, Expression lhs, Expression mhs,
			Expression rhs)
	{
		this(type, first, lhs, mhs);
		addChild(rhs);
	}
	
	public Expression getLeftHandSide()
	{
		if(getChildCount() < 1) return null;
		return (Expression)getChild(0);
	}

	public Expression getRightHandSide()
	{
		if(getChildCount() < 2) return null;
		return (Expression)getChild(1);
	}

	public Expression getMiddleHandSide()
	{
		if(getChildCount() < 3) return null;
		return (Expression)getChild(2);
	}

	
	public String getJavaType() { return javaType; }

	public void setJavaType(String value) { javaType = value; }
	
	@Override
	public String getIrTypeName()
	{
		return UtilMethods.constNameToCamelName(getExpressionType()+"", true);
	}
	
	public ExpressionType getExpressionType() { return expressionType; }
	
	@Override
	public String getHeaderString()
	{
		return	"type='" + javaType + "'";
	}
	
	public void calcJavaType()
	{
		String lhs = getLeftHandSide()==null ? null :
			getLeftHandSide().getJavaType();
		String rhs = getRightHandSide()==null ? null :
			getRightHandSide().getJavaType();
		String mhs = getMiddleHandSide()==null ? null :
			getMiddleHandSide().getJavaType();
		if(lhs == null && mhs == null && rhs == null)
		{
			
		}
		else if(lhs != null && mhs == null && rhs == null)
		{
			setJavaType(lhs);
		}
		else if(lhs != null && mhs == null && rhs != null)
		{
			setJavaType(getImplicitCastResult(lhs, rhs));
			if( (lhs == "string" || rhs == "string") &&
				expressionType == ExpressionType.ADD)
			{
				expressionType = ExpressionType.CONCATENATE;
			}
		}
	}
	
	private ExpressionType expressionType;
	private String javaType = "";

	private String getImplicitCastResult(String lhs, String rhs)
	{
		if((lhs.equals("int") && rhs.equals("long"))     || (rhs.equals("int") && lhs.equals("long")))     return "long";
		if((lhs.equals("int") && rhs.equals("float"))    || (rhs.equals("int") && lhs.equals("float")))    return "float";
		if((lhs.equals("int") && rhs.equals("double"))   || (rhs.equals("int") && lhs.equals("double")))   return "double";
		if((lhs.equals("long") && rhs.equals("float"))   || (rhs.equals("long") && lhs.equals("float")))   return "float";
		if((lhs.equals("long") && rhs.equals("double"))  || (rhs.equals("long") && lhs.equals("double")))  return "double";
		if((lhs.equals("float") && rhs.equals("double")) || (rhs.equals("float") && lhs.equals("double"))) return "double";
		return lhs;
	}
}

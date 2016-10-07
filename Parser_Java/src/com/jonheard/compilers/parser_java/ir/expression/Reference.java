package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;

public class Reference extends Expression
{
	public Reference(int line, int column, QualifiedId id)
	{
		super(ExpressionType.REFERENCE, line, column);
		addChild(id);
		addChild(new List_Expression());
		_isMethodCall = false;
	}
	public Reference(Token next, QualifiedId id)
	{
		this(next, id, new List_Expression());
		_isMethodCall = false;
	}
	public Reference(
			Token next, QualifiedId id, List_Expression parameters)
	{
		super(ExpressionType.REFERENCE, next);
		addChild(id);
		addChild(parameters);
	}
	
	public QualifiedId getId() { return (QualifiedId)getChild(0); }

	public List_Expression getParameters()
	{
		return (List_Expression)getChild(1);
	}
	
	public void setId(QualifiedId value) { replaceChild(0, value); }
	
	public Reference getSubReference()
	{
		return (getChildCount() > 2) ? (Reference)getChild(2) : null;
	}
	
	public void setSubReference(Reference value)
	{
		if(getChildCount() > 2)
		{
			replaceChild(2, value);
		}
		else
		{
			addChild(value);
		}
	}
	
	public boolean isMethodCall() { return _isMethodCall; }
	
	@Override
	public String getHeaderString()
	{
		return	"id='" + getId().getValue() + "' " +
				"isMethodCall='" + isMethodCall() + "' " +
				super.getHeaderString();
	}
	
	@Override
	public int getFirstPrintedChildIndex()
	{
		return isMethodCall() ? 1 : 2;
	}
	
	@Override
	public void calcJavaType()
	{
		Reference subReference = getSubReference();
		if(getJavaType().equals("") && subReference != null)
		{
			subReference.calcJavaType();
			setJavaType(subReference.getJavaType());
		}
	}
		
	private boolean _isMethodCall = true;
}

package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;

public class Reference extends Expression
{
	public Reference(int row, int column, QualifiedId id)
	{
		super(ExpressionType.REFERENCE, row, column);
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
	
	public void setId(String value) { getId().setValue(value); }
	
	public Reference getSubReference()
	{
		return (getChildCount() > 2) ? (Reference)getChild(2) : null;
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
	
	public void addPrefix(String value)
	{
		getId().addPrefix(value);
	}
	
	public Reference makeSubReference()
	{
		return makeSubReference(1);
	}
	public Reference makeSubReference(int secondStartIndex)
	{
		if(getSubReference() != null) return null;
		QualifiedId id = getId();
		QualifiedId subId = id.split(secondStartIndex);
		if(subId == null) return null;
		Reference subReference = new Reference(getRow(), getColumn(), subId);
		subReference.replaceChild(1, getChild(1));
		replaceChild(1, new List_Expression());
		subReference._isMethodCall = _isMethodCall;
		_isMethodCall = false;
		subReference.setJavaType(getJavaType());
		addChild(subReference);
		return subReference;
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

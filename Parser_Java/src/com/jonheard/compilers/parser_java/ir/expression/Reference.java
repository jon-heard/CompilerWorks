package com.jonheard.compilers.parser_java.ir.expression;

import java.util.ArrayList;

import com.jonheard.compilers.parser_java.ir.Id;
import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;

public class Reference extends Expression
{
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
		addChild(new QualifiedId(0, 0, new ArrayList<Id>()));
	}
	
	public QualifiedId getId() { return (QualifiedId)getChild(0); }

	public List_Expression getParameters()
	{
		return (List_Expression)getChild(1);
	}
	
	public void setId(QualifiedId value) { replaceChild(0, value); }
	
	public QualifiedId getOwner() { return (QualifiedId)getChild(2); }
	
	public void setOwner(QualifiedId value) { replaceChild(2, value); }
	
	public boolean isMethodCall() { return _isMethodCall; }
	
	@Override
	public String getHeaderString()
	{
		return	"id='" + getId().getValue() + "' " +
				"owner='" + getOwner().getValue() + "' " +
				"isMethodCall='" + isMethodCall() + "' " +
				super.getHeaderString();
	}
	
	@Override
	public int getFirstPrintedChildIndex()
	{
		return isMethodCall() ? 1 : 2;
	}
	
	@Override
	public void calcJavaType() {}
		
	private boolean _isMethodCall = true;
}

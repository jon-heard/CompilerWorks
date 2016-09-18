package com.jonheard.compilers.javaParser.ir.expression;

import com.jonheard.compilers.javaParser.ir.QualifiedIdentifier;

public class FieldReference extends Expression
{

	public FieldReference(int line, QualifiedIdentifier id)
	{
		super(ExpressionType.FIELD_REFERENCE, line);
		addChild(id);
	}
	
	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public QualifiedIdentifier getName()
	{
		return (QualifiedIdentifier)getChild(0);
	}
}

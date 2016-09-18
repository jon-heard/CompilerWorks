package com.jonheard.compilers.javaParser.ir.expression;

import com.jonheard.compilers.javaParser.ir.Identifier;

public class VariableReference extends Expression
{

	public VariableReference(int line, Identifier id)
	{
		super(ExpressionType.VARIABLE_REFERENCE, line);
		addChild(id);
	}
	
	@Override
	public String getHeaderString()
	{
		return	"name='" + getName().getValue() + "'";
	}
	
	@Override
	public int getFirstPrintedChildIndex() { return 1; }
	
	public Identifier getName()
	{
		return (Identifier)getChild(0);
	}
}
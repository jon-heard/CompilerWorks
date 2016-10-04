package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.Identifier;
import com.jonheard.compilers.tokenizer_java.Token;

public class VariableReference extends Expression
{

	public VariableReference(Token next, Identifier id)
	{
		super(ExpressionType.VARIABLE_REFERENCE, next);
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

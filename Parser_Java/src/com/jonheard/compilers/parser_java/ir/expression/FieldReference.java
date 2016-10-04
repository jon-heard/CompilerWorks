package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.QualifiedIdentifier;
import com.jonheard.compilers.tokenizer_java.Token;

public class FieldReference extends Expression
{

	public FieldReference(Token next, QualifiedIdentifier id)
	{
		super(ExpressionType.FIELD_REFERENCE, next);
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

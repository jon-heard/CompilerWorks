package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;

public class FieldReference extends Expression
{

	public FieldReference(Token next, QualifiedId id)
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
	
	public QualifiedId getName()
	{
		return (QualifiedId)getChild(0);
	}
}

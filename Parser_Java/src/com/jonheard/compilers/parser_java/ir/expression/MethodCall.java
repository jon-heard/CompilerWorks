package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedIdentifier;
import com.jonheard.compilers.tokenizer_java.Token;

public class MethodCall extends Expression
{
	public MethodCall(
			Token next, QualifiedIdentifier id, List_Expression parameters)
	{
		super(ExpressionType.METHOD_CALL, next);
		addChild(id);
		addChild(parameters);
	}
	
	public QualifiedIdentifier getIdentifier()
	{
		return (QualifiedIdentifier)getChild(0);
	}

	public List_Expression getParameters()
	{
		return (List_Expression)getChild(1);
	}
	
	public void setIdentifier(QualifiedIdentifier value)
	{
		replaceChild(0, value);
	}
}

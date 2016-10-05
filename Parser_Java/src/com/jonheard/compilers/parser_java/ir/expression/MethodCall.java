package com.jonheard.compilers.parser_java.ir.expression;

import com.jonheard.compilers.parser_java.ir.List_Expression;
import com.jonheard.compilers.parser_java.ir.QualifiedId;
import com.jonheard.compilers.tokenizer_java.Token;

public class MethodCall extends Expression
{
	public MethodCall(
			Token next, QualifiedId id, List_Expression parameters)
	{
		super(ExpressionType.METHOD_CALL, next);
		addChild(id);
		addChild(parameters);
	}
	
	public QualifiedId getId()
	{
		return (QualifiedId)getChild(0);
	}

	public List_Expression getParameters()
	{
		return (List_Expression)getChild(1);
	}
	
	public void setId(QualifiedId value)
	{
		replaceChild(0, value);
	}
}

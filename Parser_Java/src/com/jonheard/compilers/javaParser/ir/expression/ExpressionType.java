package com.jonheard.compilers.javaParser.ir.expression;

//TODO: Make all uppercase
public enum ExpressionType
{
	Assignment,
	Assignment_Add,
	Assignment_Sub,
	Assignment_Mul,
	Assignment_Div,
	Assignment_Mod,
	Equality,
	LogicalAnd,
	LogicalOr,
	Greater,
	Less,
	GreaterOrEqual,
	LessOrEqual,
	Add,
	Sub,
	Mul,
	Div,
	Mod,
	Cast,

	Conditional,
	
	PreIncrement,
	PreDecrement,
	PostIncrement,
	PostDecrement,
	Positive,
	Negative,
	LogicalNot,
	
	MethodCall,
	SuperConstructor,
	ThisConstructor,
	ThisReference,
	NewObject,
	NewArray,
	Variable,
	Field,
	Literal
}

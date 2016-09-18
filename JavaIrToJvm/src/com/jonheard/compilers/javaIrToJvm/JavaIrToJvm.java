package com.jonheard.compilers.javaIrToJvm;

import com.jonheard.compilers.assembler_jvm.backEnd.*;
import com.jonheard.compilers.javaParser.ir.*;
import com.jonheard.compilers.javaParser.ir.Class;

public class JavaIrToJvm
{
	public JavaIrToJvm(com.jonheard.compilers.javaParser.ir.BaseIrType source)
	{
		this.source = source;
	}

	public ClassRep convert()
	{
		convertIr(source, null);
		return result;
	}
	
	private void convertIr(BaseIrType ir, Object owner)
	{
		Object converted = null;
		if(ir instanceof Class)
		{
			Class irClass = (Class)ir;
			converted = new ClassRep(
					irClass.getName().getValue(),
					"",
					irClass.getModifiers().toStringCollection(),
					"");
		}
		else if(ir instanceof Variable)
		{
			Variable irVar = (Variable)ir;
			ClassRep classRep = (ClassRep)owner;
			classRep.addField(
					irVar.getName().getValue(),
					irVar.getType().toJvmDescriptor(),
					irVar.getModifiers().toStringCollection());
		}
		else if(ir instanceof Method)
		{
			Method irMethod = (Method)ir;
			ClassRep classRep = (ClassRep)owner;
			converted = classRep.addMethod(
					irMethod.getName().getValue(),
					irMethod.toJvmDescriptor(),
					irMethod.getModifiers().toStringCollection());
		}
//		else if(ir instanceof Expression)
//		{
//			Expression irExpression = (Expression)ir;
//			MethodCodeBuilder method = (MethodCodeBuilder)owner;
//			switch(irExpression.getType())
//			{
//				case METHOD_CALL:
//				{
//					//method.addOp(Op_Field, );
//				}
//			}
//		}

		for(int i = 0; i < ir.getChildCount(); i++)
		{
			convertIr(ir.getChild(i), converted);
		}
	}
	
	private BaseIrType source;
	private ClassRep result;
}

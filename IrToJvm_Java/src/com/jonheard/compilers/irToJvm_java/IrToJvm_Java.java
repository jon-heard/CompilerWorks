package com.jonheard.compilers.irToJvm_java;

import com.jonheard.compilers.assembler_jvm.backEnd.*;
import com.jonheard.compilers.parser_java.ir.*;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.util.SourceFileInfo;

public class IrToJvm_Java
{
	public ClassRep convert(SourceFileInfo source, BaseIrType ir)
	{
		this.source = source;
		convertIr(ir, null);
		return result;
	}
	
	private void convertIr(BaseIrType ir, Object owner)
	{
		Object newOwner = owner;
		if(ir instanceof Class)
		{
			Class data = (Class)ir;
			newOwner = new ClassRep(
					data.getName().getValue(),
					"",
					data.getModifiers().toStringCollection(),
					source.getFilename());
		}
		else if(ir instanceof MethodOrVariable)
		{
			MethodOrVariable data = (MethodOrVariable)ir;
			if(owner instanceof ClassRep)
			{
				ClassRep classOwner = (ClassRep)owner;
				if(data.isMethod())
				{
					newOwner = classOwner.addMethod(
							data.getId().getValue(),
							data.getJavaType().toJvmDescriptor(),
							data.getModifiers().toStringCollection());
				}
				else
				{
					classOwner.addField(
							data.getId().getValue(),
							data.getJavaType().toJvmDescriptor(),
							data.getModifiers().toStringCollection());
				}
			}
			else
			{
				
			}
		}

		for(int i = 0; i < ir.getChildCount(); i++)
		{
			convertIr(ir.getChild(i), newOwner);
		}
	}
	
	private SourceFileInfo source;
	private ClassRep result;
}

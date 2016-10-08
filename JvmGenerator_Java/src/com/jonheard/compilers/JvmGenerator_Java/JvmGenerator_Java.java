package com.jonheard.compilers.JvmGenerator_Java;

import com.jonheard.compilers.assembler_jvm.backEnd.*;
import com.jonheard.compilers.parser_java.ir.*;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.util.SourceFileInfo;

public class JvmGenerator_Java
{
	public ClassRep generate(SourceFileInfo source, BaseIrType ir)
	{
		this.source = source;
		return generateHelper(ir, null);
	}
	
	private ClassRep generateHelper(BaseIrType ir, Object owner)
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
			generateHelper(ir.getChild(i), newOwner);
		}
		
		return null;
	}
	
	private SourceFileInfo source;
	private ClassRep result;
}

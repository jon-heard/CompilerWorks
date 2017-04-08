package com.jonheard.compilers.JvmGenerator_Java;

import com.jonheard.compilers.assembler_jvm.backEnd.ClassRep;
import com.jonheard.compilers.assembler_jvm.backEnd.FieldRep;
import com.jonheard.compilers.assembler_jvm.backEnd.MethodRep;

public class JvmGeneratorStringConverter
{
	public String generatedToString(JvmGenerator_Java source)
	{
		return source.toString();
//		StringBuilder result = new StringBuilder();
//		result.append("<class " +
//				"name='" + source.getName() + "' " +
//				"super='" + source.getSuperName() + "' " +
//				"modifiers='" + source.getModifiers() + "'>\n");
//		
//		for(int i = 0 ; i < source.getFieldCount(); i++)
//		{
//			FieldRep field = source.getField(i);
//			result.append("	<field " +
//					"name='" + field.getName() + "' " +
//					"descriptor='" + field.getDescriptor() + "' " +
//					"modifiers='" + field.getModifiers() + "'/>\n");
//		}
//
//		for(int i = 0 ; i < source.getMethodCount(); i++)
//		{
//			MethodRep field = source.getMethod(i);
//			result.append("	<method " +
//					"name='" + field.getName() + "' " +
//					"descriptor='" + field.getDescriptor() + "' " +
//					"modifiers='" + field.getModifiers() + "'>\n");
//			result.append("	</method>\n");
//		}
//		
//		result.append("</class>\n");
//		return result.toString();
	}
}

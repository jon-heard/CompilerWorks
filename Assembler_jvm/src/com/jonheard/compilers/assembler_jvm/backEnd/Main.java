
package com.jonheard.compilers.assembler_jvm.backEnd;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import com.jonheard.compilers.assembler_jvm.frontEnd.MethodParser;

/*
 * Main - A testing area for This jvmClassBuilder system.
 */
public class Main
{
	public static void main(String[] args) throws IOException
	{
		/// Setup class to be written
		String[] classModList = {"public"};
		ClassRep classRep = new ClassRep(
				"Test2", "java/lang/Object",
				Arrays.asList(classModList), "Test2.java");

		/// Field - toPrint
		{
			String[] modList = {"private"};
			classRep.addField(
					"toPrint", "Ljava/lang/String;", Arrays.asList(modList));
		}
		
		/// constructor 
		{
			String[] modList = {"public"};
			MethodCodeBuilder m = classRep.addMethod(
				"<init>", "V", null, Arrays.asList(modList));
			m.addOp(MethodCodeBuilder.Op_NoArg._aload_0);
			m.addOp(MethodCodeBuilder.Op_Method._invokespecial,
					"java/lang/Object", "<init>", "()V");
			m.addOp(MethodCodeBuilder.Op_NoArg._return);
		}
		
		/// main method
		{
			String[] argList = { "[Ljava/lang/String;" };
			String[] modList = {"public", "static"};
			MethodCodeBuilder m = classRep.addMethod(
				"main", "V", Arrays.asList(argList),
				Arrays.asList(modList));
			m.addOp(MethodCodeBuilder.Op_Class._new, "Test2");
			m.addOp(MethodCodeBuilder.Op_NoArg._dup);
			m.addOp(MethodCodeBuilder.Op_Method._invokespecial, "Test2", "<init>", "()V");
			m.addOp(MethodCodeBuilder.Op_NoArg._astore_1);
			m.addOp(MethodCodeBuilder.Op_NoArg._aload_1);
			m.addOp(MethodCodeBuilder.Op_Method._invokevirtual, "Test2", "run", "()V");
			m.addOp(MethodCodeBuilder.Op_NoArg._return);
		}

		/// run method
		{
			String[] modList = {"public"};
			MethodCodeBuilder m = classRep.addMethod(
				"run", "V", null, Arrays.asList(modList));
			StringBuilder codeData = new StringBuilder();
			codeData.append("aload_0\n");
			codeData.append("ldc \"hiyooo\"\n");
			codeData.append("putField Test2 toPrint Ljava/lang/String;\n");
			codeData.append("getStatic java/lang/System out Ljava/io/PrintStream;\n");
			codeData.append("aload_0\n");
			codeData.append("getField Test2 toPrint Ljava/lang/String;\n");
			codeData.append("invokeVirtual java/io/PrintStream println (Ljava/lang/String;)V\n");
			codeData.append("return");
			MethodParser mp = new MethodParser();
			mp.parseSource(m, codeData.toString());

//			m.addOp(MethodBuilder.Op_NoArg._aload_0);
//			m.addOp(MethodBuilder.Op_String._ldc, "hiyoo");
//			m.addOp(MethodBuilder.Op_Field._putField, "Test2", "toPrint", "Ljava/lang/String;");
//			m.addOp(MethodBuilder.Op_Field._getStatic, "java/lang/System", "out", "Ljava/io/PrintStream;");
//			m.addOp(MethodBuilder.Op_NoArg._aload_0);
//			m.addOp(MethodBuilder.Op_Field._getField, "Test2", "toPrint", "Ljava/lang/String;");
//			m.addOp(MethodBuilder.Op_Method._invokeVirtual, "java/io/PrintStream", "println", "(Ljava/lang/String;)V");
//			m.addOp(MethodBuilder.Op_NoArg._return);
		}

		/// test method
		{
			String[] argList = { "[Ljava/lang/String;", "I" };
			String[] modList = {"public"};
			MethodCodeBuilder m = classRep.addMethod(
				"test", "V", Arrays.asList(argList), Arrays.asList(modList));
			m.addOp(MethodCodeBuilder.Op_NoArg._return);
		}
		
		/// Write class to a file
		FileOutputStream out = new FileOutputStream("Test2.class");
		out.write(classRep.getJvmBytes());
		out.close();
	}
}
 
package com.jonheard.compilers.JvmGenerator_Java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.jonheard.compilers.assembler_jvm.backEnd.*;
import com.jonheard.compilers.assembler_jvm.backEnd.MethodCodeBuilder.*;
import com.jonheard.compilers.parser_java.ir.*;
import com.jonheard.compilers.parser_java.ir.Class;
import com.jonheard.compilers.parser_java.ir.expression.*;
import com.jonheard.compilers.parser_java.ir.statement.*;
import com.jonheard.util.SourceFile;

public class JvmGenerator_Java
{
	public ClassRep generate(SourceFile source, CompilationUnit compUnit)
	{
		this.source = source;
		handleIr(compUnit);
		return classRep;
	}
	
	@Override
	public String toString()
	{
		if(mainLog == null) return "";
		return
				mainLog.toString() + methodLog.toString() +
				staticLog.toString() + "	</method>\n</class>\n";
	}

	public boolean getLogToString()
	{
		return mainLog != null;
	}
	public void setLogToString(boolean value)
	{
		if(value && mainLog != null) return;
		if(value)
		{
			mainLog = new StringBuffer();
			staticLog = new StringBuffer();
			methodLog = staticLog;
			logMethod("static", "()V", Arrays.asList(new String[] {"static"}));
			methodLog = new StringBuffer();
		}
		else
		{
			mainLog = methodLog = staticLog = null; 
		}
	}
	
	private StringBuffer mainLog = null;
	private StringBuffer methodLog = null;
	private StringBuffer staticLog = null;

	private SourceFile source;
	private ClassRep classRep;
	private MethodCodeBuilder currentMethod;
	private MethodCodeBuilder staticBlock = null;

	private void handleIr(BaseIrType ir)
	{
		if(ir instanceof CompilationUnit)
		{
			handleCompilationUnit(ir);
		}
		else if(ir instanceof Class)
		{
			handleClass(ir);
		}
		else if(ir instanceof Member)
		{
			handleMethodOrVariable(ir);
		}
		else if(ir instanceof CodeBlock)
		{
			handleCodeBlock(ir);
		}
		else if(ir instanceof Literal)
		{
			handleLiteral(ir);
		}
		else if(ir instanceof Reference)
		{
			handleReference(ir);
		}
		else if(ir instanceof Expression)
		{
			handleExpression(ir);
		}
	}
	
	private void handleCompilationUnit(BaseIrType ir)
	{
		for(int i = 0; i < ir.getChildCount(); i++)
		{
			BaseIrType child = ir.getChild(i);
			if(child instanceof Class)
			{
				handleIr(child);
			}
		}
	}
	
	private void handleClass(BaseIrType ir)
	{
		Class data = (Class)ir;
		classRep = new ClassRep(
				data.getName().getValue(),
				data.getSuper().getValue().replace('.', '/'),
				data.getModifiers().toStringCollection(),
				"Test1.java");
//				source.getFilename());
		logClass(
				data.getName().getValue(), data.getSuper().getValue(),
				data.getModifiers().toStringCollection());
		classRep.addDefaultConstructor("java/lang/Object");
		List<String> modifiers = new ArrayList<String>();
		modifiers.add("static");
		staticBlock = classRep.addMethod("static", "()V", modifiers);

		for(int i = 2; i < ir.getChildCount(); i++)
		{
			handleIr(ir.getChild(i));
		}
		staticBlock.addOp(Op_NoArg._return);
		StringBuffer buf = methodLog;
		methodLog = staticLog;
		logOp("return");
		methodLog = buf;
	}
	
	private void handleMethodOrVariable(BaseIrType ir)
	{
		Member data = (Member)ir;
		if(currentMethod == null)
		{
			if(data.isMethod())
			{
				currentMethod = classRep.addMethod(
						data.getId().getValue(),
						data.toJvmDescriptor(),
						data.getModifiers().toStringCollection());
				logMethod(
						data.getId().getValue(),
						data.toJvmDescriptor(),
						data.getModifiers().toStringCollection());
				handleIr(data.getCodeBlock());
				currentMethod.addOp(Op_NoArg._return);
				logOp("return");
				currentMethod = null;

				StringBuffer buf = mainLog;
				mainLog = methodLog;
				log("	</method>");
				mainLog = buf;
			}
			else
			{
				classRep.addField(
						data.getId().getValue(),
						data.getJavaType().toJvmDescriptor(),
						data.getModifiers().toStringCollection());
				logField(
						data.getId().getValue(),
						data.getJavaType().toJvmDescriptor(),
						data.getModifiers().toStringCollection());
				StringBuffer buf = methodLog;
				methodLog = staticLog;
				Expression initializer = data.getInitializer();
				if(initializer != null)
				{
					currentMethod = staticBlock;
					handleIr(initializer);
					if(data.getModifiers().isStatic())
					{
						currentMethod.addOp(
								Op_Field._putstatic, classRep.getName(),
								data.getId().getValue(),
								data.getJavaType().toJvmDescriptor());
						logOp(
								"putstatic",
								classRep.getName(), data.getId().getValue(),
								data.getJavaType().toJvmDescriptor());
					}
					else
					{
//						method.addOp(
//								Op_Field._putfield, className,
//								data.getId().getValue(),
//								data.getJavaType().toJvmDescriptor());
					}
					currentMethod = null;
				}
				methodLog = buf;
			}
		}
		else
		{
			Expression initializer = data.getInitializer();
			if(initializer != null)
			{
				handleIr(initializer);
				int id = Integer.parseInt(data.getId().getValue().substring(1));
				if(id == 0)
				{
					currentMethod.addOp(Op_NoArg._istore_0);
					logOp("istore_0");
				}
				else if(id == 1)
				{
					currentMethod.addOp(Op_NoArg._istore_1);
					logOp("istore_1");
				}
				else if(id == 2)
				{
					currentMethod.addOp(Op_NoArg._istore_2);
					logOp("istore_2");
				}
				else if(id == 3)
				{
					currentMethod.addOp(Op_NoArg._istore_3);
					logOp("istore_3");
				}
				else
				{
					currentMethod.addOp(Op_Integer._istore, id);
					logOp("istore", id);
				}
			}
		}
	}
	
	private void handleCodeBlock(BaseIrType ir)
	{
		for(int i = 0; i < ir.getChildCount(); i++)
		{
			handleIr(ir.getChild(i));
		}
	}
	
	private void handleExpression(BaseIrType ir)
	{
		Expression data = (Expression)ir;
		switch(data.getExpressionType())
		{
			case ADD:
				handleIr(data.getLeftHandSide());
				handleIr(data.getRightHandSide());
				currentMethod.addOp(Op_NoArg._iadd);
				logOp("iadd");
			default:
		}
	}
	
	private void handleLiteral(BaseIrType ir)
	{
		Literal data = (Literal)ir;
		if(data.getLiteralType().equals("integer"))
		{
			int value = Integer.parseInt(data.getLiteralValue());
			if(value == 0) currentMethod.addOp(Op_NoArg._iconst_0);
			else if(value == 1)
			{
				currentMethod.addOp(Op_NoArg._iconst_1);
				logOp("iconst_1");
			}
			else if(value == 2)
			{
				currentMethod.addOp(Op_NoArg._iconst_2);
				logOp("iconst_2");
			}
			else if(value == 3)
			{
				currentMethod.addOp(Op_NoArg._iconst_3);
				logOp("iconst_3");
			}
			else if(value == 4)
			{
				currentMethod.addOp(Op_NoArg._iconst_4);
				logOp("iconst_4");
			}
			else if(value == 5)
			{
				currentMethod.addOp(Op_NoArg._iconst_5);
				logOp("iconst_5");
			}
			else if(Math.abs(value) < 128)
			{
				currentMethod.addOp(Op_Byte._bipush, value);
				logOp("bipush", value);
			}
			else if(Math.abs(value) < 32768)
			{
				currentMethod.addOp(Op_Short._sipush, value);
				logOp("sipush", value);
			}
			else
			{
				currentMethod.addOp(Op_Integer._ldc, value);
				logOp("ldc", value);
			}
		}
	}
	
	private void handleReference(BaseIrType ir)
	{
		Reference data = (Reference)ir;
		if(data.getId().getValue().charAt(0) == '#')
		{
			int stackElement =
					Integer.parseInt(data.getId().getValue().substring(1));
			switch(stackElement)
			{
				case 0:
					currentMethod.addOp(Op_NoArg._iload_0);
					logOp("iload_0");
					break;
				case 1:
					currentMethod.addOp(Op_NoArg._iload_1);
					logOp("iload_1");
					break;
				case 2:
					currentMethod.addOp(Op_NoArg._iload_2);
					logOp("iload_2");
					break;
				case 3:
					currentMethod.addOp(Op_NoArg._iload_3);
					logOp("iload_3");
					break;
				default:
					currentMethod.addOp(Op_Integer._iload, stackElement);
					logOp("iload", stackElement);
			}
			return;
		}
		Reference sub = data.getSubReference();
		if(sub.isMethodCall())
		{
			List_Expression parameters = sub.getParameters();
			for(int i = 0; i < parameters.getChildCount(); i++)
			{
				handleIr(parameters.getChild(i));
			}
			currentMethod.addOp(
					Op_Method._invokevirtual, "java/io/PrintStream",
					sub.getId().getValue(), "(I)V");
			logOp(
					"invokevirtual", "java/io/PrintStream",
					sub.getId().getValue(), "(I)V");
		}
		else
		{
			String ref = data.getId().getValue().replace('.', '/');
			String subRef = sub.getId().getValue();
			String type = "I";
			if(sub.getId().getValue().equals("out"))
			{
				type = "Ljava/io/PrintStream;";
			}
			currentMethod.addOp(Op_Field._getstatic, ref, subRef, type);
			logOp("getstatic", ref, subRef, type);
			if(sub != null && sub.getSubReference() != null)
			{
				handleIr(sub);
			}
		}
	}

	private void log(String value)
	{
		if(mainLog == null) return;
		mainLog.append(value);
		mainLog.append("\n");
	}
	private void logClass(
			String name, String superName, Collection<String> modifiers)
	{
		if(mainLog == null) return;
		log(
				"<class name='" + name + "' super='" + superName +
				"' modifiers='" + modifiers.toString() + "'>");
	}
	private void logField(
			String name, String descriptor, Collection<String> modifiers)
	{
		if(mainLog == null) return;
		log(
				"	<field name='" + name + "' descriptor='" + descriptor +
				"' modifiers='" + modifiers.toString() + "'/>");
	}
	private void logMethod(
			String name, String descriptor, Collection<String> modifiers)
	{
		if(mainLog == null) return;
		StringBuffer buf = mainLog;
		mainLog = methodLog;
		log(
				"	<method name='" + name + "' descriptor='" + descriptor +
				"' modifiers='" + modifiers.toString() + "'>");
		mainLog = buf;
	}
	private void logOp(String name, Object... arguments)
	{
		if(mainLog == null) return;
		StringBuffer toLog = new StringBuffer();
		toLog.append("		<op name='");
		toLog.append(name);
		if(arguments.length > 0)
		{
			toLog.append("' arguments='");
			for(int i = 0; i < arguments.length; i++)
			{
				toLog.append(arguments[i]);
				if(i < arguments.length-1)
				{
					toLog.append(",");
				}
			}
		}
		toLog.append("'/>");
		StringBuffer buf = mainLog;
		mainLog = methodLog;
		log(toLog.toString());
		mainLog = buf;
	}
}

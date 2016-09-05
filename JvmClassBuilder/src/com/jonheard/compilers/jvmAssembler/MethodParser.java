package com.jonheard.compilers.jvmAssembler;

import java.util.List;

import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder;
import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.Op_Byte;
import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.Op_Class;
import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.Op_Field;
import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.Op_Label;
import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.Op_Method;
import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.Op_NoArg;
import com.jonheard.compilers.jvmClassBuilder.MethodCodeBuilder.Op_String;
import com.jonheard.util.HelperMethods;

/// Used to deserialize a MethodRep's code attribute based upon a source string
/// following JvmAsm conventions.
public class MethodParser
{
	/// Takes a MethodCodeBuilder object to build the method from and a source
	/// string that follows JvmAsm conventions (for method logic) to base the
	/// building upon.
	public boolean parseSource(
			MethodCodeBuilder codeBuilder, String logicSource)
	{
		return parseSource(codeBuilder, logicSource.split("\r\n|\n\r|\r|\n"));		
	}
	/// Takes a MethodCodeBuilder object to build the method from and an array
	/// of string lines that follow JvmAsm conventions (for method logic) to
	/// base the building upon.
	public boolean parseSource(
			MethodCodeBuilder codeBuilder, String[] logicSource)
	{
		boolean error = false;

		/// Translate the args into opcodes and add to codeBuilder
		for(int i = 0; i < logicSource.length; i++)
		{
			List<String> args = HelperMethods.tokenizeString(logicSource[i]);
			int curArg = 0;
			/// Label
			if(args.get(curArg).endsWith(":"))
			{
				String newLabel = args.get(curArg);
				newLabel = newLabel.substring(0, newLabel.length()-1);
				codeBuilder.addLabel(newLabel);
				curArg++;
				if(args.size() == 1)
				{
					continue;
				}
			}
			
			/// Op
			String opName = "_" + args.get(curArg);
			curArg++;
			
			/// NoArg ops
			if(HelperMethods.enumContainsString(Op_NoArg.class, opName))
			{
				Op_NoArg op = Op_NoArg.valueOf(opName);
				codeBuilder.addOp(op);
			}
			/// Class ops
			else if(HelperMethods.enumContainsString(Op_Class.class, opName))
			{
				Op_Class op = MethodCodeBuilder.Op_Class.valueOf(opName);
				codeBuilder.addOp(op, args.get(curArg));
			}
			/// Byte ops
			else if(HelperMethods.enumContainsString(Op_Byte.class, opName))
			{
				Op_Byte op = MethodCodeBuilder.Op_Byte.valueOf(opName);
				codeBuilder.addOp(op, Integer.parseInt(args.get(curArg)));
			}
			/// Label ops
			else if(HelperMethods.enumContainsString(Op_Label.class, opName))
			{
				Op_Label op = MethodCodeBuilder.Op_Label.valueOf(opName);
				codeBuilder.addOp(op, args.get(curArg));
			}
			/// Method ops
			else if(HelperMethods.enumContainsString(Op_Method.class, opName))
			{
				Op_Method op = MethodCodeBuilder.Op_Method.valueOf(opName);
				codeBuilder.addOp(
						op,
						args.get(curArg), args.get(curArg+1),
						args.get(curArg+2));
			}
			/// Field ops
			else if(HelperMethods.enumContainsString(Op_Field.class, opName))
			{
				Op_Field op = MethodCodeBuilder.Op_Field.valueOf(opName);
				codeBuilder.addOp(
						op,
						args.get(curArg), args.get(curArg+1),
						args.get(curArg+2));
			}
			/// String ops
			else if(HelperMethods.enumContainsString(Op_String.class, opName))
			{
				Op_String op = MethodCodeBuilder.Op_String.valueOf(opName);
				codeBuilder.addOp(op, args.get(curArg));
			}
			/// Unrecognized ops
			else
			{
				System.err.println(
						"ERROR: Invalid mnemonic '" + opName +
						"' on line " + i + ".");
				error = true;
			}
		}
		/// Make sure that we end up with a balanced stack
		if(codeBuilder.getStackCounter() != 0)
		{
			System.out.println(
					"Unbalanced stack in method: " +
					codeBuilder.getMethodRep().getName());
			error = true;
		}
		return !error;
	}
}

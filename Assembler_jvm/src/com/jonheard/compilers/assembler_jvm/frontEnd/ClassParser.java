package com.jonheard.compilers.assembler_jvm.frontEnd;

import java.util.ArrayList;

import com.jonheard.compilers.assembler_jvm.backEnd.ClassRep;
import com.jonheard.compilers.assembler_jvm.backEnd.MethodCodeBuilder;

/// Used to deserialize a ClassRep object from a source string following JvmAsm
/// conventions.
public class ClassParser
{
	private String filename;
	private String source;
	private String[] lines;
	private int curLine;

	public ClassRep parseSource(String filename, String source)
	{
		this.filename = filename;
		this.source = source;

		ClassRep result;
		
		prepSourceCode();
		result = parseClassRep();		
		return result;
	}

	/// Simplify JvmAsm source code for easier parsing and populate "lines"
	/// variable with individual line strings
	private void prepSourceCode()
	{
//		JvmAssembler.verboseLog("Prepping source code");
		source = source.replaceAll("[ |\t]+", " "); /// Unify whitespace
		source = source.replaceAll("\r", "\n"); /// Unify newline types
		source = source.replaceAll(" \n |\n | \n", "\n"); /// trim
		source = source.replaceAll("//.*\n", ""); /// Comments
		source = source.replaceAll("\n+", "\n"); /// Get rid of empty lines
		lines = source.split("\n");
		curLine = 0;
	}
	
	private ClassRep parseClassRep()
	{
		ClassRep result;
		boolean error = false;
		
		/// Get header values
		String[] tokens = lines[curLine].split(" ");
		String thisClass = tokens[0];
		String superClass = tokens[1];
		ArrayList<String> modifiers = parseModifiers(tokens);
//		JvmAssembler.verboseLog("Parsing class '" + thisClass + "'.");
		result = new ClassRep(thisClass, superClass, modifiers, filename);
		
		/// Check for curly braces
		curLine++;
		if(!confirmOpeningCurly(thisClass)) error = true;
		curLine++;
		
		/// Parse Members
//		JvmAssembler.verboseLog("Parsing class members");
		while(!lines[curLine].equals("}"))
		{
			if(!parseMember(result))
			{
				error = true;
			}
		}
//		JvmAssembler.verboseLog("Done parsing class members");

		/// Return
		if(error) result = null;
		return result;
	}

	private ArrayList<String> parseModifiers(String[] tokens)
	{
		ArrayList<String> result = new ArrayList<String>();
		for(int i = 2; i < tokens.length; i++)
		{
			result.add(tokens[i]);
		}
		return result;
	}

	private boolean parseMember(ClassRep classRep)
	{
		boolean error = false;
		String[] tokens = lines[curLine].split(" ");
		if(tokens.length < 2)
		{
			curLine++;
			return false;
		}
		String name = tokens[0];
		String descriptor = tokens[1];
		ArrayList<String> modifiers = parseModifiers(tokens);

		/// Field
		if(descriptor.charAt(0) != '(')
		{
//			JvmAssembler.verboseLog("Parsing member '" + name + "' as a field.");
			if(!parseField(classRep, name, descriptor, modifiers))
			{
				error = true;
			}
		}

		/// Method
		else
		{
//			JvmAssembler.verboseLog("Parsing member '" + name + "' as a method.");
			if(!parseMethod(classRep, name, descriptor, modifiers))
			{
				error = true;
			}
		}

		curLine++;
		return !error;
	}

	private boolean parseField(
			ClassRep classRep, String name, String descriptor,
			ArrayList<String> modifiers)
	{
		classRep.addField(name, descriptor, modifiers);
		return true;
	}
	
	private boolean parseMethod(
			ClassRep classRep, String name, String descriptor,
			ArrayList<String> modifiers)
	{
		boolean error = false;

		/// Build the method from the given data
		MethodCodeBuilder m = classRep.addMethod(name, descriptor, modifiers);

		/// Check for curly brace
		curLine++;
		if(!confirmOpeningCurly(name)) error = true;
		curLine++;

		/// Find the end of the method logic
		int codeEnd = curLine;
		while(!lines[codeEnd].equals("}"))
		{
			codeEnd++;
		}

		/// Create a new String of the method's logic
		String[] methodLogic = new String[codeEnd-curLine];
		System.arraycopy(lines, curLine, methodLogic, 0, methodLogic.length);

		/// Parse method logic
//		JvmAssembler.verboseLog("Parsing logic for method '" + name + "'.");
		MethodParser methodParser = new MethodParser();
		if(!methodParser.parseSource(m, methodLogic)) error = true;
		curLine = codeEnd;
		return !error;
	}
	
	private boolean confirmOpeningCurly(String name)
	{
		if(!lines[curLine].equals("{"))
		{
			System.err.println(
					"ERROR: Missing opening curly brace for: " + name);
			return false;
		}
		return true;
	}
}

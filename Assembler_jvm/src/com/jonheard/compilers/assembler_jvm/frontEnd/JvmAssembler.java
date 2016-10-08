package com.jonheard.compilers.assembler_jvm.frontEnd;

import java.io.File;

import com.jonheard.compilers.assembler_jvm.backEnd.ClassRep;
import com.jonheard.util.UtilMethods;

/// The driver class for JvmAsm - the sourcefile assembly system
public class JvmAssembler
{
	public static final String VERSION = "0.7.0";
	
	public static void main(String[] args)
	{
		/// Print console header
		System.out.println("Jon Heard's JVM Assembler");
		System.out.println("version " + VERSION);
		System.out.println("----------------------------");
		System.out.println(" ('-help' or '-h' for help)");
		System.out.println("----------------------------");
		
		/// Parse arguments
		boolean error = false;
		boolean helpTextPrinted = false;
		String sourceFileName = "";		
		for(String arg : args)
		{
			if(arg.startsWith("-"))
			{
				if(arg.equals("-help") || arg.equals("-h"))
				{
					printHelpText();
					helpTextPrinted = true;
				}
				else
				{
					System.err.println(
							"ERROR: Invalid parameter '" + arg + "'.");
					error = true;
				}
			}
			else if(!sourceFileName.equals(""))
			{
				System.err.println("ERROR: Invalid parameter '" + arg + "'.");
				error = true;
			}
			else
			{
				sourceFileName = arg;
			}
		}
		if(error || helpTextPrinted) return;

		/// Make sure we have a source file to load
		if(args.length < 1)
		{
			System.err.println("ERROR: A sourcefile must be included.");
			System.err.println("\tExample: ");
			System.err.println("\tjvmasm MySource.asm");
			return;
		}

		/// Make sure the source file exists
		File sourceFile = new File(sourceFileName);
		if(!sourceFile.exists())
		{
			System.err.println(
					"ERROR: the source file '" + sourceFileName +
					"' does not exist.");
			return;
		}

		/// Load the source file into a string
		String sourceData = UtilMethods.fileToString(sourceFileName);
		if(sourceData == null)
		{
			System.err.println(
					"ERROR: Unable to load the source file '" + sourceFileName +
					"'.");
			return;
		}

		/// Parse the source file into a ClassRep object
		ClassParser parser = new ClassParser();
		ClassRep classRep = parser.parseSource(sourceFileName, sourceData);
		if(classRep == null)
		{
			System.err.println(
					"ERROR: Failed to parse source file '" + sourceFileName +
					"'.");
			return;
		}
		
		/// Store the ClassRep into a class file
		String classFileName = classRep.getName() + ".class";
		if(!UtilMethods.byteArrayToFile(
				classRep.getJvmBytes(), classFileName))
		{
			System.err.println(
					"ERROR: Failed to write class file '" + classFileName +
					"'.");
			return;
		}
	}

	private static void printHelpText()
	{
		System.out.println("Command line arguments include:");
		System.out.println("   -help (or -h)     Show this help message");
		System.out.println("   non '-'           argument source file to " +
				"assemble");
		System.out.println("Examples:");
		System.out.println("   jvmasm src.asm");
		System.out.println("      Assemble the file 'src.asm'");
		System.out.println("   jvmasm -h");
		System.out.println("      Show this help message");
	}
}

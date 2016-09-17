
package com.jonheard.compilers;

import com.jonheard.compilers.javaParser.JavaParser;
import com.jonheard.compilers.javaTokenizer.JavaTokenizer;
import com.jonheard.util.HelperMethods;

public class JavaC
{
	public static final String HEADER_TEXT =  
			"------------------------------------------------\n" +
			" Java Compiler by Jonathan Heard v.0.04.000\n" +
			"------------------------------------------------\n";

	public static final String HELP_TEXT =
			"Usage: javac <source file> <options>\n" +
			"where possible options include:\n" +
			"-help (-h)      Print this help panel and quit\n" +
			"-tokenize (-t)  Run the tokenizer step only, printing results" +
			"-parse (-p)     Run up to the parser step only, printing results";
	
	public static void main(String[] args)
	{
		JavaC app = new JavaC();
		System.out.println(app.compile(args));
	}
	
	public String compile(String[] args)
	{
		StringBuffer result = new StringBuffer();
		result.append(HEADER_TEXT);

		boolean help = false;
		Stage finalStage = Stage.buildClassFile;
		String sourceFile = "";

		for(int i = 0; i < args.length; i++)
		{
			switch(args[i])
			{
			case "-help":
			case "-h":
				help = true;
				break;
			case "-tokenize":
			case "-t":
				finalStage = Stage.tokenize;
				break;
			case "-parse":
			case "-p":
				finalStage = Stage.parse;
				break;
			default:
				sourceFile = args[i];
				break;
			}
		}
		
		// Show help message
		if(help || sourceFile.equals(""))
		{
			result.append(HELP_TEXT);
			return result.toString();
		}
		// Load the file
		String source = HelperMethods.fileToString(sourceFile);
		if(source == null)
		{
			result.append("ERROR: Invalid filename: " + sourceFile);
			return result.toString();
		}
		JavaTokenizer t = new JavaTokenizer(sourceFile, source);
		if(finalStage == Stage.tokenize)
		{
			result.append(t.tokenizeToString());
			return result.toString();
		}
		JavaParser parser = new JavaParser(t.tokenize());
		if(finalStage == Stage.parse)
		{
			result.append(parser.parseToString());
			return result.toString();
		}
		return result.toString();
	}

	private enum Stage
	{
		tokenize,
		parse,
		evaluateTypes,
		buildClassFile
	}
}


package com.jonheard.compilers;

import java.util.List;

import com.jonheard.compilers.irProcessor_java.IrProcessor_Java;
import com.jonheard.compilers.javaClasspathDatabase.JavaClasspathDatabase;
import com.jonheard.compilers.parser_java.ParserStringConverter;
import com.jonheard.compilers.parser_java.Parser_Java;
import com.jonheard.compilers.parser_java.ir.CompilationUnit;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.Tokenizer;
import com.jonheard.compilers.tokenizer_java.TokenizerStringConverter;
import com.jonheard.util.SourceFileInfo;
import com.jonheard.util.UtilMethods;

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
			"-tokenize (-t)  Run up to the tokenizer step, printing results" +
			"-parse (-p)     Run up to the parser step, printing results" +
			"-process (-r)   Run up to the processing step, printing results";

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
				case "-process":
				case "-r":
					finalStage = Stage.process;
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
		String sourceCode = UtilMethods.fileToString(sourceFile);
		if(sourceCode == null)
		{
			result.append("ERROR: Invalid filename: " + sourceFile);
			return result.toString();
		}
		SourceFileInfo source = new SourceFileInfo(sourceFile, sourceCode);

		// Tokenize
		Tokenizer tokenizer = new Tokenizer();
		List<Token> tokenized = tokenizer.tokenize(source);
		if(finalStage == Stage.tokenize)
		{
			TokenizerStringConverter converter = new TokenizerStringConverter();
			result.append(converter.tokenizedToString(tokenized));
			return result.toString();
		}

		// Parse
		Parser_Java parser = new Parser_Java();
		CompilationUnit parsed = parser.parse(source, tokenized);
		if(finalStage == Stage.parse)
		{
			ParserStringConverter converter = new ParserStringConverter();
			result.append(converter.parsedToString(parsed));
			return result.toString();
		}
		
		// Setup classpath database
		JavaClasspathDatabase libs = new JavaClasspathDatabase();
		libs.addSource_Jdk();

		// Process
		IrProcessor_Java processor = new IrProcessor_Java();
		processor.process(libs, parsed);
		if(finalStage == Stage.process)
		{
			ParserStringConverter converter = new ParserStringConverter();
			result.append(converter.parsedToString(parsed));
			return result.toString();
		}
		
		return "";
	}

	private enum Stage
	{
		tokenize,
		parse,
		process,
		buildClassFile
	}
}

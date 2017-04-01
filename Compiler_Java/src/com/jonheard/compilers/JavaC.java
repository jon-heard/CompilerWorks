package com.jonheard.compilers;

import java.util.List;

import com.jonheard.compilers.JvmGenerator_Java.JvmGeneratorStringConverter;
import com.jonheard.compilers.JvmGenerator_Java.JvmGenerator_Java;
import com.jonheard.compilers.assembler_jvm.backEnd.ClassRep;
import com.jonheard.compilers.irProcessor_java.IrProcessor_Java;
import com.jonheard.compilers.javaClasspathDatabase.JavaClasspathDatabase;
import com.jonheard.compilers.parser_java.ParserStringConverter;
import com.jonheard.compilers.parser_java.Parser;
import com.jonheard.compilers.parser_java.ir.CompilationUnit;
import com.jonheard.compilers.tokenizer_java.Token;
import com.jonheard.compilers.tokenizer_java.Tokenizer;
import com.jonheard.util.SourceFile;
import com.jonheard.util.UtilMethods;

// JavaC - The front end class for this compiler application
public class JavaC {

  public static final String APP_INFO_TEXT =
        "------------------------------------------------\n"
      + " Java Compiler by Jonathan Heard v.0.04.000\n"
      + "------------------------------------------------\n";
  public static final String HELP_TEXT =
        "Usage: javac <source file> <options>\n" + "Options:\n"
      + "-help (-h)     Print this help panel and quit\n"
      + "-tokenize (-t) Compile up to the tokenizing step, printing results\n"
      + "-parse (-p)    Compile up to the parsing step, printing results\n"
      + "-process (-r)  Compile up to the processing step, printing results\n"
      + "-generate (-g) Compile up to the jvm generating step, printing results\n";

  public static void main(String[] args) {
    JavaC app = new JavaC();
    String compilerOutput = app.compile(args);
    System.out.println(compilerOutput);
  }

  public String compile(String[] args) {
    // Variables - User input
    boolean helpTextWasRequested = false;
    Stage stageToEndOn = Stage.WRITE_CLASS_FILE;
    String sourceFilename = "";

    // Variables - compiler output
    StringBuffer compilerOutputText = new StringBuffer();
    compilerOutputText.append(APP_INFO_TEXT);

    // Parse command-line parameters for user input
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "-help":
        case "-h":
          helpTextWasRequested = true;
          break;
        case "-tokenize":
        case "-t":
          stageToEndOn = Stage.TOKENIZE;
          break;
        case "-parse":
        case "-p":
          stageToEndOn = Stage.PARSE;
          break;
        case "-process":
        case "-r":
          stageToEndOn = Stage.PROCESS;
          break;
        case "-generate":
        case "-g":
          stageToEndOn = Stage.GENERATE;
          break;
        default:
          sourceFilename = args[i];
          break;
      }
    }

    // Show help message
    if (helpTextWasRequested || sourceFilename.equals("")) {
      compilerOutputText.append(HELP_TEXT);
      return compilerOutputText.toString();
    }

    // Load the file
    String sourceString = UtilMethods.fileToString(sourceFilename);
    if (sourceString == null) {
      compilerOutputText.append("ERROR: Invalid java source file name: " + sourceFilename);
      return compilerOutputText.toString();
    }
    SourceFile source = new SourceFile(sourceFilename, sourceString);

    // Tokenize
    Tokenizer tokenizer = new Tokenizer();
    List<Token> tokenizedSource = tokenizer.tokenize(source);
    if (stageToEndOn == Stage.TOKENIZE) {
      compilerOutputText.append(tokenizer.untokenize(tokenizedSource));
      return compilerOutputText.toString();
    }

    // Parse
    Parser parser = new Parser();
    CompilationUnit parsedSource = parser.parse(source, tokenizedSource);
    if (stageToEndOn == Stage.PARSE) {
      ParserStringConverter converter = new ParserStringConverter();
      compilerOutputText.append(converter.parsedToString(parsedSource));
      return compilerOutputText.toString();
    }

    // Setup java classpath database
    JavaClasspathDatabase libs = new JavaClasspathDatabase();
    libs.addSource_Jdk();

    // Process
    IrProcessor_Java processor = new IrProcessor_Java();
    processor.process(source, libs, parsedSource);
    if (stageToEndOn == Stage.PROCESS) {
      ParserStringConverter converter = new ParserStringConverter();
      compilerOutputText.append(converter.parsedToString(parsedSource));
      return compilerOutputText.toString();
    }

    // Generate
    JvmGenerator_Java generator = new JvmGenerator_Java();
    if (stageToEndOn == Stage.GENERATE) {
      generator.setLogToString(true);
      //ClassRep generated = generator.generate(source, parsedSource);
      JvmGeneratorStringConverter converter = new JvmGeneratorStringConverter();
      compilerOutputText.append(converter.generatedToString(generator));
      return compilerOutputText.toString();
    } else {
      ClassRep generated = generator.generate(source, parsedSource);
      UtilMethods.byteArrayToFile(generated.getJvmBytes(), sourceFilename.replace(".java", ".class"));
      return "";
    }
  }
  
  private enum Stage {
    TOKENIZE, PARSE, PROCESS, GENERATE, WRITE_CLASS_FILE
  }
}

package com.jonheard.compilers.assembler_jvm.frontEnd;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.junit.Test;

import com.jonheard.compilers.assembler_jvm.frontEnd.ClassParser;
import com.jonheard.compilers.assembler_jvm.frontEnd.JvmAssembler;

import static org.junit.Assert.*;

public class JvmAssemblerTest
{
	class IoCounter extends PrintStream
	{
		private int count = 0;
		public IoCounter(PrintStream p)
		{
			super(p);
		}
		public int getCount()
		{
			return count;
		}
		public void resetCount()
		{
			count = 0;
		}
		@Override
		public void print(String i)
		{
			count++;
			super.print(i);
		}
		@Override
		public void println(String i)
		{
			count++;			
			super.println(i);
		}
	}
	
	IoCounter std, err;
	
	public JvmAssemblerTest()
	{
		std = new IoCounter(System.out);
		err = new IoCounter(System.err);
		System.setOut(std);
		System.setErr(err);
	}

	public void resetCounters()
	{
		std.resetCount();
		err.resetCount();
	}
	
	@Test
	public void basicConsoleOutput()
	{
		/// No args
		resetCounters();
		JvmAssembler.main(new String[0]);
		int basicLineCount = std.getCount();
		int basicErrCount = err.getCount();
		assert(0 < basicErrCount);
		System.out.println("\n\n");
		/// Help arg
		resetCounters();
		JvmAssembler.main(new String[] {"-h"});
		assert(basicLineCount+3 < std.getCount());
		assertEquals(0, err.getCount());
	}
	
	private static final String TEST_SRC_FILENAME = "test1.txt";
	private static final String TEST_DST_FILENAME = "n1.class";
	private static final String TEST_CODE =
			"n1 s1 public abstract\n" +
			"{\n" +
			"	f1 I public\n" +
			"	m1 ()V public static\n" +
			"	{\n" +
			"	}\n" +
			"}\n";
	@Test
	public void basicFileIO() throws IOException
	{
		/// Setup test files
		File f1 = new File(TEST_SRC_FILENAME);
		File f2 = new File(TEST_DST_FILENAME);
		f1.delete();
		f2.delete();
		PrintWriter writer = new PrintWriter(f1);
		writer.print(TEST_CODE);
		writer.close();

		JvmAssembler.main(new String[] { TEST_SRC_FILENAME });
		
		assertTrue(f2.exists());
		
		ClassParser parser = new ClassParser();
		byte[] expected = parser.parseSource(TEST_SRC_FILENAME, TEST_CODE)
				.getJvmBytes();
		byte[] actual = Files.readAllBytes(Paths.get(TEST_DST_FILENAME));
		assertArrayEquals(expected, actual);
		
		/// Cleanup
		f1.delete();
		f2.delete();
	}

	@Test
	public void badFileIO() throws IOException
	{
		/// Setup test files
		File f1 = new File(TEST_SRC_FILENAME);
		File f2 = new File(TEST_DST_FILENAME);
		f1.delete();
		f2.delete();

		resetCounters();
		JvmAssembler.main(new String[] { TEST_SRC_FILENAME });
		assert(0 < err.getCount());
		assertFalse(f2.exists());
		
		/// Cleanup
		f1.delete();
		f2.delete();
	}
	@Test
	public void badSyntax() throws IOException
	{
		/// Setup test files
		File f1 = new File(TEST_SRC_FILENAME);
		File f2 = new File(TEST_DST_FILENAME);
		f1.delete();
		f2.delete();
		PrintWriter writer = new PrintWriter(f1);
		writer.print("bad code\n");
		writer.print(TEST_CODE);
		writer.close();

		resetCounters();
		JvmAssembler.main(new String[] { TEST_SRC_FILENAME });
		assert(0 < err.getCount());
		assertFalse(f2.exists());
		
		/// Cleanup
		f1.delete();
		f2.delete();
	}
}

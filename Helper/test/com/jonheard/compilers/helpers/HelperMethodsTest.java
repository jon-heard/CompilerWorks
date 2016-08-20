package com.jonheard.compilers.helpers;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

public class HelperMethodsTest
{
	final boolean INCLUDE_FILEIO_TESTS = false;

	private static final String TEST_FILENAME_1 = "test1.txt";
	private static final String TEST_FILENAME_2 = "test2.txt";

	@Test
	public void fileToString()
	{
		assumeTrue(INCLUDE_FILEIO_TESTS);

		String expected = "Hello world\nHow are you?";

		/// Setup test files
		try
		{
			File f1 = new File(TEST_FILENAME_1);
			File f2 = new File(TEST_FILENAME_2);
			f1.delete();
			f2.delete();
			PrintWriter p = new PrintWriter(f1);
			p.print(expected);
			p.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		/// Run tests
		String actual1 = HelperMethods.fileToString(TEST_FILENAME_1);
		String actual2 = HelperMethods.fileToString(TEST_FILENAME_2);

		/// Check results
		assertEquals(expected, actual1);
		assertEquals(null, actual2);

		/// Cleanup
		File f1 = new File(TEST_FILENAME_1);
		File f2 = new File(TEST_FILENAME_2);
		f1.delete();
		f2.delete();
	}

	@Test
	public void stringToFile()
	{
		assumeTrue(INCLUDE_FILEIO_TESTS);

		String expected = "Hello world\nHow are you?";

		/// Setup test files
		File f1 = new File(TEST_FILENAME_1);
		f1.delete();

		/// Run tests
		boolean success1 = HelperMethods.stringToFile(null, TEST_FILENAME_1);
		boolean success2 = HelperMethods.stringToFile("", TEST_FILENAME_1);

		/// Check success results
		assertFalse(success1);
		assertTrue(success2);

		/// Check file output
		try
		{
			Scanner s = new Scanner(f1);
			s.useDelimiter("\\z");
			assertFalse(s.hasNext());
			s.close();
		}
		catch (FileNotFoundException e)
		{
			assertTrue(false);
		}

		/// Run tests
		boolean success3 = HelperMethods.stringToFile(expected,
				TEST_FILENAME_1);
		boolean success4 = HelperMethods.stringToFile(expected,
				"c:/windows/system32/tmp.txt");

		/// Check success results
		assertTrue(success3);
		assertFalse(success4);

		/// Check file output
		try
		{
			Scanner s = new Scanner(f1);
			s.useDelimiter("\\z");
			String actual = s.next();
			assertEquals(expected, actual);
			assertFalse(s.hasNext());
			s.close();
		}
		catch (FileNotFoundException e)
		{
			assertTrue(false);
		}

		/// Cleanup
		f1.delete();
	}

	@Test
	public void byteArrayToFile()
	{
		assumeTrue(INCLUDE_FILEIO_TESTS);

		String expected = "Hello world\nHow are you?";

		/// Setup test files
		File f1 = new File(TEST_FILENAME_1);
		f1.delete();

		/// Run tests
		boolean success1 = HelperMethods.byteArrayToFile(null, TEST_FILENAME_1);
		boolean success2 = HelperMethods.byteArrayToFile(new byte[0],
				TEST_FILENAME_1);

		/// Check success results
		assertFalse(success1);
		assertTrue(success2);

		/// Check file output
		try
		{
			Scanner s = new Scanner(f1);
			s.useDelimiter("\\z");
			assertFalse(s.hasNext());
			s.close();
		}
		catch (FileNotFoundException e)
		{
			assertTrue(false);
		}

		/// Run tests
		boolean success3 = HelperMethods.byteArrayToFile(expected.getBytes(),
				TEST_FILENAME_1);
		boolean success4 = HelperMethods.byteArrayToFile(expected.getBytes(),
				"c:/windows/system32/tmp.txt");

		/// Check success results
		assertTrue(success3);
		assertFalse(success4);

		/// Check file output
		try
		{
			Scanner s = new Scanner(f1);
			s.useDelimiter("\\z");
			String actual = s.next();

			assertEquals(expected, actual);
			assertFalse(s.hasNext());
			s.close();
		}
		catch (FileNotFoundException e)
		{
			assertTrue(false);
		}

		/// Cleanup
		f1.delete();
	}

	@Test
	public void tokenizeStringTest()
	{
		List<String> expected1 = Arrays.asList();
		List<String> expected2 = Arrays.asList("first", "second", "third");
		String in1 = "first second third";
		String in2 = "  first   second    third     ";
		String in3 = "\tfirst\t\tsecond\t\t\tthird\t\t\t";
		String in4 = "\nfirst\n\nsecond\n\n\nthird\n\n\n\n";
		String in5 = " \tfirst \nsecond\t\nthird \t\n";
		String in6 = " \t \t first \n \nsecond\t\n\t\nthird \t\n \t\n";
		List<String> expected3 = Arrays.asList("first", "second third",
				"fourth");
		String in7 = "first \"second third\" fourth";
		List<String> expected4 = Arrays.asList("first", "second \t\n \t\nthird",
				"fourth");
		String in8 = "first \"second \t\n \t\nthird\" fourth";

		assertEquals(expected1, HelperMethods.tokenizeString(null));
		assertEquals(expected1, HelperMethods.tokenizeString(""));
		assertEquals(expected2, HelperMethods.tokenizeString(in1));
		assertEquals(expected2, HelperMethods.tokenizeString(in2));
		assertEquals(expected2, HelperMethods.tokenizeString(in3));
		assertEquals(expected2, HelperMethods.tokenizeString(in4));
		assertEquals(expected2, HelperMethods.tokenizeString(in5));
		assertEquals(expected2, HelperMethods.tokenizeString(in6));
		assertEquals(expected3, HelperMethods.tokenizeString(in7));
		assertEquals(expected4, HelperMethods.tokenizeString(in8));
	}

	enum EnumTest1
	{
		first, second, third
	};

	@Test
	public void enumContainsStringTest()
	{
		assertTrue(HelperMethods.enumContainsString(EnumTest1.class, "first"));
		assertTrue(HelperMethods.enumContainsString(EnumTest1.class, "second"));
		assertTrue(HelperMethods.enumContainsString(EnumTest1.class, "third"));
		assertFalse(HelperMethods.enumContainsString(EnumTest1.class, ""));
		assertFalse(
				HelperMethods.enumContainsString(EnumTest1.class, "fourth"));
		assertFalse(HelperMethods.enumContainsString(EnumTest1.class, "FIRST"));
	}

	@Test
	public void buildMethodDescriptorTest()
	{
		String expected = "()V";
		String actual = HelperMethods.buildMethodDescriptor(null, null);
		assertEquals(expected, actual);

		expected = "()V";
		actual = HelperMethods.buildMethodDescriptor("V", null);
		assertEquals(expected, actual);

		expected = "()V";
		actual = HelperMethods.buildMethodDescriptor("V", Arrays.asList());
		assertEquals(expected, actual);

		expected = "()Z";
		actual = HelperMethods.buildMethodDescriptor("Z", Arrays.asList());
		assertEquals(expected, actual);

		expected = "(BI)Z";
		actual = HelperMethods.buildMethodDescriptor("Z",
				Arrays.asList("B", "I"));
		assertEquals(expected, actual);

		expected = "(BLjava/lang/String;I)Z";
		actual = HelperMethods.buildMethodDescriptor("Z",
				Arrays.asList("B", "Ljava/lang/String;", "I"));
		assertEquals(expected, actual);

		expected = "([B[Ljava/lang/String;I)Z";
		actual = HelperMethods.buildMethodDescriptor("Z",
				Arrays.asList("[B", "[Ljava/lang/String;", "I"));
		assertEquals(expected, actual);
	}

	@Test
	public void getStackSizefMethodDescriptorTest()
	{
		int expected = HelperMethods.INVALID_STACK_SIZE;
		int actual = HelperMethods.getStackSizeOfMethodDescriptor(null);
		assertEquals(expected, actual);

		expected = HelperMethods.INVALID_STACK_SIZE;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("");
		assertEquals(expected, actual);

		expected = HelperMethods.INVALID_STACK_SIZE;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("(V");
		assertEquals(expected, actual);

		expected = HelperMethods.INVALID_STACK_SIZE;
		actual = HelperMethods.getStackSizeOfMethodDescriptor(")V");
		assertEquals(expected, actual);

		expected = 0;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("()V");
		assertEquals(expected, actual);

		expected = 1;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("(I)V");
		assertEquals(expected, actual);

		expected = 2;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("(IF)V");
		assertEquals(expected, actual);

		expected = 2;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("(IF)B");
		assertEquals(expected, actual);

		expected = 3;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("(ID)B");
		assertEquals(expected, actual);

		expected = 4;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("(JD)B");
		assertEquals(expected, actual);

		expected = 2;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("(I[D)B");
		assertEquals(expected, actual);

		expected = 2;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("([I[D)B");
		assertEquals(expected, actual);

		expected = 2;
		actual = HelperMethods.getStackSizeOfMethodDescriptor("([J[D)B");
		assertEquals(expected, actual);

		expected = 3;
		actual = HelperMethods
				.getStackSizeOfMethodDescriptor("([JLjava/lang/String;[D)B");
		assertEquals(expected, actual);

		expected = 3;
		actual = HelperMethods
				.getStackSizeOfMethodDescriptor("([J[Ljava/lang/String;[D)B");
		assertEquals(expected, actual);
	}

	@Test
	public void getStackSizefFieldDescriptorTest()
	{
		int expected = HelperMethods.INVALID_STACK_SIZE;
		int actual = HelperMethods.getStackSizeOfFieldDescriptor(null);
		assertEquals(expected, actual);

		expected = HelperMethods.INVALID_STACK_SIZE;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("");
		assertEquals(expected, actual);

		expected = 0;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("V");
		assertEquals(expected, actual);

		expected = 2;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("J");
		assertEquals(expected, actual);

		expected = 2;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("D");
		assertEquals(expected, actual);

		expected = 1;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("I");
		assertEquals(expected, actual);

		expected = 1;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("F");
		assertEquals(expected, actual);

		expected = 1;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("Z");
		assertEquals(expected, actual);

		expected = 1;
		actual = HelperMethods
				.getStackSizeOfFieldDescriptor("Ljava/lang/String");
		assertEquals(expected, actual);

		expected = 1;
		actual = HelperMethods.getStackSizeOfFieldDescriptor("[Z");
		assertEquals(expected, actual);

		expected = 1;
		actual = HelperMethods
				.getStackSizeOfFieldDescriptor("[Ljava/lang/String");
		assertEquals(expected, actual);
	}

	@Test
	public void generateFlagsFromModifierListTest()
	{
		short expected = HelperMethods.INVALID_MODIFIER_LIST;
		short actual = HelperMethods.generateFlagsFromModifierList(null);
		assertEquals(expected, actual);

		expected = 0;
		actual = HelperMethods.generateFlagsFromModifierList(Arrays.asList());
		assertEquals(expected, actual);

		expected = 0x0001;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("public"));
		assertEquals(expected, actual);

		expected = 0x0002;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("private"));
		assertEquals(expected, actual);

		expected = 0x0004;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("protected"));
		assertEquals(expected, actual);

		expected = 0x0008;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("static"));
		assertEquals(expected, actual);

		expected = 0x0010;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("final"));
		assertEquals(expected, actual);

		expected = 0x0020;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("synchronized"));
		assertEquals(expected, actual);

		expected = 0x0020;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("super"));
		assertEquals(expected, actual);

		expected = 0x0040;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("bridge"));
		assertEquals(expected, actual);

		expected = 0x0040;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("volatile"));
		assertEquals(expected, actual);

		expected = 0x0080;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("varargs"));
		assertEquals(expected, actual);

		expected = 0x0080;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("transient"));
		assertEquals(expected, actual);

		expected = 0x0100;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("native"));
		assertEquals(expected, actual);

		expected = 0x0200;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("interface"));
		assertEquals(expected, actual);

		expected = 0x00400;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("abstract"));
		assertEquals(expected, actual);

		expected = 0x0800;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("strict"));
		assertEquals(expected, actual);

		expected = 0x1000;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("synthetic"));
		assertEquals(expected, actual);

		expected = 0x2000;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("annotation"));
		assertEquals(expected, actual);

		expected = 0x4000;
		actual = HelperMethods
				.generateFlagsFromModifierList(Arrays.asList("enum"));
		assertEquals(expected, actual);

		expected = 0x0001 | 0x0002;
		actual = HelperMethods.generateFlagsFromModifierList(
				Arrays.asList("public", "private"));
		assertEquals(expected, actual);
	}
}

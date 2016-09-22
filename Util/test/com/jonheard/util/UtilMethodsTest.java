package com.jonheard.util;

import static org.junit.Assert.*;
import static org.junit.Assume.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.junit.Test;

import com.jonheard.util.UtilMethods;

public class UtilMethodsTest
{
	final boolean INCLUDE_FILEIO_TESTS = true;

	private static final String TEST_DIR = "testData";
	private static final String TEST_FILENAME_1 = "test1.txt";
	private static final String TEST_FILENAME_2 = "test2.txt";

	private void setupTestData()
	{
		File testDir = new File(TEST_DIR);
		if(testDir.exists())
		{
			String[]entries = testDir.list();
			for(String s: entries){
			    File currentFile = new File(testDir.getPath(), s);
			    currentFile.delete();
			}
		}
		else
		{
			testDir.mkdir();			
		}
	}
	
	@Test
	public void fileToString()
	{
		assumeTrue(INCLUDE_FILEIO_TESTS);

		String expected = "Hello world\nHow are you?";
		
		setupTestData();

		/// Setup test files
		try
		{
			File f1 = new File(TEST_DIR+"/"+TEST_FILENAME_1);
			PrintWriter p = new PrintWriter(f1);
			p.print(expected);
			p.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		/// Run tests
		String actual1 = UtilMethods.fileToString(TEST_DIR+"/"+TEST_FILENAME_1);
		String actual2 = UtilMethods.fileToString(TEST_DIR+"/"+TEST_FILENAME_2);

		/// Check results
		assertEquals(expected, actual1);
		assertEquals(null, actual2);
	}

	@Test
	public void stringToFile()
	{
		assumeTrue(INCLUDE_FILEIO_TESTS);

		String expected = "Hello world\nHow are you?";

		setupTestData();

		File f1 = new File(TEST_DIR+"/"+TEST_FILENAME_1);
		File f2 = new File(TEST_DIR+"/"+TEST_FILENAME_2);

		/// Run tests
		boolean success1 = UtilMethods.stringToFile(null, f1.getPath());
		boolean success2 = UtilMethods.stringToFile(
				expected, "c:/windows/system32/tmp.txt");
		boolean success3 = UtilMethods.stringToFile("", f1.getPath());
		boolean success4 = UtilMethods.stringToFile(expected, f2.getPath());


		/// Check success results
		assertFalse(success1);
		assertFalse(success2);
		assertTrue(success3);
		assertTrue(success4);

		/// Check file results
		try
		{
			// f1
			Scanner s = new Scanner(f1);
			s.useDelimiter("\\z");
			assertFalse(s.hasNext());
			s.close();

			// f2
			s = new Scanner(f2);
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
	}

	@Test
	public void fileToByteArray()
	{
		assumeTrue(INCLUDE_FILEIO_TESTS);

		String expected = "Hello world\nHow are you?";
		byte[] expectedBytes = expected.getBytes();
		
		setupTestData();

		/// Setup test files
		try
		{
			File f1 = new File(TEST_DIR+"/"+TEST_FILENAME_1);
			PrintWriter p = new PrintWriter(f1);
			p.print(expected);
			p.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		/// Run tests
		byte[] actual1 =
				UtilMethods.fileToByteArray(TEST_DIR+"/"+TEST_FILENAME_1);
		byte[] actual2 =
				UtilMethods.fileToByteArray(TEST_DIR+"/"+TEST_FILENAME_2);

		/// Check results
		assertArrayEquals(expectedBytes, actual1);
		assertEquals(null, actual2);
	}

	@Test
	public void byteArrayToFile()
	{
		assumeTrue(INCLUDE_FILEIO_TESTS);

		String expected = "Hello world\nHow are you?";
		byte[] expectedBytes = expected.getBytes();

		setupTestData();

		File f1 = new File(TEST_DIR+"/"+TEST_FILENAME_1);
		File f2 = new File(TEST_DIR+"/"+TEST_FILENAME_2);

		/// Run tests
		boolean success1 = UtilMethods.byteArrayToFile(null, f1.getPath());
		boolean success2 = UtilMethods.byteArrayToFile(
				expected.getBytes(), "c:/windows/system32/tmp.txt");
		boolean success3 =
				UtilMethods.byteArrayToFile(new byte[0], f1.getPath());
		boolean success4 =
				UtilMethods.byteArrayToFile(expectedBytes, f2.getPath());

		/// Check success results
		assertFalse(success1);
		assertFalse(success2);
		assertTrue(success3);
		assertTrue(success4);

		/// Check file output
		try
		{
			// f1
			Scanner s = new Scanner(f1);
			s.useDelimiter("\\z");
			assertFalse(s.hasNext());
			s.close();

			// f2
			s = new Scanner(f2);
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

		assertEquals(expected1, UtilMethods.tokenizeString(null));
		assertEquals(expected1, UtilMethods.tokenizeString(""));
		assertEquals(expected2, UtilMethods.tokenizeString(in1));
		assertEquals(expected2, UtilMethods.tokenizeString(in2));
		assertEquals(expected2, UtilMethods.tokenizeString(in3));
		assertEquals(expected2, UtilMethods.tokenizeString(in4));
		assertEquals(expected2, UtilMethods.tokenizeString(in5));
		assertEquals(expected2, UtilMethods.tokenizeString(in6));
		assertEquals(expected3, UtilMethods.tokenizeString(in7));
		assertEquals(expected4, UtilMethods.tokenizeString(in8));
	}

	enum EnumTest1
	{
		first, second, third
	};

	@Test
	public void enumContainsStringTest()
	{
		assertTrue(UtilMethods.enumContainsString(EnumTest1.class, "first"));
		assertTrue(UtilMethods.enumContainsString(EnumTest1.class, "second"));
		assertTrue(UtilMethods.enumContainsString(EnumTest1.class, "third"));
		assertFalse(UtilMethods.enumContainsString(EnumTest1.class, ""));
		assertFalse(
				UtilMethods.enumContainsString(EnumTest1.class, "fourth"));
		assertFalse(UtilMethods.enumContainsString(EnumTest1.class, "FIRST"));
	}
	
	@Test
	public void constNameToCamelNameTest()
	{
		assertEquals(
				"oneTwoThree",
				UtilMethods.constNameToCamelName("ONE_TWO_THREE", false));
		assertEquals(
				"OneTwoThree",
				UtilMethods.constNameToCamelName("ONE_TWO_THREE", true));
		assertEquals(
				"oneTwo_Three",
				UtilMethods.constNameToCamelName("ONE_TWO__THREE", false));
		assertEquals(
				"OneTwoThree",
				UtilMethods.constNameToCamelName("_ONE_TWO_THREE", false));
		assertEquals(
				"OneTwoThree",
				UtilMethods.constNameToCamelName("_ONE_TWO_THREE", true));
		assertEquals(
				"1neTwoThree",
				UtilMethods.constNameToCamelName("1ne_TWO_THREE", false));
		assertEquals(
				"1neTwoThree",
				UtilMethods.constNameToCamelName("1ne_TWO_THREE", true));
		assertEquals(
				"1neTwoThree",
				UtilMethods.constNameToCamelName("_1ne_TWO_THREE", false));
		assertEquals(
				"1neTwoThree",
				UtilMethods.constNameToCamelName("_1ne_TWO_THREE", true));
		assertEquals(
				"oneTwo3hree",
				UtilMethods.constNameToCamelName("ONE_TWO_3HREE", false));
		assertEquals(
				"OneTwo3hree",
				UtilMethods.constNameToCamelName("ONE_TWO_3HREE", true));
		assertEquals(
				"oneTwo_3hree",
				UtilMethods.constNameToCamelName("ONE_TWO__3HREE", false));
		assertEquals(
				"OneTwo_3hree",
				UtilMethods.constNameToCamelName("ONE_TWO__3HREE", true));
	}

	@Test
	public void buildMethodDescriptorTest()
	{
		String expected = "()V";
		String actual = UtilMethods.buildMethodDescriptor(null, null);
		assertEquals(expected, actual);

		expected = "()V";
		actual = UtilMethods.buildMethodDescriptor("V", null);
		assertEquals(expected, actual);

		expected = "()V";
		actual = UtilMethods.buildMethodDescriptor("V", Arrays.asList());
		assertEquals(expected, actual);

		expected = "()Z";
		actual = UtilMethods.buildMethodDescriptor("Z", Arrays.asList());
		assertEquals(expected, actual);

		expected = "(BI)Z";
		actual = UtilMethods.buildMethodDescriptor("Z",
				Arrays.asList("B", "I"));
		assertEquals(expected, actual);

		expected = "(BLjava/lang/String;I)Z";
		actual = UtilMethods.buildMethodDescriptor("Z",
				Arrays.asList("B", "Ljava/lang/String;", "I"));
		assertEquals(expected, actual);

		expected = "([B[Ljava/lang/String;I)Z";
		actual = UtilMethods.buildMethodDescriptor("Z",
				Arrays.asList("[B", "[Ljava/lang/String;", "I"));
		assertEquals(expected, actual);
	}

	@Test
	public void getStackSizefMethodDescriptorTest()
	{
		int expected = UtilMethods.INVALID_STACK_SIZE;
		int actual = UtilMethods.getStackSizeOfMethodDescriptor(null);
		assertEquals(expected, actual);

		expected = UtilMethods.INVALID_STACK_SIZE;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("");
		assertEquals(expected, actual);

		expected = UtilMethods.INVALID_STACK_SIZE;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("(V");
		assertEquals(expected, actual);

		expected = UtilMethods.INVALID_STACK_SIZE;
		actual = UtilMethods.getStackSizeOfMethodDescriptor(")V");
		assertEquals(expected, actual);

		expected = 0;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("()V");
		assertEquals(expected, actual);

		expected = 1;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("(I)V");
		assertEquals(expected, actual);

		expected = 2;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("(IF)V");
		assertEquals(expected, actual);

		expected = 2;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("(IF)B");
		assertEquals(expected, actual);

		expected = 3;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("(ID)B");
		assertEquals(expected, actual);

		expected = 4;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("(JD)B");
		assertEquals(expected, actual);

		expected = 2;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("(I[D)B");
		assertEquals(expected, actual);

		expected = 2;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("([I[D)B");
		assertEquals(expected, actual);

		expected = 2;
		actual = UtilMethods.getStackSizeOfMethodDescriptor("([J[D)B");
		assertEquals(expected, actual);

		expected = 3;
		actual = UtilMethods
				.getStackSizeOfMethodDescriptor("([JLjava/lang/String;[D)B");
		assertEquals(expected, actual);

		expected = 3;
		actual = UtilMethods
				.getStackSizeOfMethodDescriptor("([J[Ljava/lang/String;[D)B");
		assertEquals(expected, actual);
	}

	@Test
	public void getStackSizefFieldDescriptorTest()
	{
		int expected = UtilMethods.INVALID_STACK_SIZE;
		int actual = UtilMethods.getStackSizeOfFieldDescriptor(null);
		assertEquals(expected, actual);

		expected = UtilMethods.INVALID_STACK_SIZE;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("");
		assertEquals(expected, actual);

		expected = 0;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("V");
		assertEquals(expected, actual);

		expected = 2;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("J");
		assertEquals(expected, actual);

		expected = 2;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("D");
		assertEquals(expected, actual);

		expected = 1;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("I");
		assertEquals(expected, actual);

		expected = 1;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("F");
		assertEquals(expected, actual);

		expected = 1;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("Z");
		assertEquals(expected, actual);

		expected = 1;
		actual = UtilMethods
				.getStackSizeOfFieldDescriptor("Ljava/lang/String");
		assertEquals(expected, actual);

		expected = 1;
		actual = UtilMethods.getStackSizeOfFieldDescriptor("[Z");
		assertEquals(expected, actual);

		expected = 1;
		actual = UtilMethods
				.getStackSizeOfFieldDescriptor("[Ljava/lang/String");
		assertEquals(expected, actual);
	}

	@Test
	public void generateFlagsFromModifierListTest()
	{
		short expected = UtilMethods.INVALID_MODIFIER_LIST;
		short actual = UtilMethods.generateFlagsFromModifierList(null);
		assertEquals(expected, actual);

		expected = 0;
		actual = UtilMethods.generateFlagsFromModifierList(Arrays.asList());
		assertEquals(expected, actual);

		expected = 0x0001;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("public"));
		assertEquals(expected, actual);

		expected = 0x0002;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("private"));
		assertEquals(expected, actual);

		expected = 0x0004;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("protected"));
		assertEquals(expected, actual);

		expected = 0x0008;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("static"));
		assertEquals(expected, actual);

		expected = 0x0010;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("final"));
		assertEquals(expected, actual);

		expected = 0x0020;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("synchronized"));
		assertEquals(expected, actual);

		expected = 0x0020;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("super"));
		assertEquals(expected, actual);

		expected = 0x0040;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("bridge"));
		assertEquals(expected, actual);

		expected = 0x0040;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("volatile"));
		assertEquals(expected, actual);

		expected = 0x0080;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("varargs"));
		assertEquals(expected, actual);

		expected = 0x0080;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("transient"));
		assertEquals(expected, actual);

		expected = 0x0100;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("native"));
		assertEquals(expected, actual);

		expected = 0x0200;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("interface"));
		assertEquals(expected, actual);

		expected = 0x00400;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("abstract"));
		assertEquals(expected, actual);

		expected = 0x0800;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("strict"));
		assertEquals(expected, actual);

		expected = 0x1000;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("synthetic"));
		assertEquals(expected, actual);

		expected = 0x2000;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("annotation"));
		assertEquals(expected, actual);

		expected = 0x4000;
		actual = UtilMethods
				.generateFlagsFromModifierList(Arrays.asList("enum"));
		assertEquals(expected, actual);

		expected = 0x0001 | 0x0002;
		actual = UtilMethods.generateFlagsFromModifierList(
				Arrays.asList("public", "private"));
		assertEquals(expected, actual);
	}
}

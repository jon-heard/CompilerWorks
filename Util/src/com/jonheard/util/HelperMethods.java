package com.jonheard.util;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Helper_Methods - Here is functionality that doesn't properly belong to any
 * specific class.
 */
public class HelperMethods
{
	/// Takes a filename (a String), loads the data from the file into a String
	/// and returns the String
	public static String fileToString(String filename)
	{
		String result = "";
		try
		{
			Scanner s = new Scanner(new File(filename));
			s.useDelimiter("\\z");
			result = s.next();
			s.close();
		}
		catch (Exception e)
		{
			return null;
		}
		return result;
	}

	
	/// Takes a String of data and a filename (a String) and writes the data
	/// to file using the given filename.
	public static boolean stringToFile(String data, String filename)
	{
		if(data == null) return false;
		return byteArrayToFile(data.getBytes(), filename);
	}
	
	/// Takes a byte array of data and a filename (a String) and writes the data
	/// to file using the given filename.
	public static boolean byteArrayToFile(byte[] data, String filename)
	{
		FileOutputStream out;
		try
		{
			File file = new File(filename);
			if(file.exists()) file.delete();
			out = new FileOutputStream(file);
			out.write(data);
			out.close();
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}

	/// Convert a string to tokens: split by whitespace, except when in quotes
	public static List<String> tokenizeString(String toTokenize)
	{
		if(toTokenize == null) return Arrays.asList();
		toTokenize = toTokenize.trim();
		// http://stackoverflow.com/questions/7804335/split-string-on-spaces-in-java-except-if-between-quotes-i-e-treat-hello-wor
		ArrayList<String> result = new ArrayList<String>();
		Matcher m =
				Pattern.compile("([^\"]\\S*|\"[\\s\\S]+?\")\\s*").matcher(toTokenize);
		while (m.find())
		{
		    result.add(m.group(1).replace("\"", ""));
		}
		return result;
	}

	/// Determine if the given enum contains a value matching the given string 
	public static <T extends Enum<T>> boolean enumContainsString(
			Class<T> haystack, String needle)
	{
		boolean result = true;
		try
		{
			Enum.valueOf(haystack, needle);
		}
		catch(IllegalArgumentException e)
		{
			result = false;
		}
		return result;
	}
	
	/// Create a method descriptor from a return type & collection of arg types
	public static String buildMethodDescriptor(
			String type, Collection<String> args)
	{
		StringBuilder result = new StringBuilder("(");
		if(args != null)
		{
			for(String i : args)
			{
				result.append(i);
			}
		}
		result.append(")");
		result.append(type == null ? "V" : type);
		return result.toString();
	}
	
	public static final int INVALID_STACK_SIZE =  Integer.MAX_VALUE;

	/// Gets the number of arguments defined in a method descriptor
	public static int getStackSizeOfMethodDescriptor(String descriptor)
	{		
		int result = 0;

		/// Check formatting
		if(descriptor == null) return INVALID_STACK_SIZE;
		if(descriptor.length() == 0) return INVALID_STACK_SIZE;
		if(descriptor.charAt(0) != '(') return INVALID_STACK_SIZE;
		if(!descriptor.contains(")")) return INVALID_STACK_SIZE;
		
		int charIndex = 1;
		boolean isArray = false;
		while(descriptor.charAt(charIndex) != ')')
		{
			char cur = descriptor.charAt(charIndex);
			switch(cur)
			{
				case '[':
					isArray = true;
					break;
				case 'L':
					charIndex = descriptor.indexOf(';', charIndex);
					result++;
					isArray = false;
					break;
				case 'J':
				case 'D':
					result += isArray ? 1 : 2;
					isArray = false;
					break;
				default:
					result++;
					isArray = false;
					break;
			}
			charIndex++;
		}
		return result;
	}
	
	public static int getStackSizeOfFieldDescriptor(String descriptor)
	{
		if(descriptor == null) return INVALID_STACK_SIZE;
		switch(descriptor)
		{
			case "": return INVALID_STACK_SIZE;
			case "V": return 0;
			case "J": return 2;
			case "D": return 2;
			default: return 1;
		}
	}

	public static final short INVALID_MODIFIER_LIST =  Short.MAX_VALUE;

	/// Turn a list of modifier strings into bitflags stored as a short (as
	/// defined in the jvm spec)
	public static short generateFlagsFromModifierList(
			Collection<String> modifierList)
	{
		if(modifierList == null) return INVALID_MODIFIER_LIST;
		short result = 0;
		if(modifierList.contains("public")) result |= 0x0001;
		if(modifierList.contains("private")) result |= 0x0002;
		if(modifierList.contains("protected")) result |= 0x0004;
		if(modifierList.contains("static")) result |= 0x0008;
		if(modifierList.contains("final")) result |= 0x0010;
		if(modifierList.contains("synchronized")) result |= 0x0020;
		if(modifierList.contains("super")) result |= 0x0020;
		if(modifierList.contains("bridge")) result |= 0x0040;
		if(modifierList.contains("volatile")) result |= 0x0040;
		if(modifierList.contains("varargs")) result |= 0x0080;
		if(modifierList.contains("transient")) result |= 0x0080;
		if(modifierList.contains("native")) result |= 0x0100;
		if(modifierList.contains("interface")) result |= 0x0200;
		if(modifierList.contains("abstract")) result |= 0x0400;
		if(modifierList.contains("strict")) result |= 0x0800;
		if(modifierList.contains("synthetic")) result |= 0x1000;
		if(modifierList.contains("annotation")) result |= 0x2000;
		if(modifierList.contains("enum")) result |= 0x4000;
		return result;
	}
}

package com.jonheard.compilers.javaClassHeirarchy;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.compilers.javaClassHeirarchy.ClassFileMemberReader;
import com.jonheard.util.UtilMethods;

public class ClassFileMemberReaderTest
{

	@Test
	public void main()
	{
		byte[] data = UtilMethods.fileToByteArray(
				"bin/com/jonheard/compilers/javaClassHeirarchy/" +
				"Source.class");

		ClassFileMemberReader reader = new ClassFileMemberReader(new byte[3]);
		assertFalse(reader.isValid());

		reader = new ClassFileMemberReader(data);
		assertTrue(reader.isValid());

		assertEquals(10, reader.getMemberCount());

		assertFalse(reader.hasMember("badData"));

		assertTrue(reader.hasMember("<init>"));
		assertTrue(reader.hasMember("valid"));

		assertFalse(reader.isMemberAMethod("badData"));
		assertFalse(reader.isMemberAField("badData"));

		assertTrue(reader.isMemberAMethod("<init>"));
		assertFalse(reader.isMemberAField("<init>"));
		assertFalse(reader.isMemberAMethod("valid"));
		assertTrue(reader.isMemberAField("valid"));
		
		assertEquals(
				"(Ljava/lang/String;)V",
				reader.getMemberDescriptor("<init>"));
		assertEquals("Z", reader.getMemberDescriptor("valid"));
	}
}

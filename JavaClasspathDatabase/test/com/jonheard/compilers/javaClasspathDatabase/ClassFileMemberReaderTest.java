package com.jonheard.compilers.javaClasspathDatabase;

import static org.junit.Assert.*;

import org.junit.Test;

import com.jonheard.compilers.javaClasspathDatabase.ClassFileMemberReader;
import com.jonheard.util.UtilMethods;

public class ClassFileMemberReaderTest
{

	@Test
	public void main()
	{
		byte[] data = UtilMethods.fileToByteArray(
				"bin/com/jonheard/compilers/javaClasspathDatabase/" +
				"Source.class");

		ClassFileMemberReader reader = new ClassFileMemberReader(new byte[3]);
		assertFalse(reader.isValid());

		reader = new ClassFileMemberReader(data);
		assertTrue(reader.isValid());

		assertEquals(11, reader.getMemberCount());

		assertFalse(reader.hasMember("badData"));

		assertTrue(reader.hasMember("isValid"));
		assertTrue(reader.hasMember("valid"));

		assertFalse(reader.isMemberAMethod("badData"));
		assertFalse(reader.isMemberAField("badData"));

		assertTrue(reader.isMemberAMethod("isValid"));
		assertFalse(reader.isMemberAField("isValid"));
		assertFalse(reader.isMemberAMethod("valid"));
		assertTrue(reader.isMemberAField("valid"));
		
		assertEquals(
				"()Z",
				reader.getMemberDescriptor("isValid"));
		assertEquals("Z", reader.getMemberDescriptor("valid"));
	}
}

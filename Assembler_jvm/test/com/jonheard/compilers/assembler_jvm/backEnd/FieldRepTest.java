package com.jonheard.compilers.assembler_jvm.backEnd;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.jonheard.compilers.assembler_jvm.backEnd.ConstantPool;
import com.jonheard.compilers.assembler_jvm.backEnd.DataBuffer;
import com.jonheard.compilers.assembler_jvm.backEnd.FieldRep;

public class FieldRepTest
{
	@Test
	public void membersTest()
	{
		String expectedName = "name1";
		String expectedDescriptor = "[Ljava/lang/String;";
		
		FieldRep rep = new FieldRep(
				expectedName, expectedDescriptor,
				Arrays.asList("public", "static"), new ConstantPool());

		assertEquals(expectedName, rep.getName());
		assertEquals(expectedDescriptor, rep.getDescriptor());
		assertEquals(0x0001 | 0x0008, rep.getModifiers());
	}
	
	@Test
	public void serializationTest()
	{
		String expectedName = "name1";
		String expectedDescriptor = "[Ljava/lang/String;";
		
		ConstantPool constantPool = new ConstantPool();
		FieldRep rep = new FieldRep(
				expectedName, expectedDescriptor,
				Arrays.asList("public", "static"), constantPool);

		DataBuffer buffer = rep.serialize();

		/// modifiers
		assertEquals(0x0001 | 0x0008, buffer.nextShort());

		/// name
		short nameIndex = rep.getNameIndex();
		assertEquals(nameIndex, buffer.nextShort());
		assertEquals(expectedName, constantPool.getUtf8(nameIndex));

		/// descriptor
		short descriptorIndex = rep.getDescriptorIndex();
		assertEquals(descriptorIndex, buffer.nextShort());
		assertEquals(expectedDescriptor, constantPool.getUtf8(descriptorIndex));

		/// attribute count
		assertEquals(0, buffer.nextShort());
	}
}

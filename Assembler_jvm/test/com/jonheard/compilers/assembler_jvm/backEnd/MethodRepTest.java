package com.jonheard.compilers.assembler_jvm.backEnd;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.jonheard.compilers.assembler_jvm.backEnd.ConstantPool;
import com.jonheard.compilers.assembler_jvm.backEnd.DataBuffer;
import com.jonheard.compilers.assembler_jvm.backEnd.MethodRep;

public class MethodRepTest
{
	@Test
	public void membersTest()
	{
		String expectedName = "name1";
		String expectedDescriptor = "([Ljava/lang/String;)V";
		
		MethodRep rep = new MethodRep(
				expectedName, expectedDescriptor,
				Arrays.asList("public", "static"), new ConstantPool());

		assertEquals(expectedName, rep.getName());
		assertEquals(expectedDescriptor, rep.getDescriptor());
		assertEquals(0x0001 | 0x0008, rep.getModifiers());
		assertEquals(0, rep.getLocalSize());
		assertEquals(0, rep.getStackSize());
		rep.setLocalSize(7);
		rep.setStackSize(9);
		assertEquals(7, rep.getLocalSize());
		assertEquals(9, rep.getStackSize());
	}
	
	@Test
	public void serializationTest()
	{
		String expectedName = "name1";
		String expectedDescriptor = "[Ljava/lang/String;";
		
		ConstantPool constantPool = new ConstantPool();
		MethodRep rep = new MethodRep(
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
		assertEquals(1, buffer.nextShort());

		/// code name attribute
		int codeIndex = constantPool.addString("Code");
		assertEquals(codeIndex-1, buffer.nextShort());
		
		/// code size
		assertEquals(12, buffer.nextInt());
		
		/// stack size
		assertEquals(0, buffer.nextShort());

		/// local size
		assertEquals(0, buffer.nextShort());

		/// code size
		assertEquals(0, buffer.nextShort());

		/// exception count
		assertEquals(0, buffer.nextShort());

		/// attribute count
		assertEquals(0, buffer.nextShort());
	}
}

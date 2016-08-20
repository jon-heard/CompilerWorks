package com.jonheard.compilers.jvmClassBuilder;

import static org.junit.Assert.*;
import java.util.Arrays;
import org.junit.Test;

public class ClassRepTest
{
	public static ConstantPool checkClassStart(
			DataBuffer data, String name, String superName)
	{
		assertEquals(0xCAFEBABE, data.nextInt());
		assertEquals(0x00000034, data.nextInt());
		ConstantPool pool = ConstantPool.nextConstantPool(data);
		assertEquals(0x0421, data.nextShort());
		assertEquals(pool.addClass(name), data.nextShort());
		assertEquals(pool.addClass(superName), data.nextShort());
		return pool;
	}
	
	public static void checkClassEnd(
			DataBuffer data, ConstantPool pool, String sourceFile)
	{
		assertEquals(1, data.nextShort());
		assertEquals(pool.addUtf8("SourceFile"), data.nextShort());
		assertEquals(2, data.nextInt());
		assertEquals(pool.addUtf8(sourceFile), data.nextShort());
		assertFalse(data.hasNext());
	}
	
	public static void checkField(
			DataBuffer data, ConstantPool pool,
			int mods, String name, String descriptor)
	{
		assertEquals(mods, data.nextShort());
		assertEquals(pool.addUtf8(name), data.nextShort());
		assertEquals(pool.addUtf8(descriptor), data.nextShort());
		assertEquals(0, data.nextShort());
	}
	
	public static void checkMethod(
			DataBuffer data, ConstantPool pool,
			int mods, String name, String descriptor, int localSize)
	{
		assertEquals(mods, data.nextShort());
		assertEquals(pool.addUtf8(name), data.nextShort());
		assertEquals(pool.addUtf8(descriptor), data.nextShort());
		assertEquals(1, data.nextShort());
		assertEquals(pool.addUtf8("Code"), data.nextShort());
		assertEquals(12, data.nextInt());
		assertEquals(0, data.nextShort());
		assertEquals(localSize, data.nextShort());
		assertEquals(0, data.nextInt());
		assertEquals(0, data.nextShort());
		assertEquals(0, data.nextShort());
	}

	@Test
	public void basic()
	{
		ClassRep rep = new ClassRep("n1", "s1",
				Arrays.asList("public", "abstract"), "f1.java");
		rep.addField("f1", "I", Arrays.asList("public", "static"));
		rep.addField("f2", "J", Arrays.asList("public"));
		rep.addMethod("m1", "()V", Arrays.asList("public", "static"));
		rep.addMethod("m2", "(I)D", Arrays.asList("public"));
		
		DataBuffer data = new DataBuffer();
		data.add(rep.getJvmBytes());
		ConstantPool pool = ClassRepTest.checkClassStart(data, "n1", "s1");

		/// Interfaces
		assertEquals(0, data.nextShort());

		/// Fields
		assertEquals(2, data.nextShort());
		ClassRepTest.checkField(data, pool, 0x0009, "f1", "I");
		ClassRepTest.checkField(data, pool, 0x0001, "f2", "J");

		/// Methods
		assertEquals(2, data.nextShort());
		ClassRepTest.checkMethod(data, pool, 0x0009, "m1", "()V", 0);
		ClassRepTest.checkMethod(data, pool, 0x0001, "m2", "(I)D", 2);
		
		ClassRepTest.checkClassEnd(data, pool, "f1.java");
	}
}

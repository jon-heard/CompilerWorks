package com.jonheard.compilers.assembler_jvm.frontEnd;

import org.junit.Test;

import com.jonheard.compilers.assembler_jvm.backEnd.ClassRep;
import com.jonheard.compilers.assembler_jvm.backEnd.ClassRepTest;
import com.jonheard.compilers.assembler_jvm.backEnd.ConstantPool;
import com.jonheard.compilers.assembler_jvm.backEnd.DataBuffer;
import com.jonheard.compilers.assembler_jvm.frontEnd.ClassParser;

import static org.junit.Assert.*;

public class ClassParserTest
{
	@Test
	public void basic()
	{
		ClassParser parser = new ClassParser();
		ClassRep rep = parser.parseSource("f1",
				"n1 s1 public abstract\n" +
				"{\n" +
				"	f1 I public\n" +
				"	m1 ()V public static\n" +
				"	{\n" +
				"	}\n" +
				"}\n"
		);
		
		DataBuffer data = new DataBuffer();
		data.add(rep.getJvmBytes());
		ConstantPool pool = ClassRepTest.checkClassStart(data, "n1", "s1");

		/// Interfaces
		assertEquals(0, data.nextShort());

		/// Fields
		assertEquals(1, data.nextShort());
		ClassRepTest.checkField(data, pool, 0x0001, "f1", "I");

		/// Methods
		assertEquals(1, data.nextShort());
		ClassRepTest.checkMethod(data, pool, 0x0009, "m1", "()V", 0);

		ClassRepTest.checkClassEnd(data, pool, "f1");
	}
}

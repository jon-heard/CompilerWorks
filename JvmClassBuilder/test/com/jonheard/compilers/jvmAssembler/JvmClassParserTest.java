package com.jonheard.compilers.jvmAssembler;

import org.junit.Test;
import com.jonheard.compilers.jvmClassBuilder.ClassRep;
import com.jonheard.compilers.jvmClassBuilder.ClassRepTest;
import com.jonheard.compilers.jvmClassBuilder.ConstantPool;
import com.jonheard.compilers.jvmClassBuilder.DataBuffer;
import static org.junit.Assert.*;

public class JvmClassParserTest
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

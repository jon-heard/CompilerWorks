package com.jonheard.compilers.assembler_jvm.frontEnd;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import com.jonheard.compilers.assembler_jvm.backEnd.ConstantPool;
import com.jonheard.compilers.assembler_jvm.backEnd.DataBuffer;
import com.jonheard.compilers.assembler_jvm.backEnd.MethodCodeBuilder;
import com.jonheard.compilers.assembler_jvm.backEnd.MethodRep;
import com.jonheard.compilers.assembler_jvm.frontEnd.MethodParser;

public class MethodParserTest
{
	ConstantPool constantPool;
	MethodRep rep;
	MethodCodeBuilder builder;
	MethodParser parser;
	
	public void setup()
	{
		constantPool = new ConstantPool();
		rep = new MethodRep(
				"n1", "()V", Arrays.asList("public"), constantPool);
		builder = rep.getMethodCodeBuilder();		
		parser = new MethodParser();
	}
	
	@Test
	public void labels()
	{
		setup();

		parser.parseSource(builder,
				"a: goto d\n" +  /// 3 bytes
				"b: nop\n" +     /// 1 byte
				"c: nop\n" +     /// 1 byte
				"d: return\n"    /// 1 bytes
		);
		
		DataBuffer data = rep.serialize();
		data.setIterator(10);
		assertEquals(18, data.nextInt());
		assertEquals(0, data.nextShort());
		assertEquals(1, data.nextShort());
		assertEquals(6, data.nextInt());
		
		assertEquals((byte)0xa7, data.nextByte());
		assertEquals((byte)0x05, data.nextShort());
		assertEquals((byte)0x00, data.nextByte());
		assertEquals((byte)0x00, data.nextByte());
		assertEquals((byte)0xb1, data.nextByte());
	}
	
	@Test
	public void opTypes()
	{
		setup();
		
		parser.parseSource(builder,
				"return\n" +                   /// NoArg - 1 byte
				"new c1\n" +                   /// Class - 3 bytes
				"bipush 25\n" +                /// Byte - 2 bytes
				"goto label1\n" +              /// Label - 3 bytes
				"invokevirtual c2 m1 ()V\n" +  /// Method - 3 bytes
				"putfield c3 f1 F\n" +         /// Field - 3 bytes
				"label1:\n" +                  /// Label - 0 bytes
				"ldc hello\n"        /// String - 3 bytes
		);

		DataBuffer data = rep.serialize();
		data.setIterator(10);
		assertEquals(29, data.nextInt());
		assertEquals(2, data.nextShort());
		assertEquals(1, data.nextShort());
		assertEquals(17, data.nextInt());

		/// NoArg
		assertEquals((byte)0xb1, data.nextByte());
		/// Class
		assertEquals((byte)0xbb, data.nextByte());
		int expected = constantPool.addClass("c1");
		assertEquals(expected, data.nextShort());
		/// Byte
		assertEquals((byte)0x10, data.nextByte());
		assertEquals((byte)25, data.nextByte());
		/// Label
		assertEquals((byte)0xa7, data.nextByte());
		assertEquals(9, data.nextShort());
		/// Method
		assertEquals((byte)0xb6, data.nextByte());
		expected = constantPool.addMethod("c2", "m1", "()V");
		assertEquals(expected, data.nextShort());
		/// Field
		assertEquals((byte)0xb5, data.nextByte());
		expected = constantPool.addField("c3", "f1", "F");
		assertEquals(expected, data.nextShort());
		/// String
		assertEquals((byte)0x12, data.nextByte());
		expected = constantPool.addString("hello");
		assertEquals(expected, data.nextByte());
	}
}
